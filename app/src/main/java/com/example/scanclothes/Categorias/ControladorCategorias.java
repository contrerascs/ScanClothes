package com.example.scanclothes.Categorias;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.scanclothes.CategoriasCliente.InviernoCliente;
import com.example.scanclothes.CategoriasCliente.OtonoCliente;
import com.example.scanclothes.CategoriasCliente.PrimaveraCliente;
import com.example.scanclothes.CategoriasCliente.VeranoCliente;
import com.example.scanclothes.R;

public class ControladorCategorias extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controlador_categorias);
        Toast.makeText(ControladorCategorias.this,"Controlador",Toast.LENGTH_SHORT).show();

        String CategoriaRecuperada = getIntent().getStringExtra("Categoria");
        if (CategoriaRecuperada.equals("Invierno")){
            startActivity(new Intent(ControladorCategorias.this, InviernoCliente.class));
            finish();
        }
        if (CategoriaRecuperada.equals("Otono")){
            startActivity(new Intent(ControladorCategorias.this, OtonoCliente.class));
            finish();
        }
        if (CategoriaRecuperada.equals("Primavera")){
            startActivity(new Intent(ControladorCategorias.this, PrimaveraCliente.class));
            finish();
        }
        if (CategoriaRecuperada.equals("Verano")){
            startActivity(new Intent(ControladorCategorias.this, VeranoCliente.class));
            finish();
        }
    }
}