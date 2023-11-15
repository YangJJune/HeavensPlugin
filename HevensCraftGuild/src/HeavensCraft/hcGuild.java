package HeavensCraft;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import Clans.ClanConfiguration;

public class hcGuild extends JavaPlugin implements Listener{
	public static HashMap<UUID,Long> INSUCooldown = new HashMap<>();
	public static HashMap<UUID,Long> CNCHCooldown = new HashMap<>();
	public static HashMap<String,Long> JoinCooldown = new HashMap<>();
	
	@Override
	public void onEnable() {
		Bukkit.getConsoleSender()
				.sendMessage(ChatColor.GOLD + ("HCGuild") + ChatColor.AQUA + " �÷����� Ȱ��ȭ" + ChatColor.BLACK);
		Bukkit.getPluginManager().registerEvents(this, this);
		/*if(Bukkit.getPluginManager().isPluginEnabled("PlaceHolderAPI")) {
			new ClanPlaceholder(this).register();
		}*/
	}

	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		Bukkit.getConsoleSender()
				.sendMessage(ChatColor.GOLD + ("HCGuild") + ChatColor.DARK_AQUA + " �÷����� ��Ȱ��ȭ" + ChatColor.BLACK);
	}
	@EventHandler
	public void BreakBlock(BlockBreakEvent e) {
		ClanConfiguration clanConfiguration = new ClanConfiguration();
		String clan = clanConfiguration.getClan(e.getPlayer());
		if (clan != null) {
			if (clan.equals("Farm") || clan.equals("PVP")) {
				switch(e.getBlock().getType())
				{
				
				case CROPS:
					if(e.getBlock().getData()==7) {
					Bukkit.getServer().getScheduler().runTaskLater(this, new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							e.getBlock().setType(Material.CROPS);
							e.getBlock().setData((byte) 0);
						}
						
					}, 5L);
					}
					break;
				case POTATO:
					if(e.getBlock().getData()==7) {
					Bukkit.getServer().getScheduler().runTaskLater(this, new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							e.getBlock().setType(Material.POTATO);
							e.getBlock().setData((byte) 0);
						}
						
					}, 5L);
					}
					break;
				case BEETROOT:
					if(e.getBlock().getData()==7) {
					Bukkit.getServer().getScheduler().runTaskLater(this, new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							e.getBlock().setType(Material.BEETROOT);
							e.getBlock().setData((byte) 0);
						}
						
					}, 5L);
					}
					break;
				case CARROT:
					if(e.getBlock().getData()==7) {
					Bukkit.getServer().getScheduler().runTaskLater(this, new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							e.getBlock().setType(Material.CARROT);
							e.getBlock().setData((byte) 0);
						}
						
					}, 5L);
					}
					break;
				}
				
			}
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			ClanConfiguration clanConfiguration = new ClanConfiguration();
			if(cmd.getName().equals("Ŭ��") || cmd.getName().equals("���")) {
				if(args.length>1) {
					switch(args[0]) {
					case "����":
						((Player) sender).chat("/clan create "+args[1]);
						break;
					case "�ʴ�":
						((Player) sender).chat("/clan invite "+args[1]);
						break;
					case "�ʴ����":
						((Player) sender).chat("/clan accept "+args[1]);
						break;
					case "�߹�":
						((Player) sender).chat("/clan kick "+args[1]);
						this.JoinCooldown.put(((Player) sender).getName(), System.currentTimeMillis());
						break;
					case "����":
						((Player) sender).chat("/clan stats "+args[1]);
						break;
					case "����":
						if(clanConfiguration.exists(args[1])) {
							if(this.JoinCooldown.containsKey(((Player) sender).getName())) {
								long cooltime = System.currentTimeMillis()-JoinCooldown.get((sender.getName()));
								if(cooltime>=86400000) {
									if (clanConfiguration.getClan((Player) sender) == null) {
										
										clanConfiguration.addClient(args[1], (Player) sender);
										sender.sendMessage(ChatColor.GREEN + "���������� Ŭ���� �����߽��ϴ�.");
										
										UUID owner = UUID.fromString(clanConfiguration.getClanOwner(args[1]));

										SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat ( "yyyy.MM.dd HH:mm:ss", Locale.KOREA );
										Date currentTime = new Date ();
										String mTime = mSimpleDateFormat.format ( currentTime );
										
										String name = Bukkit.getOfflinePlayer(owner).getName();
										
										Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mail send "+name+" "+ChatColor.GREEN+sender.getName()+ChatColor.WHITE+"���� ����� Ŭ���� �����ϼ̽��ϴ�. "+ChatColor.YELLOW+mTime.toString());
										for(Player p : Bukkit.getOnlinePlayers()) {
											if(p.getUniqueId().equals(owner)) {
												p.sendMessage(ChatColor.GREEN+sender.getName()+ChatColor.WHITE+"���� ����� Ŭ���� �����ϼ̽��ϴ�. "+ChatColor.YELLOW+mTime.toString());
											}
										}
										
									}
									else {
										sender.sendMessage(ChatColor.RED + "�̹� Ŭ���� ���� ���Դϴ�.");
									}
								}
								else {
									sender.sendMessage("�ٽ� Ŭ���� �����Ͻ÷��� "+String.valueOf((86400-cooltime/1000))+"�� ���ҽ��ϴ�");
								}
							}
							else {
								if (clanConfiguration.getClan((Player) sender) == null) {
									
									clanConfiguration.addClient(args[1], (Player) sender);
									sender.sendMessage(ChatColor.GREEN + "���������� Ŭ���� �����߽��ϴ�.");
									UUID owner = UUID.fromString(clanConfiguration.getClanOwner(args[1]));

									SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat ( "yyyy.MM.dd HH:mm:ss", Locale.KOREA );
									Date currentTime = new Date ();
									String mTime = mSimpleDateFormat.format ( currentTime );

									String name = Bukkit.getOfflinePlayer(owner).getName();
									Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mail send "+name+" "+ChatColor.GREEN+sender.getName()+ChatColor.WHITE+"���� ����� Ŭ���� �����ϼ̽��ϴ�. "+ChatColor.YELLOW+mTime.toString());
									for(Player p : Bukkit.getOnlinePlayers()) {
										if(p.getUniqueId().equals(owner)) {
											p.sendMessage(ChatColor.GREEN+sender.getName()+ChatColor.WHITE+"���� ����� Ŭ���� �����ϼ̽��ϴ�. "+ChatColor.YELLOW+mTime.toString());
										}
									}
								}
								else {
									sender.sendMessage(ChatColor.RED + "�̹� Ŭ���� ���� ���Դϴ�.");
								}
							}
							
						}
						else {
							sender.sendMessage(ChatColor.RED+"�ش� Ŭ���� ã�� �� �����ϴ�. Ŭ������ �ٽ� Ȯ�����ּ���");
						}
						
						break;
					}
				}
				else if(args.length==1) {
					switch(args[0]){
					case "��ü��ŷ":
						((Player) sender).chat("/clan stats");
						break;
					case "����":
						((Player) sender).chat("/clan base");
						break;
					case "��������":
						((Player) sender).chat("/clan setbase");
						break;
					case "Ż��":
						((Player) sender).chat("/clan leave");
						this.JoinCooldown.put(((Player) sender).getName(), System.currentTimeMillis());
						break;
					case "����":
						((Player) sender).chat("/clan delete");
						break;
					case "Ŭ�������":
						((Player) sender).chat("/clan list");
						break;
					}

				}
				else {
					sender.sendMessage(ChatColor.YELLOW+"============================================================");
					sender.sendMessage(ChatColor.YELLOW+"/Ŭ�� ���� <Ŭ����> : "+ChatColor.AQUA+"Ŭ���� �����մϴ�. (100�� $)");
					sender.sendMessage(ChatColor.YELLOW+"/Ŭ�� ���� <Ŭ����> : "+ChatColor.AQUA+"Ŭ���� �����մϴ�.");
					sender.sendMessage(ChatColor.YELLOW+"/Ŭ�� �ʴ� <���� �̸�> : "+ChatColor.AQUA+"������ Ŭ���� �ʴ��մϴ�");
					sender.sendMessage(ChatColor.YELLOW+"/Ŭ�� �ʴ���� <���� �̸�> : "+ChatColor.AQUA+"Ŭ�������κ��� �� �ʴ븦 �����մϴ�");
					sender.sendMessage(ChatColor.YELLOW+"/Ŭ�� �߹� <����> : "+ChatColor.AQUA+"������ Ŭ������ �߹��մϴ�");
					sender.sendMessage(ChatColor.YELLOW+"/Ŭ�� ���� <Ŭ����> : "+ChatColor.AQUA+"�ش� Ŭ���� ������ Ȯ���մϴ�");
					sender.sendMessage(ChatColor.YELLOW+"/Ŭ�� ��ü��ŷ : "+ChatColor.AQUA+"Ŭ�� ��ü ��ŷ�� Ȯ���մϴ�");
					sender.sendMessage(ChatColor.YELLOW+"/Ŭ�� ���� : "+ChatColor.AQUA+"Ŭ�� ������ �̵��մϴ�.");
					sender.sendMessage(ChatColor.YELLOW+"/Ŭ�� �������� : "+ChatColor.AQUA+"Ŭ���� ������ �����մϴ�");
					sender.sendMessage(ChatColor.YELLOW+"/Ŭ�� Ż�� : "+ChatColor.AQUA+"Ŭ���� Ż���մϴ�.");
					sender.sendMessage(ChatColor.YELLOW+"/Ŭ�� ���� : "+ChatColor.AQUA+"Ŭ������ Ŭ���� �����մϴ�.");
					sender.sendMessage(ChatColor.YELLOW+"/Ŭ�� Ŭ������� : "+ChatColor.AQUA+"Ŭ������ ����� Ȯ���մϴ�.");
					sender.sendMessage(ChatColor.YELLOW+"============================================================");
				}
			}
			else if(cmd.getName().equals("JYK") || cmd.getName().equals("����ġ����")) {
			}
			else if(cmd.getName().equals("1") || cmd.getName().equals("INSU")) {
				if(clanConfiguration.getClan((Player) sender).equals("INSU") || sender.isOp()) {
					if(this.INSUCooldown.containsKey(((Player) sender).getUniqueId())) {
						if(System.currentTimeMillis()-INSUCooldown.get(((Player) sender).getUniqueId())>=1200000) {
							((Player) sender).addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING,12000,1));
						}
						else {
							Long left = System.currentTimeMillis()-INSUCooldown.get(((Player) sender).getUniqueId());
							sender.sendMessage(String.valueOf(1200-(left/1000))+"�� ���ҽ��ϴ�!");
						}
					}
					else {
						INSUCooldown.put(((Player) sender).getUniqueId(), System.currentTimeMillis());
						((Player) sender).addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING,12000,1));
					}
				}
			}
		}
		return false;
	}
	@EventHandler
	private void onChangeEXP(PlayerExpChangeEvent e) {
		ClanConfiguration clanConfiguration = new ClanConfiguration();
		String clan = clanConfiguration.getClan(e.getPlayer());
		if (clan != null) {
			if (clan.equals("GNG")) {
				e.setAmount((int) (e.getAmount() * 3));
			}
		}
	}
	@EventHandler
	private void onChangeFood(PlayerItemConsumeEvent e) {
		ClanConfiguration clanConfiguration = new ClanConfiguration();
		Player p = e.getPlayer();
		String clan = clanConfiguration.getClan(p);
		if(clan!=null || e.getPlayer().isOp()) {
			if(clan.equals("SBK")) {
				if(e.getItem().getType() == Material.MELON) {
					p.setFoodLevel(p.getFoodLevel()+8);
					p.setSaturation((float) (p.getSaturation()+12.8));
					p.sendMessage(ChatColor.GREEN+"Ŭ�� "+clan+ChatColor.WHITE+"�� Ư��ȿ���� ���� ������� "+ChatColor.AQUA+"4"+ChatColor.WHITE+" ��ŭ �߰��Ǿ����ϴ�");
				}
			}
			else if(clan.equals("MCN")) {
				
				int i = e.getItem().getTypeId();
				switch(i) {
				case 364:
				case 366:
				case 412:
				case 320:
				case 424:
					p.setFoodLevel((p.getFoodLevel()+16));
					p.setSaturation((float) (p.getSaturation()+12.8));
					p.sendMessage(ChatColor.GREEN+"Ŭ�� "+clan+ChatColor.WHITE+"�� Ư��ȿ���� ���� ������� "+ChatColor.AQUA+"8"+ChatColor.WHITE+" ��ŭ �߰��Ǿ����ϴ�");
					break;
				}
			}
			else if(clan.equals("CNCH") || e.getPlayer().isOp()) {
				if(e.getItem().getTypeId()==391) {
					
					if(this.CNCHCooldown.containsKey(e.getPlayer().getUniqueId())) {
						if(System.currentTimeMillis()-CNCHCooldown.get(e.getPlayer().getUniqueId())>=600000) {
							e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED,120000,3));
							e.getPlayer().sendMessage("CNCH Ŭ�� �ɷ� ����!");
						}
						else {
							Long left = System.currentTimeMillis()-CNCHCooldown.get(e.getPlayer().getUniqueId());
							e.getPlayer().sendMessage(String.valueOf(1200-(left/1000))+"�� ���ҽ��ϴ�!");
						}
					}
					else {
						CNCHCooldown.put(e.getPlayer().getUniqueId(), System.currentTimeMillis());
						e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED,120000,1));
						e.getPlayer().sendMessage("CNCH Ŭ�� �ɷ� ����!");
					}
				}				
			}
		}
	}
	
	public String getClanName(Player p) {
		ClanConfiguration clanConfiguration = new ClanConfiguration();
		return clanConfiguration.getClan(p);
	}
	@EventHandler
	private void onChangeFLevel(FoodLevelChangeEvent e) {
		ClanConfiguration clanConfiguration = new ClanConfiguration();
		Player p = (Player) e.getEntity();
		String clan = clanConfiguration.getClan(p);
		if(clan==null) {
			return;
		}
		if (clan.equals("OMEGA")) {
			if (p.getFoodLevel() > e.getFoodLevel()) {
				Random rand = new Random();
				int N = rand.nextInt(2);
				if (N == 1) {
					e.setCancelled(true);
					p.setSaturation((float) (p.getSaturation()+4));
				}
			}
		}
	}
}
