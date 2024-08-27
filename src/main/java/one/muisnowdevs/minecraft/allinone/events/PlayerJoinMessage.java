package one.muisnowdevs.minecraft.allinone.events;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import one.muisnowdevs.minecraft.allinone.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Random;

public class PlayerJoinMessage implements Listener {
    public static final String[] joinMessages = {
            "讓我們歡迎 <user> 的加入！",
            "玩家 <user> 已加入！",
            "<user> 剛剛跳入了伺服器！",
            "很高興見到你 <user>",
            "你知道嗎？ <user> 加入了！",
            "欸！看到了嗎？ <user> 加入了！",
            "忽有龐然大物，拔山倒樹而來，蓋一 <user> 也"
    };
    public static final String[] leaveMessage = {
            "啊啦！ <user> 離開了伺服器呢！",
            "<user> 消失了！",
            "WTF！ <user> 怎麼離開了？",
            "さよなら！ <user>",
            "<user> 離開了我們的視線！"
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
                    .build(),
            Component.text()
                    .append(Component.text("看不懂指令怎麼用？立即上"))
                    .append(Utils.linkComponent("https://allinone.muisnow.dev/usage/commands"))
                    .append(Component.text("查看指令的使用方法！"))
                    .build()
    };

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String player_name = player.getName();

        Component message = MiniMessage.miniMessage()
                .deserialize(
                        this.getRandomMessage(joinMessages),
                        Placeholder.component("user",
                                Component.text(player_name)
                                        .color(NamedTextColor.YELLOW)
                        )
                );

        event.joinMessage(
                Utils.messageWithHeader(Utils.titleTag("玩家加入", NamedTextColor.AQUA), (TextComponent) message));

        Utils.showMessageToPlayer(
                player,
                getRandomMessage(),
                "tip");
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String player_name = player.getName();

        Component message = MiniMessage.miniMessage()
                .deserialize(
                        this.getRandomMessage(leaveMessage),
                        Placeholder.component("user",
                                Component.text(player_name)
                                        .color(NamedTextColor.YELLOW)
                        )
                );

        event.quitMessage(
                Utils.messageWithHeader(Utils.titleTag("玩家離開", NamedTextColor.GOLD), (TextComponent) message));
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
