/**
 * @author thislooksfun
 */

package com.tlf.physics.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

import com.tlf.physics.physics.PhysicsThreadController;

public class EventHandlerCPW
{
	//cpw.mods.fml events
	/** Called when a ServerTickEvent fires */
	@SubscribeEvent
	public void tickEnd(TickEvent.ServerTickEvent event)
	{
		if (event.phase == TickEvent.Phase.END) {
			PhysicsThreadController.instance.handleAllUpdates();
		}
	}
}