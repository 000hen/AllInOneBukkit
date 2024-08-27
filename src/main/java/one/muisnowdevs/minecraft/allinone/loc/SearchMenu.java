package one.muisnowdevs.minecraft.allinone.loc;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.AnvilGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import net.kyori.adventure.text.Component;
import one.muisnowdevs.minecraft.allinone.AllInOne;
import one.muisnowdevs.minecraft.allinone.commands.PlayerLocation;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SearchMenu {
    private final Player _player;
    private final AllInOne _plugin;
    private final PlayerLocation _commander;
    private String _locationName;

    public SearchMenu(AllInOne plugin, PlayerLocation commander, Player player) {
        super();

        _plugin = plugin;
        _player = player;
        _commander = commander;

        AnvilGui _menu = menuBuilder();
        _menu.show(_player);
    }

    private AnvilGui menuBuilder() {
        AnvilGui gui = new AnvilGui("以名稱查詢座標");

        gui.getFirstItemComponent().addPane(getFirstItem());
        gui.getResultComponent().addPane(getResultItem());

        gui.setOnNameInputChanged(newName -> _locationName = newName);
        gui.setCost((short) 0);

        return gui;
    }

    private StaticPane getResultItem() {
        StaticPane nav = new StaticPane(0, 0, 1, 1);

        ItemStack res = new ItemStack(Material.SPYGLASS);
        ItemMeta resMeta = res.getItemMeta();
        resMeta.displayName(Component.text("查詢"));
        res.setItemMeta(resMeta);

        nav.addItem(new GuiItem(res, event -> {
            event.setCancelled(true);
            new ListMenu(_plugin, _commander, _player, _locationName);
        }), 0, 0);

        return nav;
    }

    private StaticPane getFirstItem() {
        StaticPane nav = new StaticPane(0, 0, 1, 1);

        ItemStack map = new ItemStack(Material.MAP);
        ItemMeta meta = map.getItemMeta();
        meta.displayName(Component.text("輸入查詢的地點"));
        map.setItemMeta(meta);

        nav.addItem(new GuiItem(map, event -> event.setCancelled(true)), 0, 0);

        return nav;
    }
}
