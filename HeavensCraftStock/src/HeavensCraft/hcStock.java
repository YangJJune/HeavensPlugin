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
				.sendMessage(ChatColor.GOLD + ("hcEvent") + ChatColor.DARK_AQUA + " 플러그인 비활성화" + ChatColor.BLACK);
		this.saveFile();
	}

	@Override
	public void onEnable() {
		// TODO Auto-generated method stub
		Bukkit.getConsoleSender()
				.sendMessage(ChatColor.GOLD + ("hcEvent") + ChatColor.AQUA + " 플러그인 활성화" + ChatColor.BLACK);

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
			if (command.getName().equals("주식")) {
				if(args.length>=1) {
					switch(args[0]) {
					case "구매":
						if (args.length >= 3) {
							switch(args[1]) {
							case "삼송전자":
								args[1] = "A";
								break;
							case "천국여행사":
								args[1] = "B";
								break;
							case "강원시티":
								args[1] = "C";
								break;
							case "리플컴퍼니":
								args[1] = "D";
								break;
							case "이더리움컴퍼니":
								args[1] = "E";
								break;
							case "어드민컴퍼니":
								args[1] = "F";
								break;
							case "헤븐즈주식회사":
								args[1] = "G";
								break;
							case "사과일렉트로닉스":
								args[1] = "H";
								break;
							case "고글":
								args[1] = "I";
								break;
							case "양쭌전자":
								args[1] = "J";
								break;
							}
							if (stockNames.contains(args[1])) {
								if (Integer.valueOf(args[2])<=0) {
									sender.sendMessage("0이하로는 주식을 구매하실 수 없습니다.");
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
											sender.sendMessage(ChatColor.GREEN+"주식을 구매하였습니다!");
									} else {
										sender.sendMessage("잔액이 부족합니다");
									}
								}
								else {
									sender.sendMessage("100을 초과하여 거래하실 수 없습니다");
								}
							}
						}
						else {
							sender.sendMessage("잘못된 입력입니다");
						}
						break;
					case "판매":
						if(args.length>=3) {
							switch(args[1]) {
							case "삼송전자":
								args[1] = "A";
								break;
							case "천국여행사":
								args[1] = "B";
								break;
							case "강원시티":
								args[1] = "C";
								break;
							case "리플컴퍼니":
								args[1] = "D";
								break;
							case "이더리움컴퍼니":
								args[1] = "E";
								break;
							case "어드민컴퍼니":
								args[1] = "F";
								break;
							case "헤븐즈주식회사":
								args[1] = "G";
								break;
							case "사과일렉트로닉스":
								args[1] = "H";
								break;
							case "고글":
								args[1] = "I";
								break;
							case "양쭌전자":
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
										sender.sendMessage(ChatColor.GREEN+"주식을 판매하였습니다!");
									}					
									else {
										sender.sendMessage("그만큼의 주식을 보유하고 있지 않습니다");
									}
								}
								else {
									sender.sendMessage("100을 초과하여 거래하실 수 없습니다");
								}
							}
							else {
								sender.sendMessage("주식 이름이 잘못 됐습니다.");
							}
						}
						else {
							sender.sendMessage(ChatColor.GOLD+" :: HCStock :: " +ChatColor.RED+"잘못된 입력입니다");
						}
						break;
					case "뉴스":
						for(Stock i:stocks) {
							sender.sendMessage(ChatColor.GOLD+" :: HCStock :: "+ChatColor.WHITE+this.news.get(i));
						}
						break;
					case "정보":
					case "목록":
						for(Stock i : stocks) {
							sender.sendMessage(i.toString());
						}
						break;
					case "보유량":
					case "확인":
						boolean chk = true;
						for(int i=0;i < StockCnt.get(((Player) sender).getUniqueId()).size(); i++) {
							String newname = null;
							switch(stockNames.get(i)) {
							case "A":
								newname = "삼송전자";
								break;
							case "B":
								newname = "천국여행사";
								break;
							case "C":
								newname = "강원시티";
								break;
							case "D":
								newname = "리플컴퍼니";
								break;
							case "E":
								newname = "이더리움컴퍼니";
								break;
							case "F":
								newname = "어드민컴퍼니";
								break;
							case "G":
								newname = "헤븐즈주식회사";
								break;
							case "H":
								newname = "사과일렉트로닉스";
								break;
							case "I":
								newname = "고글";
								break;
							case "J":
								newname = "양쭌전자";
								break;
							}
							if(this.StockCnt.get(((Player) sender).getUniqueId()).get(i)!=0) {
								sender.sendMessage(ChatColor.GREEN+newname+ChatColor.WHITE+" : "+this.StockCnt.get(((Player) sender).getUniqueId()).get(i));
								chk=false;
							}
						}
						if(chk==true) {
							sender.sendMessage("구매해놓으신 주식이 없습니다!");
						}
						
						break;
					case "양도":
						if(args.length>=4) {
							
						}
						else {
							sender.sendMessage(ChatColor.GOLD+" :: HCStock :: " +ChatColor.RED+"잘못된 입력입니다");
						}
						break;
					case "알림켜기":
						sender.sendMessage(ChatColor.GOLD+" :: HCStock :: " +ChatColor.GREEN+"주식의 알림을 받습니다");
						this.watch.put(((Player) sender).getUniqueId(), true);
						break;
					case "알림끄기":
						sender.sendMessage(ChatColor.GOLD+" :: HCStock :: " +ChatColor.RED+"주식의 알림을 받지 않습니다");
						this.watch.put(((Player) sender).getUniqueId(), false);
						break;
					case "도움말":
					default:
						sender.sendMessage(ChatColor.YELLOW+"============================================================");
						sender.sendMessage(ChatColor.YELLOW+"/주식 구매 <주식이름> <수량> : "+ChatColor.AQUA+"주식을 구매합니다 (최대 100개씩)");
						sender.sendMessage(ChatColor.YELLOW+"/주식 판매 <주식이름> <수량> : "+ChatColor.AQUA+"주식을 판매합니다 (최대 100개씩)");
						sender.sendMessage(ChatColor.YELLOW+"/주식 목록 : "+ChatColor.AQUA+"주식의 현재 주가 및 목록을 확인합니다");
						sender.sendMessage(ChatColor.YELLOW+"/주식 확인 : "+ChatColor.AQUA+"자신의 주식 보유량을 확인합니다");
						sender.sendMessage(ChatColor.YELLOW+"/주식 알림켜기 : "+ChatColor.AQUA+"주가가 갱신 될때마다 알림을 받습니다");
						sender.sendMessage(ChatColor.YELLOW+"/주식 알림끄기 : "+ChatColor.AQUA+"주식의 현재 주가 및 목록을 확인합니다");
						sender.sendMessage(ChatColor.YELLOW+"/주식 뉴스 : "+ChatColor.AQUA+"주식의 최신 뉴우스를 확인합니다");
						sender.sendMessage(ChatColor.YELLOW+"============================================================");
						break;
					}
				}
				else {
					sender.sendMessage(ChatColor.YELLOW+"============================================================");
					sender.sendMessage(ChatColor.YELLOW+"/주식 구매 <주식이름> <수량> : "+ChatColor.AQUA+"주식을 구매합니다 (최대 100개씩)");
					sender.sendMessage(ChatColor.YELLOW+"/주식 판매 <주식이름> <수량> : "+ChatColor.AQUA+"주식을 판매합니다 (최대 100개씩)");
					sender.sendMessage(ChatColor.YELLOW+"/주식 목록 : "+ChatColor.AQUA+"주식의 현재 주가 및 목록을 확인합니다");
					sender.sendMessage(ChatColor.YELLOW+"/주식 확인 : "+ChatColor.AQUA+"자신의 주식 보유량을 확인합니다");
					sender.sendMessage(ChatColor.YELLOW+"/주식 알림켜기 : "+ChatColor.AQUA+"주가가 갱신 될때마다 알림을 받습니다");
					sender.sendMessage(ChatColor.YELLOW+"/주식 알림끄기 : "+ChatColor.AQUA+"주식의 현재 주가 및 목록을 확인합니다");
					sender.sendMessage(ChatColor.YELLOW+"/주식 뉴스 : "+ChatColor.AQUA+"주식의 최신 뉴우스를 확인합니다");
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
					p.sendMessage(ChatColor.GOLD+" :: HCStock :: " +ChatColor.WHITE+"혹시 알림으로 채팅창이 너무 복잡해졌다면?"+ChatColor.LIGHT_PURPLE+" /주식 알림끄기");
				}
				else {
					p.sendMessage(ChatColor.GOLD+" :: HCStock :: " +ChatColor.GREEN+"주식"+ChatColor.YELLOW+" 가격 갱신 완료!"+ChatColor.WHITE+" 매번 자세한 내용을 알고 싶다면?"+ChatColor.LIGHT_PURPLE+" /주식 알림켜기");
				}
			}
			else {
				watch.put(p.getUniqueId(), false);
				p.sendMessage(ChatColor.GOLD+" :: HCStock :: " +ChatColor.GREEN+"주식"+ChatColor.YELLOW+" 가격 갱신 완료!"+ChatColor.WHITE+" 매번 자세한 내용을 알고 싶다면?"+ChatColor.LIGHT_PURPLE+" /주식 알림켜기");	
			}
		}
	}
	public void StockChangeStatus() {
		for (Player p : this.getServer().getOnlinePlayers()) {
			if (watch.get(p.getUniqueId())) {
				p.sendMessage(ChatColor.GOLD+" :: HCStock :: " +ChatColor.GREEN+"주식"+ChatColor.YELLOW+"에 새로운 뉴스가 추가되었습니다!");
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
					this.news.put(i, ChatColor.YELLOW+i.getRealName()+ChatColor.RED+"은(는) 회장이 범죄로 구속된 상태입니다!");
					break;
				case 1:
					this.news.put(i, ChatColor.YELLOW+i.getRealName()+ChatColor.RED+"은(는) 정치 게이트에 연루되었습니다!");
					break;
				case 2:
					this.news.put(i, ChatColor.YELLOW+i.getRealName()+ChatColor.RED+"은(는) 적자 더미에 앉았습니다!");
					break;
				}
				break;
			case 1:
				tmp=rand.nextInt(4);
				switch(tmp) {
				case 0:
					this.news.put(i, ChatColor.YELLOW+i.getRealName()+ChatColor.RED+"은(는) 물자를 수송하던 선박이 침몰하여 큰 피해를 입은 상태입니다!");
					break;
				case 1:
					this.news.put(i, ChatColor.YELLOW+i.getRealName()+ChatColor.RED+"은(는) 노조가 파업하며 협상을 요구해오고 있습니다!");
					break;
				case 2:
					this.news.put(i, ChatColor.YELLOW+i.getRealName()+ChatColor.RED+"은(는) 간부가 횡령을 하였습니다!");
					break;
				case 3:
					this.news.put(i, ChatColor.YELLOW+i.getRealName()+ChatColor.RED+"은(는) 회사건물에서 화재가 났습니다!");
					break;
				}
				break;
			case 2:
				this.news.put(i, ChatColor.YELLOW+i.getRealName()+ChatColor.WHITE+"은(는) 별일 없이 조용합니다!");
				break;
			case 3:
				tmp=rand.nextInt(3);
				switch(tmp) {
				case 0:
					this.news.put(i, ChatColor.YELLOW+i.getRealName()+ChatColor.GREEN+"은(는) 해외와 계약 체결에 성공하였습니다!");
					break;
				case 1:
					this.news.put(i, ChatColor.YELLOW+i.getRealName()+ChatColor.GREEN+"은(는) 노조와 협상을 마치고 정상화 단계에 있습니다!");
					break;
				case 2:
					this.news.put(i, ChatColor.YELLOW+i.getRealName()+ChatColor.GREEN+"은(는) 새로운 연구에 성공하였습니다!");
					break;
				case 3:
					this.news.put(i, ChatColor.YELLOW+i.getRealName()+ChatColor.GREEN+"은(는) 정부에서 지원금을 수령하였습니다!");
					break;
				}
				break;
			case 4:
				
				this.news.put(i, ChatColor.YELLOW+i.getRealName()+ChatColor.GREEN+"의 신제품이 대박이 났습니다!");
				if (i.getNow() != 0) {
					String newname=null;
					switch(i.getName()) {
					case "A":
						newname = "삼송전자";
						break;
					case "B":
						newname = "천국여행사";
						break;
					case "C":
						newname = "강원시티";
						break;
					case "D":
						newname = "리플컴퍼니";
						break;
					case "E":
						newname = "이더리움컴퍼니";
						break;
					case "F":
						newname = "어드민컴퍼니";
						break;
					case "G":
						newname = "헤븐즈주식회사";
						break;
					case "H":
						newname = "사과일렉트로닉스";
						break;
					case "I":
						newname = "고글";
						break;
					case "J":
						newname = "양쭌전자";
						break;
					}
					for (Player p : this.getServer().getOnlinePlayers()) {
						p.sendMessage(ChatColor.GOLD + " :: HCStock :: " + ChatColor.GREEN + newname
								+ ChatColor.YELLOW + "이(가) 대박이 날 조짐이 보입니다!");
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
			//File file = new File("/mnt/disks/sdb/서버/plugins/hcStock/stock.txt");
			File file = new File("C:\\백업서버\\plugins\\hcStock\\stock.txt");
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
			File file = new File("C:\\백업서버\\plugins\\hcStock\\player.txt");
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
            //파일 객체 생성
        	File file = new File("C:\\백업서버\\plugins\\hcStock\\stock.txt");
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            int size = stocks.size();
            
            if(file.isFile() && file.canWrite()){
            	bufferedWriter.write(String.valueOf(size));
    				for (Stock i:this.stocks) {
                		//개행문자쓰기
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
            //파일 객체 생성
        	File file = new File("C:\\백업서버\\plugins\\hcStock\\player.txt");
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            int size = this.StockCnt.size();
            
            if(file.isFile() && file.canWrite()){
            	bufferedWriter.write(String.valueOf(size));
                	
                	Set<Entry<UUID, List<Integer>>> entries = this.StockCnt.entrySet();
    				for (Entry<UUID, List<Integer>> entry : entries) {
                		//개행문자쓰기
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
