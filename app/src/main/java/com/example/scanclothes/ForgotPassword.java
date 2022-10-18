package com.example.scanclothes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class ForgotPassword extends AppCompatActivity {

    Button Recuperar;
    EditText Correo;

    DatabaseReference BASE_DE_DATOS_ADMINISTRADORES;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Recuperar = findViewById(R.id.Recuperar);
        Correo = findViewById(R.id.Correo);

        Recuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });
    }

    private void validate() {
        String email = Correo.getText().toString().trim();
        if(email.isEmpty()||!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Correo.setError("CORREO INVALIDO");
            return;
        }

        sendEmail(email);
    }

    private void sendEmail(String email) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = email;

        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ForgotPassword.this,"Correo enviado",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ForgotPassword.this,InicioSesion.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(ForgotPassword.this,"Correo Invalido",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(ForgotPassword.this,InicioSesion.class);
        startActivity(intent);
        finish();
    }
}