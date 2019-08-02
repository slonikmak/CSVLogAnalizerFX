import com.oceanos.csvloganalizer.csv.CsvReader;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.ICsvListReader;
import org.supercsv.prefs.CsvPreference;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * @autor slonikmak on 31.07.2019.
 */
public class TestCsvReader {

    static String CSV_FILENAME = "C:\\Users\\Oceanos\\Desktop\\Испытания ИСТ\\data1.txt";

    static CellProcessor[] getProcessors() {
        final CellProcessor[] processors = new CellProcessor[]{
                new NotNull(),
                new ParseDouble(),
                new ParseDouble(),
                new ParseDouble()
        };

        return processors;
    }


    public static void main(String[] args) throws IOException {

        CsvReader reader = new CsvReader();

        try {
            reader.read(CSV_FILENAME);
            System.out.println(reader.getHeader());
            reader.setProcessors(getProcessors());
            List<List<Object>> records = reader.getRecords();
            List<Double> speed = reader.getColumn(records, 1, Double.class);
            speed.forEach(System.out::println);
        } finally {
            reader.close();
        }
    }
}
