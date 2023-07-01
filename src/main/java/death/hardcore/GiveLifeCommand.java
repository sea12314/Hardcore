package death.hardcore;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

public class GiveLifeCommand implements CommandExecutor {
    private final Hardcore plugin;

    public GiveLifeCommand(Hardcore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("givelife")) {
            if (args.length < 1 || args.length > 2) {
                sender.sendMessage("Usage: /givelife <player> [lives]");
                return false;
            }

            Player target = plugin.getServer().getPlayer(args[0]);

            if (target == null) {
                sender.sendMessage("Player not found.");
                return false;
            }

            int livesToGive = args.length == 2 && sender.isOp() ? Integer.parseInt(args[1]) : plugin.getConfig().getInt("lives-to-give", 1);

            // Fetch the current player's lives
            int playerLives = plugin.getDeathsData().getInt(target.getUniqueId().toString() + ".lives", plugin.getConfig().getInt("starting-lives", 3));

            // Add the given lives to the current player's lives
            playerLives += livesToGive;
            plugin.getDeathsData().set(target.getUniqueId().toString() + ".lives", playerLives);
            plugin.saveDeathsData();

            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6You gave " + livesToGive + " life/lives to &a" + target.getName() + "&6."));

            return true;
        }

        return false;
    }
}
