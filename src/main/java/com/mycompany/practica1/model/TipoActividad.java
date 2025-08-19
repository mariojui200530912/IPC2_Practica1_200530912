/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica1.model;

/**
 *
 * @author Hp
 */
public enum TipoActividad {
    CHARLA("Charla"),
    TALLER("Taller"),
    DEBATE("Debate"),
    OTRA("Otra");
    
    private final String descripcion;
    
    TipoActividad(String descripcion) {
        this.descripcion = descripcion;
    }
    
    // Método getter para obtener la descripción
    public String getDescripcion() {
        return descripcion;
    }
}
