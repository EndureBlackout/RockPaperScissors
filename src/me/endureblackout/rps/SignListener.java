package me.endureblackout.rps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignListener implements Listener {
	RPSMain plugin;
	
	public SignListener(RPSMain instance) {
		this.plugin = instance;
	}
	
	public static Map<UUID, UUID> inGame = new HashMap<UUID, UUID>();
	public List<UUID> waitingGame = new ArrayList<UUID>();
	
	@EventHandler
	public void onPlace(SignChangeEvent e) {
		if(e.getLine(0).equalsIgnoreCase("[rps]") && e.getLine(1).equalsIgnoreCase("JoinRandom")) {
			e.setLine(0, ChatColor.WHITE + "R" + ChatColor.GRAY + "-" + ChatColor.WHITE + "P" + ChatColor.GRAY + "-" + ChatColor.WHITE + "S");
			e.setLine(1, ChatColor.WHITE + "Join Game");
			e.setLine(3, ChatColor.DARK_GRAY + "" + ChatColor.UNDERLINE + "Click to Join");
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			Player p = e.getPlayer();
			
			if(e.getClickedBlock().getType() == Material.WALL_SIGN) {
				Sign sign = (Sign)e.getClickedBlock().getState();

				if(ChatColor.stripColor(sign.getLine(0)).equalsIgnoreCase("r-p-s") && ChatColor.stripColor(sign.getLine(1)).equalsIgnoreCase("join game") && waitingGame.isEmpty()) {
					
					if(!(waitingGame.contains(p.getUniqueId()))) {
						waitingGame.add(p.getUniqueId());
						p.sendMessage(ChatColor.GOLD + "[RPS] You have been added to the queue");
					} else {
						p.sendMessage(ChatColor.RED + "[RPS] You are already in the queue");
					}
				} else if(ChatColor.stripColor(sign.getLine(0)).equalsIgnoreCase("r-p-s") && ChatColor.stripColor(sign.getLine(1)).equalsIgnoreCase("join game") && waitingGame.size() >= 1) {
					
					if(waitingGame.contains(p.getUniqueId())) {
						p.sendMessage(ChatColor.RED + "[RPS] You are already in the queue");
						return;
					}
					
					UUID pID = waitingGame.get(0);
					
					waitingGame.remove(pID);
					
					inGame.put(p.getUniqueId(), pID);
					
					for(Player p1 : Bukkit.getOnlinePlayers()) {
						if(p1.getUniqueId().equals(pID)) {
							p.sendMessage(ChatColor.GOLD + "[RPS] You are now in a game with " + p1.getName());
							p1.sendMessage(ChatColor.GOLD + "[RPS] You are now in a game with " + p.getName());
							
							plugin.openInv(p);
							plugin.openInv(p1);
						}
					}
				}
			}
		}
	}
}
