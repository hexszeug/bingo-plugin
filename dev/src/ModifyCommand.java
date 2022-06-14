import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class ModifyCommand implements CommandExecutor{

	public boolean onCommand(CommandSender commandSender, Command command, String commandName, String[] args) {
		if (!(commandSender instanceof Player)) return false;
		Player player = (Player) commandSender;
		if (!player.isOp()) return false;
		Inventory items = BingoPlugin.get().getItems();
		player.openInventory(items);
		return true;
	}
}
