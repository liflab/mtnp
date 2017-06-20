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

import java.util.regex.Pattern;

import ca.uqac.lif.petitpoucet.NodeFunction;
import ca.uqac.lif.petitpoucet.OwnershipManager;

public class TableCellNode implements NodeFunction 
{
	protected final Table m_table;
	
	protected final int m_row;
	
	protected final int m_col;
	
	public TableCellNode(Table t, int row, int col)
	{
		super();
		m_table = t;
		m_row = row;
		m_col = col;
	}
	
	/**
	 * Gets the identifier of a table
	 * @param t The table
	 * @return The identifier
	 */
	public static String getDatapointId(Table t)
	{
		return "T" + t.getId();
	}
	
	@Override
	public String toString()
	{
		return "Cell (" + m_row + "," + m_col + ") in Table #" + m_table.getId();
	}
	
	@Override
	public String getDataPointId()
	{
		return "T" + m_table.getId() + s_separator + m_row + s_separator + m_col;
	}
	
	@Override
	public NodeFunction dependsOn()
	{
		return m_table.getDependency(m_row, m_col);
	}
	
	public static NodeFunction dependsOn(Table t, String datapoint_id)
	{
		// Parse the datapoint ID and call the table on the extracted values
		String[] parts = datapoint_id.split(Pattern.quote(NodeFunction.s_separator));
		if (parts.length != 3)
		{
			// Invalid datapoint
			return null;
		}
		int id = Integer.parseInt(parts[0].substring(1).trim());
		if (id != t.getId())
		{
			// Wrong table
			return null;
		}
		int row = Integer.parseInt(parts[1].trim());
		int col = Integer.parseInt(parts[2].trim());
		return t.dependsOn(row, col);
	}
	
	/**
	 * Gets the owner of a datapoint
	 * @param tracker The manager responsible for ownership relationships
	 * @param datapoint_id The ID of the datapoint to fetch
	 * @return The owner, or {@code null} if this object could not
	 * find the owner
	 */
	public static Table getOwner(OwnershipManager tracker, String datapoint_id)
	{
		if (!datapoint_id.startsWith("T"))
			return null;
		String[] parts = datapoint_id.split(Pattern.quote(NodeFunction.s_separator));
		return (Table) tracker.getObjectWithId(parts[0]);
	}
	
	public Table getOwner()
	{
		return m_table;
	}
	
	public int getRow()
	{
		return m_row;
	}
	
	public int getCol()
	{
		return m_col;
	}
	
	@Override
	public int hashCode()
	{
		return m_table.getId() + m_row + m_col;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o == null || !(o instanceof TableCellNode))
		{
			return false;
		}
		TableCellNode tcn = (TableCellNode) o;
		return tcn.m_table.getId() == m_table.getId() &&
				tcn.m_col == m_col && tcn.m_row == m_row;
	}
}
