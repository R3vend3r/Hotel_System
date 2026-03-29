package hotel_system.service.csv;

import hotel_system.Exception.DataExportException;
import hotel_system.Exception.DataImportException;
import hotel_system.model.entity.Amenity;
import hotel_system.model.entity.AmenityOrder;
import hotel_system.model.entity.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AmenityOrderCsvServiceTest {

    private AmenityOrderCsvService amenityOrderCsvService;

    @BeforeEach
    void setUp() {
        amenityOrderCsvService = new AmenityOrderCsvService();
    }

    // ==================== exportCsv() TESTS ====================

    @Test
    void exportCsv_shouldWriteAmenityOrdersToFileSuccessfully(@TempDir Path tempDir) throws Exception {
        Date now = new Date();
        Client client = new Client("CL-1", "John", "Doe");
        Amenity amenity = new Amenity("AM-1", "WiFi", 15.99);
        AmenityOrder order = new AmenityOrder("ORD-1", client, 100.0, amenity, now);
        List<AmenityOrder> orders = List.of(order);

        Path testFile = tempDir.resolve("orders.csv");
        amenityOrderCsvService.exportCsv(orders, testFile.toString());

        assertTrue(Files.exists(testFile));
        List<String> lines = Files.readAllLines(testFile);
        assertEquals(2, lines.size());
        assertTrue(lines.get(0).contains("id,clientId,clientName,clientSurname,clientRoom,amenityId,amenityName,amenityPrice,creationDate,serviceDate,totalPrice"));
        assertTrue(lines.get(1).contains("ORD-1"));
        assertTrue(lines.get(1).contains("CL-1"));
        assertTrue(lines.get(1).contains("John"));
        assertTrue(lines.get(1).contains("Doe"));
        assertTrue(lines.get(1).contains("AM-1"));
        assertTrue(lines.get(1).contains("WiFi"));
    }

    @Test
    void exportCsv_shouldThrowDataExportExceptionWhenFileInvalid() {
        String invalidPath = "/invalid/path/orders.csv";
        List<AmenityOrder> orders = List.of();

        assertThrows(DataExportException.class, () -> amenityOrderCsvService.exportCsv(orders, invalidPath));
    }

    @Test
    void exportCsv_shouldEscapeSpecialCharacters(@TempDir Path tempDir) throws Exception {
        Date now = new Date();
        Client client = new Client("CL-1", "John, Jr.", "Doe");
        Amenity amenity = new Amenity("AM-1", "WiFi,Super", 15.99);
        AmenityOrder order = new AmenityOrder("ORD-1", client, 100.0, amenity, now);
        List<AmenityOrder> orders = List.of(order);

        Path testFile = tempDir.resolve("escape.csv");
        amenityOrderCsvService.exportCsv(orders, testFile.toString());

        List<String> lines = Files.readAllLines(testFile);
        assertEquals(2, lines.size());
        assertTrue(lines.get(1).contains("\"John, Jr.\""));
        assertTrue(lines.get(1).contains("\"WiFi,Super\""));
    }

    // ==================== importCsv() TESTS ====================

    @Test
    void importCsv_shouldReadAmenityOrdersFromFileSuccessfully(@TempDir Path tempDir) throws Exception {
        Path testFile = tempDir.resolve("orders.csv");
        String dateStr = "2024-01-01 10:00:00";
        String csvContent = "id,clientId,clientName,clientSurname,clientRoom,amenityId,amenityName,amenityPrice,creationDate,serviceDate,totalPrice\n" +
                "ORD-1,CL-1,John,Doe,,AM-1,WiFi,15.99," + dateStr + "," + dateStr + ",100.0";
        Files.writeString(testFile, csvContent);

        List<AmenityOrder> result = amenityOrderCsvService.importCsv(testFile.toString());

        assertEquals(1, result.size());
        assertEquals("ORD-1", result.get(0).getId());
    }

    @Test
    void importCsv_shouldReturnEmptyListWhenFileHasOnlyHeader(@TempDir Path tempDir) throws Exception {
        Path testFile = tempDir.resolve("empty.csv");
        Files.writeString(testFile, "id,clientId,clientName,clientSurname,clientRoom,amenityId,amenityName,amenityPrice,creationDate,serviceDate,totalPrice");

        List<AmenityOrder> result = amenityOrderCsvService.importCsv(testFile.toString());

        assertTrue(result.isEmpty());
    }

    @Test
    void importCsv_shouldThrowDataImportExceptionWhenFileNotFound() {
        assertThrows(DataImportException.class, () -> amenityOrderCsvService.importCsv("notexist.csv"));
    }

    @Test
    void importCsv_shouldThrowDataImportExceptionWhenInvalidNumberFormat(@TempDir Path tempDir) throws Exception {
        Path testFile = tempDir.resolve("invalid.csv");
        String csvContent = "id,clientId,clientName,clientSurname,clientRoom,amenityId,amenityName,amenityPrice,creationDate,serviceDate,totalPrice\n" +
                "ORD-1,CL-1,John,Doe,,AM-1,WiFi,invalid_price,2024-01-01,2024-01-01,100.0";
        Files.writeString(testFile, csvContent);

        assertThrows(DataImportException.class, () -> amenityOrderCsvService.importCsv(testFile.toString()));
    }

    @Test
    void importCsv_shouldHandleEscapedCommas(@TempDir Path tempDir) throws Exception {
        Path testFile = tempDir.resolve("escaped.csv");
        String dateStr = "2024-01-01 10:00:00";
        String csvContent = "id,clientId,clientName,clientSurname,clientRoom,amenityId,amenityName,amenityPrice,creationDate,serviceDate,totalPrice\n" +
                "ORD-1,CL-1,\"John, Jr.\",Doe,,AM-1,\"WiFi,Super\",15.99," + dateStr + "," + dateStr + ",100.0";
        Files.writeString(testFile, csvContent);

        List<AmenityOrder> result = amenityOrderCsvService.importCsv(testFile.toString());

        assertEquals(1, result.size());
        assertEquals("ORD-1", result.get(0).getId());
    }
}