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

import ca.uqac.lif.petitpoucet.DirectValue;
import ca.uqac.lif.petitpoucet.NodeFunction;

public class SortRowsTest 
{
	@Test
	public void test1()
	{
		TableEntry te;
		HardTable ht = new HardTable("A");
		{
			te = new TableEntry("A", 2);
			ht.add(te);
		}
		{
			te = new TableEntry("A", 1);
			ht.add(te);
		}
		{
			te = new TableEntry("A", 3);
			ht.add(te);
		}
		// Check that entries appear in the input input table in the order they
		// were entered
		assertEquals(2, ((PrimitiveValue) ht.get(0, 0)).numberValue().intValue());
		assertEquals(1, ((PrimitiveValue) ht.get(0, 1)).numberValue().intValue());
		assertEquals(3, ((PrimitiveValue) ht.get(0, 2)).numberValue().intValue());
		// Created sorted table
		TransformedTable tt = new TransformedTable(new SortRows(), ht);
		TempTable ht_out = tt.getDataTable();
		// Now check that output table is sorted
		assertEquals(1, ((PrimitiveValue) ht_out.get(0, 0)).numberValue().intValue());
		assertEquals(2, ((PrimitiveValue) ht_out.get(0, 1)).numberValue().intValue());
		assertEquals(3, ((PrimitiveValue) ht_out.get(0, 2)).numberValue().intValue());
		// Check that the relationship to the rows of the input table is preserved
		for (TableEntry entry : ht_out.getEntries())
		{
			int val = ((PrimitiveValue) entry.get("A")).numberValue().intValue();
			if (val == 1)
			{
				assertEquals(0, entry.getRowIndex());
				NodeFunction nf = entry.getDependency("A");
				assertTrue(nf instanceof DirectValue);
				DirectValue dv = (DirectValue) nf;
				List<NodeFunction> nodes = dv.getDependencyNodes();
				assertEquals(1, nodes.size());
				TableCellNode nf2 = (TableCellNode) nodes.get(0);
				assertEquals(0, nf2.getCol());
				assertEquals(1, nf2.getRow());
				assertEquals(ht.getId(), nf2.getOwner().getId());
				
			}
			if (val == 2)
			{
				assertEquals(1, entry.getRowIndex());
				NodeFunction nf = entry.getDependency("A");
				assertTrue(nf instanceof DirectValue);
				DirectValue dv = (DirectValue) nf;
				List<NodeFunction> nodes = dv.getDependencyNodes();
				assertEquals(1, nodes.size());
				TableCellNode nf2 = (TableCellNode) nodes.get(0);
				assertEquals(0, nf2.getCol());
				assertEquals(0, nf2.getRow());
				assertEquals(ht.getId(), nf2.getOwner().getId());
			}
			if (val == 3)
			{
				assertEquals(2, entry.getRowIndex());
				NodeFunction nf = entry.getDependency("A");
				assertTrue(nf instanceof DirectValue);
				DirectValue dv = (DirectValue) nf;
				List<NodeFunction> nodes = dv.getDependencyNodes();
				assertEquals(1, nodes.size());
				TableCellNode nf2 = (TableCellNode) nodes.get(0);
				assertEquals(0, nf2.getCol());
				assertEquals(2, nf2.getRow());
				assertEquals(ht.getId(), nf2.getOwner().getId());
			}
		}
	}
}