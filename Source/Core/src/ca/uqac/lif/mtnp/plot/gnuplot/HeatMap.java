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
package ca.uqac.lif.mtnp.plot.gnuplot;

import ca.uqac.lif.mtnp.plot.TwoDimensionalPlot;
import ca.uqac.lif.mtnp.table.FrequencyTable;
import ca.uqac.lif.mtnp.table.Table;

/**
 * Generates a Gnuplot file from a 2D heatmap. The input table is
 * a {@link FrequencyTable}.
 * @author Sylvain Hallé
 */
public class HeatMap extends GnuPlot implements TwoDimensionalPlot
{
	/**
	 * The caption of the X axis
	 */
	protected String m_captionX = "";

	/**
	 * The caption of the Y axis
	 */
	protected String m_captionY = "";

	/**
	 * Creates a new heat map associated to no table
	 */
	protected HeatMap()
	{
		super();
	}

	/**
	 * Creates a new heat map from a frequency table
	 * @param t A {@link FrequencyTable}
	 * @throws IllegalArgumentException If the argument passed is not a
	 * {@link FrequencyTable}
	 */
	public HeatMap(Table t)
	{
		super(t);
		if (!(t instanceof FrequencyTable))
		{
			throw new IllegalArgumentException("HeatMap must be passed a FrequencyTable");
		}
	}

	@Override
	public String toGnuplot(ImageType term, String lab_title, boolean with_caption) 
	{		
		// Create Gnuplot output file from that data
		StringBuilder out = new StringBuilder();
		out.append(getHeader(term, lab_title, with_caption));
		FrequencyTable ft = (FrequencyTable) m_table;
		//out.append("set xrange [").append(ft.getMinX() - ft.getXWidth() / 2).append(":").append(ft.getMaxX() - ft.getXWidth() / 2).append("]\n");
		//out.append("set yrange [").append(ft.getMinY() - ft.getYWidth() / 2).append(":").append(ft.getMaxY() - ft.getYWidth() / 2).append("]\n");
		out.append("set tic scale 0\n");
		out.append("unset cbtics\n");
		out.append("$map1 << EOD\n");
		double[][] values = ft.getArray();
		double[] scale_x = ft.getScaleX();
		double[] scale_y = ft.getScaleY();
		for (int j = 0; j < scale_x.length; j++)
		{
			out.append(",").append(scale_x[j]);
		}
		out.append("\n");
		for (int i = 0; i < values.length; i++)
		{
			out.append(scale_y[i]);
			for (int j = 0; j < values[i].length; j++)
			{
				out.append(",").append(values[i][j]);
			}
			out.append("\n");
		}
		out.append("EOD\n\n");
		out.append("set view map\n");
		out.append("plot '$map1' matrix rowheaders columnheaders using 1:2:3 with image\n");
		return out.toString();
	}

	@Override
	public HeatMap setCaption(Axis axis, String caption)
	{
		if (axis == Axis.X)
		{
			m_captionX = caption;
		}
		else
		{
			m_captionY = caption;
		}
		return this;
	}

	@Override
	public HeatMap setLogscale(Axis axis)
	{
		// Ignore this parameter for heat maps
		return this;
	}
}
