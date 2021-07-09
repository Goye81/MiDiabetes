package com.example.proyectofinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class Consulta extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    //Declaramos las variables de la clase
    private TextInputEditText fecha;
    private TextInputLayout txtFe;
    private TextView hora;
    private TextView valor;
    private Button buscar;
    private Button sig;
    private Button ant;
    private FirebaseAuth mAuth;
    private String idF = "";
    private String date = "";
    private String ho = "";
    private String glu = "";
    private int cont = 0;
    private FirebaseFirestore db;
    private String TAG = "Consulta";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Iniciamos las variables
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        fecha = findViewById(R.id.editTextFe);
        txtFe = findViewById(R.id.text_input_layout_fe);
        hora = findViewById(R.id.valorHora);
        valor = findViewById(R.id.valorTx);
        buscar = findViewById(R.id.buscarbtn);
        sig = findViewById(R.id.sigBtn);
        ant = findViewById(R.id.atrasbtn);
        sig.setEnabled(false);
        ant.setEnabled(false);
        idF = mAuth.getCurrentUser().getUid();

        //Método para que haga una acción al poner el foco sobre el EditText de la fecha
        fecha.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    DialogFragment newFragment = new DatePickerFragment();
                    newFragment.show(getSupportFragmentManager(), "datePicker");
                    valor.setText("");
                    hora.setText("");
                } else {

                }
            }
        });

        //Funcionalidad del botón buscar
        buscar.setOnClickListener(v -> {
            date = fecha.getText().toString();
            cont = 0;
            db.collection("valores")
                    .whereEqualTo("fecha", date).whereEqualTo("idF", idF)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                ArrayList<Integer> valores = new ArrayList<>();
                                ArrayList<String> horas = new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Map<String, Object> result = document.getData();
                                    horas.add(result.get("hora").toString());
                                    valores.add(Integer.valueOf(result.get("val").toString()));
                                }
                                if (valores.size() > 0) {
                                    ho = horas.get(0);
                                    hora.setText(ho);
                                    glu = String.valueOf(valores.get(0));
                                    valor.setText(glu + " mg/dl");
                                    sig.setEnabled(true);
                                    ant.setEnabled(false);
                                } else {
                                    Toast.makeText(getApplicationContext(), "No hay registros en esa fecha", Toast.LENGTH_LONG).show();
                                    sig.setEnabled(false);
                                    ant.setEnabled(false);
                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });


        });

        //Funcionalidad del botón siguiente
        sig.setOnClickListener(v -> {
            date = fecha.getText().toString();
            db.collection("valores")
                    .whereEqualTo("fecha", date)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                ArrayList<Integer> valores = new ArrayList<>();
                                ArrayList<String> horas = new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Map<String, Object> result = document.getData();
                                    horas.add(result.get("hora").toString());
                                    valores.add(Integer.valueOf(result.get("val").toString()));
                                }
                                if (cont == horas.size() - 1) {
                                    sig.setEnabled(false);
                                } else {
                                    ant.setEnabled(true);
                                    cont++;
                                    ho = horas.get(cont);
                                    hora.setText(ho);
                                    glu = String.valueOf(valores.get(cont));
                                    valor.setText(glu + " mg/dl");
                                }

                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        });

        //Funcionalidad del botón anterior
        ant.setOnClickListener(v -> {
            date = fecha.getText().toString();
            db.collection("valores")
                    .whereEqualTo("fecha", date)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                ArrayList<Integer> valores = new ArrayList<>();
                                ArrayList<String> horas = new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Map<String, Object> result = document.getData();
                                    horas.add(result.get("hora").toString());
                                    valores.add(Integer.valueOf(result.get("val").toString()));
                                }
                                if (cont == 0) {
                                    ant.setEnabled(false);
                                } else {
                                    sig.setEnabled(true);
                                    cont--;
                                    ho = horas.get(cont);
                                    hora.setText(ho);
                                    glu = String.valueOf(valores.get(cont));
                                    valor.setText(glu + " mg/dl");

                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        });
    }

    //Método para recoger la fecha
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        fecha.setText(dayOfMonth + "/" + ( month + 1 ) + "/" + year);
    }

    //Método para que el EditText de fecha pierda el foco al tocar otra cosa
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
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