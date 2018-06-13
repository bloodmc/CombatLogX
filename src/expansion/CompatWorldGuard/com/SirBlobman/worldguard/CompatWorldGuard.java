package com.SirBlobman.worldguard;

import java.io.File;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.Vector;

import com.SirBlobman.combatlogx.Combat;
import com.SirBlobman.combatlogx.config.ConfigLang;
import com.SirBlobman.combatlogx.config.NoEntryMode;
import com.SirBlobman.combatlogx.event.PlayerCombatEvent;
import com.SirBlobman.combatlogx.expansion.CLXExpansion;
import com.SirBlobman.combatlogx.expansion.Expansions;
import com.SirBlobman.combatlogx.utility.Util;
import com.SirBlobman.not.config.ConfigNot;
import com.SirBlobman.worldguard.config.ConfigWorldGuard;

import com.olivolja3.worldguard.ForceField;

public class CompatWorldGuard implements CLXExpansion, Listener {
    public static File FOLDER;
    
    @Override
    public void enable() {
        if(Util.PM.isPluginEnabled("WorldGuard")) {
            FOLDER = getDataFolder();
            ConfigWorldGuard.load();
            Util.regEvents(this);
            Util.regEvents(new ForceField());
        } else {
            String error = "WorldGuard is not installed. This expansion is useless!";
            print(error);
        }
    }

    public String getUnlocalizedName() {return "CompatWorldGuard";}
    public String getName() {return "WorldGuard Compatability";}
    public String getVersion() {return "3";}

    @EventHandler
    public void pce(PlayerCombatEvent e) {
        LivingEntity ler = e.getAttacker();
        LivingEntity led = e.getTarget();
        if(ler instanceof Player) { 
            Player p = (Player) ler;
            boolean safe = WorldGuardUtil.isSafeZone(p.getLocation());
            if(safe) e.setCancelled(true);
        }

        if(led instanceof Player) {
            Player p = (Player) led;
            boolean safe = WorldGuardUtil.isSafeZone(p.getLocation());
            if(safe) e.setCancelled(true);
        }
    }

    @EventHandler
    public void move(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        Location from = e.getFrom();
        Location to = e.getTo();
        checkMoveEvent(p, e, from, to);
    }

    @EventHandler
    public void tp(PlayerTeleportEvent e) {
        Player p = e.getPlayer();
        Location from = e.getFrom();
        Location to = e.getTo();
        String cause = e.getCause().name();
        if(cause.equals("CHROUS_FRUIT") || cause.equals("ENDER_PEARL") || cause.equals("PLUGIN")) checkMoveEvent(p, e, from, to);
    }
    
    public static void checkMoveEvent(Player p, Cancellable e, Location from, Location to) {
        if(ConfigWorldGuard.OPTION_NO_SAFEZONE_ENTRY) {
        	if(e.isCancelled()) return;
            if(Combat.isInCombat(p)) {
                Entity enemy = Combat.getEnemy(p);
                if(enemy != null) {
                    if(enemy instanceof Player && WorldGuardUtil.isSafeZone(to)) {
                    	if(p.isInsideVehicle()) p.leaveVehicle();
                        String mode = ConfigWorldGuard.OPTION_NO_SAFEZONE_ENTRY_MODE;
                        NoEntryMode nem = NoEntryMode.valueOf(mode);
                        if(nem == null) nem = NoEntryMode.CANCEL;
                        if(nem == NoEntryMode.CANCEL) e.setCancelled(true);
                        else if(nem == NoEntryMode.KILL) p.setHealth(0.0D);
                        else if(nem == NoEntryMode.KNOCKBACK) {
                            Vector vector = WorldGuardUtil.getSafeZoneKnockbackVector(from);
                            vector = vector.multiply(ConfigWorldGuard.OPTION_NO_SAFEZONE_ENTRY_STRENGTH);
                            p.setVelocity(vector);
                        } else if(nem == NoEntryMode.TELEPORT) {
                            Location l = enemy.getLocation();
                            p.teleport(l);
                        }
                        
                        String error = ConfigLang.MESSAGE_NO_ENTRY;
                        Util.sendMessage(p, error);
                    } else if(WorldGuardUtil.isSafeFromMobs(to)) {
                    	if(p.isInsideVehicle()) p.leaveVehicle();
                        String mode = ConfigWorldGuard.OPTION_NO_SAFEZONE_ENTRY_MODE;
                        NoEntryMode nem = NoEntryMode.valueOf(mode);
                        if(nem == null) nem = NoEntryMode.CANCEL;
                        if(nem == NoEntryMode.CANCEL) e.setCancelled(true);
                        else if(nem == NoEntryMode.KILL) p.setHealth(0.0D);
                        else if(nem == NoEntryMode.KNOCKBACK) {
                            Vector vector = WorldGuardUtil.getMobsZoneKnockbackVector(from);
                            vector = vector.multiply(ConfigWorldGuard.OPTION_NO_SAFEZONE_ENTRY_STRENGTH);
                            p.setVelocity(vector);
                        } else if(nem == NoEntryMode.TELEPORT) {
                            Location l = enemy.getLocation();
                            p.teleport(l);
                        }
                        
                        String error = ConfigLang.MESSAGE_NO_ENTRY;
                        Util.sendMessage(p, error);
                    }
                } else {
                    if(Expansions.isEnabled("NotCombatLogX")) {
                        if(ConfigNot.OPTION_NO_SAFEZONE_ENTRY && WorldGuardUtil.isSafeZone(to)) {
                        	if(p.isInsideVehicle()) p.leaveVehicle();
                            String mode = ConfigWorldGuard.OPTION_NO_SAFEZONE_ENTRY_MODE;
                            NoEntryMode nem = NoEntryMode.valueOf(mode);
                            if(nem == null) nem = NoEntryMode.CANCEL;
                            if(nem == NoEntryMode.CANCEL) e.setCancelled(true);
                            else if(nem == NoEntryMode.KILL) p.setHealth(0.0D);
                            else if(nem == NoEntryMode.KNOCKBACK) {
                                Vector vector = WorldGuardUtil.getSafeZoneKnockbackVector(from);
                                vector = vector.multiply(ConfigWorldGuard.OPTION_NO_SAFEZONE_ENTRY_STRENGTH);
                                p.setVelocity(vector);
                            } else e.setCancelled(true);
                            
                            String error = ConfigLang.MESSAGE_NO_ENTRY;
                            Util.sendMessage(p, error);
                        }
                    }
                }
            }
        }
    }
}