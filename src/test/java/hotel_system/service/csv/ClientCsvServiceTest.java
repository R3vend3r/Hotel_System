package hotel_system.service.csv;

import hotel_system.Exception.DataExportException;
import hotel_system.Exception.DataImportException;
import hotel_system.model.entity.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClientCsvServiceTest {

    private ClientCsvService clientCsvService;

    @BeforeEach
    void setUp() {
        clientCsvService = new ClientCsvService();
    }

    // ==================== exportCsv() TESTS ====================

    @Test
    void exportCsv_shouldWriteClientsToFileSuccessfully(@TempDir Path tempDir) throws Exception {
        List<Client> clients = List.of(
                new Client("CL-1", "John", "Doe"),
                new Client("CL-2", "Jane", "Smith")
        );

        Path testFile = tempDir.resolve("clients.csv");
        clientCsvService.exportCsv(clients, testFile.toString());

        assertTrue(Files.exists(testFile));
        List<String> lines = Files.readAllLines(testFile);
        assertEquals(3, lines.size());
        assertEquals("id,name,surname,roomNumber", lines.get(0));
        assertTrue(lines.get(1).contains("CL-1"));
        assertTrue(lines.get(1).contains("John"));
        assertTrue(lines.get(2).contains("CL-2"));
    }

    @Test
    void exportCsv_shouldThrowDataExportExceptionWhenFileInvalid() {
        String invalidPath = "/invalid/path/clients.csv";
        List<Client> clients = List.of(new Client("CL-1", "John", "Doe"));

        assertThrows(DataExportException.class, () -> clientCsvService.exportCsv(clients, invalidPath));
    }

    // ==================== importCsv() TESTS ====================

    @Test
    void importCsv_shouldReadClientsFromFileSuccessfully(@TempDir Path tempDir) throws Exception {
        Path testFile = tempDir.resolve("clients.csv");
        String csvContent = "id,name,surname,roomNumber\n" +
                "CL-1,John,Doe,\n" +
                "CL-2,Jane,Smith,";
        Files.writeString(testFile, csvContent);

        List<Client> result = clientCsvService.importCsv(testFile.toString());

        assertEquals(2, result.size());
        assertEquals("CL-1", result.get(0).getId());
        assertEquals("John", result.get(0).getName());
        assertEquals("Doe", result.get(0).getSurname());
    }

    @Test
    void importCsv_shouldThrowDataImportExceptionWhenFileNotFound() {
        assertThrows(DataImportException.class, () -> clientCsvService.importCsv("notexist.csv"));
    }

    @Test
    void importCsv_shouldHandleEscapedCommas(@TempDir Path tempDir) throws Exception {
        Path testFile = tempDir.resolve("escaped.csv");
        String csvContent = "id,name,surname,roomNumber\n" +
                "CL-1,\"John, Jr.\",Doe,";
        Files.writeString(testFile, csvContent);

        List<Client> result = clientCsvService.importCsv(testFile.toString());

        assertEquals(1, result.size());
        assertEquals("John, Jr.", result.get(0).getName());
    }
}