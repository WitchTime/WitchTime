package com.maxandnoah.avatar.common.bending;

import static com.maxandnoah.avatar.common.util.VectorUtils.times;

import com.maxandnoah.avatar.client.controls.AvatarKeybinding;
import com.maxandnoah.avatar.client.controls.AvatarOtherControl;
import com.maxandnoah.avatar.common.AvatarAbility;
import com.maxandnoah.avatar.common.AvatarControl;
import com.maxandnoah.avatar.common.data.AvatarPlayerData;
import com.maxandnoah.avatar.common.data.PlayerState;
import com.maxandnoah.avatar.common.entity.EntityFloatingBlock;
import com.maxandnoah.avatar.common.util.BlockPos;
import com.maxandnoah.avatar.common.util.Raytrace;
import com.maxandnoah.avatar.common.util.VectorUtils;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class Earthbending implements IBendingController {
	//EntityFallingBlock
	Earthbending() {
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		
	}

	@Override
	public int getID() {
		return BendingManager.BENDINGID_EARTHBENDING;
	}

	@Override
	public void onUpdate() {
		
	}

	@Override
	public void onAbility(AvatarAbility ability, AvatarPlayerData data) {
		PlayerState state = data.getState();
		EntityPlayer player = state.getPlayerEntity();
		World world = player.worldObj;
		EarthbendingState ebs = (EarthbendingState) data.getBendingState();
		
		if (ability == AvatarAbility.ACTION_TOGGLE_BENDING) {
			BlockPos target = state.verifyClientLookAtBlock(-1, 5);
			if (target != null) {
				if (ebs.getPickupBlock() != null) ebs.getPickupBlock().drop();
				
				Block block = world.getBlock(target.x, target.y, target.z);
				world.setBlock(target.x, target.y, target.z, Blocks.air);
				
				EntityFloatingBlock floating = new EntityFloatingBlock(world, block);
				floating.setPosition(target.x + 0.5, target.y, target.z + 0.5);
				
				Vec3 playerPos = VectorUtils.getEntityPos(player);
				Vec3 floatingPos = VectorUtils.getEntityPos(floating);
				Vec3 force = VectorUtils.minus(floatingPos, playerPos);
				force.normalize();
				VectorUtils.mult(force, 2);
				floating.lift();
				
				world.spawnEntityInWorld(floating);
				
				ebs.setPickupBlock(floating);
				
			}
		}
		if (ability == AvatarAbility.ACTION_THROW_BLOCK) {
			EntityFloatingBlock floating = ebs.getPickupBlock();
			if (floating != null) {
				float yaw = (float) Math.toRadians(player.rotationYaw);
				float pitch = (float) Math.toRadians(player.rotationPitch);
				
				// Calculate force and everything
				Vec3 lookDir = VectorUtils.fromYawPitch(yaw, pitch);
				floating.addForce(times(lookDir, 20));
				
				floating.setGravityEnabled(true);
			}
		}
		
	}

	@Override
	public IBendingState createState(AvatarPlayerData data) {
		return new EarthbendingState(data);
	}
	
}
