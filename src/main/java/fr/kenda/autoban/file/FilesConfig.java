package fr.kenda.autoban.file;

import fr.kenda.autoban.AutoBan;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class FilesConfig {

    private final String name;
    private File file;
    private YamlConfiguration config;

    public FilesConfig(String name) {
        this.name = name;
        config = null;
        create();
    }

    public static FilesConfig getFile(String name) {
        FilesConfig file = null;
        for (FilesConfig filesConfig : AutoBan.getInstance().getFilesConfigs()) {
            if (filesConfig.getName().equals(name)) {
                file = filesConfig;
                break;
            }
        }
        return file;
    }

    public String getName() {
        return name;
    }

    public void create() {
        if (!AutoBan.getInstance().getDataFolder().exists()) {
            AutoBan.getInstance().getDataFolder().mkdir();
        }
        File file = new File(AutoBan.getInstance().getDataFolder(), name + ".yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                AutoBan.getInstance().getLogger().log(Level.CONFIG, AutoBan.PREFIX + ChatColor.RED + "Une erreur est survenu à la création du fichier " + name + ".yml");
                return;
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    public File getFile() {
        return new File(AutoBan.getInstance().getDataFolder(), name + ".yml");
    }

    public FileConfiguration getConfig() {
        return YamlConfiguration.loadConfiguration(getFile());
    }
}

