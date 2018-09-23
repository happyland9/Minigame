package me.philip.wave;


import net.minecraft.server.v1_8_R1.AttributeInstance;
import net.minecraft.server.v1_8_R1.EntityLiving;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.UUID;

public class GameManager implements Listener {
    public boolean isStarted = false;
    public int playersNeeded =  16;
    public int countdown = 60;
    public int wave = 0;
    private Location gamespawn;
    public Location mainspawn;
    private Location zombie1;
    private Location zombie2;
    private Location zombie3;
    public ArmorStand stand;
    private Location zombie4;
    private Entity zombies;
    private boolean healing;
    private int healingtime = 5;

    private Main main;

    // Requiring Main as a parameter
    public GameManager(Main main) {
        // Setting the class' Main field to the parameter
        this.main = main;

        // Any method in this class can now use any public method from Main by doing  main.someMethodHere
    }

    public void gameJoin(Player player){
        UUID uuid = player.getUniqueId();
        int online = Bukkit.getOnlinePlayers().size();
        if(!main.playersIngame.contains(uuid)){
            main.playersIngame.add(uuid);
            player.sendMessage(ChatColor.GREEN + "Joined Game-1");
            this.gamespawn = (Location) main.getConfig().get("Poke.GameSpawn");
            player.teleport(gamespawn);
            player.sendMessage(ChatColor.YELLOW + "There are " + ChatColor.GRAY + main.playersIngame.size() + "/" + playersNeeded + ChatColor.YELLOW + " to start a game");
            playerChecker(online);
            main.playerScoreBoard.gamespawn(uuid, player);
        }

    }

    public boolean playerChecker(int online){
        if(main.playersIngame.size()>=playersNeeded){
            if(!isStarted){
                isStarted = true;
                lobbycountdown();
            }else{
                return true;
            }
        }else{
            return true;
        }
        return true;
    }

    public void lobbycountdown(){
        new BukkitRunnable(){

            @Override
            public void run() {
                if(countdown>0){
                    if(playerChecker(Bukkit.getOnlinePlayers().size())){
                        countdown--;
                        Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + "The game will start in " + countdown);
                        for(Player online : Bukkit.getOnlinePlayers()){
                            online.playSound(online.getLocation(), Sound.NOTE_BASS, 2, 2);
                        }
                    }else{
                        Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + "Player(s) left unti game starts " + playersNeeded + ". Game will not start!");
                        this.cancel();
                        countdown = 60;
                    }
                }else{
                    this.cancel();
                    gameStart();
                    Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "Game starting, good luck");
                }
            }
        }.runTaskTimer(main, 0, 20);
    }


    public void gameStart(){
        this.mainspawn = (Location)main.getConfig().get("Poke.MainGame");
        for(Player online : Bukkit.getOnlinePlayers()){
            UUID uuid = online.getUniqueId();
            if(main.playersIngame.contains(uuid)){
                online.teleport(mainspawn);
                Checker(online);
                main.playerScoreBoard.gameScore(uuid, online);
            }

        }
    }

    public void Checker(Player player){
        new BukkitRunnable(){
            @Override
            public void run() {
                if(!(main.playersIngame.size()==0)){
                    if(main.entities.size()==0){
                        wave = wave + 1;
                        Bukkit.getServer().broadcastMessage(ChatColor.RED + "TOUCTOUCH");
                        waveSpawn();

                    }
                }else{
                    gameStop();
                    this.cancel();
                }
            }
        }.runTaskTimer(main, 0, 20);

    }

    public void waveSpawn(){
        for(Player online : Bukkit.getOnlinePlayers()){
            UUID uuid = online.getUniqueId();
            if(main.playersIngame.contains(uuid)){
                main.playerScoreBoard.gameScore(uuid, online);
            }
        }
        this.zombie1 = (Location)main.getConfig().get("Poke.zombie1");
        this.zombie2 = (Location)main.getConfig().get("Poke.zombie2");
        this.zombie3 = (Location)main.getConfig().get("Poke.zombie3");
        this.zombie4 = (Location)main.getConfig().get("Poke.zombie4");
        for(int i = 0; i <= wave*2;i++){
            Entity entity = zombie1.getWorld().spawnEntity(zombie1, EntityType.ZOMBIE);
            Entity entity1 = zombie2.getWorld().spawnEntity(zombie2, EntityType.ZOMBIE);
            Entity entity2 = zombie3.getWorld().spawnEntity(zombie3, EntityType.ZOMBIE);
            Entity entity3 = zombie4.getWorld().spawnEntity(zombie4, EntityType.ZOMBIE);
            main.entities.add(entity);
            main.entities.add(entity1);
            main.entities.add(entity2);
            main.entities.add(entity3);
        }
    }

    public void gameStop(){

        main.Events.lobbyspawn = (Location)main.getConfig().get("Poke.Lobby");
        for(Player player : Bukkit.getOnlinePlayers()){
            if(main.playersIngame.contains(player)){
                UUID uuid = player.getUniqueId();
                if(main.getConfig().getInt("Poke.HWave." + uuid)<wave){
                    main.getConfig().set("Poke.HWave."+uuid, wave);
                    main.saveConfig();
                    player.sendMessage(ChatColor.GREEN + "Congrats you have a new recorded on your highest wave!");
                }
            }
        }
        for(World world : Bukkit.getWorlds()){
            for(Entity entity: world.getEntities()){
                if(main.entities.contains(entity)){
                    entity.remove();
                }
            }
        }
        wave = 0;
        healing = false;
        healingtime = 5;
        isStarted = false;

        for(Player online: Bukkit.getOnlinePlayers()){
            UUID uuid = online.getUniqueId();
            playerData data = main.playerdata.get(uuid);
            data.setHealing(false);
            main.playersIngame.clear();
            countdown = 60;
            main.entities.clear();
            main.stands.clear();
            main.needHealing.clear();
            if(main.playersIngame.contains(online)) {
                main.playerScoreBoard.scoreLobby(uuid, online);
                online.teleport(main.Events.lobbyspawn);

            }
        }
    }


    public void Healer(Player player){
        Location loc = player.getLocation();
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());

        SkullMeta meta = (SkullMeta)skull.getItemMeta();
        meta.setOwner(player.getName());
        skull.setItemMeta(meta);

        ArmorStand stand = loc.getWorld().spawn(loc, ArmorStand.class);
        stand.setVisible(true);
        stand.setGravity(false);
        stand.setArms(true);
        stand.setHelmet(skull);

        main.stands.add(stand);
        main.needHealing.add(player);
        main.Events.lobbyspawn = (Location)main.getConfig().get("Poke.Lobby");
        main.playerstand.put(stand, player);
        new BukkitRunnable(){

            @Override
            public void run() {
                if(main.needHealing.contains(player)){
                    main.needHealing.remove(player);
                    player.setGameMode(GameMode.SURVIVAL);
                    player.teleport(main.Events.lobbyspawn);
                    main.playersIngame.remove(player);
                    player.sendMessage(ChatColor.RED + "You are out of the game!");
                    main.stands.remove(stand);


                }

            }
        }.runTaskLater(main, 1200);

    }







}
