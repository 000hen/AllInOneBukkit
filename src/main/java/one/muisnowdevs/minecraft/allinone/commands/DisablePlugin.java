package one.muisnowdevs.minecraft.allinone.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import one.muisnowdevs.minecraft.allinone.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginBase;

import java.util.logging.Logger;

public class DisablePlugin implements CommandExecutor {
    private final PluginBase _plugin;
    private final Logger logger;

    public DisablePlugin(PluginBase plugin) {
        this._plugin = plugin;
        this.logger = plugin.getLogger();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!commandSender.isOp()) return false;

        Player player = (Player) commandSender;

        logger.info(String.format("Plugin is disabled by player %s", player.getDisplayName()));
        Bukkit.getPluginManager().disablePlugin(_plugin);

        TextComponent fullCommandMessage = Component.text()
                .append(Component.text("插件已成功禁用，使用"))
                .append(Utils.commandComponent("/reload"))
                .append(Component.text("來重新啟用。"))
                .build();

        Utils.showMessageToPlayer(
                player,
                fullCommandMessage,
                "AllInOne");

        return true;
    }
}
