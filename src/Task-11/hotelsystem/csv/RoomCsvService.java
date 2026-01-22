package hotelsystem.csv;

import hotelsystem.enums.RoomCondition;
import hotelsystem.enums.RoomType;
import hotelsystem.Exception.DataExportException;
import hotelsystem.Exception.DataImportException;
import hotelsystem.model.Room;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RoomCsvService implements ICsvService<Room> {
    @Override
    public void exportCsv(List<Room> rooms, String filePath) throws DataExportException {
        try (PrintWriter writer = new PrintWriter(new File(filePath), "UTF-8")) {
            // Убрали id из заголовка
            writer.println("number,type,price,capacity,condition,stars,available,clientId,availableDate");

            for (Room room : rooms) {
                writer.println(String.format("%d,%s,%.2f,%d,%s,%d,%b,%s,%s",
                        room.getNumberRoom(),
                        room.getType().name(),
                        room.getPriceForDay(),
                        room.getCapacity(),
                        room.getRoomCondition().name(),
                        room.getStars(),
                        room.isAvailable(),
                        room.getClientId() != null ? CsvUtils.escapeCsv(room.getClientId()) : "",
                        room.getAvailableDate() != null ? room.getAvailableDate().getTime() : ""));
            }
        } catch (IOException e) {
            throw new DataExportException("Error exporting rooms: " + e.getMessage());
        }
    }

    @Override
    public List<Room> importCsv(String filePath) throws DataImportException {
        List<Room> rooms = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(filePath), "UTF-8"))) {

            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = CsvUtils.parseCsvLine(line);
                if (parts.length < 7) continue; // Минимальное количество полей уменьшилось

                // Смещаем индексы на 1 влево, так как id больше нет
                Room room = new Room(
                        Integer.parseInt(parts[0]),  // numberRoom
                        RoomType.valueOf(parts[1]),  // type
                        Double.parseDouble(parts[2]), // priceForDay
                        Integer.parseInt(parts[3]),   // capacity
                        RoomCondition.valueOf(parts[4]), // condition
                        Integer.parseInt(parts[5])); // stars

                if (!Boolean.parseBoolean(parts[6])) {
                    room.setAvailable(false);
                }

                if (parts.length > 7 && !parts[7].isEmpty()) {
                    room.setClientId(CsvUtils.unescapeCsv(parts[7]));
                }

                if (parts.length > 8 && !parts[8].isEmpty()) {
                    room.setAvailableDate(new Date(Long.parseLong(parts[8])));
                }

                rooms.add(room);
            }
        } catch (IOException | IllegalArgumentException e) {
            throw new DataImportException("Error importing rooms: " + e.getMessage());
        }

        return rooms;
    }
}