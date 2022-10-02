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

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class Four implements Listener {

	Main plugin;
	Cooldowns cd;
	public Four(Main main) {
		plugin = main;
		cd = new Cooldowns();
	}
	
	/*
	 * WATER
	 */
	
	List<Arrow> arrows = new ArrayList<Arrow>();
	
	public void waterFour(Player player) { //strength
		ParticleAPI api = new ParticleAPI(plugin);
		if (cd.isPlayerOnCooldown(player, 4)) {
			return;}
		Particle.DustOptions dustOptions = new Particle.DustOptions(Color.BLUE, 2);
		Particle.DustOptions dustOptions2 = new Particle.DustOptions(Color.AQUA, 2);
		Point point = new Point(player.getLocation().getX(), player.getLocation().getY() + 1.5, player.getLocation().getZ());
		api.drawLine(1, 20, point, player.getLocation().getDirection(), new ParticleData<Object>(Particle.REDSTONE, player.getWorld(), 0, 0, 0, 0, dustOptions));
		api.drawLine(1, 20, point, player.getLocation().getDirection(), new ParticleData<Object>(Particle.REDSTONE, player.getWorld(), 0, 0, 0, 0, dustOptions2));
		api.drawLine(1, 20, point, player.getLocation().getDirection(), new ParticleData<Object>(Particle.WATER_SPLASH, player.getWorld(), 0));
		player.getWorld().playSound(player.getLocation(), Sound.AMBIENT_UNDERWATER_ENTER, 1, 1);
		Vector vec = player.getLocation().getDirection();
		Arrow arrow = (Arrow) player.launchProjectile(Arrow.class);
		//TODO FIND WAY TO MAKE INVISIBLE
//		PacketContainer arrowPacketContainer = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);
//		arrowPacketContainer.getIntegerArrays().write(0, new int[]{arrow.getEntityId()});
//		Bukkit.getOnlinePlayers().forEach(p -> {
//			try {
//				Main.getPlugin(Main.class).getProtManager().sendServerPacket(p, arrowPacketContainer);
//			} catch (InvocationTargetException e) {
//				e.printStackTrace();
//			}
//		});
        arrow.setBounce(false);
        arrow.setGravity(false);
        arrow.setSilent(true);
        arrow.setVelocity(vec.multiply(5));
        arrow.setShooter((ProjectileSource) ((LivingEntity) player));
        arrows.add(arrow);
		cd.activateCooldown(player, 4, 10);
	}
	
	@EventHandler
	public void onDmg(EntityDamageByEntityEvent event) {
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
	 * DEMON
	 */
	
	public void demonFour(Player player) { //defense
		if (cd.isPlayerOnCooldown(player, 4)) {
			return;}
		plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "mythicmobs:spawnmob LowLevelDemon:1 3 " + player.getWorld().getName() + 
				"," + player.getLocation().getX() + "," + player.getLocation().getY() + "," + player.getLocation().getZ() + ",1");
		cd.activateCooldown(player, 4, 30);
	}
	
	/*
	 * THUNDER
	 */
	
	Map<Player, Integer> use = new HashMap<Player, Integer>();
	Map<Player, Integer> ids = new HashMap<Player, Integer>();
	
	public void thunderFour(Player player) { //defense
		if (cd.isPlayerOnCooldown(player, 4)) {
			return;}
		OldParticleAPI oapi = new OldParticleAPI(player);
		int id = oapi.createHelix(Particle.ELECTRIC_SPARK, player);
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
            @Override
            public void run() {
            	Bukkit.getScheduler().cancelTask(id);
            }
        },(15*3)+5);
        use.put(player, 0);
        this.createParticles(player);
		cd.activateCooldown(player, 4, 35);
	}
	
	public int createParticles(Player player) {
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        ParticleAPI api = new ParticleAPI(plugin);
		int id = scheduler.scheduleSyncRepeatingTask(Main.getPlugin(Main.class), new Runnable() {
            @Override
            public void run() {
				List<Point[]> loc = api.drawBurstLine(1, 30, new Point(player.getLocation().getX(), player.getLocation().getY() + 1.5, player.getLocation().getZ()), 
						player.getLocation().getDirection(), new ParticleData<Object>(Particle.REDSTONE, player.getWorld(), 0, 0, 0, 0, new Particle.DustOptions(Color.YELLOW, 1)), 
						new ParticleData<Object>(Particle.ELECTRIC_SPARK, player.getWorld(), 2));
				int i = use.get(player) + 1;
				if (i >= 3) {
					Bukkit.getScheduler().cancelTask(ids.get(player));
				} else {
					use.put(player, i);
				}
				for (Point[] points : loc) {
					for (Point p : points) {
						Location l = p.getPointLocation(player);
						for (Entity e : player.getWorld().getNearbyEntities(l, 2, 1, 2)) {
							if (e != player) {
								if (e instanceof LivingEntity) {
									LivingEntity entity = (LivingEntity) e;
									entity.damage(3, player);
									entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 1, 1);
								}
							}
						}
					}
				}
            }
        }, 5, 15);
		ids.put(player, id);
		return id;
	}
	
	public void endTask(int id) {
		Bukkit.getScheduler().cancelTask(id);
	}
	
	public void flameFour(Player player) { //proficiency 
		ParticleAPI api = new ParticleAPI(plugin);
		if (cd.isPlayerOnCooldown(player, 4)) {
			return;}
		Particle.DustOptions dustOptions = new Particle.DustOptions(Color.RED, 1);
		Particle.DustOptions dustOptions2 = new Particle.DustOptions(Color.YELLOW, 1);
		Point point = new Point(player.getLocation().getX(), player.getLocation().getY() + 1.5, player.getLocation().getZ());
		api.drawHelix(point, 2, 5, 5, 1, player.getLocation().getYaw(), new ParticleData<Object>(Particle.REDSTONE, player.getWorld(), 0, 0, 0, 0, dustOptions));
		api.drawHelix(point, 2, 5, 5, 1, player.getLocation().getYaw(), new ParticleData<Object>(Particle.REDSTONE, player.getWorld(), 0, 0, 0, 0, dustOptions2));
		api.drawHelix(point, 2, 5, 5, 1, player.getLocation().getYaw(), new ParticleData<Object>(Particle.FLAME, player.getWorld(), 0));
		player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BLASTFURNACE_FIRE_CRACKLE, 1, 1);
		for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 4, 2, 4)) {
			if (e != player) {
				if (e instanceof LivingEntity) {
					LivingEntity entity = (LivingEntity) e;
					entity.setFireTicks(5*20);
					entity.setVelocity(entity.getLocation().getDirection().multiply(-2));
				}
			}
		}
		cd.activateCooldown(player, 4, 10);
	}
	
	public void beastFour(Player player) { //strength
		 if (cd.isPlayerOnCooldown(player, 4))
			 return;
		 OldParticleAPI oapi = new OldParticleAPI(player);
		 player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PIGLIN_JEALOUS, 1, 1);
		 player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 15, 1));
		 int id = oapi.createMassWave2(Particle.CRIT, Particle.CRIT_MAGIC);
			BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
	        scheduler.scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
	            @Override
	            public void run() {
	            	player.removePotionEffect(PotionEffectType.BLINDNESS);
	            }
	        },15);
	        scheduler.scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
	            @Override
	            public void run() {
	            	Bukkit.getScheduler().cancelTask(id);
	            	player.spigot().sendMessage(ChatMessageType.ACTION_BAR, 
	    					new TextComponent(ChatColor.YELLOW + "Entities Tracked: " + ChatColor.RED + "" + oapi.getPlayerTrackedAmt()));
	            }
	        },50);
	        cd.activateCooldown(player, 4, 20);
	 }
	
	public void insectFour(Player player) {
		if (cd.isPlayerOnCooldown(player, 4))
			 return;
		ParticleAPI api = new ParticleAPI(plugin);
		player.setVelocity(new Vector(player.getLocation().getDirection().getX(), 0, player.getLocation().getDirection().getZ()).multiply(4));
		OldParticleAPI oapi = new OldParticleAPI(player);
		oapi.startXHelix(Particle.FIREWORKS_SPARK, Particle.PORTAL, 20);
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
            @Override
            public void run() {
        		Point point = new Point(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
        		List<Point[]> loc = api.drawSlash(3.5, 2, player.getLocation().getYaw(), point, 
        				new ParticleData<Object>(Particle.REDSTONE, player.getWorld(), 0, 0, 0, 0, new Particle.DustOptions(Color.PURPLE, 1)));
        		for (Point[] points : loc) {
        			for (Point p : points) {
        				Location l = p.getPointLocation(player);
        				for (Entity e : player.getWorld().getNearbyEntities(l, 2, 1, 2)) {
        					if (e != player) {
        						if (e instanceof LivingEntity) {
        							LivingEntity entity = (LivingEntity) e;
        							entity.damage(5, player);
        							entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_BEE_STING, 1, 1);
        						}
        					}
        				}
        			}
        		}
        		//RUN IT AGAIN
        		scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
        			@Override
        			public void run() {
        				ParticleAPI api = new ParticleAPI(plugin);
        				player.setVelocity(new Vector(player.getLocation().getDirection().getX(), 0, player.getLocation().getDirection().getZ()).multiply(4));
        				OldParticleAPI oapi = new OldParticleAPI(player);
        				oapi.startXHelix(Particle.FIREWORKS_SPARK, Particle.PORTAL, 20);
        		        scheduler.scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
        		            @Override
        		            public void run() {
        		        		Point point = new Point(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
        		        		List<Point[]> loc = api.drawSlash(3.5, 2, player.getLocation().getYaw(), point, 
        		        				new ParticleData<Object>(Particle.REDSTONE, player.getWorld(), 0, 0, 0, 0, new Particle.DustOptions(Color.PURPLE, 1)));
        		        		for (Point[] points : loc) {
        		        			for (Point p : points) {
        		        				Location l = p.getPointLocation(player);
        		        				for (Entity e : player.getWorld().getNearbyEntities(l, 2, 1, 2)) {
        		        					if (e != player) {
        		        						if (e instanceof LivingEntity) {
        		        							LivingEntity entity = (LivingEntity) e;
        		        							entity.damage(5, player);
        		        							entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_BEE_STING, 1, 1);
        		        						}
        		        					}
        		        				}
        		        			}
        		        		}
        		            }
        		        }, 10);
        			}
        		},15);
            }
        }, 10);
		cd.activateCooldown(player, 4, 15);
	}
	
	public void sunFour(Player player) { //strength
		if (cd.isPlayerOnCooldown(player, 4)) {
			return;}
		ParticleAPI api = new ParticleAPI(plugin);
		Point point = new Point(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
		api.drawSlash(3.5, 2, player.getLocation().getYaw(), point, new ParticleData<Object>(Particle.NAUTILUS, player.getWorld(), 0));
		api.drawSlash(3.5, 2, player.getLocation().getYaw(), point, new ParticleData<Object>(Particle.LAVA, player.getWorld(), 0));
		api.drawSlash2(3.5, 2, player.getLocation().getYaw(), point, new ParticleData<Object>(Particle.NAUTILUS, player.getWorld(), 0));
		api.drawSlash2(3.5, 2, player.getLocation().getYaw(), point, new ParticleData<Object>(Particle.LAVA, player.getWorld(), 0));
		for (Entity e : player.getWorld().getNearbyEntities(point.getPointLocation(player), 2, 1, 2)) {
			if (e != player) {
				if (e instanceof LivingEntity) {
					LivingEntity entity = (LivingEntity) e;
					entity.damage(5, player);
					entity.setFireTicks(10*20);
					entity.getWorld().playSound(entity.getLocation(), Sound.BLOCK_BLASTFURNACE_FIRE_CRACKLE, 1, 1);
				}
			}
		}
		cd.activateCooldown(player, 4, 10);
	}
	
	public void soundFour(Player player) { //strength
		if (cd.isPlayerOnCooldown(player, 4)) {
			return;}
		ParticleAPI api = new ParticleAPI(plugin);
		Point point = new Point(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
		api.drawSlash(3.5, 2, player.getLocation().getYaw(), point, new ParticleData<Object>(Particle.NOTE, player.getWorld(), 0));
		api.drawSlash(3.5, 2, player.getLocation().getYaw(), point, new ParticleData<Object>(Particle.FLASH, player.getWorld(), 0));
		api.drawSlash2(3.5, 2, player.getLocation().getYaw(), point, new ParticleData<Object>(Particle.NOTE, player.getWorld(), 0));
		api.drawSlash2(3.5, 2, player.getLocation().getYaw(), point, new ParticleData<Object>(Particle.FLASH, player.getWorld(), 0));
		for (Entity e : player.getWorld().getNearbyEntities(point.getPointLocation(player), 2, 1, 2)) {
			if (e != player) {
				if (e instanceof LivingEntity) {
					LivingEntity entity = (LivingEntity) e;
					entity.damage(6, player);
					entity.getWorld().playSound(entity.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_BREAK, 1, 1);
				}
			}
		}
		cd.activateCooldown(player, 4, 7);
	}
	
}


