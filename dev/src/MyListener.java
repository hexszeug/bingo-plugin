import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MyListener implements Listener{
	
	@EventHandler
	public void onEntityPickupItem(EntityPickupItemEvent event) {
		if (!(event.getEntity() instanceof Player)) return;
		Player player = (Player) event.getEntity();
		Players.get().found(player, event.getItem().getItemStack().getType());
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (!(event.getWhoClicked() instanceof Player)) return;
		Player player = (Player) event.getWhoClicked();
		Inventory playerGui = Players.get().getGui(player, false);
		if (event.getClickedInventory().equals(playerGui)) {
			event.setCancelled(true);
			return;
		}
		Inventory items = BingoPlugin.get().getItems();
		if (event.getClickedInventory().equals(items) && event.isRightClick()) {
			BingoPlugin.get().replaceItem(event.getSlot());
			for (Player playerI : Bukkit.getOnlinePlayers()) {
				Players.get().showScorebord(playerI);
			}
			event.setCancelled(true);
			player.openInventory(BingoPlugin.get().getItems());
			return;
		}
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		ItemStack[] items = BingoPlugin.get().getItemList();
		for (ItemStack item : items) {
			if (player.getInventory().contains(item.getType())) {
				Players.get().found(player, item.getType());
			}
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Players.get().join(event.getPlayer());
	}
}