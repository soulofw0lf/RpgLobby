package com.vartala.soulofw0lf.rpglobby;





import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;




public class RpgLobby extends JavaPlugin {

	private RpgLobby plugin;

	public void loadInv(Player player){


		if(getConfig().getString(player.getName() + ".Armor-Inventory-Do-Not-Edit") != null) {
			Inventory a = InventoryUtil.fromBase64(getConfig().getString(player.getName() + ".Armor-Inventory-Do-Not-Edit"));
			if(a != null){


				player.getInventory().setArmorContents(a.getContents());

			}
		}
		if(getConfig().getString(player.getName() + ".Inventory-Do-Not-Edit") != null) {
			Inventory i = InventoryUtil.fromBase64(getConfig().getString(player.getName() + ".Inventory-Do-Not-Edit"));
			if(i != null){


				player.getInventory().setContents(i.getContents());

			}
		}
	}


	public void saveInv(Player player){

		Inventory storage = InventoryUtil.getArmorInventory(player.getInventory());
		Inventory GlobalInv = InventoryUtil.getContentInventory(player.getInventory());
		getConfig().set(player.getName() + ".Inventory-Do-Not-Edit", InventoryUtil.toBase64(GlobalInv));
		getConfig().set(player.getName() + ".Armor-Inventory-Do-Not-Edit", InventoryUtil.toBase64(storage));

	}

	@Override
	public void onEnable(){
		getCommand("lobby").setExecutor(new LobbyHandler(this));
		plugin = this;
		saveDefaultConfig();
		if (!(this.getConfig().contains("PerLobbyPermissions")));{
			this.getConfig().createSection("PerLobbyPermissions");
			this.getConfig().set("PerLobbyPermissions", false);
			saveConfig();
		}
		if (!(this.getConfig().contains("newbies"))){
			this.getConfig().createSection("newbies");
			saveConfig();
		}
		getLogger().info("RPG Lobby onEnable has been invoked!");
	}
	@Override
	public void onDisable(){
		getLogger().info("RPG Lobby onDisable has been invoked!");
	}



	public void enterLobby(Player p, Location loc1, String lobbyname){
		final Player player = p;
		final Location loc = loc1;
		final String name = lobbyname;

		new BukkitRunnable(){



			int count = getConfig().getInt("Lobby." + name + ".Time");

			@Override
			public void run(){



				player.sendMessage( "Wait " + count + " Seconds." );
				count--;
				if (player.getLocation().getX() != loc.getX()){
					player.sendMessage("Cancelled entering lobby, don't move!");
					cancel();

				}
				if (player.getLocation().getZ() != loc.getZ()){
					player.sendMessage("Cancelled entering lobby, don't move!");
					cancel();


				}
				if (count == 0){
					Player p = player;
					getConfig().set(p.getName() + ".X", p.getLocation().getX());
					getConfig().set(p.getName() + ".Y", p.getLocation().getY());
					getConfig().set(p.getName() + ".Z", p.getLocation().getZ());
					getConfig().set(p.getName() + ".Pitch", p.getLocation().getPitch());
					getConfig().set(p.getName() + ".Yaw", p.getLocation().getYaw());
					getConfig().set(p.getName() + ".World", p.getLocation().getWorld().getName());
					getConfig().set(p.getName() + ".Health", p.getHealth());
					getConfig().set(p.getName() + ".Current Lobby", name);
					if (getConfig().getBoolean("InventoryLoss") == true){
						saveInv(p);
						p.getInventory().clear();
						p.getInventory().setHelmet(null);
						p.getInventory().setChestplate(null);
						p.getInventory().setLeggings(null);
						p.getInventory().setBoots(null);
					}
					saveConfig();
					if (getConfig().getConfigurationSection("Lobby." + name).contains("Entry Message")){
						String Enter = getConfig().getString("Lobby." + name + ".Entry Message");
						p.sendMessage(Enter);
					}
					World Lobby = Bukkit.getServer().getWorld(getConfig().getString("Lobby." + name + ".World"));
					String pitch = getConfig().getString("Lobby." + name + ".Pitch");
					String yaw = getConfig().getString("Lobby." + name + ".Yaw");
					Float newpitch = Float.parseFloat(pitch);
					Float newyaw = Float.parseFloat(yaw);
					Location Spawn = new Location(Lobby, getConfig().getDouble("Lobby." + name + ".X"), getConfig().getDouble("Lobby." + name + ".Y"), getConfig().getDouble("Lobby." + name + ".Z"), newyaw, newpitch);
					p.teleport(Spawn);
					Integer healling = p.getMaxHealth();
					p.setHealth(healling);
					cancel();
				}
			}

		}.runTaskTimer(plugin, 20, 20);
	}
}

