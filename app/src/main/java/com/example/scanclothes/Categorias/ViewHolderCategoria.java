package com.example.scanclothes.Categorias;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scanclothes.R;
import com.squareup.picasso.Picasso;

public class ViewHolderCategoria extends RecyclerView.ViewHolder {
    View mView;

    private ViewHolderCategoria.ClickListener mClickListener;

    public interface ClickListener{
        void OnIntemClick(View view, int position); //ADMIN PRESIONA NORMAL EL ITEM
    }

    public  void setOnClickListener(ViewHolderCategoria.ClickListener clickListener){
        mClickListener = clickListener;
    }

    public ViewHolderCategoria(@NonNull View itemView) {
        super(itemView);
        mView = itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.OnIntemClick(view,getBindingAdapterPosition());
            }
        });
    }

    public void SeteoCategoria(Context context, String categoria, String imagen){
        ImageView ImagenCategoriaD;
        TextView NombreCategoriaD;

        //CONEXION CON EL ITEM
        ImagenCategoriaD = mView.findViewById(R.id.ImagenCategoriaD);
        NombreCategoriaD = mView.findViewById(R.id.NombreCategoriaD);

        NombreCategoriaD.setText(categoria);

        //CONTROLAR POSIBLES ERRORES
        try{
            //SI LA IMAGEN FUE TRAIDA EXITOSAMENTE
            Picasso.get().load(imagen).placeholder(R.drawable.categoria).into(ImagenCategoriaD);
        }catch (Exception e){
            //SI LA IMAGEN NO FUE TRAIDA EXITOSAMENTE
            Picasso.get().load(R.drawable.categoria).into(ImagenCategoriaD);
        }
    }
}
