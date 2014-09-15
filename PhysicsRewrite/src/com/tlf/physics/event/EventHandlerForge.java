/**
 * @author thislooksfun
 */

package com.tlf.physics.event;

import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

import com.tlf.physics.physics.PhysicsThreadController;
import com.tlf.physics.physics.updates.PhysicsUpdateNormal;
import com.tlf.physics.physics.updates.PhysicsUpdateSurroundings;

public class EventHandlerForge
{
	//net.minecraftforge events
	/** Called when a player clicks (left or right) on a block */
	@SubscribeEvent
	public void onClick(PlayerInteractEvent event)
	{
		if(event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK && !event.world.isRemote)
		{
			int x = event.x;
			int y = event.y;
			int z = event.z;
			
			switch (event.face) {
			case 0:
				y--;
				break;
			case 1:
				y++;
				break;
			case 2:
				z--;
				break;
			case 3:
				z++;
				break;
			case 4:
				x--;
				break;
			case 5:
				x++;
				break;
			}
			PhysicsThreadController.instance.getWorldHandler(event.world).scheduleUpdate(new PhysicsUpdateNormal(event.world, x, y, z));
		}
	}
	
	/** Called on world unload – creates a PhysicsHandler for that world */
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event) {
		PhysicsThreadController.instance.createWorldHandler(event.world);
	}
	/** Called on world unload – removes the PhysicsHandler for that world */
	@SubscribeEvent
	public void onWorldUnLoad(WorldEvent.Unload event) {
		PhysicsThreadController.instance.removeWorldHandler(event.world);
	}
	
	/** Called when a block is broken */
	@SubscribeEvent
	public void blockBreak(BlockEvent.BreakEvent event) {
		PhysicsThreadController.instance.getWorldHandler(event.world).scheduleUpdate(new PhysicsUpdateSurroundings(event.world, event.x, event.y, event.z));
	}
}