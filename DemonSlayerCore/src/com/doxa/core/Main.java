package com.doxa.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.doxa.core.breath.Beast;
import com.doxa.core.breath.Demon;
import com.doxa.core.breath.Flame;
import com.doxa.core.breath.Insect;
import com.doxa.core.breath.Setter;
import com.doxa.core.breath.Sound;
import com.doxa.core.breath.Sun;
import com.doxa.core.breath.Thunder;
import com.doxa.core.breath.Water;
import com.doxa.core.breath.forms.Eight;
import com.doxa.core.breath.forms.Five;
import com.doxa.core.breath.forms.FormSetter;
import com.doxa.core.breath.forms.Four;
import com.doxa.core.breath.forms.One;
import com.doxa.core.breath.forms.Recovery;
import com.doxa.core.breath.forms.Seven;
import com.doxa.core.breath.forms.Six;
import com.doxa.core.breath.forms.Three;
import com.doxa.core.breath.forms.Two;
import com.doxa.core.kills.Kills;
import com.doxa.core.misc.BoatDisable;
import com.doxa.core.misc.Hats;
import com.doxa.core.misc.StaffChat;
import com.doxa.core.proficiency.DamageAPI;
import com.doxa.core.proficiency.GUI;
import com.doxa.core.proficiency.ProfGUI;
import com.doxa.core.proficiency.Stats;
import com.doxa.core.proficiency.Task;
import com.doxa.core.quests.AkazaFight;
import com.doxa.core.recipe.NichirinRecipe;
import com.doxa.core.skins.SkinSetter;
import com.doxa.core.skins.Skins;

import net.milkbowl.vault.economy.Economy;

@SuppressWarnings("deprecation")
public class Main extends JavaPlugin implements Listener {
	
	public String console_prefix = "[DSC]";
	
	/*
	 * ALL PERMISSION NODES
	 * 
	 * demonslayer.staff
	 * demonslayer.demon
	 * demonslayer.donator
	 * demonslayer.rechoose
	 * demonslayer.staffchat
	 * demonslayer.skin.<name of skin>
	 * demonslayer.builder
	 * demonslayer.mod
	 * 
	 */
	
	//ECONOMY SETUP (VAULT)
	public static Economy economy = null;
    private boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }
 
        return (economy != null);
    }
    
    public Economy getVault() {
    	return economy;
    }

    //CLASSES
	public Nichirin nich;
	public Setter setter;
	public FormSetter fs;
	public Water water;
	public Demon demon;
	public Thunder thunder;
	public Flame flame;
	public Beast beast;
	public Insect insect;
	public NichirinRecipe nr;
	public BoatDisable bd;
	public Kills kills;
	public SkinSetter ss;
	public Skins skins;
	public StaffChat sc;
	public Stats stats;
	public Task task;
	public GUI gui;
	public ProfGUI pgui;
	public DamageAPI dapi;
	public Hats hat;
	public AkazaFight af;
	public Sun sun;
	public Sound sound;
	public Recovery reco;
	
	//FORM CLASSES
	public One one;
	public Two two;
	public Three three;
	public Four four;
	public Five five;
	public Six six;
	public Seven seven;
	public Eight eight;
	
	@Override
	public void onEnable() {
		this.saveDefaultConfig();
		Bukkit.getLogger().info(console_prefix + "Enabling Demon Slayer Core");
		
		//VAULT
		this.setupEconomy();
		
		//PROTOCOL LIB
		this.onLoad();
			
		//CLASS SETUPS
		this.nich = new Nichirin(this);
		setter = new Setter(this, this.getConfig());
		fs = new FormSetter(this);
		nr = new NichirinRecipe(this);
		bd = new BoatDisable(this);
		kills = new Kills(this);
		ss = new SkinSetter(this, this.getConfig());
		skins = new Skins(this, ChatColor.DARK_PURPLE + "Skin Selector", this.getVault());
		sc = new StaffChat(this);
		stats = new Stats(this, this.getConfig());
		task = new Task(this, stats);
		pgui = new ProfGUI(this, stats);
		gui = new GUI(this, task, stats, pgui);
		dapi = new DamageAPI(this, stats);
		hat = new Hats(this);
		this.af = new AkazaFight(this, this.getVault());
		
		one = new One(this);
		two = new Two(this);
		three = new Three(this);
		four = new Four(this);
		five = new Five(this);
		six = new Six(this);
		seven = new Seven(this);
		eight = new Eight(this);
		reco = new Recovery(this);
		
		//FORM SETUPS
		water = new Water(this, Form.WATER, "Water", one, two, three, four, five, six, seven, eight);
		demon = new Demon(this, Form.DEMON, "Demon", one, two, three, four, five, six, seven, eight);
		thunder = new Thunder(this, Form.THUNDER, "Thunder", one, two, three, four, five, six, seven);
		flame = new Flame(this, Form.FLAME, "Flame", one, two, three, four, five, six);
		beast = new Beast(this, Form.BEAST, "Beast", one, two, three, four, five, six, seven, eight);
		insect = new Insect(this, Form.INSECT, "Insect", one, two, three, four, five, six);
		sun = new Sun(this, Form.SUN, "Sun", one, two, three, four, five, six);
		sound = new Sound(this, Form.SOUND, "Sound", one, two, three, four, five, six);
		
		
		//EVENTS
		this.getServer().getPluginManager().registerEvents(this, this);
		this.getServer().getPluginManager().registerEvents(nich, this);
		this.getServer().getPluginManager().registerEvents(setter, this);
		this.getServer().getPluginManager().registerEvents(fs, this);
		this.getServer().getPluginManager().registerEvents(water, this);
		this.getServer().getPluginManager().registerEvents(thunder, this);
		this.getServer().getPluginManager().registerEvents(demon, this);
		this.getServer().getPluginManager().registerEvents(four, this);
		this.getServer().getPluginManager().registerEvents(bd, this);
		this.getServer().getPluginManager().registerEvents(two, this);
		this.getServer().getPluginManager().registerEvents(kills, this);
		this.getServer().getPluginManager().registerEvents(ss, this);
		this.getServer().getPluginManager().registerEvents(skins, this);
		this.getServer().getPluginManager().registerEvents(sc, this);
		this.getServer().getPluginManager().registerEvents(flame, this);
		this.getServer().getPluginManager().registerEvents(task, this);
		this.getServer().getPluginManager().registerEvents(gui, this);
		this.getServer().getPluginManager().registerEvents(pgui, this);
		this.getServer().getPluginManager().registerEvents(dapi, this);
		this.getServer().getPluginManager().registerEvents(hat, this);
		this.getServer().getPluginManager().registerEvents(af, this);
		this.getServer().getPluginManager().registerEvents(beast, this);
		this.getServer().getPluginManager().registerEvents(eight, this);
		this.getServer().getPluginManager().registerEvents(three, this);
		this.getServer().getPluginManager().registerEvents(seven, this);
		this.getServer().getPluginManager().registerEvents(insect, this);
		this.getServer().getPluginManager().registerEvents(one, this);
		this.getServer().getPluginManager().registerEvents(sun, this);
		this.getServer().getPluginManager().registerEvents(sound, this);
		this.getServer().getPluginManager().registerEvents(reco, this);
		//COMMANDS
		this.getCommand("nichirin").setExecutor(nich);
		this.getCommand("choose").setExecutor(setter);
		this.getCommand("skins").setExecutor(skins);
		this.getCommand("staffchat").setExecutor(sc);
		this.getCommand("sc").setExecutor(sc);
		this.getCommand("modspec").setExecutor(sc);
		this.getCommand("builder").setExecutor(sc);
		this.getCommand("stats").setExecutor(gui);
		this.getCommand("hatz").setExecutor(hat);
		//RECIPES
		ItemStack nichirin = new ItemStack(Material.NETHERITE_SWORD, 1);
		ItemMeta meta = nichirin.getItemMeta();
		meta.setUnbreakable(true);
		meta.setDisplayName(ChatColor.AQUA + "Nichirin Sword");
		meta.setCustomModelData(1);
		nichirin.setItemMeta(meta);
		Bukkit.addRecipe(nr.createSwordRecipe(nichirin, "netherite_sword"));
	}
	
	@Override
	public void onDisable() {
		Bukkit.getLogger().info(console_prefix + "Disabling Demon Slayer Core");
	}
	
	/*
	 * CLASS LINKS
	 */
	
	public Setter getSetter() {
		return setter;
	}
	
	public FormSetter getFormSetter() {
		return fs;
	}
	
	public SkinSetter getSkinSetter() {
		return ss;
	}
	
	public Stats getStats() {
		return stats;
	}
	
	/*
	 * BREATH FORMS
	 */
	
	public enum Form{WATER, DEMON, THUNDER, FLAME, BEAST, INSECT, SOUND, SUN, NONE}
	
	public Map<Player, Boolean> active_map = new HashMap<Player, Boolean>();
	
	public boolean isPlayerActive(Player p) {
		if (active_map.containsKey(p)) {
			if (active_map.get(p)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public boolean doesFormMatch(Form f, Form f2) {
		if (f == f2) {
			return true;
		} else {
			return false;
		}
	}
	
	public void setPlayerStatus(Player p, boolean b) {
		active_map.put(p, b);
	}
	
	public Nichirin getNichirin() {
		return nich;
	}
	
	//public Shop getShop() {
	//	return shop;
	//}
	
	Map<String, Boolean> toggled = new HashMap<String, Boolean>();
	
	public void toggleForms(Player player) {
		boolean b = toggled.get(player.getName());
		if (b) {
			toggled.put(player.getName(), false);
			if (this.getSetter().isPlayerDemon(player)) {
				player.sendMessage(ChatColor.RED + "Blood Art toggled off!");
			} else {
				player.sendMessage(ChatColor.RED + "Breath Forms toggled off!");
			}
		} else {
			toggled.put(player.getName(), true);
			if (this.getSetter().isPlayerDemon(player)) {
				player.sendMessage(ChatColor.GREEN + "Blood Art toggled on!");
			} else {
				player.sendMessage(ChatColor.GREEN + "Breath Forms toggled on!");
			}
		}
	}
	
	public boolean isPlayerToggledOn(Player player) {
		return toggled.get(player.getName());
	}
	
	public boolean isPlayerInSpawn(Player player) {
		if (player.getLocation().getWorld().getName().equalsIgnoreCase("spawn")) {
			return true;
		} else {
			return false;
		}
	}
	
	@EventHandler
	public void onJoinSetStatus(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (nich.isItemNichirin(player.getInventory().getItemInMainHand())) {
			this.setPlayerStatus(player, true);
			player.setWalkSpeed(0.6f);
		} else {
			this.setPlayerStatus(player, false);
			player.setWalkSpeed(0.2f);
		}
		/*
		 * toggle usage below;
		 */
		toggled.put(player.getName(), true);
	}
	
	@EventHandler
	public void onScrollToNichirin(PlayerItemHeldEvent event) {
		Player player = event.getPlayer();
		if (player.getInventory().getItem(event.getNewSlot()) != null) {
			if (nich.isItemNichirin(player.getInventory().getItem(event.getNewSlot()))) {
				this.setPlayerStatus(player, true);
				try {
					float f = 0.5f + (stats.getProficiency(player)/10);
					player.setWalkSpeed(f);
				} catch (IllegalArgumentException e) {
					player.setWalkSpeed(0.9f);
				}
			} else {
				this.setPlayerStatus(player, false);
				player.setWalkSpeed(0.2f);
			}
		} else {
			this.setPlayerStatus(player, false);
			player.setWalkSpeed(0.2f);
		}
	}
	
	/*
	 * METHODS THAT ONLY PERTAIN TO SPECIFIC EVENTS
	 */
	
	//SPAWN VOID RESPAWN
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (player.getWorld().getName().equals("spawn") && player.getLocation().getY() < 1) {
			player.teleport(new Location(player.getWorld(), 0, 65, 0, 90, -9));
		}
	}
	
	/*
	 *  COMMANDS
	 */
	
	public boolean isStringInt(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		} catch (NullPointerException e) {
			return false;
		}
	}
	
	public void rechoose(Player player) {
		getVault().withdrawPlayer(player, 50000);
		this.getSetter().setPlayerForm(player, Form.NONE);
		this.getServer().dispatchCommand(this.getServer().getConsoleSender(), "lp user " + player.getName() + " parent remove water");
		this.getServer().dispatchCommand(this.getServer().getConsoleSender(), "lp user " + player.getName() + " parent remove thunder");
		this.getServer().dispatchCommand(this.getServer().getConsoleSender(), "lp user " + player.getName() + " parent remove flame");
		this.getServer().dispatchCommand(this.getServer().getConsoleSender(), "lp user " + player.getName() + " parent remove demon");
		this.getServer().dispatchCommand(this.getServer().getConsoleSender(), "lp user " + player.getName() + " parent remove beast");
		this.getServer().dispatchCommand(this.getServer().getConsoleSender(), "lp user " + player.getName() + " parent remove insect");
		this.getServer().dispatchCommand(this.getServer().getConsoleSender(), "lp user " + player.getName() + " parent remove sound");
		this.getServer().dispatchCommand(this.getServer().getConsoleSender(), "lp user " + player.getName() + " parent add default");
		player.sendMessage(ChatColor.GRAY + "You have been cleansed! Choose a new form with /choose <breath>!");
	}
	
	List<Player> check = new ArrayList<Player>();
	
	@EventHandler
	public void onChat(PlayerChatEvent event) {
		Player player = event.getPlayer();
		if (check.contains(player)) {
			event.setCancelled(true);
			if (event.getMessage().contains("confirm")) {
				this.rechoose(player);
			} else {
				player.sendMessage(ChatColor.RED + "Rechoose cancelled!");
			}
			check.remove(player);
		}
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("discord")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&lDiscord&7: https://discord.gg/oasisnetwork"));
				return true;
			}
		}
		
		if (label.equalsIgnoreCase("rechoose")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (getVault().getBalance(player) < 50000) {
					player.sendMessage(ChatColor.RED + "You must have $50,000 to switch forms!");
				} else {
					player.sendMessage(ChatColor.YELLOW + "WARNING: Switching forms will take 50,000 from your current balance. If you are sure you want to do this type 'confirm' in chat."
							+ " If you do not wish to continue type 'no'.");
					check.add(player);
				}
				return true;
			}
		}
		
		if (label.equalsIgnoreCase("breath")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (args.length == 1) {
					switch (args[0].toLowerCase()) {
						case "help":
							player.sendMessage(ChatColor.GRAY + "-");
							player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Demon Slayer Help");
							player.sendMessage(ChatColor.GRAY + "Welcome to the Demon Slayer Corps. Here are some helpful hints on how our plugin works!");
							player.sendMessage("");
							player.sendMessage(ChatColor.RED + "Nichirin Swords:");
							player.sendMessage(ChatColor.WHITE + "Shift Right Click - Change Form.");
							player.sendMessage(ChatColor.WHITE + "Left Click - Use Form/Attack");
							player.sendMessage(ChatColor.WHITE + "/Breath Toggle - Turn off/on your attacks!");
							player.sendMessage("");
							player.sendMessage(ChatColor.RED + "Breath Forms:");
							player.sendMessage(ChatColor.WHITE + "Each form has a different purpose and execution. Some are mobility, some do heavy damage, others are projectile or tactical. Testing the forms"
									+ " will help you grow as a Demon Slayer. However be wary some forms have a cooldown!");
							player.sendMessage(ChatColor.GRAY + "-");
							break;
						case "version":
							player.sendMessage(ChatColor.GREEN + "Demon Slayer (BETA) - Created by Nick Doxa with help from X1XX");
							break;
						case "toggle":
							this.toggleForms(player);
							break;
						default:
							player.sendMessage(ChatColor.GREEN + "Demon Slayer (BETA) - Created by Nick Doxa with help from X1XX");
							break;
					}
				} else if (args.length == 2 && args[0].equalsIgnoreCase("help")) {
					switch (args[1].toLowerCase()) {
						case "water":
							player.sendMessage(ChatColor.AQUA + "Water Forms:");
							break;
						case "thunder":
							player.sendMessage(ChatColor.YELLOW + "Thunder Forms:");
							break;
						case "demon":
							if (player.hasPermission("demonslayer.demon")) {
								player.sendMessage(ChatColor.DARK_PURPLE + "Demon Forms:");
							} else {
								player.sendMessage(ChatColor.RED + "You dont have permission to do this!");
							}
							break;
						default:
							break;
					}
				} else {
					player.sendMessage(ChatColor.RED + "Correct Usage: /breath help <element>");
				}
			}
		}
		return false;
	}
	
}
