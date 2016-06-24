package com.maxandnoah.avatar.client;

import com.maxandnoah.avatar.common.AvatarCommonProxy;
import com.maxandnoah.avatar.common.IControlsHandler;
import com.maxandnoah.avatar.common.entity.EntityFloatingBlock;
import com.maxandnoah.avatar.common.gui.IAvatarGui;
import com.maxandnoah.avatar.common.network.IPacketHandler;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import static com.maxandnoah.avatar.common.gui.AvatarGuiIds.*;
import static com.maxandnoah.avatar.client.controls.AvatarKeybinding.*;
import static com.maxandnoah.avatar.common.AvatarAbility.*;

import com.maxandnoah.avatar.client.controls.AvatarKeybindingManager;
import com.maxandnoah.avatar.client.controls.ClientInput;
import com.maxandnoah.avatar.client.gui.RadialMenu;

@SideOnly(Side.CLIENT)
public class AvatarClientProxy implements AvatarCommonProxy {
	
	private Minecraft mc;
	private AvatarKeybindingManager keybindings;
	private PacketHandlerClient packetHandler;
	private ClientInput inputHandler;
	
	@Override
	public void preInit() {
		mc = Minecraft.getMinecraft();
		
		keybindings = new AvatarKeybindingManager();
		packetHandler = new PacketHandlerClient();
		
		inputHandler = new ClientInput();
		FMLCommonHandler.instance().bus().register(inputHandler);
		MinecraftForge.EVENT_BUS.register(inputHandler);
		
		
	}
	
	@Override
	public IControlsHandler getKeyHandler() {
		return keybindings;
	}
	
	@Override
	public IPacketHandler getClientPacketHandler() {
		return packetHandler;
	}
	
	@Override
	public double getPlayerReach() {
		PlayerControllerMP pc = mc.playerController;
		double reach = pc.getBlockReachDistance();
		if (pc.extendedReach()) reach = 6;
		return reach;
	}

	@Override
	public void init() {
		RenderingRegistry.registerEntityRenderingHandler(EntityFloatingBlock.class, new RenderFloatingBlock());
	}

	@Override
	public IAvatarGui createClientGui(int id, EntityPlayer player, World world, int x, int y, int z) {
		if (id == GUI_RADIAL_MENU)
			return new RadialMenu(KEY_RADIAL_MENU, ACTION_TOGGLE_BENDING, ACTION_THROW_BLOCK);
		
		return null;
	}

}
