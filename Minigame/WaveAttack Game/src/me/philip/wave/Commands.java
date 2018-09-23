package me.philip.wave;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
    private Main main;

    // Requiring Main as a parameter
    public Commands(Main main) {
        // Setting the class' Main field to the parameter
        this.main = main;

        // Any method in this class can now use any public method from Main by doing  main.someMethodHere
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player player = (Player)sender;
        if(player.hasPermission("poke.admin") || player.isOp()){
            if(command.getName().equalsIgnoreCase("lobbyset")){
                main.getConfig().set("Poke.Lobby", player.getLocation());
                main.saveConfig();
                player.sendMessage(ChatColor.GREEN + "Lobby set!");
            }
            if(command.getName().equalsIgnoreCase("gamespawn")){
                main.getConfig().set("Poke.GameSpawn", player.getLocation());
                main.saveConfig();
                player.sendMessage(ChatColor.GREEN + "Game Spawn Set!");
            }
            if(command.getName().equalsIgnoreCase("maingame")){
                main.getConfig().set("Poke.MainGame", player.getLocation());
                main.saveConfig();
                player.sendMessage(ChatColor.GREEN + "Game Spawn Set!");
            }
            if(command.getName().equalsIgnoreCase("zombie1")) {
                main.getConfig().set("Poke.zombie1", player.getLocation());
                main.saveConfig();
                player.sendMessage(ChatColor.GREEN + "Zombie Spawn Set!");
            }
            if(command.getName().equalsIgnoreCase("zombie2")) {
                main.getConfig().set("Poke.zombie2", player.getLocation());
                main.saveConfig();
                player.sendMessage(ChatColor.GREEN + "Zombie Spawn Set!");
            }
            if(command.getName().equalsIgnoreCase("zombie3")) {
                main.getConfig().set("Poke.zombie3", player.getLocation());
                main.saveConfig();
                player.sendMessage(ChatColor.GREEN + "Zombie Spawn Set!");
            }
            if(command.getName().equalsIgnoreCase("zombie4")) {
                main.getConfig().set("Poke.zombie4", player.getLocation());
                main.saveConfig();
                player.sendMessage(ChatColor.GREEN + "Zombie Spawn Set!");
            }
            if(command.getName().equalsIgnoreCase("force")) {
                player.sendMessage(ChatColor.GREEN + "Force starting!");
                main.GameManager.gameStart();
            }
            if(command.getName().equalsIgnoreCase("forcestop")){
                player.sendMessage(ChatColor.GREEN + "Force stopping!");
                main.GameManager.gameStop();
            }
        }

        return false;
    }
}
