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
import com.doxa.core.breath.forms.Eight;
import com.doxa.core.breath.forms.Five;
import com.doxa.core.breath.forms.Four;
import com.doxa.core.breath.forms.One;
import com.doxa.core.breath.forms.Seven;
import com.doxa.core.breath.forms.Six;
import com.doxa.core.breath.forms.Three;
import com.doxa.core.breath.forms.Two;

public class Beast extends BreathBase implements Listener {
	
	Main plugin;
	One one;
	Two two;
	Three three;
	Four four;
	Five five;
	Six six;
	Seven seven;
	Eight eight;
	public Beast(Main main, Form f, String name, One o, Two t, Three th, Four fo, Five fi, Six s, Seven se, Eight e) {
		plugin = main;
		one = o;
		two = t;
		three = th;
		four = fo;
		five = fi;
		six = s;
		seven = se;
		eight = e;
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
					one.beastOne(player);
					break;
				case 2:
					two.beastTwo(player);
					break;
				case 3:
					three.beastThree(player);
					break;
				case 4:
					four.beastFour(player);
					break;
				case 5:
					five.beastFive(player);
					break;
				case 6:
					six.beastSix(player);
					break;
				case 7:
					seven.beastSeven(player);
					break;
				case 8:
					eight.beastEight(player);
					break;
			}
		}
	}
	
}
