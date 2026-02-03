package hotelsystem.Utils;

import hotelsystem.Exception.DatabaseException;
import lombok.Getter;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@Getter
public class DatabaseManager {
    private static DatabaseManager instance;
    private Connection connection;
    private static Properties dbProperties;

    static {
        loadDatabaseProperties();
    }

    private DatabaseManager() throws DatabaseException {
        initializeConnection();
    }

    private static void loadDatabaseProperties() {
        if (dbProperties != null) return;

        dbProperties = new Properties();

        if (tryLoadFromConfigPaths()) {
            return;
        }

        if (tryLoadFromClasspath()) {
            return;
        }

        throw new RuntimeException("Файл конфигурации БД не найден");
    }

    private static boolean tryLoadFromConfigPaths() {
        String[] configPaths = getConfigPaths();

        for (String configPath : configPaths) {
            if (configPath == null) continue;

            if (tryLoadFromFile(configPath)) {
                System.out.println("Конфигурация БД загружена из: " + configPath);
                return true;
            }
        }

        return false;
    }

    private static String[] getConfigPaths() {
        return new String[] {
                System.getProperty("db.config.file"),
                "src/resources/database.properties",
                "database.properties"
        };
    }

    private static boolean tryLoadFromFile(String configPath) {
        try {
            java.nio.file.Path path = java.nio.file.Paths.get(configPath);
            if (!java.nio.file.Files.exists(path)) {
                return false;
            }

            try (InputStream input = java.nio.file.Files.newInputStream(path)) {
                dbProperties.load(input);
                return true;
            }

        } catch (Exception e) {
            logConfigLoadError(configPath, e);
            return false;
        }
    }

    private static boolean tryLoadFromClasspath() {
        try (InputStream input = DatabaseManager.class.getClassLoader()
                .getResourceAsStream("database.properties")) {

            if (input == null) {
                return false;
            }

            dbProperties.load(input);
            System.out.println("Конфигурация БД загружена из ресурсов");
            return true;

        } catch (Exception e) {
            logClasspathLoadError(e);
            return false;
        }
    }

    private static void logConfigLoadError(String configPath, Exception e) {
        System.err.println("Ошибка загрузки конфигурации БД из " + configPath +
                ": " + e.getMessage());
    }

    private static void logClasspathLoadError(Exception e) {
        System.err.println("Фатальная ошибка загрузки конфигурации БД из classpath: " +
                e.getMessage());
    }

    private void initializeConnection() throws DatabaseException {
        try {
            String url = dbProperties.getProperty("jdbc.url");
            String user = dbProperties.getProperty("jdbc.username");
            String password = dbProperties.getProperty("jdbc.password");
            String driver = dbProperties.getProperty("jdbc.driver");

            Class.forName(driver);
            this.connection = DriverManager.getConnection(url, user, password);

            try (var stmt = connection.createStatement()) {
                stmt.execute("SELECT 1");
            }
            System.out.println("✓ Подключение к PostgreSQL успешно");
        } catch (Exception e) {
            throw new DatabaseException("Ошибка подключения к БД", e);
        }
    }

    public static synchronized DatabaseManager getInstance() throws DatabaseException, SQLException {
        if (instance == null || instance.connection == null || instance.connection.isClosed()) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при закрытии соединения: " + e.getMessage());
        }
    }
}