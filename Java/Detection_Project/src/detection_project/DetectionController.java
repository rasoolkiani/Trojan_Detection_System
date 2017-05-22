/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package detection_project;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javax.swing.JOptionPane;

/**
 *
 * @author Sherlock
 */
public class DetectionController implements Initializable {

    @FXML
    private Label label;
    
   

    @FXML
    private void handleButtonAction(ActionEvent event) {
        try {
//         SerialNativeInterface  sni = new SerialNativeInterface();
//            String[] strport = sni.getSerialPortNames();
//            for(String temp : strport){
//                System.out.println(temp);
//            }
            System.out.println("Finish");
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(null, "NO Port Found");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
    }
}
