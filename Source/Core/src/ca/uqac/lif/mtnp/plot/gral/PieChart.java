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

import ca.uqac.lif.mtnp.table.HardTable;
import ca.uqac.lif.mtnp.table.Table;
import ca.uqac.lif.mtnp.table.TableTransformation;
import de.erichseifert.gral.graphics.Insets2D;
import de.erichseifert.gral.plots.PiePlot;

public class PieChart extends GralPlot implements ca.uqac.lif.mtnp.plot.PieChart
{
	public PieChart(Table t)
	{
		super(t);
	}
	
	public PieChart(Table t, TableTransformation transformation)
	{
		super(t, transformation);
	}
	
	@Override
	public de.erichseifert.gral.plots.Plot getPlot(HardTable source)
	{
		GralDataTable gdt = GralDataTable.toGral(source);
		PiePlot plot = new PiePlot(gdt);
		plot.setInsets(new Insets2D.Double(20d, 60d, 60d, 40d));
		plot.getTitle().setText(getTitle());
		plot.setLegendVisible(true);
		customize(plot);
		return plot;
	}

}
