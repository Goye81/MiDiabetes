package com.example.proyectofinal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;


public class AdaptadorLista extends RecyclerView.Adapter<AdaptadorLista.ViewHolder> {
    private FirebaseAuth mAuth;
    private ArrayList<String> imNombres;
    private ArrayList<Integer> imLi;
    private Context mContext;

    //Definimos el constructor
    public AdaptadorLista(Context context, ArrayList<String> imNom, ArrayList<Integer> liIm) {
        this.imNombres = imNom;
        this.imLi = liIm;
        this.mContext = context;
    }


    //Creamos una nueva vista invocando al layout "item_lista"
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_item, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    //Reemplazamos el contenido de la vista con el Bitmap
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Glide.with(mContext)
                .asBitmap()
                .load(imLi.get(position))
                .into(holder.imagen);
        holder.imNom.setText(imNombres.get(position));
        holder.parLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, imNombres.get(position), Toast.LENGTH_SHORT).show();
                int i = holder.getAdapterPosition();
                if (i == 0) {
                    Intent intent = new Intent(mContext, IngresarDatos.class);
                    intent.putExtra("index", i);
                    mContext.startActivity(intent);
                } else if (i == 1) {
                    Intent intent = new Intent(mContext, Hemoglobina.class);
                    intent.putExtra("index", i);
                    mContext.startActivity(intent);
                } else if (i == 2) {
                    Intent intent = new Intent(mContext, Historico.class);
                    intent.putExtra("index", i);
                    mContext.startActivity(intent);
                } else if (i == 3) {
                    Intent intent = new Intent(mContext, Consulta.class);
                    intent.putExtra("index", i);
                    mContext.startActivity(intent);
                } else if (i == 4) {
                    mAuth = FirebaseAuth.getInstance();
                    mAuth.signOut();
                    Intent intent = new Intent(mContext, MainActivity.class);
                    intent.putExtra("index", i);
                    mContext.startActivity(intent);
                    ( (Activity) mContext ).finish();
                }
            }
        });
    }

    //Retornamos el tamaño de nuestra lista
    @Override
    public int getItemCount() {
        return imNombres.size();
    }

    //Creamos la clase para referenciar los elementos de la lista
    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imagen;
        TextView imNom;
        RelativeLayout parLay;

        public ViewHolder(View itemView) {
            super(itemView);
            imagen = itemView.findViewById(R.id.imagen);
            imNom = itemView.findViewById(R.id.lista_datos);
            parLay = itemView.findViewById(R.id.parent_layout);
        }
    }
}
