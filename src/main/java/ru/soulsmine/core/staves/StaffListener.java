package ru.soulsmine.core.staves;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.soulsmine.core.SoulsMine;
import ru.soulsmine.core.spells.Spell;
import ru.soulsmine.core.spells.SpellRegistry;

import java.util.List;

public class StaffListener implements Listener {

    private final SoulsMine plugin;

    public StaffListener(SoulsMine plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onUse(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR
                && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || !item.hasItemMeta()) return;

        ItemMeta meta = item.getItemMeta();
        if (!meta.hasLore()) return;

        String spellId = null;
        String rarity = null;

        List<String> lore = meta.getLore();
        for (String line : lore) {
            if (line.startsWith("§8[soulsmine_staff_") && line.endsWith("]")) {
                spellId = line
                        .replace("§8[soulsmine_staff_", "")
                        .replace("]", "")
                        .trim();
            }
            if (line.startsWith("§7Редкость: ")) {
                // Убираем цветовые коды, оставляем только имя редкости
                rarity = ChatColor.stripColor(line.replace("§7Редкость: ", "")).trim();
            }
        }
        if (spellId == null) return;

        event.setCancelled(true);

        Spell spell = SpellRegistry.get(spellId);
        if (spell == null) {
            player.sendMessage(ChatColor.RED + "Заклинание не найдено: " + spellId);
            return;
        }

        // Скидка на ману в зависимости от редкости
        int manaCost = spell.getManaCost();
        int cooldown = spell.getCooldownSeconds();
        double damageMultiplier = 1.0;

        if (rarity != null) {
            switch (rarity) {
                case "Обычный":     manaCost = (int)(manaCost * 1.0); damageMultiplier = 1.0; break;
                case "Необычный":   manaCost = (int)(manaCost * 0.9); damageMultiplier = 1.2; break;
                case "Редкий":      manaCost = (int)(manaCost * 0.8); damageMultiplier = 1.5; break;
                case "Эпический":   manaCost = (int)(manaCost * 0.7); damageMultiplier = 1.8; break;
                case "Легендарный": manaCost = (int)(manaCost * 0.6); damageMultiplier = 2.5; break;
            }
        }

        if (!ManaManager.checkCooldown(player, spell.getId(), cooldown)) {
            long remaining = ManaManager.getRemainingCooldown(player, spell.getId(), cooldown);
            player.sendMessage(ChatColor.RED + "Посох ещё не готов: " + remaining + " сек.");
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1f, 1f);
            return;
        }

        if (!ManaManager.spendMana(player, manaCost)) {
            player.sendMessage(ChatColor.RED + "Недостаточно маны. Нужно: " + manaCost
                    + ", у тебя: " + ManaManager.getMana(player));
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1f, 0.5f);
            return;
        }

        spell.cast(player, plugin);

        String rarityText = rarity != null ? " §7(" + rarity + ")" : "";
        player.sendMessage(ChatColor.AQUA + "✦ " + spell.getId() + rarityText
                + " §7| Мана: " + ManaManager.getMana(player) + "/" + ManaManager.MAX_MANA);
    }
}