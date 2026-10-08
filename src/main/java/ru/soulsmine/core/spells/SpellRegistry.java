package ru.soulsmine.core.spells;

import java.util.HashMap;
import java.util.Map;

public class SpellRegistry {

    private static final Map<String, Spell> spells = new HashMap<>();

    static {
        register(new SpellFireball());
        register(new SpellIceArrow());
        register(new SpellHeal());
        register(new SpellTeleport());
    }

    private static void register(Spell spell) {
        spells.put(spell.getId().toLowerCase(), spell);
    }

    public static Spell get(String id) {
        if (id == null) return null;
        return spells.get(id.toLowerCase());
    }
}