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
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
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

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class Five {

	Main plugin;
	Cooldowns cd;
	public Five(Main main) {
		plugin = main;
		cd = new Cooldowns();
	}
	
	public void waterFive(Player player) { //proficiency
		if (cd.isPlayerOnCooldown(player, 5)) {
			return;}
		ParticleAPI api = new ParticleAPI(plugin);
		Particle.DustOptions dustOptions = new Particle.DustOptions(Color.BLUE, 1);
		Particle.DustOptions dustOptions2 = new Particle.DustOptions(Color.AQUA, 1);
		Point point = new Point(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
		api.drawSlash(3.5, 1, player.getLocation().getYaw(), point, new ParticleData<Object>(Particle.REDSTONE, player.getWorld(), 0, 0, 0, 0, dustOptions));
		api.drawSlash(3.5, 1, player.getLocation().getYaw(), point, new ParticleData<Object>(Particle.REDSTONE, player.getWorld(), 0, 0, 0, 0, dustOptions2));
		api.drawSlash(3.5, 1, player.getLocation().getYaw(), point, new ParticleData<Object>(Particle.WATER_SPLASH, player.getWorld(), 0));
		player.getWorld().playSound(player.getLocation(), Sound.AMBIENT_UNDERWATER_ENTER, 1, 1);
		for (Entity e : player.getWorld().getNearbyEntities(point.getPointLocation(player), 2, 1, 2)) {
			if (e != player) {
				if (e instanceof LivingEntity) {
					LivingEntity entity = (LivingEntity) e;
					entity.damage(5, player);
				}
			}
		}
		player.setVelocity(player.getLocation().getDirection().multiply(2));
		for(int i=0;i<50;i++) {
			this.randomRain(player, Particle.WATER_DROP);
		}
		cd.activateCooldown(player, 5, 8);
	}
	
	public void randomRain(Player player, Particle p) {
		double i = Math.random();
		double a = Math.random();
		Location loc = player.getLocation();
		double x = loc.getX();
		double y = loc.getY() + 3.5;
		double z = loc.getZ();
		if (i < 0.2) {
			x = x + 2;
			y = y + 1;
		} else if (i > 0.2 && i < 0.4) {
			x = x - 2;
		} else if (i > 0.4 && i < 0.6) {
			x = x + 3;
			y = y + 2;
		} else if (i > 0.6 && i < 0.9) {
			x = x - 1;
		} else {
			x = x - 3;
		}
		if (a < 0.2) {
			z = z + 2;
			y = y + 1;
		} else if (a > 0.2 && a < 0.4) {
			z = z - 2;
		} else if (a > 0.4 && a < 0.6) {
			z = z + 3;
			y = y + 2;
		} else if (a > 0.6 && a < 0.9) {
			z = z - 1;
		} else {
			z = z - 3;
		}
		player.getWorld().spawnParticle(p, new Location(player.getWorld(), x, y, z), 3);
	}
	
	/*
	 * DEMON
	 */
	
	@SuppressWarnings("deprecation")
	public void demonFive(Player player) { //proficiency
		if (cd.isPlayerOnCooldown(player, 5)) {
			return;}
		OldParticleAPI api = new OldParticleAPI(player);
		int i = api.createHelix(Particle.REDSTONE, player, Color.PURPLE);
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, 
				new TextComponent(ChatColor.DARK_PURPLE + "Demon Strength Activated!"));
        player.setMaxHealth(60);
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 30*20, 2));
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
            @Override
            public void run() {
            	Bukkit.getScheduler().cancelTask(i);
            }
        },1*20);
        scheduler.scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
            @Override
            public void run() {
            	player.setMaxHealth(40);
            	player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
            }
        },30*20);
        cd.activateCooldown(player, 5, 45);
	}
	
	/*
	 * THUNDER
	 */
	
	List<Arrow> arrows = new ArrayList<Arrow>();
	
	public void thunderFive(Player player) { //strength
		ParticleAPI api = new ParticleAPI(plugin);
		if (cd.isPlayerOnCooldown(player, 5)) {
			return;}
		Particle.DustOptions dustOptions = new Particle.DustOptions(Color.AQUA, 2);
		Particle.DustOptions dustOptions2 = new Particle.DustOptions(Color.YELLOW, 2);
		Point point = new Point(player.getLocation().getX(), player.getLocation().getY() + 1.5, player.getLocation().getZ());
		api.drawLine(1, 20, point, player.getLocation().getDirection(), new ParticleData<Object>(Particle.REDSTONE, player.getWorld(), 0, 0, 0, 0, dustOptions));
		api.drawLine(1, 20, point, player.getLocation().getDirection(), new ParticleData<Object>(Particle.REDSTONE, player.getWorld(), 0, 0, 0, 0, dustOptions2));
		api.drawLine(1, 20, point, player.getLocation().getDirection(), new ParticleData<Object>(Particle.ELECTRIC_SPARK, player.getWorld(), 0));
		player.getWorld().playSound(player.getLocation(), Sound.ENTITY_SILVERFISH_DEATH, 1, 1);
		Vector vec = player.getLocation().getDirection();
		Arrow arrow = (Arrow) player.launchProjectile(Arrow.class);
		//TODO FIND WAY TO MAKE INVISIBLE
        arrow.setBounce(false);
        arrow.setGravity(false);
        arrow.setSilent(true);
        arrow.setVelocity(vec.multiply(5));
        arrow.setShooter((ProjectileSource) ((LivingEntity) player));
        arrows.add(arrow);
		cd.activateCooldown(player, 5, 10);
	}
	
	@EventHandler
	public void onDmg(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player)
			return;
		if (event.getDamager() instanceof Arrow) {
			Arrow arrow = (Arrow) event.getDamager();
			if (arrows.contains(arrow)) {
				event.setDamage(5);
			}
		}
	}
	
	@EventHandler
	public void onHit(ProjectileHitEvent event) {
		Projectile p = event.getEntity();
		if (p instanceof Arrow) {
			if (arrows.contains(p)) {
				Arrow arrow = (Arrow) p;
				arrows.remove(arrow);
				arrow.remove();
			}
		}
	}
	
	/*
	 * FLAME
	 */
	
	public void flameFive(Player player) { //defense
		if (cd.isPlayerOnCooldown(player, 5))
			return;
		ParticleAPI api = new ParticleAPI(plugin);
		Particle.DustOptions dustOptions = new Particle.DustOptions(Color.YELLOW, 2);
		Particle.DustOptions dustOptions2 = new Particle.DustOptions(Color.RED, 2);
		Point point1 = new Point(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
		Point point = point1.add(player.getLocation().getDirection());
		List<Point[]> loc = api.drawHelix(point, 1, 15, 5, 1, player.getLocation().getYaw(), new ParticleData<Object>(Particle.REDSTONE, player.getWorld(), 0, 0, 0, 0, dustOptions));
		api.drawHelix(point, 1, 15, 5, 1, player.getLocation().getYaw(), new ParticleData<Object>(Particle.REDSTONE, player.getWorld(), 0, 0, 0, 0, dustOptions2));
		api.drawHelix(point, 1, 15, 5, 1, player.getLocation().getYaw(), new ParticleData<Object>(Particle.FLAME, player.getWorld(), 0));
		player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BLASTFURNACE_FIRE_CRACKLE, 1, 1);
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
		cd.activateCooldown(player, 5, 10);
	}
	
	public void beastFive(Player player) { //strength
		if (cd.isPlayerOnCooldown(player, 5)) {
			return;
		}
		ParticleAPI api = new ParticleAPI(plugin);
		Point point = new Point(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
		api.drawDoubleSlash(3.5, 2, player.getLocation().getYaw(), point, new ParticleData<Object>(Particle.CRIT, player.getWorld(), 0));
		api.drawDoubleSlash(3.5, 2, player.getLocation().getYaw(), point, new ParticleData<Object>(Particle.CRIT_MAGIC, player.getWorld(), 0));
		for (Entity e : player.getWorld().getNearbyEntities(point.getPointLocation(player), 2, 1, 2)) {
			if (e != player) {
				if (e instanceof LivingEntity) {
					LivingEntity entity = (LivingEntity) e;
					entity.damage(5, player);
					entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_PIGLIN_BRUTE_ANGRY, 1, 1);
				}
			}
		}
		cd.activateCooldown(player, 5, 5);
	}
	
	Map<Player, Boolean> wfo = new HashMap<Player, Boolean>();
	
	public void insectFive(Player player) { //strength
		if (wfo.containsKey(player)) {
			if (wfo.get(player)) {
				return;
			}
		}
		ParticleAPI api = new ParticleAPI(plugin);
		Particle.DustOptions dustOptions = new Particle.DustOptions(Color.PURPLE, 1);
		Particle.DustOptions dustOptions2 = new Particle.DustOptions(Color.WHITE, 1);
		Point point = new Point(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
		api.drawSlash(3.5, 2, player.getLocation().getYaw(), point, new ParticleData<Object>(Particle.REDSTONE, player.getWorld(), 0, 0, 0, 0, dustOptions));
		api.drawSlash(3.5, 2, player.getLocation().getYaw(), point, new ParticleData<Object>(Particle.REDSTONE, player.getWorld(), 0, 0, 0, 0, dustOptions2));
		api.drawSlash(3.5, 2, player.getLocation().getYaw(), point, new ParticleData<Object>(Particle.ELECTRIC_SPARK, player.getWorld(), 0));
		for (Entity e : player.getWorld().getNearbyEntities(point.getPointLocation(player), 2, 1, 2)) {
			if (e != player) {
				if (e instanceof LivingEntity) {
					LivingEntity entity = (LivingEntity) e;
					entity.damage(2, player);
					entity.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 2*20, 2));
					entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_BEE_STING, 1, 1);
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
	
	public void sunFive(Player player) {
		if (cd.isPlayerOnCooldown(player, 5)) {
			return;
		}
		OldParticleAPI api = new OldParticleAPI(player);
		int i = api.createFullSphere(Particle.NAUTILUS, 8);
		int i2 = api.createFullSphere(Particle.FLAME, 8);
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
            @Override
            public void run() {
            	scheduler.cancelTask(i);
            	scheduler.cancelTask(i2);
            }
        }, 3*20);
		cd.activateCooldown(player, 5, 7);
	}
	
	public void soundFive(Player player) {
		ParticleAPI api = new ParticleAPI(plugin);
		if (cd.isPlayerOnCooldown(player, 5)) {
			return;}
		BukkitScheduler scheduler = Bukkit.getScheduler();
		scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				api.drawVerticalCircle(player, new ParticleData<Object>(Particle.NOTE, player.getWorld(), 0), 2, 3, 3, 1);
				api.drawVerticalCircle(player, new ParticleData<Object>(Particle.FLASH, player.getWorld(), 0), 2, 3, 3, 1);
			}
		}, 25);
		player.getWorld().playSound(player.getLocation(), Sound.AMBIENT_UNDERWATER_ENTER, 1, 1);
		player.setVelocity(player.getLocation().getDirection().multiply(2.5));
		cd.activateCooldown(player, 5, 10);
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