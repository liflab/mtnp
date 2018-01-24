package ca.uqac.lif.mtnp.table.rendering;

import java.util.List;

import ca.uqac.lif.mtnp.table.PrimitiveValue;
import ca.uqac.lif.mtnp.table.Table;
import ca.uqac.lif.mtnp.table.TableNode;

public class CsvTableRenderer extends TableNodeRenderer
{
	/**
	 * OS-dependent carriage return
	 */
	protected static final String CRLF = System.getProperty("line.separator");
	
	protected int m_row = 0;

	protected int m_col = 0;
	
	protected String m_datafileSeparator = ",";
	
	protected String m_datafileMissing = "null";
	
	public CsvTableRenderer(Table t, String datafile_separator, String datafile_missing)
	{
		super(t);
		m_datafileSeparator = datafile_separator;
		m_datafileMissing = datafile_missing;
	}

	@Override
	public void startStructure(StringBuilder out)
	{
		// Nothing to do
	}

	@Override
	public void startKeys(StringBuilder out)
	{
		// Nothing to do
	}

	@Override
	public void printKey(StringBuilder out, String key)
	{
		if (m_col > 0)
		{
			out.append(m_datafileSeparator);
		}
		out.append(key);
		m_col++;
	}

	@Override
	public void endKeys(StringBuilder out)
	{
		out.append(CRLF);
		m_col = 0;
	}

	@Override
	public void startBody(StringBuilder out)
	{
		// Nothing to do
	}

	@Override
	public void startRow(StringBuilder out, int max_depth)
	{
		// Nothing to do
	}

	@Override
	public void printCell(StringBuilder out, List<PrimitiveValue> values, int nb_children, int max_depth,
			TableNode node) 
	{
		if (m_col > 0)
		{
			out.append(m_datafileSeparator);
		}
		PrimitiveValue last = node.getValue();
		if (last == null)
		{
			out.append(m_datafileMissing);
		}
		else if (last.isString())
		{
			out.append(last.toString());
		}
		else
		{
			out.append(last);
		}
		m_col++;
	}

	@Override
	public void printRepeatedCell(StringBuilder out, List<PrimitiveValue> values, int index, int max_depth) 
	{
		if (m_col > 0)
		{
			out.append(m_datafileSeparator);
		}
		PrimitiveValue last = values.get(index);
		if (last == null)
		{
			out.append(m_datafileMissing);
		}
		else if (last.isString())
		{
			out.append(last.toString());
		}
		else
		{
			out.append(last);
		}
		m_col++;
	}

	@Override
	public void endRow(StringBuilder out, int max_depth)
	{
		m_row++;
		m_col = 0;
		out.append(CRLF);
	}

	@Override
	public void endBody(StringBuilder out)
	{
		// Nothing to do
	}

	@Override
	public void endStructure(StringBuilder out)
	{
		// Nothing to do
	}

	@Override
	public void reset()
	{
		super.reset();
		m_row = 0;
		m_col = 0;
	}
}
