package com.example.scanclothes.FragmentosCliente;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.scanclothes.InicioSesion;
import com.example.scanclothes.R;
import com.example.scanclothes.Registro;

public class IngresarRegistrar extends Fragment {

    Button ingresarCliente;
    Button registrarCliente;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ingresar_registrar, container, false);

        ingresarCliente = view.findViewById(R.id.IngresarCliente);
        registrarCliente = view.findViewById(R.id.RegistrarCliente);

        ingresarCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), InicioSesion.class));
            }
        });

        registrarCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Registro.class));
            }
        });

        return view;
    }
}