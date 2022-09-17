package com.example.scanclothes.FragmentosCliente;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scanclothes.Categorias.Categoria;
import com.example.scanclothes.Categorias.ControladorCategorias;
import com.example.scanclothes.Categorias.ViewHolderCategoria;
import com.example.scanclothes.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InicioCliente extends Fragment {

    RecyclerView recyclerViewCategorias;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    LinearLayoutManager linearLayoutManager;

    FirebaseRecyclerAdapter<Categoria, ViewHolderCategoria> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Categoria> options;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inicio_cliente, container, false);

        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("CATEGORIAS");
        linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);

        recyclerViewCategorias = view.findViewById(R.id.recyclerViewCategorias);
        recyclerViewCategorias.setHasFixedSize(true);
        recyclerViewCategorias.setLayoutManager(linearLayoutManager);

        VerCategorias();

        return view;
    }

    private void VerCategorias(){
        options = new FirebaseRecyclerOptions.Builder<Categoria>().setQuery(reference,Categoria.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Categoria, ViewHolderCategoria>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderCategoria viewHolderCategoria, int position, @NonNull Categoria categoria) {
                viewHolderCategoria.SeteoCategoria(
                        getActivity(),
                        categoria.getCategoria(),
                        categoria.getImagen()
                );
            }

            @NonNull
            @Override
            public ViewHolderCategoria onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.categorias_dispositivo,parent,false);
                ViewHolderCategoria viewHolderCategoria = new ViewHolderCategoria(itemView);

                viewHolderCategoria.setOnClickListener(new ViewHolderCategoria.ClickListener() {
                    @Override
                    public void OnIntemClick(View view, int position) {
                        //OBTENEMOS EL NOMBRE DE LA CATEGORIA
                        String categoria = getItem(position).getCategoria();

                        //PASAMOS EL NOMBRE DE LA CATEGORIA
                        Intent intent = new Intent(view.getContext(), ControladorCategorias.class);
                        intent.putExtra("Categoria",categoria);
                        startActivity(intent);
                        Toast.makeText(getActivity(),categoria,Toast.LENGTH_SHORT).show();
                    }
                });

                return viewHolderCategoria;
            }
        };
        recyclerViewCategorias.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (firebaseRecyclerAdapter != null){
            firebaseRecyclerAdapter.startListening();
        }
    }
}