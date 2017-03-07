import com.fazecast.jSerialComm.SerialPort;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Timer;

/**
 * Created by wooow on 13.11.2016.
 */
public class GUIArduinoVer {
    private JFrame window;
    private static int x = 0;
    private static int ID =0;
    private DatabaseConnect databaseConnect;
    private DateFormat dateFormat;
    private Calendar cal;
    private Label label;
    private SerialPort chosenPort;;
    private ArrayList<Integer> listOfValues;
    private XYSeries series;
    public GUIArduinoVer(){
        initGUI();
    }

    class Graph extends TimerTask {
        public void run() {
            for (Integer i : listOfValues)
            {
            series.add(x++, i);
            }
            window.repaint();
            listOfValues.clear();
        }
    }

    private void initGUI() {

        // create and configure the window
        window = new JFrame();
        window.setTitle("Sensor Graph GUI");
        window.setSize(1000, 700);
        window.setLayout(new BorderLayout());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // create a drop-down box and connect button, then place them at the top of the window
        JComboBox<String> portList = new JComboBox<String>();
        JButton connectButton = new JButton("Connect");
        JPanel topPanel = new JPanel();
        topPanel.add(portList);
        topPanel.add(connectButton);
        window.add(topPanel, BorderLayout.NORTH);

        // populate the drop-down box
        SerialPort[] portNames = SerialPort.getCommPorts();
        for (int i = 0; i < portNames.length; i++)
            portList.addItem(portNames[i].getSystemPortName());

        //setUp database connection
        databaseConnect = new DatabaseConnect();
        databaseConnect.databaseSetup();

        // create the line graph
        series = new XYSeries("Movement Sensor Readings");
        XYSeriesCollection dataset = new XYSeriesCollection(series);
        JFreeChart chart = ChartFactory.createXYLineChart("Movement Sensor Readings", "Time (seconds)", "ADC Reading", dataset);
        window.add(new ChartPanel(chart), BorderLayout.CENTER);

        // create the TextArea for arduino text outputs
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        window.add(textArea, BorderLayout.SOUTH);

        //creating label for show if there is movement or not
        label = new Label("Movement detection");
        window.add(label, BorderLayout.EAST);

        //Setting up the date format
        dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        listOfValues = new ArrayList<Integer>();

        // set button on Action
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (connectButton.getText().equals("Connect")){
                    //connect to serial port
                    chosenPort = SerialPort.getCommPort(portList.getSelectedItem().toString());
                    chosenPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
                    if(chosenPort.openPort()){
                        connectButton.setText("Disconnect");
                        portList.setEnabled(false);
                    }
                    //New thread creating for graph updating
                    Thread thread = new Thread(){
                        @Override
                        public void run(){
                            Scanner scanner = new Scanner(chosenPort.getInputStream());
                            Timer timer = new Timer();
                            timer.schedule(new Graph(), 0, 10000);
                            while(scanner.hasNextLine()){
                                try {
                                    String line = scanner.nextLine();
                                    System.out.println(line);
                                    int number = Integer.parseInt(line);
                                    listOfValues.add(number);
                               //     series.add(x++, number);
                                    ID = databaseConnect.getLastKeyValue()+1;
                                    databaseConnect.insertDatBaseValues(ID, number);
                                    switch (number){
                                        case 1:
                                            label.setBackground(Color.GREEN);
                                            label.setText("Movement detected");
                                            cal = Calendar.getInstance();
                                            textArea.append("Movement detected at " + dateFormat.format(cal.getTime()) +"\n");
                                            break;
                                        case 0:
                                            label.setBackground(Color.RED);
                                            label.setText("No movement");
                                    }


                                }
                                catch (Exception e){
                                    //e.printStackTrace();
                                }
                            }
                            scanner.close();
                        }
                    };
                    thread.start();
                }
                else {
                    // disconnect the serial port
                    chosenPort.closePort();
                    portList.setEnabled(true);
                    connectButton.setText("Connect");
                    // to clear the graph
                    //series.clear();
                    //x =0;
                }
            }
        });
    }

    public JFrame getWindow(){
        return window;
    }
}
