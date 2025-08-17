/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica1.controller;

import com.mycompany.practica1.dao.ParticipanteDAO;
import com.mycompany.practica1.model.Participante;
import com.mycompany.practica1.model.TipoParticipante;
import com.mycompany.practica1.util.Validador;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Hp
 */
public class ParticipanteController {

    private Participante participante;
    private ParticipanteDAO participanteDAO;
    private Validador validador;

    public ParticipanteController() {
        this.participanteDAO = new ParticipanteDAO();
        this.validador = new Validador();
    }

    public ArrayList<String> registrarParticipante(String[] datos) {
        ArrayList<String> errores = validarParticipante(datos);

        if (!errores.isEmpty()) {
            return errores;
        }
        try {

            String nombre = datos[0];
            TipoParticipante tipo = TipoParticipante.valueOf(datos[1].toUpperCase());
            String institucion = datos[2];
            String email = datos[3];

            // Construir Participante
            Participante participante = new Participante(email, nombre, tipo, institucion);

            // Guardar en BD (DAO)
            if (participanteDAO.existeParticipante(participante.getEmail())) {
                errores.add("El correo ya esta registrado. Utilicie otro correo");
                return errores;
            }
            participanteDAO.crearParticipante(participante);
        } catch (SQLException e) {
            errores.add("Error en base de datos: " + e.getMessage());
        } catch (Exception e) {
            errores.add("Error al registrar: " + e.getMessage());
        }

        return errores;
    }

    public ArrayList<String> validarParticipante(String[] datos) {
        ArrayList<String> errores = new ArrayList<>();

        String nombreStr = datos[0];
        String tipoStr = datos[1];
        String institucionStr = datos[2];
        String emailStr = datos[3];

        try {
            validador.validarNoVacio(nombreStr, "Nombre del Participante");
            validador.validarLongitudMaxima(nombreStr, 45, "Nombre del Participante");
            validador.validarEnumerado(TipoParticipante.class, tipoStr, "tipo de participante");
            validador.validarNoVacio(institucionStr, "institucion");
            validador.validarLongitudMaxima(institucionStr, 150, "institucion");
            validador.validarEmail(emailStr);

        } catch (Exception e) {
            errores.add(e.getMessage());
        }

        return errores;
    }
    
    public List<Participante> obtenerTodosParticipantes() throws SQLException {
        return participanteDAO.obtenerTodos();
    }
}
