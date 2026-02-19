package hotel_system.Utils;

import hotel_system.Exception.DatabaseException;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@Getter
public class DatabaseManager {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseManager.class);
    private static DatabaseManager instance;
    private final Connection connection;
    private static Properties dbProperties;

    static {
        loadDatabaseProperties();
    }

    private DatabaseManager() throws DatabaseException {
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
            logger.info("✓ Подключение к PostgreSQL успешно");
        } catch (Exception e) {
            logger.error("Ошибка подключения к БД", e);
            throw new RuntimeException("Ошибка подключения к БД", e);
        }    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null || instance.connection == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                logger.info("Соединение с БД закрыто.");
            }
        } catch (SQLException e) {
            logger.error("Ошибка при закрытии соединения", e);
        }
    }

    public void beginTransaction() throws SQLException {
        connection.setAutoCommit(false);
        logger.debug("Начало транзакции");
    }

    public void commit() throws SQLException {
        connection.commit();
        connection.setAutoCommit(true);
        logger.debug("Транзакция завершена успешно");
    }

    public void rollback() {
        try {
            if (connection != null && !connection.getAutoCommit()) {
                connection.rollback();
                connection.setAutoCommit(true);
                logger.debug("Транзакция откачена");
            }
        } catch (SQLException e) {
            logger.error("Ошибка при откате транзакции", e);
        }
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
                logger.info("Конфигурация БД загружена из: {}", configPath);
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
            logger.warn("Ошибка загрузки конфигурации БД из {}", configPath, e);
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
            logger.info("Конфигурация БД загружена из ресурсов");
            return true;

        } catch (Exception e) {
            logger.error("Фатальная ошибка загрузки конфигурации БД из classpath", e);
            return false;
        }
    }
}