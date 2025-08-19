/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica1.ui;

import com.mycompany.practica1.controller.EventoController;
import com.mycompany.practica1.model.TipoEvento;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.*;

/**
 *
 * @author Hp
 */
public class ReporteEventosInternalFrame extends ReporteBaseInternalFrame {
    private JComboBox<TipoEvento> cbTipoEvento;
    private JTextField txtFechaInicio;
    private JTextField txtFechaFin;
    private JSpinner spnCupoMin;
    private JSpinner spnCupoMax;
    private EventoController eventoController;
    
    public ReporteEventosInternalFrame() {
        super("Reporte de Eventos");
        this.eventoController = new EventoController();
        initComponents();
    }
    
    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Tipo de evento (opcional)
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Tipo de evento:"), gbc);
        gbc.gridx = 1;
        cbTipoEvento = new JComboBox<>(TipoEvento.values());
        cbTipoEvento.insertItemAt(null, 0);
        cbTipoEvento.setSelectedIndex(0);
        panel.add(cbTipoEvento, gbc);
        
        // Fecha inicio (opcional, pero debe ir con fecha fin)
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Fecha inicio (dd/mm/aaaa):"), gbc);
        gbc.gridx = 1;
        txtFechaInicio = new JTextField(10);
        panel.add(txtFechaInicio, gbc);
        
        // Fecha fin (opcional, pero debe ir con fecha inicio)
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Fecha fin (dd/mm/aaaa):"), gbc);
        gbc.gridx = 1;
        txtFechaFin = new JTextField(10);
        panel.add(txtFechaFin, gbc);
        
        // Cupo mínimo (opcional, pero debe ir con cupo máximo)
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Cupo mínimo:"), gbc);
        gbc.gridx = 1;
        spnCupoMin = new JSpinner(new SpinnerNumberModel(0, 0, 10000, 1));
        panel.add(spnCupoMin, gbc);
        
        // Cupo máximo (opcional, pero debe ir con cupo mínimo)
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Cupo máximo:"), gbc);
        gbc.gridx = 1;
        spnCupoMax = new JSpinner(new SpinnerNumberModel(1000, 0, 10000, 1));
        panel.add(spnCupoMax, gbc);
        
        // Ruta de salida
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(crearPanelRuta(), gbc);
        
        // Botones
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.CENTER;
        JPanel btnPanel = new JPanel(new FlowLayout());
        
        JButton btnGenerar = new JButton("Generar Reporte");
        btnGenerar.addActionListener(e -> generarReporte());
        btnPanel.add(btnGenerar);
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        btnPanel.add(btnCancelar);
        
        panel.add(btnPanel, gbc);
        
        add(panel);
    }
    
    private void generarReporte() {
        if (!validarCampos()) return;
        if (!validarFechas()) return;
        if (!validarCupos()) return;
        
        TipoEvento tipo = (TipoEvento) cbTipoEvento.getSelectedItem();
        String fechaInicio = txtFechaInicio.getText().trim();
        String fechaFin = txtFechaFin.getText().trim();
        String cupoMin = spnCupoMin.getValue().toString();
        String cupoMax = spnCupoMax.getValue().toString();
        String rutaSalida = txtRutaSalida.getText();
        
        try {
            String[] datos = {
                tipo != null ? tipo.name() : "",
                fechaInicio,
                fechaFin,
                cupoMin,
                cupoMax
            };
            
            ArrayList<String> resultado = eventoController.generarReporte(datos, rutaSalida);
            
            if (resultado.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Reporte de eventos generado exitosamente", 
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, 
                    String.join("\n", resultado), 
                    "Errores", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private boolean validarFechas() {
        String inicio = txtFechaInicio.getText().trim();
        String fin = txtFechaFin.getText().trim();
        
        if ((!inicio.isEmpty() && fin.isEmpty()) || (inicio.isEmpty() && !fin.isEmpty())) {
            JOptionPane.showMessageDialog(this, 
                "Debe especificar ambas fechas (inicio y fin) o ninguna", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Validar formato de fechas si están presentes
        if (!inicio.isEmpty()) {
            try {
                LocalDate.parse(inicio, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                LocalDate.parse(fin, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Formato de fecha inválido. Use dd/mm/aaaa", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        
        return true;
    }
    
    private boolean validarCupos() {
        int min = (Integer) spnCupoMin.getValue();
        int max = (Integer) spnCupoMax.getValue();
        
        if (min > max) {
            JOptionPane.showMessageDialog(this, 
                "El cupo mínimo no puede ser mayor al máximo", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
