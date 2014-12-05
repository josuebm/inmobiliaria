package com.example.josu.inmobiliaria;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Josué on 04/12/2014.
 */
public class Adaptador extends ArrayAdapter {

    private Context contexto;
    private int recurso;
    private ArrayList<Inmueble> lista;
    private LayoutInflater inflador;

    public Adaptador (Context context, int resource, ArrayList<Inmueble> objects) {
        super(context, resource, objects);
        this.contexto = context;
        this.recurso = resource;
        this.lista = objects;
        inflador = (LayoutInflater)contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class ViewHolder{
        public TextView tvTipo, tvDireccion, tvLocalidad, tvPrecio;
        public ImageView iv;
        public int posicion;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if(convertView == null){
            convertView = inflador.inflate(recurso, null);
            vh = new ViewHolder();
            vh.tvTipo = (TextView)convertView.findViewById(R.id.tvTipo);
            vh.tvDireccion = (TextView)convertView.findViewById(R.id.tvDireccion);
            vh.tvLocalidad = (TextView)convertView.findViewById(R.id.tvLocalidad);
            vh.tvPrecio = (TextView)convertView.findViewById(R.id.tvPrecio);
            vh.iv = (ImageView)convertView.findViewById(R.id.iv);
            convertView.setTag(vh);
        }
        else
            vh = (ViewHolder)convertView.getTag();
        vh.tvTipo.setText(tipo(lista.get(position)));
        vh.tvDireccion.setText(lista.get(position).getDireccion());
        vh.tvLocalidad.setText(lista.get(position).getLocalidad());
        vh.tvPrecio.setText((int) (lista.get(position).getPrecio()) + " €");
        switch(lista.get(position).getHabitaciones()){
            case 0:{
                vh.iv.setImageResource(R.drawable.una);
            }break;
            case 1:{
                vh.iv.setImageResource(R.drawable.dos);
            }break;
            case 2:{
                vh.iv.setImageResource(R.drawable.tres);
            }break;
            case 3:{
                vh.iv.setImageResource(R.drawable.cuatro);
            }break;
            case 4:{
                vh.iv.setImageResource(R.drawable.cinco);
            }
        }
        vh.posicion = position;
        return convertView;
    }

    public String tipo (Inmueble inmueble){
        ArrayList <String> aux = new ArrayList (Arrays.asList(contexto.getResources().getStringArray(R.array.tipos)));
        return aux.get(inmueble.getTipo());
    }
}