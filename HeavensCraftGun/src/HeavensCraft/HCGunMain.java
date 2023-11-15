package HeavensCraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.permission.Permission;

public class HCGunMain extends JavaPlugin implements Listener {
	private static final Logger log = Logger.getLogger("Minecraft");
	private static Economy econ = null;
	private static Permission perms = null;
	private static Chat chat = null;
	private static HashMap<Player, Boolean> OnRepair = new HashMap<>();

	@Override
	public void onEnable() {
		Bukkit.getConsoleSender()
				.sendMessage(ChatColor.GOLD + ("HCGun") + ChatColor.AQUA + " 플러그인 활성화" + ChatColor.BLACK);
		Bukkit.getPluginManager().registerEvents(this, this);
		if (!setupEconomy()) {
			log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		setupPermissions();
		setupChat();
	}

	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		log.info(String.format("[%s] Disabled Version %s", getDescription().getName(), getDescription().getVersion()));
		Bukkit.getConsoleSender()
				.sendMessage(ChatColor.GOLD + ("HCGun") + ChatColor.DARK_AQUA + " 플러그인 비활성화" + ChatColor.BLACK);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// TODO Auto-generated method stub
		if (sender instanceof Player) {
			switch (cmd.getName()) {
			case "칭호추가신청":
				if(args.length>=1) {
					String str=args[0];
					for(int i=1; i<args.length; i++) {
						str+=args[i];
					}
					((Player) sender).chat("/mail send yangjjune [칭호추가] "+str);
				}
				break;
			case "디스코드":
			case "디코":
				sender.sendMessage(ChatColor.DARK_AQUA+"https://discord.gg/Cqv9JEG");
				break;
			case "마인리스트":
			case "출석체크":
			case "출첵":
				sender.sendMessage(ChatColor.DARK_AQUA+"https://minelist.kr/servers/8492/votes/new"+ ChatColor.YELLOW+"이 링크로 들어가셔서 추천을 누르시면 됩니다!");
				break;
			case "총기상점":
				HCGOpenGUI gui = new HCGOpenGUI();
				gui.open((Player) sender);
				break;
			case "도구수리":
				ItemStack tool = ((Player) sender).getInventory().getItemInMainHand();
				if (tool != null) {
					int cost = 5000 + tool.getDurability() * 10;
					if (OnRepair.get(sender) == null) {
						sender.sendMessage(ChatColor.YELLOW+"아이템 "+ tool.getData().getItemType()+"은(는) "+ChatColor.GREEN+"$ " + cost + ChatColor.YELLOW+"만큼의 비용이 필요합니다. 계속하시려면"+ChatColor.AQUA+ " /도구수리"+ChatColor.YELLOW +"를 입력해주세요.");
						OnRepair.put((Player) sender, true);
					} else if (OnRepair.get(sender) == false) {
						sender.sendMessage(ChatColor.YELLOW+"아이템 "+ tool.getData().getItemType()+"은(는) "+ChatColor.GREEN+"$ " + cost + ChatColor.YELLOW+"만큼의 비용이 필요합니다. 계속하시려면"+ChatColor.AQUA+ " /도구수리"+ChatColor.YELLOW +"를 입력해주세요.");
						OnRepair.replace((Player) sender, true);
					} else if (OnRepair.get(sender) == true) {
						if (econ.getBalance((OfflinePlayer) sender) >= cost) {
							sender.sendMessage(ChatColor.GREEN+"$ " + cost + ChatColor.GRAY+"로 도구의 수리를 완료하였습니다");
							tool.setDurability((short) 0);
							econ.withdrawPlayer((OfflinePlayer) sender, cost);
							OnRepair.replace((Player) sender, false);
						} else {
							sender.sendMessage("잔액이 부족합니다. 비용은 $ " + cost + " 입니다");
							OnRepair.replace((Player) sender, false);
						}
					}
				}
				break;
			case "블럭보호":
				if(args.length==0) {
					HelpBlockProtect(sender);
				}
				else {
					switch(args[0]) {
					case "전체해제":
						((Player) sender).performCommand("abandonallclaims");
						break;
						
					case "친구추가":
						if(args.length>=2) {
							((Player) sender).performCommand("trust "+args[1]);
						}
						else {
							sender.sendMessage("친구로 추가할 이름을 입력해주세요.\n명령어 : /블럭보호 친구추가 <이름>");
						}
						break;
						
					case "친구삭제":
						if(args.length>=2) {
							((Player) sender).performCommand("untrust "+args[1]);
						}
						else {
							sender.sendMessage("친구에서 삭제할 이름을 입력해주세요.\n명령어 : /블럭보호 친구추가 <이름>");
						}
						break;
						
					case "목록":
						((Player) sender).performCommand("claimlist");
						break;
						
					case "추가구매":
						if(args.length>=2) {
							((Player) sender).performCommand("buyclaimblocks "+args[1]);
						}
						else {
							sender.sendMessage("구매할 수량을 입력해주세요.\n"+ChatColor.AQUA+"명령어 : /블럭보호 추가구매 <수량>");
						}
						break;
						
					case "해제":
						((Player) sender).performCommand("abandonclaim");
						break;
						
					case "도움말":
						HelpBlockProtect(sender);
						break;
						
					default:
						HelpBlockProtect(sender);
						break;	
					}
					
				}
				break;
				
			case "벼룩시장":
				if(args.length==0) {
					((Player) sender).performCommand("ah");
				}
				else {
					switch(args[0]) {
					case "도움말":
						HelpAuction(sender);
						break;
					case "팔기":
						if(args.length>=2)
							((Player) sender).performCommand("ah sell "+args[1]);
						else
							sender.sendMessage("판매할 가격을 입력해주세요\n"+ChatColor.AQUA+"명령어 : /벼룩시장 팔기 <가격>");
						break;
					default:
						HelpAuction(sender);
						break;	
					}
					
				}
				break;
			case "돈":
				if(args.length==0) {
					((Player) sender).performCommand("money");
				}
				else {
					switch(args[0]) {
					case "확인":
						((Player) sender).performCommand("money");
						break;
					case "순위":
						((Player) sender).performCommand("balancetop");
						break;
					case "보내기":
						if(args.length>=3) {
							((Player) sender).performCommand("pay "+args[1]+ " "+args[2]);
						}
						else {
							sender.sendMessage("잘못된 입력입니다.\n"+ChatColor.AQUA+"명령어 : /돈 보내기 <받을사람> <금액>");
						}
						break;
					default:
						EconomyHelp(sender);
						break;	
					}					
				}
				break;
			case "집":
				if (args.length == 0) {
					((Player) sender).performCommand("home");
				} 
				else {
					if (args[0].equals("설정")) {
						((Player) sender).performCommand("sethome");
					}
					else {
						sender.sendMessage("알 수 없는 명령어입니다. 도움말을 확인해주세요"+ChatColor.AQUA+"명령어 : /도움말");
					}
				}
				break;
			case "도움말":
				HelpAuction(sender);
				HelpBlockProtect(sender);
				this.helpPVP(sender);
				EconomyHelp(sender);
				this.etcProtect(sender);
				break;
			case "귀환":
				((Player) sender).performCommand("back");
				break;
			}

		}
		return true;
	}
	private void EconomyHelp(CommandSender sender) {
		sender.sendMessage(ChatColor.YELLOW+"============================================================");
		sender.sendMessage(ChatColor.YELLOW+"/돈 : "+ChatColor.AQUA+"자신의 돈을 확인합니다");
		sender.sendMessage(ChatColor.YELLOW+"/돈 확인 : "+ChatColor.AQUA+"자신의 돈을 확인합니다");
		sender.sendMessage(ChatColor.YELLOW+"/돈 순위 : "+ChatColor.AQUA+"현재 서버내 유저들의 재산 순위를 확인합니다");
		sender.sendMessage(ChatColor.YELLOW+"/돈 보내기 <보낼 사람> <보낼 수량>: "+ChatColor.AQUA+"유저에게 돈을 보냅니다");
		//sender.sendMessage(ChatColor.YELLOW+"============================================================");
	}

	private void HelpAuction(CommandSender sender) {
		// TODO Auto-generated method stub
		sender.sendMessage(ChatColor.YELLOW+"============================================================");
		sender.sendMessage(ChatColor.YELLOW+"/벼룩시장 : "+ChatColor.AQUA+"벼룩시장 창을 엽니다");
		sender.sendMessage(ChatColor.YELLOW+"/벼룩시장 팔기 <가격> : "+ChatColor.AQUA+"손에 들고 있는 아이템을 벼룩시장에 팝니다");
		//sender.sendMessage(ChatColor.YELLOW+"============================================================");
	}

	private void HelpBlockProtect(CommandSender sender) {
		sender.sendMessage(ChatColor.YELLOW+"============================================================");
		sender.sendMessage(ChatColor.YELLOW+"/블럭보호 전체해제 : "+ChatColor.AQUA+"자신의 모든 블럭보호구역을 해제합니다");
		sender.sendMessage(ChatColor.YELLOW+"/블럭보호 친구추가 <이름> : "+ChatColor.AQUA+"자신의 구역에 친구를 추가합니다");
		sender.sendMessage(ChatColor.YELLOW+"/블럭보호 친구삭제 <이름> : "+ChatColor.AQUA+"자신의 구역에 친구를 삭제합니다");
		sender.sendMessage(ChatColor.YELLOW+"/블럭보호 목록 : "+ChatColor.AQUA+"자신의 모든 블럭보호구역을 확인합니다");
		sender.sendMessage(ChatColor.YELLOW+"/블럭보호 추가구매 : "+ChatColor.AQUA+"보호 가능한 블럭의 수를 더 늘립니다 (1블럭당 = $2000)");
		sender.sendMessage(ChatColor.YELLOW+"/블럭보호 해제 : "+ChatColor.AQUA+"자신이 서있는 위치의 블럭보호구역을 해제합니다");
		sender.sendMessage(ChatColor.YELLOW+"/블럭보호 도움말 : "+ChatColor.AQUA+"블럭보호 관련 도움말을 출력합니다");
		//sender.sendMessage(ChatColor.YELLOW+"============================================================");
	}
	private void etcProtect(CommandSender sender) {
		sender.sendMessage(ChatColor.YELLOW+"============================================================");
		sender.sendMessage(ChatColor.YELLOW+"/집 : "+ChatColor.AQUA+"집으로 이동합니다");
		sender.sendMessage(ChatColor.YELLOW+"/집 설정 : "+ChatColor.AQUA+"집을 설정합니다");
		sender.sendMessage(ChatColor.YELLOW+"/워프: "+ChatColor.AQUA+"월드 워프 GUI를 엽니다");
		sender.sendMessage(ChatColor.YELLOW+"/스폰 : "+ChatColor.AQUA+"스폰구역으로 이동합니다");
		sender.sendMessage(ChatColor.YELLOW+"/야생 : "+ChatColor.AQUA+"랜덤 텔레포트를 하여 야생구역으로 이동합니다");
		sender.sendMessage(ChatColor.YELLOW+"/총기상점 : "+ChatColor.AQUA+"총기상점을 엽니다");
		sender.sendMessage(ChatColor.YELLOW+"/도구수리 : "+ChatColor.AQUA+"도구를 수리합니다");
		sender.sendMessage(ChatColor.YELLOW+"/주식: "+ChatColor.AQUA+"주식 관련 명령어를 확인합니다.");
		sender.sendMessage(ChatColor.YELLOW+"/발렌타인 상점 : "+ChatColor.AQUA+"발렌타인 상점을 엽니다");
		sender.sendMessage(ChatColor.YELLOW+"/발렌타인 교환 : "+ChatColor.AQUA+"발렌타인 코인을 교환합니다");
		sender.sendMessage(ChatColor.YELLOW+"============================================================");
		sender.sendMessage(ChatColor.YELLOW+"/능력자카드 뽑기 : "+ChatColor.AQUA+"능력자카드 뽑기를 1회 진행합니다 (10만원)");
		sender.sendMessage(ChatColor.YELLOW+"/능력자카드 연차뽑기 : "+ChatColor.AQUA+"능력자카드 뽑기를 10회 진행합니다 (90만원)");
		sender.sendMessage(ChatColor.YELLOW+"/pvp난투: "+ChatColor.AQUA+"pvp 난투에 참가합니다");
		sender.sendMessage(ChatColor.YELLOW+"============================================================");
	}
	private void helpPVP(CommandSender sender) {
		sender.sendMessage(ChatColor.YELLOW+"============================================================");
		sender.sendMessage(ChatColor.YELLOW+"/PVP 매칭 : "+ChatColor.AQUA+"PVP 매칭을 찾습니다");
		sender.sendMessage(ChatColor.YELLOW+"/PVP 매칭취소 : "+ChatColor.AQUA+"PVP 매칭을 취소합니다");
		sender.sendMessage(ChatColor.YELLOW+"/PVP 정보 <이름> : "+ChatColor.AQUA+"플레이어의 전적을 확인합니다");
		sender.sendMessage(ChatColor.YELLOW+"/PVP 관전 : "+ChatColor.AQUA+"진행되는 PVP를 관전합니다");
		sender.sendMessage(ChatColor.YELLOW+"/PVP 순위 : "+ChatColor.AQUA+"서버 PVP 랭킹을 확인합니다");
		//sender.sendMessage(ChatColor.YELLOW+"============================================================");
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
	@EventHandler
	public void onInvEvent(InventoryClickEvent e) {
		if (e.getInventory().getName() != null) {
			String cmd = ChatColor.stripColor(e.getInventory().getName());
			if (cmd.equals("총기상점") || cmd.equals("weaponshop") || cmd.equals("wshop") || cmd.equals("Wshop")
					|| cmd.equals("WeaponShop")) {
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getTypeId() == 0
						|| !e.getCurrentItem().hasItemMeta()) {
					e.setCancelled(false);
				}

				else {
					OfflinePlayer p = (OfflinePlayer) e.getWhoClicked();
					switch (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName())) {
					case "AK-47":
						if (econ.getBalance(p) >= 500000) { // 금액
							String name;
							name = e.getWhoClicked().getName();
							String command = "shot give ";
							command += name;
							command += " AK-47_CSP";
							Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(), command);
							econ.withdrawPlayer(p, 500000); // 금액
						} else
							e.getWhoClicked().sendMessage("잔액이 부족합니다");
						break;
						
					case "헌터 스나이퍼":
						if (econ.getBalance(p) >= 800000) { // 금액
							String name;
							name = e.getWhoClicked().getName();
							String command = "shot give ";
							command += name;
							command += " Hunting_CSP";
							Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(), command);
							econ.withdrawPlayer(p, 800000); // 금액
						} else
							e.getWhoClicked().sendMessage("잔액이 부족합니다");
						break;
						
					case "Python":
						if (econ.getBalance(p) >= 100000) { // 금액
							String name;
							name = e.getWhoClicked().getName();
							String command = "shot give ";
							command += name;
							command += " Python_CSP";
							Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(), command);
							econ.withdrawPlayer(p, 100000); // 금액
						} else
							e.getWhoClicked().sendMessage("잔액이 부족합니다");
						break;
					case "Desert Eagle":
						if (econ.getBalance(p) >= 300000) { // 금액
							String name;
							name = e.getWhoClicked().getName();
							String command = "shot give ";
							command += name;
							command += " Desert_Eagle_CSP";
							Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(), command);
							econ.withdrawPlayer(p, 300000); // 금액
						} else
							e.getWhoClicked().sendMessage("잔액이 부족합니다");
						break;
					case "더블 배럴":
						if (econ.getBalance(p) >= 1000000) { // 금액
							String name;
							name = e.getWhoClicked().getName();
							String command = "shot give ";
							command += name;
							command += " Olympia_CSP";
							Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(), command);
							econ.withdrawPlayer(p, 1000000); // 금액
						} else
							e.getWhoClicked().sendMessage("잔액이 부족합니다");
						break;
						
					case "탄창":
						if(econ.getBalance(p)>=5000) {
							String name;
							name = e.getWhoClicked().getName();
							String command = "give ";
							command += name;
							command += " 383:1 1 {display:{Name:\"탄창\"}}";
							//give YangJJune 383:1 1 {display:{Name:"탄창"}}
							Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(), command);
							econ.withdrawPlayer(p, 5000); // 금액
						}
						else
							e.getWhoClicked().sendMessage("잔액이 부족합니다");
						break;
					case "샷건 탄창":
						if(econ.getBalance(p)>=500) {
							String name;
							name = e.getWhoClicked().getName();
							String command = "give ";
							command += name;
							command += " 289 1 {display:{Name:\"샷건 탄창\"}}";
							//give YangJJune 383:1 1 {display:{Name:"탄창"}}
							Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(), command);
							econ.withdrawPlayer(p, 500); // 금액
						}
						else
							e.getWhoClicked().sendMessage("잔액이 부족합니다");
						break;
					case "섬광탄":
						if(econ.getBalance(p)>=7000) {
							String name;
							name = e.getWhoClicked().getName();
							String command = "shot give ";
							command += name;
							command += " FlashBang";
							//give YangJJune 383:1 1 {display:{Name:"탄창"}}
							Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(), command);
							econ.withdrawPlayer(p, 7000); // 금액
						}
						else
							e.getWhoClicked().sendMessage("잔액이 부족합니다");
						break;
					}
				}
			}
		}
	}
	
	@EventHandler
	private void onJoin(PlayerItemHeldEvent e) {
		if(e.getPlayer().getInventory().getItemInMainHand()!=null) {
			if(e.getPlayer().getInventory().getItemInMainHand().getTypeId()==291)
				Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(),"csp update inventory "+e.getPlayer().getName());
		}
	}
}
