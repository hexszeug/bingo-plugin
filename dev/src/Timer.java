import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

//Singleton
public final class Timer {
	 private static Timer instance = new Timer();
	 
	 private Timer() {}
	 
	 public static Timer get() {
		 return instance;
	 }
	 
	 private boolean isRunning = false;
	 private BukkitRunnable timer;
	 private int[] hmsTime = {0, 0, 0};
	 
	 private int setTimeTo = 0;
	 private boolean setTime = false;
	 
	 public boolean isRunning() {
		 return isRunning;
	 }
	 
	 public void restart() {
		 isRunning = true;
		 if(timer != null) timer.cancel();
		 timer = new BukkitRunnable() {
			 
			private int time = 0;
			 
			@Override
			public void run() {
				if (Timer.get().isRunning()) {
					Timer.get().calcHmsTime(time);
					time++;
					if (Timer.get().haveToSetTime()) {
						time = Timer.get().getSetTime();
					}
				}
				Timer.get().showTimer();
			}
			 
		 };
		 timer.runTaskTimer(BingoPlugin.get(), 0, 20);
	 }
	 
	 public void pause() {
		 isRunning = false;
		 for (Player player : Bukkit.getOnlinePlayers()) player.setGameMode(GameMode.SPECTATOR);
	 }
	 
	 public void play() {
		 if (timer != null) isRunning = true;
		 for (Player player : Bukkit.getOnlinePlayers()) player.setGameMode(GameMode.SURVIVAL);
	 }
	 
	 public int[] getHmsTime() {
		 return hmsTime;
	 }
	 
	 public String getTimeMsg() {
		 String h = "" + hmsTime[0];
		 String m = "" + hmsTime[1];
		 String s = "" + hmsTime[2];
		 if (h.length() < 2) h = "0" + h;
		 if (m.length() < 2) m = "0" + m;
		 if (s.length() < 2) s = "0" + s;
		 String message = "§6" + h + " §c: §6" + m + " §c: §6" + s;
		 return message;
	 }
	 
	 public void setHmsTime(int[] hmsTime) {
		 this.hmsTime = hmsTime;
	 }
	 
	 public void setTime(int time) {
		 setTimeTo = time;
		 setTime = true;
		 calcHmsTime(time);
		 showTimer();
	 }
	 
	 public boolean haveToSetTime() {
		 return setTime;
	 }
	 
	 public int getSetTime() {
		 setTime = false;
		 return setTimeTo;
	 }
	 
	 public void showTimer() {
		Collection<? extends Player> players = Bukkit.getOnlinePlayers();
		for (Player player : players) {
			player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(getTimeMsg()));
		}
	 }
	 
	 public void calcHmsTime(int time) {
		int hours = (time - (time % 3600)) / 3600;
		int minutes = (time - (hours * 3600) - time % 60) / 60;
		int seconds = time - (hours * 3600) - (minutes * 60);
		int[] hmsTime = {hours, minutes, seconds};
		Timer.get().setHmsTime(hmsTime);
	 }
}
