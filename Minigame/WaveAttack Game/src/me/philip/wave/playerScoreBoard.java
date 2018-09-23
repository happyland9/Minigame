package me.philip.wave;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.util.UUID;

public class playerScoreBoard {
    private Main main;

    // Requiring Main as a parameter
    public playerScoreBoard(Main main) {
        // Setting the class' Main field to the parameter
        this.main = main;

        // Any method in this class can now use any public method from Main by doing  main.someMethodHere
    }

    private static ScoreboardManager m;
    private static Scoreboard b;
    private static Objective o;
    private static Score coins;
    private static Score hwave;
    private static Score kills;
    private static Score web;
    private static Score space;
    private static Score space2;
    private static Score space3;
    private static Score space4;
    private static Score players;
    private static Score countdown;
    private static Score wave;
    private static Score playerleft;

    public void scoreLobby(UUID uuid, Player player){
        m = Bukkit.getScoreboardManager();
        b = m.getNewScoreboard();
        o = b.registerNewObjective("poke", "");
        o.setDisplaySlot(DisplaySlot.SIDEBAR);
        o.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "POKÉ DEFENDERS");

        space = o.getScore(" ");
        space.setScore(9);

        space2 = o.getScore("  ");
        space2.setScore(7);

        space3 = o.getScore("   ");
        space3.setScore(5);

        space4 = o.getScore("    ");
        space4.setScore(3);

        coins = o.getScore(ChatColor.RED + "" + ChatColor.BOLD + "Coins" + ChatColor.GRAY + "» " + ChatColor.GREEN + main.getConfig().getInt("Poke.Coins." + uuid));
        coins.setScore(8);

        hwave = o.getScore(ChatColor.RED + "" + ChatColor.BOLD + "Highest Wave" + ChatColor.GRAY + "» " + ChatColor.GREEN + main.getConfig().getInt("Poke.HWave."+uuid));
        hwave.setScore(6);

        kills = o.getScore(ChatColor.RED + "" + ChatColor.BOLD + "Highest Kills" + ChatColor.GRAY + "» " + ChatColor.GREEN + main.getConfig().getInt("Poke.Kills."+uuid));
        kills.setScore(4);

        web = o.getScore(ChatColor.RED + "pokecatch.org");
        web.setScore(2);

        player.setScoreboard(b);
    }

    public void gamespawn(UUID uuid, Player player){
        m = Bukkit.getScoreboardManager();
        b = m.getNewScoreboard();
        o = b.registerNewObjective("poke", "");
        o.setDisplaySlot(DisplaySlot.SIDEBAR);
        o.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "POKÉ DEFENDERS");

        space = o.getScore(" ");
        space.setScore(6);

        space2 = o.getScore("  ");
        space2.setScore(4);

        space3 = o.getScore("   ");
        space3.setScore(2);

        countdown = o.getScore(ChatColor.RED + "" + ChatColor.BOLD + "Game Starts" + ChatColor.GRAY + "» " + ChatColor.GREEN + main.GameManager.countdown);
        countdown.setScore(5);

        players = o.getScore(ChatColor.RED + "" + ChatColor.BOLD + "Players" + ChatColor.GRAY + "» " + ChatColor.GREEN + main.playersIngame.size() + "/" + main.GameManager.playersNeeded);
        players.setScore(3);

        web = o.getScore(ChatColor.RED + "pokecatch.org");
        web.setScore(1);

        player.setScoreboard(b);



    }

    public void gameScore(UUID uuid, Player player){
        m = Bukkit.getScoreboardManager();
        b = m.getNewScoreboard();
        o = b.registerNewObjective("poke", "");
        o.setDisplaySlot(DisplaySlot.SIDEBAR);
        o.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "POKÉ DEFENDERS");

        space = o.getScore(" ");
        space.setScore(6);

        space2 = o.getScore("  ");
        space2.setScore(4);

        space2 = o.getScore("   ");
        space2.setScore(2);

        playerleft = o.getScore(ChatColor.RED + "" + ChatColor.BOLD + "Players Left" + ChatColor.GRAY + "» " + main.playersIngame.size());
        playerleft.setScore(5);

        wave = o.getScore(ChatColor.RED + "" + ChatColor.BOLD + "Wave" + ChatColor.GRAY + "» " + ChatColor.GREEN + main.GameManager.wave);
        wave.setScore(3);


        web = o.getScore(ChatColor.RED + "pokecatch.org");
        web.setScore(1);

        player.setScoreboard(b);


    }



}
