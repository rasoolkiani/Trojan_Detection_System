/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package detection_project;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import jssc.SerialNativeInterface;
import jssc.SerialPort;
import jssc.SerialPortException;

/**
 *
 * @author Sherlock
 */
public class Detection_Project extends Application {

    String log = "A";
    boolean detect = false;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Detection.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("Detection Project ... University Of Isfahan...");
        final Label label = (Label) root.lookup("#label");
        try {
            String ports = "";
            SerialNativeInterface sni = new SerialNativeInterface();
            String[] strport = sni.getSerialPortNames();
            for (int i = 1; i < strport.length + 1; i++) {
                ports += i + " - " + strport[i - 1] + "\n";
            }
            int i = Integer.parseInt(JOptionPane.showInputDialog(null, "Select a Port By id \n\n" + ports, "Give Me a Port", JOptionPane.QUESTION_MESSAGE));
            final SerialPort sp = new SerialPort(strport[i - 1]);
            sp.openPort();
            sp.setParams(9600, 8, 1, 0);

            Thread t = new Thread() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            sp.writeString("E");
                            log = "";
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException ex) {
                            }
                            log = sp.readString();
                            try {
                                if (!log.equals("null")) {
                                    if (log.contains("A")) {
                                        detect = true;
                                        System.out.println(log);

                                        Platform.runLater(new Runnable() {
                                            @Override
                                            public void run() {

                                                label.setStyle("-fx-border-color:red; -fx-background-color: White;");
                                                label.setText("Trojan Detected");

                                            }
                                        });
                                        break;
                                    } else if (log.contains("C")) {
                                        detect = false;
                                        System.out.println(log);

                                        Platform.runLater(new Runnable() {
                                            @Override
                                            public void run() {

                                                label.setStyle("-fx-border-color:green; -fx-background-color: White;");
                                                label.setText("Trojan Not Found");

                                            }
                                        });
                                        break;
                                    }


                                }
                            } catch (NullPointerException e) {
                            }

                        } catch (SerialPortException ex) {
                        }

                    }
                }
            };
            t.start();


            Button exit = (Button) root.lookup("#exit");
            exit.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {

                    System.exit(0);


                }
            });
        } catch (SerialPortException ex) {
            Logger.getLogger(DetectionController.class.getName()).log(Level.SEVERE, null, ex);
        }

        stage.show();

    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}