package ru.soulsmine.core.spells;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import ru.soulsmine.core.SoulsMine;

public class SpellHeal implements Spell {

    @Override
    public String getId() {
        return "heal";
    }

    @Override
    public int getManaCost() {
        return 15;
    }

    @Override
    public int getCooldownSeconds() {
        return 8;
    }

    @Override
    public void cast(Player player, SoulsMine plugin) {
        double max = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        double current = player.getHealth();
        double newHealth = Math.min(current + 6, max);
        player.setHealth(newHealth);

        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1.5f);
        player.getWorld().spawnParticle(Particle.HEART, player.getLocation().add(0, 1, 0),
                10, 0.5, 0.5, 0.5, 0.1);
    }
}