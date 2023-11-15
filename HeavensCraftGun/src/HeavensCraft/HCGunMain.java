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
				.sendMessage(ChatColor.GOLD + ("HCGun") + ChatColor.AQUA + " �÷����� Ȱ��ȭ" + ChatColor.BLACK);
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
				.sendMessage(ChatColor.GOLD + ("HCGun") + ChatColor.DARK_AQUA + " �÷����� ��Ȱ��ȭ" + ChatColor.BLACK);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// TODO Auto-generated method stub
		if (sender instanceof Player) {
			switch (cmd.getName()) {
			case "Īȣ�߰���û":
				if(args.length>=1) {
					String str=args[0];
					for(int i=1; i<args.length; i++) {
						str+=args[i];
					}
					((Player) sender).chat("/mail send yangjjune [Īȣ�߰�] "+str);
				}
				break;
			case "���ڵ�":
			case "����":
				sender.sendMessage(ChatColor.DARK_AQUA+"https://discord.gg/Cqv9JEG");
				break;
			case "���θ���Ʈ":
			case "�⼮üũ":
			case "��ý":
				sender.sendMessage(ChatColor.DARK_AQUA+"https://minelist.kr/servers/8492/votes/new"+ ChatColor.YELLOW+"�� ��ũ�� ���ż� ��õ�� �����ø� �˴ϴ�!");
				break;
			case "�ѱ����":
				HCGOpenGUI gui = new HCGOpenGUI();
				gui.open((Player) sender);
				break;
			case "��������":
				ItemStack tool = ((Player) sender).getInventory().getItemInMainHand();
				if (tool != null) {
					int cost = 5000 + tool.getDurability() * 10;
					if (OnRepair.get(sender) == null) {
						sender.sendMessage(ChatColor.YELLOW+"������ "+ tool.getData().getItemType()+"��(��) "+ChatColor.GREEN+"$ " + cost + ChatColor.YELLOW+"��ŭ�� ����� �ʿ��մϴ�. ����Ͻ÷���"+ChatColor.AQUA+ " /��������"+ChatColor.YELLOW +"�� �Է����ּ���.");
						OnRepair.put((Player) sender, true);
					} else if (OnRepair.get(sender) == false) {
						sender.sendMessage(ChatColor.YELLOW+"������ "+ tool.getData().getItemType()+"��(��) "+ChatColor.GREEN+"$ " + cost + ChatColor.YELLOW+"��ŭ�� ����� �ʿ��մϴ�. ����Ͻ÷���"+ChatColor.AQUA+ " /��������"+ChatColor.YELLOW +"�� �Է����ּ���.");
						OnRepair.replace((Player) sender, true);
					} else if (OnRepair.get(sender) == true) {
						if (econ.getBalance((OfflinePlayer) sender) >= cost) {
							sender.sendMessage(ChatColor.GREEN+"$ " + cost + ChatColor.GRAY+"�� ������ ������ �Ϸ��Ͽ����ϴ�");
							tool.setDurability((short) 0);
							econ.withdrawPlayer((OfflinePlayer) sender, cost);
							OnRepair.replace((Player) sender, false);
						} else {
							sender.sendMessage("�ܾ��� �����մϴ�. ����� $ " + cost + " �Դϴ�");
							OnRepair.replace((Player) sender, false);
						}
					}
				}
				break;
			case "����ȣ":
				if(args.length==0) {
					HelpBlockProtect(sender);
				}
				else {
					switch(args[0]) {
					case "��ü����":
						((Player) sender).performCommand("abandonallclaims");
						break;
						
					case "ģ���߰�":
						if(args.length>=2) {
							((Player) sender).performCommand("trust "+args[1]);
						}
						else {
							sender.sendMessage("ģ���� �߰��� �̸��� �Է����ּ���.\n��ɾ� : /����ȣ ģ���߰� <�̸�>");
						}
						break;
						
					case "ģ������":
						if(args.length>=2) {
							((Player) sender).performCommand("untrust "+args[1]);
						}
						else {
							sender.sendMessage("ģ������ ������ �̸��� �Է����ּ���.\n��ɾ� : /����ȣ ģ���߰� <�̸�>");
						}
						break;
						
					case "���":
						((Player) sender).performCommand("claimlist");
						break;
						
					case "�߰�����":
						if(args.length>=2) {
							((Player) sender).performCommand("buyclaimblocks "+args[1]);
						}
						else {
							sender.sendMessage("������ ������ �Է����ּ���.\n"+ChatColor.AQUA+"��ɾ� : /����ȣ �߰����� <����>");
						}
						break;
						
					case "����":
						((Player) sender).performCommand("abandonclaim");
						break;
						
					case "����":
						HelpBlockProtect(sender);
						break;
						
					default:
						HelpBlockProtect(sender);
						break;	
					}
					
				}
				break;
				
			case "�������":
				if(args.length==0) {
					((Player) sender).performCommand("ah");
				}
				else {
					switch(args[0]) {
					case "����":
						HelpAuction(sender);
						break;
					case "�ȱ�":
						if(args.length>=2)
							((Player) sender).performCommand("ah sell "+args[1]);
						else
							sender.sendMessage("�Ǹ��� ������ �Է����ּ���\n"+ChatColor.AQUA+"��ɾ� : /������� �ȱ� <����>");
						break;
					default:
						HelpAuction(sender);
						break;	
					}
					
				}
				break;
			case "��":
				if(args.length==0) {
					((Player) sender).performCommand("money");
				}
				else {
					switch(args[0]) {
					case "Ȯ��":
						((Player) sender).performCommand("money");
						break;
					case "����":
						((Player) sender).performCommand("balancetop");
						break;
					case "������":
						if(args.length>=3) {
							((Player) sender).performCommand("pay "+args[1]+ " "+args[2]);
						}
						else {
							sender.sendMessage("�߸��� �Է��Դϴ�.\n"+ChatColor.AQUA+"��ɾ� : /�� ������ <�������> <�ݾ�>");
						}
						break;
					default:
						EconomyHelp(sender);
						break;	
					}					
				}
				break;
			case "��":
				if (args.length == 0) {
					((Player) sender).performCommand("home");
				} 
				else {
					if (args[0].equals("����")) {
						((Player) sender).performCommand("sethome");
					}
					else {
						sender.sendMessage("�� �� ���� ��ɾ��Դϴ�. ������ Ȯ�����ּ���"+ChatColor.AQUA+"��ɾ� : /����");
					}
				}
				break;
			case "����":
				HelpAuction(sender);
				HelpBlockProtect(sender);
				this.helpPVP(sender);
				EconomyHelp(sender);
				this.etcProtect(sender);
				break;
			case "��ȯ":
				((Player) sender).performCommand("back");
				break;
			}

		}
		return true;
	}
	private void EconomyHelp(CommandSender sender) {
		sender.sendMessage(ChatColor.YELLOW+"============================================================");
		sender.sendMessage(ChatColor.YELLOW+"/�� : "+ChatColor.AQUA+"�ڽ��� ���� Ȯ���մϴ�");
		sender.sendMessage(ChatColor.YELLOW+"/�� Ȯ�� : "+ChatColor.AQUA+"�ڽ��� ���� Ȯ���մϴ�");
		sender.sendMessage(ChatColor.YELLOW+"/�� ���� : "+ChatColor.AQUA+"���� ������ �������� ��� ������ Ȯ���մϴ�");
		sender.sendMessage(ChatColor.YELLOW+"/�� ������ <���� ���> <���� ����>: "+ChatColor.AQUA+"�������� ���� �����ϴ�");
		//sender.sendMessage(ChatColor.YELLOW+"============================================================");
	}

	private void HelpAuction(CommandSender sender) {
		// TODO Auto-generated method stub
		sender.sendMessage(ChatColor.YELLOW+"============================================================");
		sender.sendMessage(ChatColor.YELLOW+"/������� : "+ChatColor.AQUA+"������� â�� ���ϴ�");
		sender.sendMessage(ChatColor.YELLOW+"/������� �ȱ� <����> : "+ChatColor.AQUA+"�տ� ��� �ִ� �������� ������忡 �˴ϴ�");
		//sender.sendMessage(ChatColor.YELLOW+"============================================================");
	}

	private void HelpBlockProtect(CommandSender sender) {
		sender.sendMessage(ChatColor.YELLOW+"============================================================");
		sender.sendMessage(ChatColor.YELLOW+"/����ȣ ��ü���� : "+ChatColor.AQUA+"�ڽ��� ��� ����ȣ������ �����մϴ�");
		sender.sendMessage(ChatColor.YELLOW+"/����ȣ ģ���߰� <�̸�> : "+ChatColor.AQUA+"�ڽ��� ������ ģ���� �߰��մϴ�");
		sender.sendMessage(ChatColor.YELLOW+"/����ȣ ģ������ <�̸�> : "+ChatColor.AQUA+"�ڽ��� ������ ģ���� �����մϴ�");
		sender.sendMessage(ChatColor.YELLOW+"/����ȣ ��� : "+ChatColor.AQUA+"�ڽ��� ��� ����ȣ������ Ȯ���մϴ�");
		sender.sendMessage(ChatColor.YELLOW+"/����ȣ �߰����� : "+ChatColor.AQUA+"��ȣ ������ ���� ���� �� �ø��ϴ� (1���� = $2000)");
		sender.sendMessage(ChatColor.YELLOW+"/����ȣ ���� : "+ChatColor.AQUA+"�ڽ��� ���ִ� ��ġ�� ����ȣ������ �����մϴ�");
		sender.sendMessage(ChatColor.YELLOW+"/����ȣ ���� : "+ChatColor.AQUA+"����ȣ ���� ������ ����մϴ�");
		//sender.sendMessage(ChatColor.YELLOW+"============================================================");
	}
	private void etcProtect(CommandSender sender) {
		sender.sendMessage(ChatColor.YELLOW+"============================================================");
		sender.sendMessage(ChatColor.YELLOW+"/�� : "+ChatColor.AQUA+"������ �̵��մϴ�");
		sender.sendMessage(ChatColor.YELLOW+"/�� ���� : "+ChatColor.AQUA+"���� �����մϴ�");
		sender.sendMessage(ChatColor.YELLOW+"/����: "+ChatColor.AQUA+"���� ���� GUI�� ���ϴ�");
		sender.sendMessage(ChatColor.YELLOW+"/���� : "+ChatColor.AQUA+"������������ �̵��մϴ�");
		sender.sendMessage(ChatColor.YELLOW+"/�߻� : "+ChatColor.AQUA+"���� �ڷ���Ʈ�� �Ͽ� �߻��������� �̵��մϴ�");
		sender.sendMessage(ChatColor.YELLOW+"/�ѱ���� : "+ChatColor.AQUA+"�ѱ������ ���ϴ�");
		sender.sendMessage(ChatColor.YELLOW+"/�������� : "+ChatColor.AQUA+"������ �����մϴ�");
		sender.sendMessage(ChatColor.YELLOW+"/�ֽ�: "+ChatColor.AQUA+"�ֽ� ���� ��ɾ Ȯ���մϴ�.");
		sender.sendMessage(ChatColor.YELLOW+"/�߷�Ÿ�� ���� : "+ChatColor.AQUA+"�߷�Ÿ�� ������ ���ϴ�");
		sender.sendMessage(ChatColor.YELLOW+"/�߷�Ÿ�� ��ȯ : "+ChatColor.AQUA+"�߷�Ÿ�� ������ ��ȯ�մϴ�");
		sender.sendMessage(ChatColor.YELLOW+"============================================================");
		sender.sendMessage(ChatColor.YELLOW+"/�ɷ���ī�� �̱� : "+ChatColor.AQUA+"�ɷ���ī�� �̱⸦ 1ȸ �����մϴ� (10����)");
		sender.sendMessage(ChatColor.YELLOW+"/�ɷ���ī�� �����̱� : "+ChatColor.AQUA+"�ɷ���ī�� �̱⸦ 10ȸ �����մϴ� (90����)");
		sender.sendMessage(ChatColor.YELLOW+"/pvp����: "+ChatColor.AQUA+"pvp ������ �����մϴ�");
		sender.sendMessage(ChatColor.YELLOW+"============================================================");
	}
	private void helpPVP(CommandSender sender) {
		sender.sendMessage(ChatColor.YELLOW+"============================================================");
		sender.sendMessage(ChatColor.YELLOW+"/PVP ��Ī : "+ChatColor.AQUA+"PVP ��Ī�� ã���ϴ�");
		sender.sendMessage(ChatColor.YELLOW+"/PVP ��Ī��� : "+ChatColor.AQUA+"PVP ��Ī�� ����մϴ�");
		sender.sendMessage(ChatColor.YELLOW+"/PVP ���� <�̸�> : "+ChatColor.AQUA+"�÷��̾��� ������ Ȯ���մϴ�");
		sender.sendMessage(ChatColor.YELLOW+"/PVP ���� : "+ChatColor.AQUA+"����Ǵ� PVP�� �����մϴ�");
		sender.sendMessage(ChatColor.YELLOW+"/PVP ���� : "+ChatColor.AQUA+"���� PVP ��ŷ�� Ȯ���մϴ�");
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
			if (cmd.equals("�ѱ����") || cmd.equals("weaponshop") || cmd.equals("wshop") || cmd.equals("Wshop")
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
						if (econ.getBalance(p) >= 500000) { // �ݾ�
							String name;
							name = e.getWhoClicked().getName();
							String command = "shot give ";
							command += name;
							command += " AK-47_CSP";
							Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(), command);
							econ.withdrawPlayer(p, 500000); // �ݾ�
						} else
							e.getWhoClicked().sendMessage("�ܾ��� �����մϴ�");
						break;
						
					case "���� ��������":
						if (econ.getBalance(p) >= 800000) { // �ݾ�
							String name;
							name = e.getWhoClicked().getName();
							String command = "shot give ";
							command += name;
							command += " Hunting_CSP";
							Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(), command);
							econ.withdrawPlayer(p, 800000); // �ݾ�
						} else
							e.getWhoClicked().sendMessage("�ܾ��� �����մϴ�");
						break;
						
					case "Python":
						if (econ.getBalance(p) >= 100000) { // �ݾ�
							String name;
							name = e.getWhoClicked().getName();
							String command = "shot give ";
							command += name;
							command += " Python_CSP";
							Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(), command);
							econ.withdrawPlayer(p, 100000); // �ݾ�
						} else
							e.getWhoClicked().sendMessage("�ܾ��� �����մϴ�");
						break;
					case "Desert Eagle":
						if (econ.getBalance(p) >= 300000) { // �ݾ�
							String name;
							name = e.getWhoClicked().getName();
							String command = "shot give ";
							command += name;
							command += " Desert_Eagle_CSP";
							Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(), command);
							econ.withdrawPlayer(p, 300000); // �ݾ�
						} else
							e.getWhoClicked().sendMessage("�ܾ��� �����մϴ�");
						break;
					case "���� �跲":
						if (econ.getBalance(p) >= 1000000) { // �ݾ�
							String name;
							name = e.getWhoClicked().getName();
							String command = "shot give ";
							command += name;
							command += " Olympia_CSP";
							Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(), command);
							econ.withdrawPlayer(p, 1000000); // �ݾ�
						} else
							e.getWhoClicked().sendMessage("�ܾ��� �����մϴ�");
						break;
						
					case "źâ":
						if(econ.getBalance(p)>=5000) {
							String name;
							name = e.getWhoClicked().getName();
							String command = "give ";
							command += name;
							command += " 383:1 1 {display:{Name:\"źâ\"}}";
							//give YangJJune 383:1 1 {display:{Name:"źâ"}}
							Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(), command);
							econ.withdrawPlayer(p, 5000); // �ݾ�
						}
						else
							e.getWhoClicked().sendMessage("�ܾ��� �����մϴ�");
						break;
					case "���� źâ":
						if(econ.getBalance(p)>=500) {
							String name;
							name = e.getWhoClicked().getName();
							String command = "give ";
							command += name;
							command += " 289 1 {display:{Name:\"���� źâ\"}}";
							//give YangJJune 383:1 1 {display:{Name:"źâ"}}
							Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(), command);
							econ.withdrawPlayer(p, 500); // �ݾ�
						}
						else
							e.getWhoClicked().sendMessage("�ܾ��� �����մϴ�");
						break;
					case "����ź":
						if(econ.getBalance(p)>=7000) {
							String name;
							name = e.getWhoClicked().getName();
							String command = "shot give ";
							command += name;
							command += " FlashBang";
							//give YangJJune 383:1 1 {display:{Name:"źâ"}}
							Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(), command);
							econ.withdrawPlayer(p, 7000); // �ݾ�
						}
						else
							e.getWhoClicked().sendMessage("�ܾ��� �����մϴ�");
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
