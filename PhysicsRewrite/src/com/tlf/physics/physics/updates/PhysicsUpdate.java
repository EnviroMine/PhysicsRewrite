/**
 * @author thislooksfun
 */

package com.tlf.physics.physics.updates;

import net.minecraft.world.World;

import com.tlf.physics.helper.Coords;
import com.tlf.physics.physics.PhysicsHandler;

/** The base class for physics updates */
public abstract class PhysicsUpdate
{
	/** The location of this update */
	public final Coords coords;
	/** The location that caused this update - used to prevent update loops (update A causing update B causing update C in the location of update A)*/
	public final Coords causedBy;
	/** The number of update cycles until this update triggers */
	public int delayTicks;
	
	public PhysicsUpdate(World world, int x, int y, int z, Coords causedBy) {
		this(new Coords(world, x, y, z), causedBy, 0);
	}
	public PhysicsUpdate(World world, int x, int y, int z, Coords causedBy, int delayTicks) {
		this(new Coords(world, x, y, z), causedBy, delayTicks);
	}
	public PhysicsUpdate(Coords coords, Coords causedBy) {
		this(coords, causedBy, 0);
	}
	public PhysicsUpdate(Coords coords, Coords causedBy, int delayTicks) {
		this.coords = coords;
		this.causedBy = causedBy;
		this.delayTicks = delayTicks;
	}
	
	/** Schedules updates for all surrounding blocks */
	protected void scheduleSurroundings(PhysicsHandler handler) {
		this.scheduleSurroundings(handler, 0);
	}
	/** Schedules updates for all surrounding blocks with specified delay */
	protected void scheduleSurroundings(PhysicsHandler handler, int delay)
	{
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				for (int k = -1; k < 2; k++) {
					if (!(i == 0 && j == 0 && k == 0))
					{
						int x = this.coords.x + i;
						int y = this.coords.y + j;
						int z = this.coords.z + k;
						
						Coords nextCoords = new Coords(this.coords.world, x, y, z);
						
						if (nextCoords != this.causedBy) {
							handler.scheduleUpdate(new PhysicsUpdateNormal(this.coords.world, x, y, z, this.coords.copy(), delay));
						}
					}
				}
			}
		}
	}
	
	/** Performs the update */
	public abstract void performUpdate(PhysicsHandler handler);
}