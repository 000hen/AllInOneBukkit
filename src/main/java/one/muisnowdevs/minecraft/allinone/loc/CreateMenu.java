package one.muisnowdevs.minecraft.allinone.loc;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.AnvilGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import net.kyori.adventure.text.Component;
import one.muisnowdevs.minecraft.allinone.AllInOne;
import one.muisnowdevs.minecraft.allinone.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CreateMenu {
    private final Player _player;
    private final AllInOne _plugin;
    private String _locationName;

    public CreateMenu(AllInOne plugin, Player player) {
        super();

        _plugin = plugin;
        _player = player;

        AnvilGui menu = menuBuilder();
        menu.show(_player);
    }

    private AnvilGui menuBuilder() {
        AnvilGui gui = new AnvilGui("設定座標名稱");

        gui.getFirstItemComponent().addPane(getFirstItem());
        gui.getResultComponent().addPane(getResultItem());

        gui.setOnNameInputChanged(newName -> _locationName = newName);
        gui.setCost((short) 0);

        return gui;
    }

    private StaticPane getResultItem() {
        StaticPane nav = new StaticPane(0, 0, 1, 1);

        ItemStack res = new ItemStack(Material.MAP);
        ItemMeta resMeta = res.getItemMeta();
        resMeta.displayName(Component.text("儲存地點"));
        res.setItemMeta(resMeta);

        nav.addItem(new GuiItem(res, event -> handleSave()), 0, 0);

        return nav;
    }

    private StaticPane getFirstItem() {
        StaticPane nav = new StaticPane(0, 0, 1, 1);

        ItemStack map = new ItemStack(Material.MAP);
        ItemMeta meta = map.getItemMeta();
        meta.displayName(Component.text("新地點"));
        map.setItemMeta(meta);

        nav.addItem(new GuiItem(map, event -> event.setCancelled(true)), 0, 0);

        return nav;
    }

    private void handleSave() {
        _player.closeInventory();

        Location location = _player.getLocation();
        Connection database = _plugin.getDatabase();

        try {
            PreparedStatement state = database.prepareStatement("INSERT INTO locations (player_id, name, world, x, y, z) VALUES (?, ?, ?, ?, ?, ?);");
            state.setString(1, _player.getUniqueId().toString());
            state.setString(2, _locationName);
            state.setString(3, location.getWorld().getName());
            state.setInt(4, location.getBlockX());
            state.setInt(5, location.getBlockY());
            state.setInt(6, location.getBlockZ());
            state.executeUpdate();

            Utils.showSuccessMessageToPlayer(_player, Component.text(String.format("已成功建立 %s 的座標點！", _locationName)), "建立成功");

            state.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
            Utils.showErrorMessageToPlayer(_player, Component.text("無法建立座標！請協助回報錯誤"));
        }
    }
}
