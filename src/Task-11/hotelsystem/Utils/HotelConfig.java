package hotelsystem.Utils;

import hotelsystem.dependencies.annotation.ConfigProperty;
import lombok.Getter;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class HotelConfig {
    private static final Logger logger = Logger.getLogger(HotelConfig.class);
    private static HotelConfig instance;
    // Возвращаем уже загруженные свойства
    @Getter
    private static Properties properties;

    @Getter
    @ConfigProperty(propertyName = "hotel.room.status.change.enabled")
    private boolean roomStatusChangeEnabled = true;

    @Getter
    @ConfigProperty(propertyName = "hotel.room.history.entries.max")
    private int maxHistoryEntries = 3;

    @Getter
    @ConfigProperty(propertyName = "hotel.database.file")
    private String databaseFilePath = "hotel_db.json";

    @Getter
    @ConfigProperty(propertyName = "hotel.auto.save.enabled")
    private boolean autoSaveEnabled = true;

    static {
        // Статическая инициализация загружается один раз при первом обращении к классу
        loadProperties();
    }

    private HotelConfig() {
        injectPropertyValues();
    }

    public static synchronized HotelConfig getInstance() {
        if (instance == null) {
            instance = new HotelConfig();
        }
        return instance;
    }

    private static void loadProperties() {
        if (properties != null) return;

        properties = new Properties();
        String[] configPaths = {
                System.getProperty("hotel.config.file"),
                "src/resources/hotel.properties",
                "hotel.properties"
        };

        for (String configPath : configPaths) {
            if (configPath == null) continue;

            try {
                Path path = Paths.get(configPath);
                if (Files.exists(path)) {
                    try (InputStream input = Files.newInputStream(path)) {
                        properties.load(input);
                        logger.info("Конфигурация загружена из: " + path.toAbsolutePath());
                        return;
                    }
                }
            } catch (Exception e) {
                logger.warn("Ошибка загрузки конфигурации из " + configPath, e);
            }
        }

        // Попытка загрузить из ресурсов
        try (InputStream input = HotelConfig.class.getClassLoader()
                .getResourceAsStream("hotel.properties")) {
            if (input != null) {
                properties.load(input);
                logger.info("Конфигурация загружена из ресурсов");
            } else {
                logger.warn("Файл конфигурации не найден, используются значения по умолчанию");
            }
        } catch (Exception e) {
            logger.error("Ошибка загрузки конфигурации из ресурсов", e);
        }
    }

    private void injectPropertyValues() {
        for (Field field : getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(ConfigProperty.class)) {
                ConfigProperty annotation = field.getAnnotation(ConfigProperty.class);
                String propertyName = annotation.propertyName();

                if (properties.containsKey(propertyName)) {
                    try {
                        field.setAccessible(true);
                        String value = properties.getProperty(propertyName);

                        if (field.getType() == boolean.class || field.getType() == Boolean.class) {
                            field.setBoolean(this, Boolean.parseBoolean(value));
                        } else if (field.getType() == int.class || field.getType() == Integer.class) {
                            field.setInt(this, Integer.parseInt(value));
                        } else if (field.getType() == String.class) {
                            field.set(this, value);
                        }
                    } catch (Exception e) {
                        logger.error("Ошибка при установке значения поля " + field.getName(), e);
                    }
                }
            }
        }
    }

}