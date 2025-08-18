/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica1.controller;

import com.mycompany.practica1.dao.EventoDAO;
import com.mycompany.practica1.dao.PagoDAO;
import com.mycompany.practica1.dao.ParticipanteDAO;
import com.mycompany.practica1.model.Evento;
import com.mycompany.practica1.model.Pago;
import com.mycompany.practica1.model.Participante;
import com.mycompany.practica1.model.TipoPago;
import com.mycompany.practica1.util.Validador;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Hp
 */
public class PagoController {

    private Pago pago;
    private PagoDAO pagoDAO;
    private Validador validador;

    public PagoController(PagoDAO pagoDAO, Validador validador) {
        this.pagoDAO = pagoDAO;
        this.validador = validador;
    }

    public ArrayList<String> registrarPago(String[] datos) {
        ArrayList<String> errores = validarPago(datos);

        if (!errores.isEmpty()) {
            return errores;
        }
        try {
            String email = datos[0];
            String codigoEvento = datos[1];
            TipoPago tipo = TipoPago.valueOf(datos[2]);
            float monto = Float.parseFloat(datos[3]);

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
            }else if (!(evento.getCostoInscripcion() <= monto)) {
                errores.add("El monto de pago no es igual o mayor al costo de inscripcion del Evento");
                return errores;
            }

            Pago pago = new Pago(tipo, monto, evento.getCodigo(), participante.getIdParticipante());
            pagoDAO.registrarPago(pago);
        } catch (SQLException e) {
            errores.add("Error en base de datos: " + e.getMessage());
        } catch (Exception e) {
            errores.add("Error al registrar: " + e.getMessage());
        }

        return errores;
    }

    public ArrayList<String> validarPago(String[] datos) {
        ArrayList<String> errores = new ArrayList<>();

        String emailStr = datos[0];
        String codigoStr = datos[1];
        String tipoStr = datos[2];
        String montoStr = datos[3];

        try {
            validador.validarEmail(emailStr);
            validador.validarCodigoEvento(codigoStr);
            validador.validarEnumerado(TipoPago.class, tipoStr, "tipo de pago");
            float monto = Float.parseFloat(montoStr);
            validador.validarDecimales(monto, 2,"monto de pago");

        } catch (Exception e) {
            errores.add(e.getMessage());
        }
        return errores;
    }

}
