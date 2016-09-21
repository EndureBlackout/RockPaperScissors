package me.endureblackout.rps;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import me.endureblackout.rps.utils.IconMenu;
import me.endureblackout.rps.utils.IconMenu.OptionClickEvent;

public class GameHandler implements Listener {
	
	public Map<UUID, String> picked = new HashMap<UUID, String>();
	public Map<UUID, Integer> stats = new HashMap<UUID, Integer>();
	
	ItemStack rock = new ItemStack(Material.STONE);
	ItemMeta rockMeta = rock.getItemMeta();
	
	ItemStack paper = new ItemStack(Material.PAPER);
	ItemMeta paperMeta = paper.getItemMeta();
	
	ItemStack scissors = new ItemStack(Material.SHEARS);
	ItemMeta scissorsMeta = scissors.getItemMeta();
	
	RPSMain plugin;
	
	public static IconMenu menu;
	

	
	
	public GameHandler(RPSMain instance) {
		this.plugin = instance;
		
		menu = new IconMenu(ChatColor.RED + "RPS", 9, this::itemClick, instance);
		
		rockMeta.setDisplayName(ChatColor.GRAY + "Rock");
		paperMeta.setDisplayName(ChatColor.WHITE + "Paper");
		scissorsMeta.setDisplayName(ChatColor.DARK_GRAY + "Scissors");
		
		rock.setItemMeta(rockMeta);
		paper.setItemMeta(paperMeta);
		scissors.setItemMeta(scissorsMeta);
		
		menu.setOption(1, rock);
		menu.setOption(4, paper);
		menu.setOption(7, scissors);
	}
	
	@SuppressWarnings("deprecation")
	public void itemClick(OptionClickEvent e) {
		Player p = e.getPlayer();
		e.willClose();
		
		if(e.getItem() == rock) {
			picked.put(p.getUniqueId(), "rock");
			p.sendMessage(ChatColor.GOLD + "[RPS] You picked rock!");
		}
		
		if(e.getItem() == paper) {
			picked.put(p.getUniqueId(), "paper");
			p.sendMessage(ChatColor.GOLD + "[RPS] You picked paper");
		}
		
		if(e.getItem() == scissors) {
			picked.put(p.getUniqueId(), "scissors");
			p.sendMessage(ChatColor.GOLD + "[RPS] You picked scissors");
		}
		
		Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(this.plugin, new BukkitRunnable() {
			public void run() {
				for(Entry<UUID, UUID> k : SignListener.inGame.entrySet()) {
					if(picked.containsKey(k.getKey()) && picked.containsKey(k.getValue())) {
						System.out.print("working");
						for(Player p1 : Bukkit.getOnlinePlayers()) {
							for(Player p2 : Bukkit.getOnlinePlayers()) {
								if(p1.getUniqueId().equals(k.getKey()) && p2.getUniqueId().equals(k.getValue())) {
									if(picked.get(k.getKey()).equals("paper") && picked.get(k.getValue()).equals("rock")) {
										p1.sendMessage(ChatColor.GOLD + "[RPS] You won that round!");
										p2.sendMessage(ChatColor.GOLD + "[RPS] You lost that round!");
										picked.remove(k.getKey());
										picked.remove(k.getValue());
										menu.open(p1);
										menu.open(p2);
										return;
									} else if(picked.get(k.getKey()).equals("rock") && picked.get(k.getValue()).equals("paper")) {
										p1.sendMessage(ChatColor.GOLD + "[RPS] You lost that round!");
										p2.sendMessage(ChatColor.GOLD + "[RPS] You won that round!");
										menu.open(p1);
										menu.open(p2);
										return;
									}
								}
							}
						}	
					}
				}
			}
		}, 10 * 20);
	}
	
}
