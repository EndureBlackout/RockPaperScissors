package me.endureblackout.rps;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class RPSMain extends JavaPlugin {
	public void onEnable() {
		Bukkit.getServer().getPluginManager().registerEvents(new SignListener(this), this);
		Bukkit.getServer().getPluginManager().registerEvents(new GameHandler(this), this);
	}
	
	public void openInv(Player p) {
		GameHandler.menu.open(p);
	}
}
