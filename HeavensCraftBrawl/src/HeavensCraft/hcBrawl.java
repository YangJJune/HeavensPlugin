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
		.sendMessage(ChatColor.GOLD + ("hcBrawl") + ChatColor.AQUA + " �÷����� Ȱ��ȭ" + ChatColor.BLACK);
		Bukkit.getPluginManager().registerEvents(this, this);
		Random rand = new Random();
		int i = rand.nextInt(9);
		switch(i) {
		case 0:
			str = "�޸���";
			break;
		case 1:
			str = "������ϱ�";
			break;
		case 2:
			str = "���";
			break;
		case 3:
			str = "��ġ";
			break;
		case 4:
			str = "��ź���ϱ�";
			break;
		case 5:
			str = "�����Ա�";
			break;
		case 6:
			str = "����ã��";
			break;
		case 7:
			str = "īƮŸ��";
			break;
		case 8:
			str = "��ȣ����";
			break;
		}
		Bukkit.getScheduler().runTaskLater(this, new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "bar msg:\"���� �̴ϰ��ӱ��� ��Ÿ�� (10��) - "+str+"\" time:30 p:@a color:yellow");
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
            			str = "�޸���";
            			break;
            		case 1:
            			str = "������ϱ�";
            			break;
            		case 2:
            			str = "���";
            			break;
            		case 3:
            			str = "��ġ";
            			break;
            		case 4:
            			str = "��ź���ϱ�";
            			break;
            		case 5:
            			str = "�����Ա�";
            			break;
            		case 6:
            			str = "����ã��";
            			break;
            		case 7:
            			str = "īƮŸ��";
            			break;
            		case 8:
            			str = "��ȣ����";
            			break;
            		}
            		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "bar msg:\"���� �̴ϰ��ӱ��� ��Ÿ�� (10��) - "+str+"\" time:30 p:@a color:yellow");

            	}
            }
        }, 2000L, 16000L);
		
	}
	@Override
	public void onDisable() {
		Bukkit.getConsoleSender()
		.sendMessage(ChatColor.GOLD + ("hcBrawl") + ChatColor.DARK_AQUA + " �÷����� ��Ȱ��ȭ" + ChatColor.BLACK);
	}
	
	@EventHandler
	private void onEnd(LinmaluMiniGamesEndEvent e) {
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mm i give "+e.getWinner().getName()+" minigamecoin");
		Random rand = new Random();
		int i = rand.nextInt(9);
		switch(i) {
		case 0:
			str = "�޸���";
			break;
		case 1:
			str = "������ϱ�";
			break;
		case 2:
			str = "���";
			break;
		case 3:
			str = "��ġ";
			break;
		case 4:
			str = "��ź���ϱ�";
			break;
		case 5:
			str = "�����Ա�";
			break;
		case 6:
			str = "����ã��";
			break;
		case 7:
			str = "īƮŸ��";
			break;
		case 8:
			str = "��ȣ����";
			break;
		}
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "bar msg:\"���� �̴ϰ��ӱ��� ��Ÿ�� (10��) - "+str+"\" time:600 p:@a color:yellow");
	}
	
	@EventHandler
	public void onBarTerminate(BarTerminateEvent evt) {
		UBossBar b = evt.getBar(); // Gets the dead instance of UBossBar
		if(b.getMessage().contains("���� �̴ϰ��ӱ���")) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "�̴ϰ��� "+str);

			for(Player p : Bukkit.getServer().getOnlinePlayers()) {
				if(ess.getUser(p).isAfk()) {
					p.chat("/linmaluminigames ���");
				}
			}
			
			this.lastTerminate=System.currentTimeMillis();
		}		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			if(cmd.getName().equals("����") || cmd.getName().equals("��������")|| cmd.getName().equals("pvp����")) {
				
				boolean chk=true;
				int cnt=0;
				Inventory inven = ((Player) sender).getInventory();
				for(ItemStack i : inven.getContents()) {
					if(i!=null) {
						cnt++;
						if(i.getType().equals(Material.ELYTRA)) {
							chk=false;
							sender.sendMessage(ChatColor.RED+"�ѳ����� ��� ���� �� �����ϴ�!");
							break;
						}
					}
					if(cnt>7) {
						chk=false;
						sender.sendMessage(ChatColor.RED+"�������� ���� ���� 7�� �̻� ��� �� �����ϴ�!");
						break;
					}
					
				}
				if(chk==true) {
					if(fighters.contains(((Player)sender))){
						sender.sendMessage("�̹� ������ �������Դϴ�");
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
					fighters.get(i).sendMessage(ChatColor.AQUA+":: PVP "+ChatColor.GOLD+"����"+ChatColor.AQUA+" :: "+ChatColor.GREEN+""+ChatColor.BOLD+e.getEntity().getKiller().getName()+ChatColor.WHITE+"���� "+ChatColor.RED+""+ChatColor.BOLD+e.getEntity().getName()+ChatColor.WHITE+"���� ���̼̽��ϴ�!");
				}
			}
			fighters.remove(fighters.indexOf(e.getEntity()));
		}
	}
}




























