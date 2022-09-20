package com.example.scanclothes.CategoriasAdministrador.InviernoA;

public class Invierno {
    private String nombre;
    private String descripcion;
    private String imagen;
    private String id_administrador;
    private String enlace;
    private int vistas;

    public Invierno() {

    }

    public Invierno(String nombre, String descripcion, String imagen, int vistas, String id_administrador,String enlace) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.vistas = vistas;
        this.id_administrador = id_administrador;
        this.enlace = enlace;
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

    public String getEnlace() {
        return enlace;
    }

    public void setEnlace(String enlace) {
        this.enlace = enlace;
    }
}
