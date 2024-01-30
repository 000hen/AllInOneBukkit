package one.muisnowdevs.minecraft.allinone.events;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
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
            "你知道嗎？ %s 加入了！",
            "欸！看到了嗎？ %s 加入了！",
            "忽有龐然大物，拔山倒樹而來，蓋一 %s 也"
    };
    public static final String[] leaveMessage = {
            "啊啦！ %s 離開了伺服器呢！",
            "%s 消失了！",
            "WTF！ %s 怎麼離開了？",
            "さよなら！ %s",
            "%s 離開了我們的視線！"
    };
    public static final TextComponent[] tips = {
            Component.text()
                    .append(Component.text("使用"))
                    .append(Utils.commandComponent("/loc"))
                    .append(Component.text("指令來開啟座標選單！"))
                    .build(),
            Component.text()
                    .append(Component.text("找不到地點嗎？使用"))
                    .append(Utils.commandComponent("/loc list"))
                    .append(Component.text("指令查看玩家們建立的座標！"))
                    .build(),
            Component.text()
                    .append(Component.text("想要公開標記座標？使用"))
                    .append(Utils.commandComponent("/loc create"))
                    .append(Component.text("指令來建立吧！"))
                    .build(),
            Component.text()
                    .append(Component.text("使用"))
                    .append(Utils.commandComponent("/loc <player>"))
                    .append(Component.text("指令傳送至玩家旁！"))
                    .build()
    };

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String player_name = player.getDisplayName();

        event.setJoinMessage(String.format(
                "%s %s",
                ChatColor.AQUA + "[玩家加入]" + ChatColor.RESET,
                String.format(this.getRandomMessage(joinMessages), ChatColor.YELLOW + player_name + ChatColor.RESET)));

        Utils.showMessageToPlayer(
                player,
                getRandomMessage(),
                "tip");
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String player_name = player.getDisplayName();

        event.setQuitMessage(String.format(
                "%s %s",
                ChatColor.GOLD + "[玩家離開]" + ChatColor.RESET,
                String.format(this.getRandomMessage(leaveMessage), ChatColor.YELLOW + player_name + ChatColor.RESET)));
    }

    private String getRandomMessage(String[] message) {
        int rand = new Random().nextInt(message.length);
        return message[rand];
    }

    private TextComponent getRandomMessage() {
        int rand = new Random().nextInt(tips.length);
        return tips[rand];
    }
}
