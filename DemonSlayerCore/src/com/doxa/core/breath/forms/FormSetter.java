package com.doxa.core.breath.forms;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitScheduler;

import com.doxa.core.Main;
import com.doxa.core.Main.Form;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class FormSetter implements Listener {
	
	Main plugin;
	public FormSetter(Main main) {
		plugin = main;
	}
	
	Map<Player, Integer> form_map = new HashMap<Player, Integer>();
	Map<Player, Boolean> can_swap = new HashMap<Player, Boolean>();
	
	public void setForm(Player p, int i) {
		form_map.put(p, i);
	}
	
	public int getPlayerForm(Player p) {
		if (form_map.containsKey(p)) {
			return form_map.get(p);
		} else {
			this.setForm(p, 1);
			return form_map.get(p);
		}
	}
	
	public void setPlayerSwap(Player p, boolean b) {
		can_swap.put(p, b);
	}
	
	public boolean isPlayerInUse(Player p) {
		if (can_swap.containsKey(p)) {
			if (can_swap.get(p)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		this.setPlayerSwap(player, false);
		this.setForm(player, 1);
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (plugin.getSetter().isPlayerDemon(player))
				return;
			if (!plugin.getNichirin().isItemNichirin(player.getInventory().getItemInMainHand()))
				return;
			if (this.isPlayerInUse(player))
				return;
			if (!player.isSneaking())
				return;
			this.setPlayerSwap(player, true);
			int form = this.getPlayerForm(player);
			if (plugin.getSetter().getPlayerForm(player) == Form.WATER) {
				if (form < 8) {
					form = form + 1;
				} else {
					form = 1;
				}
			} else if (plugin.getSetter().getPlayerForm(player) == Form.THUNDER) {
				if (form < 7) {
					form = form + 1;
				} else {
					form = 1;
				}
			} else if (plugin.getSetter().getPlayerForm(player) == Form.FLAME) {
				if (form < 6) {
					form = form + 1;
				} else {
					form = 1;
				}
			} else if (plugin.getSetter().getPlayerForm(player) == Form.BEAST) {
				if (form < 8) {
					form = form + 1;
				} else {
					form = 1;
				}
			} else if (plugin.getSetter().getPlayerForm(player) == Form.INSECT) {
				if (form < 6) {
					form = form + 1;
				} else {
					form = 1;
				}
			} else if (plugin.getSetter().getPlayerForm(player) == Form.SUN) {
				if (form < 6) {
					form = form + 1;
				} else {
					form = 1;
				}
			} else if (plugin.getSetter().getPlayerForm(player) == Form.SOUND) {
				if (form < 6) {
					form = form + 1;
				} else {
					form = 1;
				}
			} else {
				return;
			}
			this.setForm(player, form);
			player.spigot().sendMessage(ChatMessageType.ACTION_BAR, 
					new TextComponent(ChatColor.GOLD + "Form: " + form));
			BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
	        scheduler.scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
	            @Override
	            public void run() {
	            	setPlayerSwap(player, false);
	            }
	        }, (10-plugin.getStats().getProficiency(player)));
		} else if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
			if (!plugin.getSetter().isPlayerDemon(player))
				return;
			if (this.isPlayerInUse(player))
				return;
			if (!player.isSneaking())
				return;
			this.setPlayerSwap(player, true);
			int form = this.getPlayerForm(player);
			if (player.hasPermission("demonslayer.donator")) {
				if (form < 8) {
					form = form + 1;
				} else {
					form = 1;
				}
			} else {
				if (form < 4) {
					form = form + 1;
				} else {
					form = 1;
				}
			}
			this.setForm(player, form);
			player.spigot().sendMessage(ChatMessageType.ACTION_BAR, 
					new TextComponent(ChatColor.LIGHT_PURPLE + "Blood Art: " + form));
			BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
	        scheduler.scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
	            @Override
	            public void run() {
	            	setPlayerSwap(player, false);
	            }
	        }, (10-plugin.getStats().getProficiency(player)));
		}
	}

}
