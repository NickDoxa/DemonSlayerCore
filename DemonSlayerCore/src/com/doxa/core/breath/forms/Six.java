package com.doxa.core.breath.forms;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import com.doxa.core.Main;
import com.doxa.core.OldParticleAPI;
import com.doxa.core.particles.ParticleAPI;
import com.doxa.core.particles.ParticleData;
import com.doxa.core.particles.Point;

public class Six {

	Main plugin;
	Cooldowns cd;
	public Six(Main main) {
		plugin = main;
		cd = new Cooldowns();
	}
	
	public void waterSix(Player player) { //strength
		ParticleAPI api = new ParticleAPI(plugin);
		if (cd.isPlayerOnCooldown(player, 6)) {
			return;}
		Particle.DustOptions dustOptions = new Particle.DustOptions(Color.BLUE, 5);
		Particle.DustOptions dustOptions2 = new Particle.DustOptions(Color.AQUA, 5);
		Point point = new Point(player.getLocation().getX(), player.getLocation().getY() + 1.5, player.getLocation().getZ());
		List<Point[]> loc = api.drawSuccessiveCircles(4, 4, 4, player.getLocation().getYaw() + 90, 2, player.getLocation().getDirection(),
				point, new ParticleData<Object>(Particle.REDSTONE, player.getWorld(), 0, 0, 0, 0, dustOptions));
		api.drawSuccessiveCircles(4, 4, 4, player.getLocation().getYaw() + 90, 2, player.getLocation().getDirection(),
				point, new ParticleData<Object>(Particle.REDSTONE, player.getWorld(), 0, 0, 0, 0, dustOptions2));
		api.drawSuccessiveCircles(4, 4, 4, player.getLocation().getYaw() + 90, 2, player.getLocation().getDirection(),
				point, new ParticleData<Object>(Particle.WATER_SPLASH, player.getWorld(), 0));
		player.getWorld().playSound(player.getLocation(), Sound.AMBIENT_UNDERWATER_ENTER, 1, 1);
		for (Point[] points : loc) {
			for (Point p : points) {
				Location l = p.getPointLocation(player);
				for (Entity e : player.getWorld().getNearbyEntities(l, 3, 3, 3)) {
					if (e != player) {
						if (e instanceof LivingEntity) {
							LivingEntity entity = (LivingEntity) e;
							entity.damage(5, player);
						}
					}
				}
			}
		}
		cd.activateCooldown(player, 6, 20);
	}
	
	public void thunderSix(Player player) { //defense
		if (cd.isPlayerOnCooldown(player, 6)) {
			return;}
		this.createTriCircle(player);
		cd.activateCooldown(player, 6, 10);
	}
	
	 public void createTriCircle(Player player) {
		 OldParticleAPI oapi = new OldParticleAPI(player);
		 List<Location> locs = oapi.createCircleSized(player, Particle.REDSTONE, 1, Color.YELLOW);
		 List<Location> locs2 = oapi.createCircleSized(player, Particle.REDSTONE, 1, Color.WHITE);
		 List<Location> locs3 = oapi.createCircleSized(player, Particle.REDSTONE, 2, Color.YELLOW);
		 List<Location> locs4 = oapi.createCircleSized(player, Particle.REDSTONE, 2, Color.AQUA);
		 List<Location> locs5 = oapi.createCircleSized(player, Particle.REDSTONE, 3, Color.YELLOW);
		 List<Location> locs6 = oapi.createCircleSized(player, Particle.REDSTONE, 3, Color.AQUA);
		 List<List<Location>> all = new ArrayList<List<Location>>();
		 all.add(locs);
		 all.add(locs2);
		 all.add(locs3);
		 all.add(locs4);
		 all.add(locs5);
		 all.add(locs6);
		 for (List<Location> ls : all) {
			 for (Location l : ls) {
					for (Entity e : player.getWorld().getNearbyEntities(l, 2, 2, 2)) {
						if (e != player) {
							if (e instanceof LivingEntity) {
								LivingEntity entity = (LivingEntity) e;
								entity.damage(5, player);
							}
						}
					}
			 }
		 }
	 }
	 
