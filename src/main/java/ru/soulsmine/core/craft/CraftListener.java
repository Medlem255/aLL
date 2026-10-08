package ru.soulsmine.core.craft;

import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.soulsmine.core.SoulsMine;

import java.util.*;

public class CraftListener implements Listener {

    private final SoulsMine plugin;
    private final Random random = new Random();

    public CraftListener(SoulsMine plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        ItemStack result = event.getRecipe().getResult();
        if (result == null) return;

        ConfigurationSection items = plugin.getConfig().getConfigurationSection("items");
        if (items == null) return;

        String materialKey = result.getType().name();
        if (!items.isConfigurationSection(materialKey)) return;

        ConfigurationSection itemConfig = items.getConfigurationSection(materialKey);

        if (plugin.getConfig().getBoolean("block-shift-craft", true) && event.isShiftClick()) {
            event.setCancelled(true);
            if (event.getWhoClicked() instanceof Player) {
                String msg = plugin.getConfig().getString("shift-craft-message", "&cНельзя стаком.");
                ((Player) event.getWhoClicked()).sendMessage(color(msg));
            }
            return;
        }

        ItemStack rolled = rollItem(result.getType(), itemConfig);
        if (rolled != null) {
            event.setCurrentItem(rolled);
            if (event.getWhoClicked() instanceof Player) {
                playCraftEffects((Player) event.getWhoClicked());
            }
        }
    }

    private ItemStack rollItem(Material material, ConfigurationSection itemConfig) {
        ConfigurationSection rarities = itemConfig.getConfigurationSection("rarities");
        if (rarities == null) return null;

        double totalChance = 0;
        for (String key : rarities.getKeys(false)) {
            totalChance += rarities.getInt(key + ".chance", 0);
        }
        if (totalChance <= 0) return null;

        double roll = random.nextDouble() * totalChance;
        double cumulative = 0;
        String chosenKey = null;

        for (String key : rarities.getKeys(false)) {
            cumulative += rarities.getDouble(key + ".chance", 0);
            if (roll <= cumulative) {
                chosenKey = key;
                break;
            }
        }
        if (chosenKey == null) return null;

        ConfigurationSection r = rarities.getConfigurationSection(chosenKey);

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;

        String rarityColor = color(r.getString("color", "&7"));
        String rarityName = r.getString("name", "?");
        String displayName = itemConfig.getString("display-name", prettyName(material));
        meta.setDisplayName(rarityColor + "" + ChatColor.BOLD + displayName + " [" + rarityName + "]");

        ConfigurationSection enchants = r.getConfigurationSection("enchants");
        if (enchants != null) {
            for (String enchName : enchants.getKeys(false)) {
                Enchantment ench = Enchantment.getByName(enchName.toUpperCase());
                if (ench != null) {
                    meta.addEnchant(ench, enchants.getInt(enchName), true);
                }
            }
        }

        int extraDamage = r.getInt("extra-damage", 0);
        List<String> lore = new ArrayList<>();
        lore.add("§7Редкость: " + rarityColor + rarityName);
        lore.add("");
        if (extraDamage > 0) lore.add("§c+ " + extraDamage + " к урону");

        if (enchants != null) {
            for (String enchName : enchants.getKeys(false)) {
                Enchantment ench = Enchantment.getByName(enchName.toUpperCase());
                if (ench != null) {
                    lore.add("§b" + prettyEnchant(enchName) + ": " + enchants.getInt(enchName));
                }
            }
        }

        lore.add("");
        List<String> flavor = r.getStringList("lore");
        for (String line : flavor) {
            lore.add(color(line));
        }
        lore.add("§8[soulsmine_rolled]");

        // Если это посох — добавляем метку заклинания
        String staffSpell = itemConfig.getString("staff-spell", null);
        if (staffSpell != null) {
            lore.add("§8[soulsmine_staff_" + staffSpell + "]");
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private void playCraftEffects(Player player) {
        String soundName = plugin.getConfig().getString("effects.sound", "BLOCK_ANVIL_USE");
        String particleName = plugin.getConfig().getString("effects.particle", "ENCHANTMENT_TABLE");
        float volume = (float) plugin.getConfig().getDouble("effects.sound-volume", 1.0);
        float pitch = (float) plugin.getConfig().getDouble("effects.sound-pitch", 1.2);
        int count = plugin.getConfig().getInt("effects.particle-count", 20);

        try {
            Sound sound = Sound.valueOf(soundName.toUpperCase());
            player.getWorld().playSound(player.getLocation(), sound, volume, pitch);
        } catch (IllegalArgumentException ignored) {}

        try {
            Particle particle = Particle.valueOf(particleName.toUpperCase());
            player.getWorld().spawnParticle(particle,
                    player.getLocation().add(0, 1, 0), count, 0.5, 0.5, 0.5, 0.1);
        } catch (IllegalArgumentException ignored) {}
    }

    private String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    private String prettyName(Material material) {
        String raw = material.name().toLowerCase().replace('_', ' ');
        StringBuilder sb = new StringBuilder();
        for (String word : raw.split(" ")) {
            if (word.isEmpty()) continue;
            sb.append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(1))
                    .append(' ');
        }
        return sb.toString().trim();
    }

    private String prettyEnchant(String enchName) {
        String raw = enchName.toLowerCase().replace('_', ' ');
        StringBuilder sb = new StringBuilder();
        for (String word : raw.split(" ")) {
            if (word.isEmpty()) continue;
            sb.append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(1))
                    .append(' ');
        }
        return sb.toString().trim();
    }
}