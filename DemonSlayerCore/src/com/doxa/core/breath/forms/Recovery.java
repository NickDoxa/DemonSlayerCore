package com.doxa.core.breath.forms;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitScheduler;

import com.doxa.core.Main;
import com.doxa.core.OldParticleAPI;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class Recovery implements Listener {
	
	Main plugin;
	Cooldowns cd;
	public Recovery(Main main) {
		plugin = main;
		cd = new Cooldowns();
	}
	
	Map<Player, Boolean> timer_bool = new HashMap<Player, Boolean>();
	Map<Player, Integer> timer = new HashMap<Player, Integer>();
	
	//TIMER IS SET TO 10 SECONDS BY DEFAULT (NO COOLDOWN BY DEFAULT)
	public void startTimer(Player player) {
		timer_bool.put(player, true);
		BukkitScheduler scheduler = Bukkit.getScheduler();
		int t = scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				final double health = player.getHealth();
				if (health >= 10.0) {
					player.setHealth(20);
				} else {
					player.setHealth(health + 10);
				}
				OldParticleAPI api = new OldParticleAPI(player);
				int i = api.createSingleHelix(Particle.VILLAGER_HAPPY);
				scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
					public void run() {
						scheduler.cancelTask(i);
					}
				}, 2*20);
				timer_bool.put(player, false);
				timer.remove(player);
				player.spigot().sendMessage(ChatMessageType.ACTION_BAR, 
						new TextComponent(ChatColor.LIGHT_PURPLE + "You activated Recovery Breath!"));
			}
		}, 10*20);
		timer.put(player, t);
	}
	
	public boolean isTimerActive(Player player) {
		try {
			if (timer_bool.get(player)) {
				return true;
			} else {
				return false;
			}
		} catch (NullPointerException e) {
			timer_bool.put(player, false);
			return false;
		}
	}
	
	public void endTimer(Player player) {
		timer_bool.put(player, false);
		BukkitScheduler scheduler = Bukkit.getScheduler();
		scheduler.cancelTask(timer.get(player));
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (player.getInventory().getItemInMainHand() == null || player.getInventory().getItemInMainHand().getType() == Material.AIR)
			return;
		if (!plugin.getNichirin().isItemNichirin(player.getInventory().getItemInMainHand()))
			return;
		if (player.isSneaking()) {
			if (!this.isTimerActive(player)) {
				this.startTimer(player);
			} else {
				return;
			}
		} else {
			if (this.isTimerActive(player)) {
				this.endTimer(player);
			} else {
				return;
			}
		}
	}
	
}
