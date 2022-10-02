package com.doxa.core.breath;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.doxa.core.Main;
import com.doxa.core.Main.Form;

public class Setter implements Listener, CommandExecutor {

	Main plugin;
	FileConfiguration config;
	public Setter(Main main, FileConfiguration conf) {
		plugin = main;
		config = conf;
	}
	
	public void setPlayerForm(Player player, Form form) {
		config.set(player.getUniqueId().toString(), form.toString());
		plugin.saveConfig();
	}
	
	public Form convertString(String s) {
		if (s.equalsIgnoreCase("water")) {
			return Form.WATER;
		} else if (s.equalsIgnoreCase("demon")) {
			return Form.DEMON;
		} else if (s.equalsIgnoreCase("thunder")) {
			return Form.THUNDER;
		} else if (s.equalsIgnoreCase("flame")) {
			return Form.FLAME;
		} else if (s.equalsIgnoreCase("beast")) {
			return Form.BEAST;
		} else if (s.equalsIgnoreCase("insect")) {
			return Form.INSECT;
		} else if (s.equalsIgnoreCase("sun")) {
			return Form.SUN;
		} else if (s.equalsIgnoreCase("sound")) {
			return Form.SOUND;
		} else if (s.equalsIgnoreCase("none")) {
			return Form.NONE;
		} else {
			return Form.NONE;
		}
	}
	
	public Form getPlayerForm(Player player) {
		if (config.get(player.getUniqueId().toString()) == null) {
			config.set(player.getUniqueId().toString(), Form.NONE.toString());
			plugin.saveConfig();
			return Form.NONE;
		} else {
			return this.convertString(config.getString(player.getUniqueId().toString()));
		}
	}
	
