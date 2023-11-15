package HeavensCraft;

import java.util.Set;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class HCEvent extends JavaPlugin{
	HashMap<Integer, RealEvent> events = new HashMap<>();
//	HashMap<RealEvent, Time > eventTime = new HashMap<>();
	int now = 0;
	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		Bukkit.getConsoleSender()
		.sendMessage(ChatColor.GOLD + ("hcEvent") + ChatColor.DARK_AQUA + " �÷����� ��Ȱ��ȭ" + ChatColor.BLACK);
	}

	@Override
	public void onEnable() {
		// TODO Auto-generated method stub
		Bukkit.getConsoleSender()
		.sendMessage(ChatColor.GOLD + ("hcEvent") + ChatColor.AQUA + " �÷����� Ȱ��ȭ" + ChatColor.BLACK);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof ConsoleCommandSender) {
			switch(cmd.getName()) {

			}
		}
		else if (sender instanceof Player) {
			if (cmd.getName().equals("�̺�Ʈ")) {
				if (args.length >= 2) {
					if (sender.isOp()) {
						switch (args[0]) {
						case "�߰�":
							Time t = new Time(args[1], args[2], args[3], args[4], args[5]);
							String title = args[6];
							if (args.length >= 8) {
								for (int i = 7; i < args.length; i++) {
									title += args[i];
								}
							}
							RealEvent e = new RealEvent(title, now, t);
							events.put(now, e);
							this.getServer().dispatchCommand(getServer().getConsoleSender(),
									"broadcast " + args[6] + " &e�̺�Ʈ&f�� &a�߰� &f�Ǿ����ϴ�!\n�ڼ��� ������ &a '/�̺�Ʈ' &f��ɾ�� Ȯ�����ּ���!");
							break;
						case "����":
							this.getServer().dispatchCommand(getServer().getConsoleSender(),
									"broadcast " + events.get(Integer.getInteger(args[1])).getStr()
											+ "&e�̺�Ʈ&f�� &a���� &f�Ǿ����ϴ�!\n�ڼ��� ������ &a'/�̺�Ʈ' &f��ɾ�� Ȯ�����ּ���!");
							events.remove(Integer.getInteger(args[1]));
							break;
						}
					}
				} else {
					printEvents(sender);
				}
			}
			else if(cmd.getName().equals("�������")) {
				sender.sendMessage("���� �������� ��������� �����ϴ�.");
				//sender.sendMessage(ChatColor.GOLD+"1. ���нŹ� "+ChatColor.WHITE+": �Ϲ� ���忡���� "+ChatColor.GREEN+"<����� ����>,<����� ���̷���>"+ChatColor.WHITE+"���κ��� ȹ�� �����մϴ�.");
				//sender.sendMessage(ChatColor.GOLD+"2. �ְ� �������� �� Origin"+ChatColor.WHITE+" : �Ϲ� ���忡���� "+ChatColor.GREEN+"<����� ����>, <����� ���̷���>, <����� ũ����>, <����� �Ź�>"+ChatColor.WHITE+"�κ��� ����˴ϴ�.");
				//sender.sendMessage(ChatColor.GOLD+"3. �ְ� �������� �� Common"+ChatColor.WHITE+" : ����������"+ChatColor.GREEN+" <������ ����>, <������ ���̷���>, <����� �����Ǳ׸�>"+ChatColor.WHITE+"���κ��� ����˴ϴ�.");
			}
		}
		return false;
	}

	private void printEvents(CommandSender sender) {
		// TODO Auto-generated method stub
		Set<Entry<Integer, RealEvent>> entries = events.entrySet();
		for (Entry<Integer, RealEvent> entry : entries) {
			String str = entry.getValue().getStr()+ " - " + entry.getValue().getTime().toString();
			sender.sendMessage(ChatColor.GREEN+str);
		}
	}
}
