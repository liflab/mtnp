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
package ca.uqac.lif.mtnp.plot;

/**
 * Manages basic properties of 2D plots.
 * @author Sylvain Hallé
 */
public interface TwoDimensionalPlot
{
	/**
	 * The two axes of a 2D plot
	 */
	public static enum Axis {X, Y};
	
	/**
	 * Sets the caption for one of the axes 
	 * @param axis The axis
	 * @param caption The caption
	 * @return This plot
	 */
	public TwoDimensionalPlot setCaption(Axis axis, String caption);
	
	/**
	 * Sets whether to use a log scale for one of the axes 
	 * @param axis The axis
	 * @return This plot
	 */
	public TwoDimensionalPlot setLogscale(Axis axis);
}
