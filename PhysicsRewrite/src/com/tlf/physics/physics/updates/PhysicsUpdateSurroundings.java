/**
 * @author thislooksfun
 */

package com.tlf.physics.physics.updates;

import net.minecraft.world.World;

import com.tlf.physics.helper.Coords;
import com.tlf.physics.physics.PhysicsHandler;

/** Fired when a block is broken */
public class PhysicsUpdateSurroundings extends PhysicsUpdate
{
	public PhysicsUpdateSurroundings(World world, int x, int y, int z) {
		this(new Coords(world, x, y, z), 0);
	}
	public PhysicsUpdateSurroundings(World world, int x, int y, int z, int delayTicks) {
		this(new Coords(world, x, y, z), delayTicks);
	}
	public PhysicsUpdateSurroundings(Coords coords) {
		this(coords, 0);
	}
	public PhysicsUpdateSurroundings(Coords coords, int delayTicks) {
		super(coords, coords.copy(), delayTicks);
	}
	
	@Override
	public void performUpdate(PhysicsHandler handler) {
		this.scheduleSurroundings(handler);
	}
}