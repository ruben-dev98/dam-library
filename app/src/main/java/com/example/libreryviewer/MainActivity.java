package com.example.libreryviewer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<RecyclerViewCardVM> comics = new ArrayList<>();
    private RecyclerView recycleView;
    private RecyclerViewAdapter adaptador;
    private boolean isAppActive;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        PreferenceManager.setDefaultValues(this, R.xml.preferencias, false);
        isAppActive = true;
        sqlThread.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;

        switch (item.getItemId()) {
            case R.id.itExit:
                finish();
                break;

            case R.id.itSecurity:
                intent = new Intent(this, ActivityPreferencias.class);
                startActivity(intent);
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    Thread sqlThread = new Thread() {
        public void run() {
            while (true) {
                try {
                    if (isAppActive) {
                        getComics();
                    }
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private void getComics() {
        comics = new ArrayList<>();
        //"83.165.75.94:3306"
        SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String address = "jdbc:mysql://" + preferencias.getString("edit_text_address", "192.168.0.11:3306") + "/libreria";
        String username = preferencias.getString("edit_text_user", "ruben");
        String password = preferencias.getString("edit_text_password", "M4st3rRubi99!");

        Connection conexionMySQL = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conexionMySQL = DriverManager.getConnection(address, username, password);
            System.out.println(conexionMySQL);
            Log.d("DB", "Connection succesfull");
            Log.d("DB", "param1" + username);
            Log.d("DB", "param2" + password);

            String stsql = "SELECT \n" +
                    "    com.id_comic, com.titulo, com.portada \n" +

                    "FROM\n" +
                    "    libreria.comics com\n" +
                    "ORDER BY com.id_comic DESC\n" +
                    ";";
            java.sql.Statement st = conexionMySQL.createStatement();
            ResultSet rs = st.executeQuery(stsql);

            while (rs.next()) {
                comics.add(new RecyclerViewCardVM(rs.getInt(1), rs.getString(2), rs.getBlob(3)));
            }
        } catch (IllegalAccessException | ClassNotFoundException | SQLException | InstantiationException e) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "No se ha podido conectar.", Toast.LENGTH_SHORT).show();
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

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                inicializarRecyclerView();
            }
        });
    }

    private void inicializarRecyclerView() {
        adaptador = new RecyclerViewAdapter(comics);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recycleView = findViewById(R.id.comicRecyclerView);
        recycleView.setLayoutManager(layoutManager);

        recycleView.setAdapter(adaptador);
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("pause");
        isAppActive = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("resume");
        isAppActive = true;
    }
}




