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

import java.util.Calendar;

import ca.uqac.lif.mtnp.util.CommandRunner;
import ca.uqac.lif.mtnp.DataFormatter;
import ca.uqac.lif.mtnp.plot.Plot;
import ca.uqac.lif.mtnp.table.Table;
import ca.uqac.lif.mtnp.table.TableTransformation;

/**
 * Top-level class for plots drawn using the GnuPlot software.
 * @author Sylvain Hallé
 */
public abstract class GnuPlot extends Plot
{
	/**
	 * The symbol used to separate data values in a file
	 */
	public static final transient String s_datafileSeparator = ",";
	
	/**
	 * The symbol used to represent missing values in a file
	 */
	public static final transient String s_datafileMissing = "null";
	
	/**
	 * The path to launch GnuPlot
	 */
	protected static transient String s_path = "gnuplot";
	
	/**
	 * The version string obtained when checking if Gnuplot is present
	 */
	protected static transient String s_gnuplotVersionString = checkGnuplot();
	
	/**
	 * The fill style used to draw the graph
	 */
	protected transient FillStyle m_fillStyle = FillStyle.SOLID;
	
	/**
	 * The fill style used for the plot
	 */
	public static enum FillStyle {SOLID, NONE, PATTERN};
	
	/**
	 * An string defining the plot's borders
	 */
	protected String m_border = "";
	
	/**
	 * An optional string containing custom parameters that will be put in
	 * the plot's header
	 */
	protected String m_customParameters = "";

	/**
	 * The time to wait before polling GnuPlot's result
	 */
	protected static transient long s_waitInterval = 100;
	
	/**
	 * Creates an empty GnuPlot
	 */
	public GnuPlot()
	{
		super();
	}
	
	public GnuPlot(Table table)
	{
		super(table);
	}
	
	public GnuPlot(Table table, TableTransformation transformation)
	{
		super(table, "", transformation);
	}
	
	/**
	 * Generates a stand-alone Gnuplot file for this plot
	 * @param term The terminal used to display the plot
	 * @param with_caption Set to true to ignore the plot's caption when
	 *   rendering
	 * @return The Gnuplot file contents
	 */
	public final String toGnuplot(ImageType term, boolean with_caption)
	{
		return toGnuplot(term, "", with_caption);
	}
	
	/**
	 * Sets the border settings for the plot.
	 * @param border A parameter string defining the border for the plot,
	 * according to the <a href="http://gnuplot.sourceforge.net/docs_4.2/node162.html">GnuPlot</a>
	 * syntax
	 * @return This plot
	 */
	public GnuPlot setBorder(String border)
	{
		m_border = border;
		return this;
	}
	
	/**
	 * Sets custom parameters to be added to the plot's header. This method can
	 * be used to define settings that are not directly handled through object
	 * methods.
	 * @param s The parameter string
	 * @return This plot
	 */
	public GnuPlot setCustomHeader(String s)
	{
		m_customParameters = s;
		return this;
	}

	/**
	 * Generates a stand-alone Gnuplot file for this plot
	 * @param term The terminal used to display the plot
	 * @param lab_title The title of the lab. This is only used in the 
	 *   auto-generated comments in the file's header
	 * @param with_caption Set to true to ignore the plot's caption when
	 *   rendering
	 * @return The Gnuplot file contents
	 */
	public abstract String toGnuplot(ImageType term, String lab_title, boolean with_caption);
	
	@Override
	public final byte[] getImage(ImageType term, boolean with_caption)
	{
		String instructions = toGnuplot(term, with_caption);
		byte[] image = null;
		String[] command = {s_path};
		CommandRunner runner = new CommandRunner(command, instructions);
		runner.start();
		// Wait until the command is done
		while (runner.isAlive())
		{
			// Wait 0.1 s and check again
			try
			{
				Thread.sleep(s_waitInterval);
			}
			catch (InterruptedException e)
			{
				// This happens if the user cancels the command manually
				runner.stopCommand();
				runner.interrupt();
				if (term == ImageType.PDF)
				{
					return s_blankImagePdf;
				}
				return s_blankImagePng;
			}
		}
		image = runner.getBytes();
		if (runner.getErrorCode() != 0 || image == null || image.length == 0)
		{
			// Gnuplot could not produce a picture; return the blank image
			if (term == ImageType.PDF)
			{
				image = s_blankImagePdf;
			}
			else
			{
				image = s_blankImagePng;
			}
		}
		return image;		
	}

