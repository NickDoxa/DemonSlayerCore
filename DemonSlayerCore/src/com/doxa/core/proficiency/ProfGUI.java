package com.doxa.core.proficiency;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.doxa.core.Main;

public class ProfGUI implements Listener {
	
	Main plugin;
	Stats stats;
	public ProfGUI(Main main, Stats stat) {
		plugin = main;
		stats = stat;
	}

	private static Inventory inv;
	
	public void openGUI(Player player) {
		this.createInventory(player);
		player.openInventory(inv);
	}
	
	Map<Integer, ItemStack> item_map = new HashMap<Integer, ItemStack>();
	Map<Integer, Boolean> cos_map = new HashMap<Integer, Boolean>();
	
	public void addItem(ItemStack i, boolean cosmetic, String label, String l, int slot) {
		item_map.put(slot, i);
		ItemMeta meta = i.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', label));
		List<String> lore = new ArrayList<String>();
		if (l != null) {
			lore.add(ChatColor.translateAlternateColorCodes('&', l));
		}
		if (i.getType() == Material.EMERALD_BLOCK) {
			meta.addEnchant(Enchantment.LUCK, 1, true);
		}
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		i.setItemMeta(meta);
		cos_map.put(slot, cosmetic);
		inv.setItem(slot, i);
	}
	
