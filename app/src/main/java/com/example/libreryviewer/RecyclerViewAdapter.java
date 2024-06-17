package com.example.libreryviewer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.SQLException;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter {

    private ArrayList<RecyclerViewCardVM> comics = new ArrayList<>();

    public RecyclerViewAdapter(ArrayList<RecyclerViewCardVM> comics) {
        this.comics = comics;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater mInflater = (LayoutInflater) viewGroup.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = mInflater.inflate(R.layout.comic_cardview, viewGroup, false);
        RecyclerView.ViewHolder vHolder = new ViewHolder(v);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ViewHolder vHolder = (ViewHolder) viewHolder;
        vHolder.itemView.setTag(comics.get(i));
        Bitmap portada = null;
        try {
            portada = BitmapFactory.decodeStream(comics.get(i).getPortada().getBinaryStream());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        vHolder.imagen.setImageBitmap(portada);
        vHolder.texto.setText(comics.get(i).getTitulo());
    }

    @Override
    public int getItemCount() {
        return comics.size();
    }

}
