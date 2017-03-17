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
package ca.uqac.lif.mtnp.table.rendering;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import ca.uqac.lif.mtnp.table.HardTable;
import ca.uqac.lif.mtnp.table.PrimitiveValue;
import ca.uqac.lif.mtnp.table.Table;
import ca.uqac.lif.mtnp.table.Table.CellCoordinate;
import ca.uqac.lif.petitpoucet.NodeFunction;
import ca.uqac.lif.mtnp.table.TableEntry;

public class PlainTableRenderer 
{
	/**
	 * The table to render
	 */
	protected Table m_table;
	
	/**
	 * A collection of cell coordinates that should be "highlighted" when
	 * displaying the table
	 */
	protected Set<CellCoordinate> m_cellsToHighlight;
	
	public PlainTableRenderer(Table t)
	{
		super();
		m_table = t;
		m_cellsToHighlight = new HashSet<CellCoordinate>();
	}
	
	public PlainTableRenderer(Table t, Collection<CellCoordinate> to_highlight)
	{
		super();
		m_table = t;
		m_cellsToHighlight = new HashSet<CellCoordinate>();
		m_cellsToHighlight.addAll(to_highlight);
	}
	
	public String render()
	{
		StringBuilder out = new StringBuilder();
		out.append("<table border=\"1\">\n");
		HardTable dt = m_table.getDataTable();
		String col_names[] = dt.getColumnNames();
		out.append("<thead>\n<tr>\n");
		for (String col_name : col_names)
		{
			out.append("<th>").append(col_name).append("</th>");
		}
		out.append("</tr></thead>\n<tbody>\n");
		int row = 0;
		for (TableEntry te : dt.getEntries())
		{
			out.append("<tr>");
			for (int col = 0; col < col_names.length; col++)
			{
				PrimitiveValue value = te.get(col_names[col]);
				String css_class = "";
				if (isHighlighted(row, col))
				{
					css_class += " class=\"highlighted\"";
				}
				NodeFunction nf = m_table.dependsOn(row, col);
				String dp_id = nf.getDataPointId();
				out.append("<td").append(css_class).append("><a class=\"explanation\" href=\"explain?id=").append(dp_id).append("\">").append(value).append("</a></td>");
			}
			out.append("</tr>\n");
			row++;
		}
		out.append("</tbody>\n</table>\n");
		return out.toString();
	}
	
	/**
	 * Determines if an x-y cell should be highlighted
	 * @param row The row
	 * @param col The column
	 * @return true if the cell should be highlighted
	 */
	public boolean isHighlighted(int row, int col)
	{
		for (CellCoordinate cc : m_cellsToHighlight)
		{
			if (cc.row == row && cc.col == col)
				return true;
		}
		return false;
	}
}
