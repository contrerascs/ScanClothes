package com.example.scanclothes.FragmentosAdministrador;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

public class PerfilAdmin extends Fragment {

    //FIREBASE
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference BASE_DE_DATOS_ADMINISTRADORES;

    //VISTAS
    ImageView FOTOPERFILIMG;
    TextView UIDPERFIL, NOMBREPERFIL, APELLIDOPERFIL, EDADPERFIL, CORREOPERFIL, PASSWORDPERFIL;
    Button ACTUALIZARPASS, ACTUALIZARDATOS;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil_admin, container, false);

        //TEXTVIEWS
        FOTOPERFILIMG = view.findViewById(R.id.FOTOPERFILIMG);
        UIDPERFIL = view.findViewById(R.id.UIDPERFIL);
        NOMBREPERFIL = view.findViewById(R.id.NOMBREPERFIL);
        APELLIDOPERFIL = view.findViewById(R.id.APELLIDOPERFIL);
        EDADPERFIL = view.findViewById(R.id.EDADPERFIL);
        CORREOPERFIL = view.findViewById(R.id.CORREOPERFIL);
        PASSWORDPERFIL = view.findViewById(R.id.PASSWORLDPERFIL);

        //BOTONES
        ACTUALIZARPASS = view.findViewById(R.id.ACTUALIZARPASS);
        ACTUALIZARDATOS = view.findViewById(R.id.ACTUALIZARDATOS);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        BASE_DE_DATOS_ADMINISTRADORES = FirebaseDatabase.getInstance().getReference("BASE DE DATOS ADMINISTRADORES");

        BASE_DE_DATOS_ADMINISTRADORES.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    //OBTENER DATOS
                    String uid = ""+snapshot.child("UID").getValue();
                    String nombre = ""+snapshot.child("NOMBRES").getValue();
                    String apellidos = ""+snapshot.child("APELLIDOS").getValue();
                    String edad = ""+snapshot.child("EDAD").getValue();
                    String correo = ""+snapshot.child("CORREO").getValue();
                    String password = ""+snapshot.child("PASSWORD").getValue();
                    String imagen = ""+snapshot.child("IMAGEN").getValue();

                    UIDPERFIL.setText(uid);
                    NOMBREPERFIL.setText(nombre);
                    APELLIDOPERFIL.setText(apellidos);
                    EDADPERFIL.setText(edad);
                    CORREOPERFIL.setText(correo);
                    PASSWORDPERFIL.setText(password);

                    try {
                        //SI EXISTE LA IMAGEN
                        Picasso.get().load(imagen).placeholder(R.drawable.administrador).into(FOTOPERFILIMG);
                    }catch (Exception e){
                        //NO EXISTE IMAGEN
                        Picasso.get().load(R.drawable.administrador).into(FOTOPERFILIMG);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }
}