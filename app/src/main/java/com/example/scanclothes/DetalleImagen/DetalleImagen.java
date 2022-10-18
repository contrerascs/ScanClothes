package com.example.scanclothes.DetalleImagen;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.scanclothes.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DetalleImagen extends AppCompatActivity {

    ImageView ImagenDetalle;
    TextView NombreImagenDetalle, DescripcionImagen, VistaDetalle;
    Button VisualizarEnProbador,ObtenerPrenda;

    Dialog dialog;

    Bitmap bitmap;
    //SOLICITUD DE ALMACENAMIENTO
    private static final int CODIGO_DE_ALMACENAMIENTO = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_imagen);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Detalle");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        ImagenDetalle = findViewById(R.id.ImagenDetalle);
        NombreImagenDetalle = findViewById(R.id.NombreImagenDetalle);
        DescripcionImagen = findViewById(R.id.DescripcionImagen);
        VistaDetalle = findViewById(R.id.VistaDetalle);

        VisualizarEnProbador = findViewById(R.id.VisualizarEnProbador);
        ObtenerPrenda = findViewById(R.id.ObtenerPrenda);

        dialog = new Dialog(DetalleImagen.this);

        String Imagen = getIntent().getStringExtra("Imagen");
        String Nombre = getIntent().getStringExtra("Nombre");
        String Descripcion = getIntent().getStringExtra("Descripcion");
        String Link = getIntent().getStringExtra("Enlace");
        String Vistas = getIntent().getStringExtra("Vistas");

        try {
            //SI LA IMAGEN FUE TRAIDA
            Picasso.get().load(Imagen).placeholder(R.drawable.detalle_imagen).into(ImagenDetalle);
        }catch (Exception e){
            //SI LA IMAGEN NO FUE TRAIDA
            Picasso.get().load(R.drawable.detalle_imagen).into(ImagenDetalle);
        }

        NombreImagenDetalle.setText(Nombre);
        DescripcionImagen.setText(Descripcion);
        VistaDetalle.setText(Vistas);

        VisualizarEnProbador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DetalleImagen.this,"Probador Virtual",Toast.LENGTH_SHORT).show();
            }
        });

        ObtenerPrenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ObtenerPrenda(Link);
            }
        });
    }

    private void ObtenerPrenda(String Enlace) {
        //CAMBIO DE LETRA
        String ubicacion = "fuentes/CaviarDreams.ttf";
        Typeface tf = Typeface.createFromAsset(DetalleImagen.this.getAssets(),
                ubicacion);

        //DECLARANDO VISTAS
        TextView EnlacePrendaDialog, ObtenerPrendaTXT;
        Button CopiarEnlace,IrNavegador;

        //CONEXION CON EL CUADRO DE DIALOGO
        dialog.setContentView(R.layout.dialog_obtener_prenda);

        ObtenerPrendaTXT = dialog.findViewById(R.id.ObtenerPrendaTXT);
        EnlacePrendaDialog = dialog.findViewById(R.id.EnlacePrendaDialog);
        CopiarEnlace = dialog.findViewById(R.id.CopiarEnlace);
        IrNavegador = dialog.findViewById(R.id.IrNavegador);

        //CAMBIO DE FUENTE DE LETRA
        ObtenerPrendaTXT.setTypeface(tf);

        EnlacePrendaDialog.setText(Enlace);

        CopiarEnlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(DetalleImagen.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Enlace",EnlacePrendaDialog.getText().toString());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(DetalleImagen.this,"Enlace Copiado",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        IrNavegador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Enlace.substring(0,8).equals("https://")){
                    String nuevo_enlace = EnlacePrendaDialog.getText().toString();
                    Uri uri = Uri.parse(nuevo_enlace);
                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                    startActivity(intent);
                    dialog.dismiss();
                }else{
                    String nuevo_enlace = "https://"+EnlacePrendaDialog.getText().toString();
                    Uri uri = Uri.parse(nuevo_enlace);
                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                    startActivity(intent);
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    private void DescargarImagen(){
        //OBTENER MAPA DE BITS DE LA IMAGEN
        bitmap = ((BitmapDrawable)ImagenDetalle.getDrawable()).getBitmap();
        //OBTENER LA FECHA DE DESCARGA
        String FechaDescarga = new SimpleDateFormat("'Fecha Descarga: ' yyyy_MM_dd 'Hora: ' HH:mm:ss",
                Locale.getDefault()).format(System.currentTimeMillis());
        //DEFINIR LA RUTA DE ALMACENAMIENTO
        File ruta = Environment.getExternalStorageDirectory();
        File NombreCarpeta = new File(ruta+"/ScanClothes/");
        NombreCarpeta.mkdir();
        //DEFINIR NOMBRE DE LA IMAGEN DESCARGADA
        String ObtenerNombreImagen = NombreImagenDetalle.getText().toString();
        String NombreImagen = ObtenerNombreImagen+" "+FechaDescarga+".JPEG";
        File file = new File(NombreCarpeta,NombreImagen);
        OutputStream outputStream;
        try {
            outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
            outputStream.flush();
            outputStream.close();
            Toast.makeText(DetalleImagen.this,"La imagen se ha descargado",Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(DetalleImagen.this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==CODIGO_DE_ALMACENAMIENTO){
            //SI EL PERMISO FUE CONCEDIDO
            if (grantResults.length > 0 && !(grantResults[0] == PackageManager.PERMISSION_GRANTED)){
                DescargarImagen();
            }else{
                //SI EL PERMISO NO FUE CONCEDIDO
                Toast.makeText(DetalleImagen.this,"Active los permisos de almacenamiento",Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}