package com.doxa.core.recipe;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import com.doxa.core.Main;

public class NichirinRecipe {
	
	Main plugin;
	public NichirinRecipe(Main main) {
		plugin = main;
	}

	public ShapedRecipe createSwordRecipe(ItemStack item, String localized) {
		NamespacedKey key = new NamespacedKey(plugin, localized);
		ShapedRecipe recipe = new ShapedRecipe(key, item);
		recipe.shape(" SI", "RIS", "WR ");
		recipe.setIngredient('I', Material.IRON_INGOT);
		recipe.setIngredient('W', Material.OAK_PLANKS);
		recipe.setIngredient('R', Material.REDSTONE_BLOCK);
		recipe.setIngredient('S', Material.STICK);
		return recipe;
	}
	
}
