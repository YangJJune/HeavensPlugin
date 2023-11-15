package HeavensCraft;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Recipe extends JavaPlugin{
	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		Bukkit.getConsoleSender()
		.sendMessage(ChatColor.GOLD + ("hcCustomRecipe") + ChatColor.DARK_AQUA + " 플러그인 비활성화" + ChatColor.BLACK);
	}

	@Override
	public void onEnable() {
		// TODO Auto-generated method stub
		Bukkit.getConsoleSender()
		.sendMessage(ChatColor.GOLD + ("hcCustomRecipe") + ChatColor.AQUA + " 플러그인 활성화" + ChatColor.BLACK);
	}
	
	
}
