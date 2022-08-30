package com.example.scanclothes.FragmentosAdministrador;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scanclothes.MainActivityAdministrador;
import com.example.scanclothes.R;
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
import java.util.regex.Pattern;

public class RegistroAdmin extends Fragment {
    TextView FechaRegistro;
    EditText Correo, Password, Nombre, Apellidos, Edad;
    Button Registrar;

    FirebaseAuth auth;

    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registro_admin, container, false);

        FechaRegistro = view.findViewById(R.id.FechaRegistro);
        Correo = view.findViewById(R.id.Correo);
        Password = view.findViewById(R.id.Password);
        Nombre = view.findViewById(R.id.Nombre);
        Apellidos = view.findViewById(R.id.Apellidos);
        Edad = view.findViewById(R.id.Edad);

        Registrar = view.findViewById(R.id.Registrar);

        auth = FirebaseAuth.getInstance(); //Inicializando Firebase Authentication

        Date date = new Date();
        SimpleDateFormat fecha = new SimpleDateFormat("d 'de' MMMM 'del' yyyy");
        String SFecha = fecha.format(date); //CONVERTIR FECHA A STRING
        FechaRegistro.setText(SFecha);

        //AL CLIENTE EN REGISTRAR
        Registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //CONVERTIMOS A STRING LOS EDIT TEXT
                String correo = Correo.getText().toString();
                String pass = Password.getText().toString();
                String nombre = Nombre.getText().toString();
                String apellidos = Apellidos.getText().toString();
                String edad = Edad.getText().toString();

                if(correo.equals("")||pass.equals("")||nombre.equals("")|apellidos.equals("")
                        ||edad.equals("")){
                    Toast.makeText(getActivity(),"Por favor llena todos los campos",Toast.LENGTH_SHORT).show();
                }else{
                    //VALIDACION DEL CORREO
                    if(!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
                        Correo.setError("Correo inválido");
                        Correo.setFocusable(true);
                    }else if(pass.length()<6){
                        Password.setError("Contraseña muy debil");
                        Password.setFocusable(true);
                    }else{
                        RegistroAdministradores(correo,pass);
                    }
                }
            }
        });

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Registrado, espere por favor");
        progressDialog.setCancelable(false);

        return view;
    }

    //METODO PARA REGISTRAR ADMINISTRADORES
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
                            startActivity(new Intent(getActivity(), MainActivityAdministrador.class));
                            Toast.makeText(getActivity(),"Registro exitoso", Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Ha ocurriodo un error",Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}