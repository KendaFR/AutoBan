package fr.kenda.autoban.listeners;

import fr.kenda.autoban.AutoBan;
import fr.kenda.autoban.file.FilesConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.IOException;

public class JoinPlayer implements Listener {

    FileConfiguration config = FilesConfig.getFile("blacklist_player").getConfig();

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        String address = player.getAddress().toString().substring(1, player.getAddress().toString().length() - 6);

        if(config.getConfigurationSection("players") != null) {
            for (String key : config.getConfigurationSection("players").getKeys(false)) {
                if (key.equalsIgnoreCase(player.getName().toLowerCase())) {
                    e.setJoinMessage("");
                    updateInfos(player, address, config);
                    sendMessageAndKick(player);
                    return;
                }
            }
            for (String key : config.getConfigurationSection("players").getKeys(false)) {
                if (config.getConfigurationSection("players").getString(key + ".ip").equals(address)) {
                    e.setJoinMessage("");
                    updateInfos(player, address, config);
                    sendMessageAndKick(player);
                    return;
                }
            }
        }
    }


    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        String address = player.getAddress().toString().substring(1, player.getAddress().toString().length() - 6);

        if (config.getConfigurationSection("players") != null) {
            for (String key : config.getConfigurationSection("players").getKeys(false)) {
                if (key.equalsIgnoreCase(player.getName().toLowerCase())) {
                    e.setQuitMessage("");
                    updateInfos(player, address, config);
                    sendMessageAndKick(player);
                    return;
                }
            }
            for (String key : config.getConfigurationSection("players").getKeys(false)) {
                if (config.getConfigurationSection("players").getString(key + ".ip").equals(address)) {
                    e.setQuitMessage("");
                    updateInfos(player, address, config);
                    sendMessageAndKick(player);
                    return;
                }
            }
        }
    }

    private void updateInfos(Player player, String address, FileConfiguration config) {
        config.set("players." + player.getName().toLowerCase() + ".ip", address);
        config.set("players." + player.getName().toLowerCase() + ".uuid", player.getUniqueId().toString());

        try {
            config.save(FilesConfig.getFile("blacklist_player").getFile());
        } catch (IOException xe) {
            Bukkit.getConsoleSender().sendMessage(AutoBan.PREFIX + ChatColor.RED + "Une erreur est survenu pendant la sauvegarde de blacklist_player.yml");
        }
    }

    private void sendMessageAndKick(Player target) {
        Bukkit.getOnlinePlayers().forEach(p -> {
            if (AutoBan.getInstance().getConfig().getStringList("player_allowed").contains(p.getName())) {
                p.sendMessage(AutoBan.PREFIX + ChatColor.GRAY + "Le joueur " + ChatColor.GOLD + target.getName() + ChatColor.GRAY + " à essayé de se connecter.");
            }
        });
        target.kickPlayer(AutoBan.PREFIX.substring(0, AutoBan.PREFIX.length() - 3) + "\n" + "\n" +
                ChatColor.RED + "Vous avez été blacklist du serveur. \n" +
                "En cas de problème, merci de contacter un membre du staff. \n" +
                "Ou alors contacter Kenda#9890");
    }
}