package com.example.scanclothes.CategoriasAdministrador.OtonoA;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scanclothes.R;
import com.squareup.picasso.Picasso;

public class ViewHolderOtono extends RecyclerView.ViewHolder {
    View mView;

    private ViewHolderOtono.ClickListener mClickListener;

    public interface ClickListener{
        void OnIntemClick(View view, int position); //ADMIN PRESIONA NORMAL EL ITEM
        void OnIntemLongClick(View view, int position); //ADMIN MANTIENE PRESIONADO EL ITEM
    }

    public  void setOnClickListener(ViewHolderOtono.ClickListener clickListener){
        mClickListener = clickListener;
    }

    public ViewHolderOtono(@NonNull View itemView) {
        super(itemView);
        mView = itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.OnIntemClick(view,getAdapterPosition());
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mClickListener.OnIntemClick(view,getAdapterPosition());
                return true;
            }
        });
    }

    public void SeteoOtono(Context context, String nombre, int vista, String imagen, String descripcion){
        ImageView ImagenOtono;
        TextView NombreImagenOtono;
        TextView VistaOtono;
        TextView DescripcionOtono;

        //CONEXION CON EL ITEM
        ImagenOtono = mView.findViewById(R.id.ImagenOtoñoItem);
        NombreImagenOtono = mView.findViewById(R.id.NombreImagenOtoñoItem);
        VistaOtono = mView.findViewById(R.id.VistaOtoñoItem);
        DescripcionOtono = mView.findViewById(R.id.DescripcionOtoñoItem);

        NombreImagenOtono.setText(nombre);

        DescripcionOtono.setText(descripcion);

        //CONVERTIR A STRING EL PARAMETRO VISTA
        String VistaString = String.valueOf(vista);

        VistaOtono.setText(VistaString);

        //CONTROLAR POSIBLES ERRORES
        try{
            //SI LA IMAGEN FUE TRAIDA EXITOSAMENTE
            Picasso.get().load(imagen).into(ImagenOtono);
        }catch (Exception e){
            //SI LA IMAGEN NO FUE TRAIDA EXITOSAMENTE
            Toast.makeText(context, ""+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
}
