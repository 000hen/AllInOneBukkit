package one.muisnowdevs.minecraft.allinone;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class Utils {
    public static final HashMap<String, String> locations = new HashMap<String, String>() {{
        put("world", "主世界");
        put("world_nether", "地獄");
        put("world_the_end", "終界");
    }};

    private static void sendMessageToPlayer(TextComponent method, TextComponent message, Player player) {
        TextComponent finalMessage = Component.text()
                .append(method)
                .append(message)
                .build();
        player.sendMessage(finalMessage);
    }

    public static void showErrorMessageToPlayer(Player player, TextComponent message) {
        sendMessageToPlayer(
                titleTag("錯誤", NamedTextColor.RED),
                message,
                player
        );
    }

    public static void showErrorMessageToPlayer(Player player, TextComponent message, String errorType) {
        sendMessageToPlayer(
                titleTag(errorType, NamedTextColor.RED),
                message,
                player
        );
    }

    public static void showSuccessMessageToPlayer(Player player, TextComponent message) {
        sendMessageToPlayer(
                titleTag("成功", NamedTextColor.GREEN),
                message,
                player
        );
    }

    public static void showSuccessMessageToPlayer(Player player, TextComponent message, String successType) {
        sendMessageToPlayer(
                titleTag(successType, NamedTextColor.GREEN),
                message,
                player
        );
    }

    public static void showMessageToPlayer(Player player, TextComponent message) {
        sendMessageToPlayer(
                titleTag("系統通知", NamedTextColor.AQUA),
                message,
                player
        );
    }

    public static void showMessageToPlayer(Player player, TextComponent message, String messageType) {
        sendMessageToPlayer(
                titleTag(messageType, NamedTextColor.AQUA),
                message,
                player
        );
    }

    public static TextComponent commandComponent(String command) {
        return Component.text()
                .append(Component.text(" "))
                .append(Component.text(command))
                .append(Component.text(" "))
                .color(NamedTextColor.YELLOW)
                .clickEvent(ClickEvent.suggestCommand(command))
                .hoverEvent(HoverEvent.showText(Component.text("點選使用指令")))
                .build();
    }

    public static TextComponent linkComponent(String _url) {
        URL url = null;
        try {
            url = new URL(_url);
        } catch (MalformedURLException exception) {
            exception.printStackTrace();
        }

        return Component.text()
                .append(Component.text(" "))
                .append(Component.text(String.format("%s%s", url.getHost(), url.getPath())))
                .append(Component.text(" "))
                .color(NamedTextColor.YELLOW)
                .clickEvent(ClickEvent.openUrl(url))
                .hoverEvent(HoverEvent.showText(Component.text("點選開啟連結")))
                .build();
    }

    public static TextComponent playerComponent(Player player) {
        return Component.text()
                .append(Component.text(" "))
                .append(Component.text(player.getName()))
                .append(Component.text(" "))
                .color(NamedTextColor.YELLOW)
                .build();
    }

    public static TextComponent titleTag(String text, TextColor color) {
        return Component.text()
                .append(Component.text("["))
                .append(Component.text(text))
                .append(Component.text("]"))
                .append(Component.text(" "))
                .color(color)
                .build();
    }
}
