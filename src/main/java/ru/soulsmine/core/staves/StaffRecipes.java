package ru.soulsmine.core.staves;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import ru.soulsmine.core.SoulsMine;

public class StaffRecipes {

    private final SoulsMine plugin;

    public StaffRecipes(SoulsMine plugin) {
        this.plugin = plugin;
    }

    public void registerRecipes() {
        // Рецепт для Посоха Огня (BLAZE_ROD)
        // Форма:
        // B B B
        // D B D
        // N S N
        // Где B = Blaze Powder, D = Diamond, N = Nether Star, S = Stick
        NamespacedKey fireKey = new NamespacedKey(plugin, "staff_fire_recipe");
        ShapedRecipe fireRecipe = new ShapedRecipe(fireKey, new ItemStack(Material.BLAZE_ROD));
        fireRecipe.shape("BBB", "DBD", "NSN");
        fireRecipe.setIngredient('B', Material.BLAZE_POWDER);
        fireRecipe.setIngredient('D', Material.DIAMOND);
        fireRecipe.setIngredient('N', Material.NETHER_STAR);
        fireRecipe.setIngredient('S', Material.STICK);

        Bukkit.addRecipe(fireRecipe);

        plugin.getLogger().info("Рецепт Посоха Огня зарегистрирован.");
    }
}