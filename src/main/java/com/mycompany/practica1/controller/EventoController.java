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
import java.util.List;

/**
 *
 * @author Hp
 */
public class EventoController {

    private EventoDAO eventoDAO;
    private Validador validador;

    public EventoController() {
        this.eventoDAO = new EventoDAO();
        this.validador = new Validador();
    }

    public ArrayList<String> registrarEvento(String[] datos) {
        ArrayList<String> errores = validarEvento(datos);

        if (!errores.isEmpty()) {
            return errores;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate fecha = LocalDate.parse(datos[1], formatter);
            TipoEvento tipo = TipoEvento.valueOf(datos[2].toUpperCase());
            int cupoMaximo = Integer.parseInt(datos[5]);
            float costoInscripcion = Float.parseFloat(datos[6]);

            // Construir Evento
            Evento evento = new Evento(datos[0], fecha, tipo, datos[3], datos[4], cupoMaximo, costoInscripcion);
            
            // Guardar en BD (DAO)
            if (eventoDAO.existeCodigoEvento(evento.getCodigo())) {
                errores.add("El codigo de evento ya esta registrado");
                return errores;
            }
            
            eventoDAO.crearEvento(evento);
        } catch (SQLException e) {
            errores.add("Error en base de datos: " + e.getMessage());
        } catch (Exception e) {
            errores.add("Error al registrar: " + e.getMessage());
        }
        return errores;
    }

    public ArrayList<String> validarEvento(String[] datos) {
        ArrayList<String> errores = new ArrayList<>();

        String codigo = datos[0];
        String fechaStr = datos[1];
        String tipoStr = datos[2];
        String titulo = datos[3];
        String ubicacion = datos[4];
        String cupoStr = datos[5];
        String precioStr = datos[6];

        try {
            validador.validarCodigoEvento(codigo);

            validador.validarFechaFutura(fechaStr, "dd/MM/yyyy");

            validador.validarEnumerado(TipoEvento.class, tipoStr, "tipo de evento");

            validador.validarNoVacio(titulo, "título");

            validador.validarLongitudMaxima(ubicacion, 150, "ubicacion");
            validador.validarNoVacio(ubicacion, "ubicación");

            int cupoMaximo = Integer.parseInt(cupoStr);
            validador.validarNumeroPositivo(cupoMaximo, "cupo máximo");

            float costoInscripcion = Float.parseFloat(precioStr);
            validador.validarDecimales(costoInscripcion, 2,"costo de inscripcion");
            
        } catch (IllegalArgumentException e){
            errores.add(e.getMessage());
        } catch (Exception e){
            errores.add("Costo de Inscripcion invalido: " + e.getMessage());
        }

        return errores;
    }
    
    public List<Evento> obtenerTodosEventos() throws SQLException {
        return eventoDAO.obtenerTodos();
    }    
}
