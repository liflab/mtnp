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
import java.util.HashMap;
import java.util.Map;

/**
 * Transformation that applies a formatting from a {@link DecimalFormat}
 * object to every numerical cell of a table. It leaves non-numerical cells
 * as is.
 * @author Sylvain Hallé
 */
public class NumberFormatting extends SingleCellTransformation 
{
	/**
	 * Association between keys and formatters
	 */
	protected final Map<String,DecimalFormat> m_formatters;
	
	/**
	 * The default formatter for unspecified numerical fields
	 */
	protected DecimalFormat m_defaultFormatter;
	
	/**
	 * Creates a transformation with the specified formatting rules
	 * @param formatter A DecimalFormat object used to format the
	 * numbers
	 */
	public NumberFormatting(DecimalFormat formatter)
	{
		super();
		m_formatters = new HashMap<String,DecimalFormat>();
		m_defaultFormatter = formatter;
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
	
	/**
	 * Sets the formatting rules for a specific column in the table
	 * @param key The name of the column
	 * @param format The formatter instance
	 * @return This transformation
	 */
	public NumberFormatting setFormat(String key, DecimalFormat formatter)
	{
		m_formatters.put(key, formatter);
		return this;
	}
	
	/**
	 * Sets the formatting rules for a specific column in the table
	 * @param key The name of the column
	 * @param format The format string
	 * @return This transformation
	 */
	public NumberFormatting setFormat(String key, String format)
	{
		return setFormat(key, new DecimalFormat(format));
	}

	@Override
	public PrimitiveValue applyTransformation(String key, PrimitiveValue value) 
	{
		if (!value.isNumeric())
		{
			return value;
		}
		if (m_formatters.containsKey(key))
		{
			return PrimitiveValue.getInstance(m_formatters.get(key).format(value.numberValue()));
		}
		return PrimitiveValue.getInstance(m_defaultFormatter.format(value.numberValue()));
	}
}
