package com.doxa.core.enchants;

import java.util.List;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public class DemonEnchant extends Enchantment {

	public DemonEnchant(String namespace, String n, int lvl) {
		super(NamespacedKey.minecraft(namespace));
		this.setMax(lvl);
		this.setName(n);
	}
	
	private String name;
	private List<ItemStack> enchantable;
	private int start;
	private int max;
	
	public void setStart(int i) {
		start = i;
	}
	
	public void setMax(int i) {
		max = i;
	}
	
	public void addItem(ItemStack i) {
		enchantable.add(i);
	}
	
	public List<ItemStack> getEnchantableItems() {
		return enchantable;
	}
	
	public void setName(String s) {
		name = s;
	}

	@Override
	public boolean canEnchantItem(ItemStack i) {
		if (this.getEnchantableItems().contains(i)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean conflictsWith(Enchantment e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public EnchantmentTarget getItemTarget() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getMaxLevel() {
		return max;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getStartLevel() {
		return start;
	}

	@Override
	public boolean isCursed() {
		return false;
	}

	@Override
	public boolean isTreasure() {
		return false;
	}

}
