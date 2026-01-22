package hotelsystem.csv;

import hotelsystem.Exception.DataExportException;
import hotelsystem.Exception.DataImportException;

import java.util.List;

public interface ICsvService<T> {
    void exportCsv(List<T> tList, String FPath) throws DataExportException;
    List<T> importCsv(String FPath) throws DataImportException;
}
