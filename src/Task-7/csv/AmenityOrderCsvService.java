package csv;

import Exception.DataExportException;
import Exception.DataImportException;
import model.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class AmenityOrderCsvService implements ICsvService<AmenityOrder> {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void exportCsv(List<AmenityOrder> orders, String filePath) throws DataExportException {
        try (PrintWriter writer = createCsvWriter(filePath)) {
            writeCsvHeader(writer);
            writeOrdersToCsv(orders, writer);
        } catch (IOException e) {
            throw new DataExportException("Error exporting amenity orders: " + e.getMessage());
        }
    }

    private PrintWriter createCsvWriter(String filePath) throws IOException {
        return new PrintWriter(new OutputStreamWriter(
                new FileOutputStream(filePath), "UTF-8"));
    }

    private void writeCsvHeader(PrintWriter writer) {
        writer.println("id,clientId,clientName,clientSurname,clientRoom,amenityId,amenityName,amenityPrice,creationDate,serviceDate,totalPrice");    }

    private void writeOrdersToCsv(List<AmenityOrder> orders, PrintWriter writer) {
        for (AmenityOrder order : orders) {
            String csvLine = formatOrderAsCsv(order);
            writer.println(csvLine);
        }
    }

    private String formatOrderAsCsv(AmenityOrder order) {
        Client client = order.getClient();
        Amenity amenity = order.getAmenity();
        return  String.format("%s,%s,%s,%s,%d,%s,%s,%.2f,%s,%s,%.2f",
                order.getId(),
                client.getId(),
                CsvUtils.escapeCsv(client.getName()),
                CsvUtils.escapeCsv(client.getSurname()),
                client.getRoomNumber(),
                amenity.getId(),
                CsvUtils.escapeCsv(amenity.getName()),
                amenity.getPrice(),
                DATE_FORMAT.format(order.getCreationDate()),
                DATE_FORMAT.format(order.getServiceDate()),
                order.getTotalPrice());
    }

    @Override
    public List<AmenityOrder> importCsv(String filePath) throws DataImportException {
        List<AmenityOrder> orders = new ArrayList<>();

        try (BufferedReader reader = createCsvReader(filePath)) {
            skipHeaderLine(reader);
            processOrderLines(reader, orders);
        } catch (Exception e) {
            throw new DataImportException("Error importing amenity orders: " + e.getMessage());
        }

        return orders;
    }

    private BufferedReader createCsvReader(String filePath) throws IOException {
        return new BufferedReader(new InputStreamReader(
                new FileInputStream(filePath), "UTF-8"));
    }

    private void skipHeaderLine(BufferedReader reader) throws IOException {
        reader.readLine();
    }

    private void processOrderLines(BufferedReader reader, List<AmenityOrder> orders) throws Exception {
        String line;
        while ((line = reader.readLine()) != null) {
            AmenityOrder order = parseCsvLineToOrder(line);
            orders.add(order);
        }
    }

    private AmenityOrder parseCsvLineToOrder(String line) throws Exception {
        String[] parts = CsvUtils.parseCsvLine(line);
        validateCsvLineFormat(parts, line);

        Client client = createClientFromCsv(parts);
        Amenity amenity = createAmenityFromCsv(parts);
        AmenityOrder order = createOrderFromCsv(parts, client, amenity);

        setOrderAdditionalFields(parts, order);
        return order;
    }

    private void validateCsvLineFormat(String[] parts, String line) throws DataImportException {
        if (parts.length < 11) {
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

    private Amenity createAmenityFromCsv(String[] parts) {
        return new Amenity(
                parts[5],
                CsvUtils.unescapeCsv(parts[6]),
                Double.parseDouble(parts[7]));
    }

    private AmenityOrder createOrderFromCsv(String[] parts, Client client, Amenity amenity) throws Exception {
        return new AmenityOrder(
                parts[0],
                client,
                Double.parseDouble(parts[10]),
                amenity,
                DATE_FORMAT.parse(parts[9]));
    }

    private void setOrderAdditionalFields(String[] parts, AmenityOrder order) throws Exception {
        order.setTotalPrice(Double.parseDouble(parts[8]));
    }
}