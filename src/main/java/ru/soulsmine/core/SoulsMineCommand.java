package ru.soulsmine.core;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import ru.soulsmine.core.staves.StaffCommand;

public class SoulsMineCommand implements CommandExecutor {

    private final SoulsMine plugin;

    public SoulsMineCommand(SoulsMine plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.YELLOW + "Использование:");
            sender.sendMessage(ChatColor.YELLOW + "  /soulsmine reload");
            sender.sendMessage(ChatColor.YELLOW + "  /soulsmine give <игрок> <заклинание>");
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            plugin.reloadConfig();
            sender.sendMessage(ChatColor.GREEN + "[SoulsMine] Конфиг перезагружен.");
            return true;
        }

        if (args[0].equalsIgnoreCase("give")) {
            new StaffCommand(plugin).onCommand(sender, command, label, args);
            return true;
        }

        sender.sendMessage(ChatColor.RED + "Неизвестная подкоманда.");
        return true;
    }
}