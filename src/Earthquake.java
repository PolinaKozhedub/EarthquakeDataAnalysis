import com.opencsv.bean.CsvBindByName;

public class Earthquake {
    @CsvBindByName(column = "ID")
    private String id;
    @CsvBindByName(column = "Глубина в метрах")
    private double depth;
    @CsvBindByName(column = "Тип магнитуды")
    private String magnitudeType;
    @CsvBindByName(column = "Магнитуда")
    private double magnitude;
    @CsvBindByName(column = "Штат")
    private String state;
    @CsvBindByName(column = "Время")
    private String time;

    // Геттеры и сеттеры
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public double getDepth() { return depth; }
    public void setDepth(double depth) { this.depth = depth; }
    public String getMagnitudeType() { return magnitudeType; }
    public void setMagnitudeType(String magnitudeType) { this.magnitudeType = magnitudeType; }
    public double getMagnitude() { return magnitude; }
    public void setMagnitude(double magnitude) { this.magnitude = magnitude; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
}
