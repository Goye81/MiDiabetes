package com.example.proyectofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {
    //Declaramos las variables de la clase
    private FirebaseAuth mAuth;
    private TextView reg;
    private Button logbtn;
    private String email = "";
    private String passw = "";
    private TextInputEditText mail;
    private TextInputEditText pass;
    private TextInputLayout txtEmail;
    private TextInputLayout txtPass;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_ProyectoFinal);
        setContentView(R.layout.activity_main);
        user = FirebaseAuth.getInstance().getCurrentUser();
        //Método para que guarde la sesión al cerrar la aplicación
        if (user != null) {
            Intent intent = new Intent(this, Lista.class);
            startActivity(intent);
        } else {
            // No hay ningún usuario "logueado"
        }

        //Iniciamos las variables
        mail = findViewById(R.id.editTextEmail);
        pass = findViewById(R.id.editTextPassword);
        txtEmail = findViewById(R.id.text_input_layout_email);
        txtPass = findViewById(R.id.text_input_layout_pass);
        logbtn = findViewById(R.id.logBtn);
        reg = findViewById(R.id.regText);
        mAuth = FirebaseAuth.getInstance();

        //Funcionalidad del botón de login
        logbtn.setOnClickListener(v -> {
            email = mail.getText().toString();
            passw = pass.getText().toString();

            if (!email.isEmpty() && !passw.isEmpty()) {
                logIn();
            } else {
                Toast.makeText(MainActivity.this, "Hay campos incompletos", Toast.LENGTH_SHORT).show();
            }
        });

        //Funcionalidad del texto "Registrarse"
        reg.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, Register.class));

        });
    }

    //Método login que utiliza la función de autenticación de Firebase
    private void logIn() {
        mAuth.signInWithEmailAndPassword(email, passw).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                startActivity(new Intent(MainActivity.this, Lista.class));
                finish();
            } else {
                Toast.makeText(MainActivity.this, "No se pudo iniciar sesión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}