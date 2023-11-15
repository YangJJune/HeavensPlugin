package HeavensCraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class hcTyping extends JavaPlugin implements Listener {
	
	public static boolean onGame=false;
	List<Player> matching = new ArrayList<>();
	List<String> words = new ArrayList<>();
	List<String> nowWords = new ArrayList<>();
	@Override
	public void onEnable() {
		Bukkit.getConsoleSender()
		.sendMessage(ChatColor.GOLD + ("hcTyping") + ChatColor.AQUA + " 플러그인 활성화" + ChatColor.BLACK);
		Bukkit.getPluginManager().registerEvents(this, this);		
		words.add("좀비");
		words.add("스켈레톤");
		words.add("크리퍼");
		words.add("엔더맨");
		words.add("어드민");
		words.add("앵무새");
		words.add("철골렘");
		words.add("눈사람");
		words.add("스켈레톤좀비");
	}
	@Override
	public void onDisable() {
		Bukkit.getConsoleSender()
		.sendMessage(ChatColor.GOLD + ("hcTyping") + ChatColor.DARK_AQUA + " 플러그인 비활성화" + ChatColor.BLACK);
	}
	@EventHandler
	private void onChat(AsyncPlayerChatEvent e) {
		//if(e.getMessage().equals()) {			
		//}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {	
			if(cmd.equals("채팅전쟁")) {
				if(args.length>=1) {
					switch(args[0]) {
					case "매칭":
						if(matching.contains(sender))
							sender.sendMessage("이미 매칭 중입니다");
						else {
							sender.sendMessage("매칭을 시작합니다");
							matching.add((Player) sender);
						}
						
						if(matching.size()>=2) {
							Start();
						}
						
						break;
					case "매칭취소":
						if(matching.contains(sender)) {
							sender.sendMessage("매칭을 취소합니다");
							matching.remove((Player)sender);
						}
						else {
							sender.sendMessage("현재 매칭 상태가 아닙니다");
						}
						break;
					case "정보":
						
						break;
					}
				}
			}
		}
		return false;
	}
	
	private void Start(){
		Player A;
		Player B;
		A=matching.get(0);
		matching.remove(0);
		
		B=matching.get(0);
		matching.remove(0);
		
		this.getServer().dispatchCommand(getServer().getConsoleSender(), "warp chatting1 "+A.getName());
		this.getServer().dispatchCommand(getServer().getConsoleSender(), "warp chatting2 "+B.getName());
	}
}
