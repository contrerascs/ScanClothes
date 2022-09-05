package com.example.scanclothes.FragmentosAdministrador;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.scanclothes.CategoriasAdministrador.InviernoA.InviernoA;
import com.example.scanclothes.CategoriasAdministrador.OtonoA.OtonoA;
import com.example.scanclothes.CategoriasAdministrador.PrimaveraA.PrimaveraA;
import com.example.scanclothes.CategoriasAdministrador.VeranoA.VeranoA;
import com.example.scanclothes.R;

public class InventarioAdmin extends Fragment {
    Button Invierno, Verano, Otoño, Primavera;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inventario_admin, container, false);

        Invierno = view.findViewById(R.id.Invierno);
        Verano = view.findViewById(R.id.Verano);
        Otoño = view.findViewById(R.id.Otoño);
        Primavera = view.findViewById(R.id.Primavera);

        Invierno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), InviernoA.class));
            }
        });

        Verano.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), VeranoA.class));
            }
        });

        Otoño.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), OtonoA.class));
            }
        });

        Primavera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), PrimaveraA.class));
            }
        });

        return view;
    }
}