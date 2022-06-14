import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;

public class BingoPlugin extends JavaPlugin{
	
	private static BingoPlugin pluginInstance;
	
	public static BingoPlugin get() {
		return pluginInstance;
	}
	
	private Inventory items = Bukkit.createInventory(null, InventoryType.DROPPER);
	List<Material> materials = Arrays.asList(Material.values());
	int pointer;
	
	private void deleteWorld(File w) {
		if (w.exists()) {
			File files[] = w.listFiles();
	          for(int i=0; i<files.length; i++) {
	              if(files[i].isDirectory()) {
	                  deleteWorld(files[i]);
	              } else {
	                  files[i].delete();
	              }
	          }	
		}
	}
	
	public void onEnable() {
		deleteWorld(new File("world"));
		deleteWorld(new File("world_nether"));
		deleteWorld(new File("world_the_end"));
		
		this.getLogger().info("World deleted...");
		pluginInstance = this;
		this.getLogger().info("Started Hexs Bingo Plugin!");
		
		PluginManager pluginManager = this.getServer().getPluginManager();
		MyListener myListener = new MyListener();
		pluginManager.registerEvents(myListener, this);
		
		getCommand("start").setExecutor(new StartCommand());
		getCommand("pause").setExecutor(new PauseCommand());
		getCommand("play").setExecutor(new PlayCommand());
		getCommand("modify").setExecutor(new ModifyCommand());
		getCommand("bingo").setExecutor(new BingoCommand());
		getCommand("set-time").setExecutor(new SetTimerCommand());
		getCommand("reset").setExecutor(new ResetCommand());
	}
	
	public void onDisable() {
		this.getLogger().info("Stopped Hexs Bingo Plugin!");
	}
	
	public void generateItems() {
		Collections.shuffle(materials);
		this.items = Bukkit.createInventory(null, InventoryType.DROPPER);
		pointer = 0;
		for (int i = 0; i < 9; i++) {
			while (!materials.get(pointer).isItem()) pointer++;
			items.setItem(i, new ItemStack(materials.get(pointer)));
			pointer++;
		}
	}
	
	public void replaceItem(int id) {
		while (!materials.get(pointer).isItem()) pointer++;
		items.setItem(id, new ItemStack(materials.get(pointer)));
		pointer++;
	}
	
	public Inventory getItems() {
		return items;
	}
	
	public ItemStack[] getItemList() {
		return items.getContents();
	}
	
	public boolean hasItem(Material material) {
		return items.contains(material);
	}
	
	public String itemName(Material material) {
		char[] name = material.name().toCharArray();
		String text = "";
		for (int i = 0; i < name.length; i++) {
			if (i != 0) {
				if (name[i - 1] != '_') {
					name[i] = Character.toLowerCase(name[i]);
				}
			}
			text += name[i];
		}
		return text.replace('_', ' ');
	}
}
