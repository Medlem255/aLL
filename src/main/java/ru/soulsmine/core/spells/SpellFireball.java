package ru.soulsmine.core.spells;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import ru.soulsmine.core.SoulsMine;

public class SpellFireball implements Spell {

    @Override
    public String getId() {
        return "fireball";
    }

    @Override
    public int getManaCost() {
        return 10;
    }

    @Override
    public int getCooldownSeconds() {
        return 3;
    }

    @Override
    public void cast(Player player, SoulsMine plugin) {
        Location eye = player.getEyeLocation();
        Vector dir = eye.getDirection().normalize();

        LargeFireball fireball = (LargeFireball) player.getWorld()
                .spawnEntity(eye.add(dir.multiply(1.5)), EntityType.FIREBALL);
        fireball.setDirection(dir);
        fireball.setYield(0f);
        fireball.setIsIncendiary(false);
        fireball.setShooter(player);

        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1f, 1.2f);
        player.getWorld().spawnParticle(Particle.FLAME, eye, 30, 0.3, 0.3, 0.3, 0.05);
    }
}
