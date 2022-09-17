package com.example.scanclothes.Categorias;

public class Categoria {
    String categoria;
    String imagen;

    public Categoria() {
    }

    public Categoria(String categoria, String imagen) {
        this.categoria = categoria;
        this.imagen = imagen;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
