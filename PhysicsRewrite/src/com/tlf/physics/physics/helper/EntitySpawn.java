/**
 * @author thislooksfun
 */

package com.tlf.physics.physics.helper;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class EntitySpawn
{
	/** The world in which to spawn the entity */
	private final World world;
	/** The entity to be spawned */
	private final Entity entity;
	
	public EntitySpawn(World world, Entity entity)
	{
		this.world = world;
		this.entity = entity;
	}
	
	/** Performs the update */
	public void go() {
		this.world.spawnEntityInWorld(this.entity);
	}
}