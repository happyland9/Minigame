package me.philip.wave;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class Events implements Listener {
    private Main main;
    public Location lobbyspawn;
    //public Player damager;

    // Requiring Main as a parameter
    public Events(Main main) {
        // Setting the class' Main field to the parameter
        this.main = main;

        // Any method in this class can now use any public method from Main by doing  main.someMethodHere
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        playerData data = main.playerdata.get(uuid);
        main.playerdata.put(uuid, new playerData(uuid, false));
        event.setJoinMessage(ChatColor.GRAY + "[" + ChatColor.RED + "+" + ChatColor.GRAY + "] " + ChatColor.RED + player.getName());
        this.lobbyspawn = (Location)main.getConfig().get("Poke.Lobby");
        if(main.getConfig().getString("Poke.Coins." + uuid)==null){
            main.getConfig().set("Poke.Coins." + uuid, 1);
            main.saveConfig();
        }
        if(main.getConfig().getString("Poke.HWave." + uuid)==null){
            main.getConfig().set("Poke.HWave." + uuid, 1);
            main.saveConfig();
        }
        if(main.getConfig().getString("Poke.Kills." + uuid)==null){
            main.getConfig().set("Poke.Kills." + uuid, 1);
            main.saveConfig();
        }
        if(!(main.getConfig().getString("Poke.Lobby")==null)){
            player.teleport(lobbyspawn);
        }
        main.playerScoreBoard.scoreLobby(uuid, player);
    }

    @EventHandler
    public void signInteract(SignChangeEvent event){
        if(event.getLine(0).equalsIgnoreCase("[GAME]")){
            event.setLine(0, ChatColor.RED + "" + ChatColor.BOLD + "DEFENDERS");
            event.setLine(1, ChatColor.GREEN + "" + ChatColor.BOLD + "JOIN");
            event.setLine(3, ChatColor.GRAY + "" + Bukkit.getOnlinePlayers().size() + "/" + main.GameManager.playersNeeded + "");
        }
    }

    @EventHandler
    public void mobAttack(EntityDeathEvent event){
        Entity entity = event.getEntity();
        Player player = ((LivingEntity) entity).getKiller();
        UUID uuid = player.getUniqueId();
        if(((LivingEntity) entity).getKiller().equals(player)){
            if(main.entities.contains(entity)){
                main.entities.remove(entity);
            }else{
                return;
            }
        }else{
            return;
        }


    }

    @EventHandler
    public void playerMove(PlayerMoveEvent event){
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        playerData data = main.playerdata.get(uuid);
        if(data.isHealing()){
            data.setHealing(false);
            player.sendMessage(ChatColor.RED + "You have moved while healing, please try again!");
        }
    }

    @EventHandler
    public void armourStand(EntityDamageByEntityEvent event){
        Player player = (Player)event.getDamager();
        Entity entity = event.getEntity();
        UUID uuid = player.getUniqueId();
        main.GameManager.mainspawn = (Location)main.getConfig().get("Poke.MainGame");
        playerData data = main.playerdata.get(uuid);
        if(entity instanceof ArmorStand){
            event.setCancelled(true);
            if(main.stands.contains(entity)){
                data.setHealing(true);
                player.sendMessage(ChatColor.GREEN + "You are healing. Do not move for 5 seconds!");
                Player damager = main.playerstand.get((ArmorStand)entity);
                ArmorStand stand = (ArmorStand)entity;
                new BukkitRunnable(){

                    @Override
                    public void run() {
                        if(data.isHealing()){
                            player.sendMessage(ChatColor.GREEN + "You have healed the player!");
                            main.needHealing.remove(damager);
                            main.stands.remove(stand);
                            stand.remove();
                            damager.setGameMode(GameMode.SURVIVAL);
                            damager.teleport(main.GameManager.mainspawn);
                            data.setHealing(false);
                        }
                    }
                }.runTaskLater(main, 100);

            }
        }

    }


    @EventHandler
    public void signTouch(PlayerInteractEvent event){
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        main.GameManager.mainspawn = (Location)main.getConfig().get("Poke.MainGame");
        playerData data = main.playerdata.get(uuid);
        if(event.getClickedBlock().getState() instanceof Sign){
            Sign sign = (Sign)event.getClickedBlock().getState();
            if(sign.getLine(0).equalsIgnoreCase(ChatColor.RED + "" + ChatColor.BOLD + "DEFENDERS")){
                player.sendMessage(ChatColor.GREEN + "Sending you to Game-1");
                main.GameManager.gameJoin(player);
            }
        }
    }

    @EventHandler
    public void breakBlock(BlockBreakEvent event){
        Player player = event.getPlayer();
        Block block = event.getBlock();
        UUID uuid = player.getUniqueId();
        main.GameManager.mainspawn = (Location)main.getConfig().get("Poke.MainGame");
        playerData data = main.playerdata.get(uuid);
        if(!player.isOp() || !player.hasPermission("poke.admin")){
            player.sendMessage(ChatColor.RED + "You are not allowed to do that");
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void placeBlock(BlockPlaceEvent event){
        Player player = event.getPlayer();
        if(!player.isOp() || !player.hasPermission("poke.admin")){
            player.sendMessage(ChatColor.RED + "You are not allowed to do that");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if(main.playersIngame.contains(uuid)){
            main.playersIngame.remove(uuid);
        }else{
            return;
        }
    }

    @EventHandler
    public void playerDamage(EntityDamageByEntityEvent event){
        if(event.getEntity() instanceof Player){
            Player damaged = (Player)event.getEntity();
            UUID uuid = damaged.getUniqueId();

            if(main.playersIngame.contains(uuid)){
                if(damaged.getHealth() - event.getDamage() <= 0){
                    event.setCancelled(true);
                    damaged.setHealth(20);
                    damaged.setGameMode(GameMode.SPECTATOR);
                    main.GameManager.Healer(damaged);
                }
            }
        }
    }



}
