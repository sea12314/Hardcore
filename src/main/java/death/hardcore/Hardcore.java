package death.hardcore;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Hardcore extends JavaPlugin implements Listener {
    private File deathsFile;
    private FileConfiguration deathsData;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        getServer().getPluginManager().registerEvents(this, this);

        getLogger().info("");
        getLogger().info("");
        getLogger().info("░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░");
        getLogger().info("░░██╗░░██╗░█████╗░██████╗░██████╗░░█████╗░░█████╗░██████╗░███████╗░░░░░░░░░░░░░░░░░");
        getLogger().info("░░██║░░██║██╔══██╗██╔══██╗██╔══██╗██╔══██╗██╔══██╗██╔══██╗██╔════╝░░░░░░░░░░██╗░░░░");
        getLogger().info("░░███████║███████║██████╔╝██║░░██║██║░░╚═╝██║░░██║██████╔╝█████╗░░░░░░░░░░██████╗░░");
        getLogger().info("░░██╔══██║██╔══██║██╔══██╗██║░░██║██║░░██╗██║░░██║██╔══██╗██╔══╝░░░░░░░░░░╚═██╔═╝░░");
        getLogger().info("░░██║░░██║██║░░██║██║░░██║██████╔╝╚█████╔╝╚█████╔╝██║░░██║███████╗░░░░░░░░░░╚═╝░░░░");
        getLogger().info("░░╚═╝░░╚═╝╚═╝░░╚═╝╚═╝░░╚═╝╚═════╝░░╚════╝░░╚════╝░╚═╝░░╚═╝╚══════╝░░░░░░░░░░░░░░░░░");
        getLogger().info("░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░");
        getLogger().info("Hardcore+ Was Started Up with 0 Errors!");
        getLogger().info("");




        getCommand("lives").setExecutor(new LivesCommand(this));
        getCommand("revive").setExecutor(new ReviveCommand(this));
        this.getCommand("givelife").setExecutor(new GiveLifeCommand(this));
        this.getCommand("removelife").setExecutor(new RemoveLifeCommand(this));
        this.getCommand("clearheads").setExecutor(new ClearHeadsCommand());

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

        // Creating the configuration for the thunder-on-death feature if not already present
        if (!getConfig().contains("spawn-thunder-on-death")) {
            getConfig().set("spawn-thunder-on-death", true);
        }

        // Save the updated config file
        saveConfig();
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        // Check the value of spawn-thunder-on-death in the config
        if (this.getConfig().getBoolean("spawn-thunder-on-death")) {
            // Get the location of the player's death
            Player player = event.getEntity();
            Location deathLocation = player.getLocation();

            // Spawn a thunderbolt at the location of the player's death
            deathLocation.getWorld().strikeLightning(deathLocation);
        }
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
