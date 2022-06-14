import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

//Singleton
public final class Players {
	private final static Players instance = new Players();
	
	private Players() {
	}
	
	public static Players get() {
		return instance;
	}

	private Map<UUID, Map<Integer, Boolean>> plDat = new HashMap<UUID, Map<Integer, Boolean>>();
	private Map<UUID, Inventory> plGui = new HashMap<UUID, Inventory>();
	
	public void found(Player player, Material material) {
		UUID uuid = player.getUniqueId();
		Inventory items = BingoPlugin.get().getItems();
		if (!(BingoPlugin.get().hasItem(material))) return;
		int id = items.first(material);
		Map<Integer, Boolean> playerData = plDat.get(uuid);
		if (playerData.get(id)) return;
		Bukkit.broadcastMessage("§c" + player.getDisplayName() + " §6found the §areal " + BingoPlugin.get().itemName(material) + "§6!");
		player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
		playerData.replace(id, true);
		showScorebord(player);
		boolean won = true;
		for (boolean i : playerData.values()) {
			if (!i) won = false;
		}
		if (won) won(player); 
	}
	
	private void won(Player player) {
		Timer.get().pause();
		Bukkit.broadcastMessage("§6" + player.getDisplayName() + " §cwon! " + Timer.get().getTimeMsg());
		for (Player i : Bukkit.getOnlinePlayers()) {
			i.playSound(i.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1, 1);
			i.setGameMode(GameMode.SPECTATOR);
			i.setAllowFlight(true);
		}
	}
	
	public void join(Player player) {
		if (this.plDat.containsKey(player.getUniqueId())) return;
		this.plDat.put(player.getUniqueId(), getEmptyData());
		player.setGameMode(GameMode.SPECTATOR);
	}
	
	public void refresh() {
		for (UUID uuid : this.plDat.keySet()) {
			this.plDat.replace(uuid, getEmptyData());
			Player player = Bukkit.getPlayer(uuid);
			player.getInventory().clear();
			player.setGameMode(GameMode.SURVIVAL);
			player.setAllowFlight(false);
			showScorebord(player);
			player.setHealth(20);
			player.setArrowsInBody(0);
			player.setFallDistance(0);
			player.setFireTicks(0);
			player.setFoodLevel(20);
			player.setRemainingAir(0);
			player.setSaturation(20.0f);
			for (PotionEffect effect : player.getActivePotionEffects()) player.removePotionEffect(effect.getType());
			player.teleport(Bukkit.getWorld("world").getSpawnLocation());
			player.sendTitle("§cGo!", "§6Show items with /bingo", 0, 180, 20);
		}
	}
	
	private Map<Integer, Boolean> getEmptyData() {
		Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();
		for (int i = 0; i < 9; i++) {
			map.put(i, false);			
		}
		return map;
	}
	
	private Inventory selectDone(Player player, Inventory gui) {
		UUID uuid = player.getUniqueId();
		Map<Integer, Boolean> playerData = plDat.get(uuid);
		ItemStack done = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
		ItemMeta meta = done.getItemMeta();
		for (int i = 0; i < 9; i++) {
			meta.setDisplayName("§r" + BingoPlugin.get().itemName(gui.getItem(i).getType()));
			done.setItemMeta(meta);
			if (playerData.get(i)) gui.setItem(i, done);
		}
		return gui;
	}
	
	public Inventory getGui(Player player, boolean withUpdate) {
		Inventory gui;
		if (!plGui.containsKey(player.getUniqueId())) {
			gui = Bukkit.createInventory(null, InventoryType.DROPPER, "§cBingo");
			plGui.put(player.getUniqueId(), gui);
		}
		gui = plGui.get(player.getUniqueId());
		if (!withUpdate) return gui;
		ItemStack[] items = BingoPlugin.get().getItemList();
		gui.setContents(items);
		gui = selectDone(player, gui);
		plGui.replace(player.getUniqueId(), gui);
		return gui;
	}
	
	public void showScorebord(Player player) {
		ItemStack[] items = BingoPlugin.get().getItemList();
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard board = manager.getNewScoreboard();
		Objective objective = board.registerNewObjective("bingo", "dummy", "§6Bingo: §cGet these items");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		Score sc0 = objective.getScore(" ");
		sc0.setScore(10);
		int j = 9;
		for (int i = 0; i < 9; i++) {
			String color;
			if (plDat.get(player.getUniqueId()).get(i)) color = "§a";
			else color = "§c";
			Score sc = objective.getScore(color + BingoPlugin.get().itemName(items[i].getType()));
			sc.setScore(j);
			j--;
		}
		player.setScoreboard(board);
	}
}
