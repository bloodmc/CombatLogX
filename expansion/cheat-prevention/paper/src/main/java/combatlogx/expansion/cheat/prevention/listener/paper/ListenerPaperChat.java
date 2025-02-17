package combatlogx.expansion.cheat.prevention.listener.paper;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import io.papermc.paper.event.player.AsyncChatEvent;

import com.github.sirblobman.api.configuration.ConfigurationManager;
import com.github.sirblobman.combatlogx.api.expansion.Expansion;

import combatlogx.expansion.cheat.prevention.listener.CheatPreventionListener;

public final class ListenerPaperChat extends CheatPreventionListener {
    public ListenerPaperChat(Expansion expansion) {
        super(expansion);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onChat(AsyncChatEvent e) {
        Player player = e.getPlayer();
        if (isEnabled() || !isInCombat(player)) {
            return;
        }

        e.setCancelled(true);
        sendMessage(player, "expansion.cheat-prevention.no-chat", null);
    }

    private YamlConfiguration getConfiguration() {
        Expansion expansion = getExpansion();
        ConfigurationManager configurationManager = expansion.getConfigurationManager();
        return configurationManager.get("chat.yml");
    }

    private boolean isEnabled() {
        YamlConfiguration configuration = getConfiguration();
        return !configuration.getBoolean("disable-chat");
    }
}
