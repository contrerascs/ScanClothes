package com.example.scanclothes.CategoriasAdministrador.VeranoA;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scanclothes.CategoriasAdministrador.PrimaveraA.AgregarPrimavera;
import com.example.scanclothes.CategoriasAdministrador.PrimaveraA.PrimaveraA;
import com.example.scanclothes.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

        ListarImagenesVerano();
    }

    private void ListarImagenesVerano() {
        options = new FirebaseRecyclerOptions.Builder<Verano>().setQuery(mRef, Verano.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Verano, ViewHolderVerano>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderVerano viewHolderVerano, int position, @NonNull Verano verano) {
                viewHolderVerano.SeteoVerano(
                        getApplicationContext(),
                        verano.getNombre(),
                        verano.getVistas(),
                        verano.getImagen(),
                        verano.getDescripcion()
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
                        Toast.makeText(VeranoA.this, "LONG CLICK", Toast.LENGTH_SHORT).show();
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
                                    intent.putExtra("VistaEnviada",vistaString);
                                    startActivity(intent);
                                }if (i == 1){
                                    EliminarDatos(Nombre,Imagen);
                                }
                            }
                        });
                        builder.create().show();
                    }
                });

                return viewHolderVerano;
            }
        };
        recyclerViewVerano.setLayoutManager(new GridLayoutManager(VeranoA.this, 2));
        firebaseRecyclerAdapter.startListening();
        recyclerViewVerano.setAdapter(firebaseRecyclerAdapter);
    }

    private void EliminarDatos(final String NombreActual, final String ImagenActual){
        AlertDialog.Builder builder = new AlertDialog.Builder(VeranoA.this);
        builder.setTitle("Eliminar");
        builder.setMessage("¿Desea elimminar imagen?");

        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //ELIMINAR IMAGEN DE LA BASE DE DATOS
                Query query = mRef.orderByChild("nombre").equalTo(NombreActual);
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
                Toast.makeText(this, "Listar imagenes", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}