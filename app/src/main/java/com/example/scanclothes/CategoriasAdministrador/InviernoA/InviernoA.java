package com.example.scanclothes.CategoriasAdministrador.InviernoA;

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

public class InviernoA extends AppCompatActivity {

    RecyclerView recyclerViewInvierno;
    FirebaseDatabase mfirebaseDatabase;
    DatabaseReference mRef;

    FirebaseRecyclerAdapter <Invierno,ViewHolderInvierno> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions <Invierno> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invierno_a);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("INVIERNO");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        recyclerViewInvierno = findViewById(R.id.recyclerViewInvierno);
        recyclerViewInvierno.setHasFixedSize(true);
        mfirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mfirebaseDatabase.getReference("INVIERNO");
        
        ListarImagenesInvierno();
    }

    private void ListarImagenesInvierno() {
        options = new FirebaseRecyclerOptions.Builder<Invierno>().setQuery(mRef,Invierno.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Invierno, ViewHolderInvierno>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderInvierno viewHolderInvierno, int position, @NonNull Invierno invierno) {
                viewHolderInvierno.SeteoInvierno(
                        getApplicationContext(),
                        invierno.getNombre(),
                        invierno.getVistas(),
                        invierno.getImagen(),
                        invierno.getDescripcion()
                );
            }

            @NonNull
            @Override
            public ViewHolderInvierno onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                //INFLAR EN ITEM
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invierno,parent,false);

                ViewHolderInvierno viewHolderInvierno = new ViewHolderInvierno(itemView);

                viewHolderInvierno.setOnClickListener(new ViewHolderInvierno.ClickListener() {
                    @Override
                    public void OnIntemClick(View view, int position) {
                        Toast.makeText(InviernoA.this,"ITEM CLICK",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void OnIntemLongClick(View view, int position) {
                        Toast.makeText(InviernoA.this,"LONG CLICK", Toast.LENGTH_SHORT).show();
                    }
                });

                return viewHolderInvierno;
            }
        };
        recyclerViewInvierno.setLayoutManager(new GridLayoutManager(InviernoA.this,2));
        firebaseRecyclerAdapter.startListening();
        recyclerViewInvierno.setAdapter(firebaseRecyclerAdapter);
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
                startActivity(new Intent(InviernoA.this,AgregarInvierno.class));
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