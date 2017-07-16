package ca.uqac.lif.mtnp;

import ca.uqac.lif.mtnp.plot.gnuplot.GnuPlot;

public class FrontEnd
{
	/**
	 * The major version number
	 */
	private static final transient int s_majorVersionNumber = 0;

	/**
	 * The minor version number
	 */
	private static final transient int s_minorVersionNumber = 1;

	/**
	 * The revision version number
	 */
	private static final transient int s_revisionVersionNumber = 3;
	
	/**
	 * The version of GRAL embedded in MTNP
	 */
	private static final transient String s_gralVersionString = "0.11";

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
		out += "(C) 2017 Laboratoire d'informatique formelle\nUniversité du Québec à Chicoutimi, Canada\n";
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
