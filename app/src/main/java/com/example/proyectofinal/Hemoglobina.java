package com.example.proyectofinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class Hemoglobina extends AppCompatActivity {
    //Declaramos las variables de la clase
    private TextView ac;
    private TextView he;
    private String hemoglobina = "";
    private FirebaseAuth mAuth;
    private String idF = "";
    double hA;
    private FirebaseFirestore db;
    private String TAG = "Hemoglobina";
    private int suma;
    private int conta;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hemoglobina);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Iniciamos las variables
        ac = findViewById(R.id.hemoTxt);
        he = findViewById(R.id.txtHe);
        mAuth = FirebaseAuth.getInstance();
        idF = mAuth.getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();
        suma = 0;
        conta = 0;

        //Recuperamos los valores de la base de datos y los manipulamos
        db.collection("valores")
                .whereEqualTo("idF", idF)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Integer> valores = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> result = document.getData();
                                valores.add(Integer.valueOf(result.get("val").toString()));
                            }
                            for (int st : valores) {
                                suma = suma + st;
                                conta += 1;
                            }
                            if (conta != 0) {
                                suma = suma / conta;
                                hA = hemo(suma);
                                hemoglobina = String.valueOf(hA);
                                he.setText(hemoglobina + "%");
                            } else {
                                Toast.makeText(Hemoglobina.this, "No hay registros", Toast.LENGTH_SHORT).show();
                                suma = 0;
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }


    //Método que devuelve la hemoglobina y recibe como parámetro la suma de los valores de la BD
    public double hemo(int sum) {
        double hA = 0;
        if (sum < 70) {
            hA = 4;
        }
        if (sum >= 70 && sum <= 96) {
            hA = 4.5;
        }
        if (sum >= 97 && sum <= 110) {
            hA = 5;
        }
        if (sum >= 111 && sum <= 125) {
            hA = 5.5;
        }
        if (sum >= 126 && sum <= 139) {
            hA = 6;
        }
        if (sum >= 140 && sum <= 153) {
            hA = 6.5;
        }
        if (sum >= 154 && sum <= 168) {
            hA = 7;
        }
        if (sum >= 169 && sum <= 182) {
            hA = 7.5;
        }
        if (sum >= 183 && sum <= 196) {
            hA = 8;
        }
        if (sum >= 197 && sum <= 211) {
            hA = 8.5;
        }
        if (sum >= 212 && sum <= 225) {
            hA = 9;
        }
        if (sum >= 226 && sum <= 239) {
            hA = 9.5;
        }
        if (sum >= 240 && sum <= 254) {
            hA = 10;
        }
        if (sum >= 255 && sum <= 268) {
            hA = 10.5;
        }
        if (sum >= 269 && sum <= 282) {
            hA = 11;
        }
        if (sum >= 283 && sum <= 297) {
            hA = 11.5;
        }
        if (sum >= 298 && sum <= 310) {
            hA = 12;
        }
        if (sum >= 311) {
            hA = 12.5;
        }

        return hA;
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
