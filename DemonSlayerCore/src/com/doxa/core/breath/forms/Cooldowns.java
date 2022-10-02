package com.doxa.core.breath.forms;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.doxa.core.Main;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class Cooldowns {
	
	Map<Player, Map<Integer, Long>> cooldown = new HashMap<Player, Map<Integer, Long>>();
	
	public boolean isPlayerOnCooldown(Player player, int form) {
		if (cooldown.containsKey(player)) {
			if (cooldown.get(player).containsKey(form)) {
				if (cooldown.get(player).get(form) > System.currentTimeMillis()) {
					long timeleft = (cooldown.get(player).get(form) - System.currentTimeMillis()) / 1000;
					player.spigot().sendMessage(ChatMessageType.ACTION_BAR, 
							new TextComponent(ChatColor.RED + "Cannot use Form " + form + " for " + (timeleft+1) + " seconds"));
					return true;
				} else {
					return false;
				}
			} else {
				cooldown.get(player).put(form, System.currentTimeMillis());
				return false;
			}
		} else {
			Map<Integer, Long> map = new HashMap<Integer, Long>();
			map.put(form, System.currentTimeMillis());
			cooldown.put(player, map);
			return false;
		}
	}
	
	public void activateCooldown(Player player, int form, long t) {
		if (!cooldown.containsKey(player)) {
			cooldown.put(player, new HashMap<Integer, Long>());
		}
		Map<Integer, Long> map = cooldown.get(player);
		long time = this.factorStats(player, t);
		map.put(form, System.currentTimeMillis() + (time*1000));
		cooldown.put(player, map);
	}
	
	public long factorStats(Player player, long time) {
		int e = Main.getPlugin(Main.class).getStats().getProficiency(player);
		long t = time - e;
		return t;
	}
	
}
