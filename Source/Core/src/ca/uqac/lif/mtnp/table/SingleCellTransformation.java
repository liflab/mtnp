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

import java.util.Map;

/**
 * Transformation that applies an operation on each individual cell
 * (i.e. key-value pair) in the table independently of the other cells.
 * 
 * @author Sylvain Hallé
 */
public abstract class SingleCellTransformation implements TableTransformation 
{
	@Override
	public final TempTable transform(TempTable ... tables) 
	{
		TempTable tt = tables[0];
		TempTable new_tt = new TempTable(tt.getId(), tt.getColumnNames());
		for (TableEntry te : tt.getEntries())
		{
			TableEntry new_te = new TableEntry();
			for (Map.Entry<String,PrimitiveValue> entry :  te.entrySet())
			{
				String key = entry.getKey();
				PrimitiveValue new_value = applyTransformation(key, entry.getValue());
				new_te.put(key, new_value);
			}
			new_tt.add(new_te);
		}
		return new_tt;
	}
	
	/**
	 * Applies a transformation to a cell of a table
	 * @param key The key corresponding to that cell (i.e. column header)
	 * @param value The value of this cell in the original table
	 * @return The value to replace it with in the output table
	 */
	public abstract PrimitiveValue applyTransformation(String key, PrimitiveValue value);
}
