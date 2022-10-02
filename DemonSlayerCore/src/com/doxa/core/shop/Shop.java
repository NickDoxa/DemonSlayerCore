package com.doxa.core.shop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.doxa.core.Main;

import net.advancedplugins.ae.api.AEAPI;
import net.advancedplugins.ae.enchanthandler.enchantments.AdvancedEnchantment;
import net.milkbowl.vault.economy.Economy;

public class Shop implements Listener, CommandExecutor {
	
	Main plugin;
	Economy eco;
	public Shop(Main main, Economy e, int size, String n) {
		plugin = main;
		eco = e;
		this.createInventory(size, n);
	}
	
	private Inventory inv;
	private String name;
	
	public void openGUI(Player p) {
		p.openInventory(inv);
	}
	
	//INTEGER(SLOT), DOUBLE(PRICE)
	Map<Integer, Double> slot_map = new HashMap<Integer, Double>();
	//INTEGER(SLOT), BOOLEAN(REAL)
	Map<Integer, Boolean> real_map = new HashMap<Integer, Boolean>();
	//INTEGER(SLOT), ENCHANTMENT
	Map<Integer, Enchantment> enchant_map = new HashMap<Integer, Enchantment>();
	//INTEGER(SLOT), ECOENCHANT
	Map<Integer, AdvancedEnchantment> eco_map = new HashMap<Integer, AdvancedEnchantment>();
	//INTEGER(SLOT), INTEGER(LEVEL)
	Map<Integer, Integer> lvl_map = new HashMap<Integer, Integer>();
	
	public void createInventory(int size, String n) {
		name = n;
		inv = Bukkit.createInventory(null, size, name);
		this.addShopItem(new ItemStack(Material.IRON_INGOT), 5000, "Sharpness I", Enchantment.DAMAGE_ALL, true, null, 1, 0);
		this.addShopItem(new ItemStack(Material.GOLD_INGOT), 10000, "Sharpness II", Enchantment.DAMAGE_ALL, true, null, 2, 1);
		this.addShopItem(new ItemStack(Material.DIAMOND), 15000, "Sharpness III", Enchantment.DAMAGE_ALL, true, null, 3, 2);
		this.addShopItem(new ItemStack(Material.EMERALD), 20000, "Sharpness IV", Enchantment.DAMAGE_ALL, true, null, 4, 3);
		this.addShopItem(new ItemStack(Material.AMETHYST_SHARD), 25000, "Sharpness V", Enchantment.DAMAGE_ALL, true, null, 5, 4);
		this.addShopItem(new ItemStack(Material.NETHERITE_INGOT), 100000, "Sharpness X", Enchantment.DAMAGE_ALL, true, null, 10, 5);
		
		this.addShopItem(new ItemStack(Material.SKELETON_SKULL), 25000, "Red Nichirin I", null, false, AEAPI.getEnchantmentInstance("rednichirin"), 1, 9);
		this.addShopItem(new ItemStack(Material.CREEPER_HEAD), 50000, "Red Nichirin II", null, false, AEAPI.getEnchantmentInstance("rednichirin"), 2, 10);
		this.addShopItem(new ItemStack(Material.WITHER_SKELETON_SKULL), 100000, "Red Nichirin III", null, false, AEAPI.getEnchantmentInstance("rednichirin"), 3, 11);
		
		this.addShopItem(new ItemStack(Material.STONECUTTER), 100000, "Razor I", null, false, AEAPI.getEnchantmentInstance("recoverybreathing"), 1, 18);
	}
	
	public void addShopItem(ItemStack i, double price, String label, Enchantment e, boolean real, AdvancedEnchantment eb, int lvl, int slot) {
		ItemMeta meta = i.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + label + ": " + ChatColor.DARK_GREEN + "$" + price);
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.GRAY + "Click to buy enchant for");
		lore.add(ChatColor.GRAY + "your Nichirin Sword!");
		meta.setLore(lore);
		real_map.put(slot, real);
		lvl_map.put(slot, lvl);
		if (!real) {
			AEAPI.applyEnchant(eb.toString(), lvl, i);
			eco_map.put(slot, eb);
		} else {
			meta.addEnchant(e, lvl, true);
			enchant_map.put(slot, e);
		}
		i.setItemMeta(meta);
		inv.setItem(slot, i);
		slot_map.put(slot, price);
		
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onClick(InventoryClickEvent event) {
		if(!event.getView().getTitle().contains(name)) 
			return;
		if (event.getCurrentItem() == null)
			return;
		if (event.getCurrentItem().getItemMeta() == null)
			return;
		
		Player player = (Player) event.getWhoClicked();
		event.setCancelled(true);
		if(event.getClickedInventory().getType() == InventoryType.PLAYER)
			return;
		if (!slot_map.containsKey(event.getSlot()))
			return;
		double price = slot_map.get(event.getSlot());
		if (!plugin.getNichirin().isItemNichirin(player.getInventory().getItemInMainHand())) {
			player.sendMessage(ChatColor.RED + "You may only enchant nichirin's here!");
			return;
		}
		if (eco.getBalance(player) < price) {
			player.sendMessage(ChatColor.RED + "You cannot afford that enchantment for your Nichirin Sword");
			player.closeInventory();
			return;
		} else {
			//TODO ADD UPGRADE METHODS
			eco.withdrawPlayer(player, price);
			ItemStack item = player.getInventory().getItemInMainHand();
			ItemMeta meta = item.getItemMeta();
			int lvl = lvl_map.get(event.getSlot());
			boolean real = real_map.get(event.getSlot());
			if (real) {
				player.sendMessage(ChatColor.GREEN + "You purchased " + enchant_map.get(event.getSlot()).getName() +  " for " + price + "!");
				meta.addEnchant(enchant_map.get(event.getSlot()), lvl, true);
			} else {
				player.sendMessage(ChatColor.GREEN + "You purchased " + eco_map.get(event.getSlot()).getName() +  " for " + price + "!");
				AEAPI.applyEnchant(eco_map.get(event.getSlot()).getName(), lvl, item);
			}
			item.setItemMeta(meta);
			player.closeInventory();
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("blacksmith")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (plugin.getNichirin().isItemNichirin(player.getInventory().getItemInMainHand())) {
					this.openGUI(player);
				} else {
					player.sendMessage(ChatColor.RED + "You must be holding a Nichirin Sword to do that!");
				}
				return true;
			}
		}
		return false;
	}
	
}
