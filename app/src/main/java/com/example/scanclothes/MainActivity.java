package com.example.scanclothes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.scanclothes.FragmentosAdministrador.InicioAdmin;
import com.example.scanclothes.FragmentosAdministrador.InventarioAdmin;
import com.example.scanclothes.FragmentosAdministrador.PerfilAdmin;
import com.example.scanclothes.FragmentosAdministrador.RegistroAdmin;
import com.example.scanclothes.FragmentosCliente.CategoriasCliente;
import com.example.scanclothes.FragmentosCliente.EscanerCliente;
import com.example.scanclothes.FragmentosCliente.InicioCliente;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //Fragmento por defecto
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_contairner,
                    new InicioCliente()).commit();
            navigationView.setCheckedItem(R.id.InicioCliente);

        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.InicioCliente:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_contairner,
                        new InicioCliente()).commit();
                break;

            case R.id.RegistrarCuenta:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_contairner,
                        new RegistroAdmin()).commit();
                break;

            case R.id.CategoriasCliente:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_contairner,
                        new CategoriasCliente()).commit();
                break;

            case R.id.EscanerCliente:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_contairner,
                        new EscanerCliente()).commit();
                break;

            case R.id.SalirCliente:
                Toast.makeText(this,"Saliste de la aplicación", Toast.LENGTH_SHORT).show();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}