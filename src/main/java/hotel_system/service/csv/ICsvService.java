package hotel_system.service.csv;

import hotel_system.Exception.DataExportException;
import hotel_system.Exception.DataImportException;

import java.util.List;

public interface ICsvService<T> {
    void exportCsv(List<T> tList, String FPath) throws DataExportException;
    List<T> importCsv(String FPath) throws DataImportException;
}
