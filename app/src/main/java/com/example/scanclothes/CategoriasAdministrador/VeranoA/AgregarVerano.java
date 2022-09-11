package com.example.scanclothes.CategoriasAdministrador.VeranoA;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.scanclothes.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class AgregarVerano extends AppCompatActivity {
    EditText NombreVerano, DescripcionPrendaVer;
    TextView VistaVerano;
    ImageView ImagenPrendaVer;
    Button AgregarPrendaVer;

    String RutaDeAlmacenamiento = "Verano_Subida/";
    String RutaDeBaseDeDatos = "VERANO";
    Uri RutaArchivoUri;

    StorageReference mStorageReference;
    com.google.firebase.database.DatabaseReference DatabaseReference;

    ProgressDialog progressDialog;
    String rNombre,rImagen,rDescripcion,rVista;
    //int CODIGO_DE_SOLICITTUD_IMAGEN = 5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_verano);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Agregar");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        VistaVerano = findViewById(R.id.VistaVerano);
        NombreVerano = findViewById(R.id.NombreVerano);
        ImagenPrendaVer = findViewById(R.id.ImagenPrendaVer);
        DescripcionPrendaVer = findViewById(R.id.DescripcionPrendaVer);
        AgregarPrendaVer = findViewById(R.id.AgregarPrendaVer);

        mStorageReference = FirebaseStorage.getInstance().getReference();
        DatabaseReference = FirebaseDatabase.getInstance().getReference(RutaDeBaseDeDatos);
        progressDialog = new ProgressDialog(AgregarVerano.this);

        Bundle intent = getIntent().getExtras();
        if(intent!=null){
            //RECUPERAR LOS DATOS DE LA ACTIVIDAD ANTERIOR
            rNombre = intent.getString("NombreEnviado");
            rImagen = intent.getString("ImagenEnviada");
            rDescripcion = intent.getString("DescripcionEnviada");
            rVista = intent.getString("VistaEnviada");

            //SETEAR LOS DATOS EN LOS TEXTVIEW
            NombreVerano.setText(rNombre);
            VistaVerano.setText(rVista);
            DescripcionPrendaVer.setText(rDescripcion);
            Picasso.get().load(rImagen).into(ImagenPrendaVer);

            //CAMBIAR EL NOMBRE EN ACTIONBAR
            actionBar.setTitle("Actualizar");
            String actualizar = "Actualizar";
            //CAMBIAR EL NOMBRE DEL BOTÓN
            AgregarPrendaVer.setText(actualizar);
        }

        ImagenPrendaVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //SDK 30
                //Intent intent = new Intent();
                //intent.setType("image/*");
                //intent.setAction(Intent.ACTION_GET_CONTENT);
                //startActivityForResult(Intent.createChooser(intent,"Seleccionar imagen"),CODIGO_DE_SOLICITTUD_IMAGEN);
                //SDK 31
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                ObtenerImagenGaleria.launch(intent);
            }
        });

        AgregarPrendaVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(AgregarPrendaVer.getText().equals("Agregar Prenda")){
                    //METODO PARA SUBIRUNA IMAGEN
                    SubirImagen();
                }else {
                    EmpezarActualizacion();
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
                Toast.makeText(AgregarVerano.this,"La imagen anterior a sido eliminada",Toast.LENGTH_SHORT).show();
                SubirNuevaImagen();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AgregarVerano.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void SubirNuevaImagen() {
        String nuevaImagen = System.currentTimeMillis()+".png";
        StorageReference mStorageReference2 = mStorageReference.child(RutaDeAlmacenamiento+nuevaImagen);
        Bitmap bitmap = ((BitmapDrawable)ImagenPrendaVer.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
        byte [] data = byteArrayOutputStream.toByteArray();
        UploadTask uploadTask = mStorageReference2.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AgregarVerano.this,"Nueva imagen cargada",Toast.LENGTH_SHORT).show();
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                Uri downloadUri = uriTask.getResult();
                ActualizarImagenBD(downloadUri.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AgregarVerano.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void ActualizarImagenBD(final String NuevaImagen) {
        final String nombreActualizar = NombreVerano.getText().toString();
        final String descripcionActualizar = DescripcionPrendaVer.getText().toString();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        com.google.firebase.database.DatabaseReference databaseReference = firebaseDatabase.getReference("INVIERNO");
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
                Toast.makeText(AgregarVerano.this,"Actualizado Correctamente",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AgregarVerano.this, VeranoA.class));
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void SubirImagen() {
        String mNombre = NombreVerano.getText().toString();

        //VALIDAR QUE EL NOMBRE Y LA IMAGEN NO SEAN NULOS
        if (mNombre.equals("")||RutaArchivoUri==null){
            Toast.makeText(AgregarVerano.this,"Asigne un nombre o una imagen",Toast.LENGTH_SHORT).show();
        }else{
            progressDialog.setTitle("Espere por favor");
            progressDialog.setMessage("Subiendo Imagen OTONO...");
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

                            String mDescripcion = DescripcionPrendaVer.getText().toString();
                            String mVista = VistaVerano.getText().toString();
                            int VISTA = Integer.parseInt(mVista);

                            Verano verano = new Verano(mNombre, mDescripcion,downloadURI.toString(),VISTA);
                            String ID_IMAGEN = DatabaseReference.push().getKey();

                            DatabaseReference.child(ID_IMAGEN).setValue(verano);

                            progressDialog.dismiss();
                            Toast.makeText(AgregarVerano.this,"Subido Exitosamente", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(AgregarVerano.this, VeranoA.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AgregarVerano.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onPaused(@NonNull UploadTask.TaskSnapshot snapshot) {
                    progressDialog.setTitle("Agregada");
                    progressDialog.setCancelable(false);
                }
            });
        }
    }

    //OBTENEMOS LA EXTENSION .JPG/.PNG
    private String ObtenerExtensionDelArchivo(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    //COMPROBAR SI LA IMAGEN SELECCIONADA POR EL ADMINISTRADOR FUE CORRECTA
    /*@Override
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
                ImagenPrendaVer.setImageBitmap(bitmap);
            }catch (Exception e){
                Toast.makeText(this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    }*/

    //SDK 31
    //Obtener Imagen de la galeria
    private ActivityResultLauncher<Intent> ObtenerImagenGaleria = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        RutaArchivoUri = data.getData();
                        ImagenPrendaVer.setImageURI(RutaArchivoUri);
                    }else{
                        Toast.makeText(AgregarVerano.this,"Cancelado",Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );
}