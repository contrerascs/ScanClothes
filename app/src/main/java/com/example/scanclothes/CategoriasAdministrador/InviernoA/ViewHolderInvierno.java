package com.example.scanclothes.CategoriasAdministrador.InviernoA;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scanclothes.R;
import com.squareup.picasso.Picasso;

public class ViewHolderInvierno extends RecyclerView.ViewHolder {
    View mView;

    private ViewHolderInvierno.ClickListener mClickListener;

    public interface ClickListener{
        void OnIntemClick(View view, int position); //ADMIN PRESIONA NORMAL EL ITEM
        void OnIntemLongClick(View view, int position); //ADMIN MANTIENE PRESIONADO EL ITEM
    }

    public  void setOnClickListener(ViewHolderInvierno.ClickListener clickListener){
        mClickListener = clickListener;
    }

    public ViewHolderInvierno(@NonNull View itemView) {
        super(itemView);
        mView = itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.OnIntemClick(view,getBindingAdapterPosition());
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mClickListener.OnIntemLongClick(view,getBindingAdapterPosition());
                return true;
            }
        });
    }

    public void SeteoInvierno(Context context, String nombre, int vista, String imagen, String descripcion, String id_administrador){
        ImageView ImagenInvierno;
        TextView NombreImagenInvierno;
        TextView VistaInviernos;
        TextView DescripcionInvierno;

        //CONEXION CON EL ITEM
        ImagenInvierno = mView.findViewById(R.id.ImagenInviernoItem);
        NombreImagenInvierno = mView.findViewById(R.id.NombreImagenInviernoItem);
        VistaInviernos = mView.findViewById(R.id.VistaInviernoItem);
        DescripcionInvierno = mView.findViewById(R.id.DescripcionInviernoItem);

        NombreImagenInvierno.setText(nombre);
        DescripcionInvierno.setText(descripcion);

        //CONVERTIR A STRING EL PARAMETRO VISTA
        String VistaString = String.valueOf(vista);
        VistaInviernos.setText(VistaString);

        //CONTROLAR POSIBLES ERRORES
        try{
            //SI LA IMAGEN FUE TRAIDA EXITOSAMENTE
            Picasso.get().load(imagen).placeholder(R.drawable.categoria).into(ImagenInvierno);
        }catch (Exception e){
            //SI LA IMAGEN NO FUE TRAIDA EXITOSAMENTE
            Picasso.get().load(R.drawable.categoria).into(ImagenInvierno);
        }
    }
}
