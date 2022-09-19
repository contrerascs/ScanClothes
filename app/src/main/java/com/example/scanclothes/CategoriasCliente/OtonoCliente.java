package com.example.scanclothes.CategoriasCliente;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scanclothes.CategoriasAdministrador.OtonoA.Otono;
import com.example.scanclothes.CategoriasAdministrador.OtonoA.ViewHolderOtono;
import com.example.scanclothes.DetalleImagen.DetalleImagen;
import com.example.scanclothes.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OtonoCliente extends AppCompatActivity {
    RecyclerView recyclerViewOtonoC;
    FirebaseDatabase mfirebaseDatabase;
    DatabaseReference mRef;
    FirebaseRecyclerAdapter<Otono, ViewHolderOtono> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Otono> options;

    SharedPreferences sharedPreferences;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otono_cliente);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("OTOÑO");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        recyclerViewOtonoC = findViewById(R.id.recyclerViewOtonoC);
        recyclerViewOtonoC.setHasFixedSize(true);
        mfirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mfirebaseDatabase.getReference("OTOÑO");

        dialog = new Dialog(OtonoCliente.this);

        ListarImagenesOtono();
    }

    private void ListarImagenesOtono() {
        options = new FirebaseRecyclerOptions.Builder<Otono>().setQuery(mRef,Otono.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Otono, ViewHolderOtono>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderOtono viewHolderOtono, int position, @NonNull Otono Otono) {
                viewHolderOtono.SeteoOtono(
                        getApplicationContext(),
                        Otono.getNombre(),
                        Otono.getVistas(),
                        Otono.getImagen(),
                        Otono.getDescripcion(),
                        Otono.getId_administrador()
                );
            }

            @NonNull
            @Override
            public ViewHolderOtono onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                //INFLAR EN ITEM
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_otono, parent, false);

                ViewHolderOtono viewHolderOtono = new ViewHolderOtono(itemView);

                viewHolderOtono.setOnClickListener(new ViewHolderOtono.ClickListener() {
                    @Override
                    public void OnIntemClick(View view, int position) {
                        //OBTENER LOS DATOS DE LA IMAGEN
                        String Imagen = getItem(position).getImagen();
                        String Nombres = getItem(position).getNombre();
                        String Descripcion = getItem(position).getDescripcion();
                        int Vistas = getItem(position).getVistas();

                        //CONVERTIR A STRING LA VISTA
                        String VistaString = String.valueOf(Vistas);

                        //PASAMOS A LA ACTIVIDAD DETALLE IMAGEN
                        Intent intent = new Intent(OtonoCliente.this, DetalleImagen.class);

                        //DATOS A ENVIAR
                        intent.putExtra("Imagen",Imagen);
                        intent.putExtra("Nombre",Nombres);
                        intent.putExtra("Descripcion",Descripcion);
                        intent.putExtra("Vistas",VistaString);

                        startActivity(intent);

                    }

                    @Override
                    public void OnIntemLongClick(View view, int position) {

                    }
                });
                return viewHolderOtono;
            }
        };
        //AL INICIAR LA ACTIVIDAD SE VAN A LISTAR DE DOS COLUMNAS
        sharedPreferences = OtonoCliente.this.getSharedPreferences("OTONO",MODE_PRIVATE);
        String ordenar_en = sharedPreferences.getString("Ordenar","Dos");
        //ELEGIR EL TIPO DE VISTA
        if (ordenar_en.equals("Dos")){
            recyclerViewOtonoC.setLayoutManager(new GridLayoutManager(OtonoCliente.this,2));
            firebaseRecyclerAdapter.startListening();
            recyclerViewOtonoC.setAdapter(firebaseRecyclerAdapter);
        }else if(ordenar_en.equals("Tres")){
            recyclerViewOtonoC.setLayoutManager(new GridLayoutManager(OtonoCliente.this,3));
            firebaseRecyclerAdapter.startListening();
            recyclerViewOtonoC.setAdapter(firebaseRecyclerAdapter);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(firebaseRecyclerAdapter!=null){
            firebaseRecyclerAdapter.startListening();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_vista,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.Vista) {
            Ordenar_Imagenes();
        }
        return super.onOptionsItemSelected(item);
    }

    private void Ordenar_Imagenes(){
        //CAMBIO DE LETRA
        String ubicacion = "fuentes/CaviarDreams.ttf";
        Typeface tf = Typeface.createFromAsset(OtonoCliente.this.getAssets(),
                ubicacion);
        //CAMBIO DE LETRA

        //DECLARANDO VISTAS
        TextView OrdenarTXT;
        Button Dos_Columnas,Tres_Columnas;

        //CONEXION CON EL CUADRO DE DIALOGO
        dialog.setContentView(R.layout.dialog_ordenar);

        //INICIALIZAR LAS VISTAS
        OrdenarTXT = dialog.findViewById(R.id.OrdenarTXT);
        Dos_Columnas = dialog.findViewById(R.id.Dos_Columnas);
        Tres_Columnas = dialog.findViewById(R.id.Tres_Columnas);

        //CAMBIO DE FUENTE DE LETRA
        OrdenarTXT.setTypeface(tf);
        Dos_Columnas.setTypeface(tf);
        Tres_Columnas.setTypeface(tf);

        //EVENTO DOS COLUMNAS
        Dos_Columnas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Ordenar","Dos");
                editor.apply();
                recreate();
                dialog.dismiss();
            }
        });

        Tres_Columnas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Ordenar","Tres");
                editor.apply();
                recreate();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}