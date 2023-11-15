package HeavensCraft;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public class hcSnowEvent extends JavaPlugin implements Listener {
	
	
	@Override
	public void onEnable() {
		Bukkit.getConsoleSender()
				.sendMessage(ChatColor.GOLD + ("HCSNOWEVENT") + ChatColor.AQUA + " 플러그인 활성화" + ChatColor.BLACK);
		Bukkit.getPluginManager().registerEvents(this, this);
	}
	
	
	
	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		Bukkit.getConsoleSender()
				.sendMessage(ChatColor.GOLD + ("HCSNOWEVENT") + ChatColor.DARK_AQUA + " 플러그인 비활성화" + ChatColor.BLACK);
	}

	/*@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (cmd.getName().equals("발렌타인상점")) {
				this.open((Player) sender);
			}
			else if (cmd.getName().equals("발렌타인교환")) {
				this.open2((Player) sender);
			}
		}
		return false;
	}*/

	@EventHandler
	public void onCraft(CraftItemEvent e) {
		ItemStack[] i = e.getInventory().getMatrix();
		for (ItemStack is : i) {
			if (is != null) {
				if (is.getTypeId() == 399) {
					if (is.getItemMeta().isUnbreakable() == true) {
						e.setCancelled(true);
					}
				}
			}
		}
	}

	/*@EventHandler
	public void onPlayerBreak(BlockBreakEvent e) {
		if (e.getBlock() != null) {
			Block b = e.getBlock();
			Random rand = new Random();

			if (b.getTypeId() == 1) {
				int i = rand.nextInt(170);
				if (i == 1) {
					e.getPlayer().sendMessage("돌을 캐서 " + ChatColor.LIGHT_PURPLE + "발렌타인 초콜릿" + ChatColor.WHITE + "을 획득하셨습니다!");
					Bukkit.dispatchCommand(getServer().getConsoleSender(),
							"mm i give " + e.getPlayer().getName() + " chocolate");
				}
			}else if (b.getTypeId() == 17) {
				int i = rand.nextInt(170);
				if (i == 1) {
					e.getPlayer()
							.sendMessage("나무를 캐서 " + ChatColor.LIGHT_PURPLE + "발렌타인 초콜릿" + ChatColor.WHITE + "을 획득하셨습니다!");
					Bukkit.dispatchCommand(getServer().getConsoleSender(),
							"mm i give " + e.getPlayer().getName() + " chocolate");
				}
			}
			
		}
	}*/

	public void itemset(String display, int ID, int data, int stack, List<String> Lore, int loc, Inventory inv) {
		ItemStack item = new MaterialData(ID, (byte) (data)).toItemStack(stack);
		ItemMeta items = item.getItemMeta();
		items.setDisplayName(display);
		items.setUnbreakable(true);
		items.setLore(Lore);
		if(ID>=298 && ID<=301) {
			LeatherArmorMeta items2 = (LeatherArmorMeta) items;
			items2.setColor(Color.fromRGB(255, 205, 231));
			item.setItemMeta(items2);
		}
		else
			item.setItemMeta(items);
		// item.setDurability((short) (item.getDurability()-d));
		
		
		inv.setItem(loc, item);
	}

	public void open(Player p) {
		Inventory inv = Bukkit.createInventory(null, 27,  ChatColor.LIGHT_PURPLE+""+ChatColor.BOLD+"발렌타인데이 "+ ChatColor.DARK_AQUA +""+ChatColor.BOLD+""+"이벤트 상점");
		itemset(ChatColor.GOLD+"엑스칼리버", 276, 0, 1, Arrays.asList(ChatColor.GOLD + "50"+ChatColor.LIGHT_PURPLE+" 발렌타인데이 초콜릿"), 0, inv);
		itemset(ChatColor.GOLD+"글라디우스", 276, 0, 1, Arrays.asList(ChatColor.GOLD + "50"+ChatColor.LIGHT_PURPLE+" 발렌타인데이 초콜릿"), 1, inv);
		itemset(ChatColor.GOLD+"깃털의 검", 283, 0, 1, Arrays.asList(ChatColor.GOLD + "50"+ChatColor.LIGHT_PURPLE+" 발렌타인데이 초콜릿"), 2, inv);
		itemset(ChatColor.GRAY+"경험치병", 384, 0, 4, Arrays.asList(ChatColor.GOLD + "1"+ChatColor.LIGHT_PURPLE+" 발렌타인데이 초콜릿"), 4, inv);
		itemset(ChatColor.GOLD+"발렌타인 투구", 298, 0, 1, Arrays.asList(ChatColor.GOLD + "60"+ChatColor.LIGHT_PURPLE+" 발렌타인데이 초콜릿"), 9, inv);
		itemset(ChatColor.GOLD+"발렌타인 흉갑", 299, 0, 1, Arrays.asList(ChatColor.GOLD + "60"+ChatColor.LIGHT_PURPLE+" 발렌타인데이 초콜릿"), 10, inv);
		itemset(ChatColor.GOLD+"발렌타인 바지", 300, 0, 1, Arrays.asList(ChatColor.GOLD + "60"+ChatColor.LIGHT_PURPLE+" 발렌타인데이 초콜릿"), 11, inv);
		itemset(ChatColor.GOLD+"발렌타인 신발", 301, 0, 1, Arrays.asList(ChatColor.GOLD + "60"+ChatColor.LIGHT_PURPLE+" 발렌타인데이 초콜릿"), 12, inv);
		itemset(ChatColor.RED+"AK-47", 291, 1, 1, Arrays.asList(ChatColor.GOLD + "30"+ChatColor.LIGHT_PURPLE+" 발렌타인데이 초콜릿"), 18, inv);
		itemset(ChatColor.RED+"헌터 스나이퍼", 291, 1, 1, Arrays.asList(ChatColor.GOLD + "40"+ChatColor.LIGHT_PURPLE+" 발렌타인데이 초콜릿"), 19, inv);
		itemset(ChatColor.RED+"Python",291,8,1,Arrays.asList(ChatColor.GOLD + "5"+ChatColor.LIGHT_PURPLE+" 발렌타인데이 초콜릿"),20,inv);
		itemset(ChatColor.RED+"Desert Eagle",291,5,1,Arrays.asList(ChatColor.GOLD + "15"+ChatColor.LIGHT_PURPLE+" 발렌타인데이 초콜릿"),21,inv);
		

		p.openInventory(inv);
	}
	
	public void open3(Player p) {
		Inventory inv = Bukkit.createInventory(null, 27,  ChatColor.LIGHT_PURPLE+""+ChatColor.BOLD+"발렌타인데이 "+ ChatColor.DARK_AQUA +""+ChatColor.BOLD+""+"이벤트 상점");
		itemset(ChatColor.GOLD+"엑스칼리버", 276, 0, 1, Arrays.asList(ChatColor.GOLD + "50"+ChatColor.LIGHT_PURPLE+" 발렌타인데이 초콜릿"), 0, inv);
		itemset(ChatColor.GOLD+"글라디우스", 276, 0, 1, Arrays.asList(ChatColor.GOLD + "50"+ChatColor.LIGHT_PURPLE+" 발렌타인데이 초콜릿"), 1, inv);
		itemset(ChatColor.GOLD+"깃털의 검", 283, 0, 1, Arrays.asList(ChatColor.GOLD + "50"+ChatColor.LIGHT_PURPLE+" 발렌타인데이 초콜릿"), 2, inv);
		itemset(ChatColor.GRAY+"경험치병", 384, 0, 4, Arrays.asList(ChatColor.GOLD + "1"+ChatColor.LIGHT_PURPLE+" 발렌타인데이 초콜릿"), 4, inv);
		itemset(ChatColor.GOLD+"발렌타인 투구", 298, 0, 1, Arrays.asList(ChatColor.GOLD + "60"+ChatColor.LIGHT_PURPLE+" 발렌타인데이 초콜릿"), 9, inv);
		itemset(ChatColor.GOLD+"발렌타인 흉갑", 299, 0, 1, Arrays.asList(ChatColor.GOLD + "60"+ChatColor.LIGHT_PURPLE+" 발렌타인데이 초콜릿"), 10, inv);
		itemset(ChatColor.GOLD+"발렌타인 바지", 300, 0, 1, Arrays.asList(ChatColor.GOLD + "60"+ChatColor.LIGHT_PURPLE+" 발렌타인데이 초콜릿"), 11, inv);
		itemset(ChatColor.GOLD+"발렌타인 신발", 301, 0, 1, Arrays.asList(ChatColor.GOLD + "60"+ChatColor.LIGHT_PURPLE+" 발렌타인데이 초콜릿"), 12, inv);
		itemset(ChatColor.RED+"AK-47", 291, 1, 1, Arrays.asList(ChatColor.GOLD + "30"+ChatColor.LIGHT_PURPLE+" 발렌타인데이 초콜릿"), 18, inv);
		itemset(ChatColor.RED+"헌터 스나이퍼", 291, 1, 1, Arrays.asList(ChatColor.GOLD + "40"+ChatColor.LIGHT_PURPLE+" 발렌타인데이 초콜릿"), 19, inv);
		itemset(ChatColor.RED+"Python",291,8,1,Arrays.asList(ChatColor.GOLD + "5"+ChatColor.LIGHT_PURPLE+" 발렌타인데이 초콜릿"),20,inv);
		itemset(ChatColor.RED+"Desert Eagle",291,5,1,Arrays.asList(ChatColor.GOLD + "15"+ChatColor.LIGHT_PURPLE+" 발렌타인데이 초콜릿"),21,inv);
		

		p.openInventory(inv);
	}
	public void open2(Player p) {
		Inventory inv = Bukkit.createInventory(null, 27,  ChatColor.LIGHT_PURPLE+""+ChatColor.BOLD+"발렌타인데이 "+ ChatColor.DARK_AQUA +""+ChatColor.BOLD+""+"교환 상점");
		for(int i=0; i<27; i++) {
			itemset(ChatColor.GOLD+"발렌타인 초코로 교환하기", 357, 0, 1, Arrays.asList(ChatColor.GOLD + "20"+ChatColor.LIGHT_PURPLE+" 코코아"), i, inv);
		}
		p.openInventory(inv);
	}
	/*@EventHandler
	public void onInvEvent(InventoryClickEvent e) {
		if (e.getInventory().getName() != null) {
			String cmd = ChatColor.stripColor(e.getInventory().getName());
			
			if (cmd.equals("발렌타인데이 이벤트 상점")) {
				e.setCancelled(true);
			}
			else if(cmd.equals("발렌타인데이 교환 상점")) {
				e.setCancelled(true);
				Player player = (Player) e.getWhoClicked();
				switch (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName())) {
				case "발렌타인 초코로 교환하기":
					Inventory inven;
					inven = player.getInventory();
					for (int j = 0; j < inven.getSize(); j++) {
						ItemStack is = inven.getItem(j);
						if (is != null) {
							if (is.getTypeId() == 351 && is.getData().getData() == 3) {
								System.out.println(is.getAmount());
								if (is.getAmount() >= 20 && inven.firstEmpty() != -1) {
									int amount = is.getAmount() - 20;									
									inven.removeItem(is);
									is.setAmount(amount);
									inven.addItem(is);
									String command = "mm i give ";
									command += player.getName();
									command += " chocolate";
									
									Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(), command);
									break;
								}
							}
						}
					}
					break;
				}
			}
			else {
				return;
			}
			if (e.getCurrentItem() == null || e.getCurrentItem().getTypeId() == 0
					|| !e.getCurrentItem().hasItemMeta()) {
				e.setCancelled(false);
			} else {
				Player p = (Player) e.getWhoClicked();

				ItemStack i = new ItemStack(Material.COOKIE);
				ItemMeta im = i.getItemMeta();
				im.setUnbreakable(true);
				i.setItemMeta(im);

				Inventory inven;
				switch (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName())) {
				case "엑스칼리버":
					inven = p.getInventory();
					for (int j = 0; j < inven.getSize(); j++) {
						ItemStack is = inven.getItem(j);
						inven.firstEmpty();
						if (is != null) {
							if (is.getItemMeta().isUnbreakable() && is.getTypeId() == 357) {
								if (is.getAmount() >= 50 && inven.firstEmpty()!=-1) {
									int amount = is.getAmount() - 50;									
									inven.removeItem(is);
									is.setAmount(amount);
									inven.addItem(is);
									String command = "mm i give ";
									command += p.getName();
									command += " Excalibur";
									Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(), command);
									break;
								} 
							}
						}
					}
					break;

				case "글라디우스":

					inven = p.getInventory();
					for (int j = 0; j < inven.getSize(); j++) {
						ItemStack is = inven.getItem(j);
						inven.firstEmpty();
						if (is != null) {
							if (is.getItemMeta().isUnbreakable() && is.getTypeId() == 357) {
								if (is.getAmount() >= 50 && inven.firstEmpty() != -1) {
									int amount = is.getAmount() - 50;
									inven.removeItem(is);
									is.setAmount(amount);
									inven.addItem(is);
									String command = "mm i give ";
									command += p.getName();
									command += " Gladius";
									Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(), command);
									break;
								}
							}
						}
					}
					break;

				case "깃털의 검":
					inven = p.getInventory();
					for (int j = 0; j < inven.getSize(); j++) {
						ItemStack is = inven.getItem(j);
						inven.firstEmpty();
						if (is != null) {
							if (is.getItemMeta().isUnbreakable() && is.getTypeId() == 357) {
								if (is.getAmount() >= 50 && inven.firstEmpty() != -1) {
									int amount = is.getAmount() - 50;
									inven.removeItem(is);
									is.setAmount(amount);
									inven.addItem(is);
									String command = "mm i give ";
									command += p.getName();
									command += " feathersword";
									Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(), command);
									break;
								}
							}
						}
					}
					break;
				case "AK-47":
					inven = p.getInventory();
					for (int j = 0; j < inven.getSize(); j++) {
						ItemStack is = inven.getItem(j);
						inven.firstEmpty();
						if (is != null) {
							if (is.getItemMeta().isUnbreakable() && is.getTypeId() == 357) {
								if (is.getAmount() >= 30 && inven.firstEmpty() != -1) {
									int amount = is.getAmount() - 30;
									inven.removeItem(is);
									is.setAmount(amount);
									inven.addItem(is);
									String command = "shot give ";
									command += p.getName();
									command += " AK-47_CSP";
									Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(), command);
									break;
								} 
							}
						}
					}
					break;
				case "헌터 스나이퍼":
					inven = p.getInventory();
					for (int j = 0; j < inven.getSize(); j++) {
						ItemStack is = inven.getItem(j);
						inven.firstEmpty();
						if (is != null) {
							if (is.getItemMeta().isUnbreakable() && is.getTypeId() == 357) {
								if (is.getAmount() >= 40 && inven.firstEmpty() != -1) {
									int amount = is.getAmount() - 40;
									inven.removeItem(is);
									is.setAmount(amount);
									inven.addItem(is);
									String command = "shot give ";
									command += p.getName();
									command += " Hunting_CSP";
									Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(), command);
									break;
								}
							}
						}
					}
					break;
				case "발렌타인 투구":
					inven = p.getInventory();
					for (int j = 0; j < inven.getSize(); j++) {
						ItemStack is = inven.getItem(j);
						inven.firstEmpty();
						if (is != null) {
							if (is.getItemMeta().isUnbreakable() && is.getTypeId() == 357) {
								if (is.getAmount() >= 60 && inven.firstEmpty() != -1) {
									int amount = is.getAmount() - 60;
									inven.removeItem(is);
									is.setAmount(amount);
									inven.addItem(is);
									String command = "mm i give ";
									command += p.getName();
									command += " 발렌타인머리";
									Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(), command);
									break;
								}
							}
						}
					}
					break;
				case "발렌타인 흉갑":
					inven = p.getInventory();
					for (int j = 0; j < inven.getSize(); j++) {
						ItemStack is = inven.getItem(j);
						inven.firstEmpty();
						if (is != null) {
							if (is.getItemMeta().isUnbreakable() && is.getTypeId() == 357) {
								if (is.getAmount() >= 60 && inven.firstEmpty() != -1) {
									int amount = is.getAmount() - 60;
									inven.removeItem(is);
									is.setAmount(amount);
									inven.addItem(is);
									String command = "mm i give ";
									command += p.getName();
									command += " 발렌타인가슴";
									Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(), command);
									break;
								}
							}
						}
					}
					break;
				case "발렌타인 바지":
					inven = p.getInventory();
					for (int j = 0; j < inven.getSize(); j++) {
						ItemStack is = inven.getItem(j);
						inven.firstEmpty();
						if (is != null) {
							if (is.getItemMeta().isUnbreakable() && is.getTypeId() == 357) {
								if (is.getAmount() >= 60 && inven.firstEmpty() != -1) {
									int amount = is.getAmount() - 60;
									inven.removeItem(is);
									is.setAmount(amount);
									inven.addItem(is);
									String command = "mm i give ";
									command += p.getName();
									command += " 발렌타인다리";
									Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(), command);
									break;
								}
							}
						}
					}
					break;
				case "발렌타인 신발":
					inven = p.getInventory();
					for (int j = 0; j < inven.getSize(); j++) {
						ItemStack is = inven.getItem(j);
						inven.firstEmpty();
						if (is != null) {
							if (is.getItemMeta().isUnbreakable() && is.getTypeId() == 357) {
								if (is.getAmount() >= 60 && inven.firstEmpty() != -1) {
									int amount = is.getAmount() - 60;
									inven.removeItem(is);
									is.setAmount(amount);
									inven.addItem(is);
									String command = "mm i give ";
									command += p.getName();
									command += " 발렌타인신발";
									Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(), command);
									break;
								}
							}
						}
					}
					break;
				case "Python":
					inven = p.getInventory();
					for (int j = 0; j < inven.getSize(); j++) {
						ItemStack is = inven.getItem(j);
						inven.firstEmpty();
						if (is != null) {
							if (is.getItemMeta().isUnbreakable() && is.getTypeId() == 357) {
								if (is.getAmount() >= 5 && inven.firstEmpty() != -1) {
									int amount = is.getAmount() - 5;
									inven.removeItem(is);
									is.setAmount(amount);
									inven.addItem(is);
									String command = "shot give ";
									command += p.getName();
									command += " Python_CSP";
									Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(), command);
									break;
								}
							}
						}
					}
					break;
				case "Desert Eagle":
					inven = p.getInventory();
					for (int j = 0; j < inven.getSize(); j++) {
						ItemStack is = inven.getItem(j);
						inven.firstEmpty();
						if (is != null) {
							if (is.getItemMeta().isUnbreakable() && is.getTypeId() == 357) {
								if (is.getAmount() >= 15 && inven.firstEmpty() != -1) {
									int amount = is.getAmount() - 15;
									inven.removeItem(is);
									is.setAmount(amount);
									inven.addItem(is);
									String command = "shot give ";
									command += p.getName();
									command += " Desert_Eagle_CSP";
									Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(), command);
									break;
								}
							}
						}
					}
					break;
				case "경험치병":
					inven = p.getInventory();
					for (int j = 0; j < inven.getSize(); j++) {
						ItemStack is = inven.getItem(j);
						if (is != null) {
							if (is.getItemMeta().isUnbreakable() && is.getTypeId() == 357) {
								if (is.getAmount() >= 1 && inven.firstEmpty()!=-1) {
									int amount = is.getAmount() - 1;									
									inven.removeItem(is);
									is.setAmount(amount);
									inven.addItem(is);
									String command = "give ";
									command += p.getName();
									command += " 384 4";
									Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(), command);									break;
								}
									//p.sendMessage(ChatColor.LIGHT_PURPLE + "발렌타인 초콜릿" + ChatColor.WHITE + "이 부족합니다!");
								
							}
						}
					}
					break;
				}
			}
		}
	}*/
	/*@EventHandler
	public void onCaught(PlayerFishEvent e) {
		if(e.getState() == State.CAUGHT_FISH) {
			Random rand = new Random();
			int i = rand.nextInt(25);
			if (i == 5) {

				String str = e.getPlayer().getName();
				int k = rand.nextInt(10);
				if (k >= 9) {
					Bukkit.dispatchCommand(getServer().getConsoleSender(),
							"mm i give " + e.getPlayer().getName() + " chocolate 7");
					e.getPlayer().sendMessage(ChatColor.AQUA+"대박! 물고기가 발렌타인 초콜릿을 7개나!!");
				} else if (k >= 5) {
					Bukkit.dispatchCommand(getServer().getConsoleSender(),
							"mm i give " + e.getPlayer().getName() + " chocolate 3");
					e.getPlayer().sendMessage(ChatColor.GOLD+"ㅗㅜㅑ 물고기 안에 발렌타인 초콜릿 3개가 있네");
				} else {
					Bukkit.dispatchCommand(getServer().getConsoleSender(),
							"mm i give " + e.getPlayer().getName() + " chocolate 1");
					e.getPlayer().sendMessage(ChatColor.YELLOW+"물고기가 물고 있던 발렌타인 초콜릿을 획득했다");
				}

			}
		}
	}*/	
}
