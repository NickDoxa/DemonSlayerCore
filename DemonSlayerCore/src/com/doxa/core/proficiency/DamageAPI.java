package com.doxa.core.proficiency;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.doxa.core.Main;

public class DamageAPI implements Listener {
	
	Main plugin;
	Stats stats;
	public DamageAPI(Main main, Stats s) {
		plugin = main;
		stats = s;
	}
	
	@EventHandler
	public void onDamageWithStrength(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player) {
			Player player = (Player) event.getDamager();
			if (event.getEntity() instanceof LivingEntity) {
				final double og = event.getDamage();
				event.setDamage(og + (1.5*stats.getStrength(player)));
			}
		}
	}
	
	@EventHandler
	public void onDamageWithDefense(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if (event.getDamager() instanceof LivingEntity) {
				final double og = event.getDamage();
				event.setDamage(og - (1.5*stats.getDefense(player)));
			}
		}
	}

}
