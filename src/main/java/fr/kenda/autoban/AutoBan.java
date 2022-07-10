package fr.kenda.autoban;

import fr.kenda.autoban.commands.AutoBanCommand;
import fr.kenda.autoban.file.FilesConfig;
import fr.kenda.autoban.listeners.JoinPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public final class AutoBan extends JavaPlugin {


    public static final String PREFIX = ChatColor.GOLD + "[AutoBan] » ";
    private static AutoBan instance;
    private final ArrayList<FilesConfig> filesConfigs = new ArrayList<>();

    public static AutoBan getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        filesConfigs.add(new FilesConfig("blacklist_player"));
        saveDefaultConfig();
        getCommand("autoban").setExecutor(new AutoBanCommand());
        Bukkit.getConsoleSender().sendMessage(PREFIX + ChatColor.GREEN + "Plugin started");
        Bukkit.getPluginManager().registerEvents(new JoinPlayer(), this);
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(PREFIX + ChatColor.RED + "Plugin stopped");
    }

    public ArrayList<FilesConfig> getFilesConfigs() {
        return filesConfigs;
    }
}
