package hotel_system.service.csv;

import hotel_system.Exception.DataExportException;
import hotel_system.Exception.DataImportException;
import hotel_system.model.entity.Amenity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AmenityCsvServiceTest {

    private AmenityCsvService amenityCsvService;

    @BeforeEach
    void setUp() {
        amenityCsvService = new AmenityCsvService();
    }

    // ==================== exportCsv() TESTS ====================

    @Test
    void exportCsv_shouldWriteAmenitiesToFileSuccessfully(@TempDir Path tempDir) throws Exception {
        List<Amenity> amenities = List.of(
                new Amenity("AM-1", "WiFi", 15.99),
                new Amenity("AM-2", "Pool", 50.00)
        );

        Path testFile = tempDir.resolve("amenities.csv");
        amenityCsvService.exportCsv(amenities, testFile.toString());

        assertTrue(Files.exists(testFile));
        List<String> lines = Files.readAllLines(testFile);
        assertEquals(3, lines.size());
        assertEquals("id,name,price", lines.get(0));
        assertTrue(lines.get(1).contains("AM-1"));
        assertTrue(lines.get(1).contains("WiFi"));
        assertTrue(lines.get(2).contains("AM-2"));
        assertTrue(lines.get(2).contains("Pool"));
    }

    @Test
    void exportCsv_shouldThrowDataExportExceptionWhenFileInvalid() {
        String invalidPath = "/invalid/path/amenities.csv";
        List<Amenity> amenities = List.of(new Amenity("AM-1", "WiFi", 15.99));

        assertThrows(DataExportException.class, () -> amenityCsvService.exportCsv(amenities, invalidPath));
    }

    @Test
    void exportCsv_shouldEscapeSpecialCharacters(@TempDir Path tempDir) throws Exception {
        Amenity amenity = new Amenity("AM-1", "WiFi,Super", 15.99);
        List<Amenity> amenities = List.of(amenity);

        Path testFile = tempDir.resolve("escape.csv");
        amenityCsvService.exportCsv(amenities, testFile.toString());

        List<String> lines = Files.readAllLines(testFile);
        assertEquals(2, lines.size());
        assertTrue(lines.get(1).contains("\"WiFi,Super\""));
    }

    // ==================== importCsv() TESTS ====================

    @Test
    void importCsv_shouldReadAmenitiesFromFileSuccessfully(@TempDir Path tempDir) throws Exception {
        Path testFile = tempDir.resolve("amenities.csv");
        String csvContent = "id,name,price\n" +
                "AM-1,WiFi,15.99\n" +
                "AM-2,Pool,50.00";
        Files.writeString(testFile, csvContent);

        List<Amenity> result = amenityCsvService.importCsv(testFile.toString());

        assertEquals(2, result.size());
        assertEquals("AM-1", result.get(0).getId());
        assertEquals("WiFi", result.get(0).getName());
        assertEquals(15.99, result.get(0).getPrice());
        assertEquals("AM-2", result.get(1).getId());
        assertEquals("Pool", result.get(1).getName());
        assertEquals(50.00, result.get(1).getPrice());
    }

    @Test
    void importCsv_shouldReturnEmptyListWhenFileHasOnlyHeader(@TempDir Path tempDir) throws Exception {
        Path testFile = tempDir.resolve("empty.csv");
        Files.writeString(testFile, "id,name,price");

        List<Amenity> result = amenityCsvService.importCsv(testFile.toString());

        assertTrue(result.isEmpty());
    }

    @Test
    void importCsv_shouldThrowDataImportExceptionWhenFileNotFound() {
        assertThrows(DataImportException.class, () -> amenityCsvService.importCsv("notexist.csv"));
    }

    @Test
    void importCsv_shouldThrowDataImportExceptionWhenInvalidNumberFormat(@TempDir Path tempDir) throws Exception {
        Path testFile = tempDir.resolve("invalid.csv");
        String csvContent = "id,name,price\n" +
                "AM-1,WiFi,invalid_price";
        Files.writeString(testFile, csvContent);

        assertThrows(DataImportException.class, () -> amenityCsvService.importCsv(testFile.toString()));
    }

    @Test
    void importCsv_shouldHandleEscapedCommas(@TempDir Path tempDir) throws Exception {
        Path testFile = tempDir.resolve("escaped.csv");
        String csvContent = "id,name,price\n" +
                "AM-1,\"WiFi,Super\",15.99";
        Files.writeString(testFile, csvContent);

        List<Amenity> result = amenityCsvService.importCsv(testFile.toString());

        assertEquals(1, result.size());
        assertEquals("AM-1", result.get(0).getId());
        assertEquals("WiFi,Super", result.get(0).getName());
        assertEquals(15.99, result.get(0).getPrice());
    }
}