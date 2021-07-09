package com.example.proyectofinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class Lista extends AppCompatActivity {

    //Declaramos las variables
    private ArrayList<String> nomIma = new ArrayList<>();
    private ArrayList<Integer> imUrl = new ArrayList<>();
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        //Iniciamos las variables
        mAuth = FirebaseAuth.getInstance();
        String id = mAuth.getCurrentUser().getUid();
        System.out.println(id);
        //Llamamos al método del Bitmap
        imaBitmap();
    }

    //Método para agregar los elementos al bitmap
    private void imaBitmap() {
        imUrl.add(R.drawable.mano1);
        nomIma.add("Ingresar Valores");

        imUrl.add(R.drawable.prom2);
        nomIma.add("Hemoglobina A1c");

        imUrl.add(R.drawable.graph2);
        nomIma.add("Histórico");

        imUrl.add(R.drawable.libro);
        nomIma.add("Consultar Valores");

        imUrl.add(R.drawable.salida);
        nomIma.add("Cerrar Sesión");

        iniRecycler();


    }

    //Método para inicializar el RecyclerView
    private void iniRecycler() {
        RecyclerView recyclerView = findViewById(R.id.listaOp);
        AdaptadorLista adapter = new AdaptadorLista(this, nomIma, imUrl);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    //Método que crea el menú superior
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    //De aquí hacia abajo los métodos para la funcionalidad del menú
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ing:
                ingresar();
                return true;
            case R.id.hemo:
                hemoglobina();
                return true;
            case R.id.his:
                historico();
                return true;
            case R.id.con:
                consultar();
                return true;
            case R.id.cer:
                cerrarSesion();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void ingresar() {
        Intent intent = new Intent(this, IngresarDatos.class);
        startActivity(intent);
    }

    private void hemoglobina() {
        Intent intent = new Intent(this, Hemoglobina.class);
        startActivity(intent);
    }

    private void historico() {
        Intent intent = new Intent(this, Historico.class);
        startActivity(intent);
    }

    private void consultar() {
        Intent intent = new Intent(this, Consulta.class);
        startActivity(intent);
    }

    private void cerrarSesion() {
        mAuth.signOut();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}