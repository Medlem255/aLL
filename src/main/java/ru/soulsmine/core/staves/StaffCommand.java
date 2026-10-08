package ru.soulsmine.core.staves;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.soulsmine.core.SoulsMine;
import ru.soulsmine.core.spells.Spell;
import ru.soulsmine.core.spells.SpellRegistry;

import java.util.ArrayList;
import java.util.List;

public class StaffCommand implements CommandExecutor {

    private final SoulsMine plugin;

    public StaffCommand(SoulsMine plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("soulsmine.admin")) {
            sender.sendMessage(ChatColor.RED + "Нет прав.");
            return true;
        }

        if (args.length < 3 || !args[0].equalsIgnoreCase("give")) {
            sender.sendMessage(ChatColor.YELLOW + "Использование: /soulsmine give <игрок> <fireball|icearrow|heal|teleport>");
            return true;
        }

        Player target = plugin.getServer().getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Игрок не найден: " + args[1]);
            return true;
        }

        String spellId = args[2].toLowerCase();
        Spell spell = SpellRegistry.get(spellId);
        if (spell == null) {
            sender.sendMessage(ChatColor.RED + "Заклинание не найдено: " + spellId);
            return true;
        }

        ItemStack staff = createStaff(spellId, spell);
        target.getInventory().addItem(staff);
        target.sendMessage(ChatColor.GREEN + "Ты получил посох: " + spellId);
        sender.sendMessage(ChatColor.GREEN + "Посох выдан игроку " + target.getName());
        return true;
    }

    private ItemStack createStaff(String spellId, Spell spell) {
        ItemStack staff = new ItemStack(Material.BLAZE_ROD);
        ItemMeta meta = staff.getItemMeta();

        String displayName;
        String description;
        switch (spellId) {
            case "fireball":
                displayName = "§6§lПосох Огня";
                description = "§7ПКМ — огненный шар";
                break;
            case "icearrow":
                displayName = "§b§lПосох Льда";
                description = "§7ПКМ — ледяная стрела";
                break;
            case "heal":
                displayName = "§a§lПосох Жизни";
                description = "§7ПКМ — восстановление HP";
                break;
            case "teleport":
                displayName = "§5§lПосох Прыжка";
                description = "§7ПКМ — телепорт вперёд";
                break;
            default:
                displayName = "§fПосох";
                description = "§7ПКМ — магия";
        }

        meta.setDisplayName(displayName);

        List<String> lore = new ArrayList<>();
        lore.add(description);
        lore.add("§8Мана: " + spell.getManaCost());
        lore.add("§8Кулдаун: " + spell.getCooldownSeconds() + " сек.");
        lore.add("");
        lore.add("§8[soulsmine_staff_" + spellId + "]");
        meta.setLore(lore);

        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        staff.setItemMeta(meta);
        return staff;
    }
}