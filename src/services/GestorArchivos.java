/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

/**
 *
 * @author nahue
 */
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import models.Alimenticio;
import models.Electronico;
import models.Producto;
import models.Proveedor;

public class GestorArchivos {

    private static final String RUTA_PROVEEDORES = "proveedores.json";
    private static final String RUTA_PRODUCTOS = "productos.csv";
    private static final String RUTA_EXPORTACION = "productos_caros.txt";

    public static List<Proveedor> leerProveedores() throws Exception {
        List<Proveedor> lista;
        try (BufferedReader br = new BufferedReader(new FileReader(RUTA_PROVEEDORES))) {
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Proveedor>>(){}.getType();
            lista = gson.fromJson(br, listType);
        }
        return lista != null ? lista : new ArrayList<>();
    }

    public static void guardarProductos(List<Producto> productos) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_PRODUCTOS))) {
            for (Producto p : productos) {
                bw.write(p.toCSV());
                bw.newLine();
            }
        }
    }

    public static List<Producto> leerProductos() throws Exception {
        List<Producto> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(RUTA_PRODUCTOS))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                
                String[] datos = linea.split(";");
                Producto p;
                if (datos[0].equals("Electronico")) {
                    p = new Electronico();
                } else {
                    p = new Alimenticio();
                }
                p.fromCSV(linea);
                lista.add(p);
            }
        }
        return lista;
    }

    public static void exportarProductosCaros(List<Producto> productos) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_EXPORTACION))) {
            for (Producto p : productos) {
                if (p.getPrecio() > 500000) {
                    bw.write(p.toString());
                    bw.newLine();
                }
            }
        }
    }
    
    public static String leerReporteCaros() throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(RUTA_EXPORTACION))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                sb.append(linea).append("\n");
            }
        }
        return sb.toString();
    }
}