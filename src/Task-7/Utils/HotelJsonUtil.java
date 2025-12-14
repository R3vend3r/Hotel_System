package Utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import model.HotelState;
import java.io.File;

public class HotelJsonUtil {
    private static final ObjectMapper mapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT)
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public static void saveState(HotelState state, String filePath) {
        try {
            mapper.writeValue(new File(filePath), state);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save hotel state", e);
        }
    }

    public static HotelState loadState(String filePath) {
        try {
            File file = new File(filePath);
            System.out.println("Путь к файлу: " + file.getAbsolutePath());
            if (file.exists()) {
                System.out.println("Файл найден, пробуем загрузить...");
                return mapper.readValue(file, HotelState.class);
            } else {
                System.out.println("⚠ Файл не существует: " + filePath);
                return null;
            }
        } catch (Exception e) {
            System.err.println("Ошибка загрузки: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}