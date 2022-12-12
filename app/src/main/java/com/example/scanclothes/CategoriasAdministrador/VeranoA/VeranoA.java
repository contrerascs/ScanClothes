package com.example.scanclothes.CategoriasAdministrador.VeranoA;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

import android.app.Dialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scanclothes.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

public class VeranoA extends AppCompatActivity {

    RecyclerView recyclerViewVerano;
    FirebaseDatabase mfirebaseDatabase;
    DatabaseReference mRef;

    FirebaseRecyclerAdapter<Verano, ViewHolderVerano> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Verano> options;

    FirebaseAuth firebaseAuth;
    SharedPreferences sharedPreferences;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verano_a);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("VERANO");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        recyclerViewVerano = findViewById(R.id.recyclerViewVerano);
        recyclerViewVerano.setHasFixedSize(true);
        mfirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mfirebaseDatabase.getReference("VERANO");
        firebaseAuth = FirebaseAuth.getInstance();

        dialog = new Dialog(VeranoA.this);

        ListarImagenesVerano();
    }

    private void ListarImagenesVerano() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        Query query = mRef.orderByChild("id_administrador").equalTo(user.getUid());
        options = new FirebaseRecyclerOptions.Builder<Verano>().setQuery(query, Verano.class).build();

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
                        Toast.makeText(VeranoA.this, "ITEM CLICK", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void OnIntemLongClick(View view, int position) {
                        final String id = getItem(position).getId();
                        final String Nombre = getItem(position).getNombre();
                        final String Imagen = getItem(position).getImagen();

                        String Descripcion = getItem(position).getDescripcion();
                        int Vista = getItem(position).getVistas();
                        String vistaString = String.valueOf(Vista);

                        AlertDialog.Builder builder = new AlertDialog.Builder(VeranoA.this);
                        String [] opciones = {"Actualizar","Eliminar"};
                        builder.setItems(opciones, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(i == 0){
                                    Intent intent = new Intent(VeranoA.this, AgregarVerano.class);
                                    intent.putExtra("NombreEnviado",Nombre);
                                    intent.putExtra("ImagenEnviada",Imagen);
                                    intent.putExtra("DescripcionEnviada",Descripcion);
                                    intent.putExtra("IdEnviado",id);
                                    intent.putExtra("VistaEnviada",vistaString);
                                    startActivity(intent);
                                }if (i == 1){
                                    EliminarDatos(id,Imagen);
                                }
                            }
                        });
                        builder.create().show();
                    }
                });

                return viewHolderVerano;
            }
        };
        //AL INICIAR LA ACTIVIDAD SE VAN A LISTAR DE DOS COLUMNAS
        sharedPreferences = VeranoA.this.getSharedPreferences("VERANO",MODE_PRIVATE);
        String ordenar_en = sharedPreferences.getString("Ordenar","Dos");
        //ELEGIR EL TIPO DE VISTA
        if (ordenar_en.equals("Dos")){
            recyclerViewVerano.setLayoutManager(new GridLayoutManager(VeranoA.this,2));
            firebaseRecyclerAdapter.startListening();
            recyclerViewVerano.setAdapter(firebaseRecyclerAdapter);
        }else if(ordenar_en.equals("Tres")){
            recyclerViewVerano.setLayoutManager(new GridLayoutManager(VeranoA.this,3));
            firebaseRecyclerAdapter.startListening();
            recyclerViewVerano.setAdapter(firebaseRecyclerAdapter);
        }
    }

    private void EliminarDatos(final String idActual, final String ImagenActual){
        AlertDialog.Builder builder = new AlertDialog.Builder(VeranoA.this);
        builder.setTitle("Eliminar");
        builder.setMessage("¿Desea elimminar imagen?");

        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //ELIMINAR IMAGEN DE LA BASE DE DATOS
                Query query = mRef.orderByChild("id").equalTo(idActual);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()){
                            ds.getRef().removeValue();
                        }
                        Toast.makeText(VeranoA.this,"La imagen ha sido eliminada",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(VeranoA.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
                StorageReference ImagenSeleccionada = getInstance().getReferenceFromUrl(ImagenActual);
                ImagenSeleccionada.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(VeranoA.this,"Eliminado",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(VeranoA.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(VeranoA.this,"Cancelado por administrador",Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();
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
        menuInflater.inflate(R.menu.menu_agregar,menu);
        menuInflater.inflate(R.menu.menu_vista,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.Agregar:
                //Toast.makeText(this,"Agregar imagen", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(VeranoA.this,AgregarVerano.class));
                finish();
                break;

            case R.id.Vista:
                Ordenar_Imagenes();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void Ordenar_Imagenes(){
        //CAMBIO DE LETRA
        String ubicacion = "fuentes/CaviarDreams.ttf";
        Typeface tf = Typeface.createFromAsset(VeranoA.this.getAssets(),
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