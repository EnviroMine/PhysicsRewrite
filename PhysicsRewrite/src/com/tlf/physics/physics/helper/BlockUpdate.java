/**
 * @author thislooksfun
 */

package com.tlf.physics.physics.helper;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import com.tlf.physics.helper.Coords;

/** Used to perform thread-safe block updates */
public class BlockUpdate
{
	/** The coordinates to set the block at */
	public final Coords pos;
	/** The block to set at the specified location */
	public final Block setTo;
	
	/** Sets the block at this location to air */
	public BlockUpdate(Coords pos) {
		this(pos, Blocks.air);
	}
	/** Sets the block at this location to the specified block */
	public BlockUpdate(Coords pos, Block setTo)
	{
		this.pos = pos;
		this.setTo = setTo;
	}
	
	/** Performs the update */
	public void go()
	{
		if (this.setTo == null || this.setTo == Blocks.air) {
			this.pos.setAir();
		} else {
			this.pos.setBlock(this.setTo);
		}
	}
}