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

import java.text.DecimalFormat;

/**
 * Transformation that applies a formatting from a {@link DecimalFormat}
 * object to every numerical cell of a table. It leaves non-numerical cells
 * as is.
 * @author Sylvain Hallé
 */
public class NumberFormatting extends SingleCellTransformation 
{
	/**
	 * The formatter used to format the numbers
	 */
	protected final DecimalFormat m_formatter;
	
	/**
	 * Creates a transformation with the specified formatting rules
	 * @param formatter A DecimalFormat object used to format the
	 * numbers
	 */
	public NumberFormatting(DecimalFormat formatter)
	{
		super();
		m_formatter = formatter;
	}
	
	/**
	 * Creates a transformation with the specified formatting rules
	 * @param format_string A formatting string, as specified in the
	 * documentation for the {@link DecimalFormat} class
	 */
	public NumberFormatting(String format_string)
	{
		this(new DecimalFormat(format_string));
	}
	
	/**
	 * Creates a transformation with default formatting
	 */
	public NumberFormatting()
	{
		this(new DecimalFormat());
	}

	@Override
	public PrimitiveValue applyTransformation(String key, PrimitiveValue value) 
	{
		if (!value.isNumeric())
		{
			return value;
		}
		return PrimitiveValue.getInstance(m_formatter.format(value.numberValue()));
	}
}
