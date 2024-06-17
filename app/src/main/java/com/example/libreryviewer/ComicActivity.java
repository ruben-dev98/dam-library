package com.example.libreryviewer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ComicActivity extends AppCompatActivity {

    public static final String PARAM_IDCOMIC = "id_comic";
    public static final int CODIGO_PETICION = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic);

        sqlThread.start();
    }

    Thread sqlThread = new Thread() {
        public void run() {
            Intent intento = getIntent();

            int id_comic = intento.getExtras().getInt(ComicActivity.PARAM_IDCOMIC);

            SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

            //"jdbc:mysql://83.165.75.94:3306/libreria"
            String address = "jdbc:mysql://" + preferencias.getString("edit_text_address", "192.168.0.11:3306") + "/libreria";
            String username = preferencias.getString("edit_text_user", "ruben");
            String password = preferencias.getString("edit_text_password", "M4st3rRubi99!");

            System.out.println(address + "/n" + username + "/n" + password);

            final TextView titulo = findViewById(R.id.textView_Comic_Titulo);
            final ImageView portada = findViewById(R.id.textView_Comic_Portada);
            final TextView estado = findViewById(R.id.textView_Comic_Estado);
            final TextView precio = findViewById(R.id.textView_Comic_Precio);
            final TextView coleccion = findViewById(R.id.textView_Comic_Coleccion);
            final TextView editorial = findViewById(R.id.textView_Comic_Editorial);
            final TextView sinopsis = findViewById(R.id.textView_Comic_Sinopsis);
            final TextView numerosDisponibles = findViewById(R.id.textView_Comic_NumDisponibles);

            ComicVM comic = null;

            Connection conexionMySQL = null;
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                conexionMySQL = DriverManager.getConnection(address, username, password);

                Log.d("DB", "Connection succesfull");
                Log.d("DB", "param1" + username);
                Log.d("DB", "param2" + password);

                String stsql =
                        "SELECT \n" +
                                "    com.titulo, \n" +
                                "    com.portada,\n" +
                                "    com.precio,\n" +
                                "    es.estado,\n" +
                                "    col.nombre,\n" +
                                "    col.editorial,\n" +
                                "    col.sinopsis,\n" +
                                "    GROUP_CONCAT(com2.numero_coleccion)\n" +
                                "FROM\n" +
                                "    libreria.comics com\n" +
                                "NATURAL JOIN\n" +
                                "\tlibreria.colecciones col\n" +
                                "NATURAL JOIN\n" +
                                "\tlibreria.estado es\n" +
                                "LEFT JOIN\n" +
                                "\tlibreria.comics com2 ON col.id_coleccion = com2.id_coleccion\n" +
                                "WHERE com.id_comic = ?";
                PreparedStatement preparedStatement = conexionMySQL.prepareStatement(stsql);
                preparedStatement.setInt(1, id_comic);
                ResultSet rs = preparedStatement.executeQuery();

                while (rs.next()) {
                    comic = new ComicVM(
                            rs.getString(1),
                            rs.getBlob(2),
                            rs.getDouble(3),
                            rs.getString(4),
                            rs.getString(5),
                            rs.getString(6),
                            rs.getString(7),
                            rs.getString(8)
                    );
                }

                final ComicVM finalComic = comic;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        titulo.setText(finalComic.getTitulo());
                        estado.setText(finalComic.getEstado());
                        precio.setText(String.format("%.2f", finalComic.getPrecio()) + " €");
                        coleccion.setText(finalComic.getColeccion());
                        editorial.setText(finalComic.getEditorial());
                        sinopsis.setText(finalComic.getSinopsis());
                        numerosDisponibles.setText(finalComic.getNumDisponibles());

                        Bitmap bmportada = null;
                        try {
                            bmportada = BitmapFactory.decodeStream(finalComic.getPortada().getBinaryStream());
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        portada.setImageBitmap(bmportada);
                    }
                });
            } catch (IllegalAccessException | ClassNotFoundException | SQLException | InstantiationException e) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ComicActivity.this, "No se ha podido conectar.", Toast.LENGTH_SHORT).show();
                    }
                });
                e.printStackTrace();
            } finally {
                if (conexionMySQL != null) {
                    try {
                        conexionMySQL.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };
}
