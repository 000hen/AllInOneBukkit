package one.muisnowdevs.minecraft.allinone.events;

import net.kyori.adventure.text.Component;
import one.muisnowdevs.minecraft.allinone.AllInOne;
import one.muisnowdevs.minecraft.allinone.Utils;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Random;

public class PlayerDeathPunish implements Listener {
    public static final double DEFAULT_RATE = 0.21653d;

    private final AllInOne _plugin;

    public PlayerDeathPunish(AllInOne plugin) {
        _plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!_plugin.getConfig().getBoolean("deadPunishment"))
            return;

        Random random = new Random();

        Player player = event.getPlayer();
        int playerDeath = player.getStatistic(Statistic.DEATHS);
        double playerPlayedTime = Math.log(player.getStatistic(Statistic.PLAY_ONE_MINUTE));
        double playerDeathSinceLast = Math.log(player.getStatistic(Statistic.TIME_SINCE_DEATH));

        double probability = DEFAULT_RATE
                + playerDeath * 0.0657d
                + playerPlayedTime * 0.0365d
                - playerDeathSinceLast * 0.03185d;

        if (random.nextInt(10) > probability) return;

        switch (random.nextInt(3)) {
            case 0:
            case 1:
                int playerExpLevel = player.getLevel();
                int playerExp = player.getTotalExperience();
                int declineExpLevel = random.nextInt((int) (playerExpLevel * 0.3), (int) (playerExpLevel * 0.8));
                int declineExp = (int) (playerExp * 0.01 + random.nextDouble() * (playerExp * 0.8 - playerExp * 0.4));
                player.setTotalExperience(playerExp - declineExp);
                player.setLevel(playerExpLevel - declineExpLevel);

                Utils.showErrorMessageToPlayer(player, Component.text(String.format("您被扣除 %d 經驗點，與 %d 經驗等級", declineExp, declineExpLevel)), "死亡懲罰");
                return;

            case 2:
                PlayerInventory playerItems = player.getInventory();
                int playerItemsLength = playerItems.getContents().length;

                int randomItemIndex = random.nextInt(playerItemsLength);

                ItemStack item = playerItems.getContents()[randomItemIndex];
                if (item == null) return;

                playerItems.remove(item);
                Utils.showErrorMessageToPlayer(player, Component.text()
                        .append(Component.text("已移除您物品欄裡的")
                                .append(Component.text(" "))
                                .append(item.displayName()
                                        .hoverEvent(item.asHoverEvent())))
                        .build(), "死亡懲罰");
        }
    }
}
