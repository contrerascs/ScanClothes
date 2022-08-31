package com.example.scanclothes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Registro extends AppCompatActivity {
    TextView FechaRegistro;
    EditText Correo, Password, Nombre, Apellidos, Edad;
    Button Registrar;
    FirebaseAuth auth;
    ProgressDialog progressDialog;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        FechaRegistro = findViewById(R.id.FechaRegistro);
        Correo = findViewById(R.id.Correo);
        Password = findViewById(R.id.Password);
        Nombre = findViewById(R.id.Nombre);
        Apellidos = findViewById(R.id.Apellidos);
        Edad = findViewById(R.id.Edad);
        Registrar = findViewById(R.id.Registrar);

        auth = FirebaseAuth.getInstance(); //Inicializando Firebase Authentication

        Date date = new Date();
        SimpleDateFormat fecha = new SimpleDateFormat("d 'de' MMMM 'del' yyyy");
        String SFecha = fecha.format(date); //CONVERTIR FECHA A STRING
        FechaRegistro.setText(SFecha);

        Registrar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String correo = Correo.getText().toString();
                String pass = Password.getText().toString();
                String nombre = Nombre.getText().toString();
                String apellidos = Apellidos.getText().toString();
                String edad = Edad.getText().toString();

                if (correo.equals("") || pass.equals("") || nombre.equals("") | apellidos.equals("")
                        || edad.equals("")) {
                    Toast.makeText(Registro.this, "Por favor llena todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    //VALIDACION DEL CORREO
                    if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                        Correo.setError("Correo inválido");
                        Correo.setFocusable(true);
                    } else if (pass.length() < 6) {
                        Password.setError("Contraseña muy debil");
                        Password.setFocusable(true);
                    } else {
                        RegistroAdministradores(correo, pass);
                    }
                }
            }
        });
        progressDialog = new ProgressDialog(Registro.this);
        progressDialog.setMessage("Registrado, espere por favor");
        progressDialog.setCancelable(false);
    }

    private void RegistroAdministradores(String correo, String pass) {
        progressDialog.show();
        auth.createUserWithEmailAndPassword(correo,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //SI EL ADMINISTRADOR FUE CREADO CORRECTAMENTE
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            FirebaseUser user = auth.getCurrentUser();
                            assert user != null;
                            //CONVERTIR A CADENA LOS DATOS DEL ADMINISTRADOR
                            String UID = user.getUid();
                            String correo = Correo.getText().toString();
                            String pass = Password.getText().toString();
                            String nombre = Nombre.getText().toString();
                            String apellidos = Apellidos.getText().toString();
                            String edad = Edad.getText().toString();
                            int edadInt = Integer.parseInt(edad);

                            HashMap<Object, Object> Administradores = new HashMap<>();
                            Administradores.put("UID",UID);
                            Administradores.put("CORREO",correo);
                            Administradores.put("PASSWORD",pass);
                            Administradores.put("NOMBRES",nombre);
                            Administradores.put("APELLIDOS",apellidos);
                            Administradores.put("EDAD",edadInt);
                            Administradores.put("IMAGEN","");

                            //Inicializar FirebaseDatabase
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("BASE DE DATOS ADMINISTRADORES");
                            reference.child(UID).setValue(Administradores);
                            startActivity(new Intent(Registro.this, MainActivityAdministrador.class));
                            Toast.makeText(Registro.this,"Registro exitoso", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(Registro.this, "Ha ocurriodo un error",Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Registro.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}