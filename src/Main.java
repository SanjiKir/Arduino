import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;
import javax.swing.*;
import com.fazecast.jSerialComm.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


/**
 * Created by wooow on 09.11.2016.
 */


public class Main {

    public static void main(String[] args) {
        JFrame window = new GUIArduinoVer().getWindow();

        window.setVisible(true);
    }
}