/**
 * @author thislooksfun
 */

package com.tlf.physics.physics.entity;

import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;

import com.tlf.physics.helper.Coords;

public class EntityExtendedFallingBlock extends EntityFallingBlock
{
	private TileEntity te;
	
	public EntityExtendedFallingBlock(Coords coords)
	{
		super(coords.world, coords.x+0.5F, coords.y+0.5F, coords.z+0.5F, coords.getBlock(), coords.getMetadata());
		if (coords.hasTileEntity()) {
			this.te = coords.getAndRemoveTileEntity();
		}
	}
	
	/** Sets the tileEntity back on block land */
	@Override
	public void onUpdate()
	{
		super.onUpdate();
        
		if (this.isDead)
		{
			int x = MathHelper.floor_double(this.posX);
	        int y = MathHelper.floor_double(this.posY);
	        int z = MathHelper.floor_double(this.posZ);
	        
			this.worldObj.setTileEntity(x, y, z, te);
		}
	}
}