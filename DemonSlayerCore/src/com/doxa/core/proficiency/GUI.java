package com.doxa.core.proficiency;

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
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.doxa.core.Main;
import com.doxa.core.Main.Form;

public class GUI implements Listener, CommandExecutor {

	Main plugin;
	Stats stats;
	Task task;
	ProfGUI gui;
	public GUI(Main main, Task t, Stats s, ProfGUI g) {
		plugin = main;
		stats = s;
		task = t;
		gui = g;
	}
	
	private static Inventory inv;
	
	public void openGUI(Player player) {
		player.openInventory(inv);
	}
	
	@SuppressWarnings("deprecation")
	private void createInventory(Player player) {
		inv = Bukkit.createInventory(player, 36, ChatColor.AQUA + player.getName() + "'s Stats!");
		//EXCESS
		for (int i=0; i < 36; i++) {
			this.addItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), true, false, null, "&7&o.", null, i);
		}
		//PLAYERS HEAD
		ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta sm = (SkullMeta) skull.getItemMeta();
		sm.setOwningPlayer(player);
		sm.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&b" + player.getName() + "'s Stats!"));
		this.addItem(skull, true, true, sm, null, null, 0);
		//HELP HEAD
		ItemStack skull2 = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta sm2 = (SkullMeta) skull2.getItemMeta();
		sm2.setOwner("MHF_Question");
		sm2.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Stats Help?"));
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Hover over different");
		lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "items to see your stats!");
		lore.add("");
		lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Click the Diamond");
		lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "to use your points!");
		sm2.setLore(lore);
		this.addItem(skull2, true, true, sm2, null, null, 27);
		//PROFICIENCY
		this.addItem(new ItemStack(Material.DIAMOND), false, false, null, "&b&lProficiency", "&7&oClick to open!", 12);
		//POINTS
		this.addItem(new ItemStack(Material.AMETHYST_SHARD), true, false, null, "&5&lPoints: " + stats.getPoints(player), null, 13);
		this.addItem(new ItemStack(Material.SKELETON_SKULL), true, false, null, "&4&lDemon Kills: &c" + stats.getKills(player), null, 14);
		//BREATH SWORD
		ItemStack nichirin = new ItemStack(Material.NETHERITE_SWORD);
		ItemMeta meta = nichirin.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.setCustomModelData(1);
		if (plugin.getSetter().getPlayerForm(player) == Form.WATER) {
			meta.setDisplayName(ChatColor.DARK_AQUA + "Breath: " + ChatColor.AQUA + "Water!");
		} else if (plugin.getSetter().getPlayerForm(player) == Form.FLAME) {
			meta.setDisplayName(ChatColor.DARK_RED + "Breath: " + ChatColor.RED + "Flame!");
		} else if (plugin.getSetter().getPlayerForm(player) == Form.THUNDER) {
			meta.setDisplayName(ChatColor.GOLD + "Breath: " + ChatColor.YELLOW + "Thunder!");
		} else if (plugin.getSetter().getPlayerForm(player) == Form.DEMON) {
			meta.setDisplayName(ChatColor.DARK_PURPLE + "Breath: " + ChatColor.LIGHT_PURPLE + "Demon!");
		} else {
			meta.setDisplayName(ChatColor.DARK_GRAY + "Breath: " + ChatColor.GRAY + "NONE!");
		}
		this.addItem(nichirin, true, true, meta, null, null, 21);
		//BALANCE
		this.addItem(new ItemStack(Material.EMERALD), true, false, null, "&aBalance: $" + plugin.getVault().getBalance(player), null, 22);
		//RANK
		this.addItem(new ItemStack(Material.GOLDEN_HELMET), true, false, null, "&dRank: " + getRank(player), null, 23);
	}
	
	public String getRank(Player player) {
		//ORGANIZE FROM HIGHEST PARENT TO LOWEST CHILD IN ORDER TO WORK WITH LP
		if (player.hasPermission("demonslayer.stats.hashira")) {
			return ChatColor.DARK_PURPLE + "Hashira";
		} else if (player.hasPermission("demonslayer.stats.kizuki")) {
			return ChatColor.DARK_PURPLE + "Kizuki";
		} else if (player.hasPermission("demonslayer.stats.tsuguko")) {
			return ChatColor.DARK_PURPLE + "Tsuguko";
		} else if (player.hasPermission("demonslayer.stats.slayer")) {
			return ChatColor.DARK_PURPLE + "Slayer";
		} else {
			return ChatColor.GRAY + "Beginner";
		}
	}
	
	Map<Integer, ItemStack> item_map = new HashMap<Integer, ItemStack>();
	Map<Integer, Boolean> cos_map = new HashMap<Integer, Boolean>();
	
	public void addItem(ItemStack i, boolean cosmetic, boolean special, ItemMeta special_meta, String label, String l, int slot) {
		item_map.put(slot, i);
		if (!special) {
			ItemMeta meta = i.getItemMeta();
			meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', label));
			List<String> lore = new ArrayList<String>();
			if (l != null) {
				lore.add(ChatColor.translateAlternateColorCodes('&', l));
			}
			meta.setLore(lore);
			meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			i.setItemMeta(meta);
		} else {
			i.setItemMeta(special_meta);
		}
		cos_map.put(slot, cosmetic);
		inv.setItem(slot, i);
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		if(!event.getView().getTitle().contains(ChatColor.AQUA + player.getName() + "'s Stats!")) 
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
		if (event.getSlot() == 12) {
			gui.openGUI(player);
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("stats")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (args.length < 1) {
					stats.addKill(player, task.getCurrentKills(player));
					task.setKillMap(player, 0);
					this.createInventory(player);
					this.openGUI(player);
					return true;
				} else if (args.length == 2) {
					switch (args[0].toLowerCase()) {

					case "reset":
						if (!player.hasPermission("demonslayer.staff")) {
							player.sendMessage(ChatColor.RED + "Incorrect format: Type /stats");
							return true;}
						try {
							Player target = Bukkit.getPlayer(args[1]);
							stats.setDefense(target, 0);
							stats.setProficiency(target, 0);
							stats.setStrength(target, 0);
							stats.setPoints(player, 0);
						} catch(NullPointerException e) {
							player.sendMessage(ChatColor.RED + "Player is not online!");
							return true;
						}
						break;
					default:
						if (!player.hasPermission("demonslayer.staff")) {
							player.sendMessage(ChatColor.RED + "Incorrect format: Type /stats");
							return true;}
						try {
							Player target = Bukkit.getPlayer(args[0]);
							try {
								int i = Integer.parseInt(args[1]);
								stats.setPoints(player, i);
								target.sendMessage(ChatColor.GREEN + "Points set to: " + i);
								target.sendMessage(ChatColor.GREEN + "Points set to: " + i + ", For player: " + target.getName());
							} catch(NumberFormatException e) {
								player.sendMessage(ChatColor.RED + "Proper format: /stats <player> <point (number)>");
								return true;
							}
						} catch(NullPointerException e) {
							player.sendMessage(ChatColor.RED + "Player is not online!");
							return true;
						}
						break;
					}
				} else {
					if (player.hasPermission("demonslayer.staff")) {
						player.sendMessage(ChatColor.RED + "Proper format: /stats <player> <point (number)>");
					} else {
						player.sendMessage(ChatColor.RED + "Incorrect format: Type /stats");
					}
				}
			} else {
				plugin.getServer().getLogger().info("Sorry consoles dont use stats :(");
			}
		}
		return false;
	}
	
}
