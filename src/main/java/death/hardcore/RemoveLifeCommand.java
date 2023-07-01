package death.hardcore;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

public class RemoveLifeCommand implements CommandExecutor {
    private final Hardcore plugin;

    public RemoveLifeCommand(Hardcore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("removelife")) {
            if (!sender.isOp()) {
                sender.sendMessage(ChatColor.RED + "Only operators can use this command.");
                return true;
            }

            if (args.length != 2) {
                sender.sendMessage(ChatColor.RED + "Usage: /removelife <player> <lives>");
                return true;
            }

            Player target = plugin.getServer().getPlayer(args[0]);

            if (target == null) {
                sender.sendMessage(ChatColor.RED + "Player not found.");
                return true;
            }

            int livesToRemove = Integer.parseInt(args[1]);

            int playerLives = plugin.getDeathsData().getInt(target.getUniqueId().toString() + ".lives", plugin.getConfig().getInt("max-lives", 10)) - livesToRemove;
            plugin.getDeathsData().set(target.getUniqueId().toString() + ".lives", Math.max(0, playerLives));
            plugin.saveDeathsData();

            sender.sendMessage(ChatColor.GREEN + "You removed " + ChatColor.YELLOW + livesToRemove + ChatColor.GREEN + " life/lives from " + ChatColor.YELLOW + target.getName() + ChatColor.GREEN + ".");

            return true;
        }

        return false;
    }
}
