package one.muisnowdevs.minecraft.allinone;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;

public class Utils {
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
