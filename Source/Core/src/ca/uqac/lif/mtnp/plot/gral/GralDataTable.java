/*
  MTNP: Manipulate Tables N'Plots
  Copyright (C) 2017-2020 Sylvain Hallé

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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import ca.uqac.lif.mtnp.table.HardTable;
import ca.uqac.lif.mtnp.table.PrimitiveValue;
import ca.uqac.lif.mtnp.table.Table;
import ca.uqac.lif.mtnp.table.TableEntry;
import ca.uqac.lif.mtnp.table.TempTable;
import ca.uqac.lif.petitpoucet.NodeFunction;
import de.erichseifert.gral.data.Column;
import de.erichseifert.gral.data.DataListener;
import de.erichseifert.gral.data.DataSeries;
import de.erichseifert.gral.data.DataSource;
import de.erichseifert.gral.data.Row;
import de.erichseifert.gral.data.statistics.Statistics;

/**
 * A LabPal {@link Table} object that implements the {@link DataSource}
 * interface of the GRAL library. This object is necessary to convert LabPal
 * tables into objects that the GRAL library can handle.
 *  
 * @author Sylvain Hallé
 */
public class GralDataTable extends Table implements DataSource
{
	/**
	 * The data listeners associated to this table
	 */
	protected Set<DataListener> m_dataListeners;
	
	/**
	 * A reference to the original table, reified with concrete values
	 */
	protected HardTable m_table;
	
	/**
	 * Creates a new GRAL data table out of an arbitrary table.
	 * @param t The table
	 */
	private GralDataTable(Table t)
	{
		super();
		m_dataListeners = new HashSet<DataListener>();
		m_table = t.getDataTable();
	}
	
	/**
	 * Converts an arbitrary LabPal table into a GRAL data table.
	 * @param t The table
	 * @return A GRAL data table. If <tt>t</tt> is already a GRAL table,
	 *   it is returned as is.
	 */
	public static GralDataTable toGral(Table t)
	{
		if (t instanceof GralDataTable)
			return (GralDataTable) t;
		return new GralDataTable(t);
	}
	
	@Override
	public void removeDataListener(DataListener dataListener)
	{
		m_dataListeners.remove(dataListener);
	}
	
	@Override
	public Row getRow(int row)
	{
		return new Row(this, row);
	}
	
	@Override
	public final Iterator<Comparable<?>> iterator()
	{
		return new RowIterator(m_table);
	}

	@Override
	public final void addDataListener(DataListener dataListener)
	{
		m_dataListeners.add(dataListener);
	}

	@Override
	public Statistics getStatistics()
	{
		return new Statistics(this);
	}
	
	@Override
	public final Column getColumn(int col)
	{
		return new Column(this, col);
	}

	@Override
	public Comparable<?> get(int arg0, int arg1)
	{
		return m_table.get(arg0, arg1).value();
	}

	@Override
	public int getColumnCount() 
	{
		return m_table.getColumnCount();
	}

	@Override
	public Class<? extends Comparable<?>>[] getColumnTypes() 
	{
		return m_table.getColumnTypes();
	}

	@Override
	public int getRowCount() 
	{
		return m_table.getRowCount();
	}

	@Override
	public boolean isColumnNumeric(int arg0)
	{
		return m_table.isColumnNumeric(arg0);
	}

	@Override
	protected TempTable getDataTable(boolean link_to_experiments, String... ordering)
	{
		return m_table.getDataTable(link_to_experiments, ordering);
	}

	@Override
	public TempTable getDataTable(boolean temporary)
	{
		return m_table.getDataTable(temporary);
	}

	@Override
	public NodeFunction getDependency(int row, int col)
	{
		return m_table.getDependency(row, col);
	}
	
	/**
	 * Gets the name of the <i>i</i>-th column of the table 
	 * @param col The index of the column
	 * @return The name
	 */
	public String getColumnName(int col)
	{
		return m_table.getColumnName(col);
	}
	
	/**
	 * Creates a two-dimensional GRAL data series out of two columns of a
	 * LabPal table. The resulting series will only contain lines of the
	 * original tables where the values in both columns are defined, i.e.
	 * all lines with missing values for these two columns will be filtered
	 * out. This is necessary since GRAL fails to draw a plot (i.e. throws
	 * a <tt>NullPointerException</tt>) when it contains
	 * data series with missing values.
	 *  
	 * @param col_name_x The name of the first column
	 * @param col_name_y The name of the second column. This will also be the
	 *   name of the resulting GRAL data series. 
	 * @param table The table from which to create the data series
	 * @return The GRAL data series
	 */
	public static DataSeries getCleanedDataSeries(String col_name_x, String col_name_y, HardTable table)
	{
		TempTable temp_t = new TempTable(table.getId(), col_name_x, col_name_y);
		for (TableEntry te : table.getEntries())
		{
			PrimitiveValue val_x = te.get(col_name_x);
			PrimitiveValue val_y = te.get(col_name_y);
			if (val_x != null && val_y != null)
			{
				TableEntry new_te = new TableEntry();
				new_te.put(col_name_x, val_x);
				new_te.put(col_name_y, val_y);
				temp_t.add(new_te);
			}
		}
		GralDataTable gdt = GralDataTable.toGral(temp_t);
		DataSeries series = new DataSeries(col_name_y, gdt, 0, 1);
		return series;
	}

	@Override
	public Table duplicate(boolean with_state)
	{
		GralDataTable t = new GralDataTable(m_table);
		if (with_state)
		{
			throw new UnsupportedOperationException("GRAL data tables do not support stateful duplication");
		}
		return t;
	}
}
