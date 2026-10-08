package ru.soulsmine.core;

import org.bukkit.plugin.java.JavaPlugin;
import ru.soulsmine.core.craft.CombatListener;
import ru.soulsmine.core.craft.CraftListener;
import ru.soulsmine.core.staves.StaffListener;
import ru.soulsmine.core.staves.StaffRecipes;

public class SoulsMine extends JavaPlugin {

    private static SoulsMine instance;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        new StaffRecipes(this).registerRecipes();
        getServer().getPluginManager().registerEvents(new CraftListener(this), this);
        getServer().getPluginManager().registerEvents(new CombatListener(this), this);
        getServer().getPluginManager().registerEvents(new StaffListener(this), this);

        getCommand("soulsmine").setExecutor(new SoulsMineCommand(this));

        getLogger().info("SoulsMine включён. Система редкостей загружена.");
    }

    @Override
    public void onDisable() {
        getLogger().info("SoulsMine выключен.");
    }

    public static SoulsMine getInstance() {
        return instance;
    }
}