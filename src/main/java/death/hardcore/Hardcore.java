package death.hardcore;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Hardcore extends JavaPlugin {
    private File deathsFile;
    private FileConfiguration deathsData;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        getCommand("revive").setExecutor(new ReviveCommand(this));

        File dataFolder = new File(getDataFolder(), "data");
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        deathsFile = new File(dataFolder, "deaths.yml");
        if (!deathsFile.exists()) {
            try {
                deathsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        deathsData = YamlConfiguration.loadConfiguration(deathsFile);

        // Fetching all world names
        List<String> worldNames = getServer().getWorlds().stream().map(world -> world.getName()).collect(Collectors.toList());

        // Creating the configuration for each world if not already present
        worldNames.forEach(name -> {
            if(!getConfig().contains("worlds." + name)){
                getConfig().set("worlds." + name, true);
            }
        });

        // Save the updated config file
        saveConfig();
    }

    public void saveDeathsData() {
        try {
            deathsData.save(deathsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getDeathsData() {
        return deathsData;
    }
}
