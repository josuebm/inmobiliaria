package com.example.josu.inmobiliaria;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;


public class Secundaria extends Activity {

    private ImageView iv;
    private int contador, posicion;

    private ArrayList<Inmueble> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contador = 0;
        setContentView(R.layout.activity_secundaria);
        lista = (ArrayList<Inmueble>)getIntent().getExtras().get("lista");
        posicion = (Integer)getIntent().getExtras().get("posicion");
        iv = (ImageView)findViewById(R.id.ivFoto);
        cargarImagenes();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Intent i = new Intent();
        i.putExtra("posicion", posicion);
        i.putExtra("contador", contador);
        setResult(RESULT_OK, i);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.secundaria, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public void cargarImagenes(){
        if(!lista.get(posicion).getFotos().isEmpty()){
            Bitmap imagen = BitmapFactory.decodeFile(lista.get(posicion).getFotos().get(contador));
            iv.setImageBitmap(imagen);
            contador++;
        }
    }

    public void cargarSiguiente(View v){
        contador++;
        if(lista.get(posicion).getFotos().size() == contador)
            contador = 0;
        if(lista.get(posicion).getFotos().size() > contador){
            Bitmap imagen = BitmapFactory.decodeFile(lista.get(posicion).getFotos().get(contador));
            iv.setImageBitmap(imagen);
        }
    }

    public void cargarAnterior(View v){
        contador--;
        if(contador < 0)
            contador = lista.get(posicion).getFotos().size() -1;
        if(lista.get(posicion).getFotos().size() > contador){
            Bitmap imagen = BitmapFactory.decodeFile(lista.get(posicion).getFotos().get(contador));
            iv.setImageBitmap(imagen);
        }
    }
}
