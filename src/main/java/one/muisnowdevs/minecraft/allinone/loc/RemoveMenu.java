package one.muisnowdevs.minecraft.allinone.loc;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import one.muisnowdevs.minecraft.allinone.AllInOne;
import one.muisnowdevs.minecraft.allinone.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RemoveMenu {
    private final Player _player;
    private final AllInOne _plugin;
    private final PaginatedPane _pages = new PaginatedPane(0, 0, 9, 4);

    private ChestGui _menu;
    private boolean _canRemove = true;

    public RemoveMenu(AllInOne plugin, Player player) {
        super();

        _player = player;
        _plugin = plugin;

        this.showMenu();
    }

    private void showMenu() {
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

        UUID playerUUID = _player.getUniqueId();
        String playerUid = playerUUID.toString();

        try {
            PreparedStatement statement = database.prepareStatement("SELECT id, name, world, x, y, z FROM locations WHERE player_id = ?;");
            statement.setString(1, playerUid);
            ResultSet result = statement.executeQuery();

            ArrayList<GuiItem> items = new ArrayList<>();

            while (result.next()) {
                Integer id = result.getInt("id");
                String name = result.getString("name");
                String world = result.getString("world");
                String x = result.getString("x");
                String y = result.getString("y");
                String z = result.getString("z");

                String creator = "您";

                ItemStack item = new ItemStack(Material.MAP);
                ItemMeta meta = item.getItemMeta();

                TextComponent metaName = Component.text(name)
                        .color(NamedTextColor.LIGHT_PURPLE);
                meta.displayName(metaName);

                List<TextComponent> lore = Arrays.asList(
                        Component.text(String.format("創建者: %s", creator)),
                        Component.text(String.format("世界: %s", Utils.locations.get(world))),
                        Component.text(String.format("座標點: (%s, %s, %s)", x, y, z)),
                        Component.text("　"),
                        Component.text()
                                .append(Component.text("使用"))
                                .append(Component.keybind().keybind("key.mouse.left")
                                        .color(NamedTextColor.YELLOW)
                                        .decoration(TextDecoration.BOLD, true)
                                        .build())
                                .append(Component.text("來"))
                                .append(Component.text("刪除").color(NamedTextColor.RED))
                                .build()
                );
                meta.lore(lore);
                item.setItemMeta(meta);

                items.add(new GuiItem(item, event -> {
                    event.setCancelled(true);
                    _player.closeInventory();

                    if (event.isLeftClick()) {
                        Location location = new Location(
                                Bukkit.getWorld(world),
                                Double.parseDouble(x),
                                Double.parseDouble(y),
                                Double.parseDouble(z));

                        Utils.showMessageToPlayer(_player, Component.text()
                                .append(Component.text("您即將刪除座標點"))
                                .append(Component.text(" "))
                                .append(Component.text(name))
                                .append(Component.text(" "))
                                .append(Component.text("是位於"))
                                .append(Utils.formatLocation(location))
                                .build());
                        Utils.showErrorMessageToPlayer(_player, Component.text()
                                .append(Component.text("為了避免您錯誤的刪除該座標，請再次點擊"))
                                .append(Component.text(" [確認刪除] ")
                                        .color(NamedTextColor.RED)
                                        .clickEvent(ClickEvent.callback(removeEvent -> removeLocation(id)))
                                        .hoverEvent(HoverEvent.showText(Component.text("點擊即可刪除座標點，請注意此操作將無法回復。"))))
                                .build(), "刪除確認");

                        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
                        exec.schedule(this::cancelAction, 1, TimeUnit.MINUTES);
                    }
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

        ItemStack blackGlass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta blackGlassMeta = blackGlass.getItemMeta();
        blackGlassMeta.displayName(Component.text("　"));
        blackGlass.setItemMeta(blackGlassMeta);

        bar.fillWith(blackGlass);
        bar.setOnClick(event -> event.setCancelled(true));
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
        ItemMeta exitMeta = exit.getItemMeta();
        exitMeta.displayName(Component.text("關閉"));
        exit.setItemMeta(exitMeta);

        navigation.addItem(new GuiItem(exit, event ->
                _player.closeInventory()), 4, 0);

        _menu.addPane(navigation);
    }

    private void removeLocation(Integer id) {
        if (!_canRemove) {
            Utils.showErrorMessageToPlayer(_player, Component.text("無法刪除該地點，可能是該地點已被刪除，或是已超過可刪除之時效。"));
            return;
        }

        Connection database = _plugin.getDatabase();
        String playerUID = _player.getUniqueId().toString();

        try {
            PreparedStatement statement = database.prepareStatement("DELETE FROM locations WHERE id = ? AND player_id = ?");
            statement.setInt(1, id);
            statement.setString(2, playerUID);
            statement.executeUpdate();

            statement.close();
            Utils.showSuccessMessageToPlayer(_player, Component.text("已刪除座標點！"), "刪除成功");

            _canRemove = false;
        } catch (SQLException exception) {
            exception.printStackTrace();
            Utils.showErrorMessageToPlayer(_player, Component.text("無法刪除座標！請協助回報錯誤"));
        }
    }

    private void cancelAction() {
        if (!_canRemove) return;

        Utils.showErrorMessageToPlayer(_player, Component.text("已超過確認刪除的時間，如要刪除請再次操作。"), "刪除失敗");
        _canRemove = false;
    }
}
