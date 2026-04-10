package hotel_system.service.csv;

import hotel_system.Exception.DataExportException;
import hotel_system.Exception.DataImportException;
import hotel_system.enums.RoomType;
import hotel_system.model.entity.Client;
import hotel_system.model.entity.Room;
import hotel_system.model.entity.RoomBooking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoomBookingCsvServiceTest {

    private RoomBookingCsvService roomBookingCsvService;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @BeforeEach
    void setUp() {
        roomBookingCsvService = new RoomBookingCsvService();
    }

    // ==================== exportCsv() TESTS ====================

    @Test
    void exportCsv_shouldWriteRoomBookingsToFileSuccessfully(@TempDir Path tempDir) throws Exception {
        Date now = new Date();
        Client client = new Client("CL-1", "John", "Doe");
        Room room = new Room(101, RoomType.STANDARD, 100.0, 2);
        RoomBooking booking = new RoomBooking("BK-1", client, room, 500.0, now, now);
        List<RoomBooking> bookings = List.of(booking);

        Path testFile = tempDir.resolve("bookings.csv");
        roomBookingCsvService.exportCsv(bookings, testFile.toString());

        assertTrue(Files.exists(testFile));
        List<String> lines = Files.readAllLines(testFile);
        assertEquals(2, lines.size());
        assertTrue(lines.get(0).contains("id,clientId,clientName,clientSurname,clientRoom,roomNumber,roomType,roomPrice,checkInDate,checkOutDate,totalPrice"));
        assertTrue(lines.get(1).contains("BK-1"));
        assertTrue(lines.get(1).contains("CL-1"));
        assertTrue(lines.get(1).contains("John"));
    }

    @Test
    void exportCsv_shouldThrowDataExportExceptionWhenFileInvalid() {
        String invalidPath = "/invalid/path/bookings.csv";
        List<RoomBooking> bookings = List.of();

        assertThrows(DataExportException.class, () -> roomBookingCsvService.exportCsv(bookings, invalidPath));
    }

    @Test
    void exportCsv_shouldEscapeSpecialCharacters(@TempDir Path tempDir) throws Exception {
        Date now = new Date();
        Client client = new Client("CL-1", "John, Jr.", "Doe");
        Room room = new Room(101, RoomType.STANDARD, 100.0, 2);
        RoomBooking booking = new RoomBooking("BK-1", client, room, 500.0, now, now);
        List<RoomBooking> bookings = List.of(booking);

        Path testFile = tempDir.resolve("escape.csv");
        roomBookingCsvService.exportCsv(bookings, testFile.toString());

        List<String> lines = Files.readAllLines(testFile);
        assertEquals(2, lines.size());
        assertTrue(lines.get(1).contains("\"John, Jr.\""));
    }

    // ==================== importCsv() TESTS ====================

    @Test
    void importCsv_shouldReadRoomBookingsFromFileSuccessfully(@TempDir Path tempDir) throws Exception {
        Path testFile = tempDir.resolve("bookings.csv");
        String dateStr = "2024-01-01 10:00:00";
        String csvContent = "id,clientId,clientName,clientSurname,clientRoom,roomNumber,roomType,roomPrice,checkInDate,checkOutDate,totalPrice\n" +
                "BK-1,CL-1,John,Doe,,101,STANDARD,100.0," + dateStr + "," + dateStr + ",500.0";
        Files.writeString(testFile, csvContent);

        List<RoomBooking> result = roomBookingCsvService.importCsv(testFile.toString());

        assertEquals(1, result.size());
        assertEquals("BK-1", result.get(0).getId());
    }

    @Test
    void importCsv_shouldReturnEmptyListWhenFileHasOnlyHeader(@TempDir Path tempDir) throws Exception {
        Path testFile = tempDir.resolve("empty.csv");
        Files.writeString(testFile, "id,clientId,clientName,clientSurname,clientRoom,roomNumber,roomType,roomPrice,checkInDate,checkOutDate,totalPrice");

        List<RoomBooking> result = roomBookingCsvService.importCsv(testFile.toString());

        assertTrue(result.isEmpty());
    }

    @Test
    void importCsv_shouldThrowDataImportExceptionWhenFileNotFound() {
        assertThrows(DataImportException.class, () -> roomBookingCsvService.importCsv("notexist.csv"));
    }

    @Test
    void importCsv_shouldThrowDataImportExceptionWhenInvalidNumberFormat(@TempDir Path tempDir) throws Exception {
        Path testFile = tempDir.resolve("invalid.csv");
        String csvContent = "id,clientId,clientName,clientSurname,clientRoom,roomNumber,roomType,roomPrice,checkInDate,checkOutDate,totalPrice\n" +
                "BK-1,CL-1,John,Doe,,101,STANDARD,invalid_price,2024-01-01,2024-01-01,500.0";
        Files.writeString(testFile, csvContent);

        assertThrows(DataImportException.class, () -> roomBookingCsvService.importCsv(testFile.toString()));
    }

    @Test
    void importCsv_shouldHandleEscapedCommas(@TempDir Path tempDir) throws Exception {
        Path testFile = tempDir.resolve("escaped.csv");
        String dateStr = "2024-01-01 10:00:00";
        String csvContent = "id,clientId,clientName,clientSurname,clientRoom,roomNumber,roomType,roomPrice,checkInDate,checkOutDate,totalPrice\n" +
                "BK-1,CL-1,\"John, Jr.\",Doe,,101,STANDARD,100.0," + dateStr + "," + dateStr + ",500.0";
        Files.writeString(testFile, csvContent);

        List<RoomBooking> result = roomBookingCsvService.importCsv(testFile.toString());

        assertEquals(1, result.size());
        assertEquals("BK-1", result.get(0).getId());
    }
}