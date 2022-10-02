package com.doxa.core.breath.forms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import com.doxa.core.Main;
import com.doxa.core.OldParticleAPI;
import com.doxa.core.particles.ParticleAPI;
import com.doxa.core.particles.ParticleData;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class Eight implements Listener {

	Main plugin;
	Cooldowns cd;
	public Eight(Main main) {
		plugin = main;
		cd = new Cooldowns();
	}
	
	public void waterEight(Player player) { //strength
		if (cd.isPlayerOnCooldown(player, 8)) {
			return;
		}
		OldParticleAPI api = new OldParticleAPI(player);
		player.setVelocity(new Vector(player.getVelocity().getX(), 2, player.getVelocity().getZ()));
		int id = api.createWaterfall(player, 1, Particle.REDSTONE, Color.AQUA);
		int id2 = api.createWaterfallNoWake(player, 1, Particle.REDSTONE, Color.BLUE);
		int id3 = api.createWaterfallNoWake(player, 1, Particle.WATER_WAKE, null);
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
            @Override
            public void run() {
            	Bukkit.getScheduler().cancelTask(id);
            	Bukkit.getScheduler().cancelTask(id2);
            	Bukkit.getScheduler().cancelTask(id3);
            }
        },60);
        cd.activateCooldown(player, 8, 15);
	}
	
	List<Player> deu = new ArrayList<Player>();
	
	public void demonEight(Player player) {
		if (cd.isPlayerOnCooldown(player, 8))
			return;
		OldParticleAPI oapi = new OldParticleAPI(player);
		Location loc = player.getLocation();
		Location loc1 = new Location(loc.getWorld(), loc.getX() + 2, loc.getY()-1.5, loc.getZ() - 2);
		Location loc2 = new Location(loc.getWorld(), loc.getX() + 2, loc.getY()-1.5, loc.getZ() + 2);
		Location loc3 = new Location(loc.getWorld(), loc.getX() - 2, loc.getY()-1.5, loc.getZ() - 2);
		Location loc4 = new Location(loc.getWorld(), loc.getX() - 2, loc.getY()-1.5, loc.getZ() + 2);
		int i = oapi.createCircle(Particle.CRIT_MAGIC, new Location(loc.getWorld(), loc.getX(), loc.getY()-1.5, loc.getZ()), 2.5f);
		int i22 = oapi.createCircle(Particle.DRIPPING_OBSIDIAN_TEAR, new Location(loc.getWorld(), loc.getX(), loc.getY()-1.5, loc.getZ()), 2.5f);
		int i3 = oapi.createCircle(Particle.DRIPPING_OBSIDIAN_TEAR, loc1, 1f);
		int i4 = oapi.createCircle(Particle.CRIT_MAGIC, loc1, 1f);
		int i5 = oapi.createCircle(Particle.DRIPPING_OBSIDIAN_TEAR, loc2, 1f);
		int i6 = oapi.createCircle(Particle.CRIT_MAGIC, loc2, 1f);
		int i7 = oapi.createCircle(Particle.DRIPPING_OBSIDIAN_TEAR, loc3, 1f);
		int i8 = oapi.createCircle(Particle.CRIT_MAGIC, loc3, 1f);
		int i9 = oapi.createCircle(Particle.DRIPPING_OBSIDIAN_TEAR, loc4, 1f);
		int i10 = oapi.createCircle(Particle.CRIT_MAGIC, loc4, 1f);
		deu.add(player);
		BukkitScheduler scheduler = Bukkit.getScheduler();
		int i2 = scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
			@Override
			public void run() {
				player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
						new TextComponent(ChatColor.DARK_PURPLE + "Demon Speed (Left Click To Dash)"));
			}
		}, 0, 1);
		scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				deu.remove(player);
				map.put(player, false);
				scheduler.cancelTask(i2);
			}
		},10*20);
		scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				scheduler.cancelTask(i);
				scheduler.cancelTask(i22);
				scheduler.cancelTask(i3);
				scheduler.cancelTask(i4);
				scheduler.cancelTask(i5);
				scheduler.cancelTask(i6);
				scheduler.cancelTask(i7);
				scheduler.cancelTask(i8);
				scheduler.cancelTask(i9);
				scheduler.cancelTask(i10);
				map.put(player, true);
			}
		},10);
		cd.activateCooldown(player, 8, 20);
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (deu.contains(player)) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1*20, 4));
		}
	}
	
	Map<Player, Boolean> map = new HashMap<Player, Boolean>();
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		map.put(event.getPlayer(), false);
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_AIR) {
			Player player = event.getPlayer();
			if (deu.contains(player)) {
				if (map.containsKey(player)) {
					if (map.get(player)) {
						player.setVelocity(player.getLocation().getDirection().multiply(2));
						map.put(player, false);
						BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
				        scheduler.scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
				            @Override
				            public void run() {
				            	map.put(player, true);
				            }
				        },40);
					}
				}
			}
		}
	}

	public void beastEight(Player player) { //proficiency
		ParticleAPI api = new ParticleAPI(plugin);
		if (cd.isPlayerOnCooldown(player, 8)) {
			return;}
		api.drawVerticalCircle(player, new ParticleData<Object>(Particle.CRIT, player.getWorld(), 0), 2, 3, 3, 1);
		api.drawVerticalCircle(player, new ParticleData<Object>(Particle.CRIT_MAGIC, player.getWorld(), 0), 2, 3, 3, 1);
		player.getWorld().playSound(player.getLocation(), Sound.ENTITY_HOGLIN_ATTACK, 1, 1);
		player.setVelocity(player.getLocation().getDirection().multiply(1.25));
		cd.activateCooldown(player, 8, 10);
		for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 3, 3, 3)) {
			if (e != player) {
				if (e instanceof LivingEntity) {
					LivingEntity entity = (LivingEntity) e;
					entity.damage(5, player);
				}
			}
		}
	}
	
}
