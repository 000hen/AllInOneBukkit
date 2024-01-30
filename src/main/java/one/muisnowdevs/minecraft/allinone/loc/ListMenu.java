package one.muisnowdevs.minecraft.allinone.loc;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import one.muisnowdevs.minecraft.allinone.AllInOne;
import one.muisnowdevs.minecraft.allinone.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ListMenu implements Listener {
    private final Player _player;
    private final AllInOne _plugin;
    private final ChestGui _menu;
    private final PaginatedPane _pages = new PaginatedPane(0, 0, 9, 4);

    private final HashMap<String, String> _locations = new HashMap<String, String>() {{
        put("world", "主世界");
        put("world_nether", "地獄");
        put("world_the_end", "終界");
    }};

    public ListMenu(AllInOne plugin, Player player) {
        super();

        _player = player;
        _plugin = plugin;

        _menu = menuBuilder();

        this.loadItems();
        this.addNavigation();

        _menu.show(_player);
    }

    private ChestGui menuBuilder() {
        return new ChestGui(6, "已被紀錄的座標");
    }

    private void loadItems() {
        Connection database = _plugin.getDatabase();

        String playerUid = _player.getUniqueId().toString();

        try {
            PreparedStatement statement = database.prepareStatement("SELECT name, player_id, world, x, y, z FROM locations;");
            ResultSet result = statement.executeQuery();

            ArrayList<GuiItem> items = new ArrayList<>();

            while (result.next()) {
                String name = result.getString("name");
                String playerId = result.getString("player_id");
                String world = result.getString("world");
                String x = result.getString("x");
                String y = result.getString("y");
                String z = result.getString("z");

                Player player = Bukkit.getPlayer(UUID.fromString(playerId));
                String creator;

                if (player == null) creator = "未知創建者";
                else creator = playerId.equals(playerUid) ? "您" : player.getName();

                ItemStack item = new ItemStack(Material.MAP);
                ItemMeta meta = item.getItemMeta();

                TextComponent metaName = Component.text(name)
                        .color(TextColor.color(playerId.equals(playerUid) ? 0xFF55FF : 0xFFFFFF));
                meta.displayName(metaName);

                List<TextComponent> lore = Arrays.asList(
                        Component.text(String.format("創建者: %s", creator)),
                        Component.text(String.format("世界: %s", _locations.get(world))),
                        Component.text(String.format("座標點: (%s, %s, %s)", x, y, z))
                );
                meta.lore(lore);
                item.setItemMeta(meta);

                items.add(new GuiItem(item, event -> {
                    event.setCancelled(true);
                    _player.closeInventory();

                    Utils.showMessageToPlayer(_player, Component.text(String.format(
                            "標記點 %s 在 %s 的 (%s, %s, %s)",
                            name, _locations.get(world), x, y, z)), "通知");
                }));
            }

            result.close();

            _pages.populateWithGuiItems(items);
            _menu.addPane(_pages);
        } catch (SQLException exception) {
            exception.printStackTrace();
            Utils.showErrorMessageToPlayer(_player, Component.text("無法查詢座標！請協助回報錯誤"));
        }
    }

    private void addNavigation() {
        StaticPane bar = new StaticPane(0, 4, 9, 1);

        ItemStack blackGlass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta blackGlassMeta = blackGlass.getItemMeta();
        blackGlassMeta.displayName(Component.text("　"));
        blackGlass.setItemMeta(blackGlassMeta);

        bar.fillWith(blackGlass);

        _menu.addPane(bar);

        StaticPane navigation = new StaticPane(0, 5, 9, 1);
        ItemStack arrow = new ItemStack(Material.ARROW);
        ItemMeta meta = arrow.getItemMeta();

        meta.displayName(Component.text("上一頁"));
        arrow.setItemMeta(meta);

        navigation.addItem(new GuiItem(arrow.clone(), event -> {
            event.setCancelled(true);
            if (_pages.getPage() > 0) {
                _pages.setPage(_pages.getPage() - 1);

                _menu.update();
            }
        }), 0, 0);

        meta.displayName(Component.text("下一頁"));
        arrow.setItemMeta(meta);

        navigation.addItem(new GuiItem(arrow, event -> {
            event.setCancelled(true);
            if (_pages.getPage() < _pages.getPages() - 1) {
                _pages.setPage(_pages.getPage() + 1);

                _menu.update();
            }
        }), 8, 0);

        ItemStack exit = new ItemStack(Material.DARK_OAK_DOOR);
        ItemMeta exitMeta = arrow.getItemMeta();
        exitMeta.displayName(Component.text("關閉"));
        exit.setItemMeta(exitMeta);

        navigation.addItem(new GuiItem(exit, event ->
                _player.closeInventory()), 4, 0);

        _menu.addPane(navigation);
    }
}
