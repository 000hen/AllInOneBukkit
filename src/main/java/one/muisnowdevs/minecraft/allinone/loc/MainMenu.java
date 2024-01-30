package one.muisnowdevs.minecraft.allinone.loc;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import net.kyori.adventure.text.Component;
import one.muisnowdevs.minecraft.allinone.AllInOne;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MainMenu implements Listener {
    private final Player _player;
    private final AllInOne _plugin;
    private final OutlinePane _navigation = new OutlinePane(2, 1, 5, 1);

    public MainMenu(AllInOne plugin, Player player) {
        super();

        _plugin = plugin;
        _player = player;

        ChestGui menu = menuBuilder();

        this.createNewLocation();
        this.removeLocation();
        this.searchLocation();
        this.listLocation();
        this.closeMenu();

        menu.addPane(_navigation);
        menu.show(_player);
    }

    private ChestGui menuBuilder() {
        return new ChestGui(3, "座標紀錄系統");
    }

    private void createNewLocation() {
        ItemStack map = new ItemStack(Material.MAP);
        ItemMeta beaconMeta = map.getItemMeta();
        beaconMeta.displayName(Component.text("建立新座標"));
        map.setItemMeta(beaconMeta);

        _navigation.addItem(new GuiItem(map, event -> {
            _player.sendMessage("create new location");
            new CreateMenu(_plugin, _player);
        }));
    }

    private void removeLocation() {
        ItemStack barrier = new ItemStack(Material.BARRIER);
        ItemMeta beaconMeta = barrier.getItemMeta();
        beaconMeta.displayName(Component.text("刪除已建立的座標"));
        barrier.setItemMeta(beaconMeta);

        _navigation.addItem(new GuiItem(barrier, event -> _player.sendMessage("remove location")));
    }

    private void searchLocation() {
        ItemStack glass = new ItemStack(Material.SPYGLASS);
        ItemMeta beaconMeta = glass.getItemMeta();
        beaconMeta.displayName(Component.text("查詢已建立的座標"));
        glass.setItemMeta(beaconMeta);

        _navigation.addItem(new GuiItem(glass, event -> {
            _player.sendMessage("search location");
            new SearchMenu(_plugin, _player);
        }));
    }

    private void listLocation() {
        ItemStack paper = new ItemStack(Material.PAPER);
        ItemMeta beaconMeta = paper.getItemMeta();
        beaconMeta.displayName(Component.text("所有已建立的座標"));
        paper.setItemMeta(beaconMeta);

        _navigation.addItem(new GuiItem(paper, event -> {
            _player.sendMessage("list location");
            new ListMenu(_plugin, _player);
        }));
    }

    private void closeMenu() {
        ItemStack paper = new ItemStack(Material.DARK_OAK_DOOR);
        ItemMeta beaconMeta = paper.getItemMeta();
        beaconMeta.displayName(Component.text("關閉選單"));
        paper.setItemMeta(beaconMeta);

        _navigation.addItem(new GuiItem(paper, event -> _player.closeInventory()));
    }
}
