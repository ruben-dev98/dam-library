package com.example.libreryviewer;

import java.sql.Blob;

public class RecyclerViewCardVM {

    private int id_comic;
    private String titulo;
    private Blob portada;

    public RecyclerViewCardVM(int id_comic, String titulo) {
        this.id_comic = id_comic;
        this.titulo = titulo;
    }

    public RecyclerViewCardVM(int id_comic, String titulo, Blob portada) {
        this.id_comic = id_comic;
        this.titulo = titulo;
        this.portada = portada;
    }

    public int getId_Comic() {
        return id_comic;
    }

    public String getTitulo() {
        return titulo;
    }

    public Blob getPortada() {
        return portada;
    }

    @Override
    public String toString() {
        return "RecyclerViewCardVM{" +
                "id_comic=" + id_comic +
                ", titulo='" + titulo + '\'' +
                ", portada=" + portada +
                '}';
    }
}
