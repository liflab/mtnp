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

import ca.uqac.lif.petitpoucet.NodeFunction;

/**
 * Table obtained from applying a transformation to other tables
 * @author Sylvain Hallé
 */
public class TransformedTable extends Table 
{
	/**
	 * The tables used as the input for the transformation
	 */
	protected final Table[] m_inputTables;
	
	/**
	 * The table transformation to apply
	 */
	protected final TableTransformation m_transformation;
	
	public TransformedTable(TableTransformation trans, Table ... tables)
	{
		super();
		m_transformation = trans;
		m_inputTables = tables;
	}
	
	@Override
	protected TempTable getDataTable(boolean link_to_experiments, String... ordering) 
	{
		TempTable[] concrete_tables = new TempTable[m_inputTables.length];
		for (int i = 0; i < m_inputTables.length; i++)
		{
			concrete_tables[i] = m_inputTables[i].getDataTable(link_to_experiments);
			concrete_tables[i].setId(m_inputTables[i].getId());
		}
		TempTable out = m_transformation.transform(concrete_tables);
		out.setId(getId());
		return out;
	}
	
	/**
	 * Gets the tables used as the input for the transformation to
	 * be applied
	 * @return An array of tables
	 */
	public Table[] getInputTables()
	{
		return m_inputTables;
	}

	@Override
	public TempTable getDataTable(boolean temporary) 
	{
		TempTable[] concrete_tables = new TempTable[m_inputTables.length];
		for (int i = 0; i < m_inputTables.length; i++)
		{
			concrete_tables[i] = m_inputTables[i].getDataTable(temporary);
			concrete_tables[i].setId(m_inputTables[i].getId());
		}
		TempTable out = m_transformation.transform(concrete_tables);
		out.setId(getId());
		return out;
	}
	
	@Override
	public NodeFunction getDependency(int row, int col)
	{
		HardTable dt = getDataTable();
		return dt.dependsOn(row, col);
	}

	@Override
	public void clear()
	{
		super.clear();
		for (Table t : m_inputTables)
		{
			t.clear();
		}
	}
}
