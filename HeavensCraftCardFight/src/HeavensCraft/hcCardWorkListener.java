package HeavensCraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.util.Vector;

import com.shampaggon.crackshot.events.WeaponDamageEntityEvent;

import net.md_5.bungee.api.ChatColor;

public class hcCardWorkListener implements Listener{
	public static HashMap<Player,Long> alphaCool = new HashMap<>();
	final int alphacool = 15;
	public static int alpha_cnt = 0;
	/*@EventHandler
	private void onDamaged(EntityDamageByEntityEvent e) {
		if(e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if(hcCardMain.cards.containsKey(p)) {
				if(hcCardMain.cards.get(p)==3) {
					hcCardMain.battleTime.put(p, System.currentTimeMillis());
				}
			}
		}
		if(e.getDamager() instanceof Player) {
			Player p =(Player) e.getDamager();
			
			if(hcCardMain.cards.containsKey(p)) {
				switch(hcCardMain.cards.get(p)) {
				case 3:
					hcCardMain.battleTime.put(p, System.currentTimeMillis());
					break;
				case 12:
					Entity mob = e.getEntity();
					if(mob instanceof Player) {
						
					}
					else {
						Vector vector=new Vector(0,5,0);
						Location location = mob.getLocation();
						location.add(vector);
						mob.teleport(location);	
						p.setVelocity(vector);
					}
					break;
				}
				
			}
		}
	}*/
	
	@EventHandler
	private void onShoot(WeaponDamageEntityEvent e) {
		if(e.getVictim() instanceof Player) {
			Player p =(Player) e.getVictim();
			if(hcCardMain.cards.containsKey(p)) {
				if(hcCardMain.cards.get(p)==3) {
					hcCardMain.battleTime.put(p, System.currentTimeMillis());
				}
			}
		}
		
		if(e.getDamager() instanceof Player) {
			Player p =(Player) e.getDamager();
			if(hcCardMain.cards.containsKey(p)) {
				if(hcCardMain.cards.get(p)==3) {
					hcCardMain.battleTime.put(p, System.currentTimeMillis());
				}
			}
		}
	}
	@EventHandler
	private void onPlayerMove(PlayerMoveEvent e) {
		if(hcCardMain.cards.containsKey(e.getPlayer())) {
			Player p = e.getPlayer();
			if(hcCardMain.cards.get(p)==3) {
				if((System.currentTimeMillis()-hcCardMain.battleTime.get(p))/1000 >= 5) {
					if(!(p.hasPotionEffect(PotionEffectType.SPEED))) {
						e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED,Integer.MAX_VALUE, 1));
					}
				}
				else {
					p.removePotionEffect(PotionEffectType.SPEED);
				}
			}
		}
	}
	/*@EventHandler
	private void onFurnace(InventoryClickEvent e) {
		if(e.getInventory().getName().equals("container.furnace")) {
			if(e.getWhoClicked() == null || e.getCurrentItem() == null) {
				return;
			}
			if (hcCardMain.cards.get(e.getWhoClicked()) == null) {
				return;
			}
			Player p = (Player) e.getWhoClicked();
			if (hcCardMain.cards.get(p) == 0) {
				//if (p.getInventory().firstEmpty() != -1) {
					ItemStack is = e.getCurrentItem();
					switch (e.getCurrentItem().getTypeId()) {
					case 14:
						e.setCancelled(true);
						p.sendMessage(ChatColor.GOLD + "용광로의 장인" + ChatColor.WHITE + "의 " + ChatColor.GREEN + "특수효과"
								+ ChatColor.WHITE + "가 발동되었습니다.");
						p.getOpenInventory().getTopInventory().setItem(0, null);
						p.getOpenInventory().getBottomInventory().removeItem(e.getCurrentItem());
						p.updateInventory();
						e.getWhoClicked().getInventory().addItem(new ItemStack(Material.GOLD_INGOT, is.getAmount()));
						p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
						p.updateInventory();
						break;
					case 15:

						p.sendMessage(ChatColor.GOLD + "용광로의 장인" + ChatColor.WHITE + "의 " + ChatColor.GREEN + "특수효과"
								+ ChatColor.WHITE + "가 발동되었습니다.");
						p.getOpenInventory().getTopInventory().setItem(0, null);
						p.getOpenInventory().getBottomInventory().removeItem(e.getCurrentItem());
						e.getWhoClicked().getInventory().addItem(new ItemStack(Material.IRON_INGOT, is.getAmount()));
						p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
						p.updateInventory();
						break;
					case 16:

						p.sendMessage(ChatColor.GOLD + "용광로의 장인" + ChatColor.WHITE + "의 " + ChatColor.GREEN + "특수효과"
								+ ChatColor.WHITE + "가 발동되었습니다.");
						p.getOpenInventory().getTopInventory().setItem(0, null);
						p.getOpenInventory().getBottomInventory().removeItem(e.getCurrentItem());
						e.getWhoClicked().getInventory().addItem(new ItemStack(Material.COAL, is.getAmount()));
						p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
						p.updateInventory();
						break;
					case 56:
						p.sendMessage(ChatColor.GOLD + "용광로의 장인" + ChatColor.WHITE + "의 " + ChatColor.GREEN + "특수효과"
								+ ChatColor.WHITE + "가 발동되었습니다.");
						p.getOpenInventory().getTopInventory().setItem(0, null);
						p.getOpenInventory().getBottomInventory().removeItem(e.getCurrentItem());
						e.getWhoClicked().getInventory().addItem(new ItemStack(Material.DIAMOND, is.getAmount()));
						p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
						p.updateInventory();
						break;
					case 129:

						p.sendMessage(ChatColor.GOLD + "용광로의 장인" + ChatColor.WHITE + "의 " + ChatColor.GREEN + "특수효과"
								+ ChatColor.WHITE + "가 발동되었습니다.");
						p.getOpenInventory().getTopInventory().setItem(0, null);
						p.getOpenInventory().getBottomInventory().remove(e.getCurrentItem());
						e.getWhoClicked().getInventory().addItem(new ItemStack(Material.EMERALD, is.getAmount()));
						p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
						p.updateInventory();
						break;
					}
				//}
				//else {
				//	p.sendMessage(ChatColor.RED+"인벤토리에 공간을 1자리 이상 비워주세요!");
				//}
			}
		}
	}*/
	@EventHandler
	private void onDamage(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if(hcCardMain.cards.containsKey(p)) {
				if(e.getCause()==DamageCause.FALL) {
					switch(hcCardMain.cards.get(p)) {
					case 6:
					case 12:
						e.setCancelled(true);
						
						break;
					}
				}
			}
		}
	}
	
	
	
	@EventHandler
	private void onBreak(BlockBreakEvent e) {
		if (hcCardMain.cards.containsKey(e.getPlayer())) {
			if (hcCardMain.cards.get(e.getPlayer()) == 5) {
				if (e.getBlock().getTypeId() == 1) {
					Random rand = new Random();
					int i = rand.nextInt(100);
					if (i < 15) {
						i = rand.nextInt(100);
						if (i < 10) {
							e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(),
									new ItemStack(Material.DIAMOND, 1));
						} else if (i < 20) {
							e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(),
									new ItemStack(Material.GOLD_ORE, 1));
						} else if (i < 40) {
							e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(),
									new ItemStack(Material.REDSTONE, 2));
						} else if (i < 65) {
							e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(),
									new ItemStack(Material.IRON_ORE, 1));
						} else if (i < 100) {
							e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(),
									new ItemStack(Material.COAL, 1));
						}
					}
				}
			}
		}
	}
	@EventHandler
	private void onClick(PlayerInteractEvent e) {
		Player p=e.getPlayer();
		if(hcCardMain.cards.containsKey(p)) {
			if(hcCardMain.cards.get(p)==8) {
				if(e.getAction() == Action.RIGHT_CLICK_BLOCK){
					//Right Click Block
					ItemStack item = e.getItem();
					Block block = e.getClickedBlock();

					if (item == null || block == null){
						//If Item in hand is null, ignore
						return;
					}
					
					int y=block.getY();
					int x, z;
					x= block.getX();
					z=block.getZ();
					boolean chk = false;
					for (int i = x - 1; i <= x + 1; i++) {
						for (int k = z - 1; k <= z + 1; k++) {
							block = new Location(p.getWorld(),i,y,k).getBlock();
							if (item.getType() == Material.INK_SACK && item.getDurability() == 15){
								if(block.getType()==Material.CROPS) {
									block.setData((byte) 7);
									chk=true;
								}
								else {
									Material m = block.getType();
									switch(m) {
									case CARROT:
										//Grow carrots
										block.setData((byte) 7);
										chk=true;
										break;
									case MELON_STEM:
										//Mature melon stem
										block.setData((byte) 7);
										chk=true;
										break;
									case POTATO:
										//Grow potatoes
										block.setData((byte) 7);
										chk=true;
										break;
									case PUMPKIN_STEM:
										//Mature pumpkin stem
										block.setData((byte) 7);
										chk=true;
										break;
									}
								}
							}
						}
					}
					if(chk==true) {
						if((p.getGameMode()) != GameMode.CREATIVE){
							//If Survival, remove 1 bonemeal
							item.setAmount(item.getAmount() - 1 );
							if(item.getAmount() == 0){
								//If run out of bonemeal, remove from inventory
								e.getPlayer().setItemInHand(null);
							}
						}
					}
				}
				
			}
		}
	}
	

	@EventHandler
	private void onInteract(PlayerInteractEvent e) {
		if (e.getItem() != null) {
			if (e.getItem().getTypeId() == 403
					&& (ChatColor.stripColor(e.getItem().getItemMeta().getDisplayName()).equals("일격필살")||ChatColor.stripColor(e.getItem().getItemMeta().getDisplayName()).equals("김여행의 일격필살"))) {
				this.alpha_cnt = 0;
				Player sender = e.getPlayer();
				if (this.alphaCool.containsKey(e.getPlayer())) {
					long remainSec = (System.currentTimeMillis() - alphaCool.get(sender)) / 1000;
					if (remainSec >= this.alphacool) {
						final List<EntityType> MonsterType = new ArrayList<EntityType>() {
							{
								this.add(EntityType.BAT);
								this.add(EntityType.BLAZE);
								this.add(EntityType.CAVE_SPIDER);
								this.add(EntityType.CREEPER);
								this.add(EntityType.ELDER_GUARDIAN);
								this.add(EntityType.ENDER_DRAGON);
								this.add(EntityType.ENDERMAN);
								this.add(EntityType.ENDERMITE);
								this.add(EntityType.EVOKER);
								this.add(EntityType.GHAST);
								this.add(EntityType.GIANT);
								this.add(EntityType.GUARDIAN);
								this.add(EntityType.HUSK);
								this.add(EntityType.ILLUSIONER);
								this.add(EntityType.MAGMA_CUBE);
								this.add(EntityType.PIG_ZOMBIE);
								this.add(EntityType.SKELETON);
								this.add(EntityType.SPIDER);
								this.add(EntityType.SLIME);
								this.add(EntityType.SNOWMAN);
								this.add(EntityType.STRAY);
								this.add(EntityType.VEX);
								this.add(EntityType.VINDICATOR);
								this.add(EntityType.WITCH);
								this.add(EntityType.WITHER);
								this.add(EntityType.WITHER_SKELETON);
								this.add(EntityType.WITHER_SKULL);
								this.add(EntityType.ZOMBIE);
								this.add(EntityType.PLAYER);
								this.add(EntityType.ZOMBIE_VILLAGER);
							}
						};

						boolean chk;
						int index = 0;
						List<Entity> DamageList = new ArrayList<>();

						Player user = e.getPlayer();
						for (int k = 0; k < 8; k++) {
							chk = false;
							List<Entity> entityList = user.getNearbyEntities(5, 2, 5);

							for (int i = 0; i < entityList.size(); i++) {
								if (MonsterType.contains(entityList.get(i).getType())
										&& !(DamageList.contains(entityList.get(i)))) {
									chk = true;
									DamageList.add(entityList.get(i));
									index = i;
									break;
								}
							}

							if (chk == false) {
								user.setNoDamageTicks(1);
								break;
							}
						}

						this.alpha_cnt = 0;
						for (int i = 0; i < DamageList.size(); i++) {
							Bukkit.getScheduler().runTaskLater(hcCardMain.getPlugin, new Runnable() {
								@Override
								public void run() {
									user.teleport(DamageList.get(alpha_cnt).getLocation());
									alpha_cnt++;
								}
							}, ((long) (4 * i)));
						}
						user.setNoDamageTicks(4 * DamageList.size());
						Bukkit.getScheduler().runTaskLater(hcCardMain.getPlugin, new Runnable() {
							@Override
							public void run() {
								for (int j = 0; j < DamageList.size(); j++) {
									LivingEntity l = (LivingEntity) DamageList.get(j);
									l.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, l.getLocation(), 1);
									/*if(l instanceof Player) {
										e.getPlayer().sendMessage(l.getName());
										Player p = (Player) l;
										p.addPotionEffect(new PotionEffect(PotionEffectType.HARM,1,0));										
									}
									else*/
										l.damage(20.0, e.getPlayer());
								}
							}
						}, ((long) DamageList.size() * 4));
						alphaCool.put((Player) sender, System.currentTimeMillis());
					} else {
						sender.sendMessage("남은 쿨타임 : " + String.valueOf(this.alphacool - remainSec));
					}
				} else {
					hcCardWork.onXRayPlus((Player) sender);
					alphaCool.put((Player) sender, System.currentTimeMillis() - this.alphacool * 1000);
				}
			}
		}
	}
}
