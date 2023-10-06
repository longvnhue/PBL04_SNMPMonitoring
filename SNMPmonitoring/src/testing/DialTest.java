package testing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import javax.swing.JFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.dial.ArcDialFrame;
import org.jfree.chart.plot.dial.DialPlot;
import org.jfree.chart.plot.dial.DialPointer;
import org.jfree.chart.plot.dial.StandardDialRange;
import org.jfree.chart.plot.dial.StandardDialScale;
import org.jfree.data.general.DefaultValueDataset;

/**
 * @see https://stackoverflow.com/a/70648615/230513
 * @see https://stackoverflow.com/a/10353270/230513
 */
public class DialTest {

    private static final Color LT_GRAY = new Color(0xe0e0e0);

    private void display() {
        DefaultValueDataset data = new DefaultValueDataset(70);
        DialPlot plot = new DialPlot(data);
        plot.setView(0, -0.25, 1, 1);
        ArcDialFrame arcDialFrame = new ArcDialFrame();
        arcDialFrame.setInnerRadius(0.42);
        arcDialFrame.setOuterRadius(0.95);
        arcDialFrame.setForegroundPaint(Color.darkGray);
        plot.setDialFrame(arcDialFrame);
        StandardDialScale scale = new StandardDialScale(0, 100, 180, -180, 10, 0);
        scale.setTickRadius(0.95);
        scale.setTickLabelOffset(0.15);
        scale.setMajorTickIncrement(10);
        plot.addScale(0, scale);
        DialPointer.Pin pin = new DialPointer.Pin();
        pin.setPaint(Color.black);
        pin.setRadius(0.8);
        plot.addLayer(pin);
        plot.addLayer(new StandardDialRange(0, 40, Color.red));
        plot.addLayer(new StandardDialRange(40, 60, Color.yellow));
        plot.addLayer(new StandardDialRange(60, 100, Color.green));

        JFreeChart chart = new JFreeChart("Overall Performance", plot);
        chart.setBackgroundPaint(LT_GRAY);

        JFrame f = new JFrame("Meter Test");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new ChartPanel(chart) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(300, 300);
            }
        });
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new DialTest()::display);
    }
}