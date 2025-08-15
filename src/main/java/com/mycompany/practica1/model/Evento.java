/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica1.model;

import java.time.LocalDate;

/**
 *
 * @author Hp
 */
public class Evento {
    private String codigo;
    private LocalDate fecha;
    private TipoEvento tipoEvento;
    private String tituloEvento;
    private String ubicacion;
    private int cupoMaximo;  
    private float precioEvento;
    
    public Evento(String codigo, LocalDate fecha, TipoEvento tipoEvento, String tituloEvento, String ubicacion, int cupoMaximo, float precioEvento){
        this.codigo = codigo;
        this.fecha = fecha;
        this.tipoEvento = tipoEvento;
        this.ubicacion = ubicacion;
        this.cupoMaximo = cupoMaximo;    
        this.precioEvento = precioEvento;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public TipoEvento getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(TipoEvento tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public String getTituloEvento() {
        return tituloEvento;
    }

    public void setTituloEvento(String tituloEvento) {
        this.tituloEvento = tituloEvento;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public int getCupoMaximo() {
        return cupoMaximo;
    }

    public void setCupoMaximo(int cupoMaximo) {
        this.cupoMaximo = cupoMaximo;
    }

    public float getPrecioEvento() {
        return precioEvento;
    }

    public void setPrecioEvento(float precioEvento) {
        this.precioEvento = precioEvento;
    }
    
}
