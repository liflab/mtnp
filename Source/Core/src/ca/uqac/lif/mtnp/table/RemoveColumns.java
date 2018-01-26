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

import java.util.ArrayList;
import java.util.List;

import ca.uqac.lif.petitpoucet.DirectValue;

/**
 * Removes columns from a table
 * @author Sylvain Hallé
 */
public class RemoveColumns implements TableTransformation 
{
	/**
	 * The names of the columns to remove
	 */
	protected final String[] m_namesToRemove;
	
	/**
	 * Creates a new instance of this table transformation
	 * @param names_to_remove The names of the columns to remove from the
	 * original table
	 */
	public RemoveColumns(String ... names_to_remove)
	{
		super();
		m_namesToRemove = names_to_remove;
	}
	
	/**
	 * Creates a new instance of this table transformation
	 * @param names_to_remove The names of the columns to remove from the
	 * original table
	 * @return The transformation
	 */
	public static RemoveColumns get(String ... names_to_remove)
	{
		return new RemoveColumns(names_to_remove);
	}

	@Override
	public TempTable transform(TempTable... tables)
	{
		TempTable table = tables[0];
		List<String> cols = new ArrayList<String>();
		String[] col_names = table.getColumnNames();
		for (String name : col_names)
		{
			cols.add(name.intern());
		}
		for (String name : m_namesToRemove)
		{
			cols.remove(name.intern());
		}
		String[] new_col_names = new String[cols.size()];
		int i = 0;
		for (String name : cols)
		{
			new_col_names[i] = name;
			i++;
		}
		int[] old_indices = new int[new_col_names.length];
		// Keep the correspondence between the column number in the original table
		int old_j = 0;
		for (int j = 0; j < new_col_names.length; j++)
		{
			while (col_names[old_j].compareTo(new_col_names[j]) != 0)
				old_j++;
			old_indices[j] = old_j;
		}
		List<TableEntry> old_entries = table.getEntries();
		List<TableEntry> new_entries = new ArrayList<TableEntry>(old_entries.size());
		for (TableEntry te : old_entries)
		{
			TableEntry new_te = new TableEntry();
			for (int j = 0; j < new_col_names.length; j++)
			{
				String k = new_col_names[j];
				new_te.put(k, te.get(k));
				DirectValue dv = new DirectValue();
				dv.add(new TableCellNode(table, te.getRowIndex(), old_indices[j]));
				new_te.addDependency(k, dv);
			}
			new_entries.add(new_te);
		}
		TempTable tt = new TempTable(table.getId(), new_entries, new_col_names);
		return tt;
	}
	
	
}
