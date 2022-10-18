package com.example.scanclothes.CategoriasAdministrador.PrimaveraA;

public class Primavera {
    private String nombre;
    private String descripcion;
    private String imagen;
    private String id_administrador;
    private String enlace;
    private String id;
    private int vistas;

    public Primavera() {
    }

    public Primavera(String nombre, String descripcion, String imagen, String id_administrador, String enlace, String id, int vistas) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.id_administrador = id_administrador;
        this.enlace = enlace;
        this.id = id;
        this.vistas = vistas;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
