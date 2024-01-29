package one.muisnowdevs.minecraft.allinone.events;

import one.muisnowdevs.minecraft.allinone.Utils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Random;

public class PlayerJoinMessage implements Listener {
    public static final String[] joinMessages = {
            "讓我們歡迎 %s 的加入！",
            "玩家 %s 已加入！",
            "%s 剛剛跳入了伺服器！",
            "很高興見到你 %s",
            "欸！看到了嗎？ %s 加入了！"
    };
    public static final String[] leaveMessage = {
            "啊啦！ %s 離開了伺服器呢！",
            "%s 消失了！",
            "WTF！ %s 怎麼離開了？"
    };

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String player_name = player.getDisplayName();

        event.setJoinMessage(String.format(ChatColor.YELLOW + this.getRandomMessage(joinMessages), player_name));
        Utils.showMessageToPlayer(
                player,
                String.format("使用 %s 指令來建立與查詢玩家們所建立的座標吧！", ChatColor.YELLOW + "/loc" + ChatColor.RESET),
                "tip");
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String player_name = player.getDisplayName();

        event.setQuitMessage(String.format(ChatColor.YELLOW + this.getRandomMessage(leaveMessage), player_name));
    }

    private String getRandomMessage(String[] message) {
        int rand = new Random().nextInt(message.length);
        return message[rand];
    }
}
