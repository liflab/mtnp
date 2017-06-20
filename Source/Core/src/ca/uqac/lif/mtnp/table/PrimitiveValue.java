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

/**
 * Represents an atomic value that can be found in a table cell.
 * Such a value can either be a number or a string. Primitive
 * values are created so that a collection can mix values of
 * these types together, and still be able to sort them.
 * @author Sylvain Hallé
 */
public class PrimitiveValue implements Comparable<PrimitiveValue>
{
	protected String m_string;
	
	protected Number m_number;
	
	public static PrimitiveValue getInstance(Object o)
	{
		if (o instanceof PrimitiveValue)
		{
			return (PrimitiveValue) o;
		}
		return new PrimitiveValue(o);
	}
	
	private PrimitiveValue(Object o)
	{
		super();
		m_string = null;
		m_number = null;
		if (o == null)
		{
			return;
		}
		if (o instanceof Number)
		{
			m_number = (Number) o;
			return;
		}
		if (o instanceof String)
		{
			try
			{
				int f = Integer.parseInt((String) o);
				m_number = f;
				return;
			}
			catch (NumberFormatException e1)
			{
				try
				{
					float f = Float.parseFloat((String) o);
					m_number = f;
					return;
				}
				catch (NumberFormatException e2)
				{
					m_string = (String) o;
					return;
				}
			}
		}
		m_string = o.toString();
	}
	
	public boolean isNumeric()
	{
		return m_number != null;
	}
	
	public boolean isString()
	{
		return m_number == null;
	}
	
	public boolean isNull()
	{
		return m_number == null && m_string == null;
	}
	
	public Number numberValue()
	{
		return m_number;
	}
	
	public String stringValue()
	{
		return m_string;
	}
	
	/**
	 * Gets the value of this cell. 
	 * @return If the value is numeric, returns a {@code double};
	 *   otherwise, returns a string
	 */
	public Comparable<?> value()
	{
		if (isNumeric())
		{
			return m_number.doubleValue();
		}
		return m_string;
	}

	@Override
	public int compareTo(PrimitiveValue o)
	{
		if (o == null)
		{
			// Nulls go last
			return 1;
		}
		if (m_number != null && o.m_number != null)
		{
			// Numbers are compared as numbers
			return (int) Math.floor(m_number.doubleValue() - o.m_number.doubleValue());
		}
		if (m_number == null && o.m_number == null && m_string == null && o.m_string == null)
		{
			// Two nulls are equal
			return 0;
		}
		if (m_number != null && o.m_number == null)
		{
			// Numbers go before strings
			return -1;
		}
		if (m_number == null && o.m_number != null)
		{
			// Numbers go before strings
			return 1;
		}
		// Compare as strings
		return m_string.compareTo(o.m_string);
	}
	
	@Override
	public String toString()
	{
		if (m_number != null)
		{
			return m_number.toString();
		}
		if (m_string != null)
		{
			return m_string;
		}
		return "null";
	}
	
	public String toQuotedString()
	{
		if (m_number != null)
		{
			return m_number.toString();
		}
		if (m_string != null)
		{
			return "\" + m_string + \"";
		}
		return "\"null\"";
	}
	
	@Override
	public int hashCode()
	{
		if (m_number != null)
		{
			return 13 * m_number.intValue();
		}
		if (m_string != null)
		{
			return 37 * m_string.hashCode();
		}
		return 0;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o == null || !(o instanceof PrimitiveValue))
		{
			return false;
		}
		return compareTo((PrimitiveValue) o) == 0;
	}
}