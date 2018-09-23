package me.philip.wave;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Main extends JavaPlugin {
    public playerScoreBoard playerScoreBoard;
    public Events Events;
    public GameManager GameManager;
    public ArrayList<Entity> entities = new ArrayList<>();
    public ArrayList<UUID> playersIngame = new ArrayList<>();
    public ArrayList<ArmorStand> stands = new ArrayList<>();
    public ArrayList<Player> needHealing = new ArrayList<>();
    public HashMap<UUID, playerData> playerdata = new HashMap<UUID, playerData>();
    public HashMap<ArmorStand, Player> playerstand = new HashMap<ArmorStand, Player>();

    public void onEnable() {
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "PokeDefenders Enabled, made by Philip");
        instanceClasses();
        getServer().getPluginManager().registerEvents(new Events(this), this);
        getCommand("lobbyset").setExecutor(new Commands(this));
        getCommand("gamespawn").setExecutor(new Commands(this));
        getCommand("maingame").setExecutor(new Commands(this));
        getCommand("zombie1").setExecutor(new Commands(this));
        getCommand("zombie2").setExecutor(new Commands(this));
        getCommand("zombie3").setExecutor(new Commands(this));
        getCommand("zombie4").setExecutor(new Commands(this));
        getCommand("force").setExecutor(new Commands(this));
        getCommand("forcestop").setExecutor(new Commands(this));
    }

    private void instanceClasses(){
        playerScoreBoard = new playerScoreBoard(this);
        Events = new Events(this);
        GameManager = new GameManager(this);

    }
}
