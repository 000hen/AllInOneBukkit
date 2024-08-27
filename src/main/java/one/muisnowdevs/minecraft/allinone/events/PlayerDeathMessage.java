package one.muisnowdevs.minecraft.allinone.events;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import one.muisnowdevs.minecraft.allinone.AllInOne;
import one.muisnowdevs.minecraft.allinone.Utils;
import one.muisnowdevs.minecraft.allinone.commands.IPlayerLastDeathLocation;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerDeathMessage implements Listener {
    private final AllInOne _plugin;

    public PlayerDeathMessage(AllInOne plugin) {
        _plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        Location location = event.getPlayer().getLocation();

        _plugin.getPlayerLastDeathStorage().put(player.getUniqueId(), new IPlayerLastDeathLocation() {
            @Override
            public Player getPlayer() {
                return player;
            }

            @Override
            public Location getLocation() {
                return location;
            }
        });
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        IPlayerLastDeathLocation lastDeath = _plugin.getPlayerLastDeathStorage().get(player.getUniqueId());
        Location location = lastDeath.getLocation();

        Utils.showMessageToPlayer(player, Component.text()
                .append(Component.text("哎呀！您在"))
                .append(Utils.formatLocation(location))
                .append(Component.text("死亡了！不過不用擔心，您可以使用指令"))
                .append(Utils.commandComponent("/back"))
                .append(Component.text("返回您上次死亡的地點。當然您需要付出經驗等級"))
                .append(Component.text(" 20 ").color(NamedTextColor.YELLOW))
                .append(Component.text("等"))
                .build());
    }
}
