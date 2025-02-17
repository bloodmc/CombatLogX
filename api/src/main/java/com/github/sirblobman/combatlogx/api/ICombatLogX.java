package com.github.sirblobman.combatlogx.api;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.sirblobman.api.adventure.adventure.text.Component;
import com.github.sirblobman.api.configuration.ConfigurationManager;
import com.github.sirblobman.api.configuration.IResourceHolder;
import com.github.sirblobman.api.configuration.PlayerDataManager;
import com.github.sirblobman.api.language.LanguageManager;
import com.github.sirblobman.api.language.Replacer;
import com.github.sirblobman.api.nms.MultiVersionHandler;
import com.github.sirblobman.combatlogx.api.expansion.ExpansionManager;
import com.github.sirblobman.combatlogx.api.manager.ICombatManager;
import com.github.sirblobman.combatlogx.api.manager.IDeathManager;
import com.github.sirblobman.combatlogx.api.manager.IPlaceholderManager;
import com.github.sirblobman.combatlogx.api.manager.IPunishManager;
import com.github.sirblobman.combatlogx.api.manager.ITimerManager;

public interface ICombatLogX extends IResourceHolder {
    default ICombatLogX getCombatLogX() {
        return this;
    }

    JavaPlugin getPlugin();

    ClassLoader getPluginClassLoader();

    YamlConfiguration getConfig(String fileName);

    void reloadConfig(String fileName);

    void saveConfig(String fileName);

    void saveDefaultConfig(String fileName);

    YamlConfiguration getData(OfflinePlayer player);

    void saveData(OfflinePlayer player);

    void onReload();

    MultiVersionHandler getMultiVersionHandler();

    ConfigurationManager getConfigurationManager();

    PlayerDataManager getPlayerDataManager();

    LanguageManager getLanguageManager();

    ExpansionManager getExpansionManager();

    ICombatManager getCombatManager();

    ITimerManager getTimerManager();

    IPunishManager getPunishManager();

    IDeathManager getDeathManager();

    IPlaceholderManager getPlaceholderManager();

    Component getMessageWithPrefix(CommandSender audience, String key, Replacer replacer);

    void sendMessageWithPrefix(CommandSender audience, String key, Replacer replacer);

    void sendMessage(CommandSender sender, String... messageArray);

    void printDebug(String... messageArray);

    void printDebug(Throwable ex);
}
