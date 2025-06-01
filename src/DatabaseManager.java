import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:earthquakes.db";
    private Connection connection;

    public void connect() throws Exception {
        connection = DriverManager.getConnection(DB_URL);
        createTables();
    }

    private void createTables() throws Exception {
        String createStatesTable = "CREATE TABLE IF NOT EXISTS States (" +
                "state_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "state_name TEXT UNIQUE NOT NULL)";
        String createMagnitudeTypesTable = "CREATE TABLE IF NOT EXISTS MagnitudeTypes (" +
                "type_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "type_name TEXT UNIQUE NOT NULL)";
        String createEarthquakesTable = "CREATE TABLE IF NOT EXISTS Earthquakes (" +
                "id TEXT PRIMARY KEY," +
                "depth REAL," +
                "magnitude REAL," +
                "state_id INTEGER," +
                "type_id INTEGER," +
                "time TEXT," +
                "FOREIGN KEY(state_id) REFERENCES States(state_id)," +
                "FOREIGN KEY(type_id) REFERENCES MagnitudeTypes(type_id))";

        Statement stmt = connection.createStatement();
        stmt.execute(createStatesTable);
        stmt.execute(createMagnitudeTypesTable);
        stmt.execute(createEarthquakesTable);
        stmt.close();
    }

    public void insertData(List<Earthquake> earthquakes) throws Exception {
        Map<String, Integer> stateIds = new HashMap<>();
        Map<String, Integer> typeIds = new HashMap<>();

        // Вставка штатов
        String insertState = "INSERT OR IGNORE INTO States (state_name) VALUES (?)";
        PreparedStatement stateStmt = connection.prepareStatement(insertState, PreparedStatement.RETURN_GENERATED_KEYS);
        for (Earthquake eq : earthquakes) {
            if (!stateIds.containsKey(eq.getState())) {
                stateStmt.setString(1, eq.getState());
                stateStmt.executeUpdate();
                ResultSet rs = stateStmt.getGeneratedKeys();
                if (rs.next()) {
                    stateIds.put(eq.getState(), rs.getInt(1));
                }
            }
        }
        stateStmt.close();

        // Вставка типов магнитуды
        String insertType = "INSERT OR IGNORE INTO MagnitudeTypes (type_name) VALUES (?)";
        PreparedStatement typeStmt = connection.prepareStatement(insertType, PreparedStatement.RETURN_GENERATED_KEYS);
        for (Earthquake eq : earthquakes) {
            if (!typeIds.containsKey(eq.getMagnitudeType())) {
                typeStmt.setString(1, eq.getMagnitudeType());
                typeStmt.executeUpdate();
                ResultSet rs = typeStmt.getGeneratedKeys();
                if (rs.next()) {
                    typeIds.put(eq.getMagnitudeType(), rs.getInt(1));
                }
            }
        }
        typeStmt.close();

        // Вставка данных о землетрясениях
        String insertEarthquake = "INSERT OR REPLACE INTO Earthquakes (id, depth, magnitude, state_id, type_id, time) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement eqStmt = connection.prepareStatement(insertEarthquake);
        for (Earthquake eq : earthquakes) {
            eqStmt.setString(1, eq.getId());
            eqStmt.setDouble(2, eq.getDepth());
            eqStmt.setDouble(3, eq.getMagnitude());
            eqStmt.setInt(4, stateIds.get(eq.getState()));
            eqStmt.setInt(5, typeIds.get(eq.getMagnitudeType()));
            eqStmt.setString(6, eq.getTime());
            eqStmt.executeUpdate();
        }
        eqStmt.close();
    }

    public Map<Integer, Double> getAverageEarthquakesPerYear() throws Exception {
        Map<Integer, Double> result = new HashMap<>();
        String query = "SELECT strftime('%Y', time) as year, COUNT(*) as count " +
                "FROM Earthquakes " +
                "GROUP BY year";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            String yearStr = rs.getString("year");
            if (yearStr != null && !yearStr.isEmpty()) {
                try {
                    int year = Integer.parseInt(yearStr);
                    double count = rs.getDouble("count");
                    result.put(year, count);
                } catch (NumberFormatException e) {
                    System.err.println("Ошибка преобразования года: " + yearStr);
                }
            }
        }
        rs.close();
        stmt.close();
        return result;
    }

    public double getAverageMagnitudeForWestVirginia() throws Exception {
        String query = "SELECT AVG(magnitude) as avg_magnitude " +
                "FROM Earthquakes e " +
                "JOIN States s ON e.state_id = s.state_id " +
                "WHERE s.state_name = 'West Virginia'";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        double avgMagnitude = rs.next() ? rs.getDouble("avg_magnitude") : 0.0;
        rs.close();
        stmt.close();
        return avgMagnitude;
    }

    public String getStateWithDeepestEarthquakeIn2013() throws Exception {
        String query = "SELECT s.state_name, e.depth " +
                "FROM Earthquakes e " +
                "JOIN States s ON e.state_id = s.state_id " +
                "WHERE strftime('%Y', e.time) = '2013' " +
                "ORDER BY e.depth DESC " +
                "LIMIT 1";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        String state = rs.next() ? rs.getString("state_name") : "Не найдено";
        rs.close();
        stmt.close();
        return state;
    }

    public Connection getConnection() {
        return connection;
    }

    public void close() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }
}
