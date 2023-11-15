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
				.sendMessage(ChatColor.GOLD + ("HCGuild") + ChatColor.AQUA + " 플러그인 활성화" + ChatColor.BLACK);
		Bukkit.getPluginManager().registerEvents(this, this);
		/*if(Bukkit.getPluginManager().isPluginEnabled("PlaceHolderAPI")) {
			new ClanPlaceholder(this).register();
		}*/
	}

	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		Bukkit.getConsoleSender()
				.sendMessage(ChatColor.GOLD + ("HCGuild") + ChatColor.DARK_AQUA + " 플러그인 비활성화" + ChatColor.BLACK);
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
			if(cmd.getName().equals("클랜") || cmd.getName().equals("길드")) {
				if(args.length>1) {
					switch(args[0]) {
					case "생성":
						((Player) sender).chat("/clan create "+args[1]);
						break;
					case "초대":
						((Player) sender).chat("/clan invite "+args[1]);
						break;
					case "초대수락":
						((Player) sender).chat("/clan accept "+args[1]);
						break;
					case "추방":
						((Player) sender).chat("/clan kick "+args[1]);
						this.JoinCooldown.put(((Player) sender).getName(), System.currentTimeMillis());
						break;
					case "정보":
						((Player) sender).chat("/clan stats "+args[1]);
						break;
					case "가입":
						if(clanConfiguration.exists(args[1])) {
							if(this.JoinCooldown.containsKey(((Player) sender).getName())) {
								long cooltime = System.currentTimeMillis()-JoinCooldown.get((sender.getName()));
								if(cooltime>=86400000) {
									if (clanConfiguration.getClan((Player) sender) == null) {
										
										clanConfiguration.addClient(args[1], (Player) sender);
										sender.sendMessage(ChatColor.GREEN + "성공적으로 클랜에 가입했습니다.");
										
										UUID owner = UUID.fromString(clanConfiguration.getClanOwner(args[1]));

										SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat ( "yyyy.MM.dd HH:mm:ss", Locale.KOREA );
										Date currentTime = new Date ();
										String mTime = mSimpleDateFormat.format ( currentTime );
										
										String name = Bukkit.getOfflinePlayer(owner).getName();
										
										Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mail send "+name+" "+ChatColor.GREEN+sender.getName()+ChatColor.WHITE+"님이 당신의 클랜에 가입하셨습니다. "+ChatColor.YELLOW+mTime.toString());
										for(Player p : Bukkit.getOnlinePlayers()) {
											if(p.getUniqueId().equals(owner)) {
												p.sendMessage(ChatColor.GREEN+sender.getName()+ChatColor.WHITE+"님이 당신의 클랜에 가입하셨습니다. "+ChatColor.YELLOW+mTime.toString());
											}
										}
										
									}
									else {
										sender.sendMessage(ChatColor.RED + "이미 클랜에 가입 중입니다.");
									}
								}
								else {
									sender.sendMessage("다시 클랜에 가입하시려면 "+String.valueOf((86400-cooltime/1000))+"초 남았습니다");
								}
							}
							else {
								if (clanConfiguration.getClan((Player) sender) == null) {
									
									clanConfiguration.addClient(args[1], (Player) sender);
									sender.sendMessage(ChatColor.GREEN + "성공적으로 클랜에 가입했습니다.");
									UUID owner = UUID.fromString(clanConfiguration.getClanOwner(args[1]));

									SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat ( "yyyy.MM.dd HH:mm:ss", Locale.KOREA );
									Date currentTime = new Date ();
									String mTime = mSimpleDateFormat.format ( currentTime );

									String name = Bukkit.getOfflinePlayer(owner).getName();
									Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mail send "+name+" "+ChatColor.GREEN+sender.getName()+ChatColor.WHITE+"님이 당신의 클랜에 가입하셨습니다. "+ChatColor.YELLOW+mTime.toString());
									for(Player p : Bukkit.getOnlinePlayers()) {
										if(p.getUniqueId().equals(owner)) {
											p.sendMessage(ChatColor.GREEN+sender.getName()+ChatColor.WHITE+"님이 당신의 클랜에 가입하셨습니다. "+ChatColor.YELLOW+mTime.toString());
										}
									}
								}
								else {
									sender.sendMessage(ChatColor.RED + "이미 클랜에 가입 중입니다.");
								}
							}
							
						}
						else {
							sender.sendMessage(ChatColor.RED+"해당 클랜을 찾을 수 없습니다. 클랜명을 다시 확인해주세요");
						}
						
						break;
					}
				}
				else if(args.length==1) {
					switch(args[0]){
					case "전체랭킹":
						((Player) sender).chat("/clan stats");
						break;
					case "기지":
						((Player) sender).chat("/clan base");
						break;
					case "기지지정":
						((Player) sender).chat("/clan setbase");
						break;
					case "탈퇴":
						((Player) sender).chat("/clan leave");
						this.JoinCooldown.put(((Player) sender).getName(), System.currentTimeMillis());
						break;
					case "삭제":
						((Player) sender).chat("/clan delete");
						break;
					case "클랜원목록":
						((Player) sender).chat("/clan list");
						break;
					}

				}
				else {
					sender.sendMessage(ChatColor.YELLOW+"============================================================");
					sender.sendMessage(ChatColor.YELLOW+"/클랜 생성 <클랜명> : "+ChatColor.AQUA+"클랜을 생성합니다. (100만 $)");
					sender.sendMessage(ChatColor.YELLOW+"/클랜 가입 <클랜명> : "+ChatColor.AQUA+"클랜에 가입합니다.");
					sender.sendMessage(ChatColor.YELLOW+"/클랜 초대 <유저 이름> : "+ChatColor.AQUA+"유저를 클랜에 초대합니다");
					sender.sendMessage(ChatColor.YELLOW+"/클랜 초대수락 <유저 이름> : "+ChatColor.AQUA+"클랜장으로부터 온 초대를 수락합니다");
					sender.sendMessage(ChatColor.YELLOW+"/클랜 추방 <유저> : "+ChatColor.AQUA+"유저를 클랜에서 추방합니다");
					sender.sendMessage(ChatColor.YELLOW+"/클랜 정보 <클랜명> : "+ChatColor.AQUA+"해당 클랜의 정보를 확인합니다");
					sender.sendMessage(ChatColor.YELLOW+"/클랜 전체랭킹 : "+ChatColor.AQUA+"클랜 전체 랭킹을 확인합니다");
					sender.sendMessage(ChatColor.YELLOW+"/클랜 기지 : "+ChatColor.AQUA+"클랜 기지로 이동합니다.");
					sender.sendMessage(ChatColor.YELLOW+"/클랜 기지지정 : "+ChatColor.AQUA+"클랜의 기지를 지정합니다");
					sender.sendMessage(ChatColor.YELLOW+"/클랜 탈퇴 : "+ChatColor.AQUA+"클랜을 탈퇴합니다.");
					sender.sendMessage(ChatColor.YELLOW+"/클랜 삭제 : "+ChatColor.AQUA+"클랜장이 클랜을 삭제합니다.");
					sender.sendMessage(ChatColor.YELLOW+"/클랜 클랜원목록 : "+ChatColor.AQUA+"클랜원의 목록을 확인합니다.");
					sender.sendMessage(ChatColor.YELLOW+"============================================================");
				}
			}
			else if(cmd.getName().equals("JYK") || cmd.getName().equals("경험치구매")) {
			}
			else if(cmd.getName().equals("1") || cmd.getName().equals("INSU")) {
				if(clanConfiguration.getClan((Player) sender).equals("INSU") || sender.isOp()) {
					if(this.INSUCooldown.containsKey(((Player) sender).getUniqueId())) {
						if(System.currentTimeMillis()-INSUCooldown.get(((Player) sender).getUniqueId())>=1200000) {
							((Player) sender).addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING,12000,1));
						}
						else {
							Long left = System.currentTimeMillis()-INSUCooldown.get(((Player) sender).getUniqueId());
							sender.sendMessage(String.valueOf(1200-(left/1000))+"초 남았습니다!");
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
					p.sendMessage(ChatColor.GREEN+"클랜 "+clan+ChatColor.WHITE+"의 특수효과로 인해 배고픔이 "+ChatColor.AQUA+"4"+ChatColor.WHITE+" 만큼 추가되었습니다");
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
					p.sendMessage(ChatColor.GREEN+"클랜 "+clan+ChatColor.WHITE+"의 특수효과로 인해 배고픔이 "+ChatColor.AQUA+"8"+ChatColor.WHITE+" 만큼 추가되었습니다");
					break;
				}
			}
			else if(clan.equals("CNCH") || e.getPlayer().isOp()) {
				if(e.getItem().getTypeId()==391) {
					
					if(this.CNCHCooldown.containsKey(e.getPlayer().getUniqueId())) {
						if(System.currentTimeMillis()-CNCHCooldown.get(e.getPlayer().getUniqueId())>=600000) {
							e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED,120000,3));
							e.getPlayer().sendMessage("CNCH 클랜 능력 적용!");
						}
						else {
							Long left = System.currentTimeMillis()-CNCHCooldown.get(e.getPlayer().getUniqueId());
							e.getPlayer().sendMessage(String.valueOf(1200-(left/1000))+"초 남았습니다!");
						}
					}
					else {
						CNCHCooldown.put(e.getPlayer().getUniqueId(), System.currentTimeMillis());
						e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED,120000,1));
						e.getPlayer().sendMessage("CNCH 클랜 능력 적용!");
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
