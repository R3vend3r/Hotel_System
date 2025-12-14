package hotelsystem.dependencies.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConfigLoader {
    private final Map<String, Properties> loadedConfigs = new HashMap<>();


    private Properties loadConfig(String fileName) {
        Properties props = new Properties();

        try {
            java.nio.file.Path path = Paths.get(fileName);
            if (Files.exists(path)) {
                try (InputStream input = Files.newInputStream(path)) {
                    props.load(input);
                    System.out.println("Config loaded from filesystem: " + path.toAbsolutePath());
                    return props;
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading config from filesystem: " + fileName);
        }

        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream(fileName)) {
            if (input != null) {
                props.load(input);
                System.out.println("Config loaded from resources: " + fileName);
            } else {
                System.err.println("Config file not found in resources: " + fileName);
            }
        } catch (IOException e) {
            System.err.println("Error loading config from resources: " + fileName);
        }

        return props;
    }

    public String getProperty(String configFileName, String propertyName) {
        Properties props = loadedConfigs.computeIfAbsent(configFileName, this::loadConfig);
        return props.getProperty(propertyName);
    }
}