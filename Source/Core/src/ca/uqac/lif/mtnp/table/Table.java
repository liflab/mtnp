/*
  MTNP: Manipulate Tables N'Plots
  Copyright (C) 2017 Sylvain Hallé

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package ca.uqac.lif.mtnp.table;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import ca.uqac.lif.petitpoucet.DataOwner;
import ca.uqac.lif.petitpoucet.NodeFunction;
/**
 * A multi-dimensional array of values. Tables can be passed to
 * {@link ca.uqac.lif.mtnp.plot.Plot Plot} objects to generate graphics.
 */
public abstract class Table implements DataOwner
{
	/**
	 * The table's ID
	 */
	protected int m_id;

	/**
	 * A counter for auto-incrementing table IDs
	 */
	private static int s_idCounter = 1;

	/**
	 * A lock for accessing the counter
	 */
	private static Lock s_counterLock = new ReentrantLock();

	/**
	 * A table nickname. This can be used as a short "code" that refers
	 * to the table (rather than using its ID).
	 */
	protected String m_nickname = "";

	/**
	 * The table's title
	 */
	protected String m_title;

	/**
	 * A textual description of the table's contents
	 */
	public String m_description = "";
	
	/**
	 * Whether this table should be shown in the list of tables.
	 * This value may be set to {@code false} for intermediate tables.
	 */
	protected boolean m_showInList = true;

	/**
	 * The types of values that a data cell can have
	 */
	public static enum Type {TEXT, NUMERIC};

	public Table()
	{
		super();
		s_counterLock.lock();
		m_id = s_idCounter++;
		s_counterLock.unlock();
		m_title = "Table " + m_id;
	}
	
	protected Table(int id)
	{
		super();
		m_id = id;
		m_title = "Table " + m_id;
	}

	/**
	 * Gets the table's nickname
	 * @return The nickname
	 */
	public String getNickname()
	{
		return m_nickname;
	}
	
	/**
	 * Resets the ID counter for tables
	 */
	public static void resetCounter()
	{
		s_counterLock.lock();
		s_idCounter = 1;
		s_counterLock.unlock();
	}

	/**
	 * Sets a nickname for this table. 
	 * This can be used as a short "code" that refers
	 * to the table (rather than using its ID).
	 * @param nickname The nickname
	 * @return This table
	 */
	public Table setNickname(String nickname)
	{
		if (nickname == null)
		{
			m_nickname = "";
		}
		else
		{
			m_nickname = nickname;
		}
		return this;
	}
	
	/**
	 * Sets whether the table is shown in the list of tables.
	 * @param b Set to {@code true} to show in the list of
	 * tables, {@code false} otherwise
	 * @return This table
	 */
	public Table setShowInList(boolean b)
	{
		m_showInList = b;
		return this;
	}
	
	/**
	 * Gets whether the table is shown in the list of tables.
	 * @return {@code true} if shown in the list of
	 * tables, {@code false} otherwise
	 */
	public boolean showsInList()
	{
		return m_showInList;
	}

	/**
	 * Gets the table's description
	 * @return The description
	 */
	public String getDescription()
	{
		return m_description;
	}

	/**
	 * Sets the table's description
	 * @param description The description. This string can contain any valid
	 * HTML. This string will not be escaped when displayed in the web 
	 * console.
	 * @return This table
	 */
	public Table setDescription(String description)
	{
		if (description == null)
		{
			m_description = "";
		}
		m_description = description;
		return this;
	}

	/**
	 * Sets a title for the table
	 * @param title The title
	 * @return This table
	 */
	public final Table setTitle(String title)
	{
		m_title = title;
		return this;
	}

	public final String getTitle()
	{
		return m_title;
	}

	/**
	 * Gets the table's unique ID
	 * @return The ID
	 */
	public final int getId()
	{
		return m_id;
	}
	
	/**
	 * Gets an instance of {@link HardTable} from the table's
	 * data, using the columns specified in the argument
	 * @param ordering The columns to use
	 * @return The table
	 */
	public final TempTable getDataTable(String ... ordering)
	{
		return getDataTable(false, ordering);
	}

	/**
	 * Gets an instance of {@link HardTable} from the table's
	 * data, using the columns specified in the argument
	 * @param link_to_experiments Set to true to have the table
	 *   entries link directly to the experiments, instead of
	 * @param ordering The columns to use
	 * @return The table
	 */
	protected abstract TempTable getDataTable(boolean link_to_experiments, String ... ordering);

	/**
	 * Gets an instance of {@link HardTable} from the table's
	 * data, using the default column ordering
	 * @return The table
	 */
	public final TempTable getDataTable()
	{
		return getDataTable(false);
	}
	
	/**
	 * Gets an instance of {@link HardTable} from the table's
	 * data, using the default column ordering
	 * @param temporary Set {@code true} to get a temporary data table
	 * @return The table
	 */
	public abstract TempTable getDataTable(boolean temporary);

	@Override
	public String toString()
	{
		return getDataTable().toString();
	}
	
	@Override
	public Table getOwner()
	{
		return this;
	}
	
	public NodeFunction dependsOn(int row, int col)
	{
		return new TableCellNode(this, row, col);
	}
	
	public abstract NodeFunction getDependency(int row, int col);
	
	/**
	 * Represents a cell in a table by its row-column
	 * coordinates.
	 *
	 */
	public static class CellCoordinate
	{
		public final int row;
		public final int col;
		
		public CellCoordinate(int row, int col)
		{
			super();
			this.row = row;
			this.col = col;
		}
		
		@Override
		public String toString()
		{
			return "(" + row + "," + col + ")";
		}
	}

	@Override
	public final NodeFunction dependsOn(String id)
	{
		return TableCellNode.dependsOn(this, id);
	}
	
	/**
	 * Determines if this table is temporary
	 * @return {@code true} if temporary, {@code false} otherwise
	 */
	public boolean isTemporary()
	{
		return false;
	}
	
	public String getName()
	{
		return m_title;
	}
	
}
