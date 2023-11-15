package HeavensCraft;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.logging.Logger;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.items.MythicItem;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class hcCardMain extends JavaPlugin implements Listener {
	
	private static final Logger log = Logger.getLogger("Minecraft");
	private static Economy econ = null;
	private static Permission perms = null;
	private static Chat chat = null;
	
	public static HashMap<Player,Integer> cards = new HashMap<>();
	public static hcCardMain getPlugin;
	public static HashMap<Player,Long> battleTime = new HashMap<>();
	public static HashMap<Player,Long> XRayPlusCool = new HashMap<>();
	public static HashMap<Player,Long> BroadcastCool = new HashMap<>();
	public static HashMap<Player,Long> AfterBurnerCool = new HashMap<>();
	public static HashMap<UUID,Location> tpLocation = new HashMap<>();
	
	public static hcCardSelect h;
	final int XrayPlusSec = 20;
	@Override
	public void onEnable() {
		this.getPlugin=this;
		h = new hcCardSelect();
		Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + ("HCCardEvent") + ChatColor.AQUA + " �÷����� Ȱ��ȭ" + ChatColor.BLACK);
		Bukkit.getServer().getPluginManager().registerEvents(new hcCardWorkListener(), this);
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		Bukkit.getServer().getPluginManager().registerEvents(new hcCardNoAnvilName(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new hcCardSelect(), this);
	
		
		/*for(OfflinePlayer p : Bukkit.getOfflinePlayers()) {
			if(System.currentTimeMillis()-p.getLastPlayed() >= 259200000) {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex user "+p.getName()+ " add essentials.balancetop.exclude");
			}
		}*/
		if (!setupEconomy()) {
			log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		setupPermissions();
		setupChat();		
		this.readFile();
	}
	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + ("HCCardEvent") + ChatColor.DARK_AQUA + " �÷����� ��Ȱ��ȭ" + ChatColor.BLACK);
		this.saveFile();
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// TODO Auto-generated method stub
		if (sender instanceof Player) {
			switch (cmd.getName()) {
			case "�ɷ���ī��":
				if (args.length == 0) {
					if (((Player) sender).getInventory().firstEmpty() != -1) {			
						if (!(hcCardSelect.players.contains(((Player) sender).getUniqueId())))
							h.open((Player) sender);
						else {
							sender.sendMessage("�̹� �����ϼ̽��ϴ�!");
						}
					}
					else {
						sender.sendMessage("�κ��丮 ������ ���� �ٽ� �������ּ���");
					}
				}
				else {
					if(args[0].equals("�̱�")) {
						if ((((Player) sender).getInventory().firstEmpty() != -1)) {
							if (econ.getBalance(sender.getName()) >= 100000) {
								econ.withdrawPlayer(sender.getName(), 100000);
								Random rand = new Random();
								int x = rand.nextInt(100);
								if (x < 15) {
									sender.sendMessage(ChatColor.GREEN + "���ϵ帳�ϴ�! �ɷ��� ī�带 �����̽��ϴ�!");
									int y = rand.nextInt(100); // ���� or �Ϲ�
									if (y < 95) {
										int k = rand.nextInt(8);
										switch (k) {
										case 0:
											Bukkit.getServer().dispatchCommand(
													hcCardMain.getPlugin.getServer().getConsoleSender(),
													"mm i give " + sender.getName() + " furnaceBook");
											break;
										case 1:
											Bukkit.getServer().dispatchCommand(
													hcCardMain.getPlugin.getServer().getConsoleSender(),
													"mm i give " + sender.getName() + " NightVisionBook");
											break;
										case 2:
											Bukkit.getServer().dispatchCommand(
													hcCardMain.getPlugin.getServer().getConsoleSender(),
													"mm i give " + sender.getName() + " SkyScraperBook");
									 		break;
										case 3:
											Bukkit.getServer().dispatchCommand(
													hcCardMain.getPlugin.getServer().getConsoleSender(),
													"mm i give " + sender.getName() + " BoosterBook");
											break;
										case 4:
											Bukkit.getServer().dispatchCommand(
													hcCardMain.getPlugin.getServer().getConsoleSender(),
													"mm i give " + sender.getName() + " NavigatorBook");
											break;
										case 5:
											Bukkit.getServer().dispatchCommand(
													hcCardMain.getPlugin.getServer().getConsoleSender(),
													"mm i give " + sender.getName() + " NoFallBook");
											break;
										case 6:
											Bukkit.getServer().dispatchCommand(
													hcCardMain.getPlugin.getServer().getConsoleSender(),
													"mm i give " + sender.getName() + " XRayBook");
											break;
										case 7:
											Bukkit.getServer().dispatchCommand(
													hcCardMain.getPlugin.getServer().getConsoleSender(),
													"mm i give " + sender.getName() + " BoneMealBook");
											break;
										}
									} else {
										Bukkit.getServer().dispatchCommand(
												hcCardMain.getPlugin.getServer().getConsoleSender(),
												"broadcast " + sender.getName() + "�Բ��� ���� �ɷ���ī�带 �����̽��ϴ�!");
										int k = rand.nextInt(4);
										switch (k) {
										case 0:
											Bukkit.getServer().dispatchCommand(
													hcCardMain.getPlugin.getServer().getConsoleSender(),
													"mm i give " + sender.getName() + " EnchantRemoveBook");
											break;
										case 1:
											Bukkit.getServer().dispatchCommand(
													hcCardMain.getPlugin.getServer().getConsoleSender(),
													"mm i give " + sender.getName() + " XRayBookPlus");
											break;
										case 2:
											Bukkit.getServer().dispatchCommand(
													hcCardMain.getPlugin.getServer().getConsoleSender(),
													"mm i give " + sender.getName() + " RandomSBook");
											break;
										case 3:
											Bukkit.getServer().dispatchCommand(
													hcCardMain.getPlugin.getServer().getConsoleSender(),
													"mm i give " + sender.getName() + " AlphaStrikeBook");
											break;
										}
									}

								} else if (x < 100) {
									sender.sendMessage(ChatColor.GREEN + "���̾Ƹ�� 3���� �����̽��ϴ�!");
									Bukkit.getServer().dispatchCommand(
											hcCardMain.getPlugin.getServer().getConsoleSender(),
											"give " + sender.getName() + " diamond 3");
								} else {
									sender.sendMessage(ChatColor.GREEN + "��!");
									
								}
							} else {
								sender.sendMessage(ChatColor.RED + "�ܾ��� �����մϴ�");
							}
						}
						else {
							sender.sendMessage("�κ��丮 ������ ���� �ٽ� �������ּ���");
						}
					}
					else if (args[0].equals("�����̱�")) {
						for (int i = 0; i <= 10; i++) {
							if ((((Player) sender).getInventory().firstEmpty() != -1)) {
								if (econ.getBalance(sender.getName()) >= 90000) {
									econ.withdrawPlayer(sender.getName(), 90000);
									Random rand = new Random();
									int x = rand.nextInt(100);
									if (x < 20) {
										sender.sendMessage(ChatColor.GREEN + "���ϵ帳�ϴ�! �ɷ��� ī�带 �����̽��ϴ�!");
										int y = rand.nextInt(100); // ���� or �Ϲ�
										if (y < 95) {
											int k = rand.nextInt(9);
											switch (k) {
											case 0:
												Bukkit.getServer().dispatchCommand(
														hcCardMain.getPlugin.getServer().getConsoleSender(),
														"mm i give " + sender.getName() + " furnaceBook");
												break;
											case 1:
												Bukkit.getServer().dispatchCommand(
														hcCardMain.getPlugin.getServer().getConsoleSender(),
														"mm i give " + sender.getName() + " NightVisionBook");
												break;
											case 2:
												Bukkit.getServer().dispatchCommand(
														hcCardMain.getPlugin.getServer().getConsoleSender(),
														"mm i give " + sender.getName() + " SkyScraperBook");
												break;
											case 3:
												Bukkit.getServer().dispatchCommand(
														hcCardMain.getPlugin.getServer().getConsoleSender(),
														"mm i give " + sender.getName() + " BoosterBook");
												break;
											case 4:
												Bukkit.getServer().dispatchCommand(
														hcCardMain.getPlugin.getServer().getConsoleSender(),
														"mm i give " + sender.getName() + " NavigatorBook");
												break;
											case 5:
												Bukkit.getServer().dispatchCommand(
														hcCardMain.getPlugin.getServer().getConsoleSender(),
														"mm i give " + sender.getName() + " NoFallBook");
												break;
											case 6:
												Bukkit.getServer().dispatchCommand(
														hcCardMain.getPlugin.getServer().getConsoleSender(),
														"mm i give " + sender.getName() + " XRayBook");
												break;
											case 7:
												Bukkit.getServer().dispatchCommand(
														hcCardMain.getPlugin.getServer().getConsoleSender(),
														"mm i give " + sender.getName() + " BoneMealBook");
												break;
											case 8:
												Bukkit.getServer().dispatchCommand(
														hcCardMain.getPlugin.getServer().getConsoleSender(),
														"mm i give " + sender.getName() + " CookBook");
												break;
											}
										} else {
											Bukkit.getServer().dispatchCommand(
													hcCardMain.getPlugin.getServer().getConsoleSender(),
													"broadcast " + sender.getName() + "�Բ��� ���� �ɷ���ī�带 �����̽��ϴ�!");
											int k = rand.nextInt(7);
											switch (k) {
											case 0:
												Bukkit.getServer().dispatchCommand(
														hcCardMain.getPlugin.getServer().getConsoleSender(),
														"mm i give " + sender.getName() + " EnchantRemoveBook");
												break;
											case 1:
												Bukkit.getServer().dispatchCommand(
														hcCardMain.getPlugin.getServer().getConsoleSender(),
														"mm i give " + sender.getName() + " XRayBookPlus");
												break;
											case 2:
												Bukkit.getServer().dispatchCommand(
														hcCardMain.getPlugin.getServer().getConsoleSender(),
														"mm i give " + sender.getName() + " RandomSBook");
												break;
												
											case 3:
												Bukkit.getServer().dispatchCommand(
														hcCardMain.getPlugin.getServer().getConsoleSender(),
														"mm i give " + sender.getName() + " AlphaStrikeBook");
											case 4:
												Bukkit.getServer().dispatchCommand(
														hcCardMain.getPlugin.getServer().getConsoleSender(),
														"mm i give " + sender.getName() + " BroadcastBook");
												break;
											case 5:
												Bukkit.getServer().dispatchCommand(
														hcCardMain.getPlugin.getServer().getConsoleSender(),
														"mm i give " + sender.getName() + " AfterBurnerBook");
												break;
											case 6:
												Bukkit.getServer().dispatchCommand(
														hcCardMain.getPlugin.getServer().getConsoleSender(),
														"mm i give " + sender.getName() + " HomeBook");
												break;
											}
										}

									} else if (x < 100) {
										sender.sendMessage(ChatColor.GREEN + "���̾Ƹ�� 3���� �����̽��ϴ�!");
										Bukkit.getServer().dispatchCommand(
												hcCardMain.getPlugin.getServer().getConsoleSender(),
												"give " + sender.getName() + " diamond 3");
									} else {
										sender.sendMessage(ChatColor.GREEN + "��!");
										
									}
								} else {
									sender.sendMessage(ChatColor.RED + "�ܾ��� �����մϴ�");
									break;
								}
							} else {
								sender.sendMessage("�κ��丮 ������ ���� �ٽ� �������ּ���");
								break;
							}
						}
					}
					else if(args[0].equals("üũ") && sender.isOp()) {
						for(Player p : hcCardMain.getPlugin.getServer().getOnlinePlayers()) {
							if(!(hcCardSelect.players.contains(p.getUniqueId()))) {
								sender.sendMessage(p.getName());
							}
						}
					}
					else if(args[0].equals("Ȯ��")) {
						
					}
					else if(args[0].equals("����")) {
					}
					else if(args[0].equals("���")) {
						
						int cnt =0;
						for(MythicItem is : MythicMobs.inst().getItemManager().getItems()) {
							if(is.getInternalName().contains("ook")) {
								
								//invee.setItem(cnt, (ItemStack) is.generateItemStack(1));
								byte[] utf8StringBuffer = null;
								try {
									utf8StringBuffer = is.getDisplayName().getBytes("utf-16");
								} catch (UnsupportedEncodingException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								System.out.println();
								
								String decodedFromUtf8 = null;
								try {
									decodedFromUtf8 = new String(utf8StringBuffer, "utf-16");
								} catch (UnsupportedEncodingException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								System.out.println("String from utf-8 : " + decodedFromUtf8);


								sender.sendMessage(is.getDisplayName());
								System.out.println(is.getDisplayName());
								cnt++;
							}
						
						}
					}
					else if(args[0].equals("����")) {
						showCardHelp(sender);
					}
				}
				break;
			case "����Ž��":
			case "Ž��":
				if (cards.containsKey((Player) sender)) {
					if (cards.get((Player) sender) == 7) {
						hcCardWork.onXRay((Player) sender);
					}
					else if (cards.get((Player) sender) == 10) {
						hcCardWork.onXRay((Player) sender);
					}
					else {
						sender.sendMessage(ChatColor.GOLD+"���� Ž���� "+ChatColor.WHITE+"�ɷ��� �������ּ���!");
					}
				}
				break;
			case "Ȯ����":
				if(cards.containsKey((Player) sender)) {
					if(cards.get((Player) sender)==13) {
						if(args.length>=1) {
							String str="";
							for(String  i: args) {
								str +=i;
								str +=" ";
							}
							if(this.BroadcastCool.containsKey(((Player) sender))) {
								long cooltime = System.currentTimeMillis()-BroadcastCool.get(((Player) sender));
								if(cooltime>=3600000) {
									hcCardWork.onBroadcast(((Player) sender).getDisplayName(),str);
									this.BroadcastCool.put(((Player) sender), System.currentTimeMillis());
								}
								else {
									sender.sendMessage("��Ÿ���� ���� "+String.valueOf((60-cooltime/60000))+"�� ���ҽ��ϴ�");
								}
							}
							else {
								hcCardWork.onBroadcast(((Player) sender).getDisplayName(),str);
								this.BroadcastCool.put(((Player) sender), System.currentTimeMillis());
							}							
						}
						else {
							sender.sendMessage("Ȯ����� ���� ������ �������ּ���");
						}
					}
					else {
						sender.sendMessage("Ȯ���⸦ �̿��Ͻ÷���"+ChatColor.YELLOW+" �ɷ���ī��"+ChatColor.AQUA+" <Ȯ����>"+ChatColor.WHITE+"�� ��������ּ���");
					}
				}
				else {
					sender.sendMessage("Ȯ���⸦ �̿��Ͻ÷���"+ChatColor.YELLOW+" �ɷ���ī��"+ChatColor.AQUA+" <Ȯ����>"+ChatColor.WHITE+"�� ��������ּ���");
				}
				break;
			case "��Ƽ��Ȩ":
			case "msethome":
				if(cards.containsKey((Player) sender)) {
					if(cards.get((Player) sender)==15) {
						this.tpLocation.put(((Player) sender).getUniqueId(), ((Player) sender).getLocation());
						sender.sendMessage(ChatColor.GREEN+"���� �Ϸ�! "+ChatColor.YELLOW+"/��ƼȨ "+ChatColor.WHITE+" �Ǵ� "+ChatColor.YELLOW+"/mhome"+ChatColor.WHITE+" ��ɾ �̿����ּ���");
					}
					else {
						sender.sendMessage(ChatColor.RED+"���� �߻�! �ɷ���ī��"+ChatColor.AQUA+"��Ƽ��Ȩ"+ChatColor.WHITE+"�� ������ ���¿����մϴ�!");
					}
				}
				else {
					sender.sendMessage(ChatColor.RED+"���� �߻�! �ɷ���ī��"+ChatColor.AQUA+"��Ƽ��Ȩ"+ChatColor.WHITE+"�� ������ ���¿����մϴ�!");
				}
				break;
			case "��ƼȨ":
			case "mhome":
				if(cards.containsKey((Player) sender)) {
					if(cards.get((Player) sender)==15) {
						if(this.tpLocation.containsKey(((Player) sender).getUniqueId())) {
							((Player) sender).teleport(this.tpLocation.get(((Player) sender).getUniqueId()));
							sender.sendMessage(ChatColor.GREEN+"�̵� �Ϸ�!");
						}
						else {
							sender.sendMessage(ChatColor.RED+"���� �߻�! "+ChatColor.YELLOW+" /��Ƽ��Ȩ " +ChatColor.WHITE+"�Ǵ�"+ChatColor.YELLOW+" /msethome "+ChatColor.WHITE+"��ɾ�� ���� ���� �������ּ���");
						}
					}
					else {
						sender.sendMessage(ChatColor.RED+"���� �߻�! �ɷ���ī��"+ChatColor.AQUA+"��Ƽ��Ȩ"+ChatColor.WHITE+"�� ������ ���¿����մϴ�!");
					}
				}
				else {
					sender.sendMessage(ChatColor.RED+"���� �߻�! �ɷ���ī��"+ChatColor.AQUA+"��Ƽ��Ȩ"+ChatColor.WHITE+"�� ������ ���¿����մϴ�!");
				}
				break;
			case "�丮":
				if(cards.containsKey((Player) sender)) {
					if(cards.get((Player) sender)==16) {

						int arr[] = {0,0,0,0,0,0,0,0};
						Inventory inven = ((Player) sender).getInventory();
						if (args.length >= 1 && args[0].equals("��ü")) {
							for (ItemStack is : inven.getContents()) {
								if (is != null) {
									switch (is.getTypeId()) {
									case 319: //�������
										arr[0]+=is.getAmount();
										break;
									case 349: 
										//������
										if(is.getData().getData()==0) {
											arr[1]+=is.getAmount();
										}
										//������
										else if(is.getData().getData()==1) {
											arr[2]+=is.getAmount();
										}
										break;
									case 363: //�Ұ��
										arr[3]+=is.getAmount();
										break;
									case 365: //�߰��
										arr[4]+=is.getAmount();
										break;
									case 411: //�䳢���
										arr[5]+=is.getAmount();
										break;
									case 392: //����
										arr[6]+=is.getAmount();
										break;
									case 423: //����
										arr[7]+=is.getAmount();
										break;
									}
								}
							}
							inven.remove(Material.PORK);
							inven.addItem(new ItemStack(Material.GRILLED_PORK, arr[0]));
							inven.remove(Material.RAW_FISH);
							inven.addItem(new ItemStack(Material.COOKED_FISH, arr[1]+arr[2]));							
							inven.remove(Material.RAW_BEEF);
							inven.addItem(new ItemStack(Material.COOKED_BEEF, arr[3]));							
							inven.remove(Material.RAW_CHICKEN);
							inven.addItem(new ItemStack(Material.COOKED_CHICKEN, arr[4]));							
							inven.remove(Material.RABBIT);
							inven.addItem(new ItemStack(Material.COOKED_RABBIT, arr[5]));							
							inven.remove(Material.POTATO);
							inven.addItem(new ItemStack(Material.BAKED_POTATO, arr[6]));							
							inven.remove(Material.MUTTON);
							inven.addItem(new ItemStack(Material.COOKED_MUTTON, arr[7]));
						}
						else {
							int amount;
							ItemStack i;
							switch(((Player) sender).getItemInHand().getTypeId()) {
							case 319:
								i = ((Player) sender).getItemInHand();
								amount = i.getAmount();
								((Player) sender).getItemInHand().setAmount(0);
								inven.addItem(new ItemStack(Material.GRILLED_PORK,amount));
								break;
							case 349:								
								i = ((Player) sender).getItemInHand();
								amount = i.getAmount();
								((Player) sender).getItemInHand().setAmount(0);
								
								if(i.getData().getData()==0)
									inven.addItem(new ItemStack(Material.COOKED_FISH,amount));
								else if(i.getData().getData()==1)
									inven.addItem(new ItemStack(Material.COOKED_FISH,amount));
								
								break;
							case 363:								
								i = ((Player) sender).getItemInHand();
								amount = i.getAmount();
								((Player) sender).getItemInHand().setAmount(0);
								inven.addItem(new ItemStack(Material.COOKED_BEEF,amount));
								break;
							case 365:
								i = ((Player) sender).getItemInHand();
								amount = i.getAmount();
								((Player) sender).getItemInHand().setAmount(0);
								inven.addItem(new ItemStack(Material.COOKED_CHICKEN,amount));
								break;
							case 411:
								i = ((Player) sender).getItemInHand();
								amount = i.getAmount();
								((Player) sender).getItemInHand().setAmount(0);
								inven.addItem(new ItemStack(Material.COOKED_RABBIT,amount));
								break;
							case 392:
								i = ((Player) sender).getItemInHand();
								amount = i.getAmount();
								((Player) sender).getItemInHand().setAmount(0);
								inven.addItem(new ItemStack(Material.BAKED_POTATO,amount));
								break;
							case 423:
								i = ((Player) sender).getItemInHand();
								amount = i.getAmount();
								((Player) sender).getItemInHand().setAmount(0);
								inven.addItem(new ItemStack(Material.COOKED_MUTTON,amount));
								break;
							}
						}
						((Player) sender).updateInventory();
					}
				}
				break;
			case "����":
				if(cards.containsKey((Player) sender)) {
					if(cards.get((Player) sender)==0) {

						int arr[] = {0,0,0,0,0};
						Inventory inven = ((Player) sender).getInventory();
						if (args.length >= 1 && args[0].equals("��ü")) {
							for (ItemStack is : inven.getContents()) {
								if (is != null) {
									switch (is.getTypeId()) {
									case 14: // ��
										arr[0]+=is.getAmount();
										break;
									case 15: // ö
										arr[1]+=is.getAmount();
										break;
									case 16: // ��ź
										arr[2]+=is.getAmount();
										break;
									case 56: // ���̾�
										arr[3]+=is.getAmount();
										break;
									case 129: //���޶���
										arr[4]+=is.getAmount();
										break;
									}
								}
							}
							inven.remove(Material.GOLD_ORE);
							inven.addItem(new ItemStack(Material.GOLD_INGOT, arr[0]));
							
							inven.remove(Material.IRON_ORE);
							inven.addItem(new ItemStack(Material.IRON_INGOT, arr[1]));
							
							inven.remove(Material.COAL_ORE);
							inven.addItem(new ItemStack(Material.COAL, arr[2]));
							
							inven.remove(Material.DIAMOND_ORE);
							inven.addItem(new ItemStack(Material.DIAMOND, arr[3]));
							
							inven.remove(Material.EMERALD_ORE);
							inven.addItem(new ItemStack(Material.EMERALD, arr[4]));
						}
						else {
							int amount;
							ItemStack i;
							switch(((Player) sender).getItemInHand().getTypeId()) {
							case 14:
								i = ((Player) sender).getItemInHand();
								amount = i.getAmount();
								((Player) sender).getItemInHand().setAmount(0);
								inven.addItem(new ItemStack(Material.GOLD_INGOT,amount));
								break;
							case 15:
								
								i = ((Player) sender).getItemInHand();
								amount = i.getAmount();
								((Player) sender).getItemInHand().setAmount(0);
								inven.addItem(new ItemStack(Material.IRON_INGOT,amount));
								break;
							case 16:
								
								i = ((Player) sender).getItemInHand();
								amount = i.getAmount();
								((Player) sender).getItemInHand().setAmount(0);
								inven.addItem(new ItemStack(Material.COAL,amount));
								break;
							case 56:
								
								i = ((Player) sender).getItemInHand();
								amount = i.getAmount();
								((Player) sender).getItemInHand().setAmount(0);
								inven.addItem(new ItemStack(Material.DIAMOND,amount));
								break;
							case 129:
								i = ((Player) sender).getItemInHand();
								amount = i.getAmount();
								((Player) sender).getItemInHand().setAmount(0);
								inven.addItem(new ItemStack(Material.EMERALD,amount));
								break;
							}
						}
						((Player) sender).updateInventory();
					}
				}
				break;
			case "��æƮ����":
				if (cards.containsKey((Player) sender)) {
					if (cards.get((Player) sender) == 9 || sender.isOp()) {
						hcCardWork.onEnchantRemove((Player) sender);
					}
					else {
						sender.sendMessage(ChatColor.GOLD+"���� ������ "+ChatColor.WHITE+"�ɷ��� �������ּ���!");
					}
				}
				break;
			case "ä��":
				if (cards.containsKey((Player) sender)) {
					if (cards.get((Player) sender) == 10) {
						if(this.XRayPlusCool.containsKey(sender)) {
							long remainSec = (System.currentTimeMillis()-hcCardMain.XRayPlusCool.get(sender))/1000;
							if(remainSec >= this.XrayPlusSec) {
								hcCardWork.onXRayPlus((Player) sender);
								XRayPlusCool.put((Player) sender, System.currentTimeMillis());
							}
							else {
								sender.sendMessage("���� ��Ÿ�� : "+String.valueOf(this.XrayPlusSec-remainSec));
							}
						}
						else {
							hcCardWork.onXRayPlus((Player) sender);
							XRayPlusCool.put((Player) sender, System.currentTimeMillis());
						}
					}
					else {
						sender.sendMessage(ChatColor.GOLD+"���� Ž���� ���� "+ChatColor.WHITE+"�ɷ��� �������ּ���!");
					}
				}
				break;
			}
			
		}
		return false;
	}
	private void showCardHelp(CommandSender sender) {
		// TODO Auto-generated method stub
		
	}
	@EventHandler
	public void onSync(PlayerDropItemEvent e) {
		ItemStack i = e.getItemDrop().getItemStack();
		ItemMeta im = i.getItemMeta();
		if(im.isUnbreakable()== true && i.getTypeId()==403) {
			switch(ChatColor.stripColor(im.getDisplayName())) {
			case "�뱤���� ����":
				cards.put(e.getPlayer(), 0);
				e.getPlayer().removePotionEffect(PotionEffectType.NIGHT_VISION);
				e.getPlayer().removePotionEffect(PotionEffectType.SPEED);
				e.getPlayer().sendTitle(ChatColor.GOLD+"�뱤��"+ChatColor.WHITE+"��"+ ChatColor.GREEN+" ����", ChatColor.YELLOW+"�ɷ� ����ȭ �Ϸ�");
				e.setCancelled(true);
				this.getServer().dispatchCommand(this.getServer().getConsoleSender(), "pex user "+e.getPlayer().getName()+" remove essentials.top");
				this.getServer().dispatchCommand(this.getServer().getConsoleSender(), "pex user "+e.getPlayer().getName()+" remove minecraft.command.locate");
				break;
			case "�����ξ����� ��":
				cards.put(e.getPlayer(), 1);
				e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION,Integer.MAX_VALUE, 10));
				e.getPlayer().removePotionEffect(PotionEffectType.SPEED);
				e.getPlayer().sendTitle(ChatColor.YELLOW+"�����ξ���"+ChatColor.WHITE+"��"+ ChatColor.DARK_AQUA+" ��", ChatColor.YELLOW+"�ɷ� ����ȭ �Ϸ�");
				e.setCancelled(true);
				this.getServer().dispatchCommand(this.getServer().getConsoleSender(), "pex user "+e.getPlayer().getName()+" remove essentials.top");
				this.getServer().dispatchCommand(this.getServer().getConsoleSender(), "pex user "+e.getPlayer().getName()+" remove minecraft.command.locate");
				break;
			case "��ī�� ��ũ����":
				cards.put(e.getPlayer(), 2);
				e.getPlayer().removePotionEffect(PotionEffectType.NIGHT_VISION);
				e.getPlayer().removePotionEffect(PotionEffectType.SPEED);
				e.getPlayer().sendTitle(ChatColor.AQUA+"��ī�̽�ũ����", ChatColor.YELLOW+"�ɷ� ����ȭ �Ϸ�");
				e.setCancelled(true);
				this.getServer().dispatchCommand(this.getServer().getConsoleSender(), "pex user "+e.getPlayer().getName()+" add essentials.top");
				this.getServer().dispatchCommand(this.getServer().getConsoleSender(), "pex user "+e.getPlayer().getName()+" remove minecraft.command.locate");
				break;
			case "�ǽ� �ν���":
				cards.put(e.getPlayer(), 3);
				e.getPlayer().removePotionEffect(PotionEffectType.NIGHT_VISION);
				e.setCancelled(true);
				this.battleTime.put(e.getPlayer(), System.currentTimeMillis());
				e.getPlayer().sendTitle(ChatColor.AQUA+"�ǽ� �ν���", ChatColor.YELLOW+"�ɷ� ����ȭ �Ϸ�");
				this.getServer().dispatchCommand(this.getServer().getConsoleSender(), "pex user "+e.getPlayer().getName()+" remove essentials.top");
				this.getServer().dispatchCommand(this.getServer().getConsoleSender(), "pex user "+e.getPlayer().getName()+" remove minecraft.command.locate");
				break;
			case "���� �׺������":
				cards.put(e.getPlayer(), 4);
				e.getPlayer().removePotionEffect(PotionEffectType.NIGHT_VISION);
				e.getPlayer().removePotionEffect(PotionEffectType.SPEED);
				e.setCancelled(true);
				e.getPlayer().sendTitle(ChatColor.AQUA+"���� �׺������", ChatColor.YELLOW+"�ɷ� ����ȭ �Ϸ�");
				this.getServer().dispatchCommand(this.getServer().getConsoleSender(), "pex user "+e.getPlayer().getName()+" remove essentials.top");
				this.getServer().dispatchCommand(this.getServer().getConsoleSender(), "pex user "+e.getPlayer().getName()+" add minecraft.command.locate");
				break;
			case "���� ����":
				cards.put(e.getPlayer(), 5);
				e.getPlayer().removePotionEffect(PotionEffectType.NIGHT_VISION);
				e.getPlayer().removePotionEffect(PotionEffectType.SPEED);
				e.setCancelled(true);
				e.getPlayer().sendTitle(ChatColor.AQUA+"���� ����", ChatColor.YELLOW+"�ɷ� ����ȭ �Ϸ�");
				this.getServer().dispatchCommand(this.getServer().getConsoleSender(), "pex user "+e.getPlayer().getName()+" remove essentials.top");
				this.getServer().dispatchCommand(this.getServer().getConsoleSender(), "pex user "+e.getPlayer().getName()+" remove minecraft.command.locate");
				break;
			case "������ ���":
				cards.put(e.getPlayer(), 6);
				e.getPlayer().removePotionEffect(PotionEffectType.NIGHT_VISION);
				e.getPlayer().removePotionEffect(PotionEffectType.SPEED);
				e.setCancelled(true);
				e.getPlayer().sendTitle(ChatColor.AQUA+"������ ���", ChatColor.YELLOW+"�ɷ� ����ȭ �Ϸ�");
				this.getServer().dispatchCommand(this.getServer().getConsoleSender(), "pex user "+e.getPlayer().getName()+" remove essentials.top");
				this.getServer().dispatchCommand(this.getServer().getConsoleSender(), "pex user "+e.getPlayer().getName()+" remove minecraft.command.locate");
				break;
			case "���� Ž����":
				cards.put(e.getPlayer(), 7);
				e.getPlayer().removePotionEffect(PotionEffectType.NIGHT_VISION);
				e.getPlayer().removePotionEffect(PotionEffectType.SPEED);
				e.setCancelled(true);
				e.getPlayer().sendTitle(ChatColor.AQUA+"���� Ž����", ChatColor.YELLOW+"�ɷ� ����ȭ �Ϸ�");
				this.getServer().dispatchCommand(this.getServer().getConsoleSender(), "pex user "+e.getPlayer().getName()+" remove essentials.top");
				this.getServer().dispatchCommand(this.getServer().getConsoleSender(), "pex user "+e.getPlayer().getName()+" remove minecraft.command.locate");
				break;
			case "�ڶ󳪶� �۹��۹�":
				cards.put(e.getPlayer(), 8);
				e.getPlayer().removePotionEffect(PotionEffectType.NIGHT_VISION);
				e.getPlayer().removePotionEffect(PotionEffectType.SPEED);
				e.setCancelled(true);
				e.getPlayer().sendTitle(ChatColor.AQUA+"�ڶ󳪶� �۹��۹�", ChatColor.YELLOW+"�ɷ� ����ȭ �Ϸ�");
				this.getServer().dispatchCommand(this.getServer().getConsoleSender(), "pex user "+e.getPlayer().getName()+" remove essentials.top");
				this.getServer().dispatchCommand(this.getServer().getConsoleSender(), "pex user "+e.getPlayer().getName()+" remove minecraft.command.locate");
				break;
			case "���� ������":
				cards.put(e.getPlayer(), 9);
				e.getPlayer().removePotionEffect(PotionEffectType.NIGHT_VISION);
				e.getPlayer().removePotionEffect(PotionEffectType.SPEED);
				e.setCancelled(true);
				e.getPlayer().sendTitle(ChatColor.AQUA+"���� ������", ChatColor.YELLOW+"�ɷ� ����ȭ �Ϸ�");
				this.getServer().dispatchCommand(this.getServer().getConsoleSender(), "pex user "+e.getPlayer().getName()+" remove essentials.top");
				this.getServer().dispatchCommand(this.getServer().getConsoleSender(), "pex user "+e.getPlayer().getName()+" remove minecraft.command.locate");
				break;
			case "���� Ž���� ����":
				cards.put(e.getPlayer(), 10);
				e.getPlayer().removePotionEffect(PotionEffectType.NIGHT_VISION);
				e.getPlayer().removePotionEffect(PotionEffectType.SPEED);
				e.setCancelled(true);
				e.getPlayer().sendTitle(ChatColor.AQUA+"���� Ž���� ����", ChatColor.YELLOW+"�ɷ� ����ȭ �Ϸ�");
				this.getServer().dispatchCommand(this.getServer().getConsoleSender(), "pex user "+e.getPlayer().getName()+" remove essentials.top");
				this.getServer().dispatchCommand(this.getServer().getConsoleSender(), "pex user "+e.getPlayer().getName()+" remove minecraft.command.locate");
				break;
			case "�ϰ��ʻ�":
			case "�迩���� �ϰ��ʻ�":
				cards.put(e.getPlayer(), 11);
				e.getPlayer().removePotionEffect(PotionEffectType.NIGHT_VISION);
				e.getPlayer().removePotionEffect(PotionEffectType.SPEED);
				e.setCancelled(true);
				e.getPlayer().sendTitle(ChatColor.AQUA+"�ϰ��ʻ�", ChatColor.YELLOW+"�ɷ� ����ȭ �Ϸ�");
				this.getServer().dispatchCommand(this.getServer().getConsoleSender(), "pex user "+e.getPlayer().getName()+" remove essentials.top");
				this.getServer().dispatchCommand(this.getServer().getConsoleSender(), "pex user "+e.getPlayer().getName()+" remove minecraft.command.locate");
				break;
			case "������ ����":
				cards.put(e.getPlayer(),12);
				e.getPlayer().removePotionEffect(PotionEffectType.NIGHT_VISION);
				e.getPlayer().removePotionEffect(PotionEffectType.SPEED);
				e.setCancelled(true);
				e.getPlayer().sendTitle(ChatColor.AQUA+"�ϰ��ʻ�", ChatColor.YELLOW+"�ɷ� ����ȭ �Ϸ�");
				this.getServer().dispatchCommand(this.getServer().getConsoleSender(), "pex user "+e.getPlayer().getName()+" remove essentials.top");
				this.getServer().dispatchCommand(this.getServer().getConsoleSender(), "pex user "+e.getPlayer().getName()+" remove minecraft.command.locate");
				break;
			case "Ȯ����":
				cards.put(e.getPlayer(),13);
				e.getPlayer().removePotionEffect(PotionEffectType.NIGHT_VISION);
				e.getPlayer().removePotionEffect(PotionEffectType.SPEED);
				e.setCancelled(true);
				e.getPlayer().sendTitle(ChatColor.AQUA+"Ȯ����", ChatColor.YELLOW+"�ɷ� ����ȭ �Ϸ�");
				this.getServer().dispatchCommand(this.getServer().getConsoleSender(), "pex user "+e.getPlayer().getName()+" remove essentials.top");
				this.getServer().dispatchCommand(this.getServer().getConsoleSender(), "pex user "+e.getPlayer().getName()+" remove minecraft.command.locate");
				break;
			case "��Ƽ��Ȩ":
				cards.put(e.getPlayer(),15);
				e.getPlayer().removePotionEffect(PotionEffectType.NIGHT_VISION);
				e.getPlayer().removePotionEffect(PotionEffectType.SPEED);
				e.setCancelled(true);
				e.getPlayer().sendTitle(ChatColor.AQUA+"��Ƽ��Ȩ", ChatColor.YELLOW+"�ɷ� ����ȭ �Ϸ�");
				this.getServer().dispatchCommand(this.getServer().getConsoleSender(), "pex user "+e.getPlayer().getName()+" remove essentials.top");
				this.getServer().dispatchCommand(this.getServer().getConsoleSender(), "pex user "+e.getPlayer().getName()+" remove minecraft.command.locate");
				break;
			case "������ ���� �丮��":
				cards.put(e.getPlayer(),16);
				e.getPlayer().removePotionEffect(PotionEffectType.NIGHT_VISION);
				e.getPlayer().removePotionEffect(PotionEffectType.SPEED);
				e.setCancelled(true);
				e.getPlayer().sendTitle(ChatColor.AQUA+"������ ���� �丮��", ChatColor.YELLOW+"�ɷ� ����ȭ �Ϸ�");
				this.getServer().dispatchCommand(this.getServer().getConsoleSender(), "pex user "+e.getPlayer().getName()+" remove essentials.top");
				this.getServer().dispatchCommand(this.getServer().getConsoleSender(), "pex user "+e.getPlayer().getName()+" remove minecraft.command.locate");
				break;
			case "�����͹���":
				if(cards.containsKey(e.getPlayer())) {
					if(cards.get(e.getPlayer())==14) {
						e.setCancelled(true);
						if(this.AfterBurnerCool.containsKey(e.getPlayer())) {
							long cooltime = System.currentTimeMillis() - AfterBurnerCool.get(e.getPlayer());
							if(cooltime >= 20000) {
								e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 50, 2));
								e.getPlayer().getWorld().spawnParticle(Particle.SPELL_INSTANT, e.getPlayer().getLocation(), 10);
								AfterBurnerCool.put(e.getPlayer(), System.currentTimeMillis());
								
							}
							else {
								e.getPlayer().sendMessage("��Ÿ�ӱ��� "+ChatColor.RED+String.valueOf(20-cooltime/1000)+ChatColor.WHITE+"�� ���ҽ��ϴ�");
							}
						}
						else {
							e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 50, 2));
							e.getPlayer().getWorld().spawnParticle(Particle.SPELL_INSTANT, e.getPlayer().getLocation(), 10);
							AfterBurnerCool.put(e.getPlayer(), System.currentTimeMillis());
						}
					}
					else {
						cards.put(e.getPlayer(),14);
						e.getPlayer().removePotionEffect(PotionEffectType.NIGHT_VISION);
						e.getPlayer().removePotionEffect(PotionEffectType.SPEED);
						e.setCancelled(true);
						e.getPlayer().sendTitle(ChatColor.AQUA+"������ ����", ChatColor.YELLOW+"�ɷ� ����ȭ �Ϸ�");
						this.getServer().dispatchCommand(this.getServer().getConsoleSender(), "pex user "+e.getPlayer().getName()+" remove essentials.top");
						this.getServer().dispatchCommand(this.getServer().getConsoleSender(), "pex user "+e.getPlayer().getName()+" remove minecraft.command.locate");
					}
				}
				else{
					cards.put(e.getPlayer(),14);
					e.getPlayer().removePotionEffect(PotionEffectType.NIGHT_VISION);
					e.getPlayer().removePotionEffect(PotionEffectType.SPEED);
					e.setCancelled(true);
					e.getPlayer().sendTitle(ChatColor.AQUA+"������ ����", ChatColor.YELLOW+"�ɷ� ����ȭ �Ϸ�");
					this.getServer().dispatchCommand(this.getServer().getConsoleSender(), "pex user "+e.getPlayer().getName()+" remove essentials.top");
					this.getServer().dispatchCommand(this.getServer().getConsoleSender(), "pex user "+e.getPlayer().getName()+" remove minecraft.command.locate");	
				}
				break;

			}
		}
	}
	private void readFile() {
		try {
			//File file = new File("D:\\�� ���� (8)\\�� ����\\����\\plugins\\hcCardFight\\uuid.txt");
			File file = new File("C:\\�������\\plugins\\hcCardFight\\uuid.txt");
			Scanner scan = new Scanner(file);
			int N = scan.nextInt();
			for(int i=0; i<N; i++) {
				UUID uid = UUID.fromString(scan.next());
				hcCardSelect.players.add(uid);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			//File file = new File("D:\\�� ���� (8)\\�� ����\\����\\plugins\\hcCardFight\\warp.txt");
			File file = new File("C:\\�������\\plugins\\hcCardFight\\warp.txt");
			Scanner scan = new Scanner(file);
			int N = scan.nextInt();
			for(int i=0; i<N; i++) {
				UUID uid = UUID.fromString(scan.next());
				String world = "world";
				int x=0, y=0, z=0;
				
				world = scan.next();
				x = scan.nextInt();
				y = scan.nextInt();
				z = scan.nextInt();
				Location n;
				n = new Location(Bukkit.getServer().getWorld(world),x,y,z);
				this.tpLocation.put(uid, n);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void saveFile() {
		// TODO Auto-generated method stub
        try{
            //���� ��ü ����
        	//File file = new File("D:\\�� ���� (8)\\�� ����\\����\\plugins\\hcCardFight\\uuid.txt");
            File file = new File("C:\\�������\\plugins\\hcCardFight\\uuid.txt");
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            
            List<UUID> real = new ArrayList<>(); 
            
			if (file.isFile() && file.canWrite()) {
				for (UUID u : hcCardSelect.players) {
					if (!(real.contains(u))) {
						real.add(u);
						// ���๮�ھ���

					}
				}
				int size = real.size();
				bufferedWriter.write(String.valueOf(size));
				for (UUID u : real) {

					// ���๮�ھ���
					bufferedWriter.newLine();
					bufferedWriter.write(u.toString());
				}
                bufferedWriter.close();
            }
        }catch (IOException e) {
            System.out.println(e);
        }
        try{
            //���� ��ü ����
        	File file = new File("C:\\�������\\plugins\\hcCardFight\\warp.txt");
        	//File file = new File("D:\\�� ���� (8)\\�� ����\\����\\plugins\\hcCardFight\\warp.txt");
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            int size = this.tpLocation.size();
            if(file.isFile() && file.canWrite()){
            	bufferedWriter.write(String.valueOf(size));
            	Set<Entry<UUID, Location>> entries = tpLocation.entrySet();
				for (Entry<UUID, Location> entry : entries) {
            		//���๮�ھ���
            		bufferedWriter.newLine();
            		bufferedWriter.write(entry.getKey().toString());
            		Location l = entry.getValue();
					String str = " "+l.getWorld().getName()+" "+l.getX()+" "+l.getY()+" "+l.getZ();
					bufferedWriter.write(str);
				}
                bufferedWriter.close();
            }
        }catch (IOException e) {
            System.out.println(e);
        }

	}
	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}
	private boolean setupChat() {
		RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
		chat = rsp.getProvider();
		return chat != null;
	}

	private boolean setupPermissions() {
		RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
		perms = rsp.getProvider();
		return perms != null;
	}

	public static Economy getEconomy() {
		return econ;
	}

	public static Permission getPermissions() {
		return perms;
	}

	public static Chat getChat() {
		return chat;
	}
	
}
