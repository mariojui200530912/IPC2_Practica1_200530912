/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica1.model;

import java.time.LocalTime;

/**
 *
 * @author Hp
 */
public class Actividad {
    private String codigo;
    private TipoActividad tipo;
    private String titulo;
    private LocalTime horaInicio;
    private LocalTime horaFinal;
    private int cupoMaximo;
    private int idParticipanteEncargado;
    private String codigoEvento;

    public Actividad(String codigo, TipoActividad tipo, String titulo, LocalTime horaInicio, LocalTime horaFinal, int cupoMaximo, int idParticipanteEncargado, String codigoEvento) {
        this.codigo = codigo;
        this.tipo = tipo;
        this.titulo = titulo;
        this.horaInicio = horaInicio;
        this.horaFinal = horaFinal;
        this.cupoMaximo = cupoMaximo;
        this.idParticipanteEncargado = idParticipanteEncargado;
        this.codigoEvento = codigoEvento;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public TipoActividad getTipo() {
        return tipo;
    }

    public void setTipo(TipoActividad tipo) {
        this.tipo = tipo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFinal() {
        return horaFinal;
    }

    public void setHoraFinal(LocalTime horaFinal) {
        this.horaFinal = horaFinal;
    }

    public int getCupoMaximo() {
        return cupoMaximo;
    }

    public void setCupoMaximo(int cupoMaximo) {
        this.cupoMaximo = cupoMaximo;
    }

    public int getIdParticipanteEncargado() {
        return idParticipanteEncargado;
    }

    public void setIdParticipanteEncargado(int idParticipanteEncargado) {
        this.idParticipanteEncargado = idParticipanteEncargado;
    }

    public String getCodigoEvento() {
        return codigoEvento;
    }

    public void setCodigoEvento(String codigoEvento) {
        this.codigoEvento = codigoEvento;
    }
    
}
