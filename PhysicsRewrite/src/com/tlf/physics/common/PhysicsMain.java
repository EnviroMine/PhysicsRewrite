/**
 * @author thislooksfun
 */

package com.tlf.physics.common;

import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

import com.tlf.physics.event.EventHandlerCPW;
import com.tlf.physics.event.EventHandlerForge;

@Mod(modid = PhysicsMain.MODID, name = PhysicsMain.NAME, version = PhysicsMain.VERSION)
public class PhysicsMain
{
	public static final String MODID = "physics";
	public static final String NAME = "Physics testing";
	public static final String VERSION = "pre-alpha";
	
	/** The public instance */
	@Instance(PhysicsMain.MODID)
	public static PhysicsMain instance;
	
	private ModMetadata metadata;
	
	/** Called on mod preInit */
	@EventHandler
	public void onPreInit(FMLPreInitializationEvent event) {
		metadata = event.getModMetadata();
	}
	
	/** Called on mod init */
	@EventHandler
	public void onInit(FMLInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(new EventHandlerForge());
		FMLCommonHandler.instance().bus().register(new EventHandlerCPW());
	}
	
	/** Called on mod postInit */
	@EventHandler
	public void onPostInit(FMLPostInitializationEvent event) {		
		System.out.println(metadata.name + " " + metadata.version + " loaded!");
	}
}