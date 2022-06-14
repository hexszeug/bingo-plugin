import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetTimerCommand implements CommandExecutor{

	public boolean onCommand(CommandSender commandSender, Command command, String commandName, String[] args) {
		if (!(commandSender instanceof Player)) return false;
		if (!((Player) commandSender).isOp()) return false;
		int time;
		try {
			time = Integer.parseInt(args[0]);
			Timer.get().setTime(time);
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}
}
