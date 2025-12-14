package csv;

import Exception.DataExportException;
import Exception.DataImportException;
import model.Client;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ClientCsvService implements ICsvService<Client> {

    @Override
    public void exportCsv(List<Client> clients, String filePath) throws DataExportException {
        try (PrintWriter writer = createCsvWriter(filePath)) {
            writeCsvHeader(writer);
            writeClientsToCsv(clients, writer);
        } catch (IOException e) {
            throw new DataExportException("Error exporting clients: " + e.getMessage());
        }
    }

    private PrintWriter createCsvWriter(String filePath) throws IOException {
        return new PrintWriter(new File(filePath), "UTF-8");
    }

    private void writeCsvHeader(PrintWriter writer) {
        writer.println("id,name,surname,roomNumber");
    }

    private void writeClientsToCsv(List<Client> clients, PrintWriter writer) {
        for (Client client : clients) {
            String csvLine = formatClientAsCsv(client);
            writer.println(csvLine);
        }
    }

    private String formatClientAsCsv(Client client) {
        return String.format("%s,%s,%s,%d",
                CsvUtils.escapeCsv(client.getId()),
                CsvUtils.escapeCsv(client.getName()),
                CsvUtils.escapeCsv(client.getSurname()),
                client.getRoomNumber());
    }

    @Override
    public List<Client> importCsv(String filePath) throws DataImportException {
        List<Client> clients = new ArrayList<>();

        try (BufferedReader reader = createCsvReader(filePath)) {
            skipHeaderLine(reader);
            processClientLines(reader, clients);
        } catch (IOException | NumberFormatException e) {
            throw new DataImportException("Error importing clients: " + e.getMessage());
        }

        return clients;
    }

    private BufferedReader createCsvReader(String filePath) throws IOException {
        return new BufferedReader(new InputStreamReader(
                new FileInputStream(filePath), "UTF-8"));
    }

    private void skipHeaderLine(BufferedReader reader) throws IOException {
        reader.readLine();
    }

    private void processClientLines(BufferedReader reader, List<Client> clients) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            Client client = parseCsvLineToClient(line);
            if (client != null) {
                clients.add(client);
            }
        }
    }

    private Client parseCsvLineToClient(String line) {
        String[] parts = CsvUtils.parseCsvLine(line);
        if (parts.length < 4) {
            return null;
        }

        return new Client(
                CsvUtils.unescapeCsv(parts[0]),
                CsvUtils.unescapeCsv(parts[1]),
                CsvUtils.unescapeCsv(parts[2]),
                Integer.parseInt(parts[3]));
    }
}