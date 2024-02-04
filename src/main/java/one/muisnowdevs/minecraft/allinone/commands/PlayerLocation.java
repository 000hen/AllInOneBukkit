package one.muisnowdevs.minecraft.allinone.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import one.muisnowdevs.minecraft.allinone.AllInOne;
import one.muisnowdevs.minecraft.allinone.Utils;
import one.muisnowdevs.minecraft.allinone.loc.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PlayerLocation implements CommandExecutor {
    private final AllInOne _plugin;
    private final HashMap<UUID, IPlayerTeleport> _playerWaitTP = new HashMap<>();
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
                new SearchMenu(_plugin, this, player);
                break;

            case "remove":
                new RemoveMenu(_plugin, player);
                break;

            default:
                Player playerMentioned = Bukkit.getPlayer(strings[0]);
                if (playerMentioned == null) return false;

                UUID sessionUUID = UUID.randomUUID();

                _playerWaitTP.put(sessionUUID, new IPlayerTeleport() {
                    @Override
                    public Player getPlayer() {
                        return player;
                    }

                    @Override
                    public Player getPlayerMentioned() {
                        return playerMentioned;
                    }
                });

                TextComponent cancel = Component.text(" [拒絕] ")
                        .color(NamedTextColor.RED)
                        .clickEvent(ClickEvent.callback(event -> cancelAction(sessionUUID)))
                        .hoverEvent(HoverEvent.showText(Component.text("拒絕玩家的提案")));

                TextComponent allow = Component.text(" [允許] ")
                        .color(NamedTextColor.GREEN)
                        .clickEvent(ClickEvent.callback(event -> allowAction(sessionUUID)))
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

                Utils.showMessageToPlayer(player, Component.text("已發送傳送請求，正在等待對方回應。"));

                ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
                exec.schedule(() -> cancelAction(sessionUUID), 1, TimeUnit.MINUTES);
        }

        return true;
    }

    private void cancelAction(UUID sessionUUID) {
        IPlayerTeleport teleportObject = _playerWaitTP.get(sessionUUID);
        if (teleportObject == null) return;

        Utils.showMessageToPlayer(teleportObject.getPlayerMentioned(), Component.text("您已拒絕傳送請求"));

        _playerWaitTP.remove(sessionUUID);
        Utils.showErrorMessageToPlayer(teleportObject.getPlayer(), Component.text("玩家拒絕傳送"), "拒絕傳送");
    }

    private void allowAction(UUID sessionUUID) {
        IPlayerTeleport teleportObject = _playerWaitTP.get(sessionUUID);
        if (teleportObject == null) return;

        Utils.showMessageToPlayer(teleportObject.getPlayerMentioned(), Component.text("您已允許傳送請求，玩家將會被傳送至您的位置。"));
        Location playerMentionedLocation = teleportObject.getPlayerMentioned().getLocation();

        Utils.showSuccessMessageToPlayer(teleportObject.getPlayer(), Component.text("玩家已同意傳送"), "同意傳送");
        teleportObject.getPlayer().teleport(playerMentionedLocation);

        _playerWaitTP.remove(sessionUUID);
    }

    public HashMap<UUID, Integer> getTPStorage() {
        return this._tpTimesStorage;
    }
}
