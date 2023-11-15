package HeavensCraft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import net.minecraft.server.v1_12_R1.GameRules;


public class hcRecord extends JavaPlugin implements Listener{
	static int total = 0;
	List<String> bet = new ArrayList<>();
	HashMap<String,Double> damage = new HashMap<>();
	private static final Logger log = Logger.getLogger("Minecraft");
	private static Economy econ = null;
	private static Permission perms = null;
	private static Chat chat = null;
	private static HashMap<Player, Boolean> OnRepair = new HashMap<>();
	private boolean isEnd = false;
	
	
	@Override
	public void onEnable() {
		Bukkit.getServer().getWorld("world").setGameRuleValue("randomTickSpeed", String.valueOf(3));
		Bukkit.getConsoleSender()
				.sendMessage(ChatColor.GOLD + ("HCRecord") + ChatColor.AQUA + " �÷����� Ȱ��ȭ" + ChatColor.BLACK);
		if (!setupEconomy()) {
			log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		getServer().getPluginManager().registerEvents(this, this);
		setupPermissions();
		setupChat();
	}

	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		if (bet.size() > 0) {
			Random rand = new Random();
			int i = rand.nextInt(bet.size());
			this.getServer().dispatchCommand(getServer().getConsoleSender(),
					"broadcast " + bet.get(i) + "�Բ��� �Ѿ� " + total + "�� ��÷�Ǽ̽��ϴ�!");
			for (OfflinePlayer p : getServer().getOfflinePlayers()) {
				if (p.getName().equals(bet.get(i))) {
					if (p.isOnline()) {
						((Player) p).sendMessage("���ϵ帳�ϴ�! ���ǿ� ��÷�Ǽ̽��ϴ�!");
					}
					econ.depositPlayer(p, total);
					break;
				}
			}
			total = 0;
			bet.clear();
		}

		log.info(String.format("[%s] Disabled Version %s", getDescription().getName(), getDescription().getVersion()));
		Bukkit.getConsoleSender()
				.sendMessage(ChatColor.GOLD + ("HCRecord") + ChatColor.DARK_AQUA + " �÷����� ��Ȱ��ȭ" + ChatColor.BLACK);
	}
	
	public void itemset(String display, int ID, int data, int stack, List<String> Lore, int loc, Inventory inv) {
		ItemStack item = new MaterialData(ID,(byte)(data)).toItemStack(stack);
		ItemMeta items = item.getItemMeta();
		items.setDisplayName(display);
		if(ID==274) {
			items.setUnbreakable(true);
		}
		items.setLore(Lore);
		//item.setDurability((short) (item.getDurability()-d));
		item.setItemMeta(items);
		inv.setItem(loc, item);
	}
	
	public void open(Player p) {		
		Inventory inv = Bukkit.createInventory(null, 36,ChatColor.AQUA+""+ChatColor.BOLD+"�Ŀ� ����");
		for(int i=0; i<36; i++) {
			itemset("�Ŀ�����",160,15,1,Arrays.asList(""),i,inv);
		}
		itemset("��d���� ��c�ġ�6Ƽ��aŬ",443,0,1,Arrays.asList("��e�� �ڿ� ��dȭ����","��b���� ��6��ƼŬ��e�� �����غ�����!","��6��l3,000 ��6ĳ��"),4,inv);
		itemset("��c�ѱ� ��b����dŲ",274,2,1,Arrays.asList("��eĢĢ�� ��7�ѡ�e��","��dȭ���� ��Ų��e��!","��6��l2,000 ��6ĳ��"),10,inv);
		itemset("��c�ѱ� ��e�ġ�dƼ��9Ŭ",251,14,1,Arrays.asList("��e�����ø��� ����������","��b������a������e�� ��d��ƼŬ!","��6��l1,000 ��6ĳ��"),13,inv);
		itemset("��c�ѱ� ��e���a�� ��6��",95,14,1,Arrays.asList("��e������ �ɶ� �ߴ� ��7Reloading...","��b����e�� �ٲ㺸�ƿ�!","��6��l500 ��6ĳ��"),16,inv);
		
		itemset("��bĪȣ",421,0,1,Arrays.asList("��eä��â�� �ߴ�","��bĪȣ�� �����Ͻ� �� �ֽ��ϴ�.","��6��l2,000 ��6ĳ��"),21,inv);
		itemset("��b�׸� ��a�Խ�",321,0,1,Arrays.asList("��e���������� �ִ� ǥ����ó��","��e�ڽ��� ������ ���ϴ�","��b�׸���e�� ��a���ԡ�e�Ͻ� �� �ֽ��ϴ�","","��73X3 �̳��� ũ���� ��6��l500 ��6ĳ��","��7�� ���� ��� ��6��l1,000 ��6ĳ��"),22,inv);
		itemset("��bǥ���� ��a���� ��e��û",323,0,1,Arrays.asList("��e���������� �ִ� ��3����ǥ���ǡ�e��","��e�����ص帳�ϴ�.","��6��l500 ��6ĳ��"),23,inv);
		itemset("��a���� ��e�̸� ��c�ο�",278,0,1,Arrays.asList("��e����/Į ���� �����ۿ�", "��e���� �ο��Ͻ� �� �ֽ��ϴ�.","��6��l500 ��6ĳ��"),31,inv);
		
		itemset("���� ���",340,0,1,Arrays.asList("��b��ȭ��ǰ�� ��f/ ��b������ü", "��f�� ����� ����Ͻ� �� �����ø�","��e������ü ��f: ��b��l���� ��f��l110-501-883902","��7������ : �缮��","��a��ȭ��ǰ�ǡ�f�� ","��9���ڵ��f ���� �������� ����","��f���� ���ֽø� �����ϰڽ��ϴ�.","��9���ڵ� ��f: ��f��l������#1038"),35,inv);
		p.openInventory(inv);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof ConsoleCommandSender) {
			if (cmd.getName().equals("hcrecord")) {
				if ((args[0].charAt(0)!='��')){
					System.out.println(args[0]+" "+args[1]);
					if (args.length >= 2) {
						damage.put(args[0], Double.valueOf(args[1]));
					}
				}
			}
			else if(cmd.getName().equals("admin")) {
				if(!(sender instanceof Player) || isEnd==false) {
					this.getServer().dispatchCommand(getServer().getConsoleSender(), "mm m spawn ������ 1 world,-181,74,431");
					this.getServer().broadcastMessage(ChatColor.GOLD+"["+ChatColor.YELLOW+"����"+ChatColor.GOLD+"]"+ ChatColor.GREEN+ChatColor.BOLD+" (-181,74,431)"
					+ChatColor.WHITE+"�� ���� óġ�ϼ���!");
					this.isEnd=true;
				}
			}
			else if (cmd.getName().equals("bossdeath1")) {
				if(sender instanceof Player) {
				}
				else {
					this.getServer().broadcastMessage(ChatColor.GOLD+"["+ChatColor.YELLOW+"����"+ChatColor.GOLD+"] "+ChatColor.WHITE+"�����縦 ���񷯼� "+ChatColor.GREEN+""+ChatColor.BOLD+"������ �ູ"+ChatColor.WHITE+"�� �����ϴ�!");
					Bukkit.getServer().getWorld("world").setGameRuleValue("randomTickSpeed", String.valueOf(12));
					for(Player p : Bukkit.getServer().getOnlinePlayers()) {
						p.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, 72000, 3));
						this.getServer().dispatchCommand(getServer().getConsoleSender(), "eco give "+p.getName()+" 200000");
						p.sendMessage(ChatColor.GOLD+"["+ChatColor.YELLOW+"����"+ChatColor.GOLD+"]"+ ChatColor.GREEN+ChatColor.BOLD+"������ ����"+ChatColor.WHITE+"�� ������ 20������ ���޵Ǿ����ϴ�!");
					}
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "bar msg: ������ ���� time:6000 p:@a color:GREEN");
					isEnd=false;
					this.getServer().dispatchCommand(getServer().getConsoleSender(), "");
					Bukkit.getScheduler().runTaskLater(this, new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Bukkit.getServer().getWorld("world").setGameRuleValue("randomTickSpeed", String.valueOf(3));
						}
					}, 72000);
				}
			}
		} else if (cmd.getName().equals("hcrecordfinal")) {
			Set<Entry<String, Double>> entries = damage.entrySet();
			for (Entry<String, Double> entry : entries) {
				String str = entry.getKey() + " : " + entry.getValue();
				this.getServer().dispatchCommand(getServer().getConsoleSender(), "broadcast " + str);
			}

		}
		
		else if(sender instanceof Player) {
			if(cmd.getName().equals("ī����")) {
				Player player;
				
				((Player) sender).teleport(new Location(getServer().getWorld("world"), -598, 200, 163));
			}
			else if(cmd.getName().equals("�Ŀ�") || cmd.getName().equals("�Ŀ�����") ) {
				this.open((Player) sender);
			}
			else if(cmd.getName().equals("����")) {
				if(args.length>=1) {
					switch(args[0]) {
					case "����":
						if (!bet.contains(sender.getName())) {
							if (econ.getBalance((OfflinePlayer) sender) >= 10000) {
								this.total += 9000;
								bet.add(sender.getName());
								sender.sendMessage("���ݱ��� ���� ���� �Ѿ� : " + total);
							}
						}
						else {
							sender.sendMessage("�̹� ������ ��̽��ϴ�!");
						}
						break;
					case "�Ѿ�":
					case "Ȯ��":
						sender.sendMessage("���ݱ��� ���� ���� �Ѿ� : "+total);
						break;
					case "��÷":
						if(sender.hasPermission("hcCasino.owner")) {
							if (bet.size() > 0) {
								Random rand = new Random();
								int i = rand.nextInt(bet.size());
								this.getServer().dispatchCommand(getServer().getConsoleSender(),
										"broadcast " + bet.get(i) + "�Բ��� �Ѿ� " + total + "�� ��÷�Ǽ̽��ϴ�!");
								for (OfflinePlayer p : getServer().getOfflinePlayers()) {
									if (p.getName().equals(bet.get(i))) {
										econ.depositPlayer(p, total*0.9);
										break;
									}
								}
								econ.depositPlayer((OfflinePlayer)sender,  bet.size()*100.0);
								total = 0;
								bet.clear();
							}
						}
						break;
					case "�������":
						if(sender.hasPermission("hcCasino.owner")) {
							sender.sendMessage(ChatColor.YELLOW+"[���� ������]");
							for(String str : bet) {
								sender.sendMessage(str);
							}
						}
						break;
					}
				}
			}
			else if(cmd.getName().equals("Ʃ�丮��")) {
				getServer().dispatchCommand(getServer().getConsoleSender(),
						"st play newbie " + sender.getName());
			}
			else if(cmd.getName().equals("�߻�")) {
				((Player) sender).chat("/rtp");
			}
			else if(cmd.getName().equals("��������")) {
				((Player) sender).chat("/rtp");
			}
			else if(cmd.getName().equals("����") || cmd.getName().equals("GUI����") ) {
				hcWarp a = new hcWarp();
				
				a.open((Player) sender);
			}
			else if(cmd.getName().equals("�౸")) {
				getServer().dispatchCommand(getServer().getConsoleSender(),
						"warp soccer " + sender.getName()+ " ");
			}
			else if(cmd.getName().equals("����")) {
				this.getServer().dispatchCommand(getServer().getConsoleSender(), "warp aptdungeon "+sender.getName());
			}
			else if(cmd.getName().equals("����")) {
				if(args.length>=1) {
					switch(args[0]) {
					case "�����۹�ġ��":
						((Player) sender).chat("/altar donateitem "+args[1]);
						break;
					case "����ġ��":
						((Player) sender).chat("/altar donatemoney "+args[1]);
						break;
					default:
						((Player) sender).chat("/altar help");
						break;
					}
				}
				else {
					this.getServer().dispatchCommand(getServer().getConsoleSender(), "altar help");
				}
			}
		}
		return false;
	}	
	@EventHandler
	public void onInvEvent(InventoryClickEvent e) {
		if (e.getInventory().getName() != null) {
			String cmd = ChatColor.stripColor(e.getInventory().getName());
			if (cmd.equals("GUI ����") || cmd.contentEquals("�Ŀ� ����")) {
				e.setCancelled(true);
			} else {
				return;
			}
			if (e.getCurrentItem() == null || e.getCurrentItem().getTypeId() == 0
					|| !e.getCurrentItem().hasItemMeta()) {
				e.setCancelled(false);
			} else {
				World world = Bukkit.getServer().getWorld("world");
				Player p = (Player) e.getWhoClicked();
				String comd;
				switch (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName())) {
				case "��������":
					comd = "mv tp ";
					comd += e.getWhoClicked().getName();
					comd += " world";
					this.getServer().dispatchCommand(this.getServer().getConsoleSender(), comd);
					break;
				case "�״�����":
					comd = "mv tp ";
					comd += e.getWhoClicked().getName();
					comd += " world_nether";
					this.getServer().dispatchCommand(this.getServer().getConsoleSender(), comd);
					break;
				case "��������":
					comd = "mv tp ";
					comd += e.getWhoClicked().getName();
					comd += " world_the_end";
					this.getServer().dispatchCommand(this.getServer().getConsoleSender(), comd);
					break;
				case "���̸���":
					
					e.getWhoClicked().teleport(new Location(world,4524,75,1405));
					break;
				case "����":
					e.getWhoClicked().teleport(new Location(world,-1705,65,974));
					break;
				case "��õ":
					e.getWhoClicked().teleport(new Location(world,-2216,68,421));
					break;
				case "���Ƹ���":
					e.getWhoClicked().teleport(new Location(world,-637,64,6079));
					break;
				case "�λ�":
					e.getWhoClicked().teleport(new Location(world,513,63,8522));
					break;
				case "Paris":
					e.getWhoClicked().teleport(new Location(world,226,67,12500));
					break;
				}
			}
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

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCommandPreprocess(PlayerCommandPreprocessEvent e) {
		if (e.getMessage().contains("/�ֽ� ����") || e.getMessage().contains("/�ֽ� �Ǹ�")) {
			String cmd = e.getMessage();
			String[] s = cmd.split(" ");
			if(s.length>=4) {
				if(Long.valueOf(s[3])>10000) {
					e.setCancelled(true);
					e.getPlayer().sendMessage("�ֽ��� ���� �Ǵ� �ǸŴ� �ִ� 10000 ������ �����մϴ�.");
				}
			}
		}
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
