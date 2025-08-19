/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica1.model;

/**
 *
 * @author Hp
 */
public class Certificado {
    private int idParticipante;
    private String codigoEvento;

    public Certificado(int idParticipante, String codigoEvento) {
        this.idParticipante = idParticipante;
        this.codigoEvento = codigoEvento;
    }

    public int getIdParticipante() {
        return idParticipante;
    }

    public void setIdParticipante(int idParticipante) {
        this.idParticipante = idParticipante;
    }

    public String getCodigoEvento() {
        return codigoEvento;
    }

    public void setCodigoEvento(String codigoEvento) {
        this.codigoEvento = codigoEvento;
    }
    
    
}
