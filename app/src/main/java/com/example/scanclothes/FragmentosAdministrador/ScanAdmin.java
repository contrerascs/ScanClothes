package com.example.scanclothes.FragmentosAdministrador;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.example.scanclothes.R;

import java.util.ArrayList;

public class ScanAdmin extends Fragment {

    private ImageSwitcher imageFLR;
    private Button anteriorBTN;
    private Button siguienteBTN;
    private Button seleccionarBTN;

    private ArrayList<Uri> imagesUri;

    private int position;

    private ActivityResultLauncher<Intent> intentActivityResultLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scan_admin, container, false);

            imageFLR = view.findViewById(R.id.ImageFLR);
            anteriorBTN = view.findViewById(R.id.AnteriorBTN);
            siguienteBTN = view.findViewById(R.id.SiguienteBTN);
            seleccionarBTN = view.findViewById(R.id.SeleccionarBTN);

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