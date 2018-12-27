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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

import ca.uqac.lif.mtnp.DataFormatter;
import ca.uqac.lif.mtnp.table.rendering.CsvTableRenderer;
import ca.uqac.lif.petitpoucet.NodeFunction;

/**
 * A table made of concrete values
 * @author Sylvain Hallé
 */
public class HardTable extends Table
{
	/**
	 * OS-dependent carriage return
	 */
	protected static final String CRLF = System.getProperty("line.separator");
	
	/**
	 * The set of entries in the table. Note that we use a list,
	 * and not a set, as we need the entries to be enumerated in the
	 * same order every time. Otherwise, the <i>n</i>-th "row" of the
	 * table would not always refer to the same data point.
	 */
	protected List<TableEntry> m_entries;
	
	/**
	 * The preferred ordering to display the entries as a tree
	 */
	protected String[] m_preferredOrdering;
	
	/**
	 * The symbol used to separate data values in a CSV rendition
	 */
	public static final transient String s_datafileSeparator = ",";
	
	/**
	 * The symbol used to represent missing values in a CSV rendition
	 */
	public static final transient String s_datafileMissing = "?";

	/**
	 * Creates a new data table
	 * @param ordering The ordering of the columns in this table. This array
	 * should contain column names
	 */
	public HardTable(String ... ordering)
	{
		this(null, ordering);	
	}
	
	/**
	 * Creates a new data table
	 * @param ordering The ordering of the columns in this table. This array
	 * should contain column names
	 */
	HardTable(int id, String ... ordering)
	{
		this(id, null, ordering);	
	}
	
	/**
	 * Creates a new data table and fills it with existing data
	 * @param entries
	 * @param ordering
	 */
	HardTable(Collection<TableEntry> entries, String ... ordering)
	{
		super();
		m_entries = new ArrayList<TableEntry>();
		if (entries != null)
		{
			m_entries.addAll(entries);
		}
		m_preferredOrdering = ordering;
	}
	
	/**
	 * Creates a new data table and fills it with existing data
	 * @param entries
	 * @param ordering
	 */
	HardTable(int id, Collection<TableEntry> entries, String ... ordering)
	{
		super(id);
		m_entries = new ArrayList<TableEntry>();
		if (entries != null)
		{
			m_entries.addAll(entries);
		}
		m_preferredOrdering = ordering;
	}

	/**
	 * Adds a collection of entries to this data table
	 * @param entries The entries
	 */
	public void addAll(Collection<TableEntry> entries)
	{
		for (TableEntry te : entries)
		{
			add(te);
		}
	}
	
	/**
	 * Adds a new entry to the table
	 * @param e The entry
	 */
	public void add(TableEntry e)
	{
		e.m_rowIndex = m_entries.size();
		m_entries.add(e);
	}
	
	/**
	 * Gets the contents of this table as a tree
	 * @return A reference to the root node of the tree
	 */
	public TableNode getTree()
	{
		if (m_preferredOrdering != null)
		{
			return getTree(m_preferredOrdering);
		}
		String[] order = null;
		for (TableEntry e : m_entries)
		{
			order = new String[e.keySet().size()];
			int i = 0;
			for (String k : e.keySet())
			{
				order[i] = k;
				i++;
			}
			break;
		}
		return getTree(order);
	}
	
	/**
	 * Gets the table's data as a tree, with each node in the form
	 * key&nbsp;=&nbsp;value
	 * @param sort_order The order in which the dimensions of the table
	 *   should be enumerated
	 * @return A tree
	 */
	public TableNode getTree(String[] sort_order)
	{
		List<TableNode> nodes = getChildren(sort_order, 0, m_entries);
		TableNode root = new TableNode("", PrimitiveValue.getInstance(""));
		root.m_children = nodes;
		return root;
	}
	
	protected List<TableNode> getChildren(String[] sort_order, int index, Collection<TableEntry> available_entries)
	{
		List<TableNode> children = new LinkedList<TableNode>();
		if (index >= sort_order.length)
		{
			return children;
		}
		String current_key = sort_order[index];
		Map<PrimitiveValue,Set<TableEntry>> partition = partitionEntries(available_entries, current_key);
		List<PrimitiveValue> keys = new LinkedList<PrimitiveValue>();
		keys.addAll(partition.keySet());
		Collections.sort(keys);
		for (PrimitiveValue value : keys)
		{
			TableNode new_node = new TableNode(current_key, value);
			Set<TableEntry> entries = partition.get(value);
			for (TableEntry te : entries)
			{
				new_node.addCoordinate(te.getRowIndex(), index);
			}
			List<TableNode> new_node_children = getChildren(sort_order, index + 1, entries);
			new_node.m_children.addAll(new_node_children);
			children.add(new_node);
		}
		return children;
	}
	
	/**
	 * Partitions a set of entries into sets, with all entries having the
	 * same value with respect to a key being put into the same set
	 * @param available_entries The set of entries to partition
	 * @param key The key against which to partition the set
	 * @return A map from values to sets of entries
	 */
	protected static Map<PrimitiveValue,Set<TableEntry>> partitionEntries(Collection<TableEntry> available_entries, String key)
	{
		Map<PrimitiveValue,Set<TableEntry>> partition = new HashMap<PrimitiveValue,Set<TableEntry>>();
		for (TableEntry e : available_entries)
		{
			PrimitiveValue o = e.get(key);
			Set<TableEntry> value_set;
			if (partition.containsKey(o))
			{
				value_set = partition.get(o);
			}
			else
			{
				value_set = new HashSet<TableEntry>();
			}
			value_set.add(e);
			partition.put(o, value_set);
		}
		return partition;
	}
	
	/**
	 * Produces a flat HTML rendition of the table
	 * @return A string containing the HTML code for the table
	 */
	public String toHtml()
	{
		if (m_preferredOrdering != null)
		{
			return toHtml(m_preferredOrdering);
		}
		String[] order = null;
		for (TableEntry e : m_entries)
		{
			order = new String[e.keySet().size()];
			int i = 0;
			for (String k : e.keySet())
			{
				order[i] = k;
				i++;
			}
			break;
		}
		return toHtml(order);
	}
	
	/**
	 * Produces a flat HTML rendition of the table, by ordering the
	 * columns in a specific way
	 * @param sort_order An array of column names specifying the order
	 *  in which they should be shown
	 * @return A string containing the HTML code for the table
	 */
	protected String toHtml(String[] sort_order)
	{
		TableNode node = getTree(sort_order);
		StringBuilder out = new StringBuilder();
		out.append("<table border=\"1\">").append(CRLF).append("<thead>").append(CRLF);
		for (String key : sort_order)
		{
			out.append("<th>").append(key).append("</th>");
		}
		out.append("</thead>").append(CRLF).append("<tbody>").append(CRLF).append("<tr>").append(CRLF);
		toHtml(node, out, 0, sort_order.length);
		out.append("</tr>").append(CRLF).append("</tbody>").append(CRLF).append("</table>");
		return out.toString();
	}
	
	/**
	 * Produces a flat HTML rendition of the table
	 */
	protected void toHtml(TableNode cur_node, StringBuilder out, int depth, int total_depth)
	{

		if (depth > 0)
		{
			out.append("<td>");
			
			if (cur_node == null)
			{

				out.append("");
			}
			else if (cur_node.m_value == null)
			{
				
				out.append("");
			}
			else if (cur_node.m_value.isString())
			{
				out.append(cur_node.m_value);
			}
		
			else
			{
				out.append(cur_node.m_value);
			}
			out.append("</td>");
		}
		boolean first_child = true;
		for (TableNode child : cur_node.m_children)
		{
			if (first_child)
			{
				first_child = false;
			}
			else
			{
				out.append("</tr>").append(DataFormatter.CRLF).append("<tr>");
				for (int i = 0; i < depth; i++)
				{
					out.append("<td>-</td>");
				}
			}
			toHtml(child, out, depth + 1, total_depth);
		}
	}	
	
	/**
	 * Produces a flat CSV rendition of the table, by ordering the
	 * columns in a specific way
	 * @param sort_order An array of column names specifying the order
	 *  in which they should be shown
	 * @param separator The symbol used as the separator for values
	 * @param missing The symbol used for missing data
	 * @return A string containing the CSV code for the table
	 */
	protected String toCsv(String[] sort_order, String separator, String missing)
	{
		TableNode node = getTree(sort_order);
		CsvTableRenderer ctr = new CsvTableRenderer(this, separator, missing);
		return ctr.render(node, sort_order);
	}

	public PrimitiveValue get(int col, int row)
	{
		if (row < 0 || row >= m_entries.size() || col < 0 || col >= m_preferredOrdering.length)
		{
			// Out of bounds
			return null;
		}
		TableEntry e = m_entries.get(row);
		String key = m_preferredOrdering[col];
		if (!e.containsKey(key))
		{
			// This entry does not contain the key we are looking for
			return null;
		}
		PrimitiveValue o = e.get(key);
		return o;
	}

	public int getColumnCount()
	{
		return m_preferredOrdering.length;
	}

	public Class<? extends Comparable<?>>[] getColumnTypes()
	{
		@SuppressWarnings("unchecked")
		Class<? extends Comparable<?>>[] types = new Class[m_preferredOrdering.length];
		for (int i = 0; i < m_preferredOrdering.length; i++)
		{
			String key = m_preferredOrdering[i];
			types[i] = getColumnTypeFor(key);
		}
		return types;
		//return m_columnTypes;
	}

	public int getRowCount()
	{
		return m_entries.size();
	}

	/**
	 * Gets the type of the column of given name
	 * @param col_name The name of the column
	 * @return The type, or {@code null} if the column does not exist
	 */
	public Class<? extends Comparable<?>> getColumnTypeFor(String col_name)
	{
		for (TableEntry e : m_entries)
		{
			if (!e.containsKey(col_name))
			{
				continue;
			}
			PrimitiveValue elem = e.get(col_name);
			if (elem == null)
			{
				continue;
			}
			if (elem.isNumeric())
			{
				return Float.class;
			}
		}
		return String.class;
		//return m_columnTypes[pos];
	}

	/*@Override
	public DataTable getDataTable(boolean link_to_experiments, String ... ordering)
	{
		return new DataTable(m_entries, ordering);
	}*/

	/**
	 * Gets the name of the column at a given position in the table
	 * @param col The position
	 * @return The column's name, or null if the index is out of bounds
	 */
	public String getColumnName(int col)
	{
		if (col < 0 || col >= m_preferredOrdering.length)
		{
			return null;
		}
		return m_preferredOrdering[col];
	}

	/**
	 * Gets the position of the column of a given name in the table
	 * @param name The name
	 * @return The column's position, or -1 if the name was not found
	 */
	public int getColumnPosition(String name)
	{
		for (int i = 0; i < m_preferredOrdering.length; i++)
		{
			if (m_preferredOrdering[i].compareTo(name) == 0)
			{
				return i;
			}
		}
		return -1;
	}

	/**
	 * Gets the names of all the columns in the table
	 * @return An array of names
	 */
	public String[] getColumnNames()
	{
		return m_preferredOrdering;
	}

	/**
	 * Finds an entry with the same key-value pairs as the entry given
	 * as an argument
	 * @param e The entry
	 * @return The entry found, or {@code null} if none found
	 */
	public TableEntry findEntry(TableEntry e)
	{
		for (TableEntry tab_e : m_entries)
		{
			boolean same = true;
			for (Entry<String,PrimitiveValue> kv : e.entrySet())
			{
				String key = kv.getKey();
				if (!tab_e.containsKey(key) || !(tab_e.get(key).equals(e.get(key))))
				{
					same = false;
					break;
				}
			}
			if (same)
			{
				return tab_e;
			}
		}
		return null;
	}

	/**
	 * Gets the list of entries if this table
	 * @return The entries
	 */
	public List<TableEntry> getEntries()
	{
		return m_entries;
	}
	
	/**
	 * Returns the contents of the table as a CSV string
	 * @return The CSV contents
	 */
	public String toCsv()
	{
		return toCsv(m_preferredOrdering, s_datafileSeparator, s_datafileMissing);
	}

	/**
	 * Returns the contents of the table as a CSV string
	 * @param separator The symbol used as the separator for values
	 * @param missing The symbol used for missing data
	 * @return The CSV contents
	 */
	public String toCsv(String separator, String missing)
	{
		return toCsv(m_preferredOrdering, separator, missing);
	}
	
	@Override
	public String toString()
	{
		StringBuilder out = new StringBuilder();
		for (int i = 0; i < m_preferredOrdering.length; i++)
		{
			if (i > 0)
			{
				out.append(s_datafileSeparator);
			}
			out.append(m_preferredOrdering[i]);
		}
		out.append(DataFormatter.CRLF).append("---").append(DataFormatter.CRLF);
		out.append(toCsv());
		return out.toString();
	}
	
	public final boolean isColumnNumeric(int columnIndex)
	{
		Class<?> c = getColumnTypeFor(columnIndex);
		return c.isAssignableFrom(Float.class);
	}
	
	/**
	 * Gets the type of the column of given name
	 * @param position The position of the column, starting at 0 for the
	 *   first column
	 * @return The type, or {@code null} if the column does not exist
	 */
	public final Class<? extends Comparable<?>> getColumnTypeFor(int position)
	{
		String name = getColumnName(position);
		return getColumnTypeFor(name);
	}

	@Override
	public NodeFunction getDependency(int row, int col)
	{
		if (row >= m_entries.size())
		{
			return null;
		}
		TableEntry entry = m_entries.get(row);
		if (col >= m_preferredOrdering.length)
		{
			return null;
		}
		String key = m_preferredOrdering[col];
		if (!entry.containsKey(key))
		{
			return null;
		}
		return entry.getDependency(key);
	}

	@Override
	public TempTable getDataTable(boolean temporary)
	{
		return new TempTable(getId(), m_entries, m_preferredOrdering);
	}

	@Override
	public TempTable getDataTable(boolean link_to_experiments, String ... ordering) 
	{
		return new TempTable(getId(), m_entries, ordering);
	}
	
	/**
	 * Populates a table from a CSV file
	 * @param scanner A scanner to an open CSV text file
	 * @return A data table
	 */
	public static HardTable read(Scanner scanner, String separator)
	{
		HardTable dt = null;
		boolean first_line = true;
		String[] col_names = null;
		while (scanner.hasNextLine())
		{
			String line = scanner.nextLine().trim();
			if (line.isEmpty() || line.startsWith("#"))
				continue;
			String[] parts = line.split(separator);
			if (first_line)
			{
				col_names = new String[parts.length];
				for (int i = 0; i < parts.length; i++)
				{
					col_names[i] = parts[i].trim();
				}
				dt = new HardTable(col_names);
				first_line = false;
			}
			else
			{
				assert col_names != null;
				assert dt != null;
				TableEntry entry = new TableEntry();
				for (int i = 0; i < Math.min(col_names.length, parts.length); i++)
				{
					entry.put(col_names[i], PrimitiveValue.getInstance(parts[i]));
				}
				dt.add(entry);
			}
		}
		return dt;
	}
}
