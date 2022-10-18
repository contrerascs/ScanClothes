package com.example.scanclothes.FragmentosAdministrador;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.scanclothes.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class InicioAdmin extends Fragment {
    TextView fechaAdmin, NombreTXT;

    //Firebase
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference BASE_DE_DATOS_ADMINISTRADORES;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inicio_admin, container, false);

        fechaAdmin = view.findViewById(R.id.fechaAdmin);
        NombreTXT = view.findViewById(R.id.NombreTXT);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        BASE_DE_DATOS_ADMINISTRADORES = FirebaseDatabase.getInstance().getReference("BASE DE DATOS ADMINISTRADORES");

        //FECHA ACTUAL
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d 'de' MMMM 'de' yyyy");
        String StringFecha = simpleDateFormat.format(date);
        fechaAdmin.setText("Hoy es: "+StringFecha);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        comprobarUsurarioActivo();
    }

    private void comprobarUsurarioActivo(){
        if(firebaseUser!=null){
            cargarDatos();
        }
    }

    private void cargarDatos(){
        BASE_DE_DATOS_ADMINISTRADORES.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //SI USUARIO ADMIN EXISTE
                if(snapshot.exists()){
                    //OBTENER EL DATO NOMBRE
                    String nombre = ""+snapshot.child("NOMBRES").getValue();
                    NombreTXT.setText(nombre);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}