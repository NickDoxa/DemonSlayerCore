package com.doxa.core.breath.forms;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import com.doxa.core.Main;
import com.doxa.core.OldParticleAPI;
import com.doxa.core.particles.ParticleAPI;
import com.doxa.core.particles.ParticleData;
import com.doxa.core.particles.Point;

public class Seven implements Listener {

	Main plugin;
	Cooldowns cd;
	public Seven(Main main) {
		plugin = main;
		cd = new Cooldowns();
	}
	
	public void oldWaterSeven(Player player) { //defense
		ParticleAPI api = new ParticleAPI(plugin);
		if (cd.isPlayerOnCooldown(player, 7)) {
			return;}
		Particle.DustOptions dustOptions = new Particle.DustOptions(Color.BLUE, 1);
		Particle.DustOptions dustOptions2 = new Particle.DustOptions(Color.AQUA, 1);
		Point point = new Point(player.getLocation().getX(), player.getLocation().getY() + 1.5, player.getLocation().getZ());
		api.drawHelix(point, 2, 5, 5, 1, player.getLocation().getYaw(), new ParticleData<Object>(Particle.REDSTONE, player.getWorld(), 0, 0, 0, 0, dustOptions));
		api.drawHelix(point, 2, 5, 5, 1, player.getLocation().getYaw(), new ParticleData<Object>(Particle.REDSTONE, player.getWorld(), 0, 0, 0, 0, dustOptions2));
		api.drawHelix(point, 2, 5, 5, 1, player.getLocation().getYaw(), new ParticleData<Object>(Particle.WATER_SPLASH, player.getWorld(), 0));
		player.getWorld().playSound(player.getLocation(), Sound.AMBIENT_UNDERWATER_ENTER, 1, 1);
		for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 4, 2, 4)) {
			if (e != player) {
				if (e instanceof LivingEntity) {
					LivingEntity entity = (LivingEntity) e;
					entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10*20, 4));
					entity.setVelocity(entity.getLocation().getDirection().multiply(-2));
				}
			}
		}
		cd.activateCooldown(player, 7, 15);
	}
	
	@EventHandler
	public void onArmorTake(PlayerInteractAtEntityEvent event) {
		if (event.getRightClicked() instanceof ArmorStand) {
			ArmorStand as = (ArmorStand) event.getRightClicked();
			if (aslist.contains(as)) {
				event.setCancelled(true);
				event.getPlayer().sendMessage(ChatColor.RED + "You cannot take this sword!");
			}
		}
	}
	
	List<ArmorStand> aslist = new ArrayList<ArmorStand>();
	
	@SuppressWarnings("deprecation")
	public void waterSeven(Player player) {
		if (cd.isPlayerOnCooldown(player, 7)) {
			return;}
		//CREATE CIRCLES
		ParticleAPI api = new ParticleAPI(plugin);
		Particle.DustOptions dustOptions = new Particle.DustOptions(Color.BLUE, 1);
		Particle.DustOptions dustOptions2 = new Particle.DustOptions(Color.AQUA, 1);
		final Location loc2 = player.getLocation().add(player.getLocation().getDirection());
		final Location loc1 = loc2.add(player.getLocation().getDirection());
		final Location loc = loc1.add(player.getLocation().getDirection());
		Location lox = 
				new Location(loc.getWorld(), loc.getX(), loc.getY() + 2, loc.getZ(), loc.getYaw() + 90, 0);
		BukkitScheduler scheduler = Bukkit.getScheduler();
		scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				//FIRST RING
				api.drawVerticalCircle2(lox, new ParticleData<Object>(Particle.REDSTONE, player.getWorld(), 0, 0, 0, 0, dustOptions), 2, 1, 1, 1);
				api.drawVerticalCircle2(lox, new ParticleData<Object>(Particle.REDSTONE, player.getWorld(), 0, 0, 0, 0, dustOptions2), 2, 1, 1, 1);
			}
		}, 2);
		scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				//SECOND RING
				api.drawVerticalCircle2(lox, new ParticleData<Object>(Particle.REDSTONE, player.getWorld(), 0, 0, 0, 0, dustOptions), 2, 2, 1, 1);
				api.drawVerticalCircle2(lox, new ParticleData<Object>(Particle.REDSTONE, player.getWorld(), 0, 0, 0, 0, dustOptions2), 2, 2, 1, 1);
			}
		}, 4);
		scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				//THIRD RING
				api.drawVerticalCircle2(lox, new ParticleData<Object>(Particle.REDSTONE, player.getWorld(), 0, 0, 0, 0, dustOptions), 2, 3, 1, 1);
				api.drawVerticalCircle2(lox, new ParticleData<Object>(Particle.REDSTONE, player.getWorld(), 0, 0, 0, 0, dustOptions2), 2, 3, 1, 1);
			}
		}, 6);
		scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				//FOURTH RING
				api.drawVerticalCircle2(lox, new ParticleData<Object>(Particle.REDSTONE, player.getWorld(), 0, 0, 0, 0, dustOptions), 2, 4, 1, 1);
				api.drawVerticalCircle2(lox, new ParticleData<Object>(Particle.REDSTONE, player.getWorld(), 0, 0, 0, 0, dustOptions2), 2, 4, 1, 1);
			}
		}, 8);
		scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				//LAST RING
				api.drawVerticalCircle2(lox, new ParticleData<Object>(Particle.REDSTONE, player.getWorld(), 0, 0, 0, 0, dustOptions), 2, 5, 1, 1);
				api.drawVerticalCircle2(lox, new ParticleData<Object>(Particle.REDSTONE, player.getWorld(), 0, 0, 0, 0, dustOptions2), 2, 5, 1, 1);
			}
		}, 10);
		//SPAWN SWORD!
		Location ploc = player.getLocation().add(player.getLocation().getDirection());
		Location asloc = 
				new Location(ploc.getWorld(), ploc.getX(), ploc.getY() + 1.5, ploc.getZ(), loc.getYaw(), loc.getPitch());
		final ArmorStand as = (ArmorStand) player.getWorld().spawnEntity(asloc, EntityType.ARMOR_STAND);
		as.setInvisible(true);
		as.setInvulnerable(true);
		as.setArms(true);
		as.setItemInHand(player.getInventory().getItemInMainHand());
		as.setRightArmPose(new EulerAngle(0,0,0));
		aslist.add(as);
		//MAKE EVERYTHING SLOW AND DAMAGE
		for (Entity e : loc.getWorld().getNearbyEntities(player.getLocation(), 4, 2, 4)) {
			if (e != player) {
				if (e instanceof LivingEntity) {
					LivingEntity entity = (LivingEntity) e;
					entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10*20, 4));
					entity.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 15, 1));
				}
			}
		}
		scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				//REMOVE ARMOR STAND
				as.remove();
			}
		}, 35);
		cd.activateCooldown(player, 7, 15);
	}
	
	public void demonSeven(Player player) { //strength
		ParticleAPI api = new ParticleAPI(plugin);
		if (cd.isPlayerOnCooldown(player, 7)) {
			return;}
		Particle.DustOptions dustOptions = new Particle.DustOptions(Color.WHITE, 2);
		Point point = new Point(player.getLocation().getX(), player.getLocation().getY() + 1.5, player.getLocation().getZ());
		List<Point[]> loc = api.drawLine(1, 20, point, player.getLocation().getDirection(), new ParticleData<Object>(Particle.REDSTONE, player.getWorld(), 0, 0, 0, 0, dustOptions));
		api.drawLine(1, 20, point, player.getLocation().getDirection(), new ParticleData<Object>(Particle.FIREWORKS_SPARK, player.getWorld(), 0));
		for (Point[] points : loc) {
			for (Point p : points) {
				Location l = p.getPointLocation(player);
				for (Entity e : player.getWorld().getNearbyEntities(l, 1, 1, 1)) {
					if (e != player) {
						if (e instanceof LivingEntity) {
							LivingEntity entity = (LivingEntity) e;
							entity.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 5*20, 1));
						    final Block block = entity.getLocation().getBlock();
						    final Block block2 = entity.getLocation().getBlock().getRelative(BlockFace.UP);
						    if (block.getType() == Material.AIR && block2.getType() == Material.AIR) {
							    block.setType(Material.COBWEB);
							    block2.setType(Material.COBWEB);
								BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
						        scheduler.scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
						            @Override
						            public void run() {
						            	block.setType(Material.AIR);
						            	block2.setType(Material.AIR);
						            }
						        },5*20);
						    } else {
						    	entity.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, entity.getLocation(), 0);
						    }
						}
					}
				}
			}
		}
		cd.activateCooldown(player, 7, 10);
	}
	
	public void thunderSeven(Player player) { //strength
		if (cd.isPlayerOnCooldown(player, 7)) {
			return;}
		ParticleAPI api = new ParticleAPI(plugin);
		OldParticleAPI oapi = new OldParticleAPI(player);
		oapi.startXHelix(Color.YELLOW, Color.WHITE, 1*20);
		Point point = new Point(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
		this.createLightning(player);
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
            @Override
            public void run() {
				player.setVelocity(new Vector(player.getLocation().getDirection().getX(), 0, player.getLocation().getDirection().getZ()).multiply(4));
				List<Point[]> loc = api.drawHelix(point, 1, 20, 5, 1, player.getLocation().getYaw(), new ParticleData<Object>(Particle.REDSTONE, player.getWorld(), 0, 0, 0, 0, 
						new Particle.DustOptions(Color.YELLOW, 2)));
				api.drawHelix(point, 1, 20, 5, 1, player.getLocation().getYaw(), new ParticleData<Object>(Particle.REDSTONE, player.getWorld(), 0, 0, 0, 0, 
						new Particle.DustOptions(Color.AQUA, 2)));
				api.drawHelix(point, 1, 20, 5, 1, player.getLocation().getYaw(), new ParticleData<Object>(Particle.ELECTRIC_SPARK, player.getWorld(), 0));
				for (Point[] points : loc) {
					for (Point p : points) {
						Location l = p.getPointLocation(player);
						for (Entity e : player.getWorld().getNearbyEntities(l, 2, 1, 2)) {
							if (e != player) {
								if (e instanceof LivingEntity) {
									LivingEntity entity = (LivingEntity) e;
									entity.damage(3, player);
									entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1, 1);
								}
							}
						}
					}
				}
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
		        							entity.damage(10, player);
		        						}
		        					}
		        				}
		        			}
		        		}
		            }
		        },10);
            }
        },1*20);
		cd.activateCooldown(player, 7, 30);
	}
	
	public void createLightning(Player player) {
		Location loc = player.getLocation();
		Location loc1 = new Location(loc.getWorld(), loc.getX() + 5, loc.getY(), loc.getZ() + 5);
		Location loc2 = new Location(loc.getWorld(), loc.getX() - 5, loc.getY(), loc.getZ() - 5);
		Location loc3 = new Location(loc.getWorld(), loc.getX() - 5, loc.getY(), loc.getZ() + 5);
		Location loc4 = new Location(loc.getWorld(), loc.getX() + 5, loc.getY(), loc.getZ() - 5);
		List<Location> locs = new ArrayList<Location>();
		locs.add(loc1);
		locs.add(loc2);
		locs.add(loc3);
		locs.add(loc4);
		for (Location l : locs) {
			player.getWorld().strikeLightning(l);
		}
	}
	
	public void beastSeven(Player player) {
		if (cd.isPlayerOnCooldown(player, 7))
			return;
		int id = this.createFloatingSword(player);
		player.playSound(player.getLocation(), Sound.ENTITY_PIGLIN_BRUTE_ANGRY, 1, 1);
		BukkitScheduler scheduler = Bukkit.getScheduler();
		scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				scheduler.cancelTask(id);
			}
		},30);
		cd.activateCooldown(player, 7, 10);
	}
	
	@SuppressWarnings("deprecation")
	public int createFloatingSword(Player player) {
		ArmorStand as = (ArmorStand) player.getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);
		as.setInvisible(true);
		as.setInvulnerable(true);
		as.setGravity(false);
		as.setItemInHand(player.getInventory().getItemInMainHand());
		final Vector velocity = player.getLocation().getDirection();
		BukkitScheduler scheduler = Bukkit.getScheduler();
		int id = scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
			@Override
			public void run() {
				Location newloc = as.getLocation().add(velocity);
				as.teleport(newloc);
				for (Entity e : player.getWorld().getNearbyEntities(as.getLocation(), 2, 1, 2)) {
					if (e != player) {
						if (e instanceof LivingEntity) {
							LivingEntity entity = (LivingEntity) e;
							entity.damage(5, player);
						}
					}
				}
			}
		},1,1);
		scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				as.remove();
			}
		},30);
		return id;
	}
	
}
