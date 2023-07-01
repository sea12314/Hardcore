package death.hardcore;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ClearHeadsCommand implements CommandExecutor, Listener {

    private final List<Block> playerHeads = new ArrayList<>();

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        if (block.getType() == Material.PLAYER_HEAD || block.getType() == Material.PLAYER_WALL_HEAD) {
            playerHeads.add(block);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.isOp()) {
                // Remove heads from world
                for (Block block : playerHeads) {
                    block.setType(Material.AIR);
                }
                playerHeads.clear();

                // Remove heads from player inventories
                for (Player inventoryPlayer : player.getWorld().getPlayers()) {
                    inventoryPlayer.getInventory().remove(Material.PLAYER_HEAD);
                }

                player.sendMessage(ChatColor.GREEN + "All player heads have been removed from this world and player inventories.");
            } else {
                player.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            }
        } else {
            sender.sendMessage("Only players can use this command.");
        }
        return true;
    }
}
