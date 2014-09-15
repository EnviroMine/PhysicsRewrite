/**
 * @author thislooksfun
 */

package com.tlf.physics.physics.helper;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import com.tlf.physics.helper.Coords;

public class BlockUpdate
{
	/** The coordinates to set the block at */
	public final Coords coords;
	/** The block to set at the specified location */
	public final Block setTo;
	
	/** Sets the block at this location to air */
	public BlockUpdate(Coords coords) {
		this(coords, Blocks.air);
	}
	/** Sets the block at this location to the specified block */
	public BlockUpdate(Coords coords, Block setTo)
	{
		this.coords = coords;
		this.setTo = setTo;
	}
	
	/** Performs the update */
	public void go()
	{
		if (this.setTo == null || this.setTo == Blocks.air) {
			this.coords.setAir();
		} else {
			this.coords.setBlock(this.setTo);
		}
	}
}