	public boolean isPlayerDemon(Player player) {
		if (this.getPlayerForm(player) == Form.DEMON) {
			return true;
		} else {
			return false;
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (!player.hasPlayedBefore()) {
			this.setPlayerForm(player, Form.WATER);
		}
	}
	
	@SuppressWarnings("deprecation")
	public void setBreath(Player player, Form form) {
		plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "lp user " + player.getName() + " parent remove water");
		plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "lp user " + player.getName() + " parent remove flame");
		plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "lp user " + player.getName() + " parent remove thunder");
		plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "lp user " + player.getName() + " parent remove demon");
		plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "lp user " + player.getName() + " parent remove beast");
		plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "lp user " + player.getName() + " parent remove insect");
		plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "lp user " + player.getName() + " parent remove sound");
		switch (form.toString().toLowerCase()) {
		case "water":
			plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "lp user " + player.getName() + " parent add water");
			break;
		case "demon":
			plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "lp user " + player.getName() + " parent add demon");
			break;
		case "sun":
			plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "lp user " + player.getName() + " parent add sun");
			break;
		case "thunder":
			plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "lp user " + player.getName() + " parent add thunder");
			break;
		case "beast":
			plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "lp user " + player.getName() + " parent add beast");
			break;
		case "flame":
			plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "lp user " + player.getName() + " parent add flame");
			break;
		case "insect":
			plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "lp user " + player.getName() + " parent add insect");
			break;
		case "sound":
			plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "lp user " + player.getName() + " parent add sound");
			break;
		}
		if (form.equals(Form.DEMON)) {
			player.setMaxHealth(40);
		} else {
			player.setMaxHealth(20);
		}
		this.setPlayerForm(player, form);
		player.sendMessage(ChatColor.LIGHT_PURPLE + "Form set to: " + ChatColor.YELLOW + form.toString().toUpperCase());


	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("choose")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (!player.hasPermission("demonslayer.rechoose")) {
					player.sendMessage(ChatColor.RED + "You do not have permission to do that! Please type /rechoose");
					return true;}
				if (player.hasPermission("demonslayer.stats.slayer") && this.getPlayerForm(player) != Form.NONE && !player.hasPermission("demonslayer.stats.hashira")) {
					player.sendMessage(ChatColor.RED + "You do not have permission to do that! Please type /rechoose");
					return true;
				}
				if (args.length == 1) {
					switch (args[0].toLowerCase()) {
						case "water":
							this.setBreath(player, Form.WATER);
							break;
						case "sound":
							this.setBreath(player, Form.SOUND);
							break;
						case "thunder":
							this.setBreath(player, Form.THUNDER);
							break;
						case "flame":
							this.setBreath(player, Form.FLAME);
							break;
						case "beast":
							this.setBreath(player, Form.BEAST);
							break;
						case "insect":
							this.setBreath(player, Form.INSECT);
							break;
						case "sun":
							if (!player.hasPermission("demonslayer.sun")) {
								player.sendMessage(ChatColor.RED + "You do not have permission for this!");
								return true;
							}
							this.setBreath(player, Form.SUN);
							break;
						case "demon":
							if (player.hasPermission("demonslayer.demon")) {
								this.setBreath(player, Form.DEMON);
							} else {
								player.sendMessage(ChatColor.RED + "You do not have permission to do that!");
							}
							break;
						default:
							player.sendMessage(ChatColor.GRAY + "Forms to choose from:");
							player.sendMessage(ChatColor.AQUA + "Water");
							player.sendMessage(ChatColor.YELLOW + "Thunder");
							player.sendMessage(ChatColor.RED + "Flame");
							player.sendMessage(ChatColor.GOLD + "Beast");
							player.sendMessage(ChatColor.DARK_AQUA + "Insect");
							player.sendMessage(ChatColor.LIGHT_PURPLE + "Sound");
							if (player.hasPermission("demonslayer.demon")) {
								player.sendMessage(ChatColor.DARK_PURPLE + "Demon");
							}
							if (player.hasPermission("demonslayer.sun")) {
								player.sendMessage(ChatColor.GOLD + "Sun");
							}
							break;
					}
				} else if (args.length == 2 && player.hasPermission("demonslayer.staff")) {
					Player target;
					try {
						target = Bukkit.getPlayer(args[1]);
					} catch (NullPointerException e) {
						target = null;
						player.sendMessage(ChatColor.RED + "Player not found!");
					}
					switch (args[0].toLowerCase()) {
					case "water":
						this.setBreath(target, Form.WATER);
						break;
					case "thunder":
						this.setBreath(target, Form.THUNDER);
						break;
					case "flame":
						this.setBreath(target, Form.FLAME);
						break;
					case "beast":
						this.setBreath(target, Form.BEAST);
						break;
					case "insect":
						this.setBreath(target, Form.INSECT);
						break;
					case "demon":
						this.setBreath(target, Form.DEMON);
						break;
					case "sun":
						this.setBreath(target, Form.SUN);
						break;
					case "sound":
						this.setBreath(target, Form.SOUND);
						break;
					default:
						player.sendMessage(ChatColor.GRAY + "Forms to choose from:");
						player.sendMessage(ChatColor.AQUA + "Water");
						player.sendMessage(ChatColor.GOLD + "Sun");
						player.sendMessage(ChatColor.DARK_PURPLE + "Demon");
						player.sendMessage(ChatColor.YELLOW + "Thunder");
						player.sendMessage(ChatColor.RED + "Flame");
						player.sendMessage(ChatColor.GOLD + "Beast");
						player.sendMessage(ChatColor.DARK_AQUA + "Insect");
						player.sendMessage(ChatColor.LIGHT_PURPLE + "Sound");
						break;
					}
				} else {
					if (!player.hasPermission("demonslayer.staff")) {
						player.sendMessage(ChatColor.RED + "Incorrect Usage: /choose <form>");
					} else {
						player.sendMessage(ChatColor.RED + "Incorrect Usage: /choose <form> {player}");
					}
				}
			} else {
				if (args.length == 2) {
					Player target;
					try {
						target = Bukkit.getPlayer(args[1]);
					} catch (NullPointerException e) {
						target = null;
						plugin.getServer().getLogger().info(ChatColor.RED + "Player not found!");
					}
					switch (args[0].toLowerCase()) {
					case "water":
						this.setBreath(target, Form.WATER);
						break;
					case "thunder":
						this.setBreath(target, Form.THUNDER);
						break;
					case "flame":
						this.setBreath(target, Form.FLAME);
						break;
					case "beast":
						this.setBreath(target, Form.BEAST);
						break;
					case "insect":
						this.setBreath(target, Form.INSECT);
						break;
					case "demon":
						this.setBreath(target, Form.DEMON);
						break;
					case "sun":
						this.setBreath(target, Form.SUN);
						break;
					case "sound":
						this.setBreath(target, Form.SOUND);
						break;
					default:
						plugin.getLogger().info(ChatColor.GRAY + "Forms to choose from:");
						plugin.getLogger().info(ChatColor.AQUA + "Water");
						plugin.getLogger().info(ChatColor.GOLD + "Sun");
						plugin.getLogger().info(ChatColor.DARK_PURPLE + "Demon");
						plugin.getLogger().info(ChatColor.YELLOW + "Thunder");
						plugin.getLogger().info(ChatColor.RED + "Flame");
						plugin.getLogger().info(ChatColor.GOLD + "Beast");
						plugin.getLogger().info(ChatColor.DARK_AQUA + "Insect");
						plugin.getLogger().info(ChatColor.LIGHT_PURPLE + "Sound");
						break;
					}
				} else {
					plugin.getServer().getLogger().info("Correct Usage: /choose <element> <player>");
				}
			}
		}
		return false;
	}

}
