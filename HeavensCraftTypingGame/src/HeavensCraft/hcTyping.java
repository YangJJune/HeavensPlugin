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
		.sendMessage(ChatColor.GOLD + ("hcTyping") + ChatColor.AQUA + " �÷����� Ȱ��ȭ" + ChatColor.BLACK);
		Bukkit.getPluginManager().registerEvents(this, this);		
		words.add("����");
		words.add("���̷���");
		words.add("ũ����");
		words.add("������");
		words.add("����");
		words.add("�޹���");
		words.add("ö��");
		words.add("�����");
		words.add("���̷�������");
	}
	@Override
	public void onDisable() {
		Bukkit.getConsoleSender()
		.sendMessage(ChatColor.GOLD + ("hcTyping") + ChatColor.DARK_AQUA + " �÷����� ��Ȱ��ȭ" + ChatColor.BLACK);
	}
	@EventHandler
	private void onChat(AsyncPlayerChatEvent e) {
		//if(e.getMessage().equals()) {			
		//}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {	
			if(cmd.equals("ä������")) {
				if(args.length>=1) {
					switch(args[0]) {
					case "��Ī":
						if(matching.contains(sender))
							sender.sendMessage("�̹� ��Ī ���Դϴ�");
						else {
							sender.sendMessage("��Ī�� �����մϴ�");
							matching.add((Player) sender);
						}
						
						if(matching.size()>=2) {
							Start();
						}
						
						break;
					case "��Ī���":
						if(matching.contains(sender)) {
							sender.sendMessage("��Ī�� ����մϴ�");
							matching.remove((Player)sender);
						}
						else {
							sender.sendMessage("���� ��Ī ���°� �ƴմϴ�");
						}
						break;
					case "����":
						
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
