package death.hardcore;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReviveCommand implements CommandExecutor {
    private Hardcore plugin;

    public ReviveCommand(Hardcore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("hardcore.revive")) {
            sender.sendMessage("You do not have permission to use this command.");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage("Usage: /revive <player>");
            return false;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            sender.sendMessage("Player not found.");
            return false;
        }

        if (target.getGameMode() == GameMode.SPECTATOR) {
            target.setGameMode(GameMode.SURVIVAL);
            plugin.getDeathsData().set(target.getUniqueId().toString(), 0);
            plugin.saveDeathsData();
            sender.sendMessage(target.getName() + " has been revived and their deaths have been reset.");
        }

        if (Bukkit.getBanList(BanList.Type.NAME).isBanned(target.getName())) {
            Bukkit.getBanList(BanList.Type.NAME).pardon(target.getName());
            plugin.getDeathsData().set(target.getUniqueId().toString(), 0);
            plugin.saveDeathsData();
            sender.sendMessage(target.getName() + " has been unbanned and their deaths have been reset.");
        }

        return true;
    }
}
