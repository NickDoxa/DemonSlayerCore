package com.doxa.core.misc;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import com.doxa.core.Main;

public class StaffChat implements Listener, CommandExecutor{
	
	Main plugin;
	public StaffChat(Main main) {
		plugin = main;
	}

	Map<Player, Boolean> in_chat = new HashMap<Player, Boolean>();
	
	private String prefix = ChatColor.translateAlternateColorCodes('&', "&7&l[&4&lStaffChat&7&l] ");
	
	public String getPrefix(Player player) {
		String string = prefix + ChatColor.RED + player.getName() + ChatColor.DARK_GRAY + " > " + ChatColor.YELLOW + "";
		return string;
	}
	
	public void setPlayerSC(Player player, boolean b) {
		in_chat.put(player, b);
	}
	
	public boolean isPlayerInSC(Player player) {
		if (in_chat.containsKey(player)) {
			return in_chat.get(player);
		} else {
			return false;
		}
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		String msg = event.getMessage();
		if (!this.isPlayerInSC(player))
			return;
		event.setCancelled(true);
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.hasPermission("demonslayer.staffchat")) {
				p.sendMessage(this.getPrefix(player) + " " + msg);
			}
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		if (in_chat.containsKey(event.getPlayer())) {
			if (in_chat.get(event.getPlayer())) {
				in_chat.put(event.getPlayer(), false);
				event.getPlayer().sendMessage(ChatColor.RED + "StaffChat toggled off!");
			} else {
				in_chat.put(event.getPlayer(), false);
			}
		} else {
			in_chat.put(event.getPlayer(), false);
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("staffchat") || label.equalsIgnoreCase("sc")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (player.hasPermission("demonslayer.staffchat")) {
					if (args.length < 1) {
						if (in_chat.get(player)) {
							player.sendMessage(ChatColor.RED + "StaffChat toggled off!");
							in_chat.put(player, false);
						} else {
							player.sendMessage(ChatColor.GREEN + "StaffChat toggled on!");
							in_chat.put(player, true);
						}
					} else {
						for (Player p : Bukkit.getOnlinePlayers()) {
							if (p.hasPermission("demonslayer.staffchat")) {
								String msg = "";
								for (int i=0;i<args.length;i++) {
									msg = msg + " " + args[i];
								}
								p.sendMessage(this.getPrefix(player) + msg);
							}
						}
					}
				} else {
					player.sendMessage(ChatColor.RED + "You do not have permission for this!");
				}
				return true;
			}
		}
		if (label.equalsIgnoreCase("builder")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (player.hasPermission("demonslayer.builder")) {
					if (!player.getLocation().getWorld().getName().equalsIgnoreCase("build")) {
						player.teleport(new Location(player.getServer().getWorld("build"), -53.5, 4, 22));
						player.setGameMode(GameMode.CREATIVE);
						player.sendMessage(ChatColor.AQUA + "Teleported to build world. Gamemode: " + player.getGameMode().toString());
					} else {
						player.teleport(new Location(player.getServer().getWorld("spawn"), -112.5, 109, -1.5, -45, -10));
						player.setGameMode(GameMode.SURVIVAL);
						player.sendMessage(ChatColor.AQUA + "Teleported to spawn world. Gamemode: " + player.getGameMode().toString());
					}
				}
			}
		}
		if (label.equalsIgnoreCase("modspec")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (player.hasPermission("demonslayer.mod")) {
					if (player.getGameMode() == GameMode.SPECTATOR) {
						player.setGameMode(GameMode.SURVIVAL);
					} else {
						player.setGameMode(GameMode.SPECTATOR);
					}
				}
			}
		}
		return false;
	}
	
	
}
