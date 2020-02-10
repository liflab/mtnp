package ca.uqac.lif.mtnp.table;

import static org.junit.Assert.*;

import org.junit.Test;

import ca.uqac.lif.mtnp.DataFormatter;

public class TableTest
{
	@Test
	public void testCsv1()
	{
		// Test rendering of table in CSV when table is not sorted
		HardTable ht = new HardTable("A", "B", "C");
		{
			TableEntry te = new TableEntry("A", 2);
			te.put("B", 3);
			te.put("C", 6);
			ht.add(te);
		}
		{
			TableEntry te = new TableEntry("A", 1);
			te.put("B", 3);
			te.put("C", 4);
			ht.add(te);
		}
		String csv_string = ht.toCsv();
		String[] lines = csv_string.split(DataFormatter.CRLF);
		assertEquals(3, lines.length);
		assertEquals("A,B,C", lines[0]);
		assertEquals("1,3,4", lines[1]);
		assertEquals("2,3,6", lines[2]);
	}
	
	@Test
	public void testCsv2()
	{
		// Test rendering of table in CSV when table is not sorted
		HardTable ht = new HardTable("A", "B", "C");
		{
			TableEntry te = new TableEntry("A", "foo");
			te.put("B", 0.0);
			te.put("C", 6);
			ht.add(te);
		}
		{
			TableEntry te = new TableEntry("A", "foo");
			te.put("B", 0.1);
			te.put("C", 4);
			ht.add(te);
		}
		{
			TableEntry te = new TableEntry("A", "foo");
			te.put("B", 0.2);
			te.put("C", 4);
			ht.add(te);
		}
		String csv_string = ht.toCsv();
		String[] lines = csv_string.split(DataFormatter.CRLF);
		assertEquals(4, lines.length);
		assertEquals("A,B,C", lines[0]);
		assertEquals("foo,0.0,6", lines[1]);
		assertEquals("foo,0.1,4", lines[2]);
		assertEquals("foo,0.2,4", lines[3]);
	}
}
