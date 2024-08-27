package one.muisnowdevs.minecraft.allinone.commands;

import net.kyori.adventure.text.Component;
import one.muisnowdevs.minecraft.allinone.AllInOne;
import one.muisnowdevs.minecraft.allinone.Utils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlayerDeathReturnTeleport implements CommandExecutor {
    private final AllInOne _plugin;

    public PlayerDeathReturnTeleport(AllInOne plugin) {
        _plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) return false;

        int playerLevel = player.getLevel();
        IPlayerLastDeathLocation lastDeath = _plugin.getPlayerLastDeathStorage().remove(player.getUniqueId());

        if (lastDeath == null) {
            Utils.showErrorMessageToPlayer(player, Component.text("沒有記錄到您的死亡位置或您已傳送過，抱歉！"));
            return true;
        }

        if (playerLevel < 20) {
            Utils.showErrorMessageToPlayer(player, Component.text("糟糕！您沒有足夠的等級來返回您上次死亡的位置。"));
            return true;
        }

        Location location = lastDeath.getLocation();

        player.setLevel(playerLevel - 20);
        player.teleport(location);

        Utils.showSuccessMessageToPlayer(player, Component.text("傳送至上次死亡的地點"));

        return true;
    }
}
