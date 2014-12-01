/**
 * @author thislooksfun
 */

package com.tlf.physics.physics.updates;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

import com.tlf.physics.helper.Coords;
import com.tlf.physics.physics.PhysicsHandler;
import com.tlf.physics.physics.PhysicsThreadController;
import com.tlf.physics.physics.entity.EntityExtendedFallingBlock;
import com.tlf.physics.physics.helper.EntitySpawn;

/** Called when blocks are placed or updated from another PhysicsUpdate */
public class PhysicsUpdateNormal extends PhysicsUpdate
{
	public PhysicsUpdateNormal(World world, int x, int y, int z)
	{
		this(new Coords(world, x, y, z), 0);
	}
	
	public PhysicsUpdateNormal(World world, int x, int y, int z, Coords causedBy)
	{
		this(new Coords(world, x, y, z), causedBy, 0);
	}
	
	public PhysicsUpdateNormal(World world, int x, int y, int z, int delayTicks)
	{
		this(new Coords(world, x, y, z), delayTicks);
	}
	
	public PhysicsUpdateNormal(World world, int x, int y, int z, Coords causedBy, int delayTicks)
	{
		this(new Coords(world, x, y, z), causedBy, delayTicks);
	}
	
	public PhysicsUpdateNormal(Coords pos)
	{
		this(pos, 0);
	}
	
	public PhysicsUpdateNormal(Coords pos, Coords causedBy)
	{
		this(pos, causedBy, 0);
	}
	
	public PhysicsUpdateNormal(Coords pos, int delayTicks)
	{
		this(pos, pos.copy(), delayTicks);
	}
	
	public PhysicsUpdateNormal(Coords pos, Coords causedBy, int delayTicks)
	{
		super(pos, causedBy, delayTicks);
	}
	
	/** Returns true if the block specified by this update isn't air */
	private boolean isValidBlock()
	{
		return this.pos.getBlock() != Blocks.air;
	}
	
	/** Returns true if the block specified by this update is supported */
	//TODO make more advanced
	private boolean isSupported()
	{
		Block[][][] neighbors = this.pos.getNeighbors();
		
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				Block block = neighbors[i][0][j];
				if (block != null && block != Blocks.air)
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	@Override
	public void performUpdate(PhysicsHandler handler)
	{
		if (this.isValidBlock() && !this.isSupported())
		{
			EntityExtendedFallingBlock fallingBlock = new EntityExtendedFallingBlock(this.pos);
			
			PhysicsThreadController.instance.scheduleEntitySpawn(new EntitySpawn(this.pos.world, fallingBlock));
			handler.scheduleUpdate(new PhysicsUpdateCleanup(pos.copy()));
			this.scheduleSurroundings(handler, 0); //TODO
		}
	}
}