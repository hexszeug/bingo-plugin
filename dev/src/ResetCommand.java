import org.bukkit.Bukkit;
//import org.bukkit.World;
//import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
//import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.entity.Player;

public class ResetCommand implements CommandExecutor{

	public boolean onCommand(CommandSender commandSender, Command command, String commandName, String[] args) {
		if (!(commandSender instanceof Player)) return false;
		Player player = (Player) commandSender;
		if (!player.isOp()) return false;
//		World w = Bukkit.getWorld("world");
//		Bukkit.getServer().unloadWorld(w, false);
//		FileUtils.deleteDirectory(w.getWorldFolder());
		Bukkit.getServer().spigot().restart();
//		WorldCreator wc = new WorldCreator("world");
//		wc.copy(w);
//		Bukkit.getServer().createWorld(wc);
		return true;
	}
}
