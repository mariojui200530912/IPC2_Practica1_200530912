/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica1.model;

/**
 *
 * @author Hp
 */
public class Participante {
    private int idParticipante;
    private String email;
    private String nombre;
    private TipoParticipante tipoParticipante;
    private String institucionProcedencia;
    private TipoEstatus estatus;

    public Participante(String email, String nombre, TipoParticipante tipoParticipante, String institucionProcedencia) {
        this.email = email;
        this.nombre = nombre;
        this.tipoParticipante = tipoParticipante;
        this.institucionProcedencia = institucionProcedencia;
    }
    
    public Participante(int idParticipante, String email, String nombre, TipoParticipante tipoParticipante, String institucionProcedencia) {
        this.idParticipante = idParticipante;
        this.email = email;
        this.nombre = nombre;
        this.tipoParticipante = tipoParticipante;
        this.institucionProcedencia = institucionProcedencia;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoParticipante getTipoParticipante() {
        return tipoParticipante;
    }

    public void setTipoParticipante(TipoParticipante tipoParticipante) {
        this.tipoParticipante = tipoParticipante;
    }

    public String getInstitucionProcedencia() {
        return institucionProcedencia;
    }

    public void setInstitucionProcedencia(String institucionProcedencia) {
        this.institucionProcedencia = institucionProcedencia;
    }

    public TipoEstatus getEstatus() {
        return estatus;
    }

    public void setEstatus(TipoEstatus estatus) {
        this.estatus = estatus;
    }
}
