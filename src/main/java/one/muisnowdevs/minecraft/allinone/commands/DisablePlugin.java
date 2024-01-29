package one.muisnowdevs.minecraft.allinone.commands;

import one.muisnowdevs.minecraft.allinone.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

        Utils.showMessageToPlayer(
                player,
                String.format("插件已成功禁用，使用 %s 來重新啟用。",
                        ChatColor.YELLOW + "/reload" + ChatColor.RESET),
                "AllInOne");

        return true;
    }
}
