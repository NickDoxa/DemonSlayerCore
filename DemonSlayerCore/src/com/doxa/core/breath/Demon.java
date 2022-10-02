package com.doxa.core.breath;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Drowned;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Husk;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

import com.doxa.core.Main;
import com.doxa.core.Main.Form;
import com.doxa.core.breath.forms.Eight;
import com.doxa.core.breath.forms.Five;
import com.doxa.core.breath.forms.Four;
import com.doxa.core.breath.forms.One;
import com.doxa.core.breath.forms.Seven;
import com.doxa.core.breath.forms.Six;
import com.doxa.core.breath.forms.Three;
import com.doxa.core.breath.forms.Two;

public class Demon extends BreathBase implements Listener {

	Main plugin;
	One one;
	Two two;
	Three three;
	Four four;
	Five five;
	Six six;
	Seven seven;
	Eight eight;
	public Demon(Main main, Form f, String name, One o, Two t, Three th, Four fo, Five fi, Six s, Seven se, Eight e) {
		plugin = main;
		one = o;
		two = t;
		three = th;
		four = fo;
		five = fi;
		six = s;
		seven = se;
		eight = e;
		this.setForm(f);
		this.setName(name);
		this.setParticleList(new ArrayList<Particle>());
	}
	
	@EventHandler
	public void onDaylight(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (!plugin.getSetter().isPlayerDemon(player))
			return;
		if (player.getWorld().getName() == "spawn")
			return;
		if (player.getLocation().getBlock().getLightFromSky() >= 15 && player.getLocation().getWorld().getTime() < 13000) {
			player.setFireTicks(1*20);
			player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 1*20, 1));
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (plugin.getSetter().isPlayerDemon(player)) {
			BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
	        scheduler.scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
	            @Override
	            public void run() {
	            	event.getPlayer().setMaxHealth(40);
	            }
	        },1*20);
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onRespawn(PlayerRespawnEvent event) {
		if (plugin.getSetter().isPlayerDemon(event.getPlayer())) {
			BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
	        scheduler.scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
	            @Override
	            public void run() {
	            	event.getPlayer().setMaxHealth(40);
	            }
	        },1*20);
		}
	}
	
	@EventHandler
	public void onTarget(EntityTargetEvent event) {
		Entity entity = event.getEntity();
		if (entity instanceof Zombie || entity instanceof Husk || entity instanceof Drowned) {
			if (event.getTarget() instanceof Player) {
				Player player = (Player) event.getTarget();
				if (plugin.getSetter().isPlayerDemon(player)) {
					event.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
			if (player.getInventory().getItemInMainHand().getType() == Material.AIR || player.getInventory().getItemInMainHand().getType() == null) {
				if (!plugin.doesFormMatch(this.getForm(), plugin.getSetter().getPlayerForm(player)))
					return;
				if (!plugin.isPlayerToggledOn(player))
					return;
				if (!plugin.getSetter().isPlayerDemon(player))
					return;
				if (player.isSneaking())
					return;
				if (plugin.isPlayerInSpawn(player))
					return;
				switch (plugin.getFormSetter().getPlayerForm(player)) {
					case 1:
						one.demonOne(player);
						break;
					case 2:
						two.demonTwo(player);
						break;
					case 3:
						three.demonThree(player);
						break;
					case 4:
						four.demonFour(player);
						break;
					case 5:
						five.demonFive(player);
						break;
					case 6:
						six.demonSix(player);
						break;
					case 7:
						seven.demonSeven(player);
						break;
					case 8:
						eight.demonEight(player);
						break;
				}
			}
		}
	}
	
}
