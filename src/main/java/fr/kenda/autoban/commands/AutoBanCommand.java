package fr.kenda.autoban.commands;

import fr.kenda.autoban.AutoBan;
import fr.kenda.autoban.file.FilesConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;

public class AutoBanCommand implements CommandExecutor {

    private final YamlConfiguration config = (YamlConfiguration) AutoBan.getInstance().getConfig();
    FileConfiguration customConfig = FilesConfig.getFile("blacklist_player").getConfig();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!config.getBoolean("console_allowed")) {
            sender.sendMessage(AutoBan.PREFIX + ChatColor.GRAY + "Vous devez autorisé la console à effectué cette commande. Merci de configurer \"true\" dans le fichier de config.");
            return false;
        }

        if (sender instanceof Player) {
            Player player = (Player) sender;
            ArrayList<String> playerAllowed = (ArrayList<String>) config.getStringList("player_allowed");
            if (!playerAllowed.contains(player.getName())) return false;
        }
        if (args.length != 1) {
            sender.sendMessage(AutoBan.PREFIX + ChatColor.GRAY + "/autoban <player>");
            return false;
        }
        if (args[0].equalsIgnoreCase("reload")) {
            AutoBan.getInstance().reloadConfig();
            try {
                customConfig.load(FilesConfig.getFile("blacklist_player").getFile());
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
            sender.sendMessage(AutoBan.PREFIX + ChatColor.GRAY + "Plugin rechargé.");
            return false;
        }
        String target = args[0].toLowerCase();
        if (customConfig.getConfigurationSection("players." + target) != null) {
            sender.sendMessage(AutoBan.PREFIX + ChatColor.GRAY + "Ce joueur est déjà dans la liste des bans.");
            return false;
        }
        Player targetPlayer = Bukkit.getServer().getPlayer(target);
        if (!FilesConfig.getFile("blacklist_player").getFile().exists()) {
            FilesConfig.getFile("blacklist_player").create();
        }

        customConfig.set("players." + target + ".ip", (targetPlayer == null ? "NaN" : targetPlayer.getAddress().toString()));
        customConfig.set("players." + target + ".uuid", (targetPlayer == null ? "NaN" : targetPlayer.getUniqueId()));

        try {
            customConfig.save(FilesConfig.getFile("blacklist_player").getFile());
        } catch (IOException e) {
            sender.sendMessage(AutoBan.PREFIX + ChatColor.RED + "Une erreur est survenu pendant la sauvegarde de blacklist_player.yml");
            return false;
        }
        sender.sendMessage(AutoBan.PREFIX + ChatColor.GRAY + "le joueur " + ChatColor.GOLD + target + ChatColor.GRAY + " a été ajouté à la liste.");

        return false;
    }
}
