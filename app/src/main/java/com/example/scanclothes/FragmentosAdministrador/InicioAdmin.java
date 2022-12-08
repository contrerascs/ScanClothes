package com.example.scanclothes.FragmentosAdministrador;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scanclothes.CategoriasAdministrador.PrimaveraA.Primavera;
import com.example.scanclothes.CategoriasAdministrador.PrimaveraA.ViewHolderPrimavera;
import com.example.scanclothes.DetalleImagen.DetalleImagen;
import com.example.scanclothes.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InicioAdmin extends Fragment {
    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    FirebaseRecyclerAdapter<Primavera, ViewHolderPrimavera> firebaseRecyclerAdapter2;
    FirebaseRecyclerOptions<Primavera> options2;

    Dialog dialog;
    SharedPreferences sharedPreferences;

    //Firebase
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference BASE_DE_DATOS_ADMINISTRADORES;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inicio_admin, container, false);

        firebaseDatabase = FirebaseDatabase.getInstance();

        recyclerView = view.findViewById(R.id.recyclerInicioAdministrador);
        recyclerView.setHasFixedSize(true);

        dialog = new Dialog(getContext());

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        BASE_DE_DATOS_ADMINISTRADORES = FirebaseDatabase.getInstance().getReference("BASE DE DATOS ADMINISTRADORES");

        ListarImagenesPrimavera();

        return view;
    }

    private void ListarImagenesPrimavera() {
        reference = firebaseDatabase.getReference("PRIMAVERA");
        options2 = new FirebaseRecyclerOptions.Builder<Primavera>().setQuery(reference,Primavera.class).build();

        firebaseRecyclerAdapter2 = new FirebaseRecyclerAdapter<Primavera, ViewHolderPrimavera>(options2) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderPrimavera viewHolderPrimavera, int position, @NonNull Primavera primavera) {
                viewHolderPrimavera.SeteoPrimavera(
                        getContext(),
                        primavera.getNombre(),
                        primavera.getVistas(),
                        primavera.getImagen(),
                        primavera.getDescripcion(),
                        primavera.getId_administrador()
                );
            }

            @NonNull
            @Override
            public ViewHolderPrimavera onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                //INFLAR EN ITEM
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_primavera, parent, false);

                ViewHolderPrimavera viewHolderPrimavera = new ViewHolderPrimavera(itemView);

                viewHolderPrimavera.setOnClickListener(new ViewHolderPrimavera.ClickListener() {
                    @Override
                    public void OnIntemClick(View view, int position) {
                        //OBTENER LOS DATOS DE LA IMAGEN
                        String Imagen = getItem(position).getImagen();
                        String Nombres = getItem(position).getNombre();
                        String Descripcion = getItem(position).getDescripcion();
                        String LinkPrenda = getItem(position).getEnlace();
                        int Vistas = getItem(position).getVistas();

                        //CONVERTIR A STRING LA VISTA
                        String VistaString = String.valueOf(Vistas);

                        //PASAMOS A LA ACTIVIDAD DETALLE IMAGEN
                        Intent intent = new Intent(getContext(), DetalleImagen.class);

                        //DATOS A ENVIAR
                        intent.putExtra("Imagen",Imagen);
                        intent.putExtra("Nombre",Nombres);
                        intent.putExtra("Descripcion",Descripcion);
                        intent.putExtra("Vistas",VistaString);
                        intent.putExtra("Enlace",LinkPrenda);

                        startActivity(intent);
                    }

                    @Override
                    public void OnIntemLongClick(View view, int position) {

                    }
                });
                return viewHolderPrimavera;
            }
        };
        //AL INICIAR LA ACTIVIDAD SE VAN A LISTAR DE DOS COLUMNAS
        sharedPreferences = getContext().getSharedPreferences("PRIMAVERA", Context.MODE_PRIVATE);
        String ordenar_en = sharedPreferences.getString("Ordenar","Dos");
        //TIPO DE VISTA
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        firebaseRecyclerAdapter2.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter2);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (firebaseRecyclerAdapter2 != null){
            firebaseRecyclerAdapter2.startListening();
        }
    }
}