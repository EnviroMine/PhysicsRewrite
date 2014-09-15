/**
 * @author thislooksfun
 */

package com.tlf.physics.physics.updates;

import net.minecraft.world.World;

import com.tlf.physics.helper.Coords;
import com.tlf.physics.physics.PhysicsHandler;
import com.tlf.physics.physics.PhysicsThreadController;
import com.tlf.physics.physics.helper.BlockUpdate;

/** Fired when a block is moved - used to set the space to air 1 tick later */
public class PhysicsUpdateCleanup extends PhysicsUpdate
{
	public PhysicsUpdateCleanup(World world, int x, int y, int z) {
		this(new Coords(world, x, y, z), 0);
	}
	public PhysicsUpdateCleanup(World world, int x, int y, int z, int delayTicks) {
		this(new Coords(world, x, y, z), delayTicks);
	}
	public PhysicsUpdateCleanup(Coords coords) {
		this(coords, 0);
	}
	public PhysicsUpdateCleanup(Coords coords, int delayTicks) {
		super(coords, coords.copy(), delayTicks);
	}
	
	@Override
	public void performUpdate(PhysicsHandler handler) {
		PhysicsThreadController.instance.scheduleBlockUpdate(new BlockUpdate(this.coords.copy()));
	}
}