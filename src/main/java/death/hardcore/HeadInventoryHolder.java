package death.hardcore;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class HeadInventoryHolder implements InventoryHolder {
    private final Player player;

    public HeadInventoryHolder(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
