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
		.sendMessage(ChatColor.GOLD + ("hcEvent") + ChatColor.DARK_AQUA + " 플러그인 비활성화" + ChatColor.BLACK);
	}

	@Override
	public void onEnable() {
		// TODO Auto-generated method stub
		Bukkit.getConsoleSender()
		.sendMessage(ChatColor.GOLD + ("hcEvent") + ChatColor.AQUA + " 플러그인 활성화" + ChatColor.BLACK);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof ConsoleCommandSender) {
			switch(cmd.getName()) {

			}
		}
		else if (sender instanceof Player) {
			if (cmd.getName().equals("이벤트")) {
				if (args.length >= 2) {
					if (sender.isOp()) {
						switch (args[0]) {
						case "추가":
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
									"broadcast " + args[6] + " &e이벤트&f가 &a추가 &f되었습니다!\n자세한 사항은 &a '/이벤트' &f명령어로 확인해주세요!");
							break;
						case "삭제":
							this.getServer().dispatchCommand(getServer().getConsoleSender(),
									"broadcast " + events.get(Integer.getInteger(args[1])).getStr()
											+ "&e이벤트&f가 &a삭제 &f되었습니다!\n자세한 사항은 &a'/이벤트' &f명령어로 확인해주세요!");
							events.remove(Integer.getInteger(args[1]));
							break;
						}
					}
				} else {
					printEvents(sender);
				}
			}
			else if(cmd.getName().equals("한정드랍")) {
				sender.sendMessage("현재 진행중인 한정드랍은 없습니다.");
				//sender.sendMessage(ChatColor.GOLD+"1. 깃털신발 "+ChatColor.WHITE+": 일반 월드에서의 "+ChatColor.GREEN+"<평범한 좀비>,<평범한 스켈레톤>"+ChatColor.WHITE+"으로부터 획득 가능합니다.");
				//sender.sendMessage(ChatColor.GOLD+"2. 최고 약쟁이의 검 Origin"+ChatColor.WHITE+" : 일반 월드에서의 "+ChatColor.GREEN+"<평범한 좀비>, <평범한 스켈레톤>, <평범한 크리퍼>, <평범한 거미>"+ChatColor.WHITE+"로부터 드랍됩니다.");
				//sender.sendMessage(ChatColor.GOLD+"3. 최고 약쟁이의 검 Common"+ChatColor.WHITE+" : 지옥에서의"+ChatColor.GREEN+" <지옥의 좀비>, <지옥의 스켈레톤>, <평범한 좀비피그맨>"+ChatColor.WHITE+"으로부터 드랍됩니다.");
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
