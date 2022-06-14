import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class BingoCommand implements CommandExecutor{

	public boolean onCommand(CommandSender commandSender, Command command, String commandName, String[] args) {
		if (!(commandSender instanceof Player)) return false;
		Player player = (Player) commandSender;
		Inventory gui = Players.get().getGui(player, true);
		player.openInventory(gui);
		return true;
	}
}
