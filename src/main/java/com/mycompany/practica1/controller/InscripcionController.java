/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica1.controller;

import com.mycompany.practica1.dao.EventoDAO;
import com.mycompany.practica1.dao.InscripcionDAO;
import com.mycompany.practica1.dao.ParticipanteDAO;
import com.mycompany.practica1.model.Evento;
import com.mycompany.practica1.model.Inscripcion;
import com.mycompany.practica1.model.Participante;
import com.mycompany.practica1.model.TipoEstatus;
import com.mycompany.practica1.model.TipoInscripcion;
import com.mycompany.practica1.model.TipoParticipante;
import com.mycompany.practica1.util.Validador;
import java.util.ArrayList;

/**
 *
 * @author Hp
 */
public class InscripcionController {

    private Inscripcion inscripcion;
    private InscripcionDAO inscripcionDAO;
    private Validador validador;

    public InscripcionController() {
        this.inscripcionDAO = new InscripcionDAO();
        this.validador = new Validador();
    }

    public ArrayList<String> inscribirParticipante(String[] datos) {
        ArrayList<String> errores = validarIncripcion(datos);

        if (!errores.isEmpty()) {
            return errores;
        }

        try {
            String email = datos[0];
            String codigoEvento = datos[1];
            TipoInscripcion tipo = TipoInscripcion.valueOf(datos[2]);
            TipoEstatus estatus = TipoEstatus.NO_CONFIRMADO;

            ParticipanteDAO participanteDAO = new ParticipanteDAO();
            EventoDAO eventoDAO = new EventoDAO();

            Participante participante = participanteDAO.obtenerParticipantePorEmail(email);
            Evento evento = eventoDAO.obtenerEventoPorCodigo(codigoEvento);
            if (participante == null) {
                errores.add("El correo de participante no esta registrado en la base de datos");
                return errores;
            } else if (evento == null) {
                errores.add("El codigo de Evento no existe en la base de datos");
                return errores;
            }

            Inscripcion inscripcion = new Inscripcion(participante.getIdParticipante(),participante.getEmail(), evento.getCodigo(), tipo, estatus);

            inscripcionDAO.registrarIncripcion(inscripcion);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: valor inválido en los datos -> " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error inesperado al procesar inscripción: " + e.getMessage());
        }

        return errores;
    }

    public ArrayList<String> validarIncripcion(String[] datos) {
        ArrayList<String> errores = new ArrayList<>();

        String emailStr = datos[0];
        String codigoStr = datos[1];
        String tipoStr = datos[2];
       
        try {
            validador.validarEmail(emailStr);
            validador.validarCodigoEvento(codigoStr);
            validador.validarEnumerado(TipoInscripcion.class, tipoStr, "tipo de inscripcion");

        } catch (Exception e) {
            errores.add(e.getMessage());
        }

        return errores;
    }

}
