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

/**
 * Table built by renaming the columns of another table
 * @author Sylvain Hallé
 */
public class RenameColumns implements TableTransformation
{
	/**
	 * The new names to be given to the original table's columns
	 */
	protected final String[] m_names;
	
	/**
	 * Creates a new table by renaming the columns of an existing table
	 * @param new_names The new names to be given to the original 
	 *    table's columns
	 */
	public RenameColumns(String ... new_names)
	{
		super();
		m_names = new_names;
	}

	@Override
	public TempTable transform(TempTable ... tables)
	{
		TempTable table = tables[0];
		String[] ordering = table.getColumnNames();
		TempTable tt = new TempTable(-4, m_names);
		for (TableEntry te : table.getEntries())
		{
			TableEntry new_te = new TableEntry();
			for (int i = 0; i < ordering.length; i++)
			{
				new_te.put(m_names[i], te.get(ordering[i]));
			}
			tt.add(new_te);
		}
		return tt;

	}
}
