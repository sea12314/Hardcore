package death.hardcore;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class HeadInventoryHolder implements InventoryHolder {
    private final Player player;
    private final ItemStack[] items;

    public HeadInventoryHolder(Player player, ItemStack[] items) {
        this.player = player;
        this.items = items;
    }

    public Player getPlayer() {
        return this.player;
    }

    public ItemStack[] getItems() {
        return this.items;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
