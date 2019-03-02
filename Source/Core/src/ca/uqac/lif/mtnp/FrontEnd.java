/*
  MTNP: Manipulate Tables N'Plots
  Copyright (C) 2017-2019 Sylvain Hallé

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
package ca.uqac.lif.mtnp;

import ca.uqac.lif.mtnp.plot.gnuplot.GnuPlot;

public class FrontEnd
{
	/**
	 * The major version number
	 */
	public static final transient int s_majorVersionNumber = 0;

	/**
	 * The minor version number
	 */
	public static final transient int s_minorVersionNumber = 1;

	/**
	 * The revision version number
	 */
	public static final transient int s_revisionVersionNumber = 10;
	
	/**
	 * The version of GRAL embedded in MTNP
	 */
	public static final transient String s_gralVersionString = "0.11";

	public static void main(String[] args)
	{
		System.out.println(getCliHeader());
		System.out.println("You are running mtnp.jar, which is only a library to create\nplots. As a result nothing will happen here. Read the \nonline documentation to learn how to use MTNP.\n");
		System.out.println("GRAL version:    " + s_gralVersionString);
		System.out.println("Gnuplot version: " + GnuPlot.getGnuplotVersionString());
	}

	protected static String getCliHeader()
	{
		String out = "";
		out += "MTNP " + formatVersion() + " - Manipulate Tables N'Plots\n";
		out += "(C) 2017-2019 Laboratoire d'informatique formelle\nUniversité du Québec à Chicoutimi, Canada\n";
		return out;
	}

	/**
	 * Formats the version number into a string
	 * @return The version string
	 */
	protected static String formatVersion()
	{
		if (getRevision() == 0)
		{
			return s_majorVersionNumber + "." + s_minorVersionNumber;
		}
		return s_majorVersionNumber + "." + s_minorVersionNumber + "." + s_revisionVersionNumber;
	}

	/**
	 * Gets the major version number
	 * @return The number
	 */
	public static final int getMajor()
	{
		return s_majorVersionNumber;
	}

	/**
	 * Gets the minor version number
	 * @return The number
	 */
	public static final int getMinor()
	{
		return s_minorVersionNumber;
	}

	/**
	 * Gets the revision version number
	 * @return The number
	 */
	public static final int getRevision()
	{
		return s_revisionVersionNumber;
	}
}
