package com.example.scanclothes.CategoriasAdministrador.InviernoA;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

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
    String rNombre,rImagen,rDescripcion,rVista;
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

        Bundle intent = getIntent().getExtras();
        if(intent!=null){
            //RECUPERAR LOS DATOS DE LA ACTIVIDAD ANTERIOR
            rNombre = intent.getString("NombreEnviado");
            rImagen = intent.getString("ImagenEnviada");
            rDescripcion = intent.getString("DescripcionEnviada");
            rVista = intent.getString("VistaEnviada");

            //SETEAR LOS DATOS EN LOS TEXTVIEW
            NombreInvierno.setText(rNombre);
            VistaInvierno.setText(rVista);
            DescripcionPrendaInv.setText(rDescripcion);
            Picasso.get().load(rImagen).into(ImagenPrendaInv);

            //CAMBIAR EL NOMBRE EN ACTIONBAR
            actionBar.setTitle("Actualizar");
            String actualizar = "Actualizar";
            //CAMBIAR EL NOMBRE DEL BOTÓN
            AgregarPrendaInv.setText(actualizar);
        }

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
                if(AgregarPrendaInv.getText().equals("Agregar Prenda")){
                    //METODO PARA SUBIRUNA IMAGEN
                    SubirImagen();
                    //Toast.makeText(AgregarInvierno.this,"Agregar",Toast.LENGTH_SHORT).show();
                }else {
                    EmpezarActualizacion();
                    //Toast.makeText(AgregarInvierno.this,"Actualizar",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void EmpezarActualizacion() {
        progressDialog.setTitle("Actualizando");
        progressDialog.setMessage("Espere Por Favor...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        EliminarImagenAnterior();
    }

    private void EliminarImagenAnterior() {
        StorageReference Imagen = getInstance().getReferenceFromUrl(rImagen);
        Imagen.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                //SI LA IMAGEN SE ELIMINO
                Toast.makeText(AgregarInvierno.this,"La imagen anterior a sido eliminada",Toast.LENGTH_SHORT).show();
                SubirNuevaImagen();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AgregarInvierno.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void SubirNuevaImagen() {
        String nuevaImagen = System.currentTimeMillis()+".png";
        StorageReference mStorageReference2 = mStorageReference.child(RutaDeAlmacenamiento+nuevaImagen);
        Bitmap bitmap = ((BitmapDrawable)ImagenPrendaInv.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
        byte [] data = byteArrayOutputStream.toByteArray();
        UploadTask uploadTask = mStorageReference2.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AgregarInvierno.this,"Nueva imagen cargada",Toast.LENGTH_SHORT).show();
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                Uri downloadUri = uriTask.getResult();
                ActualizarImagenBD(downloadUri.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AgregarInvierno.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void ActualizarImagenBD(final String NuevaImagen) {
        final String nombreActualizar = NombreInvierno.getText().toString();
        final String descripcionActualizar = DescripcionPrendaInv.getText().toString();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("INVIERNO");
        //CONSULTA
        Query query = databaseReference.orderByChild("nombre").equalTo(rNombre);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //DATOS A ACTUALIZAR
                for(DataSnapshot ds: snapshot.getChildren()){
                    ds.getRef().child("nombre").setValue(nombreActualizar);
                    ds.getRef().child("imagen").setValue(NuevaImagen);
                    ds.getRef().child("descripcion").setValue(descripcionActualizar);
                }
                progressDialog.dismiss();
                Toast.makeText(AgregarInvierno.this,"Actualizado Correctamente",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AgregarInvierno.this,InviernoA.class));
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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