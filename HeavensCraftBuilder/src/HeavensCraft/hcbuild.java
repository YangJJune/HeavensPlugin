package HeavensCraft;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;


public class hcbuild extends JavaPlugin implements Listener {
	
	HashMap<String, Integer> diamond = new HashMap<>();
	HashMap<String,Integer> buildingpoint = new HashMap<>();
	
	@Override
	public void onEnable() {
		this.readFile();
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
            	saveFile();
            }
        }, 0L, 36000L);//1200, 36000
		
		Bukkit.getConsoleSender()
		.sendMessage(ChatColor.GOLD + ("hcBuilder") + ChatColor.AQUA + " �÷����� Ȱ��ȭ" + ChatColor.BLACK);
		Bukkit.getPluginManager().registerEvents(this, this);		
	}
	@Override
	public void onDisable() {
		this.saveFile();
		Bukkit.getConsoleSender()
		.sendMessage(ChatColor.GOLD + ("hcBuilder") + ChatColor.DARK_AQUA + " �÷����� ��Ȱ��ȭ" + ChatColor.BLACK);
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			if(cmd.getName().equals("��������Ʈ")) {
				switch(args[0]) {
				case "����":
				case "����":
					if (buildingpoint.containsKey(sender.getName())) {
						int point = buildingpoint.get(sender.getName());
						sender.sendMessage(
								ChatColor.AQUA+""+ChatColor.BOLD+ "============================================================");
						sender.sendMessage(ChatColor.YELLOW + "�̸� : "+ChatColor.GOLD + sender.getName());
						sender.sendMessage(ChatColor.YELLOW + "���� ���� ����Ʈ : "+ChatColor.WHITE + point);
						String str = "";
						if (point >= 50000) {
							str = ChatColor.DARK_PURPLE + "�׷���" + ChatColor.AQUA + "������";
						} else if (point >= 25000) {
							str = ChatColor.AQUA + "������";
						} else if (point >= 15000) {
							str = ChatColor.RED + "������";
						} else if (point >= 10000) {
							str = ChatColor.GOLD + "������";
						} else if (point >= 5000) {
							str = ChatColor.WHITE + "�����";
						} else if (point >= 1000) {
							str = ChatColor.GREEN + "���ð�";
						} else if (point >= 500) {
							str = ChatColor.YELLOW + "�Ƹ��߾�";
						} else {
							str = ChatColor.WHITE + "�ʺ�";
						}
						sender.sendMessage(ChatColor.YELLOW + "��� : " + str);
						sender.sendMessage(
								ChatColor.AQUA+""+ChatColor.BOLD+ "============================================================");
					} else {
						sender.sendMessage(
								ChatColor.AQUA+""+ChatColor.BOLD+ "============================================================");
						sender.sendMessage(ChatColor.YELLOW + "�̸� : "+ChatColor.GOLD + sender.getName());
						sender.sendMessage(ChatColor.YELLOW + "���� ���� ����Ʈ : "+ChatColor.WHITE+"0");
						String str = "";

						str = ChatColor.WHITE + "�ʺ�";

						sender.sendMessage(ChatColor.YELLOW + "��� : " + str);
						sender.sendMessage(
								ChatColor.AQUA + ""+ChatColor.BOLD+"============================================================");

					}
					break;
				default:
					sender.sendMessage(
							ChatColor.YELLOW + "============================================================");
					sender.sendMessage(ChatColor.YELLOW+"/��������Ʈ ���� : "+ChatColor.AQUA+"��������Ʈ ������ Ȯ���մϴ�"+
							ChatColor.YELLOW + "============================================================");
					break;
				case "�ֱ�":
					if (sender.hasPermission("hcBuilder.judge")) {
						if (this.buildingpoint.containsKey(args[1])) {
							CheckPro(Integer.valueOf(args[2]), buildingpoint.get(args[1])+Integer.valueOf(args[2]), args[1]);
							this.buildingpoint.put(args[1], buildingpoint.get(args[1]) + Integer.valueOf(args[2]));
						} else {
							buildingpoint.put(args[1], Integer.valueOf(args[2]));
						}
					}
					break;
				case "����":
					if (sender.hasPermission("hcBuilder.judge")) {
						if (this.buildingpoint.containsKey(args[1])) {
							this.buildingpoint.put(args[1], buildingpoint.get(args[1]) - Integer.valueOf(args[2]));
							if (buildingpoint.get(args[1]) < 0) {
								buildingpoint.put(args[1], 0);
							}
						} else {
							sender.sendMessage("�� ģ���� ���� ������ ���� ���� �����");
						}
					}
					break;
				}
			}
		}
		else if(sender instanceof ConsoleCommandSender) {
			if(cmd.getName().equals("��������Ʈ")) {
				if(args.length>1) {
					switch(args[0]) {
					case "����":
					case "����":
						sender.sendMessage("�ش� ��ɾ�� �ֿܼ��� ����� �Ұ��մϴ�");
						break;						
					case "�ֱ�":
						if(sender.hasPermission("hcBuilder.judge")) {
							if(this.buildingpoint.containsKey(args[1])) {
								CheckPro(Integer.valueOf(args[2]), buildingpoint.get(args[1])+Integer.valueOf(args[2]), args[1]);
								this.buildingpoint.put(args[1], buildingpoint.get(args[1])+Integer.valueOf(args[2]));
							}
							else {
								buildingpoint.put(args[1], Integer.valueOf(args[2]));
							}
						}
						break;
					case "����":
						if(sender.hasPermission("hcBuilder.judge")) {
							if(this.buildingpoint.containsKey(args[1])) {
								this.buildingpoint.put(args[1], buildingpoint.get(args[1])-Integer.valueOf(args[2]));
								if(buildingpoint.get(args[1])<0) {
									buildingpoint.put(args[1], 0);
								}
							}
							else {
								sender.sendMessage("�� ģ���� ���� ������ ���� ���� �����");
							}
						}
						break;
					}
				}
			}
		}
		return false;
	}
	
	private void CheckPro(int plus, int past, String sender) {
		// TODO Auto-generated method stub
		int chk1, chk2;
		if (past >= 50000) {
			chk1 = 0;
		} else if (past >= 25000) {
			chk1 = 1;
		} else if (past >= 15000) {
			chk1 = 2;
		} else if (past >= 10000) {
			chk1 = 3;
		} else if (past >= 5000) {
			chk1 = 4;
		} else if (past >= 1000) {
			chk1 = 5;
		} else if (past >= 500) {
			chk1 = 6;
		} else {
			chk1 = 7;
		}
		if (past+plus >= 50000) {
			chk2 = 0;
		} else if (past+plus >= 25000) {
			chk2 = 1;
		} else if (past+plus >= 15000) {
			chk2 = 2;
		} else if (past+plus >= 10000) {
			chk2 = 3;
		} else if (past+plus >= 5000) {
			chk2 = 4;
		} else if (past+plus >= 1000) {
			chk2 = 5;
		} else if (past+plus >= 500) {
			chk2 = 6;
		} else {
			chk2 = 7;
		}
		if(chk1 != chk2) {
			switch(chk2) {
			case 0:
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "AdjustBonusClaimBlocks "+sender +" 300");
				break;
			case 1:
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "AdjustBonusClaimBlocks "+sender +" 400");
				break;
			case 2:
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "AdjustBonusClaimBlocks "+sender +" 600");
				break;
			case 3:
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "AdjustBonusClaimBlocks "+sender +" 750");
				break;
			case 4:
				break;
			case 5:
				break;
			case 6:
				break;
				
			}
			
		}
	}
	private void readFile() {
		try {
			File file = new File("C:\\�������\\plugins\\hcBuild\\data.txt");
			//File file = new File("D:\\�� ���� (8)\\�� ���� (2)\\����\\plugins\\hcPVP\\data.txt");
			Scanner scan = new Scanner(file);
			int N = scan.nextInt();
			for(int i=0; i<N; i++) {
				String name = scan.next();
				int point = scan.nextInt();
				
				this.buildingpoint.put(name, point);
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
            File file = new File("C:\\�������\\plugins\\hcBuild\\data.txt");
        	//File file = new File("D:\\�� ���� (8)\\�� ���� (2)\\����\\plugins\\hcPVP\\data.txt");
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            int size = buildingpoint.size();
            
            if(file.isFile() && file.canWrite()){
            	bufferedWriter.write(String.valueOf(size));
                	
                	Set<Entry<String, Integer>> entries = buildingpoint.entrySet();
    				for (Entry<String, Integer> entry : entries) {
                		//���๮�ھ���
                		bufferedWriter.newLine();
                		bufferedWriter.write(entry.getKey());
    					String str = " "+entry.getValue();
    					bufferedWriter.write(str);
    				}
                bufferedWriter.close();
            }
        }catch (IOException e) {
            System.out.println(e);
        }
	}
	
}
