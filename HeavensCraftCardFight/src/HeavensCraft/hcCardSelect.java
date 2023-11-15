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
		Inventory inv = Bukkit.createInventory(null, 9,"�ɷ���ī�� ����");		
		
		itemset("�뱤���� ����",403,0,1,Arrays.asList(ChatColor.GOLD+"ȭ�ο� ������ ������",ChatColor.GOLD+"��� ���õǴ� �ɷ�"),0,inv);
		itemset("�����ξ����� ��",403,0,1,Arrays.asList(ChatColor.GOLD+"�������� �߰����ð�",ChatColor.GOLD+"����Ǵ� �ɷ�"),1,inv);
		itemset("��ī�̽�ũ����",403,0,1,Arrays.asList(ChatColor.GOLD+"/top ��ɾ ",ChatColor.GOLD+"����� �� �ִ� �ɷ�",ChatColor.WHITE+"�ز����� �ö󰡴� �ɷ�"),2,inv);
		itemset("�ǽ� �ν���",403,0,1,Arrays.asList(ChatColor.GOLD+"�������� ����� 5���� ������ ",ChatColor.GOLD+"�̵��ӵ��� �������ϴ�."),3,inv);
		itemset("���� �׺������",403,0,1,Arrays.asList(ChatColor.GOLD+"/locate ��ɾ",ChatColor.GOLD+"����� �� �ִ� �ɷ�"),4,inv);
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
		if(cmd.equals("�ɷ���ī�� ����")) {
			e.setCancelled(true);
			switch(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName())) {
			case "�뱤���� ����":
				Bukkit.getServer().dispatchCommand(hcCardMain.getPlugin.getServer().getConsoleSender(), "mm i give "+e.getWhoClicked().getName()+" furnaceBook");
				players.add(p.getUniqueId());
				p.closeInventory();
				break;
			case "�����ξ����� ��":
				Bukkit.getServer().dispatchCommand(hcCardMain.getPlugin.getServer().getConsoleSender(), "mm i give "+e.getWhoClicked().getName()+" NightVisionBook");
				players.add(p.getUniqueId());
				p.closeInventory();
				break;
			case "��ī�̽�ũ����":
				Bukkit.getServer().dispatchCommand(hcCardMain.getPlugin.getServer().getConsoleSender(), "mm i give "+e.getWhoClicked().getName()+" SkyScraperBook");
				players.add(p.getUniqueId());
				p.closeInventory();
				break;
			case "�ǽ� �ν���":
				Bukkit.getServer().dispatchCommand(hcCardMain.getPlugin.getServer().getConsoleSender(), "mm i give "+e.getWhoClicked().getName()+" BoosterBook");
				players.add(p.getUniqueId());
				p.closeInventory();
				break;
			case "���� �׺������":
				Bukkit.getServer().dispatchCommand(hcCardMain.getPlugin.getServer().getConsoleSender(), "mm i give "+e.getWhoClicked().getName()+" NavigatorBook");
				players.add(p.getUniqueId());
				p.closeInventory();
				break;
			}
		}
	}
}
