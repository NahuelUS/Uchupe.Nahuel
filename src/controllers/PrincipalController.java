/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controllers;

import exceptions.CodigoDuplicadoException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Producto;
import models.Proveedor;
import services.GestorArchivos;

public class PrincipalController implements Initializable {

    @FXML 
    private ListView<Producto> listViewProductos;
    
    @FXML 
    private Button btnAgregar;
    @FXML 
    private Button btnModificar;
    @FXML 
    private Button btnEliminar;
    @FXML 
    private Button btnExportarCaros;
    @FXML 
    private Button btnVerTotal;
    @FXML 
    private Button btnVerReporte;

    private List<Producto> inventario;
    private List<Proveedor> proveedores;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inventario = new ArrayList<>();
        proveedores = new ArrayList<>();
        cargarDatosIniciales();
        actualizarListaVista();
    }

    private void cargarDatosIniciales() {
        try {
            proveedores = GestorArchivos.leerProveedores();
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudieron cargar los proveedores.");
        }

        try {
            inventario = GestorArchivos.leerProductos();
        } catch (Exception e) {
            if (e instanceof FileNotFoundException) {
                System.out.println("Primer inicio: no se encontró archivo de productos. Se creará al guardar.");
            } else {
                mostrarAlerta("Advertencia", "Hubo un error al leer los productos guardados. El archivo podría estar corrupto.");
            }
        }
    }

    private void actualizarListaVista() {
        listViewProductos.getItems().clear();
        for (Producto p : inventario) {
            listViewProductos.getItems().add(p);
        }
    }

    @FXML
    private void agregarProducto(ActionEvent event) {
        Producto nuevo = abrirFormulario(null);
        if (nuevo != null) {
            try {
                validarCodigoDuplicado(nuevo.getCodigo());
                inventario.add(nuevo);
                guardarCambios();
                actualizarListaVista();
            } catch (CodigoDuplicadoException e) {
                mostrarAlerta("Código Duplicado", e.getMessage());
            }
        }
    }

    @FXML
    private void modificarProducto(ActionEvent event) {
        Producto seleccionado = listViewProductos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Atención", "Debe seleccionar un producto para modificar.");
            return;
        }

        Producto modificado = abrirFormulario(seleccionado);
        if (modificado != null) {
            inventario.remove(seleccionado);
            inventario.add(modificado);
            guardarCambios();
            actualizarListaVista();
        }
    }

    @FXML
    private void eliminarProducto(ActionEvent event) {
        Producto seleccionado = listViewProductos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Atención", "Debe seleccionar un producto para eliminar.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar Eliminación");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Está seguro de eliminar el producto seleccionado?");
        
        if (confirmacion.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            inventario.remove(seleccionado);
            guardarCambios();
            actualizarListaVista();
        }
    }

    @FXML
    private void exportarCaros(ActionEvent event) {
        try {
            GestorArchivos.exportarProductosCaros(inventario);
            mostrarAlertaInfo("Éxito", "Productos superiores a $500.000 exportados correctamente.");
        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo exportar el archivo.");
        }
    }

    @FXML
    private void verReporteCaros(ActionEvent event) {
        try {
            String contenido = GestorArchivos.leerReporteCaros();
            if (contenido.trim().isEmpty()) {
                contenido = "El archivo está vacío. No hay productos con valor superior a $500.000.";
            }
            abrirVisor("Reporte de Productos Caros", contenido);
        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo leer el archivo. Asegúrese de hacer clic en 'Exportar' primero.");
        }
    }

    @FXML
    private void verTotalInventario(ActionEvent event) {
        double total = 0;
        int cantidadArticulos = 0;
        
        for (Producto p : inventario) {
            total += (p.getPrecio() * p.getStock());
            cantidadArticulos += p.getStock();
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("=== RESUMEN FINANCIERO DEL INVENTARIO ===\n\n")
          .append("Cantidad total de artículos en stock: ").append(cantidadArticulos).append(" unidades\n")
          .append("Valorización total del inventario: $").append(total);
        
        abrirVisor("Valor Total del Inventario", sb.toString());
    }

    private Producto abrirFormulario(Producto productoAEditar) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/formulario.fxml"));
            Parent root = loader.load();
            
            FormularioController controller = loader.getController();
            controller.cargarProveedores(proveedores);
            
            if (productoAEditar != null) {
                controller.setProductoAEditar(productoAEditar);
            }

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(productoAEditar == null ? "Agregar Producto" : "Modificar Producto");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            return controller.getProductoResultante();

        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo abrir la ventana del formulario.");
            return null;
        }
    }

    private void abrirVisor(String titulo, String texto) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/visor.fxml"));
            Parent root = loader.load();
            
            VisorController controller = loader.getController();
            controller.setTextoVisible(texto);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(titulo);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo abrir el visor.");
        }
    }

    private void validarCodigoDuplicado(String codigo) throws CodigoDuplicadoException {
        for (Producto p : inventario) {
            if (p.getCodigo().equalsIgnoreCase(codigo)) {
                throw new CodigoDuplicadoException("Ya existe un producto registrado con el código: " + codigo);
            }
        }
    }

    private void guardarCambios() {
        try {
            GestorArchivos.guardarProductos(inventario);
        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudieron guardar los cambios en el archivo CSV.");
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarAlertaInfo(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
