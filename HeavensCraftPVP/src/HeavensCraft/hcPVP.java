package HeavensCraft;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Logger;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import com.mewin.WGRegionEvents.events.RegionEnterEvent;
import com.mewin.WGRegionEvents.events.RegionEnteredEvent;
import com.mewin.WGRegionEvents.events.RegionLeaveEvent;
import com.mewin.WGRegionEvents.events.RegionLeftEvent;
import com.shampaggon.crackshot.events.WeaponDamageEntityEvent;

import Clans.ClanConfiguration;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import net.minecraft.server.v1_12_R1.World;

public class hcPVP extends JavaPlugin implements Listener {
	private static final Logger log = Logger.getLogger("Minecraft");
	private static Economy econ = null;
	private static Permission perms = null;
	private static Chat chat = null;
	
	int Num_map=1;
	int Num_pos1=0;
	int Num_pos2=0;
	
	private static int taskId1=0;
	private static int taskId2;
	private static boolean nowLoading = false;
	private static boolean canBet = true;
	private static boolean fighting = false;
	private static Player PlayerA = null;
	private static Player PlayerB = null;
	
	public static int timer;
	public static boolean waiting;
    BukkitScheduler scheduler = getServer().getScheduler();
	static HashMap<String,Score> score= new HashMap<>();
	//HashMap<Player,Boolean> canPosion = new HashMap<>();
	HashMap<Player,Integer> bet = new HashMap<>();
	@Override
	public void onEnable() {
		Bukkit.getConsoleSender()
				.sendMessage(ChatColor.GOLD + ("hcPVP") + ChatColor.AQUA + " �÷����� Ȱ��ȭ" + ChatColor.BLACK);
		Bukkit.getPluginManager().registerEvents(this, this);
		/*if (!setupEconomy()) {
			log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
			getServer().getPluginManager().disablePlugin(this);
			return;
		}*/
		readFile();
		scheduler.runTaskTimerAsynchronously(this, new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				saveFile();
				readFile();
				
			}
			
		}, 0, 72000L);

		
		/*setupPermissions();
		setupChat();*/
	}

	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		log.info(String.format("[%s] Disabled Version %s", getDescription().getName(), getDescription().getVersion()));
		Bukkit.getConsoleSender()
				.sendMessage(ChatColor.GOLD + ("hcPVP") + ChatColor.DARK_AQUA + " �÷����� ��Ȱ��ȭ" + ChatColor.BLACK);
		saveFile();
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
	
	

	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equals("adminpvp")) {
			if(sender.isOp() || sender instanceof ConsoleCommandSender) {
				if(args[0].equals("give")) {
					this.score.get(args[1]).setElo(score.get(args[1]).getElo()+Integer.valueOf(args[2]));
				}
				else if(args[0].equals("take")) {
					this.score.get(args[1]).setElo(score.get(args[1]).getElo()-Integer.valueOf(args[2]));
				}
			}
		}
		if(sender instanceof Player) {
			switch(cmd.getName()) {
			case "PVP":
			case "Pvp":
				if (args.length > 0) {
					switch (args[0]) {
					case "��Ī":
						if(this.nowLoading==false) {
							PlayerA = (Player) sender;
							this.nowLoading=true;
							sender.sendMessage(ChatColor.GREEN+"���������� PVP ��Ī�� �����մϴ�!");
						}
						else {
							if (fighting == true) {
								sender.sendMessage("���� PVP ��û�� �Ұ��մϴ�");
							} else {
								if (PlayerA != null || PlayerB != null) {
									if (!(PlayerA.getName().equals(sender.getName()))) {
										PlayerB = (Player) sender;
										PlayerB.sendMessage(ChatColor.GREEN + "PVP ��Ī�� �������ϴ�!");
										PlayerB.sendMessage(ChatColor.YELLOW + "��� : " + PlayerA.getName());
										if (score.containsKey(PlayerA.getName()))
											PlayerB.sendMessage(ChatColor.LIGHT_PURPLE + ": "
													+ score.get(PlayerA.getName()).showStatic());
										else {
											score.put(PlayerA.getName(), new Score(0, 0, 0, 0, 1000));
											PlayerB.sendMessage(ChatColor.LIGHT_PURPLE + ": "
													+ score.get(PlayerA.getName()).showStatic());
										}

										PlayerA.sendMessage(ChatColor.GREEN + "PVP ��Ī�� �������ϴ�!");
										PlayerA.sendMessage(ChatColor.YELLOW + "��� : " + PlayerB.getName());
										if (score.containsKey(PlayerB.getName()))
											PlayerA.sendMessage(ChatColor.LIGHT_PURPLE + ": "
													+ score.get(PlayerB.getName()).showStatic());
										else {
											score.put(PlayerB.getName(), new Score(0, 0, 0, 0, 1000));
											PlayerA.sendMessage(ChatColor.LIGHT_PURPLE + ": "
													+ score.get(PlayerB.getName()).showStatic());
										}
										getServer().dispatchCommand(getServer().getConsoleSender(), "broadcast "+PlayerA.getName()+"�԰� "+PlayerB.getName()+"���� 1:1 PVP�� ���۵˴ϴ�! �����Ͻ� ���� /PVP ���� �̶�� �Է����ּ���!");
										this.StartPVP();
									} else {
										sender.sendMessage(ChatColor.RED + "���� �߻�! �̹� ��Ī ���̽ʴϴ�.");
									}
								} else {
									sender.sendMessage("�̹� PVP�� �������Դϴ�! ");// +PlayerA.getName()+" VS
																			// "+PlayerB.getName());
								}
							}

						}
						break;
					case "����":
						if(args.length>=2) {
							if(score.containsKey(args[1])) {
								sender.sendMessage(ChatColor.YELLOW+"�̸� : "+args[1]+ChatColor.LIGHT_PURPLE+"\n"+score.get(args[1]).showStatic());
								break;
							}
							sender.sendMessage("�ش� �÷��̾ �������� �ʰų� ���� PVP�� �÷������� �ʾҽ��ϴ�.");
						}
						break;
					case "��Ī���":
						if(this.fighting!=true) {
							if(PlayerA.getName().equals(sender.getName())){
								PlayerA=null;
								this.nowLoading=false;
								sender.sendMessage(ChatColor.GREEN+"��� �Ϸ�!");
							}
						}
						else {
							sender.sendMessage("���� ��Ī ��Ұ� �Ұ����մϴ�");
						}
						break;
						/*case "��û":
						if (this.nowLoading == false) {
							if (args.length >= 2) {
								Player p = null;
								for (Player i : this.getServer().getOnlinePlayers()) {
									if (i.getName().equals(args[1])) {
										p = i;											
										break;
									}
								}
								if (p == null) {
									sender.sendMessage(ChatColor.RED + "�ش� �÷��̾ ã�� �� �����ϴ�!");
								} else {
									PlayerA = (Player) sender;
									PlayerB = p;
									if(PlayerA.getName().equals(PlayerB.getName())) {
										sender.sendMessage("�ڱ� �ڽſ��Դ� PVP ��û�� �� �� �����ϴ�!");
										break;
									}
									sender.sendMessage("���������� PVP ��û�� ���½��ϴ�!");
									p.sendMessage(ChatColor.YELLOW + sender.getName() + ChatColor.WHITE
											+ "���ż� PVP ��û�� ���Խ��ϴ�!\n'/PVP ����' �Ǵ� '/PVP ����'�� �ǻ縦 ǥ���ϼ���!");
									if(score.containsKey(PlayerA.getName())) {
										p.sendMessage(ChatColor.LIGHT_PURPLE+score.get(PlayerA.getName()).showStatic());
									}
									else{
										score.put(PlayerA.getName(), new Score());
										p.sendMessage(ChatColor.LIGHT_PURPLE+score.get(PlayerA.getName()).showStatic());
									}
									this.nowLoading = true;
								}
							}
							else {
								sender.sendMessage(ChatColor.YELLOW+"============================================================");
								sender.sendMessage(ChatColor.YELLOW+"/PVP ��û <�̸�> : "+ChatColor.AQUA+"�ٸ� ������� PVP�� ��û�մϴ�");
								sender.sendMessage(ChatColor.YELLOW+"/PVP ���� : "+ChatColor.AQUA+"���� PVP ��û�� �����մϴ�");
								sender.sendMessage(ChatColor.YELLOW+"/PVP ���� : "+ChatColor.AQUA+"���� PVP ��û�� �����մϴ�");
								sender.sendMessage(ChatColor.YELLOW+"/PVP ���� : "+ChatColor.AQUA+"����Ǵ� PVP�� �����մϴ�");
								sender.sendMessage(ChatColor.YELLOW+"============================================================");
							}
						}
						else {
							sender.sendMessage("�̹� �������� PVP�� �ֽ��ϴ�! ���� �ִٰ� �ٽ� ��û���ּ���! ����� : "+PlayerA.getName()+", "+PlayerB.getName());
						}
						break;*/
					/*case "����":
						if(sender == PlayerB) {
							PlayerA.sendMessage(PlayerB.getName()+"�Բ��� PVP�� �����ϼ̽��ϴ�.");
							getServer().dispatchCommand(getServer().getConsoleSender(), "broadcast "+PlayerA.getName()+"�԰� "+PlayerB.getName()+"���� 1:1 PVP�� ���۵˴ϴ�! �����Ͻ� ���� /PVP ���� �̶�� �Է����ּ���!");
							canBet=true;
							Bukkit.getScheduler().runTaskLater(this, new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									StartPVP();
								}
							}, 0); //200����!!!!!!!!!!
						}
						else {
							sender.sendMessage("�߸��� �Է��Դϴ�");
						}
						break;
					case "����":
						if(sender == PlayerB) {
							PlayerA.sendMessage(PlayerB.getName()+"�Բ��� PVP�� �����ϼ̽��ϴ�.");
							PlayerA=null;
							PlayerB=null;
							this.nowLoading=false;
						}
						else {
							sender.sendMessage("�߸��� �Է��Դϴ�");
						}
						break;*/
					case "����":
						if (this.nowLoading == true) {
							if (sender.getName().equals(PlayerA.getName()) || sender.getName().equals(PlayerB.getName())) {
								sender.sendMessage("�ڱ� �ڽ��� PVP�� ������ �Ұ��մϴ�");
							} else {
								getServer().dispatchCommand(getServer().getConsoleSender(),
										"warp stand"+this.Num_map+" " + sender.getName());
								sender.sendMessage("���߼����� �̵��Ͽ����ϴ�.");
							}
						}
						else {
							sender.sendMessage("�������� PVP�� �����ϴ�!");
						}						
						break;
					case "����":
					case "��ŷ":
						if(args.length>=2) {
							if(Integer.valueOf(args[1])<1) {
								sender.sendMessage("�߸� �� �Է��Դϴ�.");
							}
							else {
								this.showRankPlayer(sender, Integer.valueOf(args[1]));
							}
						}
						else {
							this.showRankPlayer(sender,1);
						}
						
						break;
					/*case "����":
						if (args.length >= 2) {
							if (this.canBet == true) {
								if(econ.getBalance((OfflinePlayer)sender) >= Double.valueOf(args[1])) {	
									bet.put((Player) sender, Integer.getInteger(args[1]));
								}
								else {
									sender.sendMessage("�ܾ��� �����մϴ�!");
								}
							}
							else {
								sender.sendMessage("������ ������ �Ұ��մϴ�!");
							}
						}
						else {
							sender.sendMessage("���� �Ͻ÷��� ��ɾ�� /PVP ���� <����> �Դϴ�!");
						}
						break;*/
					case "���":
						if(sender.isOp()) {
							this.statics();
						}
						break;
					case "����":
					default:
						sender.sendMessage(ChatColor.YELLOW+"============================================================");
						sender.sendMessage(ChatColor.YELLOW+"/PVP ��Ī : "+ChatColor.AQUA+"PVP ��Ī�� ã���ϴ�");
						sender.sendMessage(ChatColor.YELLOW+"/PVP ��Ī��� : "+ChatColor.AQUA+"PVP ��Ī�� ����մϴ�");
						sender.sendMessage(ChatColor.YELLOW+"/PVP ���� <�̸�> : "+ChatColor.AQUA+"�÷��̾��� ������ Ȯ���մϴ�");
						sender.sendMessage(ChatColor.YELLOW+"/PVP ���� : "+ChatColor.AQUA+"����Ǵ� PVP�� �����մϴ�");
						sender.sendMessage(ChatColor.YELLOW+"/PVP ���� : "+ChatColor.AQUA+"���� PVP ��ŷ�� Ȯ���մϴ�");
						sender.sendMessage(ChatColor.YELLOW+"============================================================");
						break;
					}

				}
				else {
					helpPVP(sender);
				}
				break;
			}
		}
		return true;		
	}

	private void StartPVP() {
		Random rand = new Random();
		// TODO Auto-generated method stub
		this.canBet=true;
		
		Num_map=rand.nextInt(3)+1;
		
		if(Num_map == 1) {
			PlayerA.getWorld().loadChunk(-170, 110);
		}else if(Num_map == 2) {
			PlayerA.getWorld().loadChunk(-830, -178);
		}
		
		this.fighting=true;
		scheduler.runTaskLater(this, new Runnable() {
			@Override
			public void run() {
				if(PlayerA!=null && PlayerB!=null) {
					PlayerA.sendTitle("3", "");
					PlayerB.sendTitle("3", "");
				}
				return;
			}
		}, 20L);
		
		scheduler.runTaskLater(this, new Runnable() {
			@Override
			public void run() {
				if(PlayerA!=null && PlayerB!=null) {
					PlayerA.sendTitle("2", "");
					PlayerB.sendTitle("2", "");
				}
				return;
			}
		}, 40L);
		
		scheduler.runTaskLater(this, new Runnable() {
			@Override
			public void run() {
				if(PlayerA!=null && PlayerB!=null) {
					PlayerA.sendTitle("1", "");
					PlayerB.sendTitle("1", "");
				}
				return;
			}
		}, 60L);
		
		scheduler.runTaskLater(this, new Runnable() {
			@Override
			public void run() {
				if(Num_map!=3) {
				PlayerA.sendTitle("����!", "");
				PlayerB.sendTitle("����!", "");
				}
				else {
					PlayerA.sendMessage("");
					PlayerA.sendMessage(ChatColor.YELLOW+"�ش� �ʿ����� "+ChatColor.RED+"30�ʰ�"+ChatColor.YELLOW+" ������ �ð��� �帳�ϴ�!");
					PlayerA.sendMessage(ChatColor.YELLOW+"�κ��� �־��� ���� �̿��� ������ �����Ͻð� ������ "+ChatColor.GREEN+"�����ϰ�"+ChatColor.YELLOW+" �̲��������!");
					PlayerA.sendMessage("");
					
					PlayerB.sendMessage("");
					PlayerB.sendMessage(ChatColor.YELLOW+"�ش� �ʿ����� "+ChatColor.RED+"30�ʰ�"+ChatColor.YELLOW+" ������ �ð��� �帳�ϴ�!");
					PlayerB.sendMessage(ChatColor.YELLOW+"�κ��� �־��� ���� �̿��� ������ �����Ͻð� ������ "+ChatColor.GREEN+"�����ϰ�"+ChatColor.YELLOW+" �̲��������!");
					PlayerB.sendMessage("");
					
					PlayerA.getInventory().addItem(new ItemStack(Material.DIRT, 64));
					PlayerB.getInventory().addItem(new ItemStack(Material.DIRT, 64));
				}
				canBet = false;
				
				Random rand = new Random();
				
				
				Num_pos1=rand.nextInt(4);
				while(true) {
					Num_pos2=rand.nextInt(4);
					if(Num_pos2!=Num_pos1) break;
				}
				
				
				boolean canPlay=true;
				ItemStack[] is = PlayerA.getInventory().getContents();
				int countA = 0;
				for (ItemStack i : is) {
					if (i!=null) {

						if (i.getType().equals((Material.ELYTRA))) {
							canPlay = false;
							getServer().dispatchCommand(getServer().getConsoleSender(), "broadcast "+PlayerA.getName()+"���� �ѳ����� �����ϰ� �ֽ��ϴ�! PVP�� ��ҵǾ����ϴ�!");
							break;
						}
						if (i.getTypeId() == 373 || i.getTypeId() == 438 || i.getTypeId() == 441
								|| (i.getTypeId() == 322)) {
							if(i.getTypeId()==322 && i.getData().getData()==1) {
								canPlay=false;
								getServer().dispatchCommand(getServer().getConsoleSender(), "broadcast "+PlayerB.getName()+"���� ��æƮ Ȳ�ݻ���� ��� �ֽ��ϴ�! PVP�� ��ҵǾ����ϴ�!");
								break;
							}
							countA++;
						}
					}
				}
				is = PlayerB.getInventory().getContents();
				int countB = 0;
				for(ItemStack i : is) {
					if (i!=null) {

						if (i.getType().equals((Material.ELYTRA))) {
							getServer().dispatchCommand(getServer().getConsoleSender(), "broadcast "+PlayerB.getName()+"���� �ѳ����� �����ϰ� �ֽ��ϴ�! PVP�� ��ҵǾ����ϴ�!");
							canPlay = false;
							break;
						}
						if (i.getTypeId() == 373 || i.getTypeId() == 438 || i.getTypeId() == 441
								|| (i.getTypeId() == 322)) {
							if(i.getTypeId()==322 && i.getData().getData()==1) {
								canPlay=false;
								getServer().dispatchCommand(getServer().getConsoleSender(), "broadcast "+PlayerB.getName()+"���� ��æƮ Ȳ�ݻ���� ��� �ֽ��ϴ�! PVP�� ��ҵǾ����ϴ�!");
								break;
							}
							countB++;
						}
					}
				}
				
				if(countA>=2 || countB>=2) {
					getServer().dispatchCommand(getServer().getConsoleSender(), "broadcast ���ǰ� Ȳ�ݻ���� 1�������� ������ּ���! PVP�� ��ҵǾ����ϴ�!");
					canPlay=false;
				}
				if(canPlay==true) {
					getServer().dispatchCommand(getServer().getConsoleSender(), "warp map"+Num_map+"_"+Num_pos1+" " + PlayerA.getName());
					getServer().dispatchCommand(getServer().getConsoleSender(), "warp map"+Num_map+"_"+Num_pos2+" " + PlayerB.getName());
					PlayerA.getActivePotionEffects().clear();
					PlayerB.getActivePotionEffects().clear();
				}
				else {
					PlayerA = null;
					PlayerB = null;
					fighting=false;
					nowLoading = false;
				}
				return;
			}
		}, 80L);
		if(this.Num_map!=3)
			timer = 305;
		else {
			timer = 340;
			getServer().dispatchCommand(getServer().getConsoleSender(), "rg flag battleplace3 -w world pvp deny");
			getServer().dispatchCommand(getServer().getConsoleSender(), "rg flag battleplace3 -w world block-place allow");
			getServer().dispatchCommand(getServer().getConsoleSender(), "rg flag battleplace3 -w world block-break allow");
		}
		waiting = true;
		scheduler.cancelTask(taskId1);
		taskId1 = scheduler.runTaskTimer(this, new Runnable() {
			public void run() {
				timer--;
				if(PlayerA!=null && PlayerB!=null &&waiting &&timer-308>=0 &&canBet==false) {
					if(timer-308>=10) {
					PlayerA.sendTitle("", "00 : "+String.valueOf(timer-308));
					PlayerB.sendTitle("", "00 : "+String.valueOf(timer-308));
					}
					else {
						PlayerA.sendTitle("", "00 : 0"+String.valueOf(timer-308));
						PlayerB.sendTitle("", "00 : 0"+String.valueOf(timer-308));
					}
				}
				if(timer == 308) {
					getServer().dispatchCommand(getServer().getConsoleSender(), "rg flag battleplace3 -w world pvp allow");
					getServer().dispatchCommand(getServer().getConsoleSender(), "rg flag battleplace3 -w world block-place allow");
					getServer().dispatchCommand(getServer().getConsoleSender(), "rg flag battleplace3 -w world block-break allow");
					PlayerA.sendTitle("���� ����!", "");
					PlayerB.sendTitle("���� ����!", "");
				}
				if(timer == 30) {
					if(PlayerA!=null && PlayerB!=null) {
						PlayerA.sendTitle("30�� ���ҽ��ϴ�", "");
						PlayerB.sendTitle("30�� ���ҽ��ϴ�", "");
					}
				}
				if (waiting && timer < 0) {
					waiting = false;
					if(PlayerA!=null && PlayerB!=null) {
						PlayerA.sendTitle("���º�", "");
						PlayerB.sendTitle("���º�", "");
						getServer().dispatchCommand(getServer().getConsoleSender(),
								"broadcast " + PlayerB.getName() + "�԰� " + PlayerA.getName() + "���� �ºΰ� ���ºη� ������ �Ǿ����ϴ�!");
						
						getServer().dispatchCommand(getServer().getConsoleSender(), "spawn " + PlayerA.getName());
						getServer().dispatchCommand(getServer().getConsoleSender(), "spawn " + PlayerB.getName());
						getServer().dispatchCommand(getServer().getConsoleSender(), "heal " + PlayerA.getName());
						getServer().dispatchCommand(getServer().getConsoleSender(), "heal " + PlayerB.getName());
						for (int i = -728; i <= -693; i++) {
							for (int k = 62; k <= 71; k++) {
								for (int j = 101; j <= 137; j++) {
									if(Bukkit.getServer().getWorld("world").getBlockAt(new Location(Bukkit.getServer().getWorld("world"),i,k,j)).getType() == Material.OBSIDIAN) {
										Bukkit.getServer().getWorld("world").getBlockAt(new Location(Bukkit.getServer().getWorld("world"),i,k,j)).setType(Material.LAVA);
									}
									else if(k!=62) Bukkit.getServer().getWorld("world").getBlockAt(new Location(Bukkit.getServer().getWorld("world"),i,k,j)).setType(Material.AIR);
								}
							}
						}
						PlayerA = null;
						PlayerB = null;
						
						nowLoading = false;
						fighting=false;
					}
					return;
				}
			}
		}, 0, 20L).getTaskId();
		/*taskId1 = scheduler.runTaskLater(this, new Runnable() {
			@Override
			public void run() {
				if(PlayerA!=null && PlayerB!=null) {
					PlayerA.sendTitle("30�� ���ҽ��ϴ�", "");
					PlayerB.sendTitle("30�� ���ҽ��ϴ�", "");
				}
				return;
			}
		}, 3000L).getTaskId();
		taskId2=scheduler.runTaskLater(this, new Runnable() {
			@Override
			public void run() {
				if(PlayerA!=null && PlayerB!=null) {
					PlayerA.sendTitle("���º�", "");
					PlayerB.sendTitle("���º�", "");
					getServer().dispatchCommand(getServer().getConsoleSender(),
							"broadcast " + PlayerB.getName() + "�԰� " + PlayerA.getName() + "���� �ºΰ� ���ºη� ������ �Ǿ����ϴ�!");
					
					getServer().dispatchCommand(getServer().getConsoleSender(), "spawn " + PlayerA.getName());
					getServer().dispatchCommand(getServer().getConsoleSender(), "spawn " + PlayerB.getName());
					getServer().dispatchCommand(getServer().getConsoleSender(), "heal " + PlayerA.getName());
					getServer().dispatchCommand(getServer().getConsoleSender(), "heal " + PlayerB.getName());
					
					PlayerA = null;
					PlayerB = null;
					
					nowLoading = false;
					fighting=false;
				}
				return;
			}
		}, 3600L).getTaskId();*/
		

	}

	@EventHandler
	private void endPVP(PlayerDeathEvent e) {
		if (PlayerA != null && PlayerB != null) {
			if (e.getEntity() instanceof Player) {
				Player p = e.getEntity();
				if (this.nowLoading == true && this.canBet == false) {
					if (p.getName().equals(PlayerA.getName())) {
						getServer().dispatchCommand(getServer().getConsoleSender(),
								"broadcast " + PlayerB.getName() + "���� " + PlayerA.getName() + "���� �̱�̽��ϴ�!");
						Result(PlayerB, PlayerA);
						PlayerB.sendTitle(ChatColor.GREEN + "�� ��", "");
						getServer().dispatchCommand(getServer().getConsoleSender(), "back " + PlayerB.getName());
						getServer().dispatchCommand(getServer().getConsoleSender(), "spawn " + PlayerB.getName());
						PlayerA = null;
						PlayerB = null;
						if (this.Num_map == 3) {
							for (int i = -728; i <= -693; i++) {
								for (int k = 62; k <= 71; k++) {
									for (int j = 101; j <= 137; j++) {
										if(Bukkit.getServer().getWorld("world").getBlockAt(new Location(Bukkit.getServer().getWorld("world"),i,k,j)).getType() == Material.OBSIDIAN) {
											Bukkit.getServer().getWorld("world").getBlockAt(new Location(Bukkit.getServer().getWorld("world"),i,k,j)).setType(Material.LAVA);
										}
										else if(k!=62) Bukkit.getServer().getWorld("world").getBlockAt(new Location(Bukkit.getServer().getWorld("world"),i,k,j)).setType(Material.AIR);
									}
								}
							}
						}
						
						nowLoading = false;
					} else if (p.getName().equals(PlayerB.getName())) {
						getServer().dispatchCommand(getServer().getConsoleSender(),
								"broadcast " + PlayerA.getName() + "���� " + PlayerB.getName() + "���� �̱�̽��ϴ�!");
						Result(PlayerA, PlayerB);
						PlayerA.sendTitle(ChatColor.GREEN + "�� ��", "");
						getServer().dispatchCommand(getServer().getConsoleSender(), "back " + PlayerA.getName());
						getServer().dispatchCommand(getServer().getConsoleSender(), "spawn " + PlayerA.getName());
						PlayerA = null;
						PlayerB = null;
						if (this.Num_map == 3) {
							for (int i = -728; i <= -693; i++) {
								for (int k = 62; k <= 71; k++) {
									for (int j = 101; j <= 137; j++) {
										if(Bukkit.getServer().getWorld("world").getBlockAt(new Location(Bukkit.getServer().getWorld("world"),i,k,j)).getType() == Material.OBSIDIAN) {
											Bukkit.getServer().getWorld("world").getBlockAt(new Location(Bukkit.getServer().getWorld("world"),i,k,j)).setType(Material.LAVA);
										}
										else if(k!=62) Bukkit.getServer().getWorld("world").getBlockAt(new Location(Bukkit.getServer().getWorld("world"),i,k,j)).setType(Material.AIR);
									}
								}
							}
						}
						nowLoading = false;

					}
					
				}
			}
		}
	}

	private void Result(Player A, Player B) {
		// TODO Auto-generated method stub
		
		float rating1=score.get(A.getName()).getElo();
		float rating2=score.get(B.getName()).getElo();
		float R1 = (float) Math.pow(10, rating1/400);
		float R2 = (float) Math.pow(10, rating2/400);
		
		float E1 = R1/(R1+R2);
		float E2 = R2/(R1+R2);
		
		int newr1 = (int) (rating1 + (32*(1-E1))+6);
		int newr2 = (int) (rating2 + (32*(0-E2))+2);
		
		if (score.containsKey(A.getName())) {
			//�������� + (��������)*(1-����·�)
			score.get(A.getName()).setElo(newr1);
			
			score.get(A.getName()).win();
		} else {
			score.put(A.getName(), new Score());
			Score s = score.get(A.getName());
			score.get(A.getName()).win();			
		}
		if (score.containsKey(B.getName())) {
			score.get(B.getName()).setElo(newr2);
			score.get(B.getName()).lose();
		} else {
			score.put(B.getName(), new Score());
			score.get(B.getName()).lose();
		}
		this.fighting=false;
		this.getServer().getScheduler().cancelTask(taskId1);
		this.getServer().getScheduler().cancelTask(taskId2);
		getServer().dispatchCommand(getServer().getConsoleSender(), "heal " + PlayerA.getName());
		getServer().dispatchCommand(getServer().getConsoleSender(), "heal " + PlayerB.getName());
	}

	@EventHandler
	public void onLeftPlayer(PlayerQuitEvent e) {
		if (e.getPlayer() != null) {
			if (this.fighting == true) {
				if (e.getPlayer().getName().equals(PlayerA.getName())) {
					e.getPlayer().teleport(new Location(e.getPlayer().getWorld(), -246.5, 73, 267.5));
					getServer().dispatchCommand(getServer().getConsoleSender(),
							"broadcast " + PlayerA.getName() + "���� ����Ͽ� " + PlayerB.getName() + "���� �̱�̽��ϴ�!");
					Result(PlayerB, PlayerA);
					getServer().dispatchCommand(getServer().getConsoleSender(), "back " + PlayerB.getName());
					getServer().dispatchCommand(getServer().getConsoleSender(), "spawn " + PlayerB.getName());
					
					PlayerB.sendTitle(ChatColor.GREEN + "�� ��", "");
					// ����ϱ�
					PlayerA = null;
					PlayerB = null;
					if(this.Num_map==3) {
						for (int i = -728; i <= -693; i++) {
							for (int k = 62; k <= 71; k++) {
								for (int j = 101; j <= 137; j++) {
									if(Bukkit.getServer().getWorld("world").getBlockAt(new Location(Bukkit.getServer().getWorld("world"),i,k,j)).getType() == Material.OBSIDIAN) {
										Bukkit.getServer().getWorld("world").getBlockAt(new Location(Bukkit.getServer().getWorld("world"),i,k,j)).setType(Material.LAVA);
									}
									else if(k!=62) Bukkit.getServer().getWorld("world").getBlockAt(new Location(Bukkit.getServer().getWorld("world"),i,k,j)).setType(Material.AIR);
								}
							}
						}
					}
					this.nowLoading = false;
				} else if (e.getPlayer().getName().equals(PlayerB.getName())) {
					e.getPlayer().teleport(new Location(e.getPlayer().getWorld(), -246.5, 73, 267.5));
					getServer().dispatchCommand(getServer().getConsoleSender(),
							"broadcast " + PlayerB.getName() + "���� ����Ͽ� " + PlayerA.getName() + "���� �̱�̽��ϴ�!");
					Result(PlayerA, PlayerB);
					getServer().dispatchCommand(getServer().getConsoleSender(), "back " + PlayerA.getName());
					getServer().dispatchCommand(getServer().getConsoleSender(), "spawn " + PlayerA.getName());
					PlayerA.sendTitle(ChatColor.GREEN + "�� ��", "");

					PlayerA = null;
					PlayerB = null;

					this.nowLoading = false;
					
					if(this.Num_map==3) {
						for (int i = -728; i <= -693; i++) {
							for (int k = 62; k <= 71; k++) {
								for (int j = 101; j <= 137; j++) {
									if(Bukkit.getServer().getWorld("world").getBlockAt(new Location(Bukkit.getServer().getWorld("world"),i,k,j)).getType() == Material.OBSIDIAN) {
										Bukkit.getServer().getWorld("world").getBlockAt(new Location(Bukkit.getServer().getWorld("world"),i,k,j)).setType(Material.LAVA);
									}
									else if(k!=62) Bukkit.getServer().getWorld("world").getBlockAt(new Location(Bukkit.getServer().getWorld("world"),i,k,j)).setType(Material.AIR);
								}
							}
						}
					}
				}
				
			} else {
				if (PlayerA != null) {
					if (PlayerA.getName().equals(e.getPlayer().getName())) {
						PlayerA = null;
						PlayerB = null;
						this.nowLoading = false;
					}
				}
			}
		}

	}
	

	@EventHandler(priority = EventPriority.NORMAL)
	public void onEnteredRegion(RegionEnterEvent e) {
		if (e.getRegion().getId().contains("battleplace")) {
			String now = e.getPlayer().getName();
			if (PlayerA == null || PlayerB == null) {
				System.out.println("PVP ����! : " + now);
				e.getPlayer().sendTitle(ChatColor.RED + "PVP �߿��� ���� �Ұ��Դϴ�!", "");
				scheduler.runTaskLater(this, new Runnable() {

					@Override
					public void run() {
						e.getPlayer().teleport(new Location(e.getPlayer().getWorld(), -246.5, 73, 267.5));
					}
					
				}, 10L);
				
			} else if(fighting==true) {
				if (!(now.equals(PlayerA.getName()))) {
					if (!(now.equals(PlayerB.getName()))) {
						System.out.println("PVP ����! : " + now);
						e.getPlayer().sendTitle(ChatColor.RED + "PVP �߿��� ���� �Ұ��Դϴ�!", "");
						scheduler.runTaskLater(this, new Runnable() {

							@Override
							public void run() {
								e.getPlayer().teleport(new Location(e.getPlayer().getWorld(), -246.5, 73, 267.5));
							}
							
						}, 10L);
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onExitRegion(RegionLeftEvent e) {
		if(PlayerB==null)
			return;
		if (e.getPlayer() != null && this.nowLoading==true) {
			if (e.getRegion().getId().contains("battleplace")) {
				String now = e.getPlayer().getName();
				if ((now.equals(PlayerA.getName()))) {
					e.getPlayer().teleport(new Location(e.getPlayer().getWorld(), -246.5, 73, 267.5));
					getServer().dispatchCommand(getServer().getConsoleSender(),
							"broadcast " + PlayerA.getName() + "���� ����Ͽ� " + PlayerB.getName() + "���� �̱�̽��ϴ�!");
					Result(PlayerB, PlayerA);
					if (this.Num_map == 3) {
						for (int i = -728; i <= -693; i++) {
							for (int k = 62; k <= 71; k++) {
								for (int j = 101; j <= 137; j++) {
									if(Bukkit.getServer().getWorld("world").getBlockAt(new Location(Bukkit.getServer().getWorld("world"),i,k,j)).getType() == Material.OBSIDIAN) {
										Bukkit.getServer().getWorld("world").getBlockAt(new Location(Bukkit.getServer().getWorld("world"),i,k,j)).setType(Material.LAVA);
									}
									else if(k!=62) Bukkit.getServer().getWorld("world").getBlockAt(new Location(Bukkit.getServer().getWorld("world"),i,k,j)).setType(Material.AIR);
								}
							}
						}
					}
					PlayerB.sendTitle(ChatColor.GREEN + "�� ��", "");
					getServer().dispatchCommand(getServer().getConsoleSender(), "back " + PlayerB.getName());
					getServer().dispatchCommand(getServer().getConsoleSender(), "spawn " + PlayerB.getName());
					PlayerA = null;
					PlayerB = null;
					nowLoading = false;

				}
				else if ((now.equals(PlayerB.getName()))) {
					e.getPlayer().teleport(new Location(e.getPlayer().getWorld(), -246.5, 73, 267.5));
					getServer().dispatchCommand(getServer().getConsoleSender(),
							"broadcast " + PlayerB.getName() + "���� ����Ͽ� " + PlayerA.getName() + "���� �̱�̽��ϴ�!");
					Result(PlayerA, PlayerB);
					if (this.Num_map == 3) {
						for (int i = -728; i <= -693; i++) {
							for (int k = 62; k <= 71; k++) {
								for (int j = 101; j <= 137; j++) {
									if(Bukkit.getServer().getWorld("world").getBlockAt(new Location(Bukkit.getServer().getWorld("world"),i,k,j)).getType() == Material.OBSIDIAN) {
										Bukkit.getServer().getWorld("world").getBlockAt(new Location(Bukkit.getServer().getWorld("world"),i,k,j)).setType(Material.LAVA);
									}
									else if(k!=62) Bukkit.getServer().getWorld("world").getBlockAt(new Location(Bukkit.getServer().getWorld("world"),i,k,j)).setType(Material.AIR);
								}
							}
						}
					}
					PlayerA.sendTitle(ChatColor.GREEN + "�� ��", "");

					getServer().dispatchCommand(getServer().getConsoleSender(), "back " + PlayerA.getName());
					getServer().dispatchCommand(getServer().getConsoleSender(), "spawn " + PlayerA.getName());
					PlayerA = null;
					PlayerB = null;
					nowLoading = false;
				}
			}
		}
	}
	/*@EventHandler
	private void onDamage(EntityDamageEvent e) {
		if(e.getCause().equals(DamageCause.POISON)) {
			if(e.getEntity() instanceof LivingEntity) {
				LivingEntity E = (LivingEntity) e.getEntity();
				E.setNoDamageTicks(1);
				System.out.println(E.getNoDamageTicks());
				
			}
		}
	}*/
	
	
	@EventHandler
	private void onDamge(EntityDamageByEntityEvent e) {
		if ((e.getDamager() instanceof Player)) {
			Player damager = (Player) e.getDamager();
			ItemStack is = damager.getInventory().getItemInMainHand();

			if (is != null) {
				if (is.getTypeId() == 283 && is.getItemMeta().isUnbreakable()==true) {
					switch (is.getDurability()) {
					case 1:
					case 2:
					case 3:
						if (e.getEntity() != null ) {
							if (e.getEntity() instanceof LivingEntity) {
								if(e.getEntity() instanceof Player) {
									Player p = (Player) e.getEntity();
									Location loc = p.getLocation();
									
									if(isIn(loc)) {
										p.removePotionEffect(PotionEffectType.POISON);
										p.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100, 1));
									}
								}/* else {
									LivingEntity E = (LivingEntity) e.getEntity();
									E.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100, 1));
								}*/
							}
						}
						break;
					}
				}
				if (is.getTypeId() == 276 && is.getItemMeta().isUnbreakable()==true) {
					switch (is.getDurability()) {
					case 1:
						if (e.getEntity() instanceof LivingEntity && e.getDamage()>=8) {
							if (e.getEntity() instanceof Player) {
								Player p = (Player) e.getEntity();
								Location loc = p.getLocation();

								if (isIn(loc)) {
									p.removePotionEffect(PotionEffectType.SLOW);
									p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 1));
								}
							} else {
								LivingEntity E = (LivingEntity) e.getEntity();
								E.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 1));
							}
							break;
						}
					}
				}
			}
			if ((e.getEntity() instanceof Player || e.getEntity() instanceof Projectile) && this.nowLoading == true) {
				String Damager_Name = damager.getName();
				if (PlayerA != null) {
					if (Damager_Name.equals(PlayerA.getName())) {
						if (score.get(PlayerA.getName()).getStreak() >= 2) {
							double x = e.getDamage();
							if (x > 0) {
								e.setDamage(x * (1
										/ Math.pow(score.get(PlayerA.getName()).getStreak(), Math.sqrt(1.0 / 3.0))));
								System.out.println(x);
								System.out.println(e.getDamage());
							}
						}
					}
				}
				if (PlayerB != null) {
					if (Damager_Name.equals(PlayerB.getName())) {
						if (score.get(PlayerB.getName()).getStreak() >= 2) {
							double x = e.getDamage();
							if (x > 0) {

								e.setDamage(x * (1
										/ Math.pow(score.get(PlayerB.getName()).getStreak(), Math.sqrt(1.0 / 3.0))));
								System.out.println(x);
								System.out.println(e.getDamage());
							}
						}
					}
				}
			}
		}
		if(e.getEntity() instanceof Player && e.getDamager() instanceof Projectile) {
			Player p = (Player) e.getEntity();
			Projectile pro = (Projectile) e.getDamager();
			ItemStack[] is = p.getInventory().getArmorContents();
			if(is[0]!=null && is[1]!=null && is[2]!=null && is[3]!=null) {
				for(int i=0; i<4; i++) {
					ItemMeta im = is[i].getItemMeta();
					if(is[i].getTypeId()!=313-i || im.isUnbreakable()==false || is[i].getDurability()!=0) {
						break;
					}
					if (pro.getShooter() instanceof HumanEntity) {
						if (i == 3 && this.isIn(p.getLocation()) && ((HumanEntity) pro.getShooter()).getInventory()
								.getItemInMainHand().getTypeId() == 261) {
							e.setCancelled(true);
							pro.remove();
							p.damage(e.getDamage(), e.getDamager());
						}
					}
				}
			}
			if(pro.getShooter() instanceof Player) {
				Player shooter = (Player) pro.getShooter();
				if(shooter.getInventory().getItemInMainHand().getTypeId()==261 && shooter.getInventory().getItemInMainHand().getItemMeta().isUnbreakable()) {
					switch(shooter.getInventory().getItemInMainHand().getDurability()) {
					case 1:
					case 2:
						Location loc = p.getLocation();
						
						if(isIn(loc)) {
							p.removePotionEffect(PotionEffectType.POISON);
							p.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100, 1));
						}
						break;
					}
				}
			}
		}
		
	}

	private boolean isIn(Location loc) {
		// TODO Auto-generated method stub
		double X, Y, Z;
		X= loc.getX();
		Y=loc.getY();
		Z=loc.getZ();
		if(X>=-198 && X<=-158 && Y>=61 && Y<=71 && Z>=98 && Z<=129)
		{
			return true;
		}
		else if(X>=-848 && X<=-818 && Y>=62 && Y<=69 && Z>=-194 && Z<=-153)
		{
			return true;
		}
		else if(X>=-693 && X<=-728 && Y>=63 && Y<=71 && Z>=101 && Z<=137)
		{
			return true;
		}
		return false;
	}

	@EventHandler
	private void onGunshot(WeaponDamageEntityEvent e) {
		String Damager_Name = e.getPlayer().getName();
		if (this.fighting == true) {
			if (Damager_Name.equals(PlayerA.getName())) {
				if (score.get(PlayerA.getName()).getStreak() >= 2) {
					double x = e.getDamage();
					if (x > 0) {
						e.setDamage(x * (1 / Math.pow(score.get(PlayerA.getName()).getStreak(), Math.sqrt(1.0 / 3.0))));

					}
				}
			}
			if (Damager_Name.equals(PlayerB.getName())) {
				if (score.get(PlayerB.getName()).getStreak() >= 2) {

					double x = e.getDamage();
					if (x > 0) {
						e.setDamage(x * (1 / Math.pow(score.get(PlayerB.getName()).getStreak(), Math.sqrt(1.0 / 3.0))));

					}
				}
			}
		}
		
	}
	private void statics() {
		Set<Entry<String, Score>> entries = score.entrySet();
		for (Entry<String, Score> entry : entries) {
			
			String str = entry.getValue().getGames()+" "+entry.getValue().getWin()+" "+entry.getValue().getLose()+" "+entry.getValue().getStreak();
			System.out.println(entry.getKey()+" "+str);
		}
	}

	
	private void helpPVP(CommandSender sender) {
		// TODO Auto-generated method stub
		sender.sendMessage(ChatColor.YELLOW+"============================================================");
		sender.sendMessage(ChatColor.YELLOW+"/PVP ��Ī : "+ChatColor.AQUA+"PVP ��Ī�� ã���ϴ�");
		sender.sendMessage(ChatColor.YELLOW+"/PVP ��Ī��� : "+ChatColor.AQUA+"PVP ��Ī�� ����մϴ�");
		sender.sendMessage(ChatColor.YELLOW+"/PVP ���� <�̸�> : "+ChatColor.AQUA+"�÷��̾��� ������ Ȯ���մϴ�");
		sender.sendMessage(ChatColor.YELLOW+"/PVP ���� : "+ChatColor.AQUA+"����Ǵ� PVP�� �����մϴ�");
		sender.sendMessage(ChatColor.YELLOW+"/PVP ���� : "+ChatColor.AQUA+"���� PVP ��ŷ�� Ȯ���մϴ�");
		sender.sendMessage(ChatColor.YELLOW+"============================================================");
	}
	
	private void readFile() {
		try {
			//File file = new File("/mnt/disks/sdb/����/plugins/hcPVP/data.txt");
			File file = new File("C:\\�������\\plugins\\hcPVP\\data.txt");
			Scanner scan = new Scanner(file);
			int N = scan.nextInt();
			for(int i=0; i<N; i++) {
				String name = scan.next();
				int game = scan.nextInt();
				int win = scan.nextInt();
				int lose = scan.nextInt();
				int streak = scan.nextInt();
				int elo = scan.nextInt();
				score.put(name, new Score(game, win, lose, streak, elo));
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
            File file = new File("C:\\�������\\plugins\\hcPVP\\data.txt");
        	//File file = new File("D:\\�� ���� (8)\\�� ���� (2)\\����\\plugins\\hcPVP\\data.txt");
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            int size = score.size();
            
            if(file.isFile() && file.canWrite()){
            	bufferedWriter.write(String.valueOf(size));
                	
                	Set<Entry<String, Score>> entries = score.entrySet();
    				for (Entry<String, Score> entry : entries) {
                		//���๮�ھ���
                		bufferedWriter.newLine();
                		bufferedWriter.write(entry.getKey());
    					String str = " "+entry.getValue().getGames()+" "+entry.getValue().getWin()+" "+entry.getValue().getLose()+" "+entry.getValue().getStreak()+" "+entry.getValue().getElo();
    					bufferedWriter.write(str);
    				}
                bufferedWriter.close();
            }
        }catch (IOException e) {
            System.out.println(e);
        }
	}
	
	@EventHandler
	private void onPlace(BlockPlaceEvent e) {
		if (this.fighting == true) {

			if (e.getPlayer().getUniqueId().equals(PlayerA.getUniqueId())) {
				if (e.getBlock().getTypeId() != 3) {
					e.setCancelled(true);
					PlayerA.sendMessage("�� ���� ������ �� �ֽ��ϴ�!");
				}
			} else if (e.getPlayer().getUniqueId().equals(PlayerB.getUniqueId())) {
				if (e.getBlock().getTypeId() != 3) {
					e.setCancelled(true);
					PlayerB.sendMessage("�� ���� ������ �� �ֽ��ϴ�!");
				}
			}
		}
		
	}
	private void showRankServer() {
		Iterator it = sortByValue().iterator();
		getServer().dispatchCommand(getServer().getConsoleSender(), "broadcast &6[HeavensPVP ���� 2 ����ǥ]");
		int i = 0;
		while (it.hasNext()) {
			String temp = (String) it.next();
			if (i == 0) {
				getServer().dispatchCommand(getServer().getConsoleSender(),
						"broadcast &6" + temp + "&f : &e" + score.get(temp).showStatic());
			} else if (i == 1) {
				getServer().dispatchCommand(getServer().getConsoleSender(),
						"broadcast &f" + temp + "&f : &e" + score.get(temp).showStatic());
			} else if (i == 2) {
				getServer().dispatchCommand(getServer().getConsoleSender(),
						"broadcast &c" + temp + "&f : &e" + score.get(temp).showStatic());
			} else {
				getServer().dispatchCommand(getServer().getConsoleSender(),
						"broadcast &7" + temp + "&f : &e" + score.get(temp).showStatic());
			}
			if (i >= 4) {
				break;
			}
			i++;

		}
	}
	private void showRankPlayer(CommandSender sender, int page) {
		Iterator it = sortByValue().iterator();
		
		ClanConfiguration clanConfiguration = new ClanConfiguration();
		int i = 0;
		i+=(page-1)*10;
		List<String> tmp = new ArrayList<>();
		
		while (it.hasNext()) {
			tmp.add((String) it.next());
		}
		sender.sendMessage(ChatColor.GOLD + "[HeavensPVP ���� 7 ����ǥ]");
		for(int j=i;j<i+10; j++) {
			if(tmp.size()<=j) {
				break;
			}
			String temp=tmp.get(j);
			if(page==1) {
				if (j == 0) {
					
					sender.sendMessage(ChatColor.GOLD + "1st) " + temp + ChatColor.WHITE + " <" + ChatColor.YELLOW
							+ (clanConfiguration.getClan(this.getServer().getPlayer(temp)) != null
									? clanConfiguration.getClan(this.getServer().getPlayer(temp))
									: "Ŭ�� ����")
							+ "> : " + ChatColor.GREEN + score.get(temp).showStatic());
				} else if (j == 1) {
					sender.sendMessage(ChatColor.WHITE + "2nd) " + temp + ChatColor.WHITE + " <" + ChatColor.YELLOW
							+ (clanConfiguration.getClan(this.getServer().getPlayer(temp)) != null
									? clanConfiguration.getClan(this.getServer().getPlayer(temp))
									: "Ŭ�� ����")
							+ "> : " + ChatColor.GREEN + score.get(temp).showStatic());
				} else if (j == 2) {
					sender.sendMessage(ChatColor.RED + "3rd) " + temp + ChatColor.WHITE + " <" + ChatColor.YELLOW
							+ (clanConfiguration.getClan(this.getServer().getPlayer(temp)) != null
									? clanConfiguration.getClan(this.getServer().getPlayer(temp))
									: "Ŭ�� ����")
							+ "> : " + ChatColor.GREEN + score.get(temp).showStatic());
				} else {
					sender.sendMessage(ChatColor.GRAY + String.valueOf(j + 1) + "th) " + temp + ChatColor.WHITE + " <"
							+ ChatColor.YELLOW
							+ (clanConfiguration.getClan(this.getServer().getPlayer(temp)) != null
									? clanConfiguration.getClan(this.getServer().getPlayer(temp))
									: "Ŭ�� ����")
							+ "> : " + ChatColor.GREEN + score.get(temp).showStatic());
				}
			}
			else {
				sender.sendMessage(ChatColor.GRAY + String.valueOf(j + 1) + "th) " + temp + ChatColor.WHITE + " <"
						+ ChatColor.YELLOW
						+ (clanConfiguration.getClan(this.getServer().getPlayer(temp)) != null
								? clanConfiguration.getClan(this.getServer().getPlayer(temp))
								: "Ŭ�� ����")
						+ "> : " + ChatColor.GREEN + score.get(temp).showStatic());
			}
		}
	}
	// ������ ����ƽ �Լ��� ����
	@EventHandler
	private void onInvenClicK(InventoryClickEvent e) {
		if(this.fighting==true && this.canBet==false) {
			if(e.getCurrentItem()!=null) {
				if (e.getCurrentItem().getTypeId() == 443) {
					System.out.println(e.getCurrentItem().getTypeId());
					String now = e.getWhoClicked().getName();
					if ((now.equals(PlayerA.getName()))) {
						e.getWhoClicked().teleport(new Location(e.getWhoClicked().getWorld(), -246.5, 73, 267.5));
						getServer().dispatchCommand(getServer().getConsoleSender(),
								"broadcast " + PlayerA.getName() + "���� ����Ͽ� " + PlayerB.getName() + "���� �̱�̽��ϴ�!");
						Result(PlayerB, PlayerA);
						
						PlayerA.sendMessage("���� ���� �ѳ����� ��� �ִ� ������ ���������Դϴ�!");
						PlayerB.sendTitle(ChatColor.GREEN + "�� ��", "");
						getServer().dispatchCommand(getServer().getConsoleSender(), "back " + PlayerB.getName());
						getServer().dispatchCommand(getServer().getConsoleSender(), "spawn " + PlayerB.getName());
						PlayerA = null;
						PlayerB = null;
						nowLoading = false;

					}

					else if ((now.equals(PlayerB.getName()))) {
						e.getWhoClicked().teleport(new Location(e.getWhoClicked().getWorld(), -246.5, 73, 267.5));
						getServer().dispatchCommand(getServer().getConsoleSender(),
								"broadcast " + PlayerB.getName() + "���� ����Ͽ� " + PlayerA.getName() + "���� �̱�̽��ϴ�!");
						Result(PlayerA, PlayerB);
						PlayerB.sendMessage("���� ���� �ѳ����� ��� �ִ� ������ ���������Դϴ�!");

						PlayerA.sendTitle(ChatColor.GREEN + "�� ��", "");

						getServer().dispatchCommand(getServer().getConsoleSender(), "back " + PlayerA.getName());
						getServer().dispatchCommand(getServer().getConsoleSender(), "spawn " + PlayerA.getName());
						PlayerA = null;
						PlayerB = null;
						nowLoading = false;
					}
				}
			}
		}
	}
	public static List sortByValue() {
		List<String> list = new ArrayList();
		list.addAll(score.keySet());
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				int v1 = score.get(o1).getElo();
				int v2 = score.get(o2).getElo();
				return v2-v1;
			}
		});
		//Collections.reverse(list); // �ּ��� ��������
		return list;
	}
}
