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
 * Replaces the content of each entry by its fraction of the
 * sum of all values for the column
 */
public class NormalizeColumns implements TableTransformation
{
	public NormalizeColumns()
	{
		super();
	}

	@Override
	public TempTable transform(TempTable ... tables)
	{
		TempTable in_table = tables[0];
		TempTable out_table = new TempTable(in_table.getId());
		if (in_table.getRowCount() == 0)
		{
			return out_table;
		}
		for (String col_name : in_table.getColumnNames())
		{
			float total = 0;
			for (TableEntry te : in_table.getEntries())
			{
				PrimitiveValue o = te.get(col_name);
				if (o.isNumeric())
				{
					total += o.numberValue().floatValue();
				}
			}
			for (TableEntry te : in_table.getEntries())
			{
				TableEntry new_te = new TableEntry(te);
				PrimitiveValue o = te.get(col_name);
				if (o.isNumeric())
				{
					new_te.put(col_name, o.numberValue().floatValue() / total);
				}
				out_table.add(new_te);
			}
		}
		return out_table;
	}
}
