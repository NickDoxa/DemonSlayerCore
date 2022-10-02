package com.doxa.core.skins;

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
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.doxa.core.Main;

import net.milkbowl.vault.economy.Economy;

public class Skins implements Listener, CommandExecutor {

	Main plugin;
	Economy eco;
	private String name;
	public Skins(Main main, String n, Economy e) {
		plugin = main;
		name = n;
		eco = e;
	}
	
	private Inventory inv;
	
	public void openGUI(Player p) {
		p.openInventory(inv);
	}
	
	//INTEGER(SLOT), INTEGER(CUSTOM MODEL DATA)
	Map<Integer, Integer> item_map = new HashMap<Integer, Integer>();
	Map<Integer, String> perm_map = new HashMap<Integer, String>();
	
	public void createInventory(int size, Player player) {
		inv = Bukkit.createInventory(null, size, name);
		this.addGUIItem(player, new ItemStack(Material.NETHERITE_SWORD, 1), 1, "Tanjiro", 0, ChatColor.AQUA, "demonslayer.skins.tanjiro");
		this.addGUIItem(player, new ItemStack(Material.NETHERITE_SWORD, 1), 2, "Inosuke", 1, ChatColor.DARK_PURPLE, "demonslayer.skins.inosuke");
		this.addGUIItem(player, new ItemStack(Material.NETHERITE_SWORD, 1), 3, "Zenitsu", 2, ChatColor.YELLOW, "demonslayer.skins.zenitsu");
		this.addGUIItem(player, new ItemStack(Material.NETHERITE_SWORD, 1), 4, "Rengoku", 3, ChatColor.RED, "demonslayer.skins.rengoku");
		this.addGUIItem(player, new ItemStack(Material.NETHERITE_SWORD, 1), 5, "Giyu", 4, ChatColor.RED, "demonslayer.skins.giyu");
		this.addGUIItem(player, new ItemStack(Material.NETHERITE_SWORD, 1), 6, "Shinobu", 5, ChatColor.GREEN, "demonslayer.skins.shinobu");
		this.addGUIItem(player, new ItemStack(Material.NETHERITE_SWORD, 1), 12, "Kocho", 6, ChatColor.GREEN, "demonslayer.skins.kocho");
		this.addGUIItem(player, new ItemStack(Material.NETHERITE_SWORD, 1), 13, "Sanemi", 7, ChatColor.GRAY, "demonslayer.skins.sanemi");
		this.addGUIItem(player, new ItemStack(Material.NETHERITE_SWORD, 1), 14, "Uzui", 8, ChatColor.DARK_PURPLE, "demonslayer.skins.uzui");
		this.addGUIItem(player, new ItemStack(Material.NETHERITE_SWORD, 1), 15, "Kanae", 9, ChatColor.LIGHT_PURPLE, "demonslayer.skins.kanae");
		this.addGUIItem(player, new ItemStack(Material.NETHERITE_SWORD, 1), 16, "Urokodaki", 10, ChatColor.AQUA, "demonslayer.skins.urokodaki");
		
		this.addGUIItem(player, new ItemStack(Material.NETHERITE_SWORD, 1), 7, "Upper Moon 1: Kokushibo", 18, ChatColor.LIGHT_PURPLE, "demonslayer.skins.moon");
		this.addGUIItem(player, new ItemStack(Material.NETHERITE_SWORD, 1), 8, "Yoriichi", 19, ChatColor.RED, "demonslayer.skins.yoriichi");
		
		this.addGUIItem(player, new ItemStack(Material.NETHERITE_SWORD, 1), 9, "Enma", 27, ChatColor.DARK_PURPLE, "demonslayer.skins.enma");
		this.addGUIItem(player, new ItemStack(Material.NETHERITE_SWORD, 1), 10, "Ichigo", 28, ChatColor.GRAY, "demonslayer.skins.ichigo");
		this.addGUIItem(player, new ItemStack(Material.NETHERITE_SWORD, 1), 11, "Sausuke Uchiha", 29, ChatColor.DARK_AQUA, "demonslayer.skins.sasuke");
		this.addGUIItem(player, new ItemStack(Material.NETHERITE_SWORD, 1), 17, "Christmas", 30, ChatColor.WHITE, "demonslayer.skins.christmas");
	}
	
	public void addGUIItem(Player player, ItemStack i, int CustomModelData, String label, int slot, ChatColor color, String perms) {
		item_map.put(slot, CustomModelData);
		perm_map.put(slot, perms);
		ItemMeta meta = i.getItemMeta();
		meta.setDisplayName(color + label);
		meta.setCustomModelData(CustomModelData);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		List<String> lore = new ArrayList<String>();
		if (player.hasPermission(perms) || player.hasPermission("demonslayer.donator")) {
			lore.add(ChatColor.GREEN + "UNLOCKED!");
			lore.add("");
			lore.add(ChatColor.GRAY + "Click to use!");
		} else {
			lore.add(ChatColor.RED + "LOCKED!");
			lore.add("");
			lore.add(ChatColor.GRAY + "Unlock in crates or /buy!");
		}
		meta.setLore(lore);
		i.setItemMeta(meta);
		inv.setItem(slot, i);
	}
	
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
		if (!item_map.containsKey(event.getSlot()))
			return;
		if (event.getClick() == ClickType.LEFT) {
			String perm = perm_map.get(event.getSlot());
			if (player.hasPermission(perm) || player.hasPermission("demonslayer.donator")) {
				int CustomModelData = item_map.get(event.getSlot());
				if (CustomModelData == 2) {
					plugin.getSkinSetter().setPlayerSkin(player, CustomModelData);
					plugin.getSkinSetter().setInosukeBlade(player);
				} else if (CustomModelData == 14) {
					plugin.getSkinSetter().setPlayerSkin(player, CustomModelData);
					plugin.getSkinSetter().setUzuiBlade(player);
				} else {
					plugin.getSkinSetter().setPlayerSkin(player, CustomModelData);	
					plugin.getSkinSetter().removeInosukeBlade(player);
				}
				player.sendMessage(ChatColor.GREEN + "New skin selected!");
				if (plugin.getNichirin().isItemNichirin(player.getInventory().getItemInMainHand())) {
					ItemStack item = player.getInventory().getItemInMainHand();
					ItemMeta meta = item.getItemMeta();
					meta.setCustomModelData(CustomModelData);
					item.setItemMeta(meta);
				}
			} else {
				player.sendMessage(ChatColor.RED + "That skin is locked!");
			}
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("skins")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				this.createInventory(36, player);
				this.openGUI(player);
				return true;
			}
		}
		return false;
	}
	
}