	@SuppressWarnings("deprecation")
	private void createInventory(Player player) {
		inv = Bukkit.createInventory(player, 45, ChatColor.AQUA + "Proficiency Points!");
		for (int i=0; i < 45; i++) {
			this.addItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), true, "&7&o.", null, i);
		}
		
		//PLAYERS HEAD
		ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta sm = (SkullMeta) skull.getItemMeta();
		sm.setOwningPlayer(player);
		sm.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&b" + player.getName() + "'s Stats!"));
		sm.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Points: " + stats.getPoints(player));
		List<String> lore1 = new ArrayList<String>();
		lore1.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Player: " + player.getName());
		sm.setLore(lore1);
		skull.setItemMeta(sm);
		cos_map.put(0, true);
		inv.setItem(0, skull);
		
		//HELP HEAD
		ItemStack skull2 = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta sm2 = (SkullMeta) skull2.getItemMeta();
		sm2.setOwner("MHF_Question");
		sm2.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Help?"));
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Click a category to");
		lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "spend your points!");
		lore.add("");
		lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "How to acquire points:");
		lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Point 1: 50 Demon Kills");
		lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Point 2: 100 Demon Kills");
		lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Point 3: 200 Demon Kills");
		lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Point 4: 400 Demon Kills");
		lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Point 5: 600 Demon Kills");
		lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Point 6: 1000 Demon Kills");
		sm2.setLore(lore);
		skull2.setItemMeta(sm2);
		cos_map.put(9, true);
		inv.setItem(9, skull2);
		
		//BACK BUTTON
		ItemStack skull3 = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta sm3 = (SkullMeta) skull.getItemMeta();
		sm3.setOwner("MHF_arrowleft");
		sm3.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c&lBack"));
		List<String> lore2 = new ArrayList<String>();
		lore2.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Click to go back!");
		sm3.setLore(lore2);
		skull3.setItemMeta(sm3);
		cos_map.put(36, false);
		inv.setItem(36, skull3);
		
		this.addItem(new ItemStack(Material.SHIELD), false, "&c&lDefense", "&7&oClick to use point!\ntest", 2);
		this.addItem(new ItemStack(Material.DIAMOND_SWORD), false, "&b&lStrength", "&7&oClick to use point!", 4);
		this.addItem(new ItemStack(Material.FEATHER), false, "&e&lEfficiency", "&7&oClick to use point!", 6);
		
		//base redout
		this.addItem(new ItemStack(Material.REDSTONE_BLOCK), true, "&cLocked!", null, 11);
		this.addItem(new ItemStack(Material.REDSTONE_BLOCK), true, "&cLocked!", null, 20);
		this.addItem(new ItemStack(Material.REDSTONE_BLOCK), true, "&cLocked!", null, 29);
		this.addItem(new ItemStack(Material.REDSTONE_BLOCK), true, "&cLocked!", null, 38);
		this.addItem(new ItemStack(Material.REDSTONE_BLOCK), true, "&cLocked!", null, 13);
		this.addItem(new ItemStack(Material.REDSTONE_BLOCK), true, "&cLocked!", null, 22);
		this.addItem(new ItemStack(Material.REDSTONE_BLOCK), true, "&cLocked!", null, 31);
		this.addItem(new ItemStack(Material.REDSTONE_BLOCK), true, "&cLocked!", null, 40);
		this.addItem(new ItemStack(Material.REDSTONE_BLOCK), true, "&cLocked!", null, 15);
		this.addItem(new ItemStack(Material.REDSTONE_BLOCK), true, "&cLocked!", null, 24);
		this.addItem(new ItemStack(Material.REDSTONE_BLOCK), true, "&cLocked!", null, 33);
		this.addItem(new ItemStack(Material.REDSTONE_BLOCK), true, "&cLocked!", null, 42);
		
		//defense stats
		int d = stats.getDefense(player);
		if (d == 1) {
			this.addItem(new ItemStack(Material.EMERALD_BLOCK), true, "&aUnlocked!", null, 38);
		}
		if (d == 2) {
			this.addItem(new ItemStack(Material.EMERALD_BLOCK), true, "&aUnlocked!", null, 29);
			this.addItem(new ItemStack(Material.EMERALD_BLOCK), true, "&aUnlocked!", null, 38);
		}
		if (d == 3) {
			this.addItem(new ItemStack(Material.EMERALD_BLOCK), true, "&aUnlocked!", null, 20);
			this.addItem(new ItemStack(Material.EMERALD_BLOCK), true, "&aUnlocked!", null, 29);
			this.addItem(new ItemStack(Material.EMERALD_BLOCK), true, "&aUnlocked!", null, 38);
		}
		if (d >= 4) {
			this.addItem(new ItemStack(Material.EMERALD_BLOCK), true, "&aUnlocked!", null, 11);
			this.addItem(new ItemStack(Material.EMERALD_BLOCK), true, "&aUnlocked!", null, 20);
			this.addItem(new ItemStack(Material.EMERALD_BLOCK), true, "&aUnlocked!", null, 29);
			this.addItem(new ItemStack(Material.EMERALD_BLOCK), true, "&aUnlocked!", null, 38);
		}
		
		//strength stats
		int s = stats.getStrength(player);
		if (s == 1) {
			this.addItem(new ItemStack(Material.EMERALD_BLOCK), true, "&aUnlocked!", null, 40);
		}
		if (s == 2) {
			this.addItem(new ItemStack(Material.EMERALD_BLOCK), true, "&aUnlocked!", null, 31);
			this.addItem(new ItemStack(Material.EMERALD_BLOCK), true, "&aUnlocked!", null, 40);
		}
		if (s == 3) {
			this.addItem(new ItemStack(Material.EMERALD_BLOCK), true, "&aUnlocked!", null, 22);
			this.addItem(new ItemStack(Material.EMERALD_BLOCK), true, "&aUnlocked!", null, 31);
			this.addItem(new ItemStack(Material.EMERALD_BLOCK), true, "&aUnlocked!", null, 40);
		}
		if (s >= 4) {
			this.addItem(new ItemStack(Material.EMERALD_BLOCK), true, "&aUnlocked!", null, 13);
			this.addItem(new ItemStack(Material.EMERALD_BLOCK), true, "&aUnlocked!", null, 22);
			this.addItem(new ItemStack(Material.EMERALD_BLOCK), true, "&aUnlocked!", null, 31);
			this.addItem(new ItemStack(Material.EMERALD_BLOCK), true, "&aUnlocked!", null, 40);
		}
		
		//efficiency stats
		int p = stats.getProficiency(player);
		if (p == 1) {
			this.addItem(new ItemStack(Material.EMERALD_BLOCK), true, "&aUnlocked!", null, 42);
		}
		if (p == 2) {
			this.addItem(new ItemStack(Material.EMERALD_BLOCK), true, "&aUnlocked!", null, 33);
			this.addItem(new ItemStack(Material.EMERALD_BLOCK), true, "&aUnlocked!", null, 42);
		}
		if (p == 3) {
			this.addItem(new ItemStack(Material.EMERALD_BLOCK), true, "&aUnlocked!", null, 24);
			this.addItem(new ItemStack(Material.EMERALD_BLOCK), true, "&aUnlocked!", null, 33);
			this.addItem(new ItemStack(Material.EMERALD_BLOCK), true, "&aUnlocked!", null, 42);
		}
		if (p >= 4) {
			this.addItem(new ItemStack(Material.EMERALD_BLOCK), true, "&aUnlocked!", null, 15);
			this.addItem(new ItemStack(Material.EMERALD_BLOCK), true, "&aUnlocked!", null, 24);
			this.addItem(new ItemStack(Material.EMERALD_BLOCK), true, "&aUnlocked!", null, 33);
			this.addItem(new ItemStack(Material.EMERALD_BLOCK), true, "&aUnlocked!", null, 42);
		}
		
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		if(!event.getView().getTitle().contains(ChatColor.AQUA + "Proficiency Points!")) 
			return;
		if (event.getCurrentItem() == null)
			return;
		if (event.getCurrentItem().getItemMeta() == null)
			return;

		event.setCancelled(true);
		if(event.getClickedInventory().getType() == InventoryType.PLAYER)
			return;
		if (!item_map.containsKey(event.getSlot()))
			return;
		if (cos_map.get(event.getSlot()))
			return;
		//DEFENSE
		if (event.getSlot() == 2) {
			if (stats.getPoints(player) < 1) {
				player.closeInventory();
				player.sendMessage(ChatColor.RED + "You do not have any points to use!");
			} else {
				stats.removePoint(player);
				stats.setDefense(player, stats.getDefense(player)+1);
				player.closeInventory();
				this.openGUI(player);
			}
		}
		//STRENGTH
		if (event.getSlot() == 4) {
			if (stats.getPoints(player) < 1) {
				player.closeInventory();
				player.sendMessage(ChatColor.RED + "You do not have any points to use!");
			} else {
				stats.removePoint(player);
				stats.setStrength(player, stats.getStrength(player)+1);
				player.closeInventory();
				this.openGUI(player);
			}
		}
		//EFFICIENCY
		if (event.getSlot() == 6) {
			if (stats.getPoints(player) < 1) {
				player.closeInventory();
				player.sendMessage(ChatColor.RED + "You do not have any points to use!");
			} else {
				stats.removePoint(player);
				stats.setProficiency(player, stats.getProficiency(player)+1);
				player.closeInventory();
				this.openGUI(player);
			}
		}
		
		//BACK BUTTON
		if (event.getSlot() == 36) {
			player.closeInventory();
			player.getServer().dispatchCommand(player, "stats");
		}
	}
	
}
