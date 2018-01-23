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

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class ExpandAsColumnsTest 
{
	@Test
	public void test1()
	{
		HardTable ht = new HardTable("A", "Y", "Z");
		{
			TableEntry te = new TableEntry("A", 2);
			te.put("Y", "B");
			te.put("Z", 5);
			ht.add(te);
		}
		{
			TableEntry te = new TableEntry("A", 2);
			te.put("Y", "C");
			te.put("Z", 1);
			ht.add(te);
		}
		TransformedTable tt = new TransformedTable(new ExpandAsColumns("Y", "Z"), ht);
		TempTable ht_out = tt.getDataTable();
		List<TableEntry> entries = ht_out.getEntries();
		assertEquals(1, entries.size());
		System.out.println(ht_out.toString());
	}
	
	@Test
	public void test2()
	{
		HardTable ht = new HardTable("A", "X", "Y", "Z");
		{
			TableEntry te = new TableEntry("A", 2);
			te.put("X", 0);
			te.put("Y", "B");
			te.put("Z", 5);
			ht.add(te);
		}
		{
			TableEntry te = new TableEntry("A", 2);
			te.put("X", 0);
			te.put("Y", "C");
			te.put("Z", 1);
			ht.add(te);
		}
		TransformedTable tt = new TransformedTable(new ExpandAsColumns("Y", "Z"), ht);
		TempTable ht_out = tt.getDataTable();
		List<TableEntry> entries = ht_out.getEntries();
		assertEquals(1, entries.size());
		System.out.println(ht_out.toString());
	}
}
