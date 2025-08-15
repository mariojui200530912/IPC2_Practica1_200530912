/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica1.controller;

import com.mycompany.practica1.dao.EventoDAO;
import com.mycompany.practica1.model.Evento;
import com.mycompany.practica1.model.TipoEvento;
import com.mycompany.practica1.util.Validador;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 *
 * @author Hp
 */
public class EventoController {

    private EventoDAO eventoDAO;
    private Validador validador;

    public EventoController() {

    }

    public ArrayList<String> registrarEvento(ArrayList<String> datos) {
        ArrayList<String> errores = validarEvento(datos);

        if (!errores.isEmpty()) {
            return errores;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate fecha = LocalDate.parse(datos.get(1), formatter);
            TipoEvento tipo = TipoEvento.valueOf(datos.get(2).toUpperCase());
            int cupoMaximo = Integer.parseInt(datos.get(5));
            float precioEvento = Float.parseFloat(datos.get(6));

            // Construir Evento
            Evento evento = new Evento(datos.get(0), fecha, tipo, datos.get(3), datos.get(4), cupoMaximo, precioEvento);
            
            // Guardar en BD (DAO)
            EventoDAO dao = new EventoDAO();
            if (dao.existeCodigoEvento(evento.getCodigo())) {
                errores.add("El codigo de evento ya esta registrado");
                return errores;
            }
            
            dao.crearEvento(evento);
        } catch (SQLException e) {
            errores.add("Error en base de datos: " + e.getMessage());
        } catch (Exception e) {
            errores.add("Error inesperado: " + e.getMessage());
        }

        return errores;
    }

    public ArrayList<String> validarEvento(ArrayList<String> datos) {
        ArrayList<String> errores = new ArrayList<>();
        Validador v = new Validador();

        String codigo = datos.get(0);
        String fechaStr = datos.get(1);
        String tipoStr = datos.get(2);
        String titulo = datos.get(3);
        String ubicacion = datos.get(4);
        String cupoStr = datos.get(5);
        String precioStr = datos.get(6);

        try {
            v.validarCodigoEvento(codigo);
        } catch (Exception e) {
            errores.add(e.getMessage());
        }

        try {
            v.validarFechaFutura(fechaStr, "dd/MM/yyyy");
        } catch (Exception e) {
            errores.add(e.getMessage());
        }

        try {
            v.validarEnumerado(TipoEvento.class, tipoStr, "tipo de evento");
        } catch (Exception e) {
            errores.add(e.getMessage());
        }

        try {
            v.validarNoVacio(titulo, "título");
        } catch (Exception e) {
            errores.add(e.getMessage());
        }

        try {
            v.validarNoVacio(ubicacion, "ubicación");
        } catch (Exception e) {
            errores.add(e.getMessage());
        }

        try {
            int cupoMaximo = Integer.parseInt(cupoStr);
            v.validarNumeroPositivo(cupoMaximo, "cupo máximo");
        } catch (Exception e) {
            errores.add("Cupo máximo inválido: " + e.getMessage());
        }

        try {
            float precioEvento = Float.parseFloat(precioStr);
            v.validarNumeroPositivo(precioEvento, "cupo máximo");
        } catch (Exception e) {
            errores.add("Precio inválido: " + e.getMessage());
        }

        return errores;
    }
}
