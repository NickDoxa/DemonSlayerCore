package com.doxa.core.proficiency;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.doxa.core.Main;

public class Stats {
	
	Main plugin;
	FileConfiguration config;
	public Stats(Main main, FileConfiguration c) {
		plugin = main;
		config = c;
	}
	
	public void setPoints(Player player, int points) {
		config.set("stats." + player.getUniqueId().toString() + ".points", points);
		plugin.saveConfig();
	}
	
	public void addPoint(Player player) {
		this.setPoints(player, (this.getPoints(player) + 1));
	}
	
	public void removePoint(Player player) {
		this.setPoints(player, (this.getPoints(player) - 1));
	}
	
	public int getPoints(Player player) {
		return config.getInt("stats." + player.getUniqueId().toString() + ".points");
	}
	
	public int getKills(Player player) {
		return config.getInt("stats." + player.getUniqueId().toString() + ".kills");
	}
	
	public void addKill(Player player, int i) {
		final int old = this.getKills(player);
		int add = old + i;
		config.set("stats." + player.getUniqueId().toString() + ".kills", add);
		plugin.saveConfig();
	}
	
	public void setKills(Player player, int i) {
		config.set("stats." + player.getUniqueId().toString() + ".kills", i);
		plugin.saveConfig();
	}
	
	public void setStrength(Player player, int i) {
		config.set("stats." + player.getUniqueId().toString() + ".strength", i);
		plugin.saveConfig();
	}
	
	public void setDefense(Player player, int i) {
		config.set("stats." + player.getUniqueId().toString() + ".defense", i);
		plugin.saveConfig();
	}
	
	public void setProficiency(Player player, int i) {
		config.set("stats." + player.getUniqueId().toString() + ".prof", i);
		plugin.saveConfig();
	}
	
	public int getStrength(Player player) {
		return config.getInt("stats." + player.getUniqueId().toString() + ".strength");
	}
	
	public int getDefense(Player player) {
		return config.getInt("stats." + player.getUniqueId().toString() + ".defense");
	}
	
	public int getProficiency(Player player) {
		return config.getInt("stats." + player.getUniqueId().toString() + ".prof");
	}
	
}
