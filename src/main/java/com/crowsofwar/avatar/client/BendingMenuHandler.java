package com.crowsofwar.avatar.client;

import org.lwjgl.input.Mouse;

import com.crowsofwar.avatar.client.gui.RadialMenu;
import com.crowsofwar.avatar.common.bending.BendingController;
import com.crowsofwar.avatar.common.bending.BendingManager;
import com.crowsofwar.avatar.common.bending.BendingType;
import com.crowsofwar.avatar.common.gui.BendingMenuInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 
 * 
 * @author CrowsOfWar
 */
@SideOnly(Side.CLIENT)
public class BendingMenuHandler extends Gui {
	
	private RadialMenu currentGui;
	private final Minecraft mc;
	
	public BendingMenuHandler() {
		mc = Minecraft.getMinecraft();
	}
	
	@SubscribeEvent
	public void onGuiRender(RenderGameOverlayEvent.Post e) {
		
		ScaledResolution resolution = e.getResolution();
		
		int mouseX = Mouse.getX() * resolution.getScaledWidth() / mc.displayWidth;
		int mouseY = resolution.getScaledHeight()
				- (Mouse.getY() * resolution.getScaledHeight() / mc.displayHeight);
		
		if (currentGui != null) {
			if (currentGui.updateScreen(mouseX, mouseY, resolution)) {
				currentGui = null;
				mc.setIngameFocus();
			} else {
				currentGui.drawScreen(mouseX, mouseY, resolution);
			}
		}
		
	}
	
	public void openBendingGui(BendingType bending) {
		
		BendingController controller = BendingManager.getBending(bending);
		BendingMenuInfo menu = controller.getRadialMenu();
		
		this.currentGui = new RadialMenu(menu.getTheme(), menu.getKey(), menu.getButtons());
		mc.setIngameNotInFocus();
		
	}
	
}