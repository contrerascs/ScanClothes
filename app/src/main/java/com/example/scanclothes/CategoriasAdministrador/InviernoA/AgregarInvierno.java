package com.example.scanclothes.CategoriasAdministrador.InviernoA;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class AgregarInvierno extends AppCompatActivity {

    EditText NombreInvierno, DescripcionPrendaInv, LinkDePrendaInv, LinkDeModelo3DInv;
    TextView VistaInvierno, idInvierno;
    ImageView ImagenPrendaInv;
    Button AgregarPrendaInv;

    String RutaDeAlmacenamiento = "Invierno_Subida/";
    String RutaDeBaseDeDatos = "INVIERNO";
    Uri RutaArchivoUri;

    StorageReference mStorageReference;
    DatabaseReference DatabaseReference;
    FirebaseAuth firebaseAuth;

    ProgressDialog progressDialog;
    String rNombre,rImagen,rDescripcion,rId,rVista;

    private ArrayList<Uri> imagesUri;

    //int CODIGO_DE_SOLICITTUD_IMAGEN = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_invierno);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Agregar");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        imagesUri = new ArrayList<>();

        idInvierno = findViewById(R.id.idInvierno);
        NombreInvierno = findViewById(R.id.NombreInvierno);
        ImagenPrendaInv = findViewById(R.id.ImagenPrendaInv);
        DescripcionPrendaInv = findViewById(R.id.DescripcionPrendaInv);
        AgregarPrendaInv = findViewById(R.id.AgregarPrendaInv);
        LinkDePrendaInv = findViewById(R.id.LinkDePrendaInv);
        LinkDeModelo3DInv = findViewById(R.id.LinkDeModelo3DInv);
        VistaInvierno = findViewById(R.id.VistaInvierno);
        firebaseAuth = FirebaseAuth.getInstance();
        mStorageReference = FirebaseStorage.getInstance().getReference();
        DatabaseReference = FirebaseDatabase.getInstance().getReference(RutaDeBaseDeDatos);
        progressDialog = new ProgressDialog(AgregarInvierno.this);

        Bundle intent = getIntent().getExtras();
        if(intent!=null){
            //RECUPERAR LOS DATOS DE LA ACTIVIDAD ANTERIOR
            rNombre = intent.getString("NombreEnviado");
            rImagen = intent.getString("ImagenEnviada");
            rDescripcion = intent.getString("DescripcionEnviada");
            rId = intent.getString("IdEnviado");
            rVista = intent.getString("VistaEnviada");

            //SETEAR LOS DATOS EN LOS TEXTVIEW
            NombreInvierno.setText(rNombre);
            VistaInvierno.setText(rVista);
            DescripcionPrendaInv.setText(rDescripcion);
            idInvierno.setText(rId);
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
                //SDK 31
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                ObtenerImagenGaleria.launch(intent);
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
        Query query = databaseReference.orderByChild("id").equalTo(rId);
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
        String mNombre = NombreInvierno.getText().toString();
        String mDescripcion = DescripcionPrendaInv.getText().toString();
        String mLinkPrenda = LinkDePrendaInv.getText().toString();
        String mLinkModelo3D = LinkDeModelo3DInv.getText().toString();

        //VALIDAR QUE EL NOMBRE Y LA IMAGEN NO SEAN NULOS
        if (mNombre.equals("")||RutaArchivoUri==null||mDescripcion.equals("")||mLinkPrenda.equals("")||mLinkModelo3D.equals("")){
            Toast.makeText(AgregarInvierno.this,"Por favor complete todos los campos y agregue una imagen",Toast.LENGTH_SHORT).show();
        }else{
            progressDialog.setTitle("Espere por favor");
            progressDialog.setMessage("Subiendo Ropa De Invierno...");
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

                            String ID = new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss",
                                    Locale.getDefault()).format(System.currentTimeMillis());

                            idInvierno.setText(ID);
                            String mId = idInvierno.getText().toString();
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            String mVista = VistaInvierno.getText().toString();
                            int VISTA = Integer.parseInt(mVista);

                            Invierno invierno = new Invierno(mNombre, mDescripcion,downloadURI.toString(),user.getUid(),mLinkPrenda, mLinkModelo3D, mNombre+"/"+mId,VISTA);
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
    }

    //OBTENEMOS LA EXTENSION .JPG/.PNG
    private String ObtenerExtensionDelArchivo(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    //SDK 31
    //Obtener Imagen de la galeria
    private ActivityResultLauncher<Intent> ObtenerImagenGaleria = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        //OBTENER URI DE IMAGEN
                        RutaArchivoUri = data.getData();
                        ImagenPrendaInv.setImageURI(RutaArchivoUri);
                    }else{
                        Toast.makeText(AgregarInvierno.this,"Cancelado",Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

}