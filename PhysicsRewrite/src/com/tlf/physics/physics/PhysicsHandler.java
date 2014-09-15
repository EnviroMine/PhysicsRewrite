/**
 * @author thislooksfun
 */

package com.tlf.physics.physics;

import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.tlf.physics.helper.Coords;
import com.tlf.physics.physics.updates.PhysicsUpdate;

public class PhysicsHandler implements Runnable
{
	/** The Set of scheduled updates to go through */
	private final Set<PhysicsUpdate> updates = new HashSet<PhysicsUpdate>();
	/** The Set of update locations - used to make sure only one update gets scheduled per block per cycle */
	private final Set<Coords> updateLocations = new HashSet<Coords>();
	
	/** The world object for this handler */
	public final World world;
	
	public PhysicsHandler(World world) {
		this.world = world;
	}
	
	/** Schedules an update to be performed later – only works on server-side */
	public void scheduleUpdate(PhysicsUpdate update)
	{
		if (!update.coords.world.isRemote && (update.coords.world.provider.dimensionId == this.world.provider.dimensionId))
		{
			boolean dupe = false;
			Iterator<Coords> ite = updateLocations.iterator();
			while (ite.hasNext()) {
				if (ite.next().equals(update.coords)) {
					dupe = true;
					break;
				}
			}
			
			if (!dupe) {
				this.updates.add(update);
				this.updateLocations.add(update.coords);
				//System.out.println("Scheduled update at " + update.coords.x + ", " + update.coords.y + ", " + update.coords.z);
			}
		}
	}
	
	/** Returns true if the size of the update list is > 0 */
	public boolean hasUpdates() {
		return this.updates.size() > 0;
	}
	
	/** Iterates through the updates */
	@Override
	public void run()
	{
		if (this.updates.size() >= 4096)
		{
			System.err.println("Dumping updates");
			this.updates.clear();
			this.updateLocations.clear();
		}
		
		Set<PhysicsUpdate> tempUpdates = PhysicsThreadController.cloneSet(this.updates);
		this.updates.clear();
		
		Iterator <PhysicsUpdate> iterator = tempUpdates.iterator();
		while (iterator.hasNext())
		{
			PhysicsUpdate update = iterator.next();
			if (update.delayTicks <= 0) {
				update.performUpdate(this);
				iterator.remove();
				updateLocations.remove(update.coords);
			} else {
				update.delayTicks--;
				this.updates.add(update);
			}
		}
		
		if (this.updates.size() != this.updateLocations.size())
		{
			this.updateLocations.clear();
			Iterator<PhysicsUpdate> ite = this.updates.iterator();
			while (ite.hasNext()) {
				this.updateLocations.add(ite.next().coords);
			}
		}
		
		PhysicsThreadController.instance.finishHandler(this.world.provider.dimensionId);
	}
}