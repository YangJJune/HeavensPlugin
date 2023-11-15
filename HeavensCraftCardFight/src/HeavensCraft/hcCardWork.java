package HeavensCraft;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;

public class hcCardWork {
	public static void onXRay(Player p) {
		Location l = p.getLocation();
		int[] cnt = {0,0,0,0,0,0,0};
		int x = l.getBlockX();
		int y= l.getBlockY();
		int z = l.getBlockZ();

		for (int i = l.getBlockX() - 3; i <= l.getBlockX() + 3; i++) {
			for (int j = l.getBlockY() - 3; j <= l.getBlockY() + 3; j++) {
				for (int k = l.getBlockZ() - 3; k <= l.getBlockZ() + 3; k++) {
					Location now = new Location(p.getWorld(),i,j,k);
					Block b = now.getBlock();
					if(b != null) {
						switch(b.getTypeId()) {
						case 56:  //¥Ÿ¿Ãæ∆
							cnt[1]++;
							break;
						case 129:  //ø°∏ﬁ∂ˆµÂ
							cnt[2]++;
							break;
						case 14:  //±›±§ºÆ
							cnt[3]++;
							break;
						case 73:
							cnt[4]++;
							break;
						case 15://√∂
							cnt[5]++;
							break;
						case 16://ºÆ≈∫
							cnt[6]++;
							break;
						}
					}
				}
			}
		}
		/*for(int x=l.getBlockX()-2; x<=l.getBlockX()+2; x++) {
			for(int y=l.getBlockY()-2; x<=l.getBlockY()+2; y++) {
				for(int z=l.getBlockZ()-2; z<=l.getBlockZ()+2; z++) {
					//Location now = new Location(p.getWorld(),(double)x,(double)y,(double)z);
					//Block b = now.getBlock();
					//if(b != null) {
						switch(b.getTypeId()) {
						case 21:  //√ª±›ºÆ
							cnt[0]++;
							break;
						case 56:  //¥Ÿ¿Ãæ∆
							cnt[1]++;
							break;
						case 129:  //ø°∏ﬁ∂ˆµÂ
							cnt[2]++;
							break;
						case 14:  //±›±§ºÆ
							cnt[3]++;
							break;							
						}
					}
				}	
			}	
		}*/
		p.sendMessage("----[±§π∞ ≈Ω¡ˆ ¡§∫∏]----");
		p.sendMessage(ChatColor.GRAY+"ºÆ≈∫ : "+ChatColor.WHITE+cnt[6]+"∞≥");
		p.sendMessage(ChatColor.GREEN+"√∂±§ºÆ : "+ChatColor.WHITE+cnt[5]+"∞≥");
		p.sendMessage(ChatColor.GOLD+"±›±§ºÆ : "+ChatColor.WHITE+cnt[3]+"∞≥");
		p.sendMessage(ChatColor.RED+"∑πµÂΩ∫≈Ê : "+ChatColor.WHITE+cnt[4]+"∞≥");
		p.sendMessage(ChatColor.GREEN+"ø°∏ﬁ∂ˆµÂ : "+ChatColor.WHITE+cnt[2]+"∞≥");
		p.sendMessage(ChatColor.AQUA+"¥Ÿ¿Ãæ∆ : "+ChatColor.WHITE+cnt[1]+"∞≥");
	}
	
	public static void onBroadcast(String DName, String str) {
		hcCardMain.getPlugin.getServer().broadcastMessage("");
		hcCardMain.getPlugin.getServer().broadcastMessage("<"+DName+"> "+ChatColor.GREEN+ChatColor.BOLD+str);
		hcCardMain.getPlugin.getServer().broadcastMessage("");
	}
	public static void onXRayPlus(Player p) {
		Location l = p.getLocation();
		int[] cnt = {0,0,0,0,0,0,0};
		int x = l.getBlockX();
		int y= l.getBlockY();
		int z = l.getBlockZ();

		for (int i = l.getBlockX() - 3; i <= l.getBlockX() + 3; i++) {
			for (int j = l.getBlockY() - 3; j <= l.getBlockY() + 3; j++) {
				for (int k = l.getBlockZ() - 3; k <= l.getBlockZ() + 3; k++) {
					Location now = new Location(p.getWorld(),i,j,k);
					Block b = now.getBlock();
					if(b != null ) {
						switch(b.getTypeId()) {
						case 56:  //¥Ÿ¿Ãæ∆
							cnt[1]++;
							b.setType(Material.STONE);
							break;
						case 129:  //ø°∏ﬁ∂ˆµÂ
							cnt[2]++;
							b.setType(Material.STONE);
							break;
						case 14:  //±›±§ºÆ
							cnt[3]++;
							b.setType(Material.STONE);
							break;
						case 73: //∑πµÂΩ∫≈Ê
							cnt[4]++;
							b.setType(Material.STONE);
							break;
						case 15://√∂
							cnt[5]++;
							b.setType(Material.STONE);
							break;
						case 16://ºÆ≈∫
							cnt[6]++;
							b.setType(Material.STONE);
							break;
						}
					}
				}
			}
		}
		
		int amount = (int) Math.round(cnt[1]*1.3);
		p.getInventory().addItem(new ItemStack(Material.DIAMOND,amount));
		
		amount = (int) Math.round(cnt[3]*1.3);
		p.getInventory().addItem(new ItemStack(Material.GOLD_ORE,amount));
		
		amount = (int) Math.round(cnt[2]*1.3);
		p.getInventory().addItem(new ItemStack(Material.EMERALD,amount));

		amount = (int) Math.round(cnt[4]*3);
		p.getInventory().addItem(new ItemStack(Material.REDSTONE,amount));
		
		amount = (int) Math.round(cnt[5]*1.3);
		p.getInventory().addItem(new ItemStack(Material.IRON_ORE,amount));
		
		amount = (int) Math.round(cnt[6]*1.3);
		p.getInventory().addItem(new ItemStack(Material.COAL,amount));
		
	}
	public static void onEnchantRemove(Player p) {
		if (p.getInventory().getItemInMainHand() != null) {
			if (p.getLevel() >= 30) {
				ItemStack is = p.getInventory().getItemInMainHand();
				Map<Enchantment, Integer> map = is.getEnchantments();

				for (Enchantment E : map.keySet()) {
					is.removeEnchantment(E);
				}
				p.setLevel(p.getLevel()-30);
			}
			else {
				p.sendMessage("∑π∫ß¿Ã 30∏∏≈≠ « ø‰«’¥œ¥Ÿ!");
			}

		}
		else {
			p.sendMessage("º’ø° ¿Œ√¶∆Æ∏¶ ¡¶∞≈«“ æ∆¿Ã≈€¿ª µÈæÓ¡÷ººø‰!");
		}
	}
}
