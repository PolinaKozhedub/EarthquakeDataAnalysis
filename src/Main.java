import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
        // Парсинг CSV
        List<Earthquake> earthquakes = CsvParser.parseCsv("data/Zemletriaseniia_utf8.csv");
        System.out.println("Прочитано записей: " + earthquakes.size());

        // Подключение к базе данных и вставка данных
        DatabaseManager dbManager = new DatabaseManager();
        dbManager.connect();
        System.out.println("База данных успешно создана и подключена!");
        dbManager.insertData(earthquakes);
        System.out.println("Данные успешно сохранены в базу данных!");

        // Выполнение SQL-запросов
        Map<Integer, Double> avgEarthquakesPerYear = dbManager.getAverageEarthquakesPerYear();
        double avgMagnitudeWV = dbManager.getAverageMagnitudeForWestVirginia();
        String deepestState2013 = dbManager.getStateWithDeepestEarthquakeIn2013();

        // Вывод результатов в консоль
        System.out.println("\nРезультаты запросов:");
        System.out.println("1. Количество землетрясений по годам:");
        for (Map.Entry<Integer, Double> entry : avgEarthquakesPerYear.entrySet()) {
            System.out.println("Год: " + entry.getKey() + ", Количество: " + entry.getValue());
        }
        System.out.println("2. Средняя магнитуда для штата West Virginia: " + avgMagnitudeWV);
        System.out.println("3. Штат с самым глубоким землетрясением в 2013 году: " + deepestState2013);

        // Визуализация данных
        System.out.println("\nОтображение графика...");
        ChartVisualizer.createLineChart(avgEarthquakesPerYear);

        dbManager.close();
    }
}





