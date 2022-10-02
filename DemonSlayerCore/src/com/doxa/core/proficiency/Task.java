package com.doxa.core.proficiency;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Sound;
import org.bukkit.entity.Drowned;
import org.bukkit.entity.Husk;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.doxa.core.Main;

import net.md_5.bungee.api.ChatColor;

public class Task implements Listener {

	Main plugin;
	Stats stats;
	public Task(Main main, Stats s) {
		plugin = main;
		stats = s;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (stats.getKills(player) == -1) {
			stats.setDefense(player, 1);
			stats.setProficiency(player, 1);
			stats.setStrength(player, 1);
			stats.setKills(player, 0);
			stats.setPoints(player, 0);
		}
		killmap.put(player, 0);
	}
	
	Map<Player, Integer> killmap = new HashMap<Player, Integer>();
	
	public int getCurrentKills(Player player) {
		if (killmap.containsKey(player)) {
			return killmap.get(player);
		} else {
			return 0;
		}
	}
	
	public void setKillMap(Player player, int i) {
		killmap.put(player, i);
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (killmap.containsKey(player)) {
			if (killmap.get(player) < 1)
				return;
			stats.addKill(player, killmap.get(player));
			this.setKillMap(player, 0);
		}
	}
	
	@EventHandler
	public void onKill(EntityDeathEvent event) {
		if (event.getEntity() instanceof Player)
			return;
		LivingEntity entity = event.getEntity();
		if (entity instanceof Zombie || entity instanceof Husk || entity instanceof Drowned) {
			if (event.getEntity().getKiller() instanceof Player) {
				Player player = (Player) event.getEntity().getKiller();
				if (!player.getWorld().getName().equalsIgnoreCase("world"))
					return;
				final int kills = killmap.get(player);
				int newkills = kills+1;
				killmap.put(player, (newkills));
				switch (stats.getKills(player) + killmap.get(player)) {
					case 50:
						this.rewardPlayer(player, 50);
						break;
					case 100:
						this.rewardPlayer(player, 100);
						break;
					case 200:
						this.rewardPlayer(player, 200);
						break;
					case 400:
						this.rewardPlayer(player, 400);
						break;
					case 600:
						this.rewardPlayer(player, 600);
						break;
					case 1000:
						this.rewardPlayer(player, 1000);
						break;
				}
			}
		} else {
			return;
		}
	}
	
	public void rewardPlayer(Player player, int i) {
		stats.addPoint(player);
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bPoint received! Do &9/stats &bto use!"));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bDemon Kills: &9" + i));
		player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
	}
	
	
	
}
