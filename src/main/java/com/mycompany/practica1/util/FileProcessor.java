/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica1.util;

import com.mycompany.practica1.controller.ActividadController;
import com.mycompany.practica1.controller.AsistenciaController;
import com.mycompany.practica1.controller.CertificadoController;
import com.mycompany.practica1.controller.EventoController;
import com.mycompany.practica1.controller.InscripcionController;
import com.mycompany.practica1.controller.PagoController;
import com.mycompany.practica1.controller.ParticipanteController;
import com.mycompany.practica1.model.TipoInscripcion;
import com.mycompany.practica1.ui.LogWindow;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.swing.SwingUtilities;

/**
 *
 * @author Hp
 */
public class FileProcessor {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private int velocidadMs;
    private String rutaSalidaReportes;
    private EventoController eventoController;
    private ParticipanteController participanteController;
    private InscripcionController inscripcionController;
    private ActividadController actividadController;
    private PagoController pagoController;
    private AsistenciaController asistenciaController;
    private CertificadoController certificadoController;
    private LogWindow logWindow;

    public FileProcessor(int velocidadMs, String rutaSalidaReportes, LogWindow logWindow) {
        this.velocidadMs = velocidadMs;
        this.rutaSalidaReportes = rutaSalidaReportes;
        this.logWindow = logWindow;
        this.eventoController = new EventoController();
        this.participanteController = new ParticipanteController();
        this.actividadController = new ActividadController();
        this.asistenciaController = new AsistenciaController();
        this.certificadoController = new CertificadoController();
        this.inscripcionController = new InscripcionController();
        this.pagoController = new PagoController();
    }

    public void procesarArchivo(File archivo) {
        logWindow.limpiarLog();
        logWindow.setVisible(true);
        new Thread(() -> {
            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                int totalLineas = contarLineas(archivo);
                int lineaActual = 0;

                String linea;
                while ((linea = br.readLine()) != null) {
                    lineaActual++;
                    final int currentLine = lineaActual;

                    SwingUtilities.invokeLater(() -> {
                        logWindow.agregarLog("Procesar instruccion " + currentLine + "/" + totalLineas);
                    });

                    try {
                        Thread.sleep(velocidadMs);
                        String resultado = procesarLinea(linea.trim());
                        logWindow.agregarLog(resultado);
                    } catch (InterruptedException e) {
                        logWindow.agregarLog("Procesamiento interrumpido");
                        Thread.currentThread().interrupt();
                        return;
                    } catch (Exception e) {
                        logWindow.agregarLog("Error en línea: " + lineaActual + " - " + e.getMessage());
                    }
                }
                logWindow.agregarLog("Procesamiento completado.");
            } catch (IOException e) {
                logWindow.agregarLog("Error al leer el archivo: " + e.getMessage());
            }
        }).start();
    }

    private String procesarLinea(String linea) {
        return switch (extraerComando(linea)) {
            case "REGISTRO_EVENTO" -> procesarRegistroEvento(linea);
            case "REGISTRO_PARTICIPANTE" -> procesarRegistroParticipante(linea);
            case "INSCRIPCION" -> procesarInscripcion(linea);
            case "PAGO" -> procesarPago(linea);
            case "VALIDAR_INSCRIPCION" -> procesarValidacionInscripcion(linea);
            case "REGISTRO_ACTIVIDAD" -> procesarRegistroActividad(linea);
            case "ASISTENCIA" -> procesarRegistroAsistencia(linea);
            case "CERTIFICADO" -> procesarCertificado(linea);
            case "REPORTE_PARTICIPANTES" -> generarReporteParticipantes(linea);
            case "REPORTE_ACTIVIDADES" -> generarReporteActividades(linea);
            case "REPORTE_EVENTOS" -> generarReporteEventos(linea);
            default -> throw new IllegalArgumentException("Instrucción no reconocida: " + linea);
        };
    }

    // Método auxiliar para extraer el comando de la línea
    private String extraerComando(String linea) {
        int parentesisIndex = linea.indexOf('(');
        return parentesisIndex != -1 ? linea.substring(0, parentesisIndex) : linea;
    }

    private String procesarRegistroEvento(String linea) {
        // Ejemplo: REGISTRO_EVENTO("EVT-001","25/08/2025","CHARLA","Tecnología Sheikah","Auditorio Central",150,120.00);
        try {
            String[] partes = extraerParametros(linea);
            if (partes.length != 7) {
                return "Error: numero de parametros invalido en '" + linea + "'";
            }

            ArrayList<String> errores = eventoController.registrarEvento(partes);
            if (!errores.isEmpty()) {
                return "Error en registro: " + String.join(", ", errores);
            }
            return "Evento registrado: " + partes[0];
        } catch (Exception e) {
            return "Error al procesar el registro" + e.getMessage();
        }
    }

    private String procesarRegistroParticipante(String linea) {
        // Ejemplo: REGISTRO_PARTICIPANTE("Zelda Hyrule","ESTUDIANTE","Universidad de Hyrule","zelda@hyrule.edu");
        try {
            String[] partes = extraerParametros(linea);
            if (partes.length != 4) {
                return "Error: numero de parametros invalido en '" + linea + "'";
            }

            ArrayList<String> errores = participanteController.registrarParticipante(partes);
            if (!errores.isEmpty()) {
                return "Error en registro: " + String.join(", ", errores);
            }
            return "Participante registrado: " + partes[3];
        } catch (Exception e) {
            return "Error al procesar el registro " + e.getMessage();
        }
    }

    private String procesarInscripcion(String linea) {
        // Ejemplo: INSCRIPCION("zelda@hyrule.edu","EVT-001","ASISTENTE");
        try {
            String[] partes = extraerParametros(linea);
            if (partes.length != 3) {
                return "Error: numero de parametros invalido en '" + linea + "'";
            }

            ArrayList<String> errores = inscripcionController.inscribirParticipante(partes);
            if (!errores.isEmpty()) {
                return "Error en registro: " + String.join(", ", errores);
            }

            return "Inscripción registrada: " + partes[0] + " al evento " + partes[1];
        } catch (Exception e) {
            return "Error al procesar la inscripcion " + e.getMessage();
        }
    }

    private String procesarPago(String linea) {
        // Ejemplo: PAGO("zelda@hyrule.edu","EVT-00000001","EFECTIVO",50.00);
        try {
            String[] partes = extraerParametros(linea);
            if (partes.length != 4) {
                return "Error: numero de parametros invalido en '" + linea + "'";
            }

            ArrayList<String> errores = pagoController.registrarPago(partes);
            if (!errores.isEmpty()) {
                return "Error en registro: " + String.join(", ", errores);
            }

            return "Pago registrado: " + partes[0] + " al evento " + partes[1];
        } catch (Exception e) {
            return "Error al procesar el pago " + e.getMessage();
        }
    }

    private String procesarValidacionInscripcion(String linea) {
        // Ejemplo: VALIDAR_INSCRIPCION("zelda@hyrule.edu","EVT-00000001");
        try {
            String[] partes = extraerParametros(linea);
            if (partes.length != 2) {
                return "Error: numero de parametros invalido en '" + linea + "'";
            }

            ArrayList<String> errores = inscripcionController.confirmarPagoInscripcion(partes);
            if (!errores.isEmpty()) {
                return "Error en registro: " + String.join(", ", errores);
            }

            return "Inscripcion confirmada: " + partes[0] + " al evento " + partes[1];
        } catch (Exception e) {
            return "Error al procesar la confirmacion de inscripcion " + e.getMessage();
        }
    }

    private String procesarRegistroActividad(String linea) {
        // Ejemplo: REGISTRO_ACTIVIDAD("ACT-00000001","EVT-00000001","CHARLA","Taller de pociones","zelda@hyrule.edu","10:00","12:00",30);
        try {
            String[] partes = extraerParametros(linea);
            if (partes.length != 8) {
                return "Error: numero de parametros invalido en '" + linea + "'";
            }

            ArrayList<String> errores = actividadController.registrarActividad(partes);
            if (!errores.isEmpty()) {
                return "Error en registro: " + String.join(", ", errores);
            }
            return "Actividad registrada: " + partes[0];
        } catch (Exception e) {
            return "Error al procesar la actividad" + e.getMessage();
        }
    }

    private String procesarRegistroAsistencia(String linea) {
        // Ejemplo: ASISTENCIA("zelda@hyrule.edu","ACT-00000001");
        try {
            String[] partes = extraerParametros(linea);
            if (partes.length != 2) {
                return "Error: numero de parametros invalido en '" + linea + "'";
            }

            ArrayList<String> errores = asistenciaController.registrarAsistencia(partes);
            if (!errores.isEmpty()) {
                return "Error en registro: " + String.join(", ", errores);
            }

            return "Asistencia registrada: " + partes[0] + " a la actividad " + partes[1];
        } catch (Exception e) {
            return "Error al procesar el registro de asistencia " + e.getMessage();
        }
    }

    private String procesarCertificado(String linea) {
        try {
            String[] partes = extraerParametros(linea);
            if (partes.length != 2) {
                return "Error: numero de parametros invalido en '" + linea + "'";
            }

            ArrayList<String> errores = certificadoController.registrarGenerarCertificado(partes, rutaSalidaReportes);
            if (!errores.isEmpty()) {
                return "Error en registro: " + String.join(", ", errores);
            }

            return "Se registro y se genero certificado: " + partes[0] + " del evento " + partes[1];
        } catch (Exception e) {
            return "Error al procesar el registro y generacion de certificado " + e.getMessage();
        }
    }
    
    private String generarReporteParticipantes(String linea){
        try {
            String[] partes = extraerParametros(linea);
            if (partes.length < 1) return "Error: numero de parametros invalido en '" + linea + "'";
            
            ArrayList<String> errores = participanteController.generarReporte(partes, rutaSalidaReportes);
            if (!errores.isEmpty()) {
                return "Error en generacion: " + String.join(", ", errores);
            }
            
            return "Se genero reporte, del los Parcipantes del evento " + partes[0];
        } catch (Exception e) {
            return "Error al procesar la generacion de reporte de participantes " + e.getMessage();
        }
    }
    
    private String generarReporteActividades(String linea){
        try {
            String[] partes = extraerParametros(linea);
            if (partes.length < 1) return "Error: numero de parametros invalido en '" + linea + "'";
            
            ArrayList<String> errores = actividadController.generarReporte(partes, rutaSalidaReportes);
            if (!errores.isEmpty()) {
                return "Error en generacion: " + String.join(", ", errores);
            }
            
            return "Se genero reporte de las actividades " + partes[0];
        } catch (Exception e) {
            return "Error al procesar la generacion de reporte de actividades" + e.getMessage();
        }
    }
    
    private String generarReporteEventos(String linea){
        try {
            String[] partes = extraerParametros(linea);
            if (partes.length < 1) return "Error: numero de parametros invalido en '" + linea + "'";
            
            ArrayList<String> errores = eventoController.generarReporte(partes, rutaSalidaReportes);
            if (!errores.isEmpty()) {
                return "Error en registro: " + String.join(", ", errores);
            }
            
            return "Se genero reporte del Evento " + partes[0];
        } catch (Exception e) {
            return "Error al procesar la generacion de reporte de participantes " + e.getMessage();
        }
    }
    
    private static String[] extraerParametros(String linea) {
        // Extrae el contenido entre paréntesis
        String contenido = linea.substring(linea.indexOf('(') + 1, linea.lastIndexOf(')'));
        // Divide los parámetros respetando las comillas
        return Arrays.stream(contenido.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"))
                .map(param -> param.trim().replaceAll("^\"|\"$", ""))
                .toArray(String[]::new);
    }

    private int contarLineas(File archivo) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            int lineas = 0;
            while (br.readLine() != null) {
                lineas++;
            }
            return lineas;
        }
    }

}
