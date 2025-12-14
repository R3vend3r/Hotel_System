package Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public class HotelConfig {
    private static final String PROPERTIES_FILE = "hotel.properties";
    private static final Properties properties = new Properties();

    private static final boolean DEFAULT_ROOM_STATUS_CHANGE = true;
    private static final int DEFAULT_MAX_HISTORY_ENTRIES = 3;
    private static final String DEFAULT_DB_FILE = "hotel_db.json";

    static {
        try {
            File configFile = new File(PROPERTIES_FILE);
            if (configFile.exists()) {
                try (InputStream input = new FileInputStream(configFile)) {
                    properties.load(input);
                }
            } else {
                try (InputStream input = HotelConfig.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
                    if (input != null) {
                        properties.load(input);
                    } else {
                        System.out.println("⚠ Конфигурационный файл не найден. Используются значения по умолчанию.");
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("⚠ Ошибка чтения конфигурации. Используются значения по умолчанию.");
        }
    }

    public static boolean isRoomStatusChangeEnabled() {
        return Boolean.parseBoolean(properties.getProperty(
                "hotel.room.status.change.enabled",
                String.valueOf(DEFAULT_ROOM_STATUS_CHANGE)));
    }

    public static int getMaxHistoryEntries() {
        try {
            return Integer.parseInt(properties.getProperty(
                    "hotel.room.history.entries.max",
                    String.valueOf(DEFAULT_MAX_HISTORY_ENTRIES)));
        } catch (NumberFormatException e) {
            System.out.println("⚠ Некорректное значение history entries. Используется значение по умолчанию.");
            return DEFAULT_MAX_HISTORY_ENTRIES;
        }
    }

    public static String getDatabaseFilePath() {
        String path = properties.getProperty("hotel.database.file", DEFAULT_DB_FILE);

        File file = new File(path);
        if (file.exists()) {
            return path;
        }

        return DEFAULT_DB_FILE;
    }

    public static boolean isAutoSaveEnabled() {
        return Boolean.parseBoolean(properties.getProperty(
                "hotel.auto.save.enabled",
                "true"));
    }
}