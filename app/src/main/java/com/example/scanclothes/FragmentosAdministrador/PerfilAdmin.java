package com.example.scanclothes.FragmentosAdministrador;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.scanclothes.MainActivityAdministrador;
import com.example.scanclothes.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class PerfilAdmin extends Fragment {

    //FIREBASE
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference BASE_DE_DATOS_ADMINISTRADORES;

    StorageReference storageReference;
    String RutaDeAlmacenamiento = "Fotos_Perfil_Administradores/*";

    private Uri imagen_uri;
    private String imagen_perfil;
    private ProgressDialog progressDialog;

    //VISTAS
    ImageView FOTOPERFILIMG;
    TextView UIDPERFIL, NOMBREPERFIL, APELLIDOPERFIL, EDADPERFIL, CORREOPERFIL, PASSWORDPERFIL;
    Button ACTUALIZARPASS, ACTUALIZARDATOS;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil_admin, container, false);

        //TEXTVIEWS
        FOTOPERFILIMG = view.findViewById(R.id.FOTOPERFILIMG);
        UIDPERFIL = view.findViewById(R.id.UIDPERFIL);
        NOMBREPERFIL = view.findViewById(R.id.NOMBREPERFIL);
        APELLIDOPERFIL = view.findViewById(R.id.APELLIDOPERFIL);
        EDADPERFIL = view.findViewById(R.id.EDADPERFIL);
        CORREOPERFIL = view.findViewById(R.id.CORREOPERFIL);
        PASSWORDPERFIL = view.findViewById(R.id.PASSWORLDPERFIL);

        //BOTONES
        ACTUALIZARPASS = view.findViewById(R.id.ACTUALIZARPASS);
        ACTUALIZARDATOS = view.findViewById(R.id.ACTUALIZARDATOS);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        storageReference = getInstance().getReference();

        progressDialog = new ProgressDialog(getActivity());

        BASE_DE_DATOS_ADMINISTRADORES = FirebaseDatabase.getInstance().getReference("BASE DE DATOS ADMINISTRADORES");
        BASE_DE_DATOS_ADMINISTRADORES.child(user.getUid()).addValueEventListener(new ValueEventListener() {

            //VISUALIZAR EN FRAGMENTO
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    //OBTENER DATOS
                    String uid = ""+snapshot.child("UID").getValue();
                    String nombre = ""+snapshot.child("NOMBRES").getValue();
                    String apellidos = ""+snapshot.child("APELLIDOS").getValue();
                    String edad = ""+snapshot.child("EDAD").getValue();
                    String correo = ""+snapshot.child("CORREO").getValue();
                    String password = ""+snapshot.child("PASSWORD").getValue();
                    String imagen = ""+snapshot.child("IMAGEN").getValue();

                    UIDPERFIL.setText(uid);
                    NOMBREPERFIL.setText(nombre);
                    APELLIDOPERFIL.setText(apellidos);
                    EDADPERFIL.setText(edad);
                    CORREOPERFIL.setText(correo);
                    PASSWORDPERFIL.setText(password);

                    try {
                        //SI EXISTE LA IMAGEN
                        Picasso.get().load(imagen).placeholder(R.drawable.administrador).into(FOTOPERFILIMG);
                    }catch (Exception e){
                        //NO EXISTE IMAGEN
                        Picasso.get().load(R.drawable.administrador).into(FOTOPERFILIMG);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FOTOPERFILIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                CambiarImagenPerfilAdministrador();
            }
        });

        return view;
    }

    //ALERTDIALOG PARA EDITAR IMAGEN
    private void CambiarImagenPerfilAdministrador() {
        String [] opcion = {"Cambiar foto de perfil"};
        //CREAR ALERT DIALOG
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Elegir una opcion");
        builder.setItems(opcion, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i == 0){
                    imagen_perfil = "IMAGEN";
                    ElegirFoto();
                }
            }
        });
        builder.create().show();
    }

    //ELEGIR DE DONDE PROCEDE LA IMAGEN
    private void ElegirFoto() {
        String [] opciones = {"Camara","Galeria"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Seleccionar imagen de: ");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i == 0){
                    if (ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED){
                        Elegir_De_Camara();
                    }else{
                        SolicitudPermisoCamara.launch(Manifest.permission.CAMERA);
                    }
                }else if (i == 1){
                    if (ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_GRANTED){
                        Elegir_De_Galeria();
                    }else{
                        SolicitudPermisoGaleria.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    }
                }
            }
        });
        builder.create().show();
    }

    private void Elegir_De_Galeria() {
        Intent GaleriaIntent = new Intent(Intent.ACTION_PICK);
        GaleriaIntent.setType("image/*");
        ObtenerImagenGaleria.launch(GaleriaIntent);

    }

    private void Elegir_De_Camara() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"Foto Temporal");
        values.put(MediaStore.Images.Media.DESCRIPTION,"Descripcion Temporal");
        imagen_uri = (getActivity()).getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        //ACTIVIDAD PARA ABRIR LA CAMARA
        Intent camaraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camaraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imagen_uri);
        ObtenerImagenCamara.launch(camaraIntent);

    }

    private ActivityResultLauncher<Intent> ObtenerImagenCamara = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode()==Activity.RESULT_OK){
                        ActualizarImagenEnBD(imagen_uri);
                        progressDialog.setTitle("Procesando");
                        progressDialog.setMessage("La imagen se está cambiando");
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                    }else{
                        Toast.makeText(getActivity(),"Cancelado por el usuario",Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    private ActivityResultLauncher<Intent> ObtenerImagenGaleria = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode()==Activity.RESULT_OK){
                        Intent data = result.getData();
                        imagen_uri = data.getData();
                        ActualizarImagenEnBD(imagen_uri);
                        progressDialog.setTitle("Procesando");
                        progressDialog.setMessage("La imagen se está cambiando");
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                    }else{
                        Toast.makeText(getActivity(),"Cancelado por el usuario",Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    private ActivityResultLauncher <String> SolicitudPermisoCamara =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted ->{
                if (isGranted){
                    Elegir_De_Camara();
                }else{
                    Toast.makeText(getActivity(),"Permiso Denegado",Toast.LENGTH_SHORT).show();
                }
            });

    private ActivityResultLauncher <String> SolicitudPermisoGaleria =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted ->{
                //CODIGO PROVISIONAL MIENTRAS ARREGLAMOS EL ERROR DEL PERMISO
                if (!isGranted){
                    Elegir_De_Galeria();
                }else{
                    Toast.makeText(getActivity(),"Permiso Denegado",Toast.LENGTH_SHORT).show();
                }
            });

    private void ActualizarImagenEnBD(Uri uri){
        String Ruta_de_archivo_y_nombre = RutaDeAlmacenamiento+""+imagen_perfil+"_"+user.getUid();
        StorageReference storageReference2 = storageReference.child(Ruta_de_archivo_y_nombre);
        storageReference2.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task <Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                Uri downloadUri = uriTask.getResult();

                if(uriTask.isSuccessful()){
                    HashMap<String, Object> results = new HashMap<>();
                    results.put(imagen_perfil,downloadUri.toString());
                    BASE_DE_DATOS_ADMINISTRADORES.child(user.getUid()).updateChildren(results)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    startActivity(new Intent(getActivity(),MainActivityAdministrador.class));
                                    getActivity().finish();
                                    Toast.makeText(getActivity(),"Imagen cambiada con éxito",Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(getActivity(),"Ha ocurrido un error",Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}