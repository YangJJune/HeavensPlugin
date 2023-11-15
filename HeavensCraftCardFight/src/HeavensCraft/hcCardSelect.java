package HeavensCraft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.items.*;
public class hcCardSelect implements Listener{
	public static List<UUID> players = new ArrayList<>();
	
	private hcCardSelect instance;
	public void itemset(String display, int ID, int data, int stack, List<String> Lore, int loc, Inventory inv) {
		ItemStack item = new MaterialData(ID,(byte)(data)).toItemStack(stack);
		ItemMeta items = item.getItemMeta();
		items.setDisplayName(display);
		items.setUnbreakable(true);
		items.setLore(Lore);
		//item.setDurability((short) (item.getDurability()-d));
		item.setItemMeta(items);		
		inv.setItem(loc, item);
	}
	
	public void open(Player p) {		
		Inventory inv = Bukkit.createInventory(null, 9,"능력자카드 선택");		
		
		itemset("용광로의 장인",403,0,1,Arrays.asList(ChatColor.GOLD+"화로에 광석을 넣으면",ChatColor.GOLD+"즉시 제련되는 능력"),0,inv);
		itemset("수리부엉이의 눈",403,0,1,Arrays.asList(ChatColor.GOLD+"영구적인 야간투시가",ChatColor.GOLD+"적용되는 능력"),1,inv);
		itemset("스카이스크래퍼",403,0,1,Arrays.asList(ChatColor.GOLD+"/top 명령어를 ",ChatColor.GOLD+"사용할 수 있는 능력",ChatColor.WHITE+"※꼭대기로 올라가는 능력"),2,inv);
		itemset("피스 부스터",403,0,1,Arrays.asList(ChatColor.GOLD+"전투에서 벗어난지 5초이 지나면 ",ChatColor.GOLD+"이동속도가 빨라집니다."),3,inv);
		itemset("월드 네비게이터",403,0,1,Arrays.asList(ChatColor.GOLD+"/locate 명령어를",ChatColor.GOLD+"사용할 수 있는 능력"),4,inv);
		p.openInventory(inv);
	}
	
	@EventHandler
	private void onInvClick(InventoryClickEvent e) {
		if(e.getInventory().getName()==null && !(e.getWhoClicked().isOp())) {
			return;
		}
		if (e.getCurrentItem() == null || e.getCurrentItem().getTypeId() == 0
				|| !e.getCurrentItem().hasItemMeta()) {
			e.setCancelled(false);
		}
		Player p = (Player) e.getWhoClicked();
		String cmd = ChatColor.stripColor(e.getInventory().getName());
		if(cmd.equals("능력자카드 선택")) {
			e.setCancelled(true);
			switch(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName())) {
			case "용광로의 장인":
				Bukkit.getServer().dispatchCommand(hcCardMain.getPlugin.getServer().getConsoleSender(), "mm i give "+e.getWhoClicked().getName()+" furnaceBook");
				players.add(p.getUniqueId());
				p.closeInventory();
				break;
			case "수리부엉이의 눈":
				Bukkit.getServer().dispatchCommand(hcCardMain.getPlugin.getServer().getConsoleSender(), "mm i give "+e.getWhoClicked().getName()+" NightVisionBook");
				players.add(p.getUniqueId());
				p.closeInventory();
				break;
			case "스카이스크래퍼":
				Bukkit.getServer().dispatchCommand(hcCardMain.getPlugin.getServer().getConsoleSender(), "mm i give "+e.getWhoClicked().getName()+" SkyScraperBook");
				players.add(p.getUniqueId());
				p.closeInventory();
				break;
			case "피스 부스터":
				Bukkit.getServer().dispatchCommand(hcCardMain.getPlugin.getServer().getConsoleSender(), "mm i give "+e.getWhoClicked().getName()+" BoosterBook");
				players.add(p.getUniqueId());
				p.closeInventory();
				break;
			case "월드 네비게이터":
				Bukkit.getServer().dispatchCommand(hcCardMain.getPlugin.getServer().getConsoleSender(), "mm i give "+e.getWhoClicked().getName()+" NavigatorBook");
				players.add(p.getUniqueId());
				p.closeInventory();
				break;
			}
		}
	}
}
