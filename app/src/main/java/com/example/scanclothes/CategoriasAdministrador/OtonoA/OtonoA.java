package com.example.scanclothes.CategoriasAdministrador.OtonoA;

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
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scanclothes.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OtonoA extends AppCompatActivity {

    RecyclerView recyclerViewOtono;
    FirebaseDatabase mfirebaseDatabase;
    DatabaseReference mRef;

    FirebaseRecyclerAdapter<Otono, ViewHolderOtono> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Otono> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otono_a);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("OTOÑO");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        recyclerViewOtono = findViewById(R.id.recyclerViewOtono);
        recyclerViewOtono.setHasFixedSize(true);
        mfirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mfirebaseDatabase.getReference("OTOÑO");

        ListarImagenesOtono();
    }

    private void ListarImagenesOtono() {
        options = new FirebaseRecyclerOptions.Builder<Otono>().setQuery(mRef, Otono.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Otono, ViewHolderOtono>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderOtono viewHolderOtono, int position, @NonNull Otono otono) {
                viewHolderOtono.SeteoOtono(
                        getApplicationContext(),
                        otono.getNombre(),
                        otono.getVistas(),
                        otono.getImagen(),
                        otono.getDescripcion()
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
                        Toast.makeText(OtonoA.this, "ITEM CLICK", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void OnIntemLongClick(View view, int position) {
                        Toast.makeText(OtonoA.this, "LONG CLICK", Toast.LENGTH_SHORT).show();
                    }
                });

                return viewHolderOtono;
            }
        };
        recyclerViewOtono.setLayoutManager(new GridLayoutManager(OtonoA.this, 2));
        firebaseRecyclerAdapter.startListening();
        recyclerViewOtono.setAdapter(firebaseRecyclerAdapter);
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
                startActivity(new Intent(OtonoA.this,AgregarOtono.class));
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