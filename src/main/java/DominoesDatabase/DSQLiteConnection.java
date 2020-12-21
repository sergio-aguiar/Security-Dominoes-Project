package DominoesDatabase;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.sqlite.SQLiteException;

import java.io.File;
import java.io.IOException;
import java.sql.*;

public class DSQLiteConnection
{
    /**
     * The class's Hikari Configuration instance.
     */
    private static final HikariConfig config = new HikariConfig();
    /**
     * The class's Hikari Data Source instance.
     */
    private static final HikariDataSource hikariDataSource;

    static
    {
        try
        {
            final File dbFile = new File("dominoes.db");

            if (!dbFile.exists())
            {
                if (dbFile.createNewFile()) System.out.println("\n[SERVER] Database file created successfully.");
                else System.out.println("\n[SERVER] Could not create database file.");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        config.setJdbcUrl("jdbc:sqlite:dominoes.db");
        config.setConnectionTestQuery("SELECT 1");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        hikariDataSource = new HikariDataSource(config);

        try
        {
            final Statement statement = getConnection().createStatement();

            statement.execute("CREATE TABLE IF NOT EXISTS PlayerInfo(" +
                    "identifier VARCHAR(255) PRIMARY KEY," +
                    "score INTEGER NOT NULL" +
                    ");");

            statement.execute("CREATE TABLE IF NOT EXISTS Pseudonyms(" +
                    "id INTEGER PRIMARY KEY," +
                    "identifier VARCHAR(255) NOT NULL," +
                    "pseudonym VERCHAR(255) NOT NULL" +
                    ");");
        }
        catch (SQLException e)
        {
            System.out.println("\n[SERVER] Error creating database: " + e.getErrorCode());
            System.exit(100);
        }
    }

    /**
     *  Class Constructor: SQLiteConnection.
     */
    private DSQLiteConnection() { }
    /**
     * Get function for the database connection.
     * @return An instance of a connection to the database.
     * @throws SQLException When there was a database associated issue.
     */
    public static Connection getConnection() throws SQLException
    {
        return hikariDataSource.getConnection();
    }

    /**
     * Get function for checking whether a user is registered to the database.
     * @param identifier The user's identifier.
     * @return True if the user in question is present in the database (registered), and false otherwise.
     */
    public static boolean isUserRegistered(String identifier)
    {
        boolean result = false;
        Connection conn;
        try
        {
            conn = getConnection();

            PreparedStatement preparedStatement = conn.prepareStatement("SELECT identifier FROM PlayerInfo WHERE " +
                    "identifier=?;");

            preparedStatement.setString(1, identifier);
            ResultSet resultSet = preparedStatement.executeQuery();
            result = resultSet.next();

            conn.close();
        }
        catch (SQLException e)
        {
            System.out.println("\n[SERVER] Error checking if user is registered: " + e.getErrorCode());
        }

        return result;
    }

    /**
     * Function that adds a new user to the database.
     * @param identifier The user's identifier.
     * @return True is the user was registered with success, and false otherwise.
     */
    public static boolean registerUser(String identifier)
    {
        boolean result = false;
        Connection conn;
        try
        {
            conn = getConnection();

            PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO PlayerInfo(identifier, score) " +
                    "VALUES(?,?);");

            preparedStatement.setString(1, identifier);
            preparedStatement.setInt(2,1000);
            preparedStatement.execute();
            result = true;

            conn.close();
        }
        catch (SQLException e)
        {
            System.out.println("\n[SERVER] Error registering user: " + e.getErrorCode());
        }
        return result;
    }

    public static void forceStart()
    {
        System.out.println("[SERVER] Connected to Database.");
    }
}
