package com.doxa.core.breath.forms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import com.doxa.core.Main;
import com.doxa.core.OldParticleAPI;
import com.doxa.core.particles.ParticleAPI;
import com.doxa.core.particles.ParticleData;
import com.doxa.core.particles.Point;

public class One implements Listener {
	
	Main plugin;
	Cooldowns cd;
	public One(Main main) {
		plugin = main;
		cd = new Cooldowns();
	}
	
	Map<Player, Boolean> wfo = new HashMap<Player, Boolean>();
	
	public void waterOne(Player player) { //strength
		if (wfo.containsKey(player)) {
			if (wfo.get(player)) {
				return;
			}
		}
		ParticleAPI api = new ParticleAPI(plugin);
		Particle.DustOptions dustOptions = new Particle.DustOptions(Color.BLUE, 1);
		Particle.DustOptions dustOptions2 = new Particle.DustOptions(Color.AQUA, 1);
		Point point = new Point(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
		api.drawSlash(3.5, 2, player.getLocation().getYaw(), point, new ParticleData<Object>(Particle.REDSTONE, player.getWorld(), 0, 0, 0, 0, dustOptions));
		api.drawSlash(3.5, 2, player.getLocation().getYaw(), point, new ParticleData<Object>(Particle.REDSTONE, player.getWorld(), 0, 0, 0, 0, dustOptions2));
		api.drawSlash(3.5, 2, player.getLocation().getYaw(), point, new ParticleData<Object>(Particle.WATER_SPLASH, player.getWorld(), 0));
		for (Entity e : player.getWorld().getNearbyEntities(point.getPointLocation(player), 2, 1, 2)) {
			if (e != player) {
				if (e instanceof LivingEntity) {
					LivingEntity entity = (LivingEntity) e;
					entity.damage(5, player);
					entity.getWorld().playSound(entity.getLocation(), Sound.AMBIENT_UNDERWATER_ENTER, 1, 1);
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
	
	public void demonOne(Player player) { //strength
		if (cd.isPlayerOnCooldown(player, 1)) {
			return;}
		ParticleAPI api = new ParticleAPI(plugin);
		List<Point[]> loc = api.drawBurstLine(1, 30, new Point(player.getLocation().getX(), player.getLocation().getY() + 1.5, player.getLocation().getZ()), 
				player.getLocation().getDirection(), new ParticleData<Object>(Particle.SOUL_FIRE_FLAME, player.getWorld(), 0), 
				new ParticleData<Object>(Particle.SOUL_FIRE_FLAME, player.getWorld(), 2));
		for (Point[] points : loc) {
			for (Point p : points) {
				Location l = p.getPointLocation(player);
				for (Entity e : player.getWorld().getNearbyEntities(l, 2, 1, 2)) {
					if (e != player) {
						if (e instanceof LivingEntity) {
							LivingEntity entity = (LivingEntity) e;
							entity.damage(5, player);
							entity.setFireTicks(3*20);
							entity.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, entity.getLocation(), 0);
							entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
						}
					}
				}
			}
		}
		cd.activateCooldown(player, 1, 10);
	}
	
	public void thunderOne(Player player) { //strength
		if (cd.isPlayerOnCooldown(player, 1)) {
			return;}
		ParticleAPI api = new ParticleAPI(plugin);
		Point point = new Point(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
		player.setVelocity(new Vector(player.getLocation().getDirection().getX(), 0, player.getLocation().getDirection().getZ()).multiply(4));
		OldParticleAPI oapi = new OldParticleAPI(player);
		oapi.startXHelix(Color.YELLOW, Color.AQUA, 10);
		api.drawHelix(point, 1, 20, 5, 1, player.getLocation().getYaw(), new ParticleData<Object>(Particle.REDSTONE, player.getWorld(), 0, 0, 0, 0, 
				new Particle.DustOptions(Color.AQUA, 2)));
		api.drawHelix(point, 1, 20, 5, 1, player.getLocation().getYaw(), new ParticleData<Object>(Particle.REDSTONE, player.getWorld(), 0, 0, 0, 0, 
				new Particle.DustOptions(Color.YELLOW, 2)));
		api.drawHelix(point, 1, 20, 5, 1, player.getLocation().getYaw(), new ParticleData<Object>(Particle.REDSTONE, player.getWorld(), 0, 0, 0, 0, 
				new Particle.DustOptions(Color.WHITE, 2)));
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
            @Override
            public void run() {
        		Point point = new Point(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
        		List<Point[]> loc = api.drawSlash(3.5, 2, player.getLocation().getYaw(), point, 
        				new ParticleData<Object>(Particle.ELECTRIC_SPARK, player.getWorld(), 0));
        		api.drawSlash(3.5, 2, player.getLocation().getYaw(), point, 
        				new ParticleData<Object>(Particle.REDSTONE, player.getWorld(), 0, 0, 0, 0, new Particle.DustOptions(Color.YELLOW, 1)));
        		for (Point[] points : loc) {
        			for (Point p : points) {
        				Location l = p.getPointLocation(player);
        				for (Entity e : player.getWorld().getNearbyEntities(l, 2, 1, 2)) {
        					if (e != player) {
        						if (e instanceof LivingEntity) {
        							LivingEntity entity = (LivingEntity) e;
        							entity.damage(5, player);
        							entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1, 1);
        						}
        					}
        				}
        			}
        		}
            }
        }, 10);
		cd.activateCooldown(player, 1, 10);
	}
	
	public void flameOne(Player player) { //strength
		if (cd.isPlayerOnCooldown(player, 1)) {
			return;}
		ParticleAPI api = new ParticleAPI(plugin);
		player.setVelocity(new Vector(player.getLocation().getDirection().getX(), 0, player.getLocation().getDirection().getZ()).multiply(4));
		OldParticleAPI oapi = new OldParticleAPI(player);
		oapi.startXHelix(Particle.FLAME, Particle.LAVA, 20);
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
            @Override
            public void run() {
        		Point point = new Point(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
        		List<Point[]> loc = api.drawSlash(3.5, 2, player.getLocation().getYaw(), point, 
        				new ParticleData<Object>(Particle.FLAME, player.getWorld(), 0));
        		for (Point[] points : loc) {
        			for (Point p : points) {
        				Location l = p.getPointLocation(player);
        				for (Entity e : player.getWorld().getNearbyEntities(l, 2, 1, 2)) {
        					if (e != player) {
        						if (e instanceof LivingEntity) {
        							LivingEntity entity = (LivingEntity) e;
        							entity.damage(3, player);
        							entity.setFireTicks(20*3);
        							entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_BLAZE_BURN, 1, 1);
        						}
        					}
        				}
        			}
        		}
            }
        }, 10);
		cd.activateCooldown(player, 1, 5);
	}
	
	public void beastOne(Player player) { //strength
		if (wfo.containsKey(player)) {
			if (wfo.get(player)) {
				return;
			}
		}
		player.setVelocity(player.getLocation().getDirection().multiply(1.5));
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
            @Override
            public void run() {
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
            }
        },10);
		wfo.put(player, true);
        scheduler.scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
            @Override
            public void run() {
            	wfo.put(player, false);
            }
        }, 2*25);
	}
	
	Map<Player, Boolean> imap = new HashMap<Player, Boolean>();
	
	public void insectOne(Player player) {
		if (cd.isPlayerOnCooldown(player, 1)) {
			return;
		}
		final Vector vec = player.getLocation().getDirection();
		player.setVelocity(new Vector(vec.getX(), vec.getY()+2, vec.getZ()));
		BukkitScheduler scheduler = Bukkit.getScheduler();
		scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				imap.put(player, true);
				imap2.put(player, true);
			}
		}, 10);
		scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				imap.put(player, false);
			}
		}, 5*20);
		cd.activateCooldown(player, 1, 20);
	}
	
	Map<Player, Boolean> imap2 = new HashMap<Player, Boolean>();
	
	@EventHandler
	public void onInsectClick(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Vector vec = player.getLocation().getDirection();
		if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
			if (imap.containsKey(player) && imap2.containsKey(player)) {
				if (imap.get(player) == true && imap2.get(player) == true) {
					player.setVelocity(vec.multiply(2));
					imap2.put(player, false);
					BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
			        scheduler.scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
			            @Override
			            public void run() {
			            	imap2.put(player, true);
			            }
			        },25);
			        scheduler.scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
			            @Override
			            public void run() {
			            	ParticleAPI api = new ParticleAPI(plugin);
			        		Point point = new Point(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
			        		List<Point[]> loc = api.drawSlash(3.5, 2, player.getLocation().getYaw(), point,
			        						new ParticleData<Object>(Particle.REDSTONE, player.getWorld(), 0, 0, 0, 0, new Particle.DustOptions(Color.PURPLE, 2)));
			        		api.drawSlash(3.5, 2, player.getLocation().getYaw(), point, 
			        				new ParticleData<Object>(Particle.PORTAL, player.getWorld(), 0));
			        		api.drawSlash(3.5, 2, player.getLocation().getYaw(), point, 
			        				new ParticleData<Object>(Particle.FIREWORKS_SPARK, player.getWorld(), 0));
			        		for (Point[] points : loc) {
			        			for (Point p : points) {
			        				Location l = p.getPointLocation(player);
			        				for (Entity e : player.getWorld().getNearbyEntities(l, 2, 1, 2)) {
			        					if (e != player) {
			        						if (e instanceof LivingEntity) {
			        							LivingEntity entity = (LivingEntity) e;
			        							entity.damage(1, player);
			        							entity.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 3*20, 1));
			        							entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_BEE_STING, 1, 1);
			        						}
			        					}
			        				}
			        			}
			        		}
			            }
			        }, 10);
				}
			}
		}
	}
	
	public void sunOne(Player player) {
		if (wfo.containsKey(player)) {
			if (wfo.get(player)) {
				return;
			}
		}
		ParticleAPI api = new ParticleAPI(plugin);
		Particle.DustOptions dustOptions = new Particle.DustOptions(Color.RED, 1);
		Point point = new Point(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
		api.drawSlash(3.5, 3, player.getLocation().getYaw(), point, new ParticleData<Object>(Particle.REDSTONE, player.getWorld(), 0, 0, 0, 0, dustOptions));
		api.drawSlash(3.5, 3, player.getLocation().getYaw(), point, new ParticleData<Object>(Particle.LAVA, player.getWorld(), 0));
		api.drawSlash(3.5, 3, player.getLocation().getYaw(), point, new ParticleData<Object>(Particle.NAUTILUS, player.getWorld(), 0));
		for (Entity e : player.getWorld().getNearbyEntities(point.getPointLocation(player), 2, 1, 2)) {
			if (e != player) {
				if (e instanceof LivingEntity) {
					LivingEntity entity = (LivingEntity) e;
					entity.damage(3, player);
					entity.setFireTicks(5*20);
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
	
	public void soundOne(Player player) {
		if (wfo.containsKey(player)) {
			if (wfo.get(player)) {
				return;
			}
		}
		ParticleAPI api = new ParticleAPI(plugin);
		Point point = new Point(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
		api.drawSlash(3.5, 3, player.getLocation().getYaw(), point, new ParticleData<Object>(Particle.NOTE, player.getWorld(), 0));
		api.drawSlash(3.5, 3, player.getLocation().getYaw(), point, new ParticleData<Object>(Particle.FLASH, player.getWorld(), 0));
		for (Entity e : player.getWorld().getNearbyEntities(point.getPointLocation(player), 2, 1, 2)) {
			if (e != player) {
				if (e instanceof LivingEntity) {
					LivingEntity entity = (LivingEntity) e;
					entity.damage(3, player);
					entity.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 15, 1));
					entity.getWorld().playSound(entity.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_BREAK, 1, 1);
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

}
