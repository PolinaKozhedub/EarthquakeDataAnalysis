import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import java.io.FileReader;
import java.util.List;

public class CsvParser {
    public static List<Earthquake> parseCsv(String filePath) throws Exception {
        CSVParser parser = new CSVParserBuilder()
                .withSeparator(',')
                .withQuoteChar('"')
                .withEscapeChar('\\')
                .build();

        CSVReader reader = new CSVReaderBuilder(new FileReader(filePath))
                .withCSVParser(parser)
                .build();

        return new com.opencsv.bean.CsvToBeanBuilder<Earthquake>(reader)
                .withType(Earthquake.class)
                .withIgnoreEmptyLine(true) // Игнорировать пустые строки
                .withThrowExceptions(false) // Не бросать исключения при ошибках в строках
                .build()
                .parse();
    }
}




