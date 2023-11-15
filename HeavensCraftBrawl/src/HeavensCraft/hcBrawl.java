package HeavensCraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import org.maxgamer.quickshop.Shop.ShopPurchaseEvent;

import com.earth2me.essentials.Essentials;
import com.linmalu.minigames.api.event.LinmaluMiniGamesEndEvent;

import me.ryandw11.ultrabar.api.UBossBar;
import me.ryandw11.ultrabar.api.events.BarTerminateEvent;

public class hcBrawl extends JavaPlugin implements Listener {
	
	List<Player> fighters = new ArrayList<>();
	public static String str ="";
	private long lastTerminate = System.currentTimeMillis();
	static Essentials ess = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");
	@Override
	public void onEnable() {
		Bukkit.getConsoleSender()
		.sendMessage(ChatColor.GOLD + ("hcBrawl") + ChatColor.AQUA + " 플러그인 활성화" + ChatColor.BLACK);
		Bukkit.getPluginManager().registerEvents(this, this);
		Random rand = new Random();
		int i = rand.nextInt(9);
		switch(i) {
		case 0:
			str = "달리기";
			break;
		case 1:
			str = "모루피하기";
			break;
		case 2:
			str = "등반";
			break;
		case 3:
			str = "눈치";
			break;
		case 4:
			str = "폭탄피하기";
			break;
		case 5:
			str = "땅따먹기";
			break;
		case 6:
			str = "양털찾기";
			break;
		case 7:
			str = "카트타기";
			break;
		case 8:
			str = "신호등블록";
			break;
		}
		Bukkit.getScheduler().runTaskLater(this, new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "bar msg:\"다음 미니게임까지 쿨타임 (10분) - "+str+"\" time:30 p:@a color:yellow");
			}
			
		}, 20L);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
            	if(lastTerminate - System.currentTimeMillis() > 13000) {
            		Random rand = new Random();
            		int i = rand.nextInt(9);
            		switch(i) {
            		case 0:
            			str = "달리기";
            			break;
            		case 1:
            			str = "모루피하기";
            			break;
            		case 2:
            			str = "등반";
            			break;
            		case 3:
            			str = "눈치";
            			break;
            		case 4:
            			str = "폭탄피하기";
            			break;
            		case 5:
            			str = "땅따먹기";
            			break;
            		case 6:
            			str = "양털찾기";
            			break;
            		case 7:
            			str = "카트타기";
            			break;
            		case 8:
            			str = "신호등블록";
            			break;
            		}
            		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "bar msg:\"다음 미니게임까지 쿨타임 (10분) - "+str+"\" time:30 p:@a color:yellow");

            	}
            }
        }, 2000L, 16000L);
		
	}
	@Override
	public void onDisable() {
		Bukkit.getConsoleSender()
		.sendMessage(ChatColor.GOLD + ("hcBrawl") + ChatColor.DARK_AQUA + " 플러그인 비활성화" + ChatColor.BLACK);
	}
	
	@EventHandler
	private void onEnd(LinmaluMiniGamesEndEvent e) {
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mm i give "+e.getWinner().getName()+" minigamecoin");
		Random rand = new Random();
		int i = rand.nextInt(9);
		switch(i) {
		case 0:
			str = "달리기";
			break;
		case 1:
			str = "모루피하기";
			break;
		case 2:
			str = "등반";
			break;
		case 3:
			str = "눈치";
			break;
		case 4:
			str = "폭탄피하기";
			break;
		case 5:
			str = "땅따먹기";
			break;
		case 6:
			str = "양털찾기";
			break;
		case 7:
			str = "카트타기";
			break;
		case 8:
			str = "신호등블록";
			break;
		}
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "bar msg:\"다음 미니게임까지 쿨타임 (10분) - "+str+"\" time:600 p:@a color:yellow");
	}
	
	@EventHandler
	public void onBarTerminate(BarTerminateEvent evt) {
		UBossBar b = evt.getBar(); // Gets the dead instance of UBossBar
		if(b.getMessage().contains("다음 미니게임까지")) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "미니게임 "+str);

			for(Player p : Bukkit.getServer().getOnlinePlayers()) {
				if(ess.getUser(p).isAfk()) {
					p.chat("/linmaluminigames 취소");
				}
			}
			
			this.lastTerminate=System.currentTimeMillis();
		}		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			if(cmd.getName().equals("난투") || cmd.getName().equals("난투참가")|| cmd.getName().equals("pvp난투")) {
				
				boolean chk=true;
				int cnt=0;
				Inventory inven = ((Player) sender).getInventory();
				for(ItemStack i : inven.getContents()) {
					if(i!=null) {
						cnt++;
						if(i.getType().equals(Material.ELYTRA)) {
							chk=false;
							sender.sendMessage(ChatColor.RED+"겉날개는 들고 가실 수 없습니다!");
							break;
						}
					}
					if(cnt>7) {
						chk=false;
						sender.sendMessage(ChatColor.RED+"아이템은 갑옷 포함 7개 이상 들고갈 수 없습니다!");
						break;
					}
					
				}
				if(chk==true) {
					if(fighters.contains(((Player)sender))){
						sender.sendMessage("이미 난투에 참가중입니다");
					}
					else {
						((Player) sender).teleport(new Location(this.getServer().getWorld("minigameworld"),321,145,-284));
						fighters.add((Player) sender);
					}
				}
			}
		}
		return false;
	}
	@EventHandler
	private void onLeft(PlayerQuitEvent e) {
		if(fighters.contains(e.getPlayer())) {
			outPlayer(e.getPlayer());
			e.getPlayer().teleport(new Location(this.getServer().getWorld("world"),-246,73,267));
			fighters.remove(fighters.indexOf(e.getPlayer()));			
		}
	}
	
	private void outPlayer(Player p) {
		Bukkit.dispatchCommand(this.getServer().getConsoleSender(), "adminpvp take "+p.getName()+" 1");
	}
	
	private void killPlayer(Player p) {
		Bukkit.dispatchCommand(this.getServer().getConsoleSender(), "adminpvp give "+p.getName()+" 5");
	}
	
	@EventHandler
	private void onDeath(PlayerDeathEvent e) {
		if(fighters.contains(e.getEntity())) {
			outPlayer(e.getEntity());
			killPlayer(e.getEntity().getKiller());
			for(int i=0; i<fighters.size(); i++) {
				if(!(fighters.get(i).isDead())) {
					fighters.get(i).sendMessage(ChatColor.AQUA+":: PVP "+ChatColor.GOLD+"난투"+ChatColor.AQUA+" :: "+ChatColor.GREEN+""+ChatColor.BOLD+e.getEntity().getKiller().getName()+ChatColor.WHITE+"님이 "+ChatColor.RED+""+ChatColor.BOLD+e.getEntity().getName()+ChatColor.WHITE+"님을 죽이셨습니다!");
				}
			}
			fighters.remove(fighters.indexOf(e.getEntity()));
		}
	}
}




























