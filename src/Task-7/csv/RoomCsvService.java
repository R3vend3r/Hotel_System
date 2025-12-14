package csv;

import enums.RoomCondition;
import enums.RoomType;
import Exception.DataExportException;
import Exception.DataImportException;
import model.Room;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RoomCsvService implements ICsvService<Room> {

    @Override
    public void exportCsv(List<Room> rooms, String filePath) throws DataExportException {
        try (PrintWriter writer = createCsvWriter(filePath)) {
            writeCsvHeader(writer);
            writeRoomsToCsv(rooms, writer);
        } catch (IOException e) {
            throw new DataExportException("Error exporting rooms: " + e.getMessage());
        }
    }

    private PrintWriter createCsvWriter(String filePath) throws IOException {
        return new PrintWriter(new File(filePath), "UTF-8");
    }

    private void writeCsvHeader(PrintWriter writer) {
        writer.println("id,number,type,price,capacity,condition,stars,available,clientId");
    }

    private void writeRoomsToCsv(List<Room> rooms, PrintWriter writer) {
        for (Room room : rooms) {
            String csvLine = formatRoomAsCsv(room);
            writer.println(csvLine);
        }
    }

    private String formatRoomAsCsv(Room room) {
        return String.format("%s,%d,%s,%.2f,%d,%s,%d,%b,%s",
                CsvUtils.escapeCsv(room.getId()),
                room.getNumberRoom(),
                room.getType().name(),
                room.getPriceForDay(),
                room.getCapacity(),
                room.getRoomCondition().name(),
                room.getStars(),
                room.isAvailable(),
                room.getClientId() != null ? CsvUtils.escapeCsv(room.getClientId()) : "");
    }

    @Override
    public List<Room> importCsv(String filePath) throws DataImportException {
        List<Room> rooms = new ArrayList<>();

        try (BufferedReader reader = createCsvReader(filePath)) {
            skipHeaderLine(reader);
            processRoomLines(reader, rooms);
        } catch (IOException | IllegalArgumentException e) {
            throw new DataImportException("Error importing rooms: " + e.getMessage());
        }

        return rooms;
    }

    private BufferedReader createCsvReader(String filePath) throws IOException {
        return new BufferedReader(new InputStreamReader(
                new FileInputStream(filePath), "UTF-8"));
    }

    private void skipHeaderLine(BufferedReader reader) throws IOException {
        reader.readLine();
    }

    private void processRoomLines(BufferedReader reader, List<Room> rooms) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            Room room = parseCsvLineToRoom(line);
            if (room != null) {
                rooms.add(room);
            }
        }
    }

    private Room parseCsvLineToRoom(String line) {
        String[] parts = CsvUtils.parseCsvLine(line);
        if (parts.length < 8) {
            return null;
        }

        Room room = createRoomFromCsv(parts);
        setRoomAvailability(parts, room);
        setRoomClientId(parts, room);

        return room;
    }

    private Room createRoomFromCsv(String[] parts) {
        return new Room(
                CsvUtils.unescapeCsv(parts[0]),
                Integer.parseInt(parts[1]),
                RoomType.valueOf(parts[2]),
                Double.parseDouble(parts[3]),
                Integer.parseInt(parts[4]),
                RoomCondition.valueOf(parts[5]),
                Integer.parseInt(parts[6]));
    }

    private void setRoomAvailability(String[] parts, Room room) {
        if (!Boolean.parseBoolean(parts[7])) {
            room.changeAvailability();
        }
    }

    private void setRoomClientId(String[] parts, Room room) {
        if (parts.length > 8 && !parts[8].isEmpty()) {
            room.setId(CsvUtils.unescapeCsv(parts[8]));
        }
    }
}