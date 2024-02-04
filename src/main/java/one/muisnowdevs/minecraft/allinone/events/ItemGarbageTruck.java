package one.muisnowdevs.minecraft.allinone.events;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import one.muisnowdevs.minecraft.allinone.AllInOne;
import one.muisnowdevs.minecraft.allinone.Utils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;

import java.util.List;

public class ItemGarbageTruck {
    private final AllInOne _plugin;

    private Boolean isClearing = false;
    private Integer clearIndex = 0;

    public ItemGarbageTruck(AllInOne plugin) {
        _plugin = plugin;

        Bukkit.getScheduler().scheduleSyncRepeatingTask(_plugin, this::clearEvent, 0L, 12000L);
    }

    private void notifyEvent() {
        _plugin.getServer().sendMessage(Component.text()
                .append(Utils.titleTag("垃圾車", NamedTextColor.AQUA))
                .append(Component.text("即將在 10 分鐘後回收掉落物！"))
                .build());
    }

    private void clearEvent() {
        if (isClearing) return;
        if (clearIndex++ == 0) {
            notifyEvent();
            return;
        }

        isClearing = true;

        _plugin.getServer().sendMessage(Component.text()
                .append(Utils.titleTag("垃圾車", NamedTextColor.AQUA))
                .append(Component.text("正在從所有世界回收掉落物..."))
                .build());

        final int[] entityCount = {0};

        List<World> world = _plugin.getServer().getWorlds();
        world.forEach(wd -> {
            List<Entity> entities = wd.getEntities();
            entityCount[0] = entityCount[0] + entities.size();
            entities.stream().filter(Item.class::isInstance).forEach(Entity::remove);
        });

        _plugin.getServer().sendMessage(Component.text()
                .append(Utils.titleTag("垃圾車", NamedTextColor.AQUA))
                .append(Component.text(String.format("回收成功 %o 件垃圾！世界變得一片祥和了！", entityCount[0])))
                .build());

        isClearing = false;
        clearIndex = 0;
    }
}
