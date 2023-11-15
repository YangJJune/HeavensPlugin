package HeavensCraft;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class hcZombieSurvival extends JavaPlugin implements Listener{
	public static List<Player> list = new ArrayList<>();
	public static List<Player> DeathPlayer = new ArrayList<>();
	public static HashMap<Player, Integer> kill = new HashMap<>();
	public static HashMap<UUID, List<Time2>> record = new HashMap<>();
	public static HashMap<UUID, Long> startingTime = new HashMap<>();
	public static long StartTime;
	public static int countP;
	public boolean canJoin = true;
	public static int wave=0;
	
	private final int minX=969;
	private final int maxX=1103;
	private final int minZ=-294;
	private final int maxZ=-160;
	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		this.saveFile();
		Bukkit.getConsoleSender()
		.sendMessage(ChatColor.GOLD + ("hcEvent") + ChatColor.DARK_AQUA + " �÷����� ��Ȱ��ȭ" + ChatColor.BLACK);
	}
	
	
	@Override
	public void onEnable() {
		// TODO Auto-generated method stub
		Bukkit.getConsoleSender()
		.sendMessage(ChatColor.GOLD + ("hcEvent") + ChatColor.AQUA + " �÷����� Ȱ��ȭ" + ChatColor.BLACK);
		this.readFile();
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
            	if(wave>=1) {
            		
            		Random rand = new Random();
            		int x, z, spawnY;
            		Location l;
					for (int i = 0; i < wave*4; i++) {
						x = rand.nextInt(maxX - minX); // 134
						z = rand.nextInt(maxZ - minZ); // 134

						x += minX;// 969+134
						z += minZ;// -294+134
						l = new Location(Bukkit.getWorld("zombieland"), x, 0, z);
						spawnY = l.getWorld().getHighestBlockYAt(x, z);
						l.setY(spawnY);

						Bukkit.getWorld("zombieland").strikeLightning(l);
					}
            	}
            }
        }, 1200L, 200L);//1200, 36000
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
            	canJoin=true;
            	countP=0;
            	list.clear();
            	kill.clear();
            	DeathPlayer.clear();
            	Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "broadcast ���� �����̹��� �����մϴ�! �����Ͻ÷��� &b/���� ���� &a�� �Է����ּ���!");            	
            	StartTime = System.currentTimeMillis();
            	Start();
            }
        }, 1200L, 36000L);//1200, 36000
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
            	canJoin=false;
            	Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "broadcast 1�� �� ���� �����̹��� ���۵˴ϴ�! "); 
            }
        }, 0L, 36000L);//1200, 36000
	}
	
	public void Start() {
		BukkitScheduler s = Bukkit.getScheduler();
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mm m kill VZOMBIE2 ");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mm m kill SZOMBIE2 ");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mm m kill ZombieFire2 ");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mm m kill TankerZombie2 ");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mm m kill MuscleZombie2 ");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mm m kill BlindZombie2 ");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mm m kill ExplodeZombie2 ");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mm s set g:VZombie maxmobs 10");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mm s set g:SZOMBIE maxmobs 10");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mm s set ExplodeZombie maxmobs 2");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mm s set g:FireZombie maxmobs 0");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mm s set g:BlindZombie maxmobs 0");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mm s set MuscleZombie maxmobs 0");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mm s set TankerZombie maxmobs 0");
		wave =0;
		//SZOMBIE2 ZombieFire2 TankerZombie2 MuscleZombie2 SpitZombie2 BlindZombie2 ExplodeZombie2
		s.runTaskLater(this, new Runnable() {
			@Override
			public void run() {
				for(Player p : list) {
					p.sendTitle(ChatColor.RED+"���̺� 2","");
				}
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mm s set g:VZombie maxmobs 0");
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mm s set g:SZOMBIE maxmobs 5");
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mm s set g:FireZombie maxmobs 5");
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mm s set g:BlindZombie maxmobs 2");
				wave = 1;
				return;
			}
		}, 4000L); //6000
		s.runTaskLater(this, new Runnable() {
			@Override
			public void run() {
				for(Player p : list) {
					p.sendTitle(ChatColor.RED+"���̺� 3","");
				}
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mm s set ExplodeZombie maxmobs 20");
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mm s set g:SZOMBIE maxmobs 0");
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mm s set g:FireZombie maxmobs 8");
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mm s set g:BlindZombie maxmobs 3");
				wave = 2;
				return;
			}
		}, 8000L);
		s.runTaskLater(this, new Runnable() {
			@Override
			public void run() {
				for(Player p : list) {
					p.sendTitle(ChatColor.RED+"���̺� 4","");
					}
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mm s set ExplodeZombie maxmobs 30");
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mm s set g:FireZombie maxmobs 7");
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mm s set g:BlindZombie maxmobs 3");
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mm s set MuscleZombie maxmobs 30");
				wave = 3;
				return;
			}
		}, 12000L);
		s.runTaskLater(this, new Runnable() {
			@Override
			public void run() {
				for(Player p : list) {
					p.sendTitle(ChatColor.RED+"���̺� 5","");
				}
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mm s set ExplodeZombie maxmobs 40");
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mm s set g:FireZombie maxmobs 10");
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mm s set g:BlindZombie maxmobs 4");
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mm s set MuscleZombie maxmobs 30");
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mm s set TankerZombie maxmobs 8");
				wave = 4;
				return;
			}
		}, 16000L);
		s.runTaskLater(this, new Runnable() {
			@Override
			public void run() {
				for(Player p : list) {
					p.sendTitle(ChatColor.RED+"���̺� F","");
				}
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mm s set ExplodeZombie maxmobs 40");
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mm s set g:FireZombie maxmobs 10");
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mm s set g:BlindZombie maxmobs 4");
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mm s set MuscleZombie maxmobs 50");
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mm s set TankerZombie maxmobs 16");
				wave = 5;
				return;
			}
		}, 20000L);
	}
	@EventHandler
	private void onDeath(PlayerDeathEvent e) {
		if(list.contains(e.getEntity())){
			Player p = e.getEntity();
			DeathPlayer.add(p);
			
			long T = System.currentTimeMillis()-this.startingTime.get(p.getUniqueId());
			int sec = (int) (T/1000);
			int score =0;
			
			if(kill.containsKey(p))
				score = sec *5 + kill.get(p);
			else
				score = sec*5;
			
			int min = sec/60;
			sec = sec % 60;
			min %= 60;		
			list.remove(p);
			e.getEntity().sendMessage("����! ����� "+min+"��"+sec+"�� �Դϴ�!");
			System.out.println(p.getName()+"����! ����� "+min+"��"+sec+"�� �Դϴ�!");
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "broadcast ���� �����̹�, "+p.getName()+" ���� ��� : "+min+"�� "+sec+"��"+" ���� : "+score);
			Time2 t = new Time2(min,sec,score);
			List<Time2> a = record.get(p.getUniqueId());
			a.add(t);
			
			record.put(p.getUniqueId(), a);
		}
	}
	
	@EventHandler
	private void onEDeath(EntityDeathEvent e) {
		if(e.getEntity().getWorld().getName().equals("zombieland")) {
			if(kill.containsKey(e.getEntity().getKiller()))
				kill.put(e.getEntity().getKiller(), kill.get(e.getEntity().getKiller())+1);
			else
				kill.put(e.getEntity().getKiller(), 1);
		}
	}
	
	@EventHandler
	private void onDamage(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player) {
			if(e.getEntity().getWorld().getName().equals("zombieland") && e.getCause()==DamageCause.LIGHTNING) {
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "kill "+e.getEntity().getName());
			}
		}
	}
	@EventHandler
	private void onWorldChange(PlayerChangedWorldEvent e) {
		 if(e.getPlayer().getWorld().getName().equals("zombieland")) {
			 if(!(list.contains(e.getPlayer()))) {
				 Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "spawn "+e.getPlayer());
				 e.getPlayer().sendMessage("�� ���� /���� ����  �θ� ������ �� �ֽ��ϴ�!");
			 }
		 }
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (cmd.getName().equals("����")) {
				if(args.length>=1) {
					switch(args[0]) {
					case "����":
						if(DeathPlayer.contains(sender)) {
							sender.sendMessage("�̹� ���� �����̹������� �����̽��ϴ�! 30�� ���� ���� �����̹��� �������ּ���!");
						}
						else if(countP>=10) {
							sender.sendMessage("�ִ� �ο��� �Ѿ����ϴ�! ���� �����̹��� �������ּ���!");
						}
						else if(this.canJoin==false) {
							sender.sendMessage("������ ������ �Ұ����մϴ�! ���� �����̹��� ���۵Ǹ� �������ּ���!");
						}
						else {
							int size=0;
							for(ItemStack i : ((Player) sender).getInventory().getContents()) {
								if(i != null) {
									size++;
								}
							}
							if(size<=10) {
								kill.put((Player) sender, 0);
								list.add((Player) sender);
								this.countP++;
								Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mv tp "+sender.getName()+" zombieland");
								this.startingTime.put(((Player) sender).getUniqueId(), System.currentTimeMillis());
							}
							else if(((Player) sender).getInventory().contains(Material.ELYTRA)){
								sender.sendMessage("�ѳ����� �����Ͻ� �� �����ϴ�!");
							}
							else {
								sender.sendMessage("�������� ���� ���� �ִ� 10 ���� ������ ���˴ϴ�! �������� ���ּ���!");
							}
						}
						break;
					case "Ȯ��":
						if(args.length>=2) {
							UUID ID=null;
							for(OfflinePlayer player:Bukkit.getServer().getOfflinePlayers()) {
								if(args[1].equals(player.getName())) {
									ID=player.getUniqueId();
								}
							}
							if(ID==null) {
								sender.sendMessage(ChatColor.RED+"�ش� �÷��̾�� �������� �ʽ��ϴ�");
							}
							else if(!(record.containsKey(ID))) {
								sender.sendMessage(ChatColor.RED+"�ش� �÷��̾�� �� ���� ���� �����̹��� �÷������� �ʾҽ��ϴ�");
							}
							else {
								List<Time2> a = this.record.get(ID);

								sender.sendMessage(ChatColor.GREEN+"<���� �����̹� ���>");
								for(Time2 i : a) {
									sender.sendMessage(ChatColor.YELLOW+i.toString());
								}
							}
							
						}
						else {
							List<Time2> a = this.record.get(((Player) sender).getUniqueId());

							sender.sendMessage(ChatColor.GREEN+"<���� �����̹� ���>");
							for(Time2 i : a) {
								sender.sendMessage(ChatColor.YELLOW+i.toString());
							}
						}
						break;
					}
				}
			}
		}
		return false;
	}
	
	private void readFile() {
		try {
			File file = new File("C:/����/plugins/hcZombie/data.txt");
			Scanner scan = new Scanner(file);
			int N = scan.nextInt();
			for(int i=0; i<N; i++) {
				String ID = scan.next();
				int min = scan.nextInt();
				int s = scan.nextInt();
				int score = scan.nextInt();
				Time2 t = new Time2(min,s,score);
				if(record.containsKey(UUID.fromString(ID))) {
					List<Time2> a = record.get(UUID.fromString(ID));
					a.add(t);					
					record.put(UUID.fromString(ID), a);
				}
				else {
					List<Time2> a = new ArrayList<>();
					a.add(t);
					record.put(UUID.fromString(ID), a);
				}				
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
            File file = new File("C:/����/plugins/hcZombie/data.txt");
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            int size=0;
            for (UUID u : record.keySet()) {
            	List<Time2> a = record.get(u);
            	size+=a.size();
            }
            
            if(file.isFile() && file.canWrite()){
            	bufferedWriter.write(String.valueOf(size));
    				for (UUID u : record.keySet()) {
                		//���๮�ھ���
    					List<Time2> a = record.get(u);
    					for(Time2 i : a) {
    						bufferedWriter.newLine();
                			bufferedWriter.write(u.toString()+" "+i.getMinute()+" "+i.getSecond()+" "+i.getScore());                			
    					}
    				}
                bufferedWriter.close();
            }
        }catch (IOException e) {
            System.out.println(e);
        }
	}
	
}
