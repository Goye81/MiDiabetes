package com.example.proyectofinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class IngresarDatos extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    //Declaramos las variables de la clase
    private Button savebtn;
    private int glu;
    private String hor;
    private String fe;
    private FirebaseAuth mAuth;
    private TextInputEditText dato;
    private TextInputEditText hora;
    private TextInputEditText fecha;
    private TextInputLayout txtDato;
    private TextInputLayout txtHora;
    private TextInputLayout txtFecha;
    private FirebaseFirestore db;
    private String TAG = "IngresarDatos";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresar_datos);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Iniciamos las variables
        db = FirebaseFirestore.getInstance();
        dato = findViewById(R.id.editTextValor);
        hora = findViewById(R.id.editTextHora);
        fecha = findViewById(R.id.editTextFecha);
        txtDato = findViewById(R.id.text_input_layout_valor);
        txtHora = findViewById(R.id.text_input_layout_hora);
        txtFecha = findViewById(R.id.text_input_layout_fecha);
        savebtn = findViewById(R.id.guardarBtn);
        mAuth = FirebaseAuth.getInstance();

        //Funcionalidad cuando ponemos el foco en el EditText de la hora
        hora.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    DialogFragment newFragment = new TimePickerFragment();
                    newFragment.show(getSupportFragmentManager(), "timePicker");
                } else {

                }
            }
        });

        //Funcionalidad cuando ponemos el foco en el EditText de la fecha
        fecha.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    DialogFragment newFragment = new DatePickerFragment();
                    newFragment.show(getSupportFragmentManager(), "datePicker");
                } else {

                }
            }
        });

        //Funcionalidad del botón de guardar
        savebtn.setOnClickListener(v -> {
            if(!dato.getText().toString().isEmpty()) {
                glu = Integer.parseInt(dato.getText().toString());
            }else{

            }
            hor = hora.getText().toString();
            String idF = mAuth.getCurrentUser().getUid();
            fe = fecha.getText().toString();
            if (!hor.isEmpty() && !fe.isEmpty() && glu != 0) {
                Map<String, Object> valor = new HashMap<>();
                valor.put("val", glu);
                valor.put("hora", hor);
                valor.put("fecha", fe);
                valor.put("idF", idF);

                //Guarda los datos(documento) en la base de datos
                db.collection("valores")
                        .add(valor)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });

                dato.setText("");
                hora.setText("");
                fecha.setText("");
            } else {
                Toast.makeText(IngresarDatos.this, "Hay campos incompletos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Recoge el valor del reloj y lo pone en el EditText
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        hora.setText(String.format("%02d:%02d", hourOfDay, minute));
    }

    //Recoge el valor del calendario y lo pone en el EditText
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        fecha.setText(dayOfMonth + "/" + ( month + 1 ) + "/" + year);
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