	/**
	 * Checks if Gnuplot is present in the system
	 * @return true if Gnuplot is present, false otherwise
	 */
	public static boolean isGnuplotPresent()
	{
		return s_gnuplotVersionString.endsWith("exit code 0");
	}
	
	/**
	 * Gets a GnuPlot terminal name from an image type
	 * @param t The image type
	 * @return The terminal name
	 */
	public static String getTerminalName(ImageType t)
	{
		switch (t)
		{
		case PNG:
			return "png";
		case PDF:
			return "pdf";
		case DUMB:
			return "dumb";
		case CACA:
			return "caca";
		}
		return "dumb";
	}

	
	/**
	 * Produces a header that is common to all plots generated by the
	 * application
	 * @param term The terminal to display this plot
	 * @param comment_line A line to add in the header comments
	 * @param with_caption Set to true to ignore the plot's caption when
	 *   rendering
	 * @return The header
	 */
	public StringBuilder getHeader(ImageType term, String comment_line, boolean with_caption)
	{
		StringBuilder out = new StringBuilder();
		out.append("# ----------------------------------------------------------------").append(DataFormatter.CRLF);
		out.append("# ").append(comment_line).append(DataFormatter.CRLF);
		out.append("# Date:     ").append(String.format("%1$te-%1$tm-%1$tY", Calendar.getInstance())).append(DataFormatter.CRLF);
		out.append("# ----------------------------------------------------------------").append(DataFormatter.CRLF);
		if (with_caption)
		{
			out.append("set title \"").append(m_title).append("\"").append(DataFormatter.CRLF);
		}
		out.append("set datafile separator \"").append(s_datafileSeparator).append("\"").append(DataFormatter.CRLF);
		out.append("set datafile missing \"").append(s_datafileMissing).append("\"").append(DataFormatter.CRLF);
		out.append("set terminal ").append(getTerminalName(term)).append(DataFormatter.CRLF);
		switch (m_fillStyle)
		{
		case PATTERN:
			out.append("set style fill pattern").append(DataFormatter.CRLF);
			break;
		case SOLID:
			out.append("set style fill solid").append(DataFormatter.CRLF);
			break;
		default:
			// Do nothing
		}
		if (m_border != null && !m_border.isEmpty())
		{
			out.append("set border ").append(m_border).append("\n");
		}
		if (m_customParameters != null && m_customParameters.isEmpty())
		{
			out.append(m_customParameters).append("\n");
		}
		return out;
	}
	
	/**
	 * Gets the fill color associated with a number, based on the palette
	 * defined for this plot.
	 * @param color_nb The color number
	 * @return An empty string if no palette is defined, otherwise the
	 *   <tt>fillcolor</tt> expression corresponding to the color
	 */
	protected final String getFillColor(int color_nb)
	{
		if (m_palette == null || m_fillStyle != FillStyle.SOLID)
		{
			return "";
		}
		return "fillcolor rgb \"" + m_palette.getHexColor(color_nb) + "\"";
	}
	
	/**
	 * Gets the version string obtained when checking if Gnuplot is present 
	 * @return The version string
	 */
	public static String getGnuplotVersionString()
	{
		return s_gnuplotVersionString;
	}
	
	/**
	 * Checks if Gnuplot is present on the system
	 * @return A string with the version and exit code obtained when 
	 *   attempting to run Gnuplot
	 */
	protected static String checkGnuplot()
	{
		CommandRunner runner = new CommandRunner(new String[]{"gnuplot", "--version"});
		runner.run();
		return runner.getString().trim() + ", exit code " + runner.getErrorCode();
	}

}
