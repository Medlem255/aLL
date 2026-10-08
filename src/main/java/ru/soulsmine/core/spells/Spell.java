package ru.soulsmine.core.spells;

import org.bukkit.entity.Player;
import ru.soulsmine.core.SoulsMine;

public interface Spell {
    String getId();
    int getManaCost();
    int getCooldownSeconds();
    void cast(Player player, SoulsMine plugin);
}