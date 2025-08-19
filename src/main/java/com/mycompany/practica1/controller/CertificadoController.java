/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica1.controller;

import com.mycompany.practica1.dao.ActividadDAO;
import com.mycompany.practica1.dao.AsistenciaDAO;
import com.mycompany.practica1.dao.CertificadoDAO;
import com.mycompany.practica1.dao.EventoDAO;
import com.mycompany.practica1.dao.ParticipanteDAO;
import com.mycompany.practica1.model.Actividad;
import com.mycompany.practica1.model.Certificado;
import com.mycompany.practica1.model.Evento;
import com.mycompany.practica1.model.Participante;
import com.mycompany.practica1.util.Validador;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Hp
 */
public class CertificadoController {

    private Certificado certificado;
    private CertificadoDAO certificadoDAO;
    private ActividadDAO actividadDAO;
    private AsistenciaDAO asistenciaDAO;
    private Validador validador;

    public CertificadoController() {
        this.certificadoDAO = new CertificadoDAO();
        this.actividadDAO = new ActividadDAO();
        this.asistenciaDAO = new AsistenciaDAO();
        this.validador = new Validador();
    }

    public ArrayList<String> registrarGenerarCertificado(String[] datos, String rutaSalidaReportes) {
        ArrayList<String> errores = validarCertificado(datos);

        if (!errores.isEmpty()) {
            return errores;
        }

        try {
            String email = datos[0];
            String codigoEvento = datos[1];

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

            if (!tieneAsistenciasValidas(participante.getIdParticipante(), codigoEvento)) {
                errores.add("El participante no ha asistido a ninguna actividad válida de este evento");
                return errores;
            }

            // Generar el certificado
            Certificado certificado = new Certificado(
                    participante.getIdParticipante(),
                    codigoEvento
            );

            // Guardar en base de datos
            certificadoDAO.registrarCertificado(certificado);

            // Generar archivo HTML del certificado
            generarArchivoCertificado(certificado, participante, evento, rutaSalidaReportes);
        } catch (SQLException e) {
            errores.add("Error de base de datos: " + e.getMessage());
        } catch (IOException e) {
            errores.add("Error al generar archivo de certificado: " + e.getMessage());
        } catch (Exception e) {
            errores.add("Error inesperado: " + e.getMessage());
        }

        return errores;
    }

    public ArrayList<String> validarCertificado(String[] datos) {
        ArrayList<String> errores = new ArrayList<>();

        String emailStr = datos[0];
        String codigoStr = datos[1];

        try {
            validador.validarEmail(emailStr);
            validador.validarCodigoEvento(codigoStr);

        } catch (Exception e) {
            errores.add(e.getMessage());
        }

        return errores;
    }

    private boolean tieneAsistenciasValidas(int idParticipante, String codigoEvento) throws SQLException {
        // Obtener todas las actividades del evento
        List<Actividad> actividades = actividadDAO.obtenerActividadesPorEvento(codigoEvento);

        // Verificar asistencia en al menos una actividad
        for (Actividad actividad : actividades) {
            if (asistenciaDAO.verificarAsistencia(actividad.getCodigo(), idParticipante)) {
                return true;
            }
        }
        return false;
    }

    private void generarArchivoCertificado(Certificado certificado, Participante participante, Evento evento, String rutaSalidaReportes)
            throws IOException {
        // Sanitizar el nombre del participante para el archivo
        String nombreLimpio = participante.getNombre().replaceAll("[^a-zA-Z0-9_\\-]", "_");

        // Crear archivo HTML
        String nombreArchivo = rutaSalidaReportes + File.separator
                + "certificado_" + evento.getCodigo() + "_" + nombreLimpio + ".html";

        try (PrintWriter writer = new PrintWriter(new FileWriter(nombreArchivo))) {
            writer.println("<!DOCTYPE html>");
            writer.println("<html>");
            writer.println("<head>");
            writer.println("<title>Certificado de Participación</title>");
            writer.println("<style>");
            writer.println("body { font-family: Arial, sans-serif; margin: 40px; }");
            writer.println(".certificado { border: 2px solid #000; padding: 20px; text-align: center; }");
            writer.println(".titulo { font-size: 24px; font-weight: bold; margin-bottom: 20px; }");
            writer.println(".contenido { margin: 20px 0; }");
            writer.println(".firma { margin-top: 50px; }");
            writer.println("</style>");
            writer.println("</head>");
            writer.println("<body>");
            writer.println("<div class='certificado'>");
            writer.println("<div class='titulo'>CERTIFICADO DE PARTICIPACIÓN</div>");
            writer.println("<div class='contenido'>");
            writer.println("<p>Se certifica que <strong>" + participante.getNombre() + "</strong></p>");
            writer.println("<p>ha participado en el evento:</p>");
            writer.println("<p><strong>" + evento.getTituloEvento() + "</strong></p>");
            writer.println("<p>realizado el " + evento.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "</p>");
            writer.println("<p>Código de certificado: " + certificado.getCodigoEvento() + "_" + certificado.getIdParticipante() + "</p>");
            writer.println("</div>");
            writer.println("<div class='firma'>");
            writer.println("<p>_________________________</p>");
            writer.println("<p>Organización del Evento</p>");
            writer.println("</div>");
            writer.println("</div>");
            writer.println("</body>");
            writer.println("</html>");
        }
    }

}
