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
            int playerDeaths = plugin.getDeathsData().getInt(player.getUniqueId().toString(), 0);
            int maxDeaths = plugin.getConfig().getInt("ban-after-deaths", 3);
            int livesLeft = maxDeaths - playerDeaths;

            player.sendMessage(ChatColor.GREEN + "You currently have " + ChatColor.YELLOW + livesLeft + ChatColor.GREEN + " lives left.");
            return true;
        }
        return false;
    }
}
