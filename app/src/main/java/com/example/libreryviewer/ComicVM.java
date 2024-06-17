package com.example.libreryviewer;

import java.sql.Blob;

public class ComicVM {

    String titulo;
    Blob portada;
    String estado;
    double precio;
    String coleccion;
    String editorial;
    String sinopsis;
    String numDisponibles;

    public ComicVM(String titulo, Blob portada, double precio, String estado, String coleccion, String editorial, String sinopsis, String numDisponibles) {
        this.titulo = titulo;
        this.portada = portada;
        this.precio = precio;
        this.estado = estado;
        this.coleccion = coleccion;
        this.editorial = editorial;
        this.sinopsis = sinopsis;
        this.numDisponibles = numDisponibles;
    }

    public String getTitulo() {
        return titulo;
    }

    public Blob getPortada() {
        return portada;
    }

    public double getPrecio() {
        return precio;
    }

    public String getEstado() {
        return estado;
    }

    public String getColeccion() {
        return coleccion;
    }

    public String getEditorial() {
        return editorial;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public String getNumDisponibles() {
        return numDisponibles;
    }
}
