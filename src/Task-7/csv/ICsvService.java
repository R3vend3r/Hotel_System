package csv;

import Exception.*;
import java.util.List;

public interface ICsvService<T> {
    void exportCsv(List<T> tList, String FPath) throws DataExportException;
    List<T> importCsv(String FPath) throws DataImportException;
}
