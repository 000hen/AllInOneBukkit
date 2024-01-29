package one.muisnowdevs.minecraft.allinone.commands;

import one.muisnowdevs.minecraft.allinone.AllInOne;
import one.muisnowdevs.minecraft.allinone.loc.CreateMenu;
import one.muisnowdevs.minecraft.allinone.loc.ListMenu;
import one.muisnowdevs.minecraft.allinone.loc.MainMenu;
import one.muisnowdevs.minecraft.allinone.loc.SearchMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Connection;

public class PlayerLocation implements CommandExecutor {
    private final AllInOne _plugin;

    public PlayerLocation(AllInOne plugin) {
        this._plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) return false;

        Player player = (Player) commandSender;

        if (strings.length == 0) {
            new MainMenu(_plugin, player);
            return true;
        }

        switch (strings[0]) {
            case "create":
                new CreateMenu(_plugin, player);
                break;

            case "list":
                new ListMenu(_plugin, player);
                break;

            case "search":
                new SearchMenu(_plugin, player);
                break;

            case "remove":
                break;

            default:
                return false;
        }

        return true;
    }
}
