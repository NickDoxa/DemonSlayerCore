package com.doxa.core.skins;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.doxa.core.Main;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

@SuppressWarnings("deprecation")
public class SkinSetter implements Listener {
	
	Main plugin;
	FileConfiguration config;
	public SkinSetter(Main main, FileConfiguration conf) {
		config = conf;
		plugin = main;
	}
	
	public void setPlayerSkin(Player player, int CustomModelData) {
		if (CustomModelData < 1 || CustomModelData > 17) {
			config.set("skins." + player.getUniqueId().toString(), 1);
			plugin.saveConfig();
		} else {
			config.set("skins." + player.getUniqueId().toString(), CustomModelData);
			plugin.saveConfig();
		}
	}
	
	public int getPlayerSkin(Player player) {
		if (config.getInt("skins." + player.getUniqueId().toString()) < 1 || config.getInt("skins." + player.getUniqueId().toString()) > 17) {
			return 1;
		} else {
			return config.getInt("skins." + player.getUniqueId().toString());
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (!player.hasPlayedBefore()) {
			this.setPlayerSkin(player, 1);
		}
	}
	
	@EventHandler
	public void onHold(PlayerItemHeldEvent event) {
		Player player = event.getPlayer();
		ItemStack item = player.getInventory().getItem(event.getNewSlot());
		if (plugin.getNichirin().isItemNichirin(item)) {
			ItemMeta meta = item.getItemMeta();
			if (meta.getCustomModelData() == this.getPlayerSkin(player))
				return;
			meta.setCustomModelData(this.getPlayerSkin(player));
			item.setItemMeta(meta);
			player.spigot().sendMessage(ChatMessageType.ACTION_BAR, 
					new TextComponent(ChatColor.GREEN + "Sword Skin Applied!"));
		}
	}
	
	@EventHandler
	public void onPickup(PlayerPickupItemEvent event) {
		Player player = event.getPlayer();
		ItemStack item = event.getItem().getItemStack();
		if (plugin.getNichirin().isItemNichirin(item)) {
			ItemMeta meta = item.getItemMeta();
			if (meta.getCustomModelData() == this.getPlayerSkin(player))
				return;
			meta.setCustomModelData(this.getPlayerSkin(player));
			item.setItemMeta(meta);
			player.spigot().sendMessage(ChatMessageType.ACTION_BAR, 
					new TextComponent(ChatColor.GREEN + "Sword Skin Applied!"));
		}
	}
	
	/*
	 * INOSUKE DUAL WEILD BLADE
	 */
	
	public boolean isItemInosukeBladeTwo(ItemStack item) {
		if (item == null)
			return false;
		if (!item.hasItemMeta())
			return false;
		ItemMeta meta = item.getItemMeta();
		if (plugin.getNichirin().isItemNichirin(item) && meta.getCustomModelData() == 2) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isItemInosukeBladeTwo(ItemStack item, int slot) {
		if (item == null)
			return false;
		if (!item.hasItemMeta())
			return false;
		ItemMeta meta = item.getItemMeta();
		if (plugin.getNichirin().isItemNichirin(item) && meta.getCustomModelData() == 2 && slot == 40) {
			return true;
		} else {
			return false;
		}
	}
	
	public void setInosukeBlade(Player player) {
		if (player.getInventory().getItemInOffHand() == null || player.getInventory().getItemInOffHand().getType() == Material.AIR) {
			ItemStack item = new ItemStack(Material.NETHERITE_SWORD, 1);
			ItemMeta meta = item.getItemMeta();
			meta.setCustomModelData(2);
			meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
			meta.setUnbreakable(true);
			meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			meta.setDisplayName(ChatColor.AQUA + "Nichirin Sword");
			item.setItemMeta(meta);
			player.getInventory().setItemInOffHand(item);
		} else {
			return;
		}
	}
	
	@EventHandler
	public void onHoldDupe(PlayerItemHeldEvent event) {
		Player player = event.getPlayer();
		ItemStack item = player.getInventory().getItem(event.getNewSlot());
		if (plugin.getNichirin().isItemNichirin(item)) {
			if (this.isItemDuped(item)) {
				player.getInventory().remove(item);
			}
		}
	}
	
	public boolean isItemDuped(ItemStack item) {
		if (item == null || item.getType() == Material.AIR)
			return false;
		if (item.hasItemMeta()) {
			if (item.getItemMeta().hasItemFlag(ItemFlag.HIDE_POTION_EFFECTS)) {
				return true;
			}
		}
		return false;
	}
	
	public void removeInosukeBlade(Player player) {
		if (this.isItemInosukeBladeTwo(player.getInventory().getItemInOffHand())) {
			player.getInventory().setItemInOffHand(null);
		} else {
			return;
		}
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		if (this.isItemInosukeBladeTwo(event.getItemDrop().getItemStack())) {
			event.setCancelled(true);
			player.sendMessage(ChatColor.RED + "You cannot drop this sword!");
		}
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		if (this.isItemInosukeBladeTwo(player.getInventory().getItemInOffHand())) {
			event.getDrops().remove(player.getInventory().getItemInOffHand());
		}
	}
	
	@EventHandler
	public void onSwapSlots(PlayerItemHeldEvent event) {
		Player player = event.getPlayer();
		if (plugin.getNichirin().isItemNichirin(player.getInventory().getItem(event.getNewSlot()))) {
			ItemStack item = player.getInventory().getItem(event.getNewSlot());
			if (item.getItemMeta().getCustomModelData() == 2) {
				this.setInosukeBlade(player);
			} else {
				this.removeInosukeBlade(player);
			}
		} else {
			this.removeInosukeBlade(player);
		}
	}
	
	@EventHandler
	public void onMoveItem(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		if(event.getClickedInventory() == null)
			return;
		if (this.isItemInosukeBladeTwo(event.getClickedInventory().getItem(event.getSlot()), event.getSlot())) {
			event.setCancelled(true);
			player.sendMessage(ChatColor.RED + "You cannot move this sword!");
		}
		if (event.getClick() == ClickType.SWAP_OFFHAND && this.isItemInosukeBladeTwo(player.getInventory().getItemInOffHand())) {
			event.setCancelled(true);
			player.sendMessage(ChatColor.RED + "You cannot swap offhand while using Inosuke Skin!");
		}
	}
	
	/*
	 * UZUI SWORD SHIT
	 */
	
	public boolean isItemUzuiBladeTwo(ItemStack item) {
		if (item == null)
			return false;
		if (!item.hasItemMeta())
			return false;
		ItemMeta meta = item.getItemMeta();
		if (plugin.getNichirin().isItemNichirin(item) && meta.getCustomModelData() == 14) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isItemUzuiBladeTwo(ItemStack item, int slot) {
		if (item == null)
			return false;
		if (!item.hasItemMeta())
			return false;
		ItemMeta meta = item.getItemMeta();
		if (plugin.getNichirin().isItemNichirin(item) && meta.getCustomModelData() == 14 && slot == 40) {
			return true;
		} else {
			return false;
		}
	}
	
	public void setUzuiBlade(Player player) {
		if (player.getInventory().getItemInOffHand() == null || player.getInventory().getItemInOffHand().getType() == Material.AIR) {
			ItemStack item = new ItemStack(Material.NETHERITE_SWORD, 1);
			ItemMeta meta = item.getItemMeta();
			meta.setCustomModelData(14);
			meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
			meta.setUnbreakable(true);
			meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			meta.setDisplayName(ChatColor.AQUA + "Nichirin Sword");
			item.setItemMeta(meta);
			player.getInventory().setItemInOffHand(item);
		} else {
			return;
		}
	}
	
	@EventHandler
	public void onHoldUzuiDupe(PlayerItemHeldEvent event) {
		Player player = event.getPlayer();
		ItemStack item = player.getInventory().getItem(event.getNewSlot());
		if (plugin.getNichirin().isItemNichirin(item)) {
			if (this.isItemUzuiDuped(item)) {
				player.getInventory().remove(item);
			}
		}
	}
	
	public boolean isItemUzuiDuped(ItemStack item) {
		if (item == null || item.getType() == Material.AIR)
			return false;
		if (item.hasItemMeta()) {
			if (item.getItemMeta().hasItemFlag(ItemFlag.HIDE_POTION_EFFECTS)) {
				return true;
			}
		}
		return false;
	}
	
	public void removeUzuiBlade(Player player) {
		if (this.isItemUzuiBladeTwo(player.getInventory().getItemInOffHand())) {
			player.getInventory().setItemInOffHand(null);
		} else {
			return;
		}
	}
	
	@EventHandler
	public void onUzuiDrop(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		if (this.isItemUzuiBladeTwo(event.getItemDrop().getItemStack())) {
			event.setCancelled(true);
			player.sendMessage(ChatColor.RED + "You cannot drop this sword!");
		}
	}
	
	@EventHandler
	public void onUzuiDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		if (this.isItemUzuiBladeTwo(player.getInventory().getItemInOffHand())) {
			event.getDrops().remove(player.getInventory().getItemInOffHand());
		}
	}
	
	@EventHandler
	public void onUzuiSwapSlots(PlayerItemHeldEvent event) {
		Player player = event.getPlayer();
		if (plugin.getNichirin().isItemNichirin(player.getInventory().getItem(event.getNewSlot()))) {
			ItemStack item = player.getInventory().getItem(event.getNewSlot());
			if (item.getItemMeta().getCustomModelData() == 2) {
				this.setUzuiBlade(player);
			} else {
				this.removeUzuiBlade(player);
			}
		} else {
			this.removeUzuiBlade(player);
		}
	}
	
	@EventHandler
	public void onUzuiMoveItem(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		if(event.getClickedInventory() == null)
			return;
		if (this.isItemUzuiBladeTwo(event.getClickedInventory().getItem(event.getSlot()), event.getSlot())) {
			event.setCancelled(true);
			player.sendMessage(ChatColor.RED + "You cannot move this sword!");
		}
		if (event.getClick() == ClickType.SWAP_OFFHAND && this.isItemUzuiBladeTwo(player.getInventory().getItemInOffHand())) {
			event.setCancelled(true);
			player.sendMessage(ChatColor.RED + "You cannot swap offhand while using Inosuke Skin!");
		}
	}
	
}
