/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica1.controller;

import com.mycompany.practica1.dao.ActividadDAO;
import com.mycompany.practica1.dao.AsistenciaDAO;
import com.mycompany.practica1.dao.EventoDAO;
import com.mycompany.practica1.dao.ParticipanteDAO;
import com.mycompany.practica1.model.Actividad;
import com.mycompany.practica1.model.Asistencia;
import com.mycompany.practica1.model.Evento;
import com.mycompany.practica1.model.Participante;
import com.mycompany.practica1.util.Validador;
import java.util.ArrayList;

/**
 *
 * @author Hp
 */
public class AsistenciaController {

    private Asistencia asistencia;
    private AsistenciaDAO asistenciaDAO;
    private Validador validador;

    public AsistenciaController() {
        this.asistenciaDAO = new AsistenciaDAO();
        this.validador = new Validador();
    }

    public ArrayList<String> registrarAsistencia(String[] datos) {
        ArrayList<String> errores = validarAsistencia(datos);

        if (!errores.isEmpty()) {
            return errores;
        }

        try {
            String email = datos[0];
            String codigoActividad = datos[1];

            ParticipanteDAO participanteDAO = new ParticipanteDAO();
            ActividadDAO actividadDAO = new ActividadDAO();

            Participante participante = participanteDAO.obtenerParticipantePorEmail(email);
            Actividad actividad = actividadDAO.obtenerActividadPorCodigo(codigoActividad);

            if (participante == null) {
                errores.add("El correo de participante no esta registrado en la base de datos");
                return errores;
            } else if (actividad == null) {
                errores.add("El codigo de Actividad no existe en la base de datos");
                return errores;
            }

            Asistencia asistencia = new Asistencia(actividad.getCodigo(), participante.getIdParticipante());
            if (asistenciaDAO.existeAsistencia(asistencia)) {
                errores.add("La asistencia ya esta registrada");
                return errores;
            }
            asistenciaDAO.registrarAsistencia(asistencia);
        } catch (IllegalArgumentException e) {
            errores.add("Error: valor inválido en los datos -> " + e.getMessage());
        } catch (Exception e) {
            errores.add("Error inesperado al procesar inscripción: " + e.getMessage());
        }

        return errores;
    }

    public ArrayList<String> validarAsistencia(String[] datos) {
        ArrayList<String> errores = new ArrayList<>();

        String emailStr = datos[0];
        String codigoStr = datos[1];

        try {
            validador.validarEmail(emailStr);
            validador.validarCodigoActividad(codigoStr);

        } catch (Exception e) {
            errores.add(e.getMessage());
        }

        return errores;
    }

}
