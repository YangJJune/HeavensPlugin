package HeavensCraft;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import com.mewin.WGRegionEvents.events.RegionEnterEvent;
import com.mewin.WGRegionEvents.events.RegionLeaveEvent;

public class HCRegionMain extends JavaPlugin implements Listener{
	@Override
	public void onEnable() {
		Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + ("HCGun") + ChatColor.AQUA + " 플러그인 활성화" + ChatColor.BLACK);
		Bukkit.getPluginManager().registerEvents(this, this);
	}
	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + ("HCGun") + ChatColor.DARK_AQUA + " 플러그인 비활성화" + ChatColor.BLACK);
	}

	/*@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		Block b = e.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN);
		if(b.getTypeId() == 251 && b.getData()==15) {
			System.out.println("끄덕");
		}
	}*/
	@EventHandler(priority = EventPriority.NORMAL)
	public void onRegionEnter(RegionEnterEvent e){
		if(e.getRegion().getId().contains("road")) {
			/*Player p=e.getPlayer();
			p.setWalkSpeed(p.getWalkSpeed()*3);*/
			e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED,Integer.MAX_VALUE, 10));
		}
		if(e.getRegion().getId().contains("TESTYSJ")) {
			e.getPlayer().sendMessage("dd");
			/*Player p=e.getPlayer();
			p.setWalkSpeed(p.getWalkSpeed()*3);*/
			e.getPlayer().setGravity(false);
		}
	}
	

	@EventHandler(priority = EventPriority.NORMAL)
	public void onRegionLeave(RegionLeaveEvent e){
		if(e.getRegion().getId().contains("road")) {
			/*Player p=e.getPlayer();
			p.setWalkSpeed(p.getWalkSpeed()/3);*/
			e.getPlayer().removePotionEffect(PotionEffectType.SPEED);
		}
		if(e.getRegion().getId().contains("TESTYSJ")) {
			/*Player p=e.getPlayer();
			p.setWalkSpeed(p.getWalkSpeed()*3);*/
			e.getPlayer().setGravity(true);
		}
	}
	
	@EventHandler
	public void onWorldmove(PlayerChangedWorldEvent e) {
		e.getPlayer().removePotionEffect(PotionEffectType.SPEED);
	}
}
