import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import javax.swing.JFrame;
import java.util.Map;

public class ChartVisualizer {
    public static void createLineChart(Map<Integer, Double> data) {
        XYSeries series = new XYSeries("Количество землетрясений");
        for (Map.Entry<Integer, Double> entry : data.entrySet()) {
            series.add(entry.getKey(), entry.getValue());
        }

        XYDataset dataset = new XYSeriesCollection(series);
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Количество землетрясений по годам",
                "Год",
                "Количество",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false
        );

        JFrame frame = new JFrame("График землетрясений");
        frame.setContentPane(new ChartPanel(chart));
        frame.setSize(800, 600);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}
