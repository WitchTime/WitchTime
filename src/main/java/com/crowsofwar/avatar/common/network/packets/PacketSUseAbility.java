package com.crowsofwar.avatar.common.network.packets;

import com.crowsofwar.avatar.AvatarLog;
import com.crowsofwar.avatar.common.bending.BendingAbility;
import com.crowsofwar.avatar.common.bending.BendingManager;
import com.crowsofwar.avatar.common.controls.AvatarControl;
import com.crowsofwar.avatar.common.network.PacketRedirector;
import com.crowsofwar.gorecore.util.VectorI;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Packet which tells the server that the client pressed a control. The control is given to the
 * player's active bending controller.
 * 
 * @see AvatarControl
 *
 */
public class PacketSUseAbility extends AvatarPacket<PacketSUseAbility> {
	
	private BendingAbility ability;
	private VectorI target;
	/** ID of EnumFacing of the side of the block player is looking at */
	private EnumFacing side;
	
	public PacketSUseAbility() {}
	
	public PacketSUseAbility(BendingAbility ability, VectorI target, EnumFacing side) {
		this.ability = ability;
		this.target = target;
		this.side = side;
		AvatarLog.debug("sending use ability packet...");
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		ability = BendingManager.getAbility(buf.readInt());
		if (ability == null) {
			throw new NullPointerException("Server sent invalid ability over network: ID " + ability);
		}
		target = buf.readBoolean() ? VectorI.fromBytes(buf) : null;
		side = EnumFacing.getFront(buf.readInt());
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(ability.getId());
		buf.writeBoolean(target != null);
		if (target != null) {
			target.toBytes(buf);
		}
		buf.writeInt(side == null ? -1 : side.ordinal());
	}
	
	@Override
	public Side getRecievedSide() {
		return Side.SERVER;
	}
	
	public BendingAbility getAbility() {
		return ability;
	}
	
	public VectorI getTargetPos() {
		return target;
	}
	
	public EnumFacing getSideHit() {
		return side;
	}
	
	@Override
	protected AvatarPacket.Handler<PacketSUseAbility> getPacketHandler() {
		return PacketRedirector::redirectMessage;
	}
	
}