	 public void flameSix(Player player) { //strength
		 if (cd.isPlayerOnCooldown(player, 6))
			 return;
		 OldParticleAPI oapi = new OldParticleAPI(player);
		 player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BLASTFURNACE_FIRE_CRACKLE, 1, 1);
		 int id = oapi.createMassWave(Particle.FLAME, Particle.FIREWORKS_SPARK);
			BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
	        scheduler.scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
	            @Override
	            public void run() {
	            	Bukkit.getScheduler().cancelTask(id);
	            }
	        },50);
	        cd.activateCooldown(player, 6, 20);
	 }
	 
		public void demonSix(Player player) { //defense
			if (cd.isPlayerOnCooldown(player, 6))
				return;
			ParticleAPI api = new ParticleAPI(plugin);
			Particle.DustOptions dustOptions = new Particle.DustOptions(Color.BLACK, 2);
			Particle.DustOptions dustOptions2 = new Particle.DustOptions(Color.BLACK, 2);
			Point point1 = new Point(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
			Point point = point1.add(player.getLocation().getDirection());
			List<Point[]> loc = api.drawHelix(point, 1, 15, 5, 1, player.getLocation().getYaw(), new ParticleData<Object>(Particle.REDSTONE, player.getWorld(), 0, 0, 0, 0, dustOptions));
			api.drawHelix(point, 1, 15, 5, 1, player.getLocation().getYaw(), new ParticleData<Object>(Particle.REDSTONE, player.getWorld(), 0, 0, 0, 0, dustOptions2));
			api.drawHelix(point, 1, 15, 5, 1, player.getLocation().getYaw(), new ParticleData<Object>(Particle.FLAME, player.getWorld(), 0));
			player.getWorld().playSound(player.getLocation(), Sound.ITEM_TRIDENT_THUNDER, 1, 1);
			for (Point[] points : loc) {
				for (Point p : points) {
					Location l = p.getPointLocation(player);
					for (Entity e : player.getWorld().getNearbyEntities(l, 1, 1, 1)) {
						if (e != player) {
							if (e instanceof LivingEntity) {
								LivingEntity entity = (LivingEntity) e;
								entity.damage(3, player);
							}
						}
					}
				}
			}
			cd.activateCooldown(player, 6, 10);
		}
		
		public void beastSix(Player player) { //strength
			if (cd.isPlayerOnCooldown(player, 6))
				return;
			ParticleAPI api = new ParticleAPI(plugin);
			Point point = new Point(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
			api.drawRandomSlashes(3.5, 2, player.getLocation().getYaw(), point, new ParticleData<Object>(Particle.CRIT, player.getWorld(), 0));
			api.drawRandomSlashes(3.5, 2, player.getLocation().getYaw(), point, new ParticleData<Object>(Particle.CRIT_MAGIC, player.getWorld(), 0));
			for (Entity e : player.getWorld().getNearbyEntities(point.getPointLocation(player), 2, 1, 2)) {
				if (e != player) {
					if (e instanceof LivingEntity) {
						LivingEntity entity = (LivingEntity) e;
						entity.damage(5, player);
						entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_PIGLIN_BRUTE_ANGRY, 1, 1);
					}
				}
			}
			cd.activateCooldown(player, 6, 20);
		}
		
		public void insectSix(Player player) {
			if (cd.isPlayerOnCooldown(player, 6)) {
				return;}
			OldParticleAPI oapi = new OldParticleAPI(player);
			Location loc = new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY() + 1, player.getLocation().getZ());
			Location loc2 = new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY() + 2, player.getLocation().getZ());
			int i = oapi.createCircle(Particle.DRIPPING_OBSIDIAN_TEAR, player.getLocation(), 2);
			int i2 = oapi.createCircle(Particle.FIREWORKS_SPARK, player.getLocation(), 2);
			int i3 = oapi.createCircle(Particle.DRIPPING_OBSIDIAN_TEAR, loc, 2);
			int i4 = oapi.createCircle(Particle.FIREWORKS_SPARK, loc, 2);
			int i5 = oapi.createCircle(Particle.DRIPPING_OBSIDIAN_TEAR, loc2, 2);
			int i6 = oapi.createCircle(Particle.FIREWORKS_SPARK, loc2, 2);
			player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 10*20, 2));
			player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10*20, 4));
			player.removePotionEffect(PotionEffectType.POISON);
			BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
	        scheduler.scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
	            @Override
	            public void run() {
	            	Bukkit.getScheduler().cancelTask(i);
	            	Bukkit.getScheduler().cancelTask(i2);
	            	Bukkit.getScheduler().cancelTask(i3);
	            	Bukkit.getScheduler().cancelTask(i4);
	            	Bukkit.getScheduler().cancelTask(i5);
	            	Bukkit.getScheduler().cancelTask(i6);
	            }
	        },2);
	        scheduler.scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
	            @Override
	            public void run() {
	            	player.removePotionEffect(PotionEffectType.JUMP);
	            	player.removePotionEffect(PotionEffectType.SPEED);
	            }
	        },10*20);
			cd.activateCooldown(player, 6, 30);
		}
		
		public void sunSix(Player player) {
			if (cd.isPlayerOnCooldown(player, 6)) {
				return;}
			OldParticleAPI oapi = new OldParticleAPI(player);
			int i = oapi.createCircle(Particle.NAUTILUS, player.getLocation(), 2);
			player.setVelocity(new Vector(player.getLocation().getDirection().getX(), player.getLocation().getDirection().getY() + 2, player.getLocation().getDirection().getZ()));
			player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 10*20, 2));
			BukkitScheduler scheduler = Bukkit.getScheduler();
			scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
				@Override
				public void run() {
					player.setVelocity(player.getLocation().getDirection().multiply(2));
					scheduler.cancelTask(i);
					scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
						@Override
						public void run() {
							ParticleAPI api = new ParticleAPI(plugin);
							Particle.DustOptions dustOptions = new Particle.DustOptions(Color.RED, 1);
							Point point = new Point(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
							api.drawSlash(3.5, 3, player.getLocation().getYaw(), point, new ParticleData<Object>(Particle.REDSTONE, player.getWorld(), 0, 0, 0, 0, dustOptions));
							api.drawSlash(3.5, 3, player.getLocation().getYaw(), point, new ParticleData<Object>(Particle.LAVA, player.getWorld(), 0));
							api.drawSlash(3.5, 3, player.getLocation().getYaw(), point, new ParticleData<Object>(Particle.NAUTILUS, player.getWorld(), 0));
						}
					}, 10);
				}
			},20);
			cd.activateCooldown(player, 6, 20);
		}
		
		public void soundSix(Player player) { //proficiency
			if (cd.isPlayerOnCooldown(player, 6)) {
				return;}
			OldParticleAPI api = new OldParticleAPI(player);
			int i = api.createHelix(Particle.REDSTONE, player, Color.WHITE);
	        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20*20, 1));
			BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
	        scheduler.scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
	            @Override
	            public void run() {
	            	Bukkit.getScheduler().cancelTask(i);
	            }
	        },1*20);
	        cd.activateCooldown(player, 6, 45);
		}
	 
}
