package HeavensCraft;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import net.md_5.bungee.api.ChatColor;

public class hcWarp implements Listener{
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
		Inventory inv = Bukkit.createInventory(null, 27,"GUI ����");
		itemset("��������",2,0,1,Arrays.asList(""),0,inv);
		itemset("�״�����",87,0,1,Arrays.asList(""),2,inv);
		itemset("��������",121,0,1,Arrays.asList(""),4,inv);
		
		itemset("���̸���",349,1,1,Arrays.asList(""),18,inv);
		itemset("����",251,0,1,Arrays.asList(""),19,inv);
		itemset("��õ",275,0,1,Arrays.asList(""),20,inv);
		itemset("���Ƹ���",388,0,1,Arrays.asList(""),21,inv);
		itemset("�λ�",346,0,1,Arrays.asList(""),22,inv);
		itemset("Paris",297,0,1,Arrays.asList(""),23,inv);
		///give YangJJune 2��91 1 1 {Unbreakable:1, display:{Name:"AK-47", Lore:[aa]}}
		//give @p minecraft:stone_hoe 1 10 {Unbreakable:1}","predicate": {"damaged": 0, "damage": 0.07575757575757576}, "model": "item/nubx/weapons/hunting"},
		//give @p minecraft:stone_hoe 1 8 {Unbreakable:1}", "predicate": {"damaged": 0, "damage": 0.06060606060606061}, "model": "item/nubx/weapons/python
		p.openInventory(inv);
	}
}
