/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author nahue
 */
public class Electronico extends Producto {
    private int garantiaMeses;

    public Electronico() {
    }

    public Electronico(String codigo, String marca, String modelo, double precio, int stock, Proveedor proveedor, int garantiaMeses) {
        super(codigo, marca, modelo, precio, stock, proveedor);
        this.garantiaMeses = garantiaMeses;
    }

    public int getGarantiaMeses() {
        return garantiaMeses;
    }

    public void setGarantiaMeses(int garantiaMeses) {
        this.garantiaMeses = garantiaMeses;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString())
          .append(" | Gtía: ").append(garantiaMeses).append(" meses");
        return sb.toString();
    }
    
    
    @Override
    public String toCSV() {
        StringBuilder sb = new StringBuilder();
        sb.append("Electronico;").append(getCodigo()).append(";")
          .append(getMarca()).append(";").append(getModelo()).append(";")
          .append(getPrecio()).append(";").append(getStock()).append(";")
          .append(getProveedor().getId()).append(";")
          .append(getProveedor().getRazonSocial()).append(";")
          .append(getProveedor().getTelefono()).append(";")
          .append(getProveedor().getEmail()).append(";")
          .append(getProveedor().getCiudad()).append(";")
          .append(garantiaMeses);
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
        
        this.garantiaMeses = Integer.parseInt(datos[11]);
    }
}