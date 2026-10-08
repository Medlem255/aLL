package ru.soulsmine.core.craft;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.soulsmine.core.SoulsMine;

import java.util.List;

public class CombatListener implements Listener {

    public CombatListener(SoulsMine plugin) {
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;

        Player player = (Player) event.getDamager();
        ItemStack weapon = player.getInventory().getItemInMainHand();
        if (weapon == null || !weapon.hasItemMeta()) return;

        ItemMeta meta = weapon.getItemMeta();
        if (!meta.hasLore()) return;

        List<String> lore = meta.getLore();
        for (String line : lore) {
            if (line.startsWith("§c+ ") && line.endsWith(" к урону")) {
                String number = line
                        .replace("§c+ ", "")
                        .replace(" к урону", "")
                        .trim();
                try {
                    double bonus = Double.parseDouble(number);
                    event.setDamage(event.getDamage() + bonus);
                } catch (NumberFormatException ignored) {}
                return;
            }
        }
    }
}