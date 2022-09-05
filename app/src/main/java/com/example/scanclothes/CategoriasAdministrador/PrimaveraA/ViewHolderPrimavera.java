package com.example.scanclothes.CategoriasAdministrador.PrimaveraA;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scanclothes.CategoriasAdministrador.OtonoA.ViewHolderOtono;
import com.example.scanclothes.R;
import com.squareup.picasso.Picasso;

public class ViewHolderPrimavera extends RecyclerView.ViewHolder {
    View mView;

    private ViewHolderOtono.ClickListener mClickListener;

    public interface ClickListener{
        void OnIntemClick(View view, int position); //ADMIN PRESIONA NORMAL EL ITEM
        void OnIntemLongClick(View view, int position); //ADMIN MANTIENE PRESIONADO EL ITEM
    }

    public  void setOnClickListener(ViewHolderOtono.ClickListener clickListener){
        mClickListener = clickListener;
    }

    public ViewHolderPrimavera(@NonNull View itemView) {
        super(itemView);
        mView = itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.OnIntemClick(view, getAdapterPosition());
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mClickListener.OnIntemClick(view, getAdapterPosition());
                return true;
            }
        });

    }

    public void SeteoPrimavera(Context context, String nombre, int vista, String imagen, String descripcion){
        ImageView ImagenPrimavera;
        TextView NombreImagenPrimavera;
        TextView VistaPrimavera;
        TextView DescripcionPrimavera;

        //CONEXION CON EL ITEM
        ImagenPrimavera = mView.findViewById(R.id.ImagenPrimaveraItem);
        NombreImagenPrimavera = mView.findViewById(R.id.NombreImagenPrimaveraItem);
        VistaPrimavera = mView.findViewById(R.id.VistaPrimaveraItem);
        DescripcionPrimavera = mView.findViewById(R.id.DescripcionPrimaveraItem);

        NombreImagenPrimavera.setText(nombre);

        DescripcionPrimavera.setText(descripcion);

        //CONVERTIR A STRING EL PARAMETRO VISTA
        String VistaString = String.valueOf(vista);

        VistaPrimavera.setText(VistaString);

        //CONTROLAR POSIBLES ERRORES
        try{
            //SI LA IMAGEN FUE TRAIDA EXITOSAMENTE
            Picasso.get().load(imagen).into(ImagenPrimavera);
        }catch (Exception e){
            //SI LA IMAGEN NO FUE TRAIDA EXITOSAMENTE
            Toast.makeText(context, ""+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
}
