/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica1.util;

import com.mycompany.practica1.controller.EventoController;
import com.mycompany.practica1.controller.ParticipanteController;
import com.mycompany.practica1.model.Participante;
import com.mycompany.practica1.model.TipoParticipante;
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
    private LogWindow logWindow;

    public FileProcessor(int velocidadMs, String rutaSalidaReportes, LogWindow logWindow) {
        this.velocidadMs = velocidadMs;
        this.rutaSalidaReportes = rutaSalidaReportes;
        this.logWindow = logWindow;
        this.eventoController = new EventoController();
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

        if (linea.startsWith("REGISTRO_EVENTO")) {
            return procesarRegistroEvento(linea);
        } else if (linea.startsWith("REGISTRO_PARTICIPANTE")) {
            return procesarRegistroParticipante(linea);
        } else if (linea.startsWith("INSCRIPCION")) {
            // return procesarInscripcion(linea);
        }
        // ... otros tipos de instrucciones

        throw new IllegalArgumentException("Instrucción no reconocida");
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

    /*
    private static String procesarInscripcion(String linea) {
        // Ejemplo: INSCRIPCION("zelda@hyrule.edu","EVT-001","ASISTENTE");
        String[] partes = extraerParametros(linea);

        String email = partes[0];
        String codigoEvento = partes[1];
        TipoInscripcion tipo = TipoInscripcion.valueOf(partes[2]);

        // Aquí implementarías la lógica de inscripción
        // 1. Buscar participante por email
        // 2. Buscar evento por código
        // 3. Crear inscripción
        return "Inscripción registrada: " + email + " al evento " + codigoEvento;
    }
    
    private String procesarReporte(String linea){
        if(rutaSalidaReportes == null || rutaSalidaReportes.isEmpty()){
        throw new IllegalArgumentException("Ruta de salida para reportes no configurada");
        }
        File directorio = new File(rutaSalidaReportes);
        if(!directorio.exists()){
            directorio.mkdirs();
        }
    
        if(linea.startsWith("REPORTE_PARTICIPANTES")){
            return generarReporteParticipantes(linea);
        }else if(linea.startsWith("REPORTE_ACTIVIDADES")){
            return generarReporteActividades(linea);
        }else if(linea.startsWith("REPORTE_EVENTOS"){
            return generarReporteEventos(linea);
        }
        throw new IllegalArgumentException("Tipo de reporte no reconocido");
    }
     */
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
