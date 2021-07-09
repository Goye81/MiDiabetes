package com.example.proyectofinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class Historico extends AppCompatActivity {
    //Declaramos las variables de la clase
    private BarChart barChart;
    private int val1;
    private int val2;
    private int val3;
    private int val4;
    private FirebaseAuth mAuth;
    private String idF = "";
    private FirebaseFirestore db;
    private String TAG = "Hemoglobina";
    private int zTs;
    private int sTt;
    private int tTs;
    private int sTz;
    private int v1;
    private int v2;
    private int v3;
    private int v4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Iniciamos las variables
        mAuth = FirebaseAuth.getInstance();
        barChart = findViewById(R.id.barChart);
        idF = mAuth.getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();

        //Recuperamos los valores de la base de datos y los manipulamos
        db.collection("valores")
                .whereEqualTo("idF", idF)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            v1 = 0;
                            v2 = 0;
                            v3 = 0;
                            v4 = 0;
                            zTs = 0;
                            sTt = 0;
                            tTs = 0;
                            sTz = 0;

                            ArrayList<Integer> valores = new ArrayList<>();
                            ArrayList<String> horas = new ArrayList<>();
                            Map<String, Object> result = null;
                            String[] splitted;
                            int index;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                result = document.getData();
                                horas.add(result.get("hora").toString());
                                valores.add(Integer.valueOf(result.get("val").toString()));
                            }

                            /*Recorremos el ArrayList de las horas y recuperamos el índice donde se cumple la condición
                             * y recogemos los valores de los mismos índices y los sumamos*/
                            for (String h : horas) {
                                splitted = h.split(":");
                                int hour = Integer.valueOf(splitted[0]);
                                if (hour >= 0 && hour <= 6) {
                                    zTs = zTs + 1;
                                    index = horas.indexOf(h);
                                    v1 = v1 + valores.get(index);
                                }else if (hour > 6 && hour <= 12) {
                                    sTt = sTt + 1;
                                    index = horas.indexOf(h);
                                    v2 = v2 + valores.get(index);
                                }else if (hour > 12 && hour <= 18) {
                                    tTs = tTs + 1;
                                    index = horas.indexOf(h);
                                    v3 = v3 + valores.get(index);
                                }else {
                                    sTz = sTz + 1;
                                    index = horas.indexOf(h);
                                    v4 = v4 + valores.get(index);
                                }
                            }

                            //Hacemos la cuenta para darle el valor a las barras
                            if (zTs != 0) {
                                val1 = v1 / zTs;
                            } else {
                                Toast.makeText(Historico.this, "No hay registros suficientes", Toast.LENGTH_SHORT).show();
                                val1 = 0;
                            }

                            if (sTt != 0) {
                                val2 = v2 / sTt;
                            } else {
                                Toast.makeText(Historico.this, "No hay registros suficientes", Toast.LENGTH_SHORT).show();
                                val2 = 0;
                            }

                            if (tTs != 0) {
                                val3 = v3 / tTs;
                            } else {
                                Toast.makeText(Historico.this, "No hay registros suficientes", Toast.LENGTH_SHORT).show();
                                val3 = 0;
                            }

                            if (sTz != 0) {
                                val4 = v4 / sTz;
                            } else {
                                Toast.makeText(Historico.this, "No hay registros suficientes", Toast.LENGTH_SHORT).show();
                                val4 = 0;
                            }

                            /*Le damos valores a las barras, a las etiquetas del eje X y definimos
                            * el diseño que va a tener el gráfico de barras*/
                            ArrayList<BarEntry> datos = new ArrayList<>();
                            datos.add(new BarEntry(val1, 0));
                            datos.add(new BarEntry(val2, 1));
                            datos.add(new BarEntry(val3, 2));
                            datos.add(new BarEntry(val4, 3));

                            BarDataSet barDataSet = new BarDataSet(datos, "Datos Históricos");
                            barDataSet.setColor(Color.GREEN, Color.RED);
                            ArrayList<String> labels = new ArrayList<>();
                            labels.add("0-6");
                            labels.add("6-12");
                            labels.add("12-18");
                            labels.add("18-24");
                            barDataSet.setValueTextColor(Color.BLACK);
                            barDataSet.setValueTextSize(20f);
                            BarData barData = new BarData(labels, barDataSet);

                            barChart.setData(barData);
                            barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                            barChart.animateY(2000);

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
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
            case R.id.home:
                home();
                return true;
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

    private void home() {
        Intent intent = new Intent(this, Lista.class);
        startActivity(intent);
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