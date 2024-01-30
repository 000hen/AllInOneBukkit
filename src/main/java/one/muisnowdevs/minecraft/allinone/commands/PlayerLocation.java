package one.muisnowdevs.minecraft.allinone.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import one.muisnowdevs.minecraft.allinone.AllInOne;
import one.muisnowdevs.minecraft.allinone.Utils;
import one.muisnowdevs.minecraft.allinone.loc.CreateMenu;
import one.muisnowdevs.minecraft.allinone.loc.ListMenu;
import one.muisnowdevs.minecraft.allinone.loc.MainMenu;
import one.muisnowdevs.minecraft.allinone.loc.SearchMenu;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

public class PlayerLocation implements CommandExecutor {
    private final AllInOne _plugin;
    private final HashMap<UUID, Player> _playerWaitTP = new HashMap<>();
    private final HashMap<UUID, Integer> _tpTimesStorage = new HashMap<>();

    public PlayerLocation(AllInOne plugin) {
        this._plugin = plugin;

        // Schedule a clear job
        Calendar calendar = Calendar.getInstance();
        long now = calendar.getTimeInMillis();

        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long offset = calendar.getTimeInMillis() - now;
        long tick = offset / 50L;

        Bukkit.getScheduler().scheduleSyncRepeatingTask(_plugin, _tpTimesStorage::clear, tick, 1728000L);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) return false;

        Player player = (Player) commandSender;

        if (strings.length == 0) {
            new MainMenu(_plugin, this, player);
            return true;
        }

        switch (strings[0]) {
            case "create":
                new CreateMenu(_plugin, player);
                break;

            case "list":
                new ListMenu(_plugin, this, player);
                break;

            case "search":
                new SearchMenu(_plugin, player);
                break;

            case "remove":
                break;

            default:
                Player playerMentioned = Bukkit.getPlayer(strings[0]);
                if (playerMentioned == null) return false;

                _playerWaitTP.put(player.getUniqueId(), playerMentioned);

                TextComponent cancel = Component.text(" [拒絕] ")
                        .color(NamedTextColor.RED)
                        .clickEvent(ClickEvent.callback(event -> cancelAction(player)))
                        .hoverEvent(HoverEvent.showText(Component.text("拒絕玩家的提案")));

                TextComponent allow = Component.text(" [允許] ")
                        .color(NamedTextColor.GREEN)
                        .clickEvent(ClickEvent.callback(event -> allowAction(player)))
                        .hoverEvent(HoverEvent.showText(Component.text("接受玩家的提案")));

                TextComponent message = Component.text()
                        .append(Component.text("玩家"))
                        .append(Utils.playerComponent(player))
                        .append(Component.text("想要傳送至您身旁。請問您允許嗎？"))
                        .build();

                TextComponent finalChoiceMessage = Component.text()
                        .append(allow)
                        .append(cancel)
                        .build();

                Utils.showMessageToPlayer(playerMentioned, message);
                playerMentioned.sendMessage(finalChoiceMessage);
        }

        return true;
    }

    private void cancelAction(Player player) {
        Player mentioned = _playerWaitTP.get(player.getUniqueId());
        if (mentioned == null) return;

        Utils.showMessageToPlayer(mentioned, Component.text("您已拒絕傳送請求"));

        _playerWaitTP.remove(player.getUniqueId());
        Utils.showErrorMessageToPlayer(player, Component.text("玩家拒絕傳送"), "拒絕傳送");
    }

    private void allowAction(Player player) {
        Player playerMentioned = _playerWaitTP.get(player.getUniqueId());
        if (playerMentioned == null) return;

        Utils.showMessageToPlayer(playerMentioned, Component.text("您已允許傳送請求，玩家將會被傳送至您的位置。"));
        Location playerMentionedLocation = playerMentioned.getLocation();

        Utils.showSuccessMessageToPlayer(player, Component.text("玩家已同意傳送"), "同意傳送");
        player.teleport(playerMentionedLocation);
    }

    private void resetStorage() {

    }

    public HashMap<UUID, Integer> getTPStorage() {
        return this._tpTimesStorage;
    }
}
