/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Alimenticio;
import models.Electronico;
import models.Producto;
import models.Proveedor;

public class FormularioController implements Initializable {

    @FXML 
    private ComboBox<String> cbTipo;
    @FXML 
    private ComboBox<Proveedor> cbProveedor;
    @FXML 
    private TextField txtCodigo;
    @FXML 
    private TextField txtMarca;
    @FXML 
    private TextField txtModelo;
    @FXML 
    private TextField txtPrecio;
    @FXML 
    private TextField txtStock;
    @FXML 
    private Label lblDatoExtra;
    @FXML 
    private TextField txtDatoExtra;
    @FXML 
    private Button btnAceptar;
    @FXML 
    private Button btnCancelar;

    private Producto productoResultante;
    private boolean esEdicion = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbTipo.getItems().add("Electronico");
        cbTipo.getItems().add("Alimenticio");
        cbTipo.getSelectionModel().selectFirst();
        actualizarLabelDatoExtra();
    }

    public void cargarProveedores(List<Proveedor> proveedores) {
        for (Proveedor p : proveedores) {
            cbProveedor.getItems().add(p);
        }
        if (!proveedores.isEmpty()) {
            cbProveedor.getSelectionModel().selectFirst();
        }
    }

    public void setProductoAEditar(Producto p) {
        this.esEdicion = true;
        txtCodigo.setText(p.getCodigo());
        txtCodigo.setDisable(true);
        
        txtMarca.setText(p.getMarca());
        txtModelo.setText(p.getModelo());
        txtPrecio.setText(String.valueOf(p.getPrecio()));
        txtStock.setText(String.valueOf(p.getStock()));
        cbProveedor.setValue(p.getProveedor());

        if (p instanceof Electronico) {
            cbTipo.setValue("Electronico");
            txtDatoExtra.setText(String.valueOf(((Electronico) p).getGarantiaMeses()));
        } else if (p instanceof Alimenticio) {
            cbTipo.setValue("Alimenticio");
            txtDatoExtra.setText(((Alimenticio) p).getFechaVencimiento());
        }
        
        actualizarLabelDatoExtra();
    }

    public Producto getProductoResultante() {
        return productoResultante;
    }

    @FXML
    private void onTipoCambiado(ActionEvent event) {
        actualizarLabelDatoExtra();
        txtDatoExtra.clear();
    }

    private void actualizarLabelDatoExtra() {
        if ("Electronico".equals(cbTipo.getValue())) {
            lblDatoExtra.setText("Garantía (Meses):");
        } else {
            lblDatoExtra.setText("Vencimiento:");
        }
    }

    @FXML
    private void aceptar(ActionEvent event) {
        try {
            String codigo = txtCodigo.getText().trim();
            String marca = txtMarca.getText().trim();
            String modelo = txtModelo.getText().trim();
            String precioStr = txtPrecio.getText().trim();
            String stockStr = txtStock.getText().trim();
            String datoExtra = txtDatoExtra.getText().trim();
            Proveedor proveedor = cbProveedor.getValue();

            if (codigo.isEmpty() || marca.isEmpty() || modelo.isEmpty() || precioStr.isEmpty() || stockStr.isEmpty() || datoExtra.isEmpty() || proveedor == null) {
                mostrarAlerta("Error", "Todos los campos son obligatorios.");
                return;
            }

            double precio = Double.parseDouble(precioStr);
            int stock = Integer.parseInt(stockStr);

            if ("Electronico".equals(cbTipo.getValue())) {
                int garantia = Integer.parseInt(datoExtra);
                productoResultante = new Electronico(codigo, marca, modelo, precio, stock, proveedor, garantia);
            } else {
                productoResultante = new Alimenticio(codigo, marca, modelo, precio, stock, proveedor, datoExtra);
            }

            cerrarVentana();

        } catch (NumberFormatException e) {
            mostrarAlerta("Error de formato", "El precio, stock y/o garantía deben ser números válidos.");
        }
    }

    @FXML
    private void cancelar(ActionEvent event) {
        productoResultante = null;
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
