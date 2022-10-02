package com.doxa.core.breath;

import java.util.ArrayList;

import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.doxa.core.Main;
import com.doxa.core.Main.Form;
import com.doxa.core.breath.forms.Five;
import com.doxa.core.breath.forms.Four;
import com.doxa.core.breath.forms.One;
import com.doxa.core.breath.forms.Six;
import com.doxa.core.breath.forms.Three;
import com.doxa.core.breath.forms.Two;

public class Flame extends BreathBase implements Listener {

	Main plugin;
	One one;
	Two two;
	Three three;
	Four four;
	Five five;
	Six six;
	public Flame(Main main, Form f, String name, One o, Two t, Three th, Four fo, Five fi, Six s) {
		plugin = main;
		one = o;
		two = t;
		three = th;
		four = fo;
		five = fi;
		six = s;
		this.setForm(f);
		this.setName(name);
		this.setParticleList(new ArrayList<Particle>());
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
			if (!plugin.getNichirin().isItemNichirin(player.getInventory().getItemInMainHand()))
				return;
			if (!plugin.doesFormMatch(this.getForm(), plugin.getSetter().getPlayerForm(player)))
				return;
			if (!plugin.isPlayerToggledOn(player))
				return;
			if (plugin.getSetter().isPlayerDemon(player))
				return;
			if (plugin.isPlayerInSpawn(player))
				return;
			switch (plugin.getFormSetter().getPlayerForm(player)) {
				case 1:
					one.flameOne(player);
					break;
				case 2:
					two.flameTwo(player);
					break;
				case 3:
					three.flameThree(player);
					break;
				case 4:
					four.flameFour(player);
					break;
				case 5:
					five.flameFive(player);
					break;
				case 6:
					six.flameSix(player);
					break;
			}
		}
	}
	
}
