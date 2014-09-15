/**
 * @author thislooksfun
 */

package com.tlf.physics.physics;

import net.minecraft.world.World;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.tlf.physics.physics.helper.BlockUpdate;
import com.tlf.physics.physics.helper.EntitySpawn;

public class PhysicsThreadController
{
	/** The map of all handlers */
	private static final Map<World, PhysicsHandler> instances = new HashMap<World, PhysicsHandler>();
	/** The set of the dimentionIDs of the active PhysicsHandler worlds. Used to make sure two threads don't get started for the same world */
	private static final Set<Integer> activeHandlerWorlds = new HashSet<Integer>();
	
	/** The set of entities to spawn next tick */
	private final Set<EntitySpawn> toSpawn = new HashSet<EntitySpawn>();
	/** The set of block updates to perform next tick */
	private final Set<BlockUpdate> blockUpdates = new HashSet<BlockUpdate>();
	
	/** How many ticks to wait before the next update. 0 = every tick, 1 = every other, 2 = every 3rd, etc. */
	public static int updateFrequency = 0;
	/** How many ticks until the next update. See {@link #updateFrequency} */
	private int updateDelay = 0;
	
	/** The public instance */
	public static final PhysicsThreadController instance = new PhysicsThreadController();
	
	/** Private so there is only one instance */
	private PhysicsThreadController() {}
	
	/** If there isn't a PhysicsHandler for the specified world, then it creates and returns one. Otherwise, it just returns the existing one */
	public PhysicsHandler createWorldHandler(World world)
	{
		if (this.instances.containsKey(world)) {
			return this.getWorldHandler(world);
		} else {
			PhysicsHandler handler = new PhysicsHandler(world);
			this.instances.put(world, handler);
			return handler;
		}
	}
	/** Gets the existing PhysicsHandler for this world if one exists. If not, then it creates one and returns that. */
	public PhysicsHandler getWorldHandler(World world) {
		return this.instances.containsKey(world) ? this.instances.get(world) : this.createWorldHandler(world);
	}
	/** Removes the PhysicsHandler for the specified world and returns it */
	public PhysicsHandler removeWorldHandler(World world) {
		return this.instances.remove(world);
	}
	
	/** Called from the PhysicsHandlers thread to remove them from the active list <br> <b>DO NOT CALL ELSEWARE</b>*/
	public void finishHandler(int worldID) {
		this.activeHandlerWorlds.remove(worldID);
	}
	
	/** Handles entity spawns */
	private void handleSpawns()
	{
		Set<EntitySpawn> tempSpawns = cloneSet(this.toSpawn);
		this.toSpawn.clear();
		Iterator<EntitySpawn> ite = tempSpawns.iterator();
		while (ite.hasNext()) {
			ite.next().go();
			ite.remove();
		}
		
	}
	/** Handles block updates */
	private void handleBlockUpdates()
	{
		Set<BlockUpdate> tempBlockUpdates = cloneSet(this.blockUpdates);
		this.blockUpdates.clear();
		Iterator<BlockUpdate> ite = tempBlockUpdates.iterator();
		while (ite.hasNext()) {
			ite.next().go();
			ite.remove();
		}
	}
	
	/** Handles all updates */
	public void handleAllUpdates()
	{
		this.handleSpawns();
		this.handleBlockUpdates();
		
		if (this.updateDelay == 0)
		{
			Iterator<PhysicsHandler> ite = this.instances.values().iterator();
			while (ite.hasNext()) {
				PhysicsHandler handler = ite.next();
				if (!activeHandlerWorlds.contains(handler.world.provider.dimensionId) && handler.hasUpdates())
				{
					Thread thread = new Thread(handler, "Physics handler ("+handler.world.provider.getDimensionName()+")");
					thread.start();
					this.activeHandlerWorlds.add(handler.world.provider.dimensionId);
				}
			}
			
			this.updateDelay = updateFrequency;
		} else {
			this.updateDelay--;
		}
	}
	
	/** Schedules an entity to spawn next tick */
	public synchronized void scheduleEntitySpawn(EntitySpawn es) {
		this.toSpawn.add(es);
	}
	/** Schedules an block update to occur next tick */
	public synchronized void scheduleBlockUpdate(BlockUpdate bu) {
		this.blockUpdates.add(bu);
	}
	
	/** Creates a duplicate of the specified set */
	public static <E> Set<E> cloneSet(Set<E> set)
	{
		Set<E> tempUpdates = new HashSet<E>();
		Iterator<E> iterator = set.iterator();
		while (iterator.hasNext()) {
			tempUpdates.add(iterator.next());
		}
		
		return tempUpdates;
	}
}