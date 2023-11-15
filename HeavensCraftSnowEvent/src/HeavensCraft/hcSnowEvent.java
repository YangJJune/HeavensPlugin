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
				.sendMessage(ChatColor.GOLD + ("HCSNOWEVENT") + ChatColor.AQUA + " �÷����� Ȱ��ȭ" + ChatColor.BLACK);
		Bukkit.getPluginManager().registerEvents(this, this);
	}
	
	
	
	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		Bukkit.getConsoleSender()
				.sendMessage(ChatColor.GOLD + ("HCSNOWEVENT") + ChatColor.DARK_AQUA + " �÷����� ��Ȱ��ȭ" + ChatColor.BLACK);
	}

	/*@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (cmd.getName().equals("�߷�Ÿ�λ���")) {
				this.open((Player) sender);
			}
			else if (cmd.getName().equals("�߷�Ÿ�α�ȯ")) {
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
					e.getPlayer().sendMessage("���� ĳ�� " + ChatColor.LIGHT_PURPLE + "�߷�Ÿ�� ���ݸ�" + ChatColor.WHITE + "�� ȹ���ϼ̽��ϴ�!");
					Bukkit.dispatchCommand(getServer().getConsoleSender(),
							"mm i give " + e.getPlayer().getName() + " chocolate");
				}
			}else if (b.getTypeId() == 17) {
				int i = rand.nextInt(170);
				if (i == 1) {
					e.getPlayer()
							.sendMessage("������ ĳ�� " + ChatColor.LIGHT_PURPLE + "�߷�Ÿ�� ���ݸ�" + ChatColor.WHITE + "�� ȹ���ϼ̽��ϴ�!");
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
		Inventory inv = Bukkit.createInventory(null, 27,  ChatColor.LIGHT_PURPLE+""+ChatColor.BOLD+"�߷�Ÿ�ε��� "+ ChatColor.DARK_AQUA +""+ChatColor.BOLD+""+"�̺�Ʈ ����");
		itemset(ChatColor.GOLD+"����Į����", 276, 0, 1, Arrays.asList(ChatColor.GOLD + "50"+ChatColor.LIGHT_PURPLE+" �߷�Ÿ�ε��� ���ݸ�"), 0, inv);
		itemset(ChatColor.GOLD+"�۶��콺", 276, 0, 1, Arrays.asList(ChatColor.GOLD + "50"+ChatColor.LIGHT_PURPLE+" �߷�Ÿ�ε��� ���ݸ�"), 1, inv);
		itemset(ChatColor.GOLD+"������ ��", 283, 0, 1, Arrays.asList(ChatColor.GOLD + "50"+ChatColor.LIGHT_PURPLE+" �߷�Ÿ�ε��� ���ݸ�"), 2, inv);
		itemset(ChatColor.GRAY+"����ġ��", 384, 0, 4, Arrays.asList(ChatColor.GOLD + "1"+ChatColor.LIGHT_PURPLE+" �߷�Ÿ�ε��� ���ݸ�"), 4, inv);
		itemset(ChatColor.GOLD+"�߷�Ÿ�� ����", 298, 0, 1, Arrays.asList(ChatColor.GOLD + "60"+ChatColor.LIGHT_PURPLE+" �߷�Ÿ�ε��� ���ݸ�"), 9, inv);
		itemset(ChatColor.GOLD+"�߷�Ÿ�� �䰩", 299, 0, 1, Arrays.asList(ChatColor.GOLD + "60"+ChatColor.LIGHT_PURPLE+" �߷�Ÿ�ε��� ���ݸ�"), 10, inv);
		itemset(ChatColor.GOLD+"�߷�Ÿ�� ����", 300, 0, 1, Arrays.asList(ChatColor.GOLD + "60"+ChatColor.LIGHT_PURPLE+" �߷�Ÿ�ε��� ���ݸ�"), 11, inv);
		itemset(ChatColor.GOLD+"�߷�Ÿ�� �Ź�", 301, 0, 1, Arrays.asList(ChatColor.GOLD + "60"+ChatColor.LIGHT_PURPLE+" �߷�Ÿ�ε��� ���ݸ�"), 12, inv);
		itemset(ChatColor.RED+"AK-47", 291, 1, 1, Arrays.asList(ChatColor.GOLD + "30"+ChatColor.LIGHT_PURPLE+" �߷�Ÿ�ε��� ���ݸ�"), 18, inv);
		itemset(ChatColor.RED+"���� ��������", 291, 1, 1, Arrays.asList(ChatColor.GOLD + "40"+ChatColor.LIGHT_PURPLE+" �߷�Ÿ�ε��� ���ݸ�"), 19, inv);
		itemset(ChatColor.RED+"Python",291,8,1,Arrays.asList(ChatColor.GOLD + "5"+ChatColor.LIGHT_PURPLE+" �߷�Ÿ�ε��� ���ݸ�"),20,inv);
		itemset(ChatColor.RED+"Desert Eagle",291,5,1,Arrays.asList(ChatColor.GOLD + "15"+ChatColor.LIGHT_PURPLE+" �߷�Ÿ�ε��� ���ݸ�"),21,inv);
		

		p.openInventory(inv);
	}
	
	public void open3(Player p) {
		Inventory inv = Bukkit.createInventory(null, 27,  ChatColor.LIGHT_PURPLE+""+ChatColor.BOLD+"�߷�Ÿ�ε��� "+ ChatColor.DARK_AQUA +""+ChatColor.BOLD+""+"�̺�Ʈ ����");
		itemset(ChatColor.GOLD+"����Į����", 276, 0, 1, Arrays.asList(ChatColor.GOLD + "50"+ChatColor.LIGHT_PURPLE+" �߷�Ÿ�ε��� ���ݸ�"), 0, inv);
		itemset(ChatColor.GOLD+"�۶��콺", 276, 0, 1, Arrays.asList(ChatColor.GOLD + "50"+ChatColor.LIGHT_PURPLE+" �߷�Ÿ�ε��� ���ݸ�"), 1, inv);
		itemset(ChatColor.GOLD+"������ ��", 283, 0, 1, Arrays.asList(ChatColor.GOLD + "50"+ChatColor.LIGHT_PURPLE+" �߷�Ÿ�ε��� ���ݸ�"), 2, inv);
		itemset(ChatColor.GRAY+"����ġ��", 384, 0, 4, Arrays.asList(ChatColor.GOLD + "1"+ChatColor.LIGHT_PURPLE+" �߷�Ÿ�ε��� ���ݸ�"), 4, inv);
		itemset(ChatColor.GOLD+"�߷�Ÿ�� ����", 298, 0, 1, Arrays.asList(ChatColor.GOLD + "60"+ChatColor.LIGHT_PURPLE+" �߷�Ÿ�ε��� ���ݸ�"), 9, inv);
		itemset(ChatColor.GOLD+"�߷�Ÿ�� �䰩", 299, 0, 1, Arrays.asList(ChatColor.GOLD + "60"+ChatColor.LIGHT_PURPLE+" �߷�Ÿ�ε��� ���ݸ�"), 10, inv);
		itemset(ChatColor.GOLD+"�߷�Ÿ�� ����", 300, 0, 1, Arrays.asList(ChatColor.GOLD + "60"+ChatColor.LIGHT_PURPLE+" �߷�Ÿ�ε��� ���ݸ�"), 11, inv);
		itemset(ChatColor.GOLD+"�߷�Ÿ�� �Ź�", 301, 0, 1, Arrays.asList(ChatColor.GOLD + "60"+ChatColor.LIGHT_PURPLE+" �߷�Ÿ�ε��� ���ݸ�"), 12, inv);
		itemset(ChatColor.RED+"AK-47", 291, 1, 1, Arrays.asList(ChatColor.GOLD + "30"+ChatColor.LIGHT_PURPLE+" �߷�Ÿ�ε��� ���ݸ�"), 18, inv);
		itemset(ChatColor.RED+"���� ��������", 291, 1, 1, Arrays.asList(ChatColor.GOLD + "40"+ChatColor.LIGHT_PURPLE+" �߷�Ÿ�ε��� ���ݸ�"), 19, inv);
		itemset(ChatColor.RED+"Python",291,8,1,Arrays.asList(ChatColor.GOLD + "5"+ChatColor.LIGHT_PURPLE+" �߷�Ÿ�ε��� ���ݸ�"),20,inv);
		itemset(ChatColor.RED+"Desert Eagle",291,5,1,Arrays.asList(ChatColor.GOLD + "15"+ChatColor.LIGHT_PURPLE+" �߷�Ÿ�ε��� ���ݸ�"),21,inv);
		

		p.openInventory(inv);
	}
	public void open2(Player p) {
		Inventory inv = Bukkit.createInventory(null, 27,  ChatColor.LIGHT_PURPLE+""+ChatColor.BOLD+"�߷�Ÿ�ε��� "+ ChatColor.DARK_AQUA +""+ChatColor.BOLD+""+"��ȯ ����");
		for(int i=0; i<27; i++) {
			itemset(ChatColor.GOLD+"�߷�Ÿ�� ���ڷ� ��ȯ�ϱ�", 357, 0, 1, Arrays.asList(ChatColor.GOLD + "20"+ChatColor.LIGHT_PURPLE+" ���ھ�"), i, inv);
		}
		p.openInventory(inv);
	}
	/*@EventHandler
	public void onInvEvent(InventoryClickEvent e) {
		if (e.getInventory().getName() != null) {
			String cmd = ChatColor.stripColor(e.getInventory().getName());
			
			if (cmd.equals("�߷�Ÿ�ε��� �̺�Ʈ ����")) {
				e.setCancelled(true);
			}
			else if(cmd.equals("�߷�Ÿ�ε��� ��ȯ ����")) {
				e.setCancelled(true);
				Player player = (Player) e.getWhoClicked();
				switch (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName())) {
				case "�߷�Ÿ�� ���ڷ� ��ȯ�ϱ�":
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
				case "����Į����":
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

				case "�۶��콺":

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

				case "������ ��":
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
				case "���� ��������":
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
				case "�߷�Ÿ�� ����":
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
									command += " �߷�Ÿ�θӸ�";
									Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(), command);
									break;
								}
							}
						}
					}
					break;
				case "�߷�Ÿ�� �䰩":
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
									command += " �߷�Ÿ�ΰ���";
									Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(), command);
									break;
								}
							}
						}
					}
					break;
				case "�߷�Ÿ�� ����":
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
									command += " �߷�Ÿ�δٸ�";
									Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(), command);
									break;
								}
							}
						}
					}
					break;
				case "�߷�Ÿ�� �Ź�":
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
									command += " �߷�Ÿ�νŹ�";
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
				case "����ġ��":
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
									//p.sendMessage(ChatColor.LIGHT_PURPLE + "�߷�Ÿ�� ���ݸ�" + ChatColor.WHITE + "�� �����մϴ�!");
								
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
					e.getPlayer().sendMessage(ChatColor.AQUA+"���! ����Ⱑ �߷�Ÿ�� ���ݸ��� 7����!!");
				} else if (k >= 5) {
					Bukkit.dispatchCommand(getServer().getConsoleSender(),
							"mm i give " + e.getPlayer().getName() + " chocolate 3");
					e.getPlayer().sendMessage(ChatColor.GOLD+"�Ǥ̤� ����� �ȿ� �߷�Ÿ�� ���ݸ� 3���� �ֳ�");
				} else {
					Bukkit.dispatchCommand(getServer().getConsoleSender(),
							"mm i give " + e.getPlayer().getName() + " chocolate 1");
					e.getPlayer().sendMessage(ChatColor.YELLOW+"����Ⱑ ���� �ִ� �߷�Ÿ�� ���ݸ��� ȹ���ߴ�");
				}

			}
		}
	}*/	
}
