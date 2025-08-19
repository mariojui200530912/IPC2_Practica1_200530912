/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica1.controller;

import com.mycompany.practica1.dao.ActividadDAO;
import com.mycompany.practica1.dao.AsistenciaDAO;
import com.mycompany.practica1.dao.EventoDAO;
import com.mycompany.practica1.dao.InscripcionDAO;
import com.mycompany.practica1.dao.ParticipanteDAO;
import com.mycompany.practica1.model.Actividad;
import com.mycompany.practica1.model.Evento;
import com.mycompany.practica1.model.Inscripcion;
import com.mycompany.practica1.model.Participante;
import com.mycompany.practica1.model.TipoActividad;
import com.mycompany.practica1.model.TipoEstatus;
import com.mycompany.practica1.model.TipoInscripcion;
import com.mycompany.practica1.util.Validador;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Hp
 */
public class ActividadController {

    private ActividadDAO actividadDAO;
    private Validador validador;
    private InscripcionDAO inscripcionDAO;
    private Evento evento;
    private EventoDAO eventoDAO;

    public ActividadController() {
        this.eventoDAO = new EventoDAO();
        this.inscripcionDAO = new InscripcionDAO();
        this.actividadDAO = new ActividadDAO();
        this.validador = new Validador();
    }

    public ArrayList<String> registrarActividad(String[] datos) {
        ArrayList<String> errores = validarActividad(datos);

        if (!errores.isEmpty()) {
            return errores;
        }
        try {
            String codigo = datos[0];
            String codigoEvento = datos[1];
            TipoActividad tipo = TipoActividad.valueOf(datos[2].toUpperCase());
            String titulo = datos[3];
            String email = datos[4];
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime horaInicio = LocalTime.parse(datos[5], formatter);
            LocalTime horaFin = LocalTime.parse(datos[6], formatter);
            int cupoMaximo = Integer.parseInt(datos[7]);

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

            Inscripcion inscripcion = inscripcionDAO.obtenerInscripcion(evento.getCodigo(), participante.getIdParticipante());
            if (inscripcion == null) {
                errores.add("El participante no está inscrito en este evento");
                return errores;
            } else if (inscripcion.getTipoInscripcion() == TipoInscripcion.ASISTENTE) {
                errores.add("El participante debe tener un rol diferente a ASISTENTE para impartir actividades");
                return errores;
            } else if (inscripcion.getEstatus() != TipoEstatus.CONFIRMADO) {
                errores.add("La inscripción del participante no está confirmada");
                return errores;
            }
            // Construir Actividad
            Actividad actividad = new Actividad(codigo, tipo, titulo, horaInicio, horaFin, cupoMaximo, participante.getIdParticipante(), evento.getCodigo());

            // Guardar en BD (DAO)
            if (actividadDAO.existeCodigoActividad(actividad.getCodigo())) {
                errores.add("El codigo de actividad ya esta registrado");
                return errores;
            }

            actividadDAO.crearActividad(actividad);
        } catch (SQLException e) {
            errores.add("Error en base de datos: " + e.getMessage());
        } catch (Exception e) {
            errores.add("Error al registrar: " + e.getMessage());
        }
        return errores;
    }

    public ArrayList<String> validarActividad(String[] datos) {
        ArrayList<String> errores = new ArrayList<>();

        String codigo = datos[0];
        String codigoEventoStr = datos[1];
        String tipoStr = datos[2];
        String titulo = datos[3];
        String emailStr = datos[4];
        String horaInicioStr = datos[5];
        String horaFinStr = datos[6];
        String cupoStr = datos[7];

        try {
            validador.validarCodigoActividad(codigo);
            validador.validarCodigoEvento(codigoEventoStr);
            validador.validarEnumerado(TipoActividad.class, tipoStr, "tipo de actividad");
            validador.validarNoVacio(titulo, "título");
            validador.validarLongitudMaxima(titulo, 200, "titulo");
            validador.validarEmail(emailStr);
            validador.validarHora(horaFinStr);
            validador.validarHora(horaInicioStr);
            validador.validarRangoHoras(horaInicioStr, horaFinStr);

            int cupoMaximo = Integer.parseInt(cupoStr);
            validador.validarNumeroPositivo(cupoMaximo, "cupo máximo");

        } catch (IllegalArgumentException e) {
            errores.add(e.getMessage());
        } catch (Exception e) {
            errores.add("Costo de Inscripcion invalido: " + e.getMessage());
        }

        return errores;
    }

    public List<Actividad> obtenerTodosEventos() throws SQLException {
        return actividadDAO.obtenerTodos();
    }

    public ArrayList<String> generarReporte(String[] datos, String rutaSalidaReportes) {
        ArrayList<String> errores = new ArrayList<>();
        try {
            String codigoEvento = datos[0];
            String tipoActividad = datos[1];
            String emailEncargado = datos[2];
            LocalDateTime ahora = LocalDateTime.now();
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
            String fechaHora = ahora.format(format);

            AsistenciaDAO asistenciaDAO = new AsistenciaDAO();
            Evento evento = eventoDAO.obtenerEventoPorCodigo(codigoEvento);
            if (evento == null) {
                errores.add("Evento no encontrado");
                return errores;
            }
            // Construir Actividades
            List<Actividad> actividades = actividadDAO.obtenerActividadesParaReporte(codigoEvento, tipoActividad, emailEncargado);
            if (actividades == null) {
                errores.add("No hay actividades que cumplan con estos filtros");
                return errores;
            }
            String nombreArchivo = rutaSalidaReportes + File.separator + "reporte_actividades_" + codigoEvento + "_" + fechaHora + ".html";
            try (PrintWriter writer = new PrintWriter(new FileWriter(nombreArchivo))) {
                writer.println("<!DOCTYPE html>");
                writer.println("<html>");
                writer.println("<head>");
                writer.println("<title>Reporte de Actividades - " + codigoEvento + "</title>");
                writer.println("<style>");
                writer.println("table { width: 100%; border-collapse: collapse; }");
                writer.println("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
                writer.println("th { background-color: #f2f2f2; }");
                writer.println("</style>");
                writer.println("</head>");
                writer.println("<body>");
                writer.println("<h1>Reporte de Actividades</h1>");
                writer.println("<h2>Evento: " + evento.getTituloEvento() + "</h2>");

                writer.println("<table>");
                writer.println("<tr>");
                writer.println("<th>Código Actividad</th>");
                writer.println("<th>Tipo</th>");
                writer.println("<th>Título</th>");
                writer.println("<th>Encargado</th>");
                writer.println("<th>Hora Inicio</th>");
                writer.println("<th>Hora Fin</th>");
                writer.println("<th>Cupo Máximo</th>");
                writer.println("<th>Asistentes</th>");
                writer.println("</tr>");

                for (Actividad a : actividades) {
                    int asistentes = asistenciaDAO.contarAsistentes(a.getCodigo());
                    writer.println("<tr>");
                    writer.println("<td>" + a.getCodigo() + "</td>");
                    writer.println("<td>" + a.getTipo() + "</td>");
                    writer.println("<td>" + a.getTitulo() + "</td>");
                    writer.println("<td>" + a.getEmailParticipanteEncargado() + "</td>");
                    writer.println("<td>" + a.getHoraInicio() + "</td>");
                    writer.println("<td>" + a.getHoraFinal() + "</td>");
                    writer.println("<td>" + a.getCupoMaximo() + "</td>");
                    writer.println("<td>" + asistentes + "</td>");
                    writer.println("</tr>");
                }

                writer.println("</table>");
                writer.println("<p>Total actividades: " + actividades.size() + "</p>");
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
