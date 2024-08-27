package one.muisnowdevs.minecraft.allinone.events;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import one.muisnowdevs.minecraft.allinone.AllInOne;
import one.muisnowdevs.minecraft.allinone.Utils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Item;

import java.util.List;

enum Times {
    TEN_MINUTE,
    ONE_MINUTE
}

public class ItemGarbageTruck {
    private final AllInOne _plugin;

    public ItemGarbageTruck(AllInOne plugin) {
        _plugin = plugin;

        Bukkit.getScheduler().scheduleSyncRepeatingTask(_plugin, () -> notifyEvent(Times.TEN_MINUTE), 12000L, 24000L);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(_plugin, () -> notifyEvent(Times.ONE_MINUTE), 22800L, 24000L);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(_plugin, this::clearEvent, 24000L, 24000L);
    }

    private void notifyEvent(Times time) {
        String timeString = switch (time) {
            case ONE_MINUTE -> "1";
            case TEN_MINUTE -> "10";
        };

        if (_plugin.getServer().getOnlinePlayers().isEmpty())
            return;

        _plugin.getServer().sendMessage(Component.text()
                .append(Utils.titleTag("垃圾車", NamedTextColor.AQUA))
                .append(Component.text(String.format("即將在 %s 分鐘後回收掉落物！", timeString)))
                .build());
    }

    private void clearEvent() {
        if (_plugin.getServer().getOnlinePlayers().isEmpty())
            return;

        _plugin.getServer().sendMessage(Component.text()
                .append(Utils.titleTag("垃圾車", NamedTextColor.AQUA))
                .append(Component.text("正在從所有世界回收掉落物..."))
                .build());

        final long[] entityCount = {0};

        List<World> world = _plugin.getServer().getWorlds();
        world.forEach(wd -> {
            wd.getEntities().stream().filter(Item.class::isInstance).forEach(entity -> {
                entityCount[0]++;
                entity.remove();
            });
        });

        _plugin.getServer().sendMessage(Component.text()
                .append(Utils.titleTag("垃圾車", NamedTextColor.AQUA))
                .append(Component.text(String.format("回收成功 %d 件垃圾！世界變得一片祥和了！", entityCount[0])))
                .build());
    }
}
