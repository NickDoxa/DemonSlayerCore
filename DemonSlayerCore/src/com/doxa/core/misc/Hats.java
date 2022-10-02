package com.doxa.core.misc;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.doxa.core.Main;

public class Hats implements Listener, CommandExecutor {
	
	Main plugin;
	public Hats(Main main) {
		plugin = main;
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		if (!event.getItemInHand().getItemMeta().hasCustomModelData())
			return;
		if (event.getItemInHand().getItemMeta().getCustomModelData() > 0) {
			player.sendMessage(ChatColor.RED + "You cannot place this!");
			event.setCancelled(true);
		}
	}
	
	public void createHat(String s, Player p) {
		ItemStack item = new ItemStack(Material.CARVED_PUMPKIN);
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.setUnbreakable(true);
		switch (s.toLowerCase()) {
		case "witch":
			meta.setCustomModelData(2);
			meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Witch Hat");
			break;
			
		case "krusty":
			meta.setCustomModelData(4);
			meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Krusty Krab Hat");
			break;
			
		case "horn":
			meta.setCustomModelData(3);
			meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Horns Hat");
			break;
			
		case "bolt":
			meta.setCustomModelData(1);
			meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Bolt Hat");
			break;
			
		default:
			p.sendMessage(ChatColor.RED + "Incorrect Usage. Choices: /hatz <krusty | witch | bolt | horn>");
			break;
		}
		item.setItemMeta(meta);
		p.getInventory().addItem(item);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("hatz")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (!player.hasPermission("demonslayer.staff"))
					return true;
				if (args.length < 1) {
					player.sendMessage(ChatColor.RED + "Incorrect Usage. Choices: /hatz <krusty | witch | bolt | horn>");
				} else {
					switch (args[0].toLowerCase()) {
					case "witch":
						this.createHat("witch", player);
						break;
					case "krusty":
						this.createHat("krusty", player);
						break;
					case "horn":
						this.createHat("horn", player);
						break;
					case "bolt":
						this.createHat("bolt", player);
						break;
					default:
						player.sendMessage(ChatColor.RED + "Incorrect Usage. Choices: /hatz <krusty | witch | bolt | horn>");
						break;
					}
				}
			}
		}
		return false;
	}

}
