package com.example.proyectofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    //Declaramos las variables
    private FirebaseAuth mAuth;
    private Button regbtn;
    private String email = "";
    private String passw = "";
    private String namestr = "";
    private TextInputEditText mail;
    private TextInputEditText pass;
    private TextInputEditText name;
    private TextInputLayout txtEmail;
    private TextInputLayout txtPass;
    private TextInputLayout txtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Iniciamos las variables
        name = findViewById(R.id.editTextNombre);
        mail = findViewById(R.id.editTextEmail);
        pass = findViewById(R.id.editTextPassword);
        txtName = findViewById(R.id.text_input_layout_nombre);
        txtEmail = findViewById(R.id.text_input_layout_email);
        txtPass = findViewById(R.id.text_input_layout_pass);
        regbtn = findViewById(R.id.regBtn);
        mAuth = FirebaseAuth.getInstance();

        //Funcionalidad del botón registrar
        regbtn.setOnClickListener(v -> {
            namestr = name.getText().toString();
            email = mail.getText().toString();
            passw = pass.getText().toString();

            if (!email.isEmpty() && !passw.isEmpty()) {
                if (passw.length() >= 6) {
                    registerUser();
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(Register.this, "El password debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(Register.this, "Hay campos incompletos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Método para guardar el usuario en la autenticación de Firebase
    private void registerUser() {
        mAuth.createUserWithEmailAndPassword(email, passw).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                namestr = name.getText().toString();
                email = mail.getText().toString();
                passw = pass.getText().toString();
                Toast.makeText(Register.this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Register.this, "No se pudo registrar a este usuario", Toast.LENGTH_SHORT).show();
            }
        });
    }
}