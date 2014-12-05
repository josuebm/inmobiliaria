package com.example.josu.inmobiliaria;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;


public class Anadir extends Activity {

    private ArrayList<Inmueble> lista;
    private EditText etLocalidad, etDireccion, etPrecio;
    private Spinner spHabitaciones, spTipo;
    private Button boton;
    private String accion;
    private int posicion;
    private int numFotos;
    private TextView tvFotos;
    private ArrayList<String> fotos;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir);

        fotos = new ArrayList<String>();

        accion = (String)getIntent().getExtras().get("accion");

        lista = (ArrayList<Inmueble>)getIntent().getExtras().get("lista");
        etLocalidad = (EditText)findViewById(R.id.etLocalidad);
        etDireccion = (EditText)findViewById(R.id.etDireccion);
        etPrecio = (EditText)findViewById(R.id.etPrecio);
        spHabitaciones = (Spinner)findViewById(R.id.spHabitaciones);
        ArrayAdapter<CharSequence> adapterHabitaciones = ArrayAdapter.createFromResource(this, R.array.habitaciones, android.R.layout.simple_spinner_item);
        adapterHabitaciones.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spHabitaciones.setAdapter(adapterHabitaciones);

        spTipo = (Spinner)findViewById(R.id.spTipo);
        ArrayAdapter<CharSequence> adapterTipos = ArrayAdapter.createFromResource(this, R.array.tipos, android.R.layout.simple_spinner_item);
        adapterTipos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipo.setAdapter(adapterTipos);

        boton = (Button)findViewById(R.id.btAnadir);

        numFotos = 0;
        tvFotos = (TextView)findViewById(R.id.tvFoto);
        tvFotos.setVisibility(View.INVISIBLE);

        if(accion.equals("editar")){
            posicion = (Integer)getIntent().getExtras().get("posicion");

            if(!lista.get(posicion).getFotos().isEmpty())
                fotos = lista.get(posicion).getFotos();

            tostada(lista.get(posicion).getFotos().size()+"");
            etLocalidad.setText(lista.get(posicion).getLocalidad());
            etDireccion.setText(lista.get(posicion).getDireccion());
            etPrecio.setText(lista.get(posicion).getPrecio() + " €");
            spHabitaciones.setSelection(lista.get(posicion).getHabitaciones());
            spTipo.setSelection(lista.get(posicion).getTipo());
            boton.setText("Editar");
            id = lista.get(posicion).getId();
        }
        else{
            if(lista.size() == 0)
                id = 0;
            else
                id = lista.get(lista.size()-1).getId()+1;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.anadir, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_camara) {
            camara();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void anadir(View v){
        Collections.sort(lista);
        if(!accion.equals("editar"))
            lista.add(new Inmueble(id, etLocalidad.getText().toString(), etDireccion.getText().toString(), spTipo.getSelectedItemPosition(), spHabitaciones.getSelectedItemPosition(), Float.valueOf(etPrecio.getText().toString()), fotos));

        else{
            lista.get(posicion).setLocalidad(etLocalidad.getText().toString());
            lista.get(posicion).setDireccion(etDireccion.getText().toString());
            lista.get(posicion).setTipo(spTipo.getSelectedItemPosition());
            lista.get(posicion).setHabitaciones(spHabitaciones.getSelectedItemPosition());
            if(etPrecio.getText().toString().endsWith("€"))
                etPrecio.setText(etPrecio.getText().toString().replace("€", ""));
            lista.get(posicion).setPrecio(Float.valueOf(etPrecio.getText().toString()));
            lista.get(posicion).setFotos(fotos);
        }
        new Escribir(lista, this).escribir();
        Intent i = new Intent();
        i.putParcelableArrayListExtra("lista", lista);

        if(!accion.equals("editar"))
            setResult(RESULT_OK, i);
        else
            setResult(RESULT_OK, i);

        finish();
    }

    public void tostada(String mensaje){
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }

    private static final int IDACTIVIDADFOTO = 1;

    public void camara(){
        Intent i = new Intent ("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(i, IDACTIVIDADFOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IDACTIVIDADFOTO && resultCode == RESULT_OK){
            Bitmap foto = data.getParcelableExtra("data");
            try {
                guardarFoto(foto);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void guardarFoto(Bitmap foto) throws IOException {
        FileOutputStream fos = null;
        String nombre = nombreFoto();
        File archivo = null;
        if(espacioSuficiente(getExternalFilesDir(Environment.DIRECTORY_DCIM))){
            archivo = new File(getExternalFilesDir(Environment.DIRECTORY_DCIM), nombre);
            fos = new FileOutputStream(archivo);
            fos.flush();
            foto.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            numFotos++;
            if (tvFotos.getVisibility() == View.INVISIBLE)
                tvFotos.setVisibility(View.VISIBLE);
            tvFotos.setText(numFotos + " foto(s) añadidas");
            fotos.add(archivo.getPath());
        }
    }

    public boolean espacioSuficiente(File f) {
        double eTotal, eDisponible, porcentaje;
        eTotal = (double) f.getTotalSpace();
        eDisponible = (double) f.getFreeSpace();
        porcentaje = (eDisponible / eTotal) * 100;
        return porcentaje > 10;
    }

    public String nombreFoto(){
        GregorianCalendar fecha = new GregorianCalendar();
        String nombre;
        String year = String.valueOf(fecha.get(Calendar.YEAR));
        String mes = String.valueOf(fecha.get(Calendar.MONTH));
        if(mes.length()==1)
            mes = "0" + mes;
        String dia = String.valueOf(fecha.get(Calendar.DAY_OF_MONTH));
        if(dia.length() == 1)
            dia = "0" + dia;
        String hora = String.valueOf(fecha.get(Calendar.HOUR_OF_DAY));
        if(hora.length() == 1)
            hora = "0" + hora;
        String minuto = String.valueOf(fecha.get(Calendar.MINUTE));
        if(minuto.length() == 1)
            minuto = "0" + minuto;
        String segundo = String.valueOf(fecha.get(Calendar.SECOND));
        if(segundo.length() == 1)
            segundo = "0" + segundo;
        nombre = "Inmueble_" + id + "_" + year + "_" + mes + "_" + dia + "_" + hora + "_" + minuto + "_" + segundo + ".jpg";
        return nombre;

    }
}
