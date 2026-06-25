/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author nahue
 */
import interfaces.SerializableCSV;

public abstract class Producto implements SerializableCSV {
    private String codigo;
    private String marca;
    private String modelo;
    private double precio;
    private int stock;
    private Proveedor proveedor;

    public Producto() {
    }

    public Producto(String codigo, String marca, String modelo, double precio, int stock, Proveedor proveedor) {
        this.codigo = codigo;
        this.marca = marca;
        this.modelo = modelo;
        this.precio = precio;
        this.stock = stock;
        this.proveedor = proveedor;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(codigo).append("] ")
          .append(marca).append(" ").append(modelo)
          .append(" | Precio: $").append(precio)
          .append(" | Stock: ").append(stock)
          .append(" | Prov: ").append(proveedor.getRazonSocial());
        return sb.toString();
    }
}