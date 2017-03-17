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

import java.util.Map.Entry;

/**
 * Replaces the content of each entry by its fraction of the
 * sum of all values for the row
 */
public class NormalizeRows implements TableTransformation 
{
	public NormalizeRows()
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
		for (TableEntry te : in_table.getEntries())
		{
			float total = 0;
			for (Object o : te.values())
			{
				if (o instanceof Number)
				{
					total += ((Number) o).floatValue();
				}
			}
			TableEntry new_entry = new TableEntry();
			for (Entry<String,PrimitiveValue> map_entry : te.entrySet())
			{
				String key = map_entry.getKey();
				PrimitiveValue o = map_entry.getValue();
				if (o.isNumeric())
				{
					new_entry.put(key, o.numberValue().floatValue() / total);
				}
				else
				{
					new_entry.put(key, o);
				}
			}
			out_table.add(new_entry);
		}
		return out_table;
	}
}
