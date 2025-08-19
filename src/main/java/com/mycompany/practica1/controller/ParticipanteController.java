/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica1.controller;

import com.mycompany.practica1.dao.EventoDAO;
import com.mycompany.practica1.dao.ParticipanteDAO;
import com.mycompany.practica1.model.Evento;
import com.mycompany.practica1.model.Participante;
import com.mycompany.practica1.model.TipoParticipante;
import com.mycompany.practica1.util.Validador;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Hp
 */
public class ParticipanteController {

    private Participante participante;
    private ParticipanteDAO participanteDAO;
    private Evento evento;
    private EventoDAO eventoDAO;
    private Validador validador;

    public ParticipanteController() {
        this.eventoDAO = new EventoDAO();
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

    public ArrayList<String> generarReporte(String[] datos, String rutaSalidaReportes) {
        ArrayList<String> errores = new ArrayList<>();
        try {
            String codigoEvento = datos[0];
            String tipoParticipante = datos[1];
            String institucion = datos[2];
            LocalDateTime ahora = LocalDateTime.now();
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
            String fechaHora = ahora.format(format);

            Evento evento = eventoDAO.obtenerEventoPorCodigo(codigoEvento);
            if (evento == null) {
                errores.add("Evento no encontrado");
                return errores;
            }
            // Construir Participante
            List<Participante> participantes = participanteDAO.obtenerParticipantesParaReporte(codigoEvento, tipoParticipante, institucion);
            if (participantes == null) {
                errores.add("No hay participantes que cumplan con estos filtros");
                return errores;
            }
            String nombreArchivo = rutaSalidaReportes + File.separator + "reporte_participantes_" + codigoEvento + "_" + fechaHora + ".html";
            try (PrintWriter writer = new PrintWriter(new FileWriter(nombreArchivo))) {
                writer.println("<!DOCTYPE html>");
                writer.println("<html>");
                writer.println("<head>");
                writer.println("<title>Reporte de Participantes - " + codigoEvento + "</title>");
                writer.println("<style>");
                writer.println("table { width: 100%; border-collapse: collapse; }");
                writer.println("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
                writer.println("th { background-color: #f2f2f2; }");
                writer.println("</style>");
                writer.println("</head>");
                writer.println("<body>");
                writer.println("<h1>Reporte de Participantes</h1>");
                writer.println("<h2>Evento: " + evento.getTituloEvento() + "</h2>");
                writer.println("<p><strong>Código:</strong> " + codigoEvento + "</p>");
                writer.println("<p><strong>Fecha:</strong> " + evento.getFecha() + "</p>");

                writer.println("<table>");
                writer.println("<tr>");
                writer.println("<th>Correo Electrónico</th>");
                writer.println("<th>Tipo</th>");
                writer.println("<th>Nombre Completo</th>");
                writer.println("<th>Institución</th>");
                writer.println("<th>Validado</th>");
                writer.println("</tr>");

                for (Participante p : participantes) {
                    writer.println("<tr>");
                    writer.println("<td>" + p.getEmail() + "</td>");
                    writer.println("<td>" + p.getTipoParticipante().getDescripcion() + "</td>");
                    writer.println("<td>" + p.getNombre() + "</td>");
                    writer.println("<td>" + p.getInstitucionProcedencia() + "</td>");
                    writer.println("<td>" + (p.getEstatus().getDescripcion() == "Confirmado" ? "Sí" : "No") + "</td>");
                    writer.println("</tr>");
                }

                writer.println("</table>");
                writer.println("<p>Total participantes: " + participantes.size() + "</p>");
                writer.println("</body>");
                writer.println("</html>");
            }

            
        } catch (SQLException e) {
            errores.add("Error en base de datos: " + e.getMessage());
        } catch (Exception e) {
            errores.add("Error al registrar: " + e.getMessage());
        }

        return errores;
    }
}
