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
import com.mycompany.practica1.model.TipoEstatus;
import com.mycompany.practica1.model.TipoEvento;
import com.mycompany.practica1.util.Validador;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Hp
 */
public class EventoController {

    private ParticipanteDAO participanteDAO;
    private PagoDAO pagoDAO;
    private EventoDAO eventoDAO;
    private Validador validador;

    public EventoController() {
        this.participanteDAO = new ParticipanteDAO();
        this.pagoDAO = new PagoDAO();
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
            validador.validarDecimales(costoInscripcion, 2, "costo de inscripcion");

        } catch (IllegalArgumentException e) {
            errores.add(e.getMessage());
        } catch (Exception e) {
            errores.add("Costo de Inscripcion invalido: " + e.getMessage());
        }

        return errores;
    }

    public List<Evento> obtenerTodosEventos() throws SQLException {
        return eventoDAO.obtenerTodos();
    }

    public ArrayList<String> generarReporte(String[] datos, String rutaSalidaReportes) {
        ArrayList<String> errores = new ArrayList<>();
        try {
            String tipoEvento = datos.length > 0 && !datos[0].isEmpty() ? datos[0] : "";
            String fechaInicio = datos.length > 1 && !datos[1].isEmpty() ? datos[1] : "";
            String fechaFin = datos.length > 2 && !datos[2].isEmpty() ? datos[2] : "";

            String cupoMin = "";
            String cupoMax = "";

            if (datos.length > 3) {
                cupoMin = datos[3].isEmpty() ? "" : datos[3];
            }
            if (datos.length > 4) {
                cupoMax = datos[4].isEmpty() ? "" : datos[4];
            }
            LocalDateTime ahora = LocalDateTime.now();
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
            String fechaHora = ahora.format(format);

            // Construir Actividades
            List<Evento> eventos = eventoDAO.obtenerEventosParaReporte(tipoEvento, fechaInicio, fechaFin, cupoMin, cupoMax);
            if (eventos == null) {
                errores.add("No hay actividades que cumplan con estos filtros");
                return errores;
            }
            String nombreArchivo = rutaSalidaReportes + File.separator + "reporte_eventos_" + fechaHora + ".html";
            try (PrintWriter writer = new PrintWriter(new FileWriter(nombreArchivo))) {
                writer.println("<!DOCTYPE html>");
                writer.println("<html>");
                writer.println("<head>");
                writer.println("<title>Reporte de Eventos</title>");
                writer.println("<style>");
                writer.println("table { width: 100%; border-collapse: collapse; margin-bottom: 20px; }");
                writer.println("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
                writer.println("th { background-color: #f2f2f2; }");
                writer.println(".evento { margin-bottom: 30px; }");
                writer.println("</style>");
                writer.println("</head>");
                writer.println("<body>");
                writer.println("<h1>Reporte de Eventos</h1>");

                for (Evento evento : eventos) {
                    writer.println("<div class='evento'>");
                    writer.println("<h2>" + evento.getTituloEvento() + "</h2>");
                    writer.println("<p><strong>Código:</strong> " + evento.getCodigo() + "</p>");
                    writer.println("<p><strong>Fecha:</strong> " + evento.getFecha() + "</p>");
                    writer.println("<p><strong>Tipo:</strong> " + evento.getTipoEvento() + "</p>");
                    writer.println("<p><strong>Ubicación:</strong> " + evento.getUbicacion() + "</p>");
                    writer.println("<p><strong>Cupo máximo:</strong> " + evento.getCupoMaximo() + "</p>");

                    List<Participante> participantes = participanteDAO.obtenerParticipantesParaReporte(evento.getCodigo(), "", "");
                    int confirmados = (int) participantes.stream().filter(p -> p.getEstatus() == TipoEstatus.CONFIRMADO).count();

                    writer.println("<table>");
                    writer.println("<tr>");
                    writer.println("<th>Correo</th>");
                    writer.println("<th>Nombre</th>");
                    writer.println("<th>Tipo</th>");
                    writer.println("<th>Método Pago</th>");
                    writer.println("<th>Monto</th>");
                    writer.println("</tr>");

                    double totalRecaudado = 0;
                    for (Participante p : participantes) {
                        Pago pago = pagoDAO.obtenerPago(p.getIdParticipante(), evento.getCodigo());
                        writer.println("<tr>");
                        writer.println("<td>" + p.getEmail() + "</td>");
                        writer.println("<td>" + p.getNombre() + "</td>");
                        writer.println("<td>" + p.getTipoParticipante().getDescripcion() + "</td>");
                        writer.println("<td>" + (pago != null ? pago.getTipo().getDescripcion() : "Sin pago") + "</td>");
                        writer.println("<td>" + (pago != null ? pago.getMonto() : "0.00") + "</td>");
                        writer.println("</tr>");
                        if (pago != null) {
                            totalRecaudado += pago.getMonto();
                        }
                    }

                    writer.println("</table>");
                    writer.println("<p><strong>Total recaudado:</strong> Q" + String.format("%.2f", totalRecaudado) + "</p>");
                    writer.println("<p><strong>Participantes validados:</strong> " + confirmados + "</p>");
                    writer.println("<p><strong>Participantes no validados:</strong> " + (participantes.size() - confirmados) + "</p>");
                    writer.println("</div>");
                }

                writer.println("<p>Total eventos: " + eventos.size() + "</p>");
                writer.println("</body>");
                writer.println("</html>");
            }

        } catch (Exception e) {
            errores.add("Error al generar reporte: " + e.getMessage());
            return errores;
        }
        return errores;
    }
}
