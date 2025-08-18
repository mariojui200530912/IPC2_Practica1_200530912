/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica1.model;

/**
 *
 * @author Hp
 */
public class Inscripcion {
    private int idParticipante;
    private String email;
    private String codigoEvento;
    private TipoInscripcion tipoInscripcion;
    private TipoEstatus estatus;

    public Inscripcion(int idParticipante, String email, String codigoEvento, TipoInscripcion tipoInscripcion, TipoEstatus estatus) {
        this.idParticipante = idParticipante;
        this.email = email;
        this.codigoEvento = codigoEvento;
        this.tipoInscripcion = tipoInscripcion;
        this.estatus = estatus;
    }
    
    public Inscripcion(String codigoEvento, int idParticipante, TipoInscripcion tipoInscripcion, TipoEstatus estatus){
        this.codigoEvento = codigoEvento;
        this.idParticipante = idParticipante;
        this.tipoInscripcion = tipoInscripcion;
        this.estatus = estatus;
    }

    public int getIdParticipante() {
        return idParticipante;
    }

    public void setIdParticipante(int idParticipante) {
        this.idParticipante = idParticipante;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCodigoEvento() {
        return codigoEvento;
    }

    public void setCodigoEvento(String codigoEvento) {
        this.codigoEvento = codigoEvento;
    }

    public TipoInscripcion getTipoInscripcion() {
        return tipoInscripcion;
    }

    public void setTipoInscripcion(TipoInscripcion tipoInscripcion) {
        this.tipoInscripcion = tipoInscripcion;
    }

    public TipoEstatus getEstatus() {
        return estatus;
    }

    public void setEstatus(TipoEstatus estatus) {
        this.estatus = estatus;
    }   
}
