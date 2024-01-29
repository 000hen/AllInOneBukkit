package one.muisnowdevs.minecraft.allinone.loc;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.AnvilGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import one.muisnowdevs.minecraft.allinone.AllInOne;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginBase;

public class SearchMenu {
    private final Player _player;
    private final AllInOne _plugin;
    private final AnvilGui _menu;
    private String _locationName;

    public SearchMenu(AllInOne plugin, Player player) {
        super();

        _plugin = plugin;
        _player = player;

        _menu = menuBuilder();
        _menu.show(_player);
    }

    private AnvilGui menuBuilder() {
        AnvilGui gui = new AnvilGui("以名稱查詢座標");

        gui.getFirstItemComponent().addPane(getFirstItem());
        gui.getResultComponent().addPane(getResultItem());

        gui.setOnNameInputChanged(newName -> {
            _locationName = newName;
            _player.sendMessage(newName);
        });
        gui.setCost((short) 0);

        return gui;
    }

    private StaticPane getResultItem() {
        StaticPane nav = new StaticPane(0, 0, 1, 1);

        ItemStack res = new ItemStack(Material.SPYGLASS);
        ItemMeta resMeta = res.getItemMeta();
        resMeta.setDisplayName("查詢");
        res.setItemMeta(resMeta);

        nav.addItem(new GuiItem(res, event -> _player.closeInventory()), 0, 0);

        return nav;
    }

    private StaticPane getFirstItem() {
        StaticPane nav = new StaticPane(0, 0, 1, 1);

        ItemStack map = new ItemStack(Material.MAP);
        ItemMeta meta = map.getItemMeta();
        meta.setDisplayName("輸入查詢的地點");
        map.setItemMeta(meta);

        nav.addItem(new GuiItem(map, event -> event.setCancelled(true)), 0, 0);

        return nav;
    }
}
