/*
  MTNP: Manipulate Tables N'Plots
  Copyright (C) 2017-2018 Sylvain Hallé

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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ca.uqac.lif.petitpoucet.DirectValue;

/**
 * Sorts the rows of the input table.
 * @author Sylvain Hallé
 */
public class SortRows implements TableTransformation 
{

	@Override
	public TempTable transform(TempTable... tables)
	{
		TempTable in_table = tables[0];
		String[] col_names = in_table.getColumnNames();
		List<TableEntry> entries = in_table.getEntries();
		Collections.sort(entries, new RowComparator(col_names));
		TempTable out_table = new TempTable(in_table.getId(), col_names);
		for (TableEntry te : entries)
		{
			TableEntry new_te = new TableEntry(te);
			for (int j = 0; j < col_names.length; j++)
			{
				DirectValue dv = new DirectValue();
				dv.add(new TableCellNode(in_table, te.getRowIndex(), j));
				new_te.addDependency(col_names[j], dv);				
			}
			out_table.add(new_te);
		}
		return out_table;
	}

	/**
	 * Compares two rows of a table, so that they can be sorted
	 */
	public static class RowComparator implements Comparator<TableEntry>
	{
		String[] m_columnOrder;
		
		public RowComparator(String[] column_order)
		{
			super();
			m_columnOrder = column_order;
		}

		@Override
		public int compare(TableEntry te1, TableEntry te2)
		{
			for (String s : m_columnOrder)
			{
				if (!te1.containsKey(s))
				{
					if (te2.containsKey(s))
					{
						return 1;
					}
				}
				else
				{
					if (!te2.containsKey(s))
					{
						return -1;
					}
					PrimitiveValue v1 = te1.get(s);
					PrimitiveValue v2 = te2.get(s);
					if (v1.isNumeric() && v2.isNumeric())
					{
						double d1 = v1.numberValue().doubleValue();
						double d2 = v2.numberValue().doubleValue();
						if (d1 < d2)
						{
							return -1;
						}
						if (d1 == d2)
						{
							return 0;
						}
						return 1;
					}
					if (v1.isString() && v2.isString())
					{
						String s1 = v1.stringValue();
						String s2 = v2.stringValue();
						return s1.compareTo(s2);
					}
				}
			}
			return 0;
		}
	}
}
