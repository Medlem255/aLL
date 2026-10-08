package ru.soulsmine.core.spells;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import ru.soulsmine.core.SoulsMine;

public class SpellTeleport implements Spell {

    @Override
    public String getId() {
        return "teleport";
    }

    @Override
    public int getManaCost() {
        return 12;
    }

    @Override
    public int getCooldownSeconds() {
        return 5;
    }

    @Override
    public void cast(Player player, SoulsMine plugin) {
        Location start = player.getLocation();
        Vector dir = start.getDirection().normalize();
        Location target = null;

        for (double d = 1; d <= 12; d += 0.5) {
            Location check = start.clone().add(dir.clone().multiply(d));
            Block block = check.getBlock();
            Block above = check.clone().add(0, 1, 0).getBlock();
            if (!block.getType().isSolid() && !above.getType().isSolid()) {
                target = check;
            } else {
                break;
            }
        }

        if (target == null) {
            player.sendMessage("§cНет свободного места для телепорта.");
            return;
        }

        player.getWorld().spawnParticle(Particle.PORTAL, start, 30, 0.3, 0.5, 0.3, 0.5);
        player.teleport(target);
        player.getWorld().spawnParticle(Particle.PORTAL, target, 30, 0.3, 0.5, 0.3, 0.5);
        player.getWorld().playSound(target, Sound.ENTITY_ENDERMEN_TELEPORT, 1f, 1.2f);
    }
}