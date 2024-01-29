package one.muisnowdevs.minecraft.allinone.database;

import java.sql.*;

public class SQLConnection {
    public static final Integer VERSION = 1;

    private final Connection _driver;

    public SQLConnection(String url) throws SQLException {
        super();

        _driver = DriverManager.getConnection(url);
        this.initDatabase();
    }

    public Connection getDatabase() {
        return this._driver;
    }

    private void initDatabase() {
        try {
            PreparedStatement checkState = _driver.prepareStatement("SELECT true FROM config WHERE name = ? AND value = ?");
            checkState.setString(1, "version");
            checkState.setString(2, VERSION.toString());
            if (!checkState.executeQuery().next()) createDatabase();
        } catch (SQLException err) {
            try {
                this.createDatabase();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }

    private void createDatabase() throws SQLException {
        Statement createStatement = _driver.createStatement();
        createStatement.executeUpdate("CREATE TABLE IF NOT EXISTS locations (id INTEGER PRIMARY KEY AUTOINCREMENT, player_id VARCHAR(36) NOT NULL, name TEXT NOT NULL, timestamp DATETIME DEFAULT CURRENT_TIMESTAMP, world TEXT NOT NULL, x INTEGER NOT NULL, y INTEGER NOT NULL, z INTEGER NOT NULL);");
        createStatement.executeUpdate("CREATE TABLE IF NOT EXISTS config (name VARCHAR(32) PRIMARY KEY, value TEXT NOT NULL);");

        createStatement.close();

        PreparedStatement state = _driver.prepareStatement("INSERT INTO config (name, value) VALUES (?, ?);");
        state.setString(1, "version");
        state.setString(2, VERSION.toString());
        state.executeUpdate();

        state.close();
    }
}
