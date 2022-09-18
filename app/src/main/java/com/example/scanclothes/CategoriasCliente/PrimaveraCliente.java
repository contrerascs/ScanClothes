package com.example.scanclothes.CategoriasCliente;

import android.app.Dialog;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scanclothes.CategoriasAdministrador.PrimaveraA.Primavera;
import com.example.scanclothes.CategoriasAdministrador.PrimaveraA.ViewHolderPrimavera;
import com.example.scanclothes.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PrimaveraCliente extends AppCompatActivity {
    RecyclerView recyclerViewPrimaveraC;
    FirebaseDatabase mfirebaseDatabase;
    DatabaseReference mRef;
    FirebaseRecyclerAdapter<Primavera, ViewHolderPrimavera> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Primavera> options;

    SharedPreferences sharedPreferences;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primavera_cliente);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("PRIMAVERA");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        recyclerViewPrimaveraC = findViewById(R.id.recyclerViewPrimaveraC);
        recyclerViewPrimaveraC.setHasFixedSize(true);
        mfirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mfirebaseDatabase.getReference("PRIMAVERA");

        dialog = new Dialog(PrimaveraCliente.this);

        ListarImagenesInvierno();
    }

    private void ListarImagenesInvierno() {
        options = new FirebaseRecyclerOptions.Builder<Primavera>().setQuery(mRef,Primavera.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Primavera, ViewHolderPrimavera>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderPrimavera viewHolderPrimavera, int position, @NonNull Primavera primavera) {
                viewHolderPrimavera.SeteoPrimavera(
                        getApplicationContext(),
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
                        Toast.makeText(PrimaveraCliente.this, "ITEM CLICK", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void OnIntemLongClick(View view, int position) {

                    }
                });
                return viewHolderPrimavera;
            }
        };
        //AL INICIAR LA ACTIVIDAD SE VAN A LISTAR DE DOS COLUMNAS
        sharedPreferences = PrimaveraCliente.this.getSharedPreferences("PRIMAVERA",MODE_PRIVATE);
        String ordenar_en = sharedPreferences.getString("Ordenar","Dos");
        //ELEGIR EL TIPO DE VISTA
        if (ordenar_en.equals("Dos")){
            recyclerViewPrimaveraC.setLayoutManager(new GridLayoutManager(PrimaveraCliente.this,2));
            firebaseRecyclerAdapter.startListening();
            recyclerViewPrimaveraC.setAdapter(firebaseRecyclerAdapter);
        }else if(ordenar_en.equals("Tres")){
            recyclerViewPrimaveraC.setLayoutManager(new GridLayoutManager(PrimaveraCliente.this,3));
            firebaseRecyclerAdapter.startListening();
            recyclerViewPrimaveraC.setAdapter(firebaseRecyclerAdapter);
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
        Typeface tf = Typeface.createFromAsset(PrimaveraCliente.this.getAssets(),
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