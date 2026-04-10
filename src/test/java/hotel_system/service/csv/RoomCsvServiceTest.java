package hotel_system.service.csv;

import hotel_system.Exception.DataExportException;
import hotel_system.Exception.DataImportException;
import hotel_system.enums.RoomCondition;
import hotel_system.enums.RoomType;
import hotel_system.model.entity.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoomCsvServiceTest {

    private RoomCsvService roomCsvService;

    @BeforeEach
    void setUp() {
        roomCsvService = new RoomCsvService();
    }

    // ==================== exportCsv() TESTS ====================

    @Test
    void exportCsv_shouldWriteRoomsToFileSuccessfully(@TempDir Path tempDir) throws Exception {
        Room room = new Room(101, RoomType.STANDARD, 100.0, 2, RoomCondition.READY, 3);
        List<Room> rooms = List.of(room);

        Path testFile = tempDir.resolve("rooms.csv");
        roomCsvService.exportCsv(rooms, testFile.toString());

        assertTrue(Files.exists(testFile));
        List<String> lines = Files.readAllLines(testFile);
        assertEquals(2, lines.size());
        assertTrue(lines.get(0).contains("number,type,price,capacity,condition,stars,available,clientId,availableDate"));
        assertTrue(lines.get(1).contains("101"));
        assertTrue(lines.get(1).contains("STANDARD"));
    }

    @Test
    void exportCsv_shouldThrowDataExportExceptionWhenFileInvalid() {
        String invalidPath = "/invalid/path/rooms.csv";
        List<Room> rooms = List.of(new Room(101, RoomType.STANDARD, 100.0, 2, RoomCondition.READY, 3));

        assertThrows(DataExportException.class, () -> roomCsvService.exportCsv(rooms, invalidPath));
    }

    @Test
    void exportCsv_shouldExportEmptyList(@TempDir Path tempDir) throws Exception {
        Path testFile = tempDir.resolve("empty.csv");
        roomCsvService.exportCsv(List.of(), testFile.toString());

        assertTrue(Files.exists(testFile));
        List<String> lines = Files.readAllLines(testFile);
        assertEquals(1, lines.size());
    }

    // ==================== importCsv() TESTS ====================

    @Test
    void importCsv_shouldReadRoomsFromFileSuccessfully(@TempDir Path tempDir) throws Exception {
        Path testFile = tempDir.resolve("rooms.csv");
        String csvContent = "number,type,price,capacity,condition,stars,available,clientId,availableDate\n" +
                "101,STANDARD,100.0,2,READY,3,true,,";
        Files.writeString(testFile, csvContent);

        List<Room> result = roomCsvService.importCsv(testFile.toString());

        assertEquals(1, result.size());
        assertEquals(101, result.get(0).getNumber());
        assertEquals(RoomType.STANDARD, result.get(0).getType());
        assertEquals(100.0, result.get(0).getPriceForDay());
    }

    @Test
    void importCsv_shouldThrowDataImportExceptionWhenFileNotFound() {
        assertThrows(DataImportException.class, () -> roomCsvService.importCsv("notexist.csv"));
    }

    @Test
    void importCsv_shouldSkipInvalidLines(@TempDir Path tempDir) throws Exception {
        Path testFile = tempDir.resolve("invalid.csv");
        String csvContent = "number,type,price,capacity,condition,stars,available,clientId,availableDate\n" +
                "invalid line\n" +
                "101,STANDARD,100.0,2,READY,3,true,,";
        Files.writeString(testFile, csvContent);

        List<Room> result = roomCsvService.importCsv(testFile.toString());

        assertEquals(1, result.size());
    }
}