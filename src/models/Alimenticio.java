/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author nahue
 */
public class Alimenticio extends Producto {
    private String fechaVencimiento;

    public Alimenticio() {
    }

    public Alimenticio(String codigo, String marca, String modelo, double precio, int stock, Proveedor proveedor, String fechaVencimiento) {
        super(codigo, marca, modelo, precio, stock, proveedor);
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(String fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }
    
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString())
          .append(" | Vto: ").append(fechaVencimiento);
        return sb.toString();
    }
    
    @Override
    public String toCSV() {
        StringBuilder sb = new StringBuilder();
        sb.append("Alimenticio;").append(getCodigo()).append(";")
          .append(getMarca()).append(";").append(getModelo()).append(";")
          .append(getPrecio()).append(";").append(getStock()).append(";")
          .append(getProveedor().getId()).append(";")
          .append(getProveedor().getRazonSocial()).append(";")
          .append(getProveedor().getTelefono()).append(";")
          .append(getProveedor().getEmail()).append(";")
          .append(getProveedor().getCiudad()).append(";")
          .append(fechaVencimiento);
        return sb.toString();
    }

    @Override
    public void fromCSV(String linea) {
        String[] datos = linea.split(";");
        setCodigo(datos[1]);
        setMarca(datos[2]);
        setModelo(datos[3]);
        setPrecio(Double.parseDouble(datos[4]));
        setStock(Integer.parseInt(datos[5]));
        
        Proveedor prov = new Proveedor(Integer.parseInt(datos[6]), datos[7], datos[8], datos[9], datos[10]);
        setProveedor(prov);
        
        this.fechaVencimiento = datos[11];
    }
}