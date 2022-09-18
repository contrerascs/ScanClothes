package com.example.scanclothes.CategoriasAdministrador.VeranoA;

public class Verano {
    private String nombre;
    private String descripcion;
    private String imagen;
    private String id_administrador;
    private int vistas;

    public Verano() {
    }

    public Verano(String nombre, String descripcion, String imagen, int vistas, String id_administrador) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.vistas = vistas;
        this.id_administrador = id_administrador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public int getVistas() {
        return vistas;
    }

    public void setVistas(int vistas) {
        this.vistas = vistas;
    }

    public String getId_administrador() {
        return id_administrador;
    }

    public void setId_administrador(String id_administrador) {
        this.id_administrador = id_administrador;
    }
}
