package one.muisnowdevs.minecraft.allinone;

import one.muisnowdevs.minecraft.allinone.commands.DisablePlugin;
import one.muisnowdevs.minecraft.allinone.commands.PlayerLocation;
import one.muisnowdevs.minecraft.allinone.database.SQLConnection;
import one.muisnowdevs.minecraft.allinone.events.PlayerDeathPunish;
import one.muisnowdevs.minecraft.allinone.events.PlayerJoinMessage;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

public final class AllInOne extends JavaPlugin {

    private final Logger logger = getLogger();
    private final FileConfiguration _config = getConfig();
    private Connection _database;

    @Override
    public void onEnable() {
        logger.info("Plugin Enabled!");

        _config.addDefault(
                "sqlUrl",
                String.format(
                        "jdbc:sqlite:%s",
                        Paths.get(getDataFolder().getPath(), "database.db")
                )
        );

        try {
            _config.save(Paths.get(getDataFolder().getPath(), "config.yaml").toFile());
        } catch (IOException e) {
            logger.info("Cannot write config. Using default config instead.");
        }

        logger.info("Database initializing...");
        initDatabase();
        logger.info("Database initialized!");

        logger.info("Mounting Events...");

        this.getServer().getPluginManager().registerEvents(new PlayerJoinMessage(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerDeathPunish(), this);

        logger.info("Event mounted!");
        logger.info("Mounting Commands...");

        getCommand("loc").setExecutor(new PlayerLocation(this));
        getCommand("unloc").setExecutor(new DisablePlugin(this));

        logger.info("Command mounted!");
    }

    @Override
    public void onDisable() {
        try {
            _database.close();
        } catch (SQLException e) {
            logger.warning("Cannot close a database connection.");
        }
    }

    private void initDatabase() {
        try {
            SQLConnection sql = new SQLConnection(_config.get("sqlUrl").toString());
            _database = sql.getDatabase();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getDatabase() {
        return this._database;
    }
}
