/* 
  This file is part of AvatarMod.
    
  AvatarMod is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.
  
  AvatarMod is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.
  
  You should have received a copy of the GNU General Public License
  along with AvatarMod. If not, see <http://www.gnu.org/licenses/>.
*/
package com.crowsofwar.avatar.common.bending.lightning;

import com.crowsofwar.avatar.common.bending.BendingStyle;
import com.crowsofwar.avatar.common.bending.fire.Firebending;
import com.crowsofwar.avatar.common.gui.BendingMenuInfo;
import com.crowsofwar.avatar.common.gui.MenuTheme;
import com.crowsofwar.avatar.common.gui.MenuTheme.ThemeColor;

import java.util.UUID;

/**
 * @author CrowsOfWar
 */
public class Lightningbending extends BendingStyle {

	public static UUID ID = UUID.fromString("a1e698cb-f945-4354-8e47-df13446d7da5");

	private final BendingMenuInfo menu;

	public Lightningbending() {

		super(Firebending.ID);

		addAbility("lightning_arc");
		addAbility("lightning_redirect");
		addAbility("lightning_spear");

		ThemeColor bkgd = new ThemeColor(0xEBF4F5, 0xDBE1E2);
		ThemeColor edge = new ThemeColor(0xC5DDDF, 0xACBFC0);
		ThemeColor icon = new ThemeColor(0xFFEBC2, 0xFBE9C3);

		MenuTheme theme = new MenuTheme(bkgd, edge, icon, 0xFFEBC2);
		menu = new BendingMenuInfo(theme, this);

	}

	@Override
	public BendingMenuInfo getRadialMenu() {
		return menu;
	}

	@Override
	public String getName() {
		return "lightningbending";
	}

	@Override
	public UUID getId() {
		return ID;
	}

}
