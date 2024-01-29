package one.muisnowdevs.minecraft.allinone;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Utils {
    private static void sendMessageToPlayer(String method, String message, Player player) {
        player.sendMessage(String.format("%s %s", method, message));
    }

    public static void showErrorMessageToPlayer(Player player, String message) {
        sendMessageToPlayer(ChatColor.RED + "[error]" + ChatColor.RESET, message, player);
    }

    public static void showErrorMessageToPlayer(Player player, String message, String errorType) {
        sendMessageToPlayer(String.format(ChatColor.RED + "[%s]" + ChatColor.RESET, errorType), message, player);
    }

    public static void showSuccessMessageToPlayer(Player player, String message) {
        sendMessageToPlayer(ChatColor.GREEN + "[success]" + ChatColor.RESET, message, player);
    }

    public static void showSuccessMessageToPlayer(Player player, String message, String successType) {
        sendMessageToPlayer(String.format(ChatColor.GREEN + "[%s]" + ChatColor.RESET, successType), message, player);
    }

    public static void showMessageToPlayer(Player player, String message) {
        sendMessageToPlayer(ChatColor.AQUA + "[info]" + ChatColor.RESET, message, player);
    }

    public static void showMessageToPlayer(Player player, String message, String messageType) {
        sendMessageToPlayer(String.format(ChatColor.AQUA + "[%s]" + ChatColor.RESET, messageType), message, player);
    }
}
