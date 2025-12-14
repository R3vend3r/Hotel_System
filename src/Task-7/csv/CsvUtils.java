package csv;

import java.util.ArrayList;
import java.util.List;

public class CsvUtils {

    public static String escapeCsv(String value) {
        if (value == null) return "";

        String escaped = value.replace("\"", "\"\"");
        boolean needsQuoting = escaped.contains(",") || escaped.contains("\"");

        return needsQuoting ? "\"" + escaped + "\"" : escaped;
    }

    public static String unescapeCsv(String value) {
        if (value == null || value.isEmpty()) return value;

        if (value.startsWith("\"") && value.endsWith("\"")) {
            value = value.substring(1, value.length() - 1);
        }
        return value.replace("\"\"", "\"");
    }

    public static String[] parseCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder currentField = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);

            if (ch == '\"') {
                processQuoteCharacter(line, i, currentField, inQuotes);
                if (isDoubleQuote(line, i, inQuotes)) {
                    i++; // Skip the next quote
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (ch == ',' && !inQuotes) {
                fields.add(currentField.toString());
                currentField.setLength(0);
            } else {
                currentField.append(ch);
            }
        }

        fields.add(currentField.toString());
        return fields.toArray(new String[0]);
    }

    private static void processQuoteCharacter(String line, int index,
                                              StringBuilder currentField, boolean inQuotes) {
        if (inQuotes && index + 1 < line.length() && line.charAt(index + 1) == '\"') {
            currentField.append('\"');
        }
    }

    private static boolean isDoubleQuote(String line, int index, boolean inQuotes) {
        return inQuotes && index + 1 < line.length() && line.charAt(index + 1) == '\"';
    }
}