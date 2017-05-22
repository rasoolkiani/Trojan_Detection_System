/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wizard;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.swing.JOptionPane;
import jssc.SerialNativeInterface;
import jssc.SerialPort;
import jssc.SerialPortException;

/**
 *
 * @author Rasool
 */
public class Wizard extends Application {

    Stage s;
    Scene scene1, scene2, scene3;
    SerialNativeInterface sni;
    String newlog;
    SerialPort sp;
    private String vlanID;

    @Override
    public void start(Stage stage) throws Exception {

        wizard1();

    }

    public static void main(String[] args) {
        launch(args);
    }

    private void wizard1() {

        try {
            Parent wizard1 = FXMLLoader.load(getClass().getResource("Wizard1.fxml"));
            scene1 = new Scene(wizard1);
            s.setScene(scene1);
            s.show();
            s.setTitle("Step 1");
            sni = new SerialNativeInterface();
            String[] strport = sni.getSerialPortNames();
            final ComboBox<String> serialports = (ComboBox<String>) wizard1.lookup("#serialports");
            ObservableList<String> listofports = FXCollections.observableList(Arrays.asList(strport));
            serialports.setItems(listofports);
            serialports.setValue(strport[0]);

            final ComboBox<String> bitrate = (ComboBox<String>) wizard1.lookup("#bitrate");
            String[] strbits = {"4800", "9600", "38400", "115000"};
            ObservableList<String> listofbitrate = FXCollections.observableList(Arrays.asList(strbits));
            bitrate.setItems(listofbitrate);
            bitrate.setValue(strbits[0]);
            final TextField datatbit = (TextField) wizard1.lookup("#databit");
            final TextField stopbit = (TextField) wizard1.lookup("#stopbit");
            final TextField parity = (TextField) wizard1.lookup("#parity");
            final TextField hostname = (TextField) wizard1.lookup("#hostname");

            final PasswordField enpass = (PasswordField) wizard1.lookup("#enpass");
            final CheckBox en = (CheckBox) wizard1.lookup("#en");

            en.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent t) {
                    if (en.isSelected()) {
                        enpass.setDisable(false);
                    } else {
                        enpass.setDisable(true);
                    }

                }
            });

        } catch (IOException ex) {
            Logger.getLogger(Wizard.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    boolean check;

    private boolean Connected(String port, String bitrate, String databit, String stopbit, String parity) {
        try {
            int baud = Integer.parseInt(bitrate);
            int data = Integer.parseInt(databit);
            int stop = Integer.parseInt(stopbit);
            int p = Integer.parseInt(parity);
            sp = new SerialPort(port);
            sp.openPort();
            sp.setParams(baud, data, stop, p);
            check = true;
            System.out.println("haji");

            sp.writeBytes("\r\n".getBytes());

            Thread t = new Thread() {
                @Override
                public void run() {
                    while (true) {
                        try {

                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException ex) {
                            }
                            newlog = sp.readString();
                            try {
                                if (!newlog.equals("null")) {
                                    System.out.println(newlog);
                                }
                            } catch (NullPointerException e) {

                            }

                        } catch (SerialPortException ex) {

                        }

                    }
                }

            };
            t.start();

        } catch (SerialPortException ex) {
            Logger.getLogger(Wizard.class.getName()).log(Level.SEVERE, null, ex);
        }

        return check;

    }

    public void sendconsolcommand(String command, int delay) {
        try {
            newlog = "";
            command += "\n";
            sp.writeBytes(command.getBytes());

            try {
                Thread.sleep(delay);
            } catch (InterruptedException ex) {
                Logger.getLogger(Wizard.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (SerialPortException ex) {
            Logger.getLogger(Wizard.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
