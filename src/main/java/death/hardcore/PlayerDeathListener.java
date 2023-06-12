package death.hardcore;

import org.bukkit.GameMode;
import org.bukkit.BanList;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class PlayerDeathListener implements Listener {
    private Hardcore plugin;
    private int deathsSinceLastSave = 0;
    private static final int SAVE_INTERVAL = 5;

    public PlayerDeathListener(Hardcore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        String action = plugin.getConfig().getString("action-on-death", "spectate");
        String banMessage = plugin.getConfig().getString("ban-message", "You died!");
        String banPermissions = plugin.getConfig().getString("ban-permissions", "hardcore.death.banimmune");
        String spectatePermissions = plugin.getConfig().getString("spectate-permissions", "hardcore.death.spectateimmune");
        int banLength = plugin.getConfig().getInt("ban-length", 0);
        int banAfterDeaths = plugin.getConfig().getInt("ban-after-deaths", 3);
        boolean banMessageEnabled = plugin.getConfig().getBoolean("ban-message-enabled", true);
        boolean banAfterDeathsEnabled = plugin.getConfig().getBoolean("ban-after-deaths-enabled", true);
        int playerDeaths = plugin.getDeathsData().getInt(player.getUniqueId().toString(), 0) + 1;

        plugin.getDeathsData().set(player.getUniqueId().toString(), playerDeaths);

        deathsSinceLastSave++;
        if (deathsSinceLastSave >= SAVE_INTERVAL) {
            plugin.saveDeathsData();
            deathsSinceLastSave = 0;
        }

        if(banAfterDeathsEnabled && playerDeaths >= banAfterDeaths){
            if (action.equalsIgnoreCase("ban") && !player.hasPermission(banPermissions)) {
                Date expiry = banLength > 0 ? new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(banLength)) : null;
                player.getServer().getBanList(BanList.Type.NAME).addBan(player.getName(), banMessageEnabled ? banMessage : null, expiry, "Hardcore plugin");
            } else if (!player.hasPermission(spectatePermissions)) {
                player.setGameMode(GameMode.SPECTATOR);
            }
        }
    }
}
