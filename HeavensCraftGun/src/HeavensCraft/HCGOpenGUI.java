package HeavensCraft;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import net.md_5.bungee.api.ChatColor;

public class HCGOpenGUI {
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
		Inventory inv = Bukkit.createInventory(null, 9,"ÃÑ±â»óÁ¡");
		itemset("AK-47",291,1,1,Arrays.asList(ChatColor.GOLD+"$500000"),0,inv);
		itemset("ÇåÅÍ ½º³ªÀÌÆÛ",291,10,1,Arrays.asList(ChatColor.GOLD+"$800000"),1,inv);
		itemset("Python",291,8,1,Arrays.asList(ChatColor.GOLD+"$100000"),2,inv);
		itemset("´õºí ¹è·²",291,7,1,Arrays.asList(ChatColor.GOLD+"$1000000"),3,inv);
		itemset("Desert Eagle",291,5,1,Arrays.asList(ChatColor.GOLD+"$300000"),4,inv);
		itemset("ÅºÃ¢",383,1,1,Arrays.asList(ChatColor.GOLD+"$5000"),5,inv);
		itemset("¼¦°Ç ÅºÃ¢",289,0,1,Arrays.asList(ChatColor.GOLD+"$500"),6,inv);
		itemset("¼¶±¤Åº",272,5,1,Arrays.asList(ChatColor.GOLD+"$7000"),7,inv);
		///give YangJJune 291 1 1 {Unbreakable:1, display:{Name:"AK-47", Lore:[aa]}}
		//give @p minecraft:stone_hoe 1 10 {Unbreakable:1}","predicate": {"damaged": 0, "damage": 0.07575757575757576}, "model": "item/nubx/weapons/hunting"},
		//give @p minecraft:stone_hoe 1 8 {Unbreakable:1}", "predicate": {"damaged": 0, "damage": 0.06060606060606061}, "model": "item/nubx/weapons/python
		p.openInventory(inv);
	}
}

