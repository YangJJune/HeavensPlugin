package HeavensCraft;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class hcStock extends JavaPlugin {

	private static final Logger log = Logger.getLogger("Minecraft");
	private static Economy econ = null;
	private static Permission perms = null;
	private static Chat chat = null;
	
	public static List<Stock> stocks = new ArrayList<>();
	public static List<String> stockNames = new ArrayList<>();
	public static HashMap<Stock,String> news = new HashMap<>();
	public static HashMap<UUID, Boolean> watch = new HashMap<>();
	public static HashMap<UUID,List<Integer>> StockCnt = new HashMap<>();
	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		Bukkit.getConsoleSender()
				.sendMessage(ChatColor.GOLD + ("hcEvent") + ChatColor.DARK_AQUA + " �÷����� ��Ȱ��ȭ" + ChatColor.BLACK);
		this.saveFile();
	}

	@Override
	public void onEnable() {
		// TODO Auto-generated method stub
		Bukkit.getConsoleSender()
				.sendMessage(ChatColor.GOLD + ("hcEvent") + ChatColor.AQUA + " �÷����� Ȱ��ȭ" + ChatColor.BLACK);

		if (!setupEconomy()) {
			log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
            	StockChangePrice();
            }
        }, 0L, 1200L);
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
            	StockChangeStatus();
            }
        }, 0L, 12000L);
		setupPermissions();
		setupChat();
		this.readFile();
		for(int i=0; i<stocks.size(); i++) {
			stockNames.add(stocks.get(i).getName());
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// TODO Auto-generated method stub

		if (sender instanceof Player) {
			if (command.getName().equals("�ֽ�")) {
				if(args.length>=1) {
					switch(args[0]) {
					case "����":
						if (args.length >= 3) {
							switch(args[1]) {
							case "�������":
								args[1] = "A";
								break;
							case "õ�������":
								args[1] = "B";
								break;
							case "������Ƽ":
								args[1] = "C";
								break;
							case "�������۴�":
								args[1] = "D";
								break;
							case "�̴��������۴�":
								args[1] = "E";
								break;
							case "�������۴�":
								args[1] = "F";
								break;
							case "������ֽ�ȸ��":
								args[1] = "G";
								break;
							case "����Ϸ�Ʈ�δн�":
								args[1] = "H";
								break;
							case "���":
								args[1] = "I";
								break;
							case "��������":
								args[1] = "J";
								break;
							}
							if (stockNames.contains(args[1])) {
								if (Integer.valueOf(args[2])<=0) {
									sender.sendMessage("0���Ϸδ� �ֽ��� �����Ͻ� �� �����ϴ�.");
								}
								else if (Integer.valueOf(args[2]) <= 100) {
									int index = stockNames.indexOf(args[1]);
									long price;
									price = stocks.get(index).getNow() * Integer.valueOf(args[2]);

									if (econ.getBalance((OfflinePlayer) sender) >= price) {
											if (this.StockCnt.containsKey(((Player) sender).getUniqueId())) {
												int past = StockCnt.get(((Player) sender).getUniqueId()).get(index);
												List<Integer> cnt = this.StockCnt.get(((Player) sender).getUniqueId());
												cnt.set(index, past + Integer.valueOf(args[2]));
												StockCnt.put(((Player) sender).getUniqueId(), cnt);
											} else {
												List<Integer> cnt = new ArrayList<>();
												for (int i = 0; i < stocks.size(); i++) {
													cnt.add(0);
												}
												cnt.set(index, Integer.valueOf(args[2]));
												StockCnt.put(((Player) sender).getUniqueId(), cnt);
											}
											econ.withdrawPlayer(((OfflinePlayer) sender), price);
											stocks.get(index)
													.setLeftTotal((int) (stocks.get(index).getLeftTotal() - price));
											sender.sendMessage(ChatColor.GREEN+"�ֽ��� �����Ͽ����ϴ�!");
									} else {
										sender.sendMessage("�ܾ��� �����մϴ�");
									}
								}
								else {
									sender.sendMessage("100�� �ʰ��Ͽ� �ŷ��Ͻ� �� �����ϴ�");
								}
							}
						}
						else {
							sender.sendMessage("�߸��� �Է��Դϴ�");
						}
						break;
					case "�Ǹ�":
						if(args.length>=3) {
							switch(args[1]) {
							case "�������":
								args[1] = "A";
								break;
							case "õ�������":
								args[1] = "B";
								break;
							case "������Ƽ":
								args[1] = "C";
								break;
							case "�������۴�":
								args[1] = "D";
								break;
							case "�̴��������۴�":
								args[1] = "E";
								break;
							case "�������۴�":
								args[1] = "F";
								break;
							case "������ֽ�ȸ��":
								args[1] = "G";
								break;
							case "����Ϸ�Ʈ�δн�":
								args[1] = "H";
								break;
							case "���":
								args[1] = "I";
								break;
							case "��������":
								args[1] = "J";
								break;
							}
							if(stockNames.contains(args[1])) {
								if(Integer.valueOf(args[2])<=100) {
									
								}
								else if(Integer.valueOf(args[2])<=100) {
									int index = stockNames.indexOf(args[1]);
									List<Integer> cnt = this.StockCnt.get(((Player) sender).getUniqueId());
									if(Integer.valueOf(args[2])<=cnt.get(index)) {
										long price;
										price = stocks.get(index).getNow()*Integer.valueOf(args[2]);
										econ.depositPlayer(((OfflinePlayer)sender), price*0.98);
										cnt.set(index, cnt.get(index)-Integer.valueOf(args[2]));
										stocks.get(index).setLeftTotal((int) (stocks.get(index).getTotal()+price));
										sender.sendMessage(ChatColor.GREEN+"�ֽ��� �Ǹ��Ͽ����ϴ�!");
									}					
									else {
										sender.sendMessage("�׸�ŭ�� �ֽ��� �����ϰ� ���� �ʽ��ϴ�");
									}
								}
								else {
									sender.sendMessage("100�� �ʰ��Ͽ� �ŷ��Ͻ� �� �����ϴ�");
								}
							}
							else {
								sender.sendMessage("�ֽ� �̸��� �߸� �ƽ��ϴ�.");
							}
						}
						else {
							sender.sendMessage(ChatColor.GOLD+" :: HCStock :: " +ChatColor.RED+"�߸��� �Է��Դϴ�");
						}
						break;
					case "����":
						for(Stock i:stocks) {
							sender.sendMessage(ChatColor.GOLD+" :: HCStock :: "+ChatColor.WHITE+this.news.get(i));
						}
						break;
					case "����":
					case "���":
						for(Stock i : stocks) {
							sender.sendMessage(i.toString());
						}
						break;
					case "������":
					case "Ȯ��":
						boolean chk = true;
						for(int i=0;i < StockCnt.get(((Player) sender).getUniqueId()).size(); i++) {
							String newname = null;
							switch(stockNames.get(i)) {
							case "A":
								newname = "�������";
								break;
							case "B":
								newname = "õ�������";
								break;
							case "C":
								newname = "������Ƽ";
								break;
							case "D":
								newname = "�������۴�";
								break;
							case "E":
								newname = "�̴��������۴�";
								break;
							case "F":
								newname = "�������۴�";
								break;
							case "G":
								newname = "������ֽ�ȸ��";
								break;
							case "H":
								newname = "����Ϸ�Ʈ�δн�";
								break;
							case "I":
								newname = "���";
								break;
							case "J":
								newname = "��������";
								break;
							}
							if(this.StockCnt.get(((Player) sender).getUniqueId()).get(i)!=0) {
								sender.sendMessage(ChatColor.GREEN+newname+ChatColor.WHITE+" : "+this.StockCnt.get(((Player) sender).getUniqueId()).get(i));
								chk=false;
							}
						}
						if(chk==true) {
							sender.sendMessage("�����س����� �ֽ��� �����ϴ�!");
						}
						
						break;
					case "�絵":
						if(args.length>=4) {
							
						}
						else {
							sender.sendMessage(ChatColor.GOLD+" :: HCStock :: " +ChatColor.RED+"�߸��� �Է��Դϴ�");
						}
						break;
					case "�˸��ѱ�":
						sender.sendMessage(ChatColor.GOLD+" :: HCStock :: " +ChatColor.GREEN+"�ֽ��� �˸��� �޽��ϴ�");
						this.watch.put(((Player) sender).getUniqueId(), true);
						break;
					case "�˸�����":
						sender.sendMessage(ChatColor.GOLD+" :: HCStock :: " +ChatColor.RED+"�ֽ��� �˸��� ���� �ʽ��ϴ�");
						this.watch.put(((Player) sender).getUniqueId(), false);
						break;
					case "����":
					default:
						sender.sendMessage(ChatColor.YELLOW+"============================================================");
						sender.sendMessage(ChatColor.YELLOW+"/�ֽ� ���� <�ֽ��̸�> <����> : "+ChatColor.AQUA+"�ֽ��� �����մϴ� (�ִ� 100����)");
						sender.sendMessage(ChatColor.YELLOW+"/�ֽ� �Ǹ� <�ֽ��̸�> <����> : "+ChatColor.AQUA+"�ֽ��� �Ǹ��մϴ� (�ִ� 100����)");
						sender.sendMessage(ChatColor.YELLOW+"/�ֽ� ��� : "+ChatColor.AQUA+"�ֽ��� ���� �ְ� �� ����� Ȯ���մϴ�");
						sender.sendMessage(ChatColor.YELLOW+"/�ֽ� Ȯ�� : "+ChatColor.AQUA+"�ڽ��� �ֽ� �������� Ȯ���մϴ�");
						sender.sendMessage(ChatColor.YELLOW+"/�ֽ� �˸��ѱ� : "+ChatColor.AQUA+"�ְ��� ���� �ɶ����� �˸��� �޽��ϴ�");
						sender.sendMessage(ChatColor.YELLOW+"/�ֽ� �˸����� : "+ChatColor.AQUA+"�ֽ��� ���� �ְ� �� ����� Ȯ���մϴ�");
						sender.sendMessage(ChatColor.YELLOW+"/�ֽ� ���� : "+ChatColor.AQUA+"�ֽ��� �ֽ� ���콺�� Ȯ���մϴ�");
						sender.sendMessage(ChatColor.YELLOW+"============================================================");
						break;
					}
				}
				else {
					sender.sendMessage(ChatColor.YELLOW+"============================================================");
					sender.sendMessage(ChatColor.YELLOW+"/�ֽ� ���� <�ֽ��̸�> <����> : "+ChatColor.AQUA+"�ֽ��� �����մϴ� (�ִ� 100����)");
					sender.sendMessage(ChatColor.YELLOW+"/�ֽ� �Ǹ� <�ֽ��̸�> <����> : "+ChatColor.AQUA+"�ֽ��� �Ǹ��մϴ� (�ִ� 100����)");
					sender.sendMessage(ChatColor.YELLOW+"/�ֽ� ��� : "+ChatColor.AQUA+"�ֽ��� ���� �ְ� �� ����� Ȯ���մϴ�");
					sender.sendMessage(ChatColor.YELLOW+"/�ֽ� Ȯ�� : "+ChatColor.AQUA+"�ڽ��� �ֽ� �������� Ȯ���մϴ�");
					sender.sendMessage(ChatColor.YELLOW+"/�ֽ� �˸��ѱ� : "+ChatColor.AQUA+"�ְ��� ���� �ɶ����� �˸��� �޽��ϴ�");
					sender.sendMessage(ChatColor.YELLOW+"/�ֽ� �˸����� : "+ChatColor.AQUA+"�ֽ��� ���� �ְ� �� ����� Ȯ���մϴ�");
					sender.sendMessage(ChatColor.YELLOW+"/�ֽ� ���� : "+ChatColor.AQUA+"�ֽ��� �ֽ� ���콺�� Ȯ���մϴ�");
					sender.sendMessage(ChatColor.YELLOW+"============================================================");
				}
			}
		}
		return false;
	}

	public void StockChangePrice() {
		for (Stock i : this.stocks) {
			if (!(i.isEnd())) {
				i.setPast(i.getNow());
				int now = i.getNow();

				Random rand = new Random();

				int percent = rand.nextInt(100);
				int change = rand.nextInt(i.getMax());
				switch (i.getStatus()) {
				case 0:
					if (percent < 90) {
						i.setNow(i.getNow() - change);
					} else {
						i.setNow(i.getNow() + change);
					}
					break;
				case 1:
					if (percent < 75) {
						i.setNow(i.getNow() - change);
					} else {
						i.setNow(i.getNow() + change);
					}
					break;
				case 2:
					if (percent < 50) {
						i.setNow(i.getNow() - change / 2);
					} else {
						i.setNow(i.getNow() + change / 2);
					}
					break;
				case 3:
					if (percent < 75) {
						i.setNow(i.getNow() + change);
					} else {
						i.setNow(i.getNow() - change);
					}
					break;
				case 4:
					if (percent < 90) {
						i.setNow(i.getNow() + change);
					} else {
						i.setNow(i.getNow() - change);
					}
					break;
				}
			}
			if (i.getNow() <= 0) {
				i.setEnd(true);
				i.setNow(0);
			}
		}
		for(Player p : this.getServer().getOnlinePlayers()) {
			if(this.watch.containsKey(p.getUniqueId())) {
				if(watch.get(p.getUniqueId())) {
					for(Stock i : stocks) {
						p.sendMessage(i.toString());
					}
					p.sendMessage(ChatColor.GOLD+" :: HCStock :: " +ChatColor.WHITE+"Ȥ�� �˸����� ä��â�� �ʹ� ���������ٸ�?"+ChatColor.LIGHT_PURPLE+" /�ֽ� �˸�����");
				}
				else {
					p.sendMessage(ChatColor.GOLD+" :: HCStock :: " +ChatColor.GREEN+"�ֽ�"+ChatColor.YELLOW+" ���� ���� �Ϸ�!"+ChatColor.WHITE+" �Ź� �ڼ��� ������ �˰� �ʹٸ�?"+ChatColor.LIGHT_PURPLE+" /�ֽ� �˸��ѱ�");
				}
			}
			else {
				watch.put(p.getUniqueId(), false);
				p.sendMessage(ChatColor.GOLD+" :: HCStock :: " +ChatColor.GREEN+"�ֽ�"+ChatColor.YELLOW+" ���� ���� �Ϸ�!"+ChatColor.WHITE+" �Ź� �ڼ��� ������ �˰� �ʹٸ�?"+ChatColor.LIGHT_PURPLE+" /�ֽ� �˸��ѱ�");	
			}
		}
	}
	public void StockChangeStatus() {
		for (Player p : this.getServer().getOnlinePlayers()) {
			if (watch.get(p.getUniqueId())) {
				p.sendMessage(ChatColor.GOLD+" :: HCStock :: " +ChatColor.GREEN+"�ֽ�"+ChatColor.YELLOW+"�� ���ο� ������ �߰��Ǿ����ϴ�!");
			}
		}
		Random rand = new Random();
		for(Stock i : this.stocks) {
			int s = rand.nextInt(5);
			i.setStatus(s);
			int tmp;
			switch(s) {
			case 0:
				tmp=rand.nextInt(3);
				switch(tmp) {
				case 0:
					this.news.put(i, ChatColor.YELLOW+i.getRealName()+ChatColor.RED+"��(��) ȸ���� ���˷� ���ӵ� �����Դϴ�!");
					break;
				case 1:
					this.news.put(i, ChatColor.YELLOW+i.getRealName()+ChatColor.RED+"��(��) ��ġ ����Ʈ�� ����Ǿ����ϴ�!");
					break;
				case 2:
					this.news.put(i, ChatColor.YELLOW+i.getRealName()+ChatColor.RED+"��(��) ���� ���̿� �ɾҽ��ϴ�!");
					break;
				}
				break;
			case 1:
				tmp=rand.nextInt(4);
				switch(tmp) {
				case 0:
					this.news.put(i, ChatColor.YELLOW+i.getRealName()+ChatColor.RED+"��(��) ���ڸ� �����ϴ� ������ ħ���Ͽ� ū ���ظ� ���� �����Դϴ�!");
					break;
				case 1:
					this.news.put(i, ChatColor.YELLOW+i.getRealName()+ChatColor.RED+"��(��) ������ �ľ��ϸ� ������ �䱸�ؿ��� �ֽ��ϴ�!");
					break;
				case 2:
					this.news.put(i, ChatColor.YELLOW+i.getRealName()+ChatColor.RED+"��(��) ���ΰ� Ⱦ���� �Ͽ����ϴ�!");
					break;
				case 3:
					this.news.put(i, ChatColor.YELLOW+i.getRealName()+ChatColor.RED+"��(��) ȸ��ǹ����� ȭ�簡 �����ϴ�!");
					break;
				}
				break;
			case 2:
				this.news.put(i, ChatColor.YELLOW+i.getRealName()+ChatColor.WHITE+"��(��) ���� ���� �����մϴ�!");
				break;
			case 3:
				tmp=rand.nextInt(3);
				switch(tmp) {
				case 0:
					this.news.put(i, ChatColor.YELLOW+i.getRealName()+ChatColor.GREEN+"��(��) �ؿܿ� ��� ü�ῡ �����Ͽ����ϴ�!");
					break;
				case 1:
					this.news.put(i, ChatColor.YELLOW+i.getRealName()+ChatColor.GREEN+"��(��) ������ ������ ��ġ�� ����ȭ �ܰ迡 �ֽ��ϴ�!");
					break;
				case 2:
					this.news.put(i, ChatColor.YELLOW+i.getRealName()+ChatColor.GREEN+"��(��) ���ο� ������ �����Ͽ����ϴ�!");
					break;
				case 3:
					this.news.put(i, ChatColor.YELLOW+i.getRealName()+ChatColor.GREEN+"��(��) ���ο��� �������� �����Ͽ����ϴ�!");
					break;
				}
				break;
			case 4:
				
				this.news.put(i, ChatColor.YELLOW+i.getRealName()+ChatColor.GREEN+"�� ����ǰ�� ����� �����ϴ�!");
				if (i.getNow() != 0) {
					String newname=null;
					switch(i.getName()) {
					case "A":
						newname = "�������";
						break;
					case "B":
						newname = "õ�������";
						break;
					case "C":
						newname = "������Ƽ";
						break;
					case "D":
						newname = "�������۴�";
						break;
					case "E":
						newname = "�̴��������۴�";
						break;
					case "F":
						newname = "�������۴�";
						break;
					case "G":
						newname = "������ֽ�ȸ��";
						break;
					case "H":
						newname = "����Ϸ�Ʈ�δн�";
						break;
					case "I":
						newname = "���";
						break;
					case "J":
						newname = "��������";
						break;
					}
					for (Player p : this.getServer().getOnlinePlayers()) {
						p.sendMessage(ChatColor.GOLD + " :: HCStock :: " + ChatColor.GREEN + newname
								+ ChatColor.YELLOW + "��(��) ����� �� ������ ���Դϴ�!");
					}
				}
				break;
			}
			
		}		
		this.saveFile();
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
	
	private void readFile() {
		try {
			//File file = new File("/mnt/disks/sdb/����/plugins/hcStock/stock.txt");
			File file = new File("C:\\�������\\plugins\\hcStock\\stock.txt");
			Scanner scan = new Scanner(file);
			int N = scan.nextInt();
			for(int i=0; i<N; i++) {
				String name = scan.next();
				int now = scan.nextInt();
				int past = scan.nextInt();
				int status = scan.nextInt();
				int max = scan.nextInt();
				int total = scan.nextInt();
				int left = scan.nextInt();
				boolean e = scan.nextBoolean();
				stocks.add(new Stock(name, now, past,status,max,total,left,e));
			}			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			File file = new File("C:\\�������\\plugins\\hcStock\\player.txt");
			Scanner scan = new Scanner(file);
			int N = scan.nextInt();
			for(int i=0; i<N; i++) {
				String ID;
				ID = scan.next();
				List<Integer> cnt = new ArrayList<>();
				cnt.clear();
				for(int j=0; j<stocks.size(); j++) {
					int k;
					k = scan.nextInt();
					cnt.add(k);				
				}
				this.StockCnt.put(UUID.fromString(ID), cnt);
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
        	File file = new File("C:\\�������\\plugins\\hcStock\\stock.txt");
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            int size = stocks.size();
            
            if(file.isFile() && file.canWrite()){
            	bufferedWriter.write(String.valueOf(size));
    				for (Stock i:this.stocks) {
                		//���๮�ھ���
                		bufferedWriter.newLine();
                		bufferedWriter.write(i.getName());
    					String str = " "+i.getNow()+" "+i.getPast()+" "+i.getStatus()+" "+i.getMax()+" "+i.getTotal()+" "+i.getLeftTotal()+" "+i.isEnd();
    					bufferedWriter.write(str);
    				}
                bufferedWriter.close();
            }
        }catch (IOException e) {
            System.out.println(e);
        }
        try{
            //���� ��ü ����
        	File file = new File("C:\\�������\\plugins\\hcStock\\player.txt");
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            int size = this.StockCnt.size();
            
            if(file.isFile() && file.canWrite()){
            	bufferedWriter.write(String.valueOf(size));
                	
                	Set<Entry<UUID, List<Integer>>> entries = this.StockCnt.entrySet();
    				for (Entry<UUID, List<Integer>> entry : entries) {
                		//���๮�ھ���
                		bufferedWriter.newLine();
                		bufferedWriter.write(entry.getKey().toString());
                		String str = " ";
    					for(int i=0; i<entry.getValue().size(); i++) {
    						str += entry.getValue().get(i).toString()+" ";
    					}
    					bufferedWriter.write(str);
    				}
                bufferedWriter.close();
            }
        }catch (IOException e) {
            System.out.println(e);
        }

	}
}
