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
				.sendMessage(ChatColor.GOLD + ("HCRecord") + ChatColor.AQUA + " 플러그인 활성화" + ChatColor.BLACK);
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
					"broadcast " + bet.get(i) + "님께서 총액 " + total + "에 당첨되셨습니다!");
			for (OfflinePlayer p : getServer().getOfflinePlayers()) {
				if (p.getName().equals(bet.get(i))) {
					if (p.isOnline()) {
						((Player) p).sendMessage("축하드립니다! 복권에 당첨되셨습니다!");
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
				.sendMessage(ChatColor.GOLD + ("HCRecord") + ChatColor.DARK_AQUA + " 플러그인 비활성화" + ChatColor.BLACK);
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
		Inventory inv = Bukkit.createInventory(null, 36,ChatColor.AQUA+""+ChatColor.BOLD+"후원 상점");
		for(int i=0; i<36; i++) {
			itemset("후원상점",160,15,1,Arrays.asList(""),i,inv);
		}
		itemset("§d날개 §c파§6티§a클",443,0,1,Arrays.asList("§e등 뒤에 §d화려한","§b날개 §6파티클§e을 장착해보세요!","§6§l3,000 §6캐시"),4,inv);
		itemset("§c총기 §b스§d킨",274,2,1,Arrays.asList("§e칙칙한 §7총§e에","§d화려한 스킨§e을!","§6§l2,000 §6캐시"),10,inv);
		itemset("§c총기 §e파§d티§9클",251,14,1,Arrays.asList("§e발포시마다 퍼져나가는","§b형형§a색색§e의 §d파티클!","§6§l1,000 §6캐시"),13,inv);
		itemset("§c총기 §e장§a전 §6색",95,14,1,Arrays.asList("§e재장전 될때 뜨는 §7Reloading...","§b색§e을 바꿔보아요!","§6§l500 §6캐시"),16,inv);
		
		itemset("§b칭호",421,0,1,Arrays.asList("§e채팅창에 뜨는","§b칭호를 구매하실 수 있습니다.","§6§l2,000 §6캐시"),21,inv);
		itemset("§b그림 §a게시",321,0,1,Arrays.asList("§e스폰구역에 있는 표지판처럼","§e자신의 공간에 원하는","§b그림§e을 §a삽입§e하실 수 있습니다","","§73X3 이내의 크기라면 §6§l500 §6캐시","§7그 밖의 경우 §6§l1,000 §6캐시"),22,inv);
		itemset("§b표지판 §a제작 §e요청",323,0,1,Arrays.asList("§e스폰구역에 있는 §3교통표지판§e을","§e제작해드립니다.","§6§l500 §6캐시"),23,inv);
		itemset("§a도구 §e이름 §c부여",278,0,1,Arrays.asList("§e도구/칼 등의 아이템에", "§e색을 부여하실 수 있습니다.","§6§l500 §6캐시"),31,inv);
		
		itemset("구매 방법",340,0,1,Arrays.asList("§b문화상품권 §f/ §b계좌이체", "§f의 방법을 사용하실 수 있으시며","§e계좌이체 §f: §b§l신한 §f§l110-501-883902","§7예금주 : 양석준","§a문화상품권§f은 ","§9디스코드§f 등의 수단으로 직접","§f문의 해주시면 감사하겠습니다.","§9디스코드 §f: §f§l양쭌쭌#1038"),35,inv);
		p.openInventory(inv);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof ConsoleCommandSender) {
			if (cmd.getName().equals("hcrecord")) {
				if ((args[0].charAt(0)!='§')){
					System.out.println(args[0]+" "+args[1]);
					if (args.length >= 2) {
						damage.put(args[0], Double.valueOf(args[1]));
					}
				}
			}
			else if(cmd.getName().equals("admin")) {
				if(!(sender instanceof Player) || isEnd==false) {
					this.getServer().dispatchCommand(getServer().getConsoleSender(), "mm m spawn 광전사 1 world,-181,74,431");
					this.getServer().broadcastMessage(ChatColor.GOLD+"["+ChatColor.YELLOW+"제단"+ChatColor.GOLD+"]"+ ChatColor.GREEN+ChatColor.BOLD+" (-181,74,431)"
					+ChatColor.WHITE+"에 가서 처치하세요!");
					this.isEnd=true;
				}
			}
			else if (cmd.getName().equals("bossdeath1")) {
				if(sender instanceof Player) {
				}
				else {
					this.getServer().broadcastMessage(ChatColor.GOLD+"["+ChatColor.YELLOW+"제단"+ChatColor.GOLD+"] "+ChatColor.WHITE+"광전사를 무찔러서 "+ChatColor.GREEN+""+ChatColor.BOLD+"제단의 축복"+ChatColor.WHITE+"이 내립니다!");
					Bukkit.getServer().getWorld("world").setGameRuleValue("randomTickSpeed", String.valueOf(12));
					for(Player p : Bukkit.getServer().getOnlinePlayers()) {
						p.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, 72000, 3));
						this.getServer().dispatchCommand(getServer().getConsoleSender(), "eco give "+p.getName()+" 200000");
						p.sendMessage(ChatColor.GOLD+"["+ChatColor.YELLOW+"제단"+ChatColor.GOLD+"]"+ ChatColor.GREEN+ChatColor.BOLD+"제단의 은총"+ChatColor.WHITE+"이 내리고 20만원이 지급되었습니다!");
					}
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "bar msg: 제단의 은총 time:6000 p:@a color:GREEN");
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
			if(cmd.getName().equals("카지노")) {
				Player player;
				
				((Player) sender).teleport(new Location(getServer().getWorld("world"), -598, 200, 163));
			}
			else if(cmd.getName().equals("후원") || cmd.getName().equals("후원상점") ) {
				this.open((Player) sender);
			}
			else if(cmd.getName().equals("복권")) {
				if(args.length>=1) {
					switch(args[0]) {
					case "구매":
						if (!bet.contains(sender.getName())) {
							if (econ.getBalance((OfflinePlayer) sender) >= 10000) {
								this.total += 9000;
								bet.add(sender.getName());
								sender.sendMessage("지금까지 모인 복권 총액 : " + total);
							}
						}
						else {
							sender.sendMessage("이미 복권을 사셨습니다!");
						}
						break;
					case "총액":
					case "확인":
						sender.sendMessage("지금까지 모인 복권 총액 : "+total);
						break;
					case "추첨":
						if(sender.hasPermission("hcCasino.owner")) {
							if (bet.size() > 0) {
								Random rand = new Random();
								int i = rand.nextInt(bet.size());
								this.getServer().dispatchCommand(getServer().getConsoleSender(),
										"broadcast " + bet.get(i) + "님께서 총액 " + total + "에 당첨되셨습니다!");
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
					case "유저목록":
						if(sender.hasPermission("hcCasino.owner")) {
							sender.sendMessage(ChatColor.YELLOW+"[복권 구매자]");
							for(String str : bet) {
								sender.sendMessage(str);
							}
						}
						break;
					}
				}
			}
			else if(cmd.getName().equals("튜토리얼")) {
				getServer().dispatchCommand(getServer().getConsoleSender(),
						"st play newbie " + sender.getName());
			}
			else if(cmd.getName().equals("야생")) {
				((Player) sender).chat("/rtp");
			}
			else if(cmd.getName().equals("랜덤텔포")) {
				((Player) sender).chat("/rtp");
			}
			else if(cmd.getName().equals("워프") || cmd.getName().equals("GUI워프") ) {
				hcWarp a = new hcWarp();
				
				a.open((Player) sender);
			}
			else if(cmd.getName().equals("축구")) {
				getServer().dispatchCommand(getServer().getConsoleSender(),
						"warp soccer " + sender.getName()+ " ");
			}
			else if(cmd.getName().equals("던전")) {
				this.getServer().dispatchCommand(getServer().getConsoleSender(), "warp aptdungeon "+sender.getName());
			}
			else if(cmd.getName().equals("제단")) {
				if(args.length>=1) {
					switch(args[0]) {
					case "아이템바치기":
						((Player) sender).chat("/altar donateitem "+args[1]);
						break;
					case "돈바치기":
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
			if (cmd.equals("GUI 워프") || cmd.contentEquals("후원 상점")) {
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
				case "오버월드":
					comd = "mv tp ";
					comd += e.getWhoClicked().getName();
					comd += " world";
					this.getServer().dispatchCommand(this.getServer().getConsoleSender(), comd);
					break;
				case "네더월드":
					comd = "mv tp ";
					comd += e.getWhoClicked().getName();
					comd += " world_nether";
					this.getServer().dispatchCommand(this.getServer().getConsoleSender(), comd);
					break;
				case "엔더월드":
					comd = "mv tp ";
					comd += e.getWhoClicked().getName();
					comd += " world_the_end";
					this.getServer().dispatchCommand(this.getServer().getConsoleSender(), comd);
					break;
				case "유미마을":
					
					e.getWhoClicked().teleport(new Location(world,4524,75,1405));
					break;
				case "서울":
					e.getWhoClicked().teleport(new Location(world,-1705,65,974));
					break;
				case "인천":
					e.getWhoClicked().teleport(new Location(world,-2216,68,421));
					break;
				case "다훈마을":
					e.getWhoClicked().teleport(new Location(world,-637,64,6079));
					break;
				case "부산":
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
		if (e.getMessage().contains("/주식 구매") || e.getMessage().contains("/주식 판매")) {
			String cmd = e.getMessage();
			String[] s = cmd.split(" ");
			if(s.length>=4) {
				if(Long.valueOf(s[3])>10000) {
					e.setCancelled(true);
					e.getPlayer().sendMessage("주식의 구매 또는 판매는 최대 10000 개까지 가능합니다.");
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
