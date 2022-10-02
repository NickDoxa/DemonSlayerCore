package com.doxa.core.kills;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import com.doxa.core.Main;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class Kills implements Listener {
	
	Main plugin;
	public Kills(Main main) {
		plugin = main;
	}

	//DEMON MONEY
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onEntityKill(EntityDeathEvent event) {
		if (event.getEntity().getKiller() instanceof Player) {
			Player player = event.getEntity().getKiller();
			if (!player.getLocation().getWorld().getName().equalsIgnoreCase("world"))
				return;
			if (plugin.getSetter().isPlayerDemon(player))
				return;
			if (event.getEntity().getCustomName() == null)
				return;
			if (this.isPlayerInGuard(player))
				return;
			if (event.getEntity().getCustomName().contains("Low Level Demon") || event.getEntity().getCustomName().contains("High Level Demon") 
					|| event.getEntity().getCustomName().contains("Kizuki")) {
				switch (event.getEntity().getType().toString().toLowerCase()) {
					case "zombie":
						player.sendMessage(ChatColor.GREEN + "+50");
						plugin.getVault().depositPlayer(player.getName(), 50.0);
						break;
					case "husk":
						player.sendMessage(ChatColor.GREEN + "+100");
						plugin.getVault().depositPlayer(player.getName(), 100.0);
						break;
					case "drowned":
						player.sendMessage(ChatColor.GREEN + "+500");
						plugin.getVault().depositPlayer(player.getName(), 500.0);
						break;
				}
			}
		}
	}
	
	//SLAYER DAMAGE BUFF
	@EventHandler
	public void onDamageWithNichirin(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player) {
			final double damage = event.getDamage();
			Player player = (Player) event.getDamager();
			if (!plugin.getNichirin().isItemNichirin(player.getInventory().getItemInMainHand()))
				return;
			if (plugin.getSetter().isPlayerDemon(player)) {
				return;
			}	
			if (event.getEntity() instanceof Player) {
				Player target = (Player) event.getEntity();
				if (target.hasMetadata("NPC"))
					return;
				if (!plugin.getSetter().isPlayerDemon(target)) {
					return;
				}
				double rando = Math.random();
				if (rando > 0.2) {
					event.setDamage(damage*1.25);
				} else {
					event.setDamage(damage*3);
					player.spigot().sendMessage(ChatMessageType.ACTION_BAR, 
							new TextComponent(ChatColor.DARK_PURPLE + "Critical Hit Landed!"));
					target.spigot().sendMessage(ChatMessageType.ACTION_BAR, 
							new TextComponent(ChatColor.DARK_RED + "Critical Hit Sustained!"));
				}
			}
		} else {
			return;
		}
	}
	
	//PLAYER MONEY
	@EventHandler
	public void onPlayerKill(PlayerDeathEvent event) {
		Player player = event.getEntity();
		if (player.getKiller() instanceof Player) {
			Player killer = player.getKiller();
			if (plugin.getSetter().isPlayerDemon(killer) && !plugin.getSetter().isPlayerDemon(player)) {
				if (plugin.getVault().getBalance(player) > 500) {
					killer.sendMessage(ChatColor.GREEN + "+250");
					player.sendMessage(ChatColor.RED + "-250");
					plugin.getVault().withdrawPlayer(player, 250);
					plugin.getVault().depositPlayer(killer, 250);
				}
			}
			if (!plugin.getSetter().isPlayerDemon(killer) && plugin.getSetter().isPlayerDemon(player)) {
				if (plugin.getVault().getBalance(player) > 500) {
					killer.sendMessage(ChatColor.GREEN + "+250");
					player.sendMessage(ChatColor.RED + "-250");
					plugin.getVault().withdrawPlayer(player, 250);
					plugin.getVault().depositPlayer(killer, 250);
				}
			}
		}
	}
    
    @SuppressWarnings("unchecked")
	public boolean isPlayerInGuard(Player player) {
    	Location loc = BukkitAdapter.adapt(player.getLocation());
    	RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
    	RegionQuery query = container.createQuery();
    	ApplicableRegionSet set = query.getApplicableRegions(loc);
    	try {
	    	for (ProtectedRegion r : set.getRegions()) {
	    		if (((List<String>)plugin.getConfig().getList("worldguard.protected")).contains(r.getId())) {
	    			return true;
	    		} else {
	    			return false;
	    		}
	    	}
    	} catch (NullPointerException e) {
    		return false;
    	}
    	return false;
    }
	
}
