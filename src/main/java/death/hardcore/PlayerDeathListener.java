package death.hardcore;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.event.block.Action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
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
        Player killer = player.getKiller();

        String action = plugin.getConfig().getString("action-on-death", "spectate");
        String banMessage = plugin.getConfig().getString("ban-message", "You died!");
        String banPermissions = plugin.getConfig().getString("ban-permissions", "hardcore.death.banimmune");
        String spectatePermissions = plugin.getConfig().getString("spectate-permissions", "hardcore.death.spectateimmune");
        int banLength = plugin.getConfig().getInt("ban-length", 0);
        int startingLives = plugin.getConfig().getInt("starting-lives", 3);
        boolean banMessageEnabled = plugin.getConfig().getBoolean("ban-message-enabled", true);
        boolean banAfterDeathsEnabled = plugin.getConfig().getBoolean("ban-after-deaths-enabled", true);

        if (!plugin.getConfig().getBoolean("worlds." + player.getWorld().getName(), true)) {
            return;
        }

        int playerDeaths = plugin.getDeathsData().getInt(player.getUniqueId().toString() + ".deaths", 0) + 1;
        plugin.getDeathsData().set(player.getUniqueId().toString() + ".deaths", playerDeaths);

        int playerLives = plugin.getDeathsData().getInt(player.getUniqueId().toString() + ".lives", startingLives) - 1;
        plugin.getDeathsData().set(player.getUniqueId().toString() + ".lives", playerLives);

        boolean stealLives = plugin.getConfig().getBoolean("steal-lives", false);

        if (stealLives && killer != null) {
            int killerLives = plugin.getDeathsData().getInt(killer.getUniqueId().toString() + ".lives", startingLives) + 1;
            plugin.getDeathsData().set(killer.getUniqueId().toString() + ".lives", killerLives);
        }

        plugin.saveDeathsData();

        boolean loseLivesOnlyByPlayers = plugin.getConfig().getBoolean("lose-lives-only-by-players", false);
        if (loseLivesOnlyByPlayers && !(event.getEntity().getKiller() instanceof Player)) {
            return;
        }

        if(banAfterDeathsEnabled && playerLives <= 0){
            if (action.equalsIgnoreCase("ban") && !player.hasPermission(banPermissions)) {
                Date expiry = banLength > 0 ? new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(banLength)) : null;
                player.getServer().getBanList(BanList.Type.NAME).addBan(player.getName(), banMessageEnabled ? banMessage : null, expiry, "Hardcore plugin");
                player.kickPlayer(banMessageEnabled ? banMessage : null);
            } else if (!player.hasPermission(spectatePermissions)) {
                player.setGameMode(GameMode.SPECTATOR);
            }
        }

        boolean placeHeadOnDeath = plugin.getConfig().getBoolean("place-head-on-death", true);
        if (placeHeadOnDeath) {
            String deathTimestamp = String.valueOf(System.currentTimeMillis());
            placePlayerHead(player, deathTimestamp);
        }

        // Create a copy of the player's inventory
        ItemStack[] inventoryCopy = player.getInventory().getContents().clone();

        // Store player items in your data file
        List<ItemStack> inventoryItems = new ArrayList<>(Arrays.asList(inventoryCopy));
        List<ItemStack> armorItems = new ArrayList<>(Arrays.asList(player.getInventory().getArmorContents()));

        plugin.getDeathsData().set(player.getUniqueId().toString() + ".inventory", inventoryItems);
        plugin.getDeathsData().set(player.getUniqueId().toString() + ".armor", armorItems);

        // Don't drop
        event.getDrops().clear();

        deathsSinceLastSave++;
        if (deathsSinceLastSave >= SAVE_INTERVAL) {
            plugin.saveDeathsData();
            deathsSinceLastSave = 0;
        }
    }


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        Block block = event.getClickedBlock();

        // Check if action is right click and block is a player head
        if (block != null && block.getType() == Material.PLAYER_HEAD && (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)) {
            Skull skull = (Skull) block.getState();
            Player player = event.getPlayer();

            if (skull.hasOwner()) {
                Player deadPlayer = skull.getOwningPlayer().getPlayer();

                // Skip if deadPlayer is null or if there's no record for this player in plugin data
                if (deadPlayer == null || !plugin.getDeathsData().contains(deadPlayer.getUniqueId().toString())) {
                    return;
                }

                List<ItemStack> inventoryItems = (List<ItemStack>) plugin.getDeathsData().getList(deadPlayer.getUniqueId().toString() + ".inventory");
                List<ItemStack> armorItems = (List<ItemStack>) plugin.getDeathsData().getList(deadPlayer.getUniqueId().toString() + ".armor");

                // Combine inventory and armor items
                List<ItemStack> combinedItems = new ArrayList<>();
                if (inventoryItems != null) combinedItems.addAll(inventoryItems);
                if (armorItems != null) combinedItems.addAll(armorItems);

                // Create HeadInventoryHolder with dead player and their items
                Inventory inventory = plugin.getServer().createInventory(new HeadInventoryHolder(deadPlayer, combinedItems.toArray(new ItemStack[0])), 27, deadPlayer.getName() + "'s Items");

                if (inventoryItems != null) {
                    for (ItemStack item : inventoryItems) {
                        if (item != null) {
                            inventory.addItem(item);
                        }
                    }
                }

                if (armorItems != null) {
                    for (ItemStack item : armorItems) {
                        if (item != null) {
                            inventory.addItem(item);
                        }
                    }
                }

                player.openInventory(inventory);
            }
        }
    }



    private void placePlayerHead(Player player, String deathTimestamp) {
        Block block = player.getLocation().getBlock();
        block.setType(Material.PLAYER_HEAD);

        Skull skull = (Skull) block.getState();
        skull.setOwningPlayer(player);
        skull.getPersistentDataContainer().set(new NamespacedKey(plugin, "deathTimestamp"), PersistentDataType.STRING, deathTimestamp);
        skull.update();
    }
}

