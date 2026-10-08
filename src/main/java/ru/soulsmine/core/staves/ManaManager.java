package ru.soulsmine.core.staves;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ManaManager {

    private static final Map<UUID, Integer> mana = new HashMap<>();
    private static final Map<UUID, Map<String, Long>> cooldowns = new HashMap<>();

    public static final int MAX_MANA = 100;

    public static int getMana(Player player) {
        return mana.getOrDefault(player.getUniqueId(), MAX_MANA);
    }

    public static void setMana(Player player, int value) {
        mana.put(player.getUniqueId(), Math.max(0, Math.min(value, MAX_MANA)));
    }

    public static void addMana(Player player, int amount) {
        setMana(player, getMana(player) + amount);
    }

    public static boolean spendMana(Player player, int amount) {
        if (getMana(player) < amount) return false;
        setMana(player, getMana(player) - amount);
        return true;
    }

    public static boolean checkCooldown(Player player, String spellId, int seconds) {
        long now = System.currentTimeMillis();
        Map<String, Long> playerCooldowns = cooldowns.computeIfAbsent(
                player.getUniqueId(), k -> new HashMap<>());

        long last = playerCooldowns.getOrDefault(spellId, 0L);
        if (now - last < seconds * 1000L) {
            return false;
        }
        playerCooldowns.put(spellId, now);
        return true;
    }

    public static long getRemainingCooldown(Player player, String spellId, int seconds) {
        Map<String, Long> playerCooldowns = cooldowns.get(player.getUniqueId());
        if (playerCooldowns == null) return 0;
        long last = playerCooldowns.getOrDefault(spellId, 0L);
        long remaining = (last + seconds * 1000L) - System.currentTimeMillis();
        return Math.max(0, remaining / 1000);
    }
}