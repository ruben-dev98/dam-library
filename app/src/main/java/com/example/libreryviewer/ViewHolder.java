package com.example.libreryviewer;

import android.content.Intent;
//import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.libreryviewer.ComicActivity.PARAM_IDCOMIC;

public class ViewHolder extends RecyclerView.ViewHolder {

    public ImageView imagen;
    public TextView texto;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        ((MainActivity) itemView.getContext()).registerForContextMenu(itemView);
        imagen = itemView.findViewById(R.id.cardView_imageView);
        texto = itemView.findViewById(R.id.cardView_textView);
        itemView.setOnClickListener(v -> {
            int posicion = getBindingAdapterPosition();
            RecyclerViewCardVM comic = (RecyclerViewCardVM) v.getTag();
            Intent intento = new Intent(v.getContext(), ComicActivity.class);
            int datoEnviar = comic.getId_Comic();
            intento.putExtra(PARAM_IDCOMIC, datoEnviar);
            v.getContext().startActivity(intento);
        });
    }
}
