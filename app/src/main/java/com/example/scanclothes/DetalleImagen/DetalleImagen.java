package com.example.scanclothes.DetalleImagen;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.scanclothes.R;
import com.github.clans.fab.FloatingActionButton;
import com.squareup.picasso.Picasso;

public class DetalleImagen extends AppCompatActivity {

    ImageView ImagenDetalle;
    TextView NombreImagenDetalle, DescripcionImagen, VistaDetalle;
    Button VisualizarEnProbador,ObtenerPrenda;

    FloatingActionButton fabDescargar,fabCompratir,fabEstablecer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_imagen);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Detalle");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        ImagenDetalle = findViewById(R.id.ImagenDetalle);
        NombreImagenDetalle = findViewById(R.id.NombreImagenDetalle);
        DescripcionImagen = findViewById(R.id.DescripcionImagen);
        VistaDetalle = findViewById(R.id.VistaDetalle);

        VisualizarEnProbador = findViewById(R.id.VisualizarEnProbador);
        ObtenerPrenda = findViewById(R.id.ObtenerPrenda);

        fabDescargar = findViewById(R.id.fabDescargar);
        fabCompratir = findViewById(R.id.fabCompratir);
        fabEstablecer = findViewById(R.id.fabEstablecer);

        String Imagen = getIntent().getStringExtra("Imagen");
        String Nombre = getIntent().getStringExtra("Nombre");
        String Descripcion = getIntent().getStringExtra("Descripcion");
        String Vistas = getIntent().getStringExtra("Vistas");

        try {
            //SI LA IMAGEN FUE TRAIDA
            Picasso.get().load(Imagen).placeholder(R.drawable.detalle_imagen).into(ImagenDetalle);
        }catch (Exception e){
            //SI LA IMAGEN NO FUE TRAIDA
            Picasso.get().load(R.drawable.detalle_imagen).into(ImagenDetalle);
        }

        NombreImagenDetalle.setText(Nombre);
        DescripcionImagen.setText(Descripcion);
        VistaDetalle.setText(Vistas);

        VisualizarEnProbador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DetalleImagen.this,"Probador Virtual",Toast.LENGTH_SHORT).show();
            }
        });

        ObtenerPrenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DetalleImagen.this,"Obtener Prenda",Toast.LENGTH_SHORT).show();
            }
        });

        fabDescargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DetalleImagen.this,"Descargar",Toast.LENGTH_SHORT).show();
            }
        });

        fabCompratir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DetalleImagen.this,"Compartir",Toast.LENGTH_SHORT).show();
            }
        });

        fabEstablecer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DetalleImagen.this,"Compartir",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}