package ru.soulsmine.core.spells;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.util.Vector;
import ru.soulsmine.core.SoulsMine;

public class SpellIceArrow implements Spell {

    @Override
    public String getId() {
        return "icearrow";
    }

    @Override
    public int getManaCost() {
        return 8;
    }

    @Override
    public int getCooldownSeconds() {
        return 2;
    }

    @Override
    public void cast(Player player, SoulsMine plugin) {
        Location eye = player.getEyeLocation();
        Vector dir = eye.getDirection().normalize();

        Snowball snowball = (Snowball) player.getWorld()
                .spawnEntity(eye.add(dir.multiply(1.5)), EntityType.SNOWBALL);
        snowball.setVelocity(dir.multiply(1.5));
        snowball.setShooter(player);

        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_SNOWBALL_THROW, 1f, 1.5f);
        player.getWorld().spawnParticle(Particle.SNOW_SHOVEL, eye, 20, 0.3, 0.3, 0.3, 0.05);
    }
}