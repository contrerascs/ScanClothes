package com.example.scanclothes.CategoriasAdministrador.InviernoA;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.scanclothes.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AgregarInvierno extends AppCompatActivity {

    EditText NombreInvierno, DescripcionPrendaInv;
    TextView VistaInvierno;
    ImageView ImagenPrendaInv;
    Button AgregarPrendaInv;

    String RutaDeAlmacenamiento = "Invierno_Subida/";
    String RutaDeBaseDeDatos = "INVIERNO";
    Uri RutaArchivoUri;

    StorageReference mStorageReference;
    DatabaseReference DatabaseReference;

    ProgressDialog progressDialog;
    int CODIGO_DE_SOLICITTUD_IMAGEN = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_invierno);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Agregar");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        VistaInvierno = findViewById(R.id.VistaInvierno);
        NombreInvierno = findViewById(R.id.NombreInvierno);
        ImagenPrendaInv = findViewById(R.id.ImagenPrendaInv);
        DescripcionPrendaInv = findViewById(R.id.DescripcionPrendaInv);
        AgregarPrendaInv = findViewById(R.id.AgregarPrendaInv);

        mStorageReference = FirebaseStorage.getInstance().getReference();
        DatabaseReference = FirebaseDatabase.getInstance().getReference(RutaDeBaseDeDatos);
        progressDialog = new ProgressDialog(AgregarInvierno.this);

        ImagenPrendaInv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Seleccionar imagen"),CODIGO_DE_SOLICITTUD_IMAGEN);
            }
        });

        AgregarPrendaInv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //METODO PARA SUBIRUNA IMAGEN
                SubirImagen();
            }
        });
    }

    private void SubirImagen() {
        if(RutaArchivoUri!=null){
            progressDialog.setTitle("Espere por favor");
            progressDialog.setMessage("Subiendo Imagen INVIERNO...");
            progressDialog.show();
            progressDialog.setCancelable(false);
            StorageReference storageReference2 = mStorageReference.child(RutaDeAlmacenamiento+System.currentTimeMillis()+"."+ObtenerExtensionDelArchivo(RutaArchivoUri));
            storageReference2.putFile(RutaArchivoUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while(!uriTask.isSuccessful());

                            Uri downloadURI = uriTask.getResult();

                            String mNombre = NombreInvierno.getText().toString();
                            String mDescripcion = DescripcionPrendaInv.getText().toString();
                            String mVista = VistaInvierno.getText().toString();
                            int VISTA = Integer.parseInt(mVista);

                            Invierno invierno = new Invierno(mNombre, mDescripcion,downloadURI.toString(),VISTA);
                            String ID_IMAGEN = DatabaseReference.push().getKey();

                            DatabaseReference.child(ID_IMAGEN).setValue(invierno);

                            progressDialog.dismiss();
                            Toast.makeText(AgregarInvierno.this,"Subido Exitosamente", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(AgregarInvierno.this, InviernoA.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AgregarInvierno.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onPaused(@NonNull UploadTask.TaskSnapshot snapshot) {
                    progressDialog.setTitle("Agregada");
                    progressDialog.setCancelable(false);
                }
            });
        }
        else{
            Toast.makeText(this,"DEBE ASIGNAR UNA IMAGEN",Toast.LENGTH_SHORT).show();
        }
    }

    //OBTENEMOS LA EXTENSION .JPG/.PNG
    private String ObtenerExtensionDelArchivo(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    //COMPROBAR SI LA IMAGEN SELECCIONADA POR EL ADMINISTRADOR FUE CORRECTA
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CODIGO_DE_SOLICITTUD_IMAGEN
                && resultCode == RESULT_OK
                && data != null
                & data.getData() != null){
            RutaArchivoUri = data.getData();
            try {
                //CONVERTIMOS A UN BITMAP
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),RutaArchivoUri);
                //SETEAMOS LA IMAGEN
                ImagenPrendaInv.setImageBitmap(bitmap);
            }catch (Exception e){
                Toast.makeText(this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    }
}