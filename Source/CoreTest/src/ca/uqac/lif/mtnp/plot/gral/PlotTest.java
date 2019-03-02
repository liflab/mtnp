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
package ca.uqac.lif.mtnp.plot.gral;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

import ca.uqac.lif.mtnp.plot.Plot.ImageType;
import ca.uqac.lif.mtnp.table.HardTable;
import ca.uqac.lif.mtnp.table.TableEntry;

/**
 * Tests for the generation of plots from dummy data. These tests
 * are mostly there to check that the whole process does not throw
 * exceptions (i.e. we don't actually check what the plot looks like). 
 * @author Sylvain Hallé
 */
public class PlotTest
{
	@Test(timeout = 5000)
	public void testScatterplot1()
	{
		HardTable ht = new HardTable("a", "b", "c");
		ht.add(createEntry(0, 0, 0));
		ht.add(createEntry(1, 2, 3));
		ht.add(createEntry(2, 4, 6));
		Scatterplot sp = new Scatterplot(ht);
		byte[] img_contents = sp.getImage(ImageType.PNG);
		assertNotNull(img_contents);
	}
	
	@Test(timeout = 5000)
	@Ignore
	public void testScatterplot2()
	{
		// This test times out due to this bug in GRAL
		// https://github.com/eseifert/gral/issues/173
		HardTable ht = new HardTable("a", "b", "c");
		ht.add(createEntry(0, 0, 0));
		ht.add(createEntry(1, 2, 3));
		ht.add(createEntry(2, 4, 6));
		Scatterplot sp = new Scatterplot(ht);
		byte[] img_contents = sp.getImage(ImageType.PDF);
		assertNotNull(img_contents);
	}
	
	@Test(timeout = 5000)
	public void testPieChart1()
	{
		HardTable ht = new HardTable("a", "b", "c");
		ht.add(createEntry(0, 0, 0));
		ht.add(createEntry(1, 2, 3));
		ht.add(createEntry(2, 4, 6));
		PieChart sp = new PieChart(ht);
		byte[] img_contents = sp.getImage(ImageType.PNG);
		assertNotNull(img_contents);
	}
	
	@Test(timeout = 5000)
	@Ignore
	public void testPieChart2()
	{
		// This test times out due to this bug in GRAL
		// https://github.com/eseifert/gral/issues/173
		HardTable ht = new HardTable("a", "b", "c");
		ht.add(createEntry(0, 0, 0));
		ht.add(createEntry(1, 2, 3));
		ht.add(createEntry(2, 4, 6));
		PieChart sp = new PieChart(ht);
		byte[] img_contents = sp.getImage(ImageType.PDF);
		assertNotNull(img_contents);
	}
	
	protected static TableEntry createEntry(float a, float b, float c)
	{
		TableEntry te = new TableEntry();
		te.put("a", a);
		te.put("b", b);
		te.put("c", c);
		return te;
	}
}
