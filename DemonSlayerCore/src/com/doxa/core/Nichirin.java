package com.doxa.core;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class Nichirin implements Listener, CommandExecutor {
	
	Main plugin;
	public Nichirin(Main main) {
		plugin = main;
	}
	
	@EventHandler
	public void onDamageWithNichirin(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player) {
			Player player = (Player) event.getDamager();
			if (event.getEntity() instanceof Player)
				return;
			if (this.isItemNichirin(player.getInventory().getItemInMainHand())) {
				event.setDamage(event.getDamage() * 2);
			} else {
				return;
			}
		} else {
			return;
		}
	}

	public boolean isItemNichirin(ItemStack i) {
		if (i == null)
			return false;
		if (!i.hasItemMeta())
			return false;
		ItemMeta meta = i.getItemMeta();
		try {
			if (i.getType() == Material.NETHERITE_SWORD && meta.isUnbreakable()) {
				return true;
			} else {
				return false;
			}
		} catch (NullPointerException e) {
			return false;
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (label.equalsIgnoreCase("nichirin")) {
				if (!player.hasPermission("demonslayer.staff"))
					return true;
				if (args.length < 2) {
					player.sendMessage(ChatColor.RED + "Incorrect Usage. Try: /nichirin give <player> [sharp lvl]");
					return true;
				} else {
				switch (args[0].toLowerCase()) {
					case "give":
						Player give;
						try {
							give = Bukkit.getPlayer(args[1]);
						} catch(NullPointerException e) {
							player.sendMessage(ChatColor.RED + "Player: " + args[1] + " is not online!");
							return true;
						}
						ItemStack nichirin = new ItemStack(Material.NETHERITE_SWORD, 1);
						ItemMeta meta = nichirin.getItemMeta();
						meta.setDisplayName(ChatColor.AQUA + "Nichirin Sword");
						meta.setUnbreakable(true);
						meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
						meta.setCustomModelData(1);
						if (args.length > 2) {
							if (args[2].equalsIgnoreCase("max")) {
								meta.addEnchant(Enchantment.DAMAGE_ALL, 10, true);
							} else {
							if (plugin.isStringInt(args[2])) {
								meta.addEnchant(Enchantment.DAMAGE_ALL, Integer.parseInt(args[2]), true);
							}}
						}
						nichirin.setItemMeta(meta);
						give.getInventory().addItem(nichirin);
						String send = ChatColor.GREEN + "You were given a Nichirin Sword";
						if (args.length > 2) {
							if (args[2].equalsIgnoreCase("max")) {
								send = send + " with the maximum levels!";
							} else {
							if (plugin.isStringInt(args[2])) {
								send = send + " with sharpness " + args[2];
							}}
						}
						give.sendMessage(send);
						player.sendMessage(ChatColor.GREEN + "Nichirin sword given to " + give.getName());
						break;
					case "help":
						player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Nichirin Swords");
						player.sendMessage("");
						player.sendMessage("/nichirin give <player> [sharp lvl] - give a player a nichirin sword");
						break;
				}
			}}
		} else {
			if (label.equalsIgnoreCase("nichirin")) {
				if (args.length < 2) {
					plugin.getLogger().info(ChatColor.RED + "Incorrect Usage. Try: /nichirin give <player> [sharp lvl]");
					return true;
				} else {
				switch (args[0].toLowerCase()) {
					case "give":
						Player give;
						try {
							give = Bukkit.getPlayer(args[1]);
						} catch(NullPointerException e) {
							plugin.getLogger().info(ChatColor.RED + "Player: " + args[1] + " is not online!");
							return true;
						}
						ItemStack nichirin = new ItemStack(Material.NETHERITE_SWORD, 1);
						ItemMeta meta = nichirin.getItemMeta();
						meta.setDisplayName(ChatColor.AQUA + "Nichirin Sword");
						meta.setUnbreakable(true);
						meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
						meta.setCustomModelData(1);
						if (args.length > 2) {
							if (args[2].equalsIgnoreCase("max")) {
								meta.addEnchant(Enchantment.DAMAGE_ALL, 10, true);
							} else {
							if (plugin.isStringInt(args[2])) {
								meta.addEnchant(Enchantment.DAMAGE_ALL, Integer.parseInt(args[2]), true);
							}}
						}
						nichirin.setItemMeta(meta);
						give.getInventory().addItem(nichirin);
						String send = ChatColor.GREEN + "You were given a Nichirin Sword";
						if (args.length > 2) {
							if (args[2].equalsIgnoreCase("max")) {
								send = send + " with the maximum levels!";
							} else {
							if (plugin.isStringInt(args[2])) {
								send = send + " with sharpness " + args[2];
							}}
						}
						give.sendMessage(send);
						plugin.getLogger().info(ChatColor.GREEN + "Nichirin sword given to " + give.getName());
						break;
					case "help":
						plugin.getLogger().info(ChatColor.GOLD + "" + ChatColor.BOLD + "Nichirin Swords");
						plugin.getLogger().info("");
						plugin.getLogger().info("/nichirin give <player> [sharp lvl] - give a player a nichirin sword");
						break;
				}
			}}
		}
		return false;
	}

}
