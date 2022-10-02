package com.doxa.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import oshi.util.tuples.Pair;

public class OldParticleAPI {
	
	private Player player;
	public OldParticleAPI(Player pl) {
		player = pl;
	}
	
	/*
	 * YOU MUST USE A NEW INSTANCE OF THIS CLASS FOR EACH NEW PARTICLE/SHAPE YOU CREATE!!!
	 */
	
	private int id;
	
	public void stopTask(int i) {
		Bukkit.getScheduler().cancelTask(i);
	}

	public int createSingleHelix(Particle p) {
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), new Runnable() {
            Location loc = player.getLocation();
            double t = 0;
            double r = 2;
            public void run(){
                t = t + Math.PI/16;
                double x = r*Math.cos(t);
                double y = r*Math.sin(t);
                double z = r*Math.sin(t);
                loc.add(x, y, z);
                player.getWorld().spawnParticle(p, loc, 0, 0, 0, 0, 1);
                loc.subtract(x, y, z);
            }
        }, 0, 1);
		return id;
	}
	
	public int createMassWave(Particle p1, Particle p2, Color color) {
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), new Runnable() {
			double t = Math.PI/4;
			Location loc = player.getLocation();
			public void run(){
				t = t + 0.1*Math.PI;
				for (double theta = 0; theta <= 2*Math.PI; theta = theta + Math.PI/32){
					double x = t*Math.cos(theta);
					double y = 2*Math.exp(-0.1*t) * Math.sin(t) + 1.5;
					double z = t*Math.sin(theta);
					loc.add(x,y,z);
					Particle.DustOptions dustOptions = new Particle.DustOptions(color, 2);
					player.getWorld().spawnParticle(p1, loc, 0, 0, 0, 0, dustOptions);
					loc.subtract(x,y,z);
	
					theta = theta + Math.PI/64;
	
					x = t*Math.cos(theta);
					y = 2*Math.exp(-0.1*t) * Math.sin(t) + 1.5;
					z = t*Math.sin(theta);
					loc.add(x,y,z);
					player.getWorld().spawnParticle(p2, loc, 0);
					if (loc.getWorld().getNearbyEntities(loc, 1, 1, 1) != null) {
						for (Entity e : player.getWorld().getNearbyEntities(loc, 1, 1, 1)) {
							if (e != player) {
								if (e instanceof LivingEntity) {
									LivingEntity entity = (LivingEntity) e;
									entity.damage(3, player);
								}
							}
						}
					}
					loc.subtract(x,y,z);
				}
				if (t > 20){
					stopTask(id);
				}
			}
		}, 0, 1);
		return id;
	}
	
	Map<Player, Integer> tracked = new HashMap<Player, Integer>();
	
	public int getPlayerTrackedAmt() {
		if (tracked.containsKey(player)) {
			return tracked.get(player);
		} else {
			return 0;
		}
	}
	
	public int createMassWave2(Particle p1, Particle p2) {
		List<LivingEntity> entities = new ArrayList<LivingEntity>();
		tracked.remove(player);
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), new Runnable() {
			double t = Math.PI/4;
			Location loc = player.getLocation();
			public void run(){
				t = t + 0.1*Math.PI;
				for (double theta = 0; theta <= 2*Math.PI; theta = theta + Math.PI/32){
					double x = t*Math.cos(theta);
					double y = 2*Math.exp(-0.1*t) * Math.sin(t) + 1.5;
					double z = t*Math.sin(theta);
					loc.add(x,y,z);
					player.getWorld().spawnParticle(p1, loc, 0);
					loc.subtract(x,y,z);
	
					theta = theta + Math.PI/64;
	
					x = t*Math.cos(theta);
					y = 2*Math.exp(-0.1*t) * Math.sin(t) + 1.5;
					z = t*Math.sin(theta);
					loc.add(x,y,z);
					player.getWorld().spawnParticle(p2, loc, 0);
					if (loc.getWorld().getNearbyEntities(loc, 1, 1, 1) != null) {
						for (Entity e : player.getWorld().getNearbyEntities(loc, 1, 1, 1)) {
							if (e != player) {
								if (e instanceof LivingEntity) {
									final LivingEntity entity = (LivingEntity) e;
									if (!entities.contains(entity)) {
										entity.setGlowing(true);
										entities.add(entity);
										if (tracked.containsKey(player)) {
											tracked.put(player, (tracked.get(player)+1));
										} else {
											tracked.put(player, 1);
										}
										Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
											public void run() {
												entity.setGlowing(false);
											}
										},5*20);
									}
								}
							}
						}
					}
					loc.subtract(x,y,z);
				}
				if (t > 20){
					stopTask(id);
				}
			}
		}, 0, 1);
		return id;
	}
	
	public int createMassWave(Particle p1, Particle p2, Color color, Location loc) {
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), new Runnable() {
			double t = Math.PI/4;
			public void run(){
				t = t + 0.1*Math.PI;
				for (double theta = 0; theta <= 2*Math.PI; theta = theta + Math.PI/32){
					double x = t*Math.cos(theta);
					double y = 2*Math.exp(-0.1*t) * Math.sin(t) + 1.5;
					double z = t*Math.sin(theta);
					loc.add(x,y,z);
					Particle.DustOptions dustOptions = new Particle.DustOptions(color, 2);
					player.getWorld().spawnParticle(p1, loc, 0, 0, 0, 0, dustOptions);
					loc.subtract(x,y,z);
	
					theta = theta + Math.PI/64;
	
					x = t*Math.cos(theta);
					y = 2*Math.exp(-0.1*t) * Math.sin(t) + 1.5;
					z = t*Math.sin(theta);
					loc.add(x,y,z);
					player.getWorld().spawnParticle(p2, loc, 0);
					if (loc.getWorld().getNearbyEntities(loc, 1, 1, 1) != null) {
						for (Entity e : player.getWorld().getNearbyEntities(loc, 1, 1, 1)) {
							if (e != player) {
								if (e instanceof LivingEntity) {
									LivingEntity entity = (LivingEntity) e;
									entity.damage(3, player);
								}
							}
						}
					}
					loc.subtract(x,y,z);
				}
				if (t > 20){
					stopTask(id);
				}
			}
		}, 0, 1);
		return id;
	}
	
	public int createMassWave(Particle p1, Particle p2) {
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), new Runnable() {
			double t = Math.PI/4;
			Location loc = player.getLocation();
			public void run(){
				t = t + 0.1*Math.PI;
				for (double theta = 0; theta <= 2*Math.PI; theta = theta + Math.PI/32){
					double x = t*Math.cos(theta);
					double y = 2*Math.exp(-0.1*t) * Math.sin(t) + 1.5;
					double z = t*Math.sin(theta);
					loc.add(x,y,z);
					player.getWorld().spawnParticle(p1, loc, 0);
					loc.subtract(x,y,z);
	
					theta = theta + Math.PI/64;
	
					x = t*Math.cos(theta);
					y = 2*Math.exp(-0.1*t) * Math.sin(t) + 1.5;
					z = t*Math.sin(theta);
					loc.add(x,y,z);
					player.getWorld().spawnParticle(p2, loc, 0);
					if (loc.getWorld().getNearbyEntities(loc, 1, 1, 1) != null) {
						for (Entity e : player.getWorld().getNearbyEntities(loc, 1, 1, 1)) {
							if (e != player) {
								if (e instanceof LivingEntity) {
									LivingEntity entity = (LivingEntity) e;
									entity.damage(3, player);
									entity.setFireTicks(3*20);
								}
							}
						}
					}
					loc.subtract(x,y,z);
				}
				if (t > 20){
					stopTask(id);
				}
			}
		}, 0, 1);
		return id;
	}
	
	public int createMassWaveSound(Particle p1, Particle p2) {
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), new Runnable() {
			double t = Math.PI/4;
			Location loc = player.getLocation();
			public void run(){
				t = t + 0.1*Math.PI;
				for (double theta = 0; theta <= 2*Math.PI; theta = theta + Math.PI/32){
					double x = t*Math.cos(theta);
					double y = 2*Math.exp(-0.1*t) * Math.sin(t) + 1.5;
					double z = t*Math.sin(theta);
					loc.add(x,y,z);
					player.getWorld().spawnParticle(p1, loc, 0);
					loc.subtract(x,y,z);
	
					theta = theta + Math.PI/64;
	
					x = t*Math.cos(theta);
					y = 2*Math.exp(-0.1*t) * Math.sin(t) + 1.5;
					z = t*Math.sin(theta);
					loc.add(x,y,z);
					player.getWorld().spawnParticle(p2, loc, 0);
					if (loc.getWorld().getNearbyEntities(loc, 1, 1, 1) != null) {
						for (Entity e : player.getWorld().getNearbyEntities(loc, 1, 1, 1)) {
							if (e != player) {
								if (e instanceof LivingEntity) {
									LivingEntity entity = (LivingEntity) e;
									entity.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 25, 1));
									entity.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 6*20, 5));
									entity.damage(2, player);
								}
							}
						}
					}
					loc.subtract(x,y,z);
				}
				if (t > 20){
					stopTask(id);
				}
			}
		}, 0, 1);
		return id;
	}
	
	public void startXHelix(Color color1, Color color2, int time) {
		int taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), new Runnable() {
			double var = 0;
			Location loc, first, second;
			boolean swap;
			@Override
			public void run() {
					
					var += Math.random() / 12;
					
					loc = player.getLocation();
					first = loc.clone().add(Math.cos(var), Math.sin(var) + 1, Math.sin(var));
					second = loc.clone().add(Math.cos(var + Math.PI), Math.sin(var) + 1, Math.sin(var + Math.PI));
					Particle.DustOptions dustOptions;
					if (swap) {
						dustOptions = new Particle.DustOptions(color1, 1);
						swap = false;
					} else {
						dustOptions = new Particle.DustOptions(color2, 1);
						swap = true;
					}
					player.getWorld().spawnParticle(Particle.REDSTONE, first, 0, 0, 0, 0, dustOptions);
					player.getWorld().spawnParticle(Particle.REDSTONE, second, 0, 0, 0, 0, dustOptions);
				}
			
		}, 0, 1);
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
			@Override
			public void run() {
				Bukkit.getScheduler().cancelTask(taskID);
			}
		}, time);
	}
	
	public void startXHelix(Particle particle, Particle particle2, int time) {
		int taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), new Runnable() {
			double var = 0;
			Location loc, first, second;
			@Override
			public void run() {
					
					var += Math.random() / 12;
					
					loc = player.getLocation();
					first = loc.clone().add(Math.cos(var), Math.sin(var) + 1, Math.sin(var));
					second = loc.clone().add(Math.cos(var + Math.PI), Math.sin(var) + 1, Math.sin(var + Math.PI));
					player.getWorld().spawnParticle(particle, first, 0);
					player.getWorld().spawnParticle(particle2, second, 0);
				}
			
		}, 0, 1);
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
			@Override
			public void run() {
				Bukkit.getScheduler().cancelTask(taskID);
			}
		}, time);
	}
	
	public void startXHelixWithDamage(Color color1, Color color2, int time) {
		int taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), new Runnable() {
			double var = 0;
			Location loc, first, second;
			boolean swap;
			@Override
			public void run() {
					
					var += Math.random() / 12;
					
					loc = player.getLocation();
					first = loc.clone().add(Math.cos(var), Math.sin(var) + 1, Math.sin(var));
					second = loc.clone().add(Math.cos(var + Math.PI), Math.sin(var) + 1, Math.sin(var + Math.PI));
					Particle.DustOptions dustOptions;
					if (swap) {
						dustOptions = new Particle.DustOptions(color1, 4);
						swap = false;
					} else {
						dustOptions = new Particle.DustOptions(color2, 4);
						swap = true;
					}
					player.getWorld().spawnParticle(Particle.REDSTONE, first, 0, 0, 0, 0, dustOptions);
					player.getWorld().spawnParticle(Particle.REDSTONE, second, 0, 0, 0, 0, dustOptions);
					for (Entity e : player.getWorld().getNearbyEntities(first, 2, 1, 2)) {
						if (e instanceof LivingEntity) {
							if (e != player) {
								LivingEntity entity = (LivingEntity) e;
								entity.damage(3, player);
							}
						}
					}
					for (Entity e : player.getWorld().getNearbyEntities(second, 2, 1, 2)) {
						if (e instanceof LivingEntity) {
							if (e != player) {
								LivingEntity entity = (LivingEntity) e;
								entity.damage(3, player);
							}
						}
					}
				}
			
		}, 0, 1);
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
			@Override
			public void run() {
				Bukkit.getScheduler().cancelTask(taskID);
			}
		}, time);
	}
	
	public int createFullSphere(Particle p, Color color) {	
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), new Runnable() {
			double phi = 0;
			public void run(){
				Location loc = player.getLocation();
				phi += Math.PI/5;
				for (double theta = 0; theta <= 2*Math.PI; theta += Math.PI/20) {
					double r = 1.5;
					double x = r*Math.cos(theta) * Math.sin(phi);
					double y = r*Math.cos(phi) + 1.5;
					double z = r*Math.sin(theta) * Math.sin(phi);
					loc.add(x,y,z);
					Particle.DustOptions dustOptions = new Particle.DustOptions(color, 2);
					player.getWorld().spawnParticle(p, loc, 0, 0, 0, 0, dustOptions);
					for (Entity e : player.getWorld().getNearbyEntities(loc, 2, 1, 2)) {
						if (e instanceof LivingEntity) {
							if (e != player) {
								LivingEntity entity = (LivingEntity) e;
								entity.damage(3, player);
							}
						}
					}
					loc.subtract(x,y,z);
				}
			}
		}, 0, 1);
		return id;
	}
	
	public int createFullSphere(Particle p, int r) {	
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), new Runnable() {
			double phi = 0;
			public void run(){
				Location loc = player.getLocation();
				phi += Math.PI/5;
				for (double theta = 0; theta <= 2*Math.PI; theta += Math.PI/20) {
					double x = r*Math.cos(theta) * Math.sin(phi);
					double y = r*Math.cos(phi) + 1.5;
					double z = r*Math.sin(theta) * Math.sin(phi);
					loc.add(x,y,z);
					player.getWorld().spawnParticle(p, loc, 0);
					for (Entity e : player.getWorld().getNearbyEntities(loc, 8, 5, 8)) {
						if (e instanceof LivingEntity) {
							if (e != player) {
								LivingEntity entity = (LivingEntity) e;
								entity.damage(3, player);
								entity.setFireTicks(10*20);
							}
						}
					}
					loc.subtract(x,y,z);
				}
			}
		}, 0, 1);
		return id;
	}
	
	 public int createCircle(Particle p, Location loc, float radius){
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), new Runnable() {
			int i = 0;
			public void run(){
		        for(double t = 0; t<50; t+=0.5) {
		            float x = (radius*(float)Math.sin(t));
		            float z = (radius*(float)Math.cos(t));
		            loc.add(x, 1.5, z);
		            player.getWorld().spawnParticle(p, loc, 0, 0, 0, 0, 1);
					for (Entity e : player.getWorld().getNearbyEntities(loc, 2, 1, 2)) {
						if (e instanceof LivingEntity) {
							if (e != player) {
								LivingEntity entity = (LivingEntity) e;
								entity.damage(5, player);
							}
						}
					}
		            loc.subtract(x, 1.5, z);
		            i++;
		            if (i > 5) {
		            	stopTask(id);
		            }
		        }
			}
		}, 0, 1);
		return id;
	 }
	 
	 public int createCircleSound(Particle p, Location loc, float radius){
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), new Runnable() {
			int i = 0;
			public void run(){
		        for(double t = 0; t<50; t+=0.5) {
		            float x = (radius*(float)Math.sin(t));
		            float z = (radius*(float)Math.cos(t));
		            loc.add(x, 1.5, z);
		            player.getWorld().spawnParticle(p, loc, 0, 0, 0, 0, 1);
					for (Entity e : player.getWorld().getNearbyEntities(loc, 2, 1, 2)) {
						if (e instanceof LivingEntity) {
							if (e != player) {
								LivingEntity entity = (LivingEntity) e;
								entity.damage(5, player);
								entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 5*20, 2));
								entity.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 2*20, 2));
							}
						}
					}
		            loc.subtract(x, 1.5, z);
		            i++;
		            if (i > 5) {
		            	stopTask(id);
		            }
		        }
			}
		}, 0, 1);
		return id;
	 }
	 
	 public int createCircleFlame(Particle p, Location loc, float radius){
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), new Runnable() {
			int i = 0;
			public void run(){
		        for(double t = 0; t<50; t+=0.5) {
		            float x = (radius*(float)Math.sin(t));
		            float z = (radius*(float)Math.cos(t));
		            loc.add(x, 1.5, z);
		            player.getWorld().spawnParticle(p, loc, 0, 0, 0, 0, 1);
					for (Entity e : player.getWorld().getNearbyEntities(loc, 2, 1, 2)) {
						if (e instanceof LivingEntity) {
							if (e != player) {
								LivingEntity entity = (LivingEntity) e;
								entity.damage(6, player);
								entity.setFireTicks(3*10);
							}
						}
					}
		            loc.subtract(x, 1.5, z);
		            i++;
		            if (i > 5) {
		            	stopTask(id);
		            }
		        }
			}
		}, 0, 1);
		return id;
	 }
	 
	 public Location createSemiCircle(Particle p, Location loc, float radius){
		 	Location l = null;
	        for(double t = 0; t<25; t+=0.5) {
	            float x = radius*(float)Math.sin(t);
	            float z = radius*(float)Math.cos(t);
	            l = new Location(player.getWorld(), (float) loc.getX() + x, (float) loc.getY() + 1.5,(float) loc.getZ() + z);
	            player.getWorld().spawnParticle(p, l, 0, 0, 0, 0, 1);
	        }
	        return l;
	 }
	 
	 public void drawTornado(Particle p, Location loc, float radius, float increase){
	        float y = (float) loc.getY();
	        for(double t = 0; t<50; t+=0.05){
	            float x = radius*(float)Math.sin(t);
	            float z = radius*(float)Math.cos(t);
	            player.getWorld().spawnParticle(p, new Location(player.getWorld(), (float) loc.getX() + x, (float) y,(float) loc.getZ() + z), 0, 0, 0, 0, 1);
				for (Entity e : player.getWorld().getNearbyEntities(loc, 2, 1, 2)) {
					if (e instanceof LivingEntity) {
						if (e != player) {
							LivingEntity entity = (LivingEntity) e;
							entity.damage(5, player);
							entity.setFireTicks(5*20);
						}
					}
				}
	            y += 0.01f;
	            radius += increase;
	        }
	  }
	 
	 public void createVerticleCircle(Particle p, int particles, int radius, Color color) {
		 final Location loc = player.getLocation();
         for (int i = 0; i < particles; i++) {
             double angle;
             angle = 2 * Math.PI * i / particles;
             Vector offset = loc.getDirection().clone().multiply(Math.cos(angle) * radius);
             offset.setY(Math.sin(angle) * radius);
             loc.add(offset);
             Particle.DustOptions dustOptions = new Particle.DustOptions(color, 2);
			 player.getWorld().spawnParticle(p, loc, 0, 0, 0, 0, dustOptions);
             loc.subtract(offset);
         }
	 }
	 
	 public int createHelix(Particle p, Player player, Color color) {
		 id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), new Runnable() {
		 double phi = 0;
			public void run(){
				phi = phi + Math.PI/8;					
				double x, y, z;			

				Location location1 = player.getLocation();
				for (double t = 0; t <= 2*Math.PI; t = t + Math.PI/16){
					for (double i = 0; i <= 1; i = i + 1){
						x = 0.4*(2*Math.PI-t)*0.5*Math.cos(t + phi + i*Math.PI);
						y = 0.5*t;
						z = 0.4*(2*Math.PI-t)*0.5*Math.sin(t + phi + i*Math.PI);
						location1.add(x, y, z);
						Particle.DustOptions dustOptions = new Particle.DustOptions(color, 2);
						player.getWorld().spawnParticle(p, location1, 0, 0, 0, 0, dustOptions);
						location1.subtract(x,y,z);
					}

				}
			}	
		 }, 0, 1);
		 return id;
	 }
	 
	 public int createHelix(Particle p, Player player) {
		 id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), new Runnable() {
		 double phi = 0;
			public void run(){
				phi = phi + Math.PI/8;					
				double x, y, z;			

				Location location1 = player.getLocation();
				for (double t = 0; t <= 2*Math.PI; t = t + Math.PI/16){
					for (double i = 0; i <= 1; i = i + 1){
						x = 0.4*(2*Math.PI-t)*0.5*Math.cos(t + phi + i*Math.PI);
						y = 0.5*t;
						z = 0.4*(2*Math.PI-t)*0.5*Math.sin(t + phi + i*Math.PI);
						location1.add(x, y, z);
						player.getWorld().spawnParticle(p, location1, 0);
						location1.subtract(x,y,z);
					}

				}
			}	
		 }, 0, 1);
		 return id;
	 }
	 
	 public List<Location> createCircleSized(Player p, Particle particle, float size, Color color) {
         Location location1 = p.getEyeLocation();
         Location location2 = p.getEyeLocation();
         Location location3 = p.getEyeLocation();
         List<Location> locs = new ArrayList<Location>();
         int particles = 50;
         float radius = size;
         for (int i = 0; i < particles; i++) {
             double angle, x, z;
             angle = 2 * Math.PI * i / particles;
             x = Math.cos(angle) * radius;
             z = Math.sin(angle) * radius;
             location1.add(x, 0, z);
             location2.add(x, -0.66, z);
             location3.add(x, -1.33, z);
             if (particle != Particle.REDSTONE) {
	             p.getWorld().spawnParticle(particle, location1, 0, 0, 0, 0, 1);
	             p.getWorld().spawnParticle(particle, location2, 0, 0, 0, 0, 1);
	             p.getWorld().spawnParticle(particle, location3, 0, 0, 0, 0, 1);
             } else {
	             p.getWorld().spawnParticle(particle, location1, 0, 0, 0, 0, new Particle.DustOptions(color, 1));
	             p.getWorld().spawnParticle(particle, location2, 0, 0, 0, 0, new Particle.DustOptions(color, 1));
	             p.getWorld().spawnParticle(particle, location3, 0, 0, 0, 0, new Particle.DustOptions(color, 1));
             }
             location1.subtract(x, 0, z);
             location2.subtract(x, -0.66, z);
             location3.subtract(x, -1.33, z);
             locs.add(location1);
             locs.add(location2);
             locs.add(location3);
         }
         return locs;
	 }
	 
     public Vector rotateAroundAxisX(Vector v, double cos, double sin) {
         double y = v.getY() * cos - v.getZ() * sin;
         double z = v.getY() * sin + v.getZ() * cos;
         return v.setY(y).setZ(z);
     }

     public Vector rotateAroundAxisY(Vector v, double cos, double sin) {
         double x = v.getX() * cos + v.getZ() * sin;
         double z = v.getX() * -sin + v.getZ() * cos;
         return v.setX(x).setZ(z);
     }

     public Vector rotateAroundAxisZ(Vector v, double cos, double sin) {
         double x = v.getX() * cos - v.getY() * sin;
         double y = v.getX() * sin + v.getY() * cos;
         return v.setX(x).setY(y);
     }
     
     public Vector getDirectionBetweenLocations(Location Start, Location End) {
         Vector from = Start.toVector();
         Vector to = End.toVector();
         return to.subtract(from);
     }
     
     public void createPointedLine(Location point1, Location point2, Particle particle) {
    	    World world = point1.getWorld();
    	    Validate.isTrue(point2.getWorld().equals(world), "Lines cannot be in different worlds!");
    	    double distance = point1.distance(point2);
    	    Vector p1 = point1.toVector();
    	    Vector p2 = point2.toVector();
    	    Vector vector = p2.clone().subtract(p1).normalize().multiply(1);
    	    double length = 0;
    	    for (; length < distance; p1.add(vector)) {
    	        world.spawnParticle(particle, p1.getX(), p1.getY(), p1.getZ(), 0);
    	        length += 1;
    	    }
    	}
     
     public Pair<Block, Block> getLeftAndRightBlocks(Location midpoint) {
    	 	Block RelativeBlock = midpoint.getBlock();
    	    double Yaw = Math.toRadians(midpoint.getYaw());

    	    double lx = RelativeBlock.getLocation().getX() + (Yaw * 1.0);
    	    double lz = RelativeBlock.getLocation().getZ() + (Yaw * 1.0);

    	    double rx = RelativeBlock.getLocation().getX() + (Yaw * -1.0);
    	    double rz = RelativeBlock.getLocation().getZ() + (Yaw * -1.0);

    	    double y = RelativeBlock.getLocation().getY();

    	    Block LeftBlock, RightBlock;

    	    LeftBlock = RelativeBlock.getRelative((int) lx, (int) y, (int) lz);
    	    RightBlock = RelativeBlock.getRelative((int) rx, (int) y, (int) rz);
    	 
    	    return new Pair<Block, Block>(LeftBlock, RightBlock);
    	}
     
     public void createLightning(Player player, Particle particle) {
    	 final Vector vec = player.getLocation().getDirection();
    	 final Location playerloc = new Location(player.getLocation().getWorld(), player.getLocation().getX(), player.getLocation().getY() + 1.5,
    			 player.getLocation().getZ());
    	 Location end = playerloc.add(vec.add(vec));
    	 Location first = this.getLeftAndRightBlocks(playerloc.add(vec)).getB().getLocation();
    	 this.createPointedLine(playerloc, first, particle);
    	 Location second = this.getLeftAndRightBlocks(this.getLeftAndRightBlocks(first.add(vec)).getA().getLocation()).getA().getLocation();
    	 this.createPointedLine(first, second, particle);
    	 Location third = this.getLeftAndRightBlocks(this.getLeftAndRightBlocks(second.add(vec)).getB().getLocation()).getB().getLocation();
    	 this.createPointedLine(second, third, particle);
    	 Location fourth = this.getLeftAndRightBlocks(this.getLeftAndRightBlocks(third.add(vec)).getA().getLocation()).getA().getLocation();
    	 this.createPointedLine(third, fourth, particle);
    	 this.createPointedLine(fourth, end, particle);
    	 List<Location> locs = new ArrayList<Location>();
    	 locs.add(first);
    	 locs.add(second);
    	 locs.add(third);
    	 locs.add(fourth);
    	 locs.add(end);
    	 for (Location point : locs) {
	 		 for (Entity e : player.getWorld().getNearbyEntities(point, 2, 1, 2)) {
				if (e != player) {
					if (e instanceof LivingEntity) {
						LivingEntity entity = (LivingEntity) e;
						entity.damage(5, player);
						entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 1, 1);
					}
				}
			 }
    	 }
     }
     
     Map<Player, Double> map = new HashMap<Player, Double>();
     Map<Player, List<Integer>> ids = new HashMap<Player, List<Integer>>();
 	
 	 public int createWaterfall(Player player, int delay, Particle particle, Color color) {
 		 Location loc = player.getLocation();
 		 BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
          scheduler.scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
              @Override
              public void run() {
                	map.put(player, player.getLocation().getY());
              }
          },delay*20);
          int id = scheduler.scheduleSyncRepeatingTask(Main.getPlugin(Main.class), new Runnable() {
         	double x = player.getLocation().getX();
     		double z = player.getLocation().getZ();
             @Override
             public void run() {
             	double y = map.get(player);
             	double new_y = y-0.6;
             	map.put(player, new_y);
             	Location new_loc = new Location(loc.getWorld(), x, new_y, z);
             	Location new_loc2 = new Location(loc.getWorld(), x + 0.3, new_y, z + 0.3);
             	Location new_loc3 = new Location(loc.getWorld(), x - 0.3, new_y, z - 0.3);
             	Location new_loc4 = new Location(loc.getWorld(), x + 0.3, new_y, z - 0.3);
             	Location new_loc5 = new Location(loc.getWorld(), x - 0.3, new_y, z + 0.3);            	
             	Location new_loc6 = new Location(loc.getWorld(), x + 0.6, new_y, z + 0.6);
             	Location new_loc7 = new Location(loc.getWorld(), x - 0.6, new_y, z - 0.6);
             	Location new_loc8 = new Location(loc.getWorld(), x + 0.6, new_y, z - 0.6);
             	Location new_loc9 = new Location(loc.getWorld(), x - 0.6, new_y, z + 0.6);
             	List<Location> locs = new ArrayList<Location>();
             	locs.add(new_loc);
             	locs.add(new_loc2);
             	locs.add(new_loc3);
             	locs.add(new_loc4);
             	locs.add(new_loc5);
             	locs.add(new_loc6);
             	locs.add(new_loc7);
             	locs.add(new_loc8);
             	locs.add(new_loc9);
             	int n = 0;
             	if (new_loc.getBlock().getType() != Material.AIR && new_loc.getBlock().getType() != null) {
             		if (ids.containsKey(player) && n < 1) {
             			for (int s : ids.get(player))  {
             				Bukkit.getScheduler().cancelTask(s);
             			}
             			int i = createMassWave(Particle.REDSTONE, Particle.DRIP_WATER, Color.WHITE, new_loc);
             	        scheduler.scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
             	            @Override
             	            public void run() {
             	            	Bukkit.getScheduler().cancelTask(i);
             	            }
             	        },15);
             	        n++;
             		}
             	}
             	for (Location l : locs) {
                 	if (particle == Particle.REDSTONE) {
                 		Particle.DustOptions dustOptions;
                 		dustOptions = new Particle.DustOptions(color, 20);
                 		new_loc.getWorld().spawnParticle(particle, l, 0, 0, 0, 0, dustOptions);
                 	} else {
                 		new_loc.getWorld().spawnParticle(particle, l, 0);
                 	}
 					for (Entity e : player.getWorld().getNearbyEntities(l, 2, 1, 2)) {
 						if (e != player) {
 							if (e instanceof LivingEntity) {
 								LivingEntity entity = (LivingEntity) e;
 								entity.damage(5, player);
 								entity.getWorld().playSound(entity.getLocation(), Sound.AMBIENT_UNDERWATER_ENTER, 1, 1);
 							}
 						}
 					}
             	}
             }
         }, delay*20, 1);
         List<Integer> list;
         if (ids.containsKey(player)) {
        	 list = ids.get(player);
         } else {
        	 list = new ArrayList<Integer>();
         }
         list.add(id);
         ids.put(player, list);
         return id;
 	}
 	
 	public int createWaterfallNoWake(Player player, int delay, Particle particle, Color color) {
 		Location loc = player.getLocation();
 		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
         scheduler.scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
             @Override
             public void run() {
             	map.put(player, player.getLocation().getY());
             }
         },delay*20);
         int id = scheduler.scheduleSyncRepeatingTask(Main.getPlugin(Main.class), new Runnable() {
         	double x = player.getLocation().getX();
     		double z = player.getLocation().getZ();
             @Override
             public void run() {
             	double y = map.get(player);
             	double new_y = y-0.5;
             	map.put(player, new_y);
             	Location new_loc = new Location(loc.getWorld(), x, new_y, z);
             	Location new_loc2 = new Location(loc.getWorld(), x + 0.3, new_y, z + 0.3);
             	Location new_loc3 = new Location(loc.getWorld(), x - 0.3, new_y, z - 0.3);
             	Location new_loc4 = new Location(loc.getWorld(), x + 0.3, new_y, z - 0.3);
             	Location new_loc5 = new Location(loc.getWorld(), x - 0.3, new_y, z + 0.3);            	
             	Location new_loc6 = new Location(loc.getWorld(), x + 0.6, new_y, z + 0.6);
             	Location new_loc7 = new Location(loc.getWorld(), x - 0.6, new_y, z - 0.6);
             	Location new_loc8 = new Location(loc.getWorld(), x + 0.6, new_y, z - 0.6);
             	Location new_loc9 = new Location(loc.getWorld(), x - 0.6, new_y, z + 0.6);
             	List<Location> locs = new ArrayList<Location>();
             	locs.add(new_loc);
             	locs.add(new_loc2);
             	locs.add(new_loc3);
             	locs.add(new_loc4);
             	locs.add(new_loc5);
             	locs.add(new_loc6);
             	locs.add(new_loc7);
             	locs.add(new_loc8);
             	locs.add(new_loc9);
             	for (Location l : locs) {
                 	if (particle == Particle.REDSTONE) {
                 		Particle.DustOptions dustOptions;
                 		dustOptions = new Particle.DustOptions(color, 20);
                 		new_loc.getWorld().spawnParticle(particle, l, 0, 0, 0, 0, dustOptions);
                 	} else {
                 		new_loc.getWorld().spawnParticle(particle, l, 0);
                 	}
 					for (Entity e : player.getWorld().getNearbyEntities(l, 2, 1, 2)) {
 						if (e != player) {
 							if (e instanceof LivingEntity) {
 								LivingEntity entity = (LivingEntity) e;
 								entity.damage(5, player);
 								entity.getWorld().playSound(entity.getLocation(), Sound.AMBIENT_UNDERWATER_ENTER, 1, 1);
 							}
 						}
 					}
             	}
             }
         }, delay*20, 1);
         List<Integer> list;
         if (ids.containsKey(player)) {
        	 list = ids.get(player);
         } else {
        	 list = new ArrayList<Integer>();
         }
         list.add(id);
         ids.put(player, list);
         return id;
 	}
     
}
