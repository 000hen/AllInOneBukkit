package one.muisnowdevs.minecraft.allinone.loc;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import one.muisnowdevs.minecraft.allinone.AllInOne;
import one.muisnowdevs.minecraft.allinone.Utils;
import one.muisnowdevs.minecraft.allinone.commands.PlayerLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ListMenu {
    private final Player _player;
    private final AllInOne _plugin;
    private final String _search;
    private final PlayerLocation _commander;
    private final PaginatedPane _pages = new PaginatedPane(0, 0, 9, 4);

    private ChestGui _menu;

    public ListMenu(AllInOne plugin, PlayerLocation commander, Player player, String searchParams) {
        super();

        _player = player;
        _plugin = plugin;
        _search = searchParams;
        _commander = commander;

        this.showMenu();
    }

    public ListMenu(AllInOne plugin, PlayerLocation commander, Player player) {
        super();

        _player = player;
        _plugin = plugin;
        _commander = commander;
        _search = null;

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
            PreparedStatement statement = database.prepareStatement(
                    _search != null
                            ? "SELECT name, player_id, world, x, y, z FROM locations WHERE name LIKE ?;"
                            : "SELECT name, player_id, world, x, y, z FROM locations;"
            );
            if (_search != null) statement.setString(1, "%" + _search + "%");
            ResultSet result = statement.executeQuery();

            ArrayList<GuiItem> items = new ArrayList<>();

            while (result.next()) {
                String name = result.getString("name");
                String playerId = result.getString("player_id");
                String world = result.getString("world");
                String x = result.getString("x");
                String y = result.getString("y");
                String z = result.getString("z");

                OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(playerId));
                String creator;

                if (playerId.equals(playerUid)) creator = "您";
                else creator = player.getName();

                ItemStack item = new ItemStack(Material.MAP);
                ItemMeta meta = item.getItemMeta();

                TextComponent metaName = Component.text(name)
                        .color(playerId.equals(playerUid) ? NamedTextColor.LIGHT_PURPLE : NamedTextColor.WHITE);
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
                                .append(Component.text("來進行"))
                                .append(Component.text("傳送").color(NamedTextColor.AQUA))
                                .build(),
                        Component.text()
                                .append(Component.text("使用"))
                                .append(Component.keybind().keybind("key.mouse.right")
                                        .color(NamedTextColor.YELLOW)
                                        .decoration(TextDecoration.BOLD, true)
                                        .build())
                                .append(Component.text("將座標顯示在"))
                                .append(Component.text("聊天框").color(NamedTextColor.AQUA))
                                .append(Component.text("中"))
                                .build()
                );
                meta.lore(lore);
                item.setItemMeta(meta);

                items.add(new GuiItem(item, event -> {
                    event.setCancelled(true);
                    _player.closeInventory();

                    if (event.isLeftClick()) {
                        HashMap<UUID, Integer> storage = _commander.getTPStorage();
                        Integer times = storage.get(playerUUID);
                        if (times == null) {
                            storage.put(playerUUID, 1);
                            times = 1;
                        } else if (times == 3) {
                            Utils.showErrorMessageToPlayer(_player, Component.text("您的傳送次數已達上限，今天無法再次使用這個功能！"), "次數上限");
                            return;
                        } else {
                            storage.replace(playerUUID, ++times);
                        }

                        Location location = new Location(
                                Bukkit.getWorld(world),
                                Double.parseDouble(x),
                                Double.parseDouble(y),
                                Double.parseDouble(z));
                        _player.teleport(location);

                        Utils.showMessageToPlayer(
                                _player,
                                Component.text()
                                        .append(Component.text("您今天還剩下"))
                                        .append(Component.text(" "))
                                        .append(Component.text(String.format("%o次", 3 - times)).color(NamedTextColor.YELLOW))
                                        .append(Component.text(" "))
                                        .append(Component.text("的傳送機會"))
                                        .build());

                        Utils.showSuccessMessageToPlayer(
                                _player,
                                Component.text()
                                        .append(Component.text("已傳送至"))
                                        .append(Component.text(" "))
                                        .append(Component.text(Utils.locations.get(world)).color(NamedTextColor.YELLOW))
                                        .append(Component.text(" "))
                                        .append(Component.text("的"))
                                        .append(Component.text(" "))
                                        .append(Component.text()
                                                .append(Component.text(name))
                                                .append(Component.text(" "))
                                                .append(Component.text(String.format("(%s, %s, %s)", x, y, z)))
                                                .color(NamedTextColor.YELLOW)
                                                .build())
                                        .build(),
                                "傳送成功");
                        return;
                    }

                    if (event.isRightClick()) {
                        Utils.showMessageToPlayer(_player, Component.text(String.format(
                                "標記點 %s 在 %s 的 (%s, %s, %s)",
                                name, Utils.locations.get(world), x, y, z)), "通知");
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


        Integer timesUsed = _commander.getTPStorage().get(_player.getUniqueId());
        if (timesUsed == null) timesUsed = 0;

        ItemStack times = new ItemStack(Material.ENDER_PEARL);
        ItemMeta timesMeta = times.getItemMeta();
        timesMeta.displayName(Component.text()
                .append(Component.text("傳送剩餘次數:"))
                .append(Component.text(" "))
                .append(Component.text(String.format("%o次", 3 - timesUsed))
                        .color(NamedTextColor.YELLOW)
                        .decoration(TextDecoration.BOLD, true))
                .build());
        times.setItemMeta(timesMeta);
        navigation.addItem(new GuiItem(times, event -> event.setCancelled(true)), 5, 0);

        _menu.addPane(navigation);
    }
}
