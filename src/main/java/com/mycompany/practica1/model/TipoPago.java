/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica1.model;

/**
 *
 * @author Hp
 */
public enum TipoPago {
    EFECTIVO("Efectivo"),
    TRANSFERENCIA("Transferencia"),
    TARJETA("Tarjeta");
    
    private final String descripcion;
    
    TipoPago(String descripcion) {
        this.descripcion = descripcion;
    }
    
    // Método getter para obtener la descripción
    public String getDescripcion() {
        return descripcion;
    }
}
