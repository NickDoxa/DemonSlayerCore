package com.doxa.core.breath.forms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import com.doxa.core.Main;
import com.doxa.core.OldParticleAPI;
import com.doxa.core.particles.ParticleAPI;
import com.doxa.core.particles.ParticleData;
import com.doxa.core.particles.Point;

public class Three implements Listener {

	Main plugin;
	Cooldowns cd;
	public Three(Main main) {
		plugin = main;
		cd = new Cooldowns();
	}
	
	Map<Player, Integer> amt = new HashMap<Player, Integer>();
	
	public void waterThree(Player player) { //proficiency
		ParticleAPI api = new ParticleAPI(plugin);
		if (cd.isPlayerOnCooldown(player, 3)) {
			return;}
		if (!amt.containsKey(player)) {
			amt.put(player, 0);
		}
		
		Particle.DustOptions dustOptions = new Particle.DustOptions(Color.BLUE, 2);
		Particle.DustOptions dustOptions2 = new Particle.DustOptions(Color.AQUA, 2);
		Point point1 = new Point(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
		Point point = point1.add(player.getLocation().getDirection());
		List<Point[]> loc = api.drawHelix(point, 1, 20, 5, 1, player.getLocation().getYaw(), new ParticleData<Object>(Particle.REDSTONE, player.getWorld(), 0, 0, 0, 0, dustOptions));
		api.drawHelix(point, 1, 20, 5, 1, player.getLocation().getYaw(), new ParticleData<Object>(Particle.REDSTONE, player.getWorld(), 0, 0, 0, 0, dustOptions2));
		api.drawHelix(point, 1, 20, 5, 1, player.getLocation().getYaw(), new ParticleData<Object>(Particle.WATER_SPLASH, player.getWorld(), 0));
		player.getWorld().playSound(player.getLocation(), Sound.AMBIENT_UNDERWATER_ENTER, 1, 1);
		player.setVelocity(new Vector(player.getLocation().getDirection().getX(), 0, player.getLocation().getDirection().getZ()).multiply(4));
		for (Point[] points : loc) {
			for (Point p : points) {
				Location l = p.getPointLocation(player);
				for (Entity e : player.getWorld().getNearbyEntities(l, 1, 1, 1)) {
					if (e != player) {
						if (e instanceof LivingEntity) {
							LivingEntity entity = (LivingEntity) e;
							entity.damage(2, player);
						}
					}
				}
			}
		}
		int a = amt.get(player);
		if (a < 2) {
			a++;
			amt.put(player, a);
			BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
	        scheduler.scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
	            @Override
	            public void run() {
	            	amt.put(player, 0);
	            }
	        },10*20);
		} else {
			amt.put(player, 0);
			cd.activateCooldown(player, 3, 10);
		}
	}
	
	List<Arrow> arrows = new ArrayList<Arrow>();
	Map<Arrow, Vector> map = new HashMap<Arrow, Vector>();
	
	public void demonThree(Player player) { //strength
		ParticleAPI api = new ParticleAPI(plugin);
		if (cd.isPlayerOnCooldown(player, 3)) {
			return;}
		Particle.DustOptions dustOptions = new Particle.DustOptions(Color.BLACK, 2);
		Particle.DustOptions dustOptions2 = new Particle.DustOptions(Color.RED, 2);
		Point point = new Point(player.getLocation().getX(), player.getLocation().getY() + 1.5, player.getLocation().getZ());
		api.drawLine(1, 20, point, player.getLocation().getDirection(), new ParticleData<Object>(Particle.REDSTONE, player.getWorld(), 0, 0, 0, 0, dustOptions));
		api.drawLine(1, 20, point, player.getLocation().getDirection(), new ParticleData<Object>(Particle.REDSTONE, player.getWorld(), 0, 0, 0, 0, dustOptions2));
		api.drawLine(1, 20, point, player.getLocation().getDirection(), new ParticleData<Object>(Particle.CRIT_MAGIC, player.getWorld(), 0));
		player.getWorld().playSound(player.getLocation(), Sound.ITEM_TRIDENT_RIPTIDE_1, 1, 1);
		Vector vec = player.getLocation().getDirection();
		Arrow arrow = (Arrow) player.launchProjectile(Arrow.class);
		map.put(arrow, vec);
		//TODO FIND WAY TO MAKE INVISIBLE
        arrow.setBounce(false);
        arrow.setGravity(false);
        arrow.setSilent(true);
        arrow.setVelocity(vec.multiply(5));
        arrow.setShooter((ProjectileSource) ((LivingEntity) player));
        arrows.add(arrow);
		cd.activateCooldown(player, 3, 7);
	}
	
	@EventHandler
	public void onDmg(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Arrow) {
			Arrow arrow = (Arrow) event.getDamager();
			if (arrows.contains(arrow)) {
				arrows.remove(arrow);
				event.setDamage(1);
				if (event.getEntity() instanceof Player) {
					Player player = (Player) event.getEntity();
					player.setVelocity(map.get(arrow).multiply(5));
				}
			}
		}
	}
	
	@EventHandler
	public void onHit(ProjectileHitEvent event) {
		Projectile p = event.getEntity();
		if (p instanceof Arrow) {
			if (arrows.contains(p)) {
				Arrow arrow = (Arrow) p;
				arrow.remove();
			}
		}
	}
	
	public void thunderThree(Player player) { //defense
		if (cd.isPlayerOnCooldown(player, 3)) {
			return;}
		OldParticleAPI oapi = new OldParticleAPI(player);
		oapi.startXHelixWithDamage(Color.AQUA, Color.YELLOW, 5*20);
		player.setWalkSpeed(1f);
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
            @Override
            public void run() {
            	player.setWalkSpeed(0.6f);
            }
        },5*20);
		cd.activateCooldown(player, 3, 20);
	}
	
	Map<Player, Boolean> wfo = new HashMap<Player, Boolean>();
	
	public void flameThree(Player player) { //strength
		if (wfo.containsKey(player)) {
			if (wfo.get(player)) {
				return;
			}
		}
		ParticleAPI api = new ParticleAPI(plugin);
		Particle.DustOptions dustOptions = new Particle.DustOptions(Color.YELLOW, 1);
		Particle.DustOptions dustOptions2 = new Particle.DustOptions(Color.RED, 1);
		Point point = new Point(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
		api.drawSlash(3.5, 2, player.getLocation().getYaw(), point, new ParticleData<Object>(Particle.REDSTONE, player.getWorld(), 0, 0, 0, 0, dustOptions));
		api.drawSlash(3.5, 2, player.getLocation().getYaw(), point, new ParticleData<Object>(Particle.FLAME, player.getWorld(), 0));
		api.drawSlash(3.5, 2, player.getLocation().getYaw(), point, new ParticleData<Object>(Particle.REDSTONE, player.getWorld(), 0, 0, 0, 0, dustOptions2));
		for (Entity e : player.getWorld().getNearbyEntities(point.getPointLocation(player), 2, 1, 2)) {
			if (e != player) {
				if (e instanceof LivingEntity) {
					LivingEntity entity = (LivingEntity) e;
					entity.damage(5, player);
					entity.getWorld().playSound(entity.getLocation(), Sound.BLOCK_BLASTFURNACE_FIRE_CRACKLE, 1, 1);
				}
			}
		}
		wfo.put(player, true);
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
            @Override
            public void run() {
            	wfo.put(player, false);
            }
        }, 1*20);
	}
	
	public void beastThree(Player player) { //strength
		if (wfo.containsKey(player)) {
			if (wfo.get(player)) {
				return;
			}
		}
		ParticleAPI api = new ParticleAPI(plugin);
		Point point = new Point(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
		api.drawSlash(3.5, 2, player.getLocation().getYaw(), point, new ParticleData<Object>(Particle.CRIT, player.getWorld(), 0));
		api.drawSlash(3.5, 2, player.getLocation().getYaw(), point, new ParticleData<Object>(Particle.CRIT_MAGIC, player.getWorld(), 0));
		for (Entity e : player.getWorld().getNearbyEntities(point.getPointLocation(player), 2, 1, 2)) {
			if (e != player) {
				if (e instanceof LivingEntity) {
					LivingEntity entity = (LivingEntity) e;
					entity.damage(5, player);
					entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_PIGLIN_BRUTE_ANGRY, 1, 1);
				}
			}
		}
		wfo.put(player, true);
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
            @Override
            public void run() {
            	wfo.put(player, false);
            }
        }, 1*20);
	}
	
	public void insectThree(Player player) { //strength
		if (cd.isPlayerOnCooldown(player, 3))
			return;
		ParticleAPI api = new ParticleAPI(plugin);
		Point point = new Point(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
		api.drawRandomSlashes(3.5, 2, player.getLocation().getYaw(), point, new ParticleData<Object>(Particle.FIREWORKS_SPARK, player.getWorld(), 0));
		api.drawRandomSlashes(3.5, 2, player.getLocation().getYaw(), point, new ParticleData<Object>(Particle.PORTAL, player.getWorld(), 0));
		Particle.DustOptions dustop = new Particle.DustOptions(Color.PURPLE, 1);
		api.drawRandomSlashes(3.5, 2, player.getLocation().getYaw(), point, new ParticleData<Object>(Particle.REDSTONE, player.getWorld(), 0, 0, 0, 0, dustop));
		for (Entity e : player.getWorld().getNearbyEntities(point.getPointLocation(player), 2, 1, 2)) {
			if (e != player) {
				if (e instanceof LivingEntity) {
					LivingEntity entity = (LivingEntity) e;
					entity.damage(2, player);
					entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 3*20, 3));
					entity.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 3*20, 1));
					entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_BEE_STING, 1, 1);
				}
			}
		}
		cd.activateCooldown(player, 3, 10);
	}
	
	public void sunThree(Player player) {
		if (cd.isPlayerOnCooldown(player, 3))
			return;
		OldParticleAPI oapi = new OldParticleAPI(player);
		final Location loc = player.getLocation();
		List<Integer> list = new ArrayList<Integer>();
		int i = oapi.createCircle(Particle.NAUTILUS, loc, 1);
		list.add(i);
		BukkitScheduler scheduler = Bukkit.getScheduler();
		scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				int i2 = oapi.createCircle(Particle.NAUTILUS, loc, 2);
				list.add(i2);
				scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
					@Override
					public void run() {
						int i3 = oapi.createCircle(Particle.NAUTILUS, loc, 3);
						list.add(i3);
					}
				},4);
				scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
					@Override
					public void run() {
						int i4 = oapi.createCircle(Particle.NAUTILUS, loc, 4);
						list.add(i4);
					}
				},7);
				scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
					@Override
					public void run() {
						int i5 = oapi.createCircle(Particle.NAUTILUS, loc, 5);
						list.add(i5);
					}
				},10);
				scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
					@Override
					public void run() {
						for (int cancels : list) {
							scheduler.cancelTask(cancels);
						}
					}
				},20);
			}
		},2);
		cd.activateCooldown(player, 3, 15);
	}
	
	public void soundThree(Player player) {
		if (cd.isPlayerOnCooldown(player, 3)) {
			return;}
		OldParticleAPI oapi = new OldParticleAPI(player);
		List<Integer> cancels = new ArrayList<Integer>();
		int i = oapi.createCircleSound(Particle.NOTE, (player.getLocation().add(new Location(player.getWorld(), 0, 1, 0))), 2);
		int i2 = oapi.createCircleSound(Particle.NOTE, (player.getLocation().add(new Location(player.getWorld(), 0, 1, 0))), 2);
		cancels.add(i);
		cancels.add(i2);
		player.setVelocity(player.getLocation().getDirection().multiply(2));
		BukkitScheduler scheduler = Bukkit.getScheduler();
		scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				int i = oapi.createCircleSound(Particle.NOTE, (player.getLocation().add(new Location(player.getWorld(), 0, 1, 0))), 2);
				int i2 = oapi.createCircleSound(Particle.NOTE, (player.getLocation().add(new Location(player.getWorld(), 0, 1, 0))), 2);
				player.setVelocity(player.getLocation().getDirection().multiply(2));
				cancels.add(i);
				cancels.add(i2);
			}
		}, 15);
		scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				int i = oapi.createCircleSound(Particle.NOTE, (player.getLocation().add(new Location(player.getWorld(), 0, 1, 0))), 2);
				int i2 = oapi.createCircleSound(Particle.NOTE, (player.getLocation().add(new Location(player.getWorld(), 0, 1, 0))), 2);
				cancels.add(i);
				cancels.add(i2);
			}
		}, 30);
		scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				for (int i : cancels) {
					scheduler.cancelTask(i);
				}
			}
		}, 35);
		cd.activateCooldown(player, 3, 8);
	}
	
}
