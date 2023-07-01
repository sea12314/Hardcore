package death.hardcore;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LivesCommand implements CommandExecutor {
    private final Hardcore plugin;

    public LivesCommand(Hardcore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("lives")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("This command can only be used in-game.");
                return true;
            }

            Player player = (Player) sender;
            int playerLives = plugin.getDeathsData().getInt(player.getUniqueId().toString() + ".lives", plugin.getConfig().getInt("starting-lives", 3));

            player.sendMessage(ChatColor.GREEN + "You currently have " + ChatColor.YELLOW + playerLives + ChatColor.GREEN + " lives.");
            return true;
        }
        return false;
    }
}
