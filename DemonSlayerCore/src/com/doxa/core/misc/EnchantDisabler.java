package com.doxa.core.misc;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import com.doxa.core.Main;

public class EnchantDisabler implements Listener {

	Main plugin;
	public EnchantDisabler(Main main) {
		plugin = main;
	}
	
	@EventHandler
	public void onEnchant(EnchantItemEvent event) {
		Player player = event.getEnchanter();
		ItemStack item = event.getItem();
		if (player.hasPermission("demonslayer.enchant"))
			return;
		if (!plugin.getNichirin().isItemNichirin(item))
			return;
		event.setCancelled(true);
		player.sendMessage(ChatColor.RED + "You cannot enchant this sword like other items. Its special. Take it to the blacksmith at spawn and see what he can do for you! (/warp blacksmith)");
	}
	
	@EventHandler
	public void onAnvil(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		if (event.getView().getTopInventory().getType() != InventoryType.ANVIL)
			return;
		if (plugin.getNichirin().isItemNichirin(event.getCurrentItem())) {
			event.setCancelled(true);
			player.sendMessage(ChatColor.RED + "You cannot use Nichirin Swords in an anvil!");
		}
	}
	
}
