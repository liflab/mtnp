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
package ca.uqac.lif.mtnp.table;

import org.junit.Test;

public class FrequencyTableTest
{
	@Test
	public void test1()
	{
		FrequencyTable ft = new FrequencyTable(0, 10, 5, 0, 10, 5);
		ft.add(2, 3);
		ft.add(5, 0, 2);
		TempTable tt = ft.getDataTable();
		System.out.println(tt);
	}
}
