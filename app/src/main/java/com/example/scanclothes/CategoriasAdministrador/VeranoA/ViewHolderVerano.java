package com.example.scanclothes.CategoriasAdministrador.VeranoA;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scanclothes.R;
import com.squareup.picasso.Picasso;

public class ViewHolderVerano extends RecyclerView.ViewHolder {
    View mView;

    private ViewHolderVerano.ClickListener mClickListener;

    public interface ClickListener{
        void OnIntemClick(View view, int position); //ADMIN PRESIONA NORMAL EL ITEM
        void OnIntemLongClick(View view, int position); //ADMIN MANTIENE PRESIONADO EL ITEM
    }

    public  void setOnClickListener(ViewHolderVerano.ClickListener clickListener){
        mClickListener = clickListener;
    }

    public ViewHolderVerano(@NonNull View itemView) {
        super(itemView);
        mView = itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.OnIntemClick(view, getBindingAdapterPosition());
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mClickListener.OnIntemLongClick(view, getBindingAdapterPosition());
                return true;
            }
        });

    }

    public void SeteoVerano(Context context, String nombre, int vista, String imagen, String descripcion, String id_administrador){
        ImageView ImagenVerano;
        TextView NombreImagenVerano;
        TextView VistaVerano;
        TextView DescripcionVerano;

        //CONEXION CON EL ITEM
        ImagenVerano = mView.findViewById(R.id.ImagenVeranoItem);
        NombreImagenVerano = mView.findViewById(R.id.NombreImagenVeranoItem);
        VistaVerano = mView.findViewById(R.id.VistaVeranoItem);
        DescripcionVerano = mView.findViewById(R.id.DescripcionVeranoItem);

        NombreImagenVerano.setText(nombre);

        DescripcionVerano.setText(descripcion);

        //CONVERTIR A STRING EL PARAMETRO VISTA
        String VistaString = String.valueOf(vista);

        VistaVerano.setText(VistaString);

        //CONTROLAR POSIBLES ERRORES
        try{
            //SI LA IMAGEN FUE TRAIDA EXITOSAMENTE
            Picasso.get().load(imagen).placeholder(R.drawable.categoria).into(ImagenVerano);
        }catch (Exception e){
            //SI LA IMAGEN NO FUE TRAIDA EXITOSAMENTE
            Picasso.get().load(R.drawable.categoria).into(ImagenVerano);
        }
    }
}
