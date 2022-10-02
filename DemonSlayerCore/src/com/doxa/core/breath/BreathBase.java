package com.doxa.core.breath;

import java.util.List;

import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.doxa.core.Main;
import com.doxa.core.Main.Form;

public abstract class BreathBase implements Listener {

	private Form form;
	private String name;
	private List<Particle> particles;
	private Main plugin = Main.getPlugin(Main.class);
	
	public void setForm(Form f) {
		form = f;
	}
	
	public Form getForm() {
		return form;
	}

	public void setName(String n) {
		name = n;
	}
	
	public String getName() {
		return name;
	}
	
	public void setParticleList(List<Particle> l) {
		particles = l;
	}
	
	public List<Particle> getAllParticles() {
		return particles;
	}
	
	public void addParticle(Particle p) {
		particles.add(p);
	}
	
	public boolean isPlayerThisForm(Player player) {
		if (plugin.getSetter().getPlayerForm(player).equals(this.getForm())) {
			return true;
		} else {
			return false;
		}
	}
	
}
