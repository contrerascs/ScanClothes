package com.example.scanclothes.FragmentosAdministrador;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.example.scanclothes.R;
import com.example.scanclothes.RealityCapture.APIClientAutodesk;
import com.example.scanclothes.RealityCapture.APIInterfaceAutodesk;
import com.example.scanclothes.RealityCapture.Capture;
import com.example.scanclothes.RealityCapture.ReqBody;
import com.example.scanclothes.RealityCapture.ReqBodyID;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanAdmin extends Fragment {

    private ImageSwitcher imageFLR;
    private Button anteriorBTN;
    private Button siguienteBTN;
    private Button seleccionarBTN;
    private Button crearModelo3D;
    private Button avanzarBTM;

    private ArrayList<Uri> imagesUri;

    private int position;
    private String photoID;
    private int PhotosceneProgress = 100;
    private String comparativo;

    private ActivityResultLauncher<Intent> intentActivityResultLauncher;

    Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scan_admin, container, false);

            imageFLR = view.findViewById(R.id.ImageFLR);
            anteriorBTN = view.findViewById(R.id.AnteriorBTN);
            siguienteBTN = view.findViewById(R.id.SiguienteBTN);
            seleccionarBTN = view.findViewById(R.id.SeleccionarBTN);
            crearModelo3D = view.findViewById(R.id.CrearModelo3D);


        dialog = new Dialog(getContext());

        imagesUri = new ArrayList<>();

        setUpImagesSwitcher();

        activityResult();

        onClicksHandler();

        return view;
    }

    private void seleccionarImagenesIntent(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);

        intentActivityResultLauncher.launch(Intent.createChooser(intent, "Select Image(s"));
    }

    private void onClicksHandler(){
        anteriorBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position>0){
                    position--;
                    imageFLR.setImageURI(imagesUri.get(position));
                }else{
                    Toast.makeText(getActivity(),"No previous images", Toast.LENGTH_SHORT).show();
                }
            }
        });

        siguienteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position < imagesUri.size() - 1){
                    position++;
                    imageFLR.setImageURI(imagesUri.get(position));
                }else{
                    Toast.makeText(getActivity(),"No more images", Toast.LENGTH_SHORT).show();
                }
            }
        });

        seleccionarBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seleccionarImagenesIntent();
            }
        });

        crearModelo3D.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Authenticar();
            }
        });
    }

    private void Authenticar(){
        TextView respuesta;
        APIInterfaceAutodesk apiInterfaceAutodesk;
        dialog.setContentView(R.layout.dialog_crear_modelo_3d);
        avanzarBTM = dialog.findViewById(R.id.AvanzarBTN);

        respuesta = (TextView) dialog.findViewById(R.id.Respuesta);
        apiInterfaceAutodesk = APIClientAutodesk.getClient().create(APIInterfaceAutodesk.class);

        Call<Capture> call = apiInterfaceAutodesk.getOauth();
        call.enqueue(new Callback<Capture>() {
            @Override
            public void onResponse(Call<Capture> call, Response<Capture> response) {
                if(!response.isSuccessful()){
                    respuesta.setText("Codigo: "+response.code());
                    return;
                }
                Capture capture = response.body();
                respuesta.setText(capture.getRespuesta());
            }

            @Override
            public void onFailure(Call<Capture> call, Throwable t) {
                respuesta.setText(t.getMessage());
            }
        });

        avanzarBTM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AgregarPhotoscene();
            }
        });

        dialog.show();
    }

    private void AgregarPhotoscene() {
        TextView respuesta;
        APIInterfaceAutodesk apiInterfaceAutodesk;
        dialog.setContentView(R.layout.dialog_crear_modelo_3d);
        avanzarBTM = dialog.findViewById(R.id.AvanzarBTN);

        respuesta = (TextView) dialog.findViewById(R.id.Respuesta);
        apiInterfaceAutodesk = APIClientAutodesk.getClient().create(APIInterfaceAutodesk.class);

        Call<Capture> call = apiInterfaceAutodesk.getPhotosceneAdd();
        call.enqueue(new Callback<Capture>() {
            @Override
            public void onResponse(Call<Capture> call, Response<Capture> response) {
                if(!response.isSuccessful()){
                    respuesta.setText("Codigo: "+response.code());
                    return;
                }
                Capture capture = response.body();
                respuesta.setText(capture.getRespuesta());
                photoID = capture.getPhotosceneID();
            }

            @Override
            public void onFailure(Call<Capture> call, Throwable t) {
                respuesta.setText(t.getMessage());
            }
        });

        avanzarBTM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPhotosceneID(photoID);
            }
        });

        dialog.show();
    }

    private void getPhotosceneID(String ID){
        TextView respuesta;
        APIInterfaceAutodesk apiInterfaceAutodesk;
        dialog.setContentView(R.layout.dialog_crear_modelo_3d);
        avanzarBTM = dialog.findViewById(R.id.AvanzarBTN);
        ReqBodyID reqBodyID = new ReqBodyID(ID);
        respuesta = (TextView) dialog.findViewById(R.id.Respuesta);
        apiInterfaceAutodesk = APIClientAutodesk.getClient().create(APIInterfaceAutodesk.class);
        Call <ReqBody> call = apiInterfaceAutodesk.getPhotosceneID(reqBodyID);
        call.enqueue(new Callback<ReqBody>() {
            @Override
            public void onResponse(Call<ReqBody> call, Response<ReqBody> response) {
                if(!response.isSuccessful()){
                    respuesta.setText("Codigo: "+response.code());
                    return;
                }
                ReqBody capture = response.body();
                respuesta.setText(capture.getRespuesta());
            }

            @Override
            public void onFailure(Call<ReqBody> call, Throwable t) {
                respuesta.setText(t.getMessage());
            }
        });

        avanzarBTM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getProgress(photoID);
            }
        });

        dialog.show();
    }

    private void getProgress(String ID) {
        TextView respuesta;
        APIInterfaceAutodesk apiInterfaceAutodesk;
        dialog.setContentView(R.layout.dialog_crear_modelo_3d);
        avanzarBTM = dialog.findViewById(R.id.AvanzarBTN);
        ReqBodyID reqBodyID = new ReqBodyID(ID);
        respuesta = (TextView) dialog.findViewById(R.id.Respuesta);
        apiInterfaceAutodesk = APIClientAutodesk.getClient().create(APIInterfaceAutodesk.class);
        Call <ReqBody> call = apiInterfaceAutodesk.getProcess(reqBodyID);
        call.enqueue(new Callback<ReqBody>() {
            @Override
            public void onResponse(Call<ReqBody> call, Response<ReqBody> response) {
                if(!response.isSuccessful()){
                    respuesta.setText("Codigo: "+response.code());
                    return;
                }
                ReqBody capture = response.body();
                respuesta.setText(capture.getRespuesta());
            }

            @Override
            public void onFailure(Call<ReqBody> call, Throwable t) {
                respuesta.setText(t.getMessage());
            }
        });

        avanzarBTM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkProcess(photoID);
            }
        });

        dialog.show();
    }

    private void checkProcess(String ID) {
        TextView respuesta;
        APIInterfaceAutodesk apiInterfaceAutodesk;
        dialog.setContentView(R.layout.dialog_crear_modelo_3d);
        avanzarBTM = dialog.findViewById(R.id.AvanzarBTN);
        ReqBodyID reqBodyID = new ReqBodyID(ID);
        respuesta = (TextView) dialog.findViewById(R.id.Respuesta);
        apiInterfaceAutodesk = APIClientAutodesk.getClient().create(APIInterfaceAutodesk.class);
        Call <ReqBody> call = apiInterfaceAutodesk.getCheckProgress(reqBodyID);
        call.enqueue(new Callback<ReqBody>() {
            @Override
            public void onResponse(Call<ReqBody> call, Response<ReqBody> response) {
                if(!response.isSuccessful()){
                    respuesta.setText("Codigo: "+response.code());
                    return;
                }
                ReqBody capture = response.body();
                respuesta.setText("Progress: "+capture.getProgress()+"%");
                ReqBody capture2 = response.body();
                comparativo = capture2.getProgress();
                //Toast.makeText(getContext(),""+PhotosceneProgress,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ReqBody> call, Throwable t) {
                respuesta.setText(t.getMessage());
            }
        });

        avanzarBTM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(),""+PhotosceneProgress,Toast.LENGTH_SHORT).show();
                if(PhotosceneProgress == Integer.parseInt(comparativo)){
                    Toast.makeText(getContext(),"TERMINADO",Toast.LENGTH_SHORT).show();
                    obtenerModelo3D(photoID);
                }else{
                    checkProcess(photoID);
                }
            }
        });

        dialog.show();
    }

    private void obtenerModelo3D(String photoID) {
        TextView respuesta;
        APIInterfaceAutodesk apiInterfaceAutodesk;
        dialog.setContentView(R.layout.dialog_crear_modelo_3d);
        avanzarBTM = dialog.findViewById(R.id.AvanzarBTN);
        avanzarBTM.setText("DESCARGAR MODELO 3D");
        ReqBodyID reqBodyID = new ReqBodyID(photoID);
        respuesta = (TextView) dialog.findViewById(R.id.Respuesta);
        apiInterfaceAutodesk = APIClientAutodesk.getClient().create(APIInterfaceAutodesk.class);
        Call <ReqBody> call = apiInterfaceAutodesk.getResult(reqBodyID);
        call.enqueue(new Callback<ReqBody>() {
            @Override
            public void onResponse(Call<ReqBody> call, Response<ReqBody> response) {
                if(!response.isSuccessful()){
                    respuesta.setText("Codigo: "+response.code());
                    return;
                }
                ReqBody capture = response.body();
                respuesta.setText(capture.getRespuesta());
            }

            @Override
            public void onFailure(Call<ReqBody> call, Throwable t) {
                respuesta.setText(t.getMessage());
            }
        });

        avanzarBTM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nuevo_enlace = respuesta.getText().toString();
                Uri uri = Uri.parse(nuevo_enlace);
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void setUpImagesSwitcher(){
        imageFLR.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                return new ImageView(getActivity());
            }
        });
    }

    private void activityResult(){
        intentActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode()== Activity.RESULT_OK){
                        Uri imageUri;
                        if(result.getData().getClipData() != null){
                            //SELECCIONAR MULTIPLES IMAGENES
                            int count = result.getData().getClipData().getItemCount();
                            for(int i=0; i<count;i++) {
                                imageUri = result.getData().getClipData().getItemAt(i).getUri();
                                imagesUri.add(imageUri);
                            }
                        }else{
                            //SELECCIONAR UNA IMAGEN
                            imageUri = result.getData().getData();
                            imagesUri.add(imageUri);
                        }
                        //ESTABLECER LA PRIMERA IMAGENE EN EL IMAGE SWITCHER
                        imageFLR.setImageURI(imagesUri.get(0));
                        position = 0;
                    }
                });
    }


}