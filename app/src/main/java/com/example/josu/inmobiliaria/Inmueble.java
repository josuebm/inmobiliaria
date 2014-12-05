package com.example.josu.inmobiliaria;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Josué on 04/12/2014.
 */
public class Inmueble implements Parcelable, Comparable<Inmueble>{

    int id, habitaciones, tipo;
    String localidad, direccion;
    float precio;
    ArrayList<String> fotos;

    public Inmueble() {
    }

    public Inmueble(int id, String localidad, String direccion, int tipo, int habitaciones, float precio, ArrayList<String> fotos) {
        this.id = id;
        this.habitaciones = habitaciones;
        this.tipo = tipo;
        this.localidad = localidad;
        this.direccion = direccion;
        this.precio = precio;
        this.fotos = fotos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHabitaciones() {
        return habitaciones;
    }

    public void setHabitaciones(int habitaciones) {
        this.habitaciones = habitaciones;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public ArrayList<String> getFotos() {
        return fotos;
    }

    public void setFotos(ArrayList<String> fotos) {
        this.fotos = fotos;
    }

    @Override
    public String toString() {
        return "Inmueble{" +
                "id=" + id +
                ", habitaciones=" + habitaciones +
                ", tipo=" + tipo +
                ", localidad='" + localidad + '\'' +
                ", direccion='" + direccion + '\'' +
                ", precio=" + precio +
                ", fotos=" + fotos +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.id);
        parcel.writeString(this.localidad);
        parcel.writeString(this.direccion);
        parcel.writeInt(this.tipo);
        parcel.writeInt(this.habitaciones);
        parcel.writeFloat(this.precio);
        parcel.writeList(this.fotos);
    }

    public Inmueble (Parcel p){
        this.id=p.readInt();
        this.localidad=p.readString();
        this.direccion=p.readString();
        this.tipo=p.readInt();
        this.habitaciones = p.readInt();
        this.precio = p.readFloat();
        this.fotos = p.readArrayList(String.class.getClassLoader());

    }

    public static final Parcelable.Creator <Inmueble> CREATOR =
            new Parcelable.Creator <Inmueble>() {
                @Override
                public Inmueble createFromParcel(Parcel parcel) {
                    return new Inmueble(parcel);
                }
                @Override
                public Inmueble[] newArray(int i) {
                    return new Inmueble[i];
                }
            };

    @Override
    public int compareTo(Inmueble another) {
        int a = getId();
        int b = another.getId();
        if(a < b)
            return -1;
        else
        if(b > a)
            return 1;
        else
            return 0;
    }
}
