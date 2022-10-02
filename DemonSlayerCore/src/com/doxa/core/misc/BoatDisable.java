package com.doxa.core.misc;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleEnterEvent;

import com.doxa.core.Main;

public class BoatDisable implements Listener {
	
	Main plugin;
	public BoatDisable(Main main) {
		plugin = main;
	}
	
	@EventHandler
	public void onBoat(VehicleEnterEvent event) { 
		Entity e = event.getEntered();
		if (e.getType().equals(EntityType.ZOMBIE) || e.getType().equals(EntityType.HUSK) || e.getType().equals(EntityType.DROWNED)) {
			event.setCancelled(true);
		}
	}

}
