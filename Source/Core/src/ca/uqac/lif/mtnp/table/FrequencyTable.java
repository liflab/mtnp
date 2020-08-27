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
package ca.uqac.lif.mtnp.table;

import ca.uqac.lif.petitpoucet.NodeFunction;

/**
 * Table that accumulates frequency values
 * @since 0.1.13
 * @author Sylvain Hallé
 */
public class FrequencyTable extends Table
{
	/**
	 * The preconfigured minimum value of the generated frequency table
	 * along the x axis
	 */
	protected double m_minX;

	/**
	 * The preconfigured maximum value of the generated frequency table
	 * along the x axis
	 */
	protected double m_maxX;

	/**
	 * The number of buckets that will divide the interval
	 * x<sub>max</sub> - x<sub>min</sub>
	 */
	protected int m_numBucketsX;

	/**
	 * The preconfigured minimum value of the generated frequency table
	 * along the y axis
	 */
	protected double m_minY;

	/**
	 * The preconfigured maximum value of the generated frequency table
	 * along the y axis
	 */
	protected double m_maxY;

	/**
	 * The number of buckets that will divide the interval
	 * y<sub>max</sub> - y<sub>min</sub>
	 */
	protected int m_numBucketsY;

	/**
	 * The actual values of the table
	 */
	protected double[][] m_values;
	
	/**
	 * The lower value of each bucket on the x axis
	 */
	protected double[] m_scaleX;
	
	/**
	 * The lower value of each bucket on the y axis
	 */
	protected double[] m_scaleY;
	
	/**
	 * An array containing the names given to the columns of the resulting
	 * table
	 */
	protected String[] m_columnNames;
	
	/**
	 * The width of each bucket on the x axis
	 */
	protected double m_widthX;
	
	/**
	 * The width of each bucket on the y axis
	 */
	protected double m_widthY;
	
	public FrequencyTable(double min_x, double max_x, int b_x, double min_y, double max_y, int b_y)
	{
		super();
		m_minX = min_x;
		m_minY = min_y;
		m_maxX = max_x;
		m_maxY = max_y;
		m_numBucketsX = b_x;
		m_numBucketsY = b_y;
		m_values = new double[b_y][b_x];
		for (int i = 0; i < b_y; i++)
		{
			for (int j = 0; j < b_x; j++)
			{
				m_values[i][j] = 0;
			}
		}
		m_widthX = (max_x - min_x) / (double) b_x;
		m_widthY = (max_y - min_y) / (double) b_y;
		m_scaleX = new double[b_x];
		for (int i = 0; i < b_x; i++)
		{
			m_scaleX[i] = m_widthX * (double) i;
		}
		m_scaleY = new double[b_y];
		for (int i = 0; i < b_y; i++)
		{
			m_scaleY[i] = m_widthY * (double) i;
		}
		m_columnNames = new String[b_x + 1];
		m_columnNames[0] = "y";
		for (int i = 0; i < m_scaleX.length; i++)
		{
			m_columnNames[i + 1] = Double.toString(m_scaleX[i]);
		}
	}
	
	/**
	 * Gets the minimum value on the x axis
	 * @return The value
	 */
	public double getMinX()
	{
		return m_minX;
	}
	
	/**
	 * Gets the minimum value on the y axis
	 * @return The value
	 */
	public double getMinY()
	{
		return m_minY;
	}
	
	/**
	 * Gets the maximum value on the x axis
	 * @return The value
	 */
	public double getMaxX()
	{
		return m_maxX;
	}
	
	/**
	 * Gets the maximum value on the y axis
	 * @return The value
	 */
	public double getMaxY()
	{
		return m_maxY;
	}
	
	/**
	 * Gets the width of each bucket on the x axis
	 * @return The width
	 */
	public double getXWidth()
	{
		return (m_maxX - m_minX) / (double) m_numBucketsX;
	}
	
	/**
	 * Gets the width of each bucket on the y axis
	 * @return The width
	 */
	public double getYWidth()
	{
		return (m_maxY - m_minY) / (double) m_numBucketsY;
	}
	
	/**
	 * Gets the array of values composing the frequency table
	 * @return The array of values
	 */
	public double[][] getArray()
	{
		return m_values;
	}
	
	/**
	 * Gets the number of buckets that divide the interval
	 * x<sub>max</sub> - x<sub>min</sub>
	 * @return The number of buckets
	 */
	public int getNumBucketsX()
	{
		return m_numBucketsX;
	}
	
	/**
	 * Gets the number of buckets that divide the interval
	 * y<sub>max</sub> - y<sub>min</sub>
	 * @return The number of buckets
	 */
	public int getNumBucketsY()
	{
		return m_numBucketsY;
	}
	
	/**
	 * Gets the labels of the table's scale on the x axis
	 * @return The array of labels
	 */
	public double[] getScaleX()
	{
		return m_scaleX;
	}
	
	/**
	 * Gets the labels of the table's scale on the y axis
	 * @return The array of labels
	 */
	public double[] getScaleY()
	{
		return m_scaleY;
	}
	
	/**
	 * Adds a value to the frequency table
	 * @param x The x position
	 * @param y The y position
	 * @param v The value to add in the corresponding cell
	 * @return This table
	 */
	public FrequencyTable add(double x, double y, double v)
	{
		int bin_x = (int) Math.floor(((x - m_minX) / (m_maxX - m_minX)) * (double) m_numBucketsX);
		int bin_y = (int) Math.floor(((y - m_minY) / (m_maxY - m_minY)) * (double) m_numBucketsY);
		if (bin_x < 0 || bin_x >= m_numBucketsX || bin_y < 0 || bin_y >= m_numBucketsY)
		{
			// Out of bounds: ignore
			return this;
		}
		m_values[bin_y][bin_x] += v;
		return this;
	}
	
	/**
	 * Adds 1 to the frequency table
	 * @param x The x position
	 * @param y The y position
	 * @param v The value to add in the corresponding cell
	 * @return This table
	 */
	public FrequencyTable add(double x, double y)
	{
		return add(x, y, 1);
	}

	@Override
	protected TempTable getDataTable(boolean link_to_experiments, String... ordering)
	{
		// Ignore column ordering, as it is important in such a table
		return getDataTable(link_to_experiments);
	}

	@Override
	public TempTable getDataTable(boolean temporary)
	{
		TempTable tt = new TempTable(getId(), m_columnNames);
		for (int i = 0; i < m_numBucketsY; i++)
		{
			TableEntry te = new TableEntry();
			te.put("y", m_scaleY[i]);
			for (int j = 0; j < m_numBucketsX; j++)
			{
				te.put(Double.toString(m_scaleX[j]), m_values[j][i]);
			}
			tt.add(te);
		}
		return tt;
	}

	@Override
	public NodeFunction getDependency(int row, int col) 
	{
		// No dependency given
		return null;
	}
}
