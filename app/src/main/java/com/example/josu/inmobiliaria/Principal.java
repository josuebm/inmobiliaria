package com.example.josu.inmobiliaria;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Xml;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;


public class Principal extends Activity {

    private ListView listaInmuebles;
    private ArrayList<Inmueble> lista;
    private Adaptador ad;
    private final int SECUNDARIA = 2;
    private final int ANADIR = 3;
    private final int EDITAR = 4;
    private ImageView ivFoto;
    int posicion, contador;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        listaInmuebles = (ListView) findViewById(R.id.listaInmuebles);
        ivFoto = (ImageView)findViewById(R.id.ivFoto);
        try {
            leer();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }


        ad = new Adaptador(this, R.layout.detalle, lista);
        listaInmuebles.setAdapter(ad);

        final Context contexto = this;
        final DetalleListaInmuebles fragmentoDetalle = (DetalleListaInmuebles) getFragmentManager().findFragmentById(R.id.fragment4);
        final boolean horizontal = fragmentoDetalle != null && fragmentoDetalle.isInLayout();


        listaInmuebles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(horizontal){
                    posicion = position;
                    contador = 0;
                    cargarImagenes();
                }

                else{
                    Intent intent = new Intent(contexto, Secundaria.class);
                    intent.putParcelableArrayListExtra("lista", lista);
                    intent.putExtra("posicion", position);
                    startActivityForResult(intent, SECUNDARIA);
                }
            }
        });
        registerForContextMenu(listaInmuebles);
    }

    @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if(requestCode == ANADIR || requestCode == EDITAR && resultCode == RESULT_OK){
                lista = (ArrayList<Inmueble>)data.getExtras().get("lista");
                ad = new Adaptador(this, R.layout.detalle, lista);
                listaInmuebles.setAdapter(ad);
            }
            else
                if (requestCode == SECUNDARIA && resultCode == RESULT_OK) {

                    posicion = (Integer)data.getExtras().get("posicion");
                    contador = (Integer) data.getExtras().get("contador");
                    final DetalleListaInmuebles fragmentoDetalle = (DetalleListaInmuebles) getFragmentManager().findFragmentById(R.id.fragment4);
                    cargarImagenes();
                }
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_anadir) {
            Intent intent = new Intent(getApplicationContext(), Anadir.class);
            intent.putParcelableArrayListExtra("lista", lista);
            intent.putExtra("accion", "anadir");
            startActivityForResult(intent, ANADIR);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.longclick_principal, menu);
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id=item.getItemId();
        AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int index= info.position;
        Object o= info.targetView.getTag();
        if (id == R.id.action_eliminar) {
            lista.remove(index);
            ad.notifyDataSetChanged();
            new Escribir(lista, this).escribir();
            return true;
        }else if (id == R.id.action_editar) {
            Intent intent = new Intent(getApplicationContext(), Anadir.class);
            intent.putParcelableArrayListExtra("lista", lista);
            intent.putExtra("posicion", index);
            intent.putExtra("accion", "editar");
            startActivityForResult(intent, EDITAR);
            return true;
        }
        return super.onContextItemSelected(item);
    }

    public void tostada(String mensaje){
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }

    public void leer() throws IOException, XmlPullParserException {
        lista = new ArrayList<Inmueble>();
        ArrayList <String> fotos = new ArrayList<String>();
        Inmueble inmuebleActual = new Inmueble();
        XmlPullParser lectorxml = Xml.newPullParser();
        String etiqueta = "";

        String atributo1 = "", atributo2 = "", atributo3 = "", atributo4 = "", atributo5 = "", atributo6 = "";
        lectorxml.setInput(new FileInputStream(new File(getExternalFilesDir(null), "archivo.xml")), "utf-8");
        int evento = lectorxml.getEventType();
        while (evento != XmlPullParser.END_DOCUMENT) {
            if (evento == XmlPullParser.START_TAG) {
                etiqueta = lectorxml.getName();
                if (etiqueta.compareTo("inmueble") == 0) {
                    atributo1 = lectorxml.getAttributeValue(null, "id");
                    atributo2 = lectorxml.getAttributeValue(null, "localidad");
                    atributo3 = lectorxml.getAttributeValue(null, "direccion");
                    atributo4 = lectorxml.getAttributeValue(null, "tipo");
                    atributo5 = lectorxml.getAttributeValue(null, "habitaciones");
                    atributo6 = lectorxml.getAttributeValue(null, "precio");
                    inmuebleActual = new Inmueble(Integer.valueOf(atributo1.substring(2)), atributo2, atributo3, Integer.valueOf(atributo4.substring(4)), Integer.valueOf(atributo5.substring(3)), Float.valueOf(atributo6.substring(6)), new ArrayList<String>());
                }
                else if(etiqueta.compareTo("foto") == 0){
                    atributo1 = lectorxml.getAttributeValue(null, "ruta");
                    fotos.add(new String(atributo1));
                }
            }
            else if(evento == XmlPullParser.END_TAG){
                etiqueta = lectorxml.getName();
                if(etiqueta.compareTo("inmueble") == 0 && fotos != null){
                    inmuebleActual.setFotos(fotos);
                    lista.add(inmuebleActual);
                    fotos = new ArrayList<String>();
                }
            }
            evento = lectorxml.next();
        }
    }

    public void cargarImagenes(){
        comprobarExistencia();
        if(!lista.get(posicion).getFotos().isEmpty()){
            Bitmap imagen = BitmapFactory.decodeFile(lista.get(posicion).getFotos().get(contador));
            ivFoto.setImageBitmap(imagen);
        }
        else
            ivFoto.setImageResource(R.drawable.no_image_available);
    }

    public void cargarSiguiente(View v){
        contador++;
        if(lista.get(posicion).getFotos().size() == contador)
            contador = 0;
        if(lista.get(posicion).getFotos().size() > contador){
            Bitmap imagen = BitmapFactory.decodeFile(lista.get(posicion).getFotos().get(contador));
            ivFoto.setImageBitmap(imagen);
        }
    }

    public void cargarAnterior(View v){
        contador--;
        if(contador < 0)
            contador = lista.get(posicion).getFotos().size() -1;
        if(lista.get(posicion).getFotos().size() > contador ){
            Bitmap imagen = BitmapFactory.decodeFile(lista.get(posicion).getFotos().get(contador));
            ivFoto.setImageBitmap(imagen);
        }
    }

    public void comprobarExistencia(){
        for(int i=0; i<lista.get(posicion).getFotos().size(); i++)
            if(BitmapFactory.decodeFile(lista.get(posicion).getFotos().get(i)) == null)
                lista.get(posicion).getFotos().remove(i);
        new Escribir(lista, this).escribir();
    }
}
