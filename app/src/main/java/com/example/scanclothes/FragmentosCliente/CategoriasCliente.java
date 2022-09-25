package com.example.scanclothes.FragmentosCliente;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.scanclothes.CategoriasCliente.InviernoCliente;
import com.example.scanclothes.CategoriasCliente.OtonoCliente;
import com.example.scanclothes.CategoriasCliente.PrimaveraCliente;
import com.example.scanclothes.CategoriasCliente.VeranoCliente;
import com.example.scanclothes.R;

public class CategoriasCliente extends Fragment {

    Button Invierno, Verano, Otoño, Primavera;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_categorias_cliente, container, false);

        Invierno = view.findViewById(R.id.Invierno);
        Verano = view.findViewById(R.id.Verano);
        Otoño = view.findViewById(R.id.Otoño);
        Primavera = view.findViewById(R.id.Primavera);

        Invierno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), InviernoCliente.class));
            }
        });

        Verano.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), VeranoCliente.class));
            }
        });

        Otoño.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), OtonoCliente.class));
            }
        });

        Primavera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), PrimaveraCliente.class));
            }
        });

        return view;
    }
}