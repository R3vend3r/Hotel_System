package hotelsystem.dependencies.config;

import hotelsystem.Utils.HotelConfig;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class ConfigLoader {
    private static final Map<String, Properties> loadedConfigs = new ConcurrentHashMap<>();
    private static final String CONFIG_DIR = "src/resources/";

    public static String getProperty(String configFileName, String propertyName) {
        Properties props = loadedConfigs.computeIfAbsent(configFileName, ConfigLoader::loadConfig);
        return props.getProperty(propertyName);
    }

    private static Properties loadConfig(String fileName) {
        if (fileName.equals("hotel.properties")) {
            return HotelConfig.getProperties();
        }
        Properties props = new Properties();
        Path filePath = Paths.get(CONFIG_DIR + fileName);

        // Попытка загрузить из файловой системы
        if (Files.exists(filePath)) {
            try (InputStream input = Files.newInputStream(filePath)) {
                props.load(input);
                System.out.println("Конфиг загружен из файла: " + filePath.toAbsolutePath());
                return props;
            } catch (IOException e) {
                System.err.println("Ошибка загрузки конфига из файла: " + fileName);
            }
        }

        // Попытка загрузить из ресурсов
        try (InputStream input = ConfigLoader.class.getClassLoader()
                .getResourceAsStream(fileName)) {
            if (input != null) {
                props.load(input);
                System.out.println("Конфиг загружен из ресурсов: " + fileName);
            } else {
                System.err.println("Конфиг не найден: " + fileName);
            }
        } catch (IOException e) {
            System.err.println("Ошибка загрузки конфига из ресурсов: " + fileName);
        }

        return props;
    }
}