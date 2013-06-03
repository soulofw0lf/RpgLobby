package com.vartala.soulofw0lf.rpglobby;




import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;




public class LobbyHandler implements CommandExecutor {


	RpgLobby Rpgl;

	public LobbyHandler(RpgLobby rpgl){
		this.Rpgl = rpgl;

	}


	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		Player player = (Player) sender;
		if (args.length == 0){
			return false;
		}
		if (args[0].equalsIgnoreCase("iloss")){
			if (!(player.hasPermission("lobby.iloss"))){
				player.sendMessage("You do not have permission to toggle item loss!");
				return true;
			}
			if (!(this.Rpgl.getConfig().contains("InventoryLoss"))){
				this.Rpgl.getConfig().set("InventoryLoss", true);
				player.sendMessage("Inventory loss has been toggled on!");
				return true;
			}
			if (this.Rpgl.getConfig().getBoolean("InventoryLoss") == true){
				this.Rpgl.getConfig().set("InventoryLoss", false);
				player.sendMessage("Inventory loss has been toggled off!");
				return true;
			}
			if (this.Rpgl.getConfig().getBoolean("InventoryLoss") == false){
				this.Rpgl.getConfig().set("InventoryLoss", true);
				player.sendMessage("Inventory loss has been toggled on!");
				return true;
			}
		}
		if (args[0].equalsIgnoreCase("entermsg")){
			if (!(player.hasPermission("lobby.msg"))){
				player.sendMessage("you do not have permission to set entry and exit messages");
				return true;
			}
			if (args.length <= 2){
				player.sendMessage("Improper usage, command must be /lobby exitmsg lobbyname whatever message you want");
				return true;
			}
			if (!(this.Rpgl.getConfig().getConfigurationSection("Lobby").contains(args[1]))){
				player.sendMessage("This lobby does not exist!");
				return true;
			}
			StringBuilder buffer = new StringBuilder();
			for (int i = 2; i < args.length; i++) {
				buffer.append(' ').append(args[i]);
			}
			String s = buffer.toString();
			String r = s.replaceAll("&", "§");
			this.Rpgl.getConfig().set("Lobby." + args[1] + ".Entry Message", r);
			player.sendMessage("Entry message for lobby " + args[1] + " set!");
			this.Rpgl.saveConfig();
			return true;
		}
		if (args[0].equalsIgnoreCase("exitmsg")){
			if (!(player.hasPermission("lobby.msg"))){
				player.sendMessage("you do not have permission to set entry and exit messages");
				return true;
			}
			if (args.length <= 2){
				player.sendMessage("Improper usage, command must be /lobby exitmsg lobbyname whatever message you want");
				return true;
			}
			if (!(this.Rpgl.getConfig().getConfigurationSection("Lobby").contains(args[1]))){
				player.sendMessage("This lobby does not exist!");
				return true;
			}
			StringBuilder buffer = new StringBuilder();
			for (int i = 2; i < args.length; i++) {
				buffer.append(' ').append(args[i]);
			}
			String s = buffer.toString();
			String r = s.replaceAll("&","§");
			this.Rpgl.getConfig().set("Lobby." + args[1] + ".Exit Message", r);
			player.sendMessage("Exit message for lobby " + args[1] + " set!");
			this.Rpgl.saveConfig();
			return true;
		}
		if (args[0].equalsIgnoreCase("toggle")){
			if (args.length != 1){
				player.sendMessage("Improper usage, please use </lobby toggle> to turn per lobby permissions on and off");
				return true;
			}
			if (player.hasPermission("lobby.toggle")){


				if ((this.Rpgl.getConfig().getBoolean("PerLobbyPermissions")) == true){
					this.Rpgl.getConfig().set("PerLobbyPermissions", false);
					player.sendMessage("Per Lobby Permissions set to False");
					this.Rpgl.saveConfig();
					return true;
				}
				if ((this.Rpgl.getConfig().getBoolean("PerLobbyPermissions"))== false){
					this.Rpgl.getConfig().set("PerLobbyPermissions", true);
					player.sendMessage("Per Lobby Permissions set to True");
					this.Rpgl.saveConfig();
					return true;
				}
			}
			else {
				player.sendMessage("you do not have permission to toggle Per Lobby Permissions");
				return true;
			}
		}
		if (args[0].equalsIgnoreCase("list")){
			if (!(player.hasPermission("lobby.list"))){
				player.sendMessage("You do not have permission to use the </lobby list> command");
				return true;
			}
			if (!(this.Rpgl.getConfig().contains("Lobby"))){
				player.sendMessage("There are no Lobbies yet! please use (/lobby set {lobbyname}) to add a lobby here!");
				return true;
			}
			final Set<String> keys = this.Rpgl.getConfig().getConfigurationSection("Lobby").getKeys(false);
			if (keys.size() <= 0) {
				player.sendMessage("There are no Lobbies");
				return true;
			}
			for (String key : this.Rpgl.getConfig().getConfigurationSection("Lobby").getKeys(false)){

				player.sendMessage(key + "\n");
			}
			return true;
		}
		if ((args[0].equalsIgnoreCase("del")) && (args.length == 2)){
			if (!(player.hasPermission("lobby.del"))){
				player.sendMessage("You do not have permission to use the </lobby del> command");
				return true;
			}
			if (this.Rpgl.getConfig().getString("Lobby." + args[1]) == null){
				player.sendMessage("That Lobby does not exist");
				return true;
			}
			this.Rpgl.getConfig().set("Lobby." + args[1], null);
			this.Rpgl.saveConfig();
			player.sendMessage("Lobby " + args[1] + " has been deleted!");
			return true;
		} else {
			if (args[0].equalsIgnoreCase("del")){
				player.sendMessage("Improper usage, please use /lobby del {name}");
				return true;
			}
		}
		if (args[0].equalsIgnoreCase("first")){
			if (!(player.hasPermission("lobby.first"))){
				player.sendMessage("You do not have permission to use the </lobby first> command");
				return true;
			}
			if (args.length != 1){
				player.sendMessage("Incorrect usage! to save the default lobby please use /lobby first");
				return true;
			}
			this.Rpgl.getConfig().set("First.X", player.getLocation().getX());
			this.Rpgl.getConfig().set("First.Y", player.getLocation().getY());
			this.Rpgl.getConfig().set("First.Z", player.getLocation().getZ());
			this.Rpgl.getConfig().set("First.Yaw", player.getLocation().getYaw());
			this.Rpgl.getConfig().set("First.Pitch", player.getLocation().getPitch());
			this.Rpgl.getConfig().set("First.World", player.getLocation().getWorld().getName());
			this.Rpgl.saveConfig();
			player.sendMessage("First Lobby exit point set.");
			return true;
		}
		if (args[0].equalsIgnoreCase("set")){
			if (!(player.hasPermission("lobby.set"))){
				player.sendMessage("You do not have permission to use the </lobby set> command");
				return true;
			}
			if (args.length != 3){
				player.sendMessage("Please use the command correctly! Proper usage is /lobby set {name} timetoenter");
				return true;
			}
			if (this.Rpgl.getConfig().contains(args[1]))
			{
				player.sendMessage("this lobby already exists!");
				return true;
			}
			Integer tte = Integer.parseInt(args[2]);
			this.Rpgl.getConfig().set("Lobby." + args[1] + ".X", player.getLocation().getX());
			this.Rpgl.getConfig().set("Lobby." + args[1] + ".Y", player.getLocation().getY());
			this.Rpgl.getConfig().set("Lobby." + args[1] + ".Z", player.getLocation().getZ());
			this.Rpgl.getConfig().set("Lobby." + args[1] + ".Yaw", player.getLocation().getPitch());
			this.Rpgl.getConfig().set("Lobby." + args[1] + ".Pitch", player.getLocation().getYaw());
			this.Rpgl.getConfig().set("Lobby." + args[1] + ".World", player.getLocation().getWorld().getName());
			this.Rpgl.getConfig().set("Lobby." + args[1] + ".Time", tte);
			this.Rpgl.saveConfig();
			player.sendMessage("Lobby location saved");
			return true;
		}
		if ((args.length > 2) || ((!(args[0].equalsIgnoreCase("enter")) && (!(args[0].equalsIgnoreCase("leave")))))){
			player.sendMessage("Please use {/lobby enter name} or {/lobby leave} ");
			return true;
		}
		if (args[0].equalsIgnoreCase("enter")){
			if (args.length != 2){
				player.sendMessage("Please use {/lobby enter name}");
				return true;
			}
		}
		if ((args[0].equalsIgnoreCase("enter")) && (this.Rpgl.getConfig().getString("Lobby." + args[1]) == null)){
			player.sendMessage("That lobby does not exist!");
			return true;
		}
		if ((args[0].equalsIgnoreCase("enter"))&& (!(this.Rpgl.getConfig().contains(player.getName())))){
			if (!(player.hasPermission("lobby.enter"))){
				player.sendMessage("You do not have permission to use the </lobby enter LobbyName> command");
				return true;
			}
			if (this.Rpgl.getConfig().getBoolean("PerLobbyPermissions") == true){
				if (!(player.hasPermission("lobby.enter." + args[1]))){
					player.sendMessage("You do not have permission to enter this lobby.");
					return true;
				}
			}
			Location newloc = player.getLocation();
			this.Rpgl.enterLobby(player, newloc, args[1]);
			return true;
		}
		if ((args[0].equalsIgnoreCase("enter")) && (this.Rpgl.getConfig().contains(player.getName()))){
			player.sendMessage("You cannot use this command from the Lobby");
			return true;
		}
		if ((args[0].equalsIgnoreCase("leave")) && (!(this.Rpgl.getConfig().contains(player.getName())))){
			if (!(this.Rpgl.getConfig().getConfigurationSection("newbies").contains(player.getName()))){
				World First = Bukkit.getServer().getWorld(this.Rpgl.getConfig().getString("First.World"));
				String pitch = this.Rpgl.getConfig().getString("First.Pitch");
				String yaw = this.Rpgl.getConfig().getString("First.Yaw");
				Float firstpitch = Float.parseFloat(pitch);
				Float firstyaw = Float.parseFloat(yaw);
				Location newloc = new Location(First,this.Rpgl.getConfig().getDouble("First.X"),this.Rpgl.getConfig().getDouble("First.Y"),this.Rpgl.getConfig().getDouble("First.Z"),firstyaw,firstpitch);
				player.teleport(newloc);
				this.Rpgl.getConfig().set("newbies." + player.getName(), "no longer a newbie");
				this.Rpgl.saveConfig();
				return true;
			}
			player.sendMessage("You can only use this command from the Lobby");
			return true;
		}
		if ((args[0].equalsIgnoreCase("leave")) && (this.Rpgl.getConfig().contains(player.getName()))){
			if (!(player.hasPermission("lobby.leave"))){
				player.sendMessage("You do not have permission to use the </lobby leave> command");
				return true;
			}
			
			String pitch = this.Rpgl.getConfig().getString(player.getName() + ".Pitch");
			String yaw = this.Rpgl.getConfig().getString(player.getName() + ".Yaw");
			World thisworld = Bukkit.getWorld(this.Rpgl.getConfig().getString(player.getName() + ".World"));
			Float newpitch = Float.parseFloat(pitch);
			Float newyaw = Float.parseFloat(yaw);
			Location newloc = new Location(thisworld,this.Rpgl.getConfig().getDouble(player.getName() + ".X"),this.Rpgl.getConfig().getDouble(player.getName() + ".Y"),this.Rpgl.getConfig().getDouble(player.getName() + ".Z"),newyaw,newpitch);
			player.teleport(newloc);
			String name = this.Rpgl.getConfig().getString(player.getName() + ".Current Lobby");
			if (this.Rpgl.getConfig().getConfigurationSection("Lobby." + name).contains("Exit Message")){
				String Exit = this.Rpgl.getConfig().getString("Lobby." + name + ".Exit Message");
				player.sendMessage(Exit);
			}
			this.Rpgl.loadInv(player);
			player.setHealth(this.Rpgl.getConfig().getInt(player.getName() + ".Health"));
			this.Rpgl.getConfig().set(player.getName(), null);
			this.Rpgl.saveConfig();

			return true;
		}
		return false;
	}
}
