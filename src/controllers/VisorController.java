/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class VisorController implements Initializable {

    @FXML
    private TextArea txtAreaReporte;

    @FXML
    private Button btnCerrarVisor;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtAreaReporte.setEditable(false);
    }

    public void setTextoVisible(String texto) {
        txtAreaReporte.setText(texto);
    }

    @FXML
    private void cerrar(ActionEvent event) {
        Stage stage = (Stage) btnCerrarVisor.getScene().getWindow();
        stage.close();
    }
}
