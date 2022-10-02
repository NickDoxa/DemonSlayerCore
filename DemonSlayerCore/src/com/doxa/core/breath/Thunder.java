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
import com.doxa.core.breath.forms.Seven;
import com.doxa.core.breath.forms.Six;
import com.doxa.core.breath.forms.Three;
import com.doxa.core.breath.forms.Two;

public class Thunder extends BreathBase implements Listener {

	Main plugin;
	One one;
	Two two;
	Three three;
	Four four;
	Five five;
	Six six;
	Seven seven;
	public Thunder(Main main, Form f, String name, One o, Two t, Three th, Four fo, Five fi, Six s, Seven se) {
		plugin = main;
		one = o;
		two = t;
		three = th;
		four = fo;
		five = fi;
		six = s;
		seven = se;
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
					one.thunderOne(player);
					break;
				case 2:
					two.thunderTwo(player);
					break;
				case 3:
					three.thunderThree(player);
					break;
				case 4:
					four.thunderFour(player);
					break;
				case 5:
					five.thunderFive(player);
					break;
				case 6:
					six.thunderSix(player);
					break;
				case 7:
					seven.thunderSeven(player);
					break;
			}
		}
	}
	
}
