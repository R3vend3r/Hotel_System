package csv;

import Exception.DataExportException;
import Exception.DataImportException;
import model.Amenity;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AmenityCsvService implements ICsvService<Amenity> {

    @Override
    public void exportCsv(List<Amenity> amenities, String filePath) throws DataExportException {
        try (PrintWriter writer = createWriter(filePath)) {
            writeCsvHeader(writer);
            writeAmenitiesData(amenities, writer);
        } catch (IOException e) {
            throw new DataExportException("Error exporting amenities: " + e.getMessage());
        }
    }

    private PrintWriter createWriter(String filePath) throws IOException {
        return new PrintWriter(new File(filePath), "UTF-8");
    }

    private void writeCsvHeader(PrintWriter writer) {
        writer.println("id,name,price");
    }

    private void writeAmenitiesData(List<Amenity> amenities, PrintWriter writer) {
        for (Amenity amenity : amenities) {
            String csvLine = formatAmenityAsCsv(amenity);
            writer.println(csvLine);
        }
    }

    private String formatAmenityAsCsv(Amenity amenity) {
        return String.format("%s,%s,%.2f",
                CsvUtils.escapeCsv(amenity.getId()),
                CsvUtils.escapeCsv(amenity.getName()),
                amenity.getPrice());
    }

    @Override
    public List<Amenity> importCsv(String filePath) throws DataImportException {
        List<Amenity> amenities = new ArrayList<>();

        try (BufferedReader reader = createReader(filePath)) {
            skipHeaderLine(reader);
            processCsvLines(reader, amenities);
        } catch (IOException | NumberFormatException e) {
            throw new DataImportException("Error importing amenities: " + e.getMessage());
        }

        return amenities;
    }

    private BufferedReader createReader(String filePath) throws IOException {
        return new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
    }

    private void skipHeaderLine(BufferedReader reader) throws IOException {
        reader.readLine(); // Skip header
    }

    private void processCsvLines(BufferedReader reader, List<Amenity> amenities) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            Amenity amenity = parseCsvLineToAmenity(line);
            if (amenity != null) {
                amenities.add(amenity);
            }
        }
    }

    private Amenity parseCsvLineToAmenity(String line) {
        String[] parts = CsvUtils.parseCsvLine(line);
        if (parts.length < 3) return null;

        return new Amenity(
                CsvUtils.unescapeCsv(parts[0]),
                CsvUtils.unescapeCsv(parts[1]),
                Double.parseDouble(parts[2]));
    }
}