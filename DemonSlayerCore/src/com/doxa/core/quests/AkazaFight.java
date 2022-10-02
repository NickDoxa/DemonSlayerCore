package com.doxa.core.quests;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.doxa.core.Main;

import net.milkbowl.vault.economy.Economy;

public class AkazaFight implements Listener {
	
	Main plugin;
	Economy eco;
	public AkazaFight(Main main, Economy e) {
		plugin = main;
		eco = e;
	}
	
	@EventHandler
	public void onKill(PlayerDeathEvent event) {
		if (event.getEntity() instanceof Player) {
			Player killed = event.getEntity();
			if (killed.hasMetadata("NPC")) {
				if (killed.getDisplayName().contains("Akaza")) {
					Player player = event.getEntity().getKiller();
					player.teleport(new Location(player.getServer().getWorld("spawn"), -112.5, 109, -1.5, -45, -10));
					player.sendMessage(ChatColor.GREEN + "Congratulations you have completed the final part of the Akaza Quest!");
					player.sendMessage(ChatColor.GRAY + "Now you have a choice to make...");
					player.sendMessage(ChatColor.GRAY + "To become a powerful slayer, or become a demon!");
					player.sendMessage(ChatColor.GRAY + "When you have made your choice do:");
					player.sendMessage(ChatColor.AQUA + "/choose <breath>! (you can now choose demon)");
					player.getServer().dispatchCommand(player.getServer().getConsoleSender(), "lp user " + player.getName() + " permission set demonslayer.demon true");
					player.getServer().dispatchCommand(player.getServer().getConsoleSender(), "lp user " + player.getName() + " permission set demonslayer.akaza true");
					player.getServer().dispatchCommand(player.getServer().getConsoleSender(), "lp user " + player.getName() + " parent remove thunder");
					player.getServer().dispatchCommand(player.getServer().getConsoleSender(), "lp user " + player.getName() + " parent remove water");
					player.getServer().dispatchCommand(player.getServer().getConsoleSender(), "lp user " + player.getName() + " parent remove flame");
				}
			}
		}
	}

}
