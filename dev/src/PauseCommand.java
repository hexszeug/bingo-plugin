import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PauseCommand implements CommandExecutor{

	public boolean onCommand(CommandSender commandSender, Command command, String commandName, String[] args) {
		if (!(commandSender instanceof Player)) return false;
		if (!((Player) commandSender).isOp()) return false;
		Timer.get().pause();
		return true;
	}
}
