/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica1.util;

import com.mycompany.practica1.dao.EventoDAO;
import com.mycompany.practica1.model.Evento;
import com.mycompany.practica1.model.TipoEvento;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;
import javax.swing.SwingUtilities;

/**
 *
 * @author Hp
 */
public class FileProcessor {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private int velocidadMs;
    private String rutaSalidaReportes;
    private ProgressMonitor monitor;

    public FileProcessor(int velocidadMs, String rutaSalidaReportes, ProgressMonitor monitor) {
        this.velocidadMs = velocidadMs;
        this.rutaSalidaReportes = rutaSalidaReportes;
        this.monitor = monitor;
    }

    public List<String> procesarArchivo(File archivo) {
        List<String> resultados = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            int totalLineas = contarLineas(archivo);
            int lineaActual = 0;
            String linea;

            while ((linea = br.readLine()) != null) {
                lineaActual++;
                actualizarProgreso(lineaActual, totalLineas);

                try {
                    Thread.sleep(velocidadMs);
                    String resultado = procesarLinea(linea.trim());
                    resultados.add(resultado);
                } catch (InterruptedException e) {
                    manejarInterrupcion(resultados);
                    return resultados;
                } catch (IllegalArgumentException e) {
                    resultados.add("Error en línea: " + lineaActual + " - " + e.getMessage());
                }

                if (monitor.isCanceled()) {
                    resultados.add("Procesamiento cancelado por el usuario");
                    return resultados;
                }
            }
        } catch (IOException e) {
            resultados.add("Error al leer el archivo: " + e.getMessage());
        }

        return resultados;
    }

    private static String procesarLinea(String linea) {
        if (linea.startsWith("REGISTRO_EVENTO")) {
            return procesarRegistroEvento(linea);
        } else if (linea.startsWith("REGISTRO_PARTICIPANTE")) {
            // return procesarRegistroParticipante(linea);
        } else if (linea.startsWith("INSCRIPCION")) {
            // return procesarInscripcion(linea);
        }
        // ... otros tipos de instrucciones

        throw new IllegalArgumentException("Instrucción no reconocida");
    }

    private static String procesarRegistroEvento(String linea) {
        // Ejemplo: REGISTRO_EVENTO("EVT-001","25/08/2025","CHARLA","Tecnología Sheikah","Auditorio Central",150);
        try {
            String[] partes = extraerParametros(linea);
            if (partes.length != 7) {
                return "Error: numero de parametros invalido en '" + linea + "'";
            }
            String codigo = partes[0].trim();
            LocalDate fecha = LocalDate.parse(partes[1], DATE_FORMATTER);
            TipoEvento tipo = TipoEvento.valueOf(partes[2]);
            String titulo = partes[3];
            String ubicacion = partes[4];
            int cupoMaximo = Integer.parseInt(partes[5]);
            float precio = Float.parseFloat(partes[6]);

            Evento evento = new Evento(codigo, fecha, tipo, titulo, ubicacion, cupoMaximo, precio);

            EventoDAO guardarEvento = new EventoDAO();

            guardarEvento.crearEvento(evento);

            return "Evento registrado: " + codigo;
        } catch (Exception e) {
            return "Error al procesar el registro" + e.getMessage();
        }
    }

    /*
    private static String procesarRegistroParticipante(String linea) {
        // Ejemplo: REGISTRO_PARTICIPANTE("Zelda Hyrule","ESTUDIANTE","Universidad de Hyrule","zelda@hyrule.edu");
        String[] partes = extraerParametros(linea);

        String nombre = partes[0];
        TipoParticipante tipo = TipoParticipante.valueOf(partes[1]);
        String institucion = partes[2];
        String email = partes[3];

        Participante participante = new Participante(nombre, tipo, institucion, email);

        // Aquí llamarías al ParticipanteDAO para guardar
        // participanteDAO.guardar(participante);
        return "Participante registrado: " + email;
    }

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
    private void actualizarProgreso(int actual, int total) {
        if (monitor != null) {
            SwingUtilities.invokeLater(() -> {
                monitor.setNote("Procesando linea " + actual + " de " + total);
                monitor.setProgress((actual * 100) / total);
            });
        }
    }

    private void manejarInterrupcion(List<String> resultados) {
        Thread.currentThread().interrupt();
        resultados.add("Procesamiento Interrumpido");
        if (monitor != null) {
            monitor.close();
        }
    }

    private static String[] extraerParametros(String linea) {
        // Extrae el contenido entre paréntesis
        String contenido = linea.substring(linea.indexOf('(') + 1, linea.lastIndexOf(')'));
        // Divide los parámetros respetando las comillas
        return contenido.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
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
