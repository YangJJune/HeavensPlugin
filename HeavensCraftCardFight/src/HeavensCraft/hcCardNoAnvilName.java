package HeavensCraft;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;


public class hcCardNoAnvilName implements Listener{
	
	
	@EventHandler
	private void onClick(PrepareAnvilEvent e) {
		if(e.getInventory().getItem(0)!=null) {
			ItemStack is = e.getInventory().getItem(0);
			if(is.getTypeId()==403 && is.getItemMeta().isUnbreakable()) {
				e.setResult(is);
			}
		}
	}
}
