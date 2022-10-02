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
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

import com.doxa.core.Main;
import com.doxa.core.OldParticleAPI;
import com.doxa.core.particles.ParticleAPI;
import com.doxa.core.particles.ParticleData;
import com.doxa.core.particles.Point;

public class Two implements Listener {
	
	Main plugin;
	Cooldowns cd;
	public Two(Main main) {
		plugin = main;
		cd = new Cooldowns();
	}
	
	public void waterTwo(Player player) { //proficiency
		ParticleAPI api = new ParticleAPI(plugin);
		if (cd.isPlayerOnCooldown(player, 2)) {
			return;}
		Particle.DustOptions dustOptions = new Particle.DustOptions(Color.BLUE, 1);
		Particle.DustOptions dustOptions2 = new Particle.DustOptions(Color.AQUA, 1);
		api.drawVerticalCircle(player, new ParticleData<Object>(Particle.REDSTONE, player.getWorld(), 0, 0, 0, 0, dustOptions), 2, 3, 3, 1);
		api.drawVerticalCircle(player, new ParticleData<Object>(Particle.REDSTONE, player.getWorld(), 0, 0, 0, 0, dustOptions2), 2, 3, 3, 1);
		api.drawVerticalCircle(player, new ParticleData<Object>(Particle.WATER_SPLASH, player.getWorld(), 0), 2, 3, 3, 1);
		player.getWorld().playSound(player.getLocation(), Sound.AMBIENT_UNDERWATER_ENTER, 1, 1);
		player.setVelocity(player.getLocation().getDirection().multiply(1.25));
		cd.activateCooldown(player, 2, 10);
		for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 3, 3, 3)) {
			if (e != player) {
				if (e instanceof LivingEntity) {
					LivingEntity entity = (LivingEntity) e;
					entity.damage(5, player);
				}
			}
		}
	}
	
	List<Snowball> snowlist = new ArrayList<Snowball>();
	
	public void demonTwo(Player player) { //strength
		if (cd.isPlayerOnCooldown(player, 2)) {
			return;}
		Snowball sb = (Snowball) player.launchProjectile(Snowball.class);
		sb.setGlowing(true);
		sb.setBounce(false);
		sb.setGravity(true);
		sb.setVelocity(player.getLocation().getDirection().multiply(4));
		sb.setShooter(player);
		snowlist.add(sb);
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
            @Override
            public void run() {
        		Snowball sb2 = (Snowball) player.launchProjectile(Snowball.class);
        		sb2.setGlowing(true);
        		sb2.setBounce(false);
        		sb2.setGravity(true);
        		sb2.setVelocity(player.getLocation().getDirection().multiply(4));
        		sb2.setShooter(player);
        		snowlist.add(sb2);
            }
        },12);
		cd.activateCooldown(player, 2, 10);
	}
	
	@EventHandler
	public void onDmgWithSnowball(EntityDamageByEntityEvent event) {
		Entity d = event.getEntity();
		if (d instanceof LivingEntity) {
			LivingEntity damaged = (LivingEntity) d;
			Entity entity = event.getDamager();
			if (entity instanceof Snowball) {
				Snowball sb = (Snowball) entity;
				if (snowlist.contains(sb)) {
					damaged.damage(4.0, (Entity) sb.getShooter());
				}
			}
		}
	}
	
	@EventHandler
	public void onHit(ProjectileHitEvent event) {
		Projectile proj = event.getEntity();
		if (snowlist.contains(proj)) {
			proj.remove();
		}
	}
	
	public void thunderTwo(Player player) { //defense
		OldParticleAPI oapi = new OldParticleAPI(player);
		if (cd.isPlayerOnCooldown(player, 2)) {
			return;}
		player.setVelocity(player.getLocation().getDirection().multiply(1.25));
		player.getWorld().playSound(player.getLocation(), Sound.BLOCK_CAMPFIRE_CRACKLE, 1, 1);
		int id = oapi.createFullSphere(Particle.REDSTONE, Color.YELLOW);
		int id2 = oapi.createFullSphere(Particle.REDSTONE, Color.WHITE);
		player.getWorld().playSound(player.getLocation(), Sound.ENTITY_SILVERFISH_DEATH, 1, 1);
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
            @Override
            public void run() {
            	Bukkit.getScheduler().cancelTask(id);
            	Bukkit.getScheduler().cancelTask(id2);
            }
        },25);
        cd.activateCooldown(player, 2, 7);
	}
	
	public void flameTwo(Player player) { //proficiency
		ParticleAPI api = new ParticleAPI(plugin);
		if (cd.isPlayerOnCooldown(player, 2)) {
			return;}
		Particle.DustOptions dustOptions = new Particle.DustOptions(Color.RED, 1);
		Particle.DustOptions dustOptions2 = new Particle.DustOptions(Color.YELLOW, 1);
		api.drawVerticalCircle(player, new ParticleData<Object>(Particle.REDSTONE, player.getWorld(), 0, 0, 0, 0, dustOptions), 2, 3, 3, 1);
		api.drawVerticalCircle(player, new ParticleData<Object>(Particle.REDSTONE, player.getWorld(), 0, 0, 0, 0, dustOptions2), 2, 3, 3, 1);
		api.drawVerticalCircle(player, new ParticleData<Object>(Particle.FLAME, player.getWorld(), 0), 2, 3, 3, 1);
		player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BLASTFURNACE_FIRE_CRACKLE, 1, 1);
		cd.activateCooldown(player, 2, 10);
		for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 3, 3, 3)) {
			if (e != player) {
				if (e instanceof LivingEntity) {
					LivingEntity entity = (LivingEntity) e;
					entity.damage(3, player);
					entity.setFireTicks(3*20);
				}
			}
		}
	}
	
	public void beastTwo(Player player) { //strength
		if (cd.isPlayerOnCooldown(player, 2)) {
			return;}
		ParticleAPI api = new ParticleAPI(plugin);
		Point point = new Point(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
		api.drawSlash(3.5, 2, player.getLocation().getYaw(), point, new ParticleData<Object>(Particle.CRIT, player.getWorld(), 0));
		api.drawSlash(3.5, 2, player.getLocation().getYaw(), point, new ParticleData<Object>(Particle.CRIT_MAGIC, player.getWorld(), 0));
		api.drawSlash2(3.5, 2, player.getLocation().getYaw(), point, new ParticleData<Object>(Particle.CRIT, player.getWorld(), 0));
		api.drawSlash2(3.5, 2, player.getLocation().getYaw(), point, new ParticleData<Object>(Particle.CRIT_MAGIC, player.getWorld(), 0));
		for (Entity e : player.getWorld().getNearbyEntities(point.getPointLocation(player), 2, 1, 2)) {
			if (e != player) {
				if (e instanceof LivingEntity) {
					LivingEntity entity = (LivingEntity) e;
					entity.damage(5, player);
					entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_PIGLIN_BRUTE_ANGRY, 1, 1);
				}
			}
		}
		cd.activateCooldown(player, 2, 5);
	}
	
	public void insectTwo(Player player) {
		if (cd.isPlayerOnCooldown(player, 2)) {
			return;}
        		Point point = new Point(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
        		ParticleAPI api = new ParticleAPI(plugin);
        		List<Point[]> loc = api.drawSlash(3.5, 2, player.getLocation().getYaw(), point, 
        				new ParticleData<Object>(Particle.REDSTONE, player.getWorld(), 0, 0, 0, 0, new Particle.DustOptions(Color.PURPLE, 2)));
        		api.drawSlash(3.5, 2, player.getLocation().getYaw(), point, 
        				new ParticleData<Object>(Particle.PORTAL, player.getWorld(), 0));
        		api.drawSlash(3.5, 2, player.getLocation().getYaw(), point, 
        				new ParticleData<Object>(Particle.FIREWORKS_SPARK, player.getWorld(), 0));
        		api.drawSlash2(3.5, 2, player.getLocation().getYaw(), point, 
        				new ParticleData<Object>(Particle.REDSTONE, player.getWorld(), 0, 0, 0, 0, new Particle.DustOptions(Color.PURPLE, 2)));
        		api.drawSlash2(3.5, 2, player.getLocation().getYaw(), point, 
        				new ParticleData<Object>(Particle.PORTAL, player.getWorld(), 0));
        		api.drawSlash2(3.5, 2, player.getLocation().getYaw(), point, 
        				new ParticleData<Object>(Particle.FIREWORKS_SPARK, player.getWorld(), 0));
        		for (Point[] points : loc) {
        			for (Point p : points) {
        				Location l = p.getPointLocation(player);
        				for (Entity e : player.getWorld().getNearbyEntities(l, 2, 1, 2)) {
        					if (e != player) {
        						if (e instanceof LivingEntity) {
        							LivingEntity entity = (LivingEntity) e;
        							entity.damage(1, player);
        							entity.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 5*20, 2));
        							entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_BEE_STING, 1, 1);
        						}
        					}
        				}
        			}
        		}
		cd.activateCooldown(player, 2, 15);
	}
	
	public void sunTwo(Player player) { //proficiency
		ParticleAPI api = new ParticleAPI(plugin);
		if (cd.isPlayerOnCooldown(player, 2)) {
			return;}
		Particle.DustOptions dustOptions = new Particle.DustOptions(Color.RED, 1);
		api.drawVerticalCircle(player, new ParticleData<Object>(Particle.REDSTONE, player.getWorld(), 0, 0, 0, 0, dustOptions), 2, 3, 3, 1);
		api.drawVerticalCircle(player, new ParticleData<Object>(Particle.LAVA, player.getWorld(), 0), 2, 3, 3, 1);
		api.drawVerticalCircle(player, new ParticleData<Object>(Particle.NAUTILUS, player.getWorld(), 0), 2, 3, 3, 1);
		OldParticleAPI oapi = new OldParticleAPI(player);
		int i = oapi.createCircle(Particle.NAUTILUS, player.getLocation(), 3);
		int i2 = oapi.createCircle(Particle.FLAME, player.getLocation(), 3);
		player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BLASTFURNACE_FIRE_CRACKLE, 1, 1);
		player.setVelocity(player.getLocation().getDirection().multiply(1.25));
		cd.activateCooldown(player, 2, 10);
		for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 3, 3, 3)) {
			if (e != player) {
				if (e instanceof LivingEntity) {
					LivingEntity entity = (LivingEntity) e;
					entity.damage(5, player);
					entity.setFireTicks(3*20);
				}
			}
		}
		BukkitScheduler scheduler = Bukkit.getScheduler();
		scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				scheduler.cancelTask(i);
				scheduler.cancelTask(i2);
			}
		},10);
	}
	
	public void soundTwo(Player player) {
		if (cd.isPlayerOnCooldown(player, 2)) {
			return;}
		OldParticleAPI api = new OldParticleAPI(player);
		int i = api.createMassWave2(Particle.NOTE, Particle.FLASH);
		BukkitScheduler scheduler = Bukkit.getScheduler();
		scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				scheduler.cancelTask(i);
			}
		}, 50);
		cd.activateCooldown(player, 2, 10);
	}
	
}
