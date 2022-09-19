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

import com.example.scanclothes.CategoriasAdministrador.VeranoA.Verano;
import com.example.scanclothes.CategoriasAdministrador.VeranoA.ViewHolderVerano;
import com.example.scanclothes.DetalleImagen.DetalleImagen;
import com.example.scanclothes.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class VeranoCliente extends AppCompatActivity {
    RecyclerView recyclerViewVeranoC;
    FirebaseDatabase mfirebaseDatabase;
    DatabaseReference mRef;
    FirebaseRecyclerAdapter<Verano, ViewHolderVerano> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Verano> options;

    SharedPreferences sharedPreferences;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verano_cliente);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("VERANO");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        recyclerViewVeranoC = findViewById(R.id.recyclerViewVeranoC);
        recyclerViewVeranoC.setHasFixedSize(true);
        mfirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mfirebaseDatabase.getReference("VERANO");

        dialog = new Dialog(VeranoCliente.this);

        ListarImagenesInvierno();
    }

    private void ListarImagenesInvierno() {
        options = new FirebaseRecyclerOptions.Builder<Verano>().setQuery(mRef,Verano.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Verano, ViewHolderVerano>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderVerano viewHolderVerano, int position, @NonNull Verano verano) {
                viewHolderVerano.SeteoVerano(
                        getApplicationContext(),
                        verano.getNombre(),
                        verano.getVistas(),
                        verano.getImagen(),
                        verano.getDescripcion(),
                        verano.getId_administrador()
                );
            }

            @NonNull
            @Override
            public ViewHolderVerano onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                //INFLAR EN ITEM
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_verano, parent, false);

                ViewHolderVerano viewHolderVerano = new ViewHolderVerano(itemView);

                viewHolderVerano.setOnClickListener(new ViewHolderVerano.ClickListener() {
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
                        Intent intent = new Intent(VeranoCliente.this, DetalleImagen.class);

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
                return viewHolderVerano;
            }
        };
        //AL INICIAR LA ACTIVIDAD SE VAN A LISTAR DE DOS COLUMNAS
        sharedPreferences = VeranoCliente.this.getSharedPreferences("VERANO",MODE_PRIVATE);
        String ordenar_en = sharedPreferences.getString("Ordenar","Dos");
        //ELEGIR EL TIPO DE VISTA
        if (ordenar_en.equals("Dos")){
            recyclerViewVeranoC.setLayoutManager(new GridLayoutManager(VeranoCliente.this,2));
            firebaseRecyclerAdapter.startListening();
            recyclerViewVeranoC.setAdapter(firebaseRecyclerAdapter);
        }else if(ordenar_en.equals("Tres")){
            recyclerViewVeranoC.setLayoutManager(new GridLayoutManager(VeranoCliente.this,3));
            firebaseRecyclerAdapter.startListening();
            recyclerViewVeranoC.setAdapter(firebaseRecyclerAdapter);
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
        Typeface tf = Typeface.createFromAsset(VeranoCliente.this.getAssets(),
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