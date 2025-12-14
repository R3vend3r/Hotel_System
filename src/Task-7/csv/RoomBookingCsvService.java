package csv;

import Exception.DataExportException;
import Exception.DataImportException;
import enums.RoomType;
import model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class RoomBookingCsvService implements ICsvService<RoomBooking> {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void exportCsv(List<RoomBooking> bookings, String filePath) throws DataExportException {
        try (PrintWriter writer = createCsvWriter(filePath)) {
            writeCsvHeader(writer);
            writeBookingsToCsv(bookings, writer);
        } catch (IOException e) {
            throw new DataExportException("Error exporting room bookings: " + e.getMessage());
        }
    }

    private PrintWriter createCsvWriter(String filePath) throws IOException {
        return new PrintWriter(new OutputStreamWriter(
                new FileOutputStream(filePath), StandardCharsets.UTF_8));
    }

    private void writeCsvHeader(PrintWriter writer) {
        writer.println("id,clientId,clientName,clientSurname,clientRoom,roomNumber,roomType,roomPrice,checkInDate,checkOutDate,totalPrice");
    }

    private void writeBookingsToCsv(List<RoomBooking> bookings, PrintWriter writer) {
        for (RoomBooking booking : bookings) {
            String csvLine = formatBookingAsCsv(booking);
            writer.println(csvLine);
        }
    }

    private String formatBookingAsCsv(RoomBooking booking) {
        Client client = booking.getClient();
        Room room = booking.getRoom();
        return String.format("%s,%s,%s,%s,%d,%d,%s,%.2f,%s,%s,%.2f",
                booking.getId(),
                client.getId(),
                CsvUtils.escapeCsv(client.getName()),
                CsvUtils.escapeCsv(client.getSurname()),
                client.getRoomNumber(),
                room.getNumberRoom(),
                room.getType(),
                room.getPriceForDay(),
                DATE_FORMAT.format(booking.getCreationDate()),
                DATE_FORMAT.format(booking.getCheckOutDate()),
                booking.getTotalPrice());
    }

    @Override
    public List<RoomBooking> importCsv(String filePath) throws DataImportException {
        List<RoomBooking> bookings = new ArrayList<>();

        try (BufferedReader reader = createCsvReader(filePath)) {
            skipHeaderLine(reader);
            processBookingLines(reader, bookings);
        } catch (Exception e) {
            throw new DataImportException("Error importing room bookings: " + e.getMessage());
        }

        return bookings;
    }

    private BufferedReader createCsvReader(String filePath) throws IOException {
        return new BufferedReader(new InputStreamReader(
                new FileInputStream(filePath), StandardCharsets.UTF_8));
    }

    private void skipHeaderLine(BufferedReader reader) throws IOException {
        reader.readLine();
    }

    private void processBookingLines(BufferedReader reader, List<RoomBooking> bookings) throws Exception {
        String line;
        while ((line = reader.readLine()) != null) {
            RoomBooking booking = parseCsvLineToBooking(line);
            bookings.add(booking);
        }
    }

    private RoomBooking parseCsvLineToBooking(String line) throws Exception {
        String[] parts = CsvUtils.parseCsvLine(line);
        validateCsvLineFormat(parts, line);

        Client client = createClientFromCsv(parts);
        Room room = createRoomFromCsv(parts);
        RoomBooking booking = createBookingFromCsv(parts, client, room);

        setBookingAdditionalFields(parts, booking);
        return booking;
    }

    private void validateCsvLineFormat(String[] parts, String line) throws DataImportException {
        if (parts.length < 12) {
            throw new DataImportException("Invalid data format in line: " + line);
        }
    }

    private Client createClientFromCsv(String[] parts) {
        return new Client(
                parts[1],
                CsvUtils.unescapeCsv(parts[2]),
                CsvUtils.unescapeCsv(parts[3]),
                Integer.parseInt(parts[4]));
    }

    private Room createRoomFromCsv(String[] parts) {
        return new Room(
                Integer.parseInt(parts[5]),
                RoomType.valueOf(parts[6]),
                Double.parseDouble(parts[7]),
                1);
    }

    private RoomBooking createBookingFromCsv(String[] parts, Client client, Room room) throws Exception {
        return new RoomBooking(
                parts[0],
                client,
                room,
                Double.parseDouble(parts[11]),
                DATE_FORMAT.parse(parts[9]),
                DATE_FORMAT.parse(parts[10]));
    }

    private void setBookingAdditionalFields(String[] parts, RoomBooking booking) throws Exception {
        booking.setTotalPrice(Double.parseDouble(parts[8]));
    }
}