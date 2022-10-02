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
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.doxa.core.Main;
import net.milkbowl.vault.economy.Economy;

public class FoodShop implements Listener, CommandExecutor {

	Main plugin;
	Economy eco;
	public FoodShop(Main main, Economy e, int size, String n) {
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
	Map<Integer, ItemStack> item_map = new HashMap<Integer, ItemStack>();
	
	public void createInventory(int size, String n) {
		name = n;
		inv = Bukkit.createInventory(null, size, name);
		this.addShopItem(new ItemStack(Material.COOKED_PORKCHOP, 1), 25, "Porkchop", 0);
		this.addShopItem(new ItemStack(Material.COOKED_BEEF, 1), 25, "Steak", 1);
		this.addShopItem(new ItemStack(Material.COOKED_CHICKEN, 1), 25, "Chicken", 2);
		this.addShopItem(new ItemStack(Material.APPLE, 1), 5, "Apple", 3);
		this.addShopItem(new ItemStack(Material.MUSHROOM_STEW, 1), 30, "Soup", 4);
		this.addShopItem(new ItemStack(Material.CARROT, 1), 5, "Carrot", 5);
		this.addShopItem(new ItemStack(Material.MUSIC_DISC_11, 1), 5000, "Konky Sings Demon Slayer Theme", 7);
		this.addShopItem(new ItemStack(Material.MUSIC_DISC_CAT, 1), 5000, "Demon Slayer Theme", 8);
	}
	
	public void addShopItem(ItemStack i, double price, String label, int slot) {
		item_map.put(slot, i);
		ItemMeta meta = i.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + label + ": " + ChatColor.DARK_GREEN + "$" + price);
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.GRAY + "Left Click to buy 1");
		lore.add(ChatColor.GRAY + "Right Click to buy 32");
		meta.setLore(lore);
		i.setItemMeta(meta);
		inv.setItem(slot, i);
		slot_map.put(slot, price);
		
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
		if (!slot_map.containsKey(event.getSlot()))
			return;
		double price = slot_map.get(event.getSlot());
		if (player.getInventory().firstEmpty() == -1) {
			player.sendMessage(ChatColor.RED + "Theres no room in your inventory!");
			player.closeInventory();
			return;}
		if (event.getClick() == ClickType.LEFT) {
			if (eco.getBalance(player) < price) {
				player.sendMessage(ChatColor.RED + "You cannot afford that!");
				player.closeInventory();
				return;
			} else {
				eco.withdrawPlayer(player, price);
				ItemStack item = item_map.get(event.getSlot());
				player.getInventory().addItem(new ItemStack(item.getType(), 1));
				player.sendMessage(ChatColor.GREEN + "Item Received! Price paid: " + price);
				player.closeInventory();
			}
		}
		if (event.getClick() == ClickType.RIGHT) {
			double new_price = price * 32;
			if (eco.getBalance(player) < new_price) {
				player.sendMessage(ChatColor.RED + "You cannot afford that!");
				player.closeInventory();
				return;
			} else {
				eco.withdrawPlayer(player, new_price);
				ItemStack item = item_map.get(event.getSlot());
				player.getInventory().addItem(new ItemStack(item.getType(), 32));
				player.sendMessage(ChatColor.GREEN + "Item Received! Price paid: " + new_price);
				player.closeInventory();
			}
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("foodshop")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				this.openGUI(player);
				return true;
			}
		}
		return false;
	}
	
}
