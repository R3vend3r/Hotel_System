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
        String[] configPaths = {
                System.getProperty("db.config.file"),
                "src/resources/database.properties",
                "database.properties"
        };

        for (String configPath : configPaths) {
            if (configPath == null) continue;

            try {
                java.nio.file.Path path = java.nio.file.Paths.get(configPath);
                if (java.nio.file.Files.exists(path)) {
                    try (InputStream input = java.nio.file.Files.newInputStream(path)) {
                        dbProperties.load(input);
                        System.out.println("Конфигурация БД загружена из: " + path.toAbsolutePath());
                        return;
                    }
                }
            } catch (Exception e) {
                System.err.println("Ошибка загрузки конфигурации БД из " + configPath);
            }
        }

        try (InputStream input = DatabaseManager.class.getClassLoader()
                .getResourceAsStream("database.properties")) {
            if (input != null) {
                dbProperties.load(input);
                System.out.println("Конфигурация БД загружена из ресурсов");
            } else {
                throw new RuntimeException("Файл конфигурации БД не найден");
            }
        } catch (Exception e) {
            System.err.println("Фатальная ошибка загрузки конфигурации БД");
            throw new RuntimeException(e);
        }
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

    public void beginTransaction() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.setAutoCommit(false);
        }
    }

    public void commit() throws SQLException {
        connection.commit();
        connection.setAutoCommit(true);
    }

    public void rollback() {
        try {
            connection.rollback();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}