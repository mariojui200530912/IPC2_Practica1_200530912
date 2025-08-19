/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica1.ui;

import com.mycompany.practica1.controller.ParticipanteController;
import com.mycompany.practica1.dao.EventoDAO;
import com.mycompany.practica1.model.Evento;
import com.mycompany.practica1.model.TipoParticipante;
import java.util.List;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.*;

/**
 *
 * @author Hp
 */
public class ReporteParticipantesInternalFrame extends ReporteBaseInternalFrame {
    private JComboBox<String> cbEventos;
    private JComboBox<TipoParticipante> cbTipoParticipante;
    private JTextField txtInstitucion;
    private ParticipanteController participanteController;
    
    public ReporteParticipantesInternalFrame() {
        super("Reporte de Participantes");
        this.participanteController = new ParticipanteController();
        initComponents();
    }
    
    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Evento (obligatorio)
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Evento *:"), gbc);
        gbc.gridx = 1;
        cbEventos = new JComboBox<>(cargarEventos());
        panel.add(cbEventos, gbc);
        
        // Tipo de participante (opcional)
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Tipo de participante:"), gbc);
        gbc.gridx = 1;
        cbTipoParticipante = new JComboBox<>(TipoParticipante.values());
        cbTipoParticipante.insertItemAt(null, 0); // Opción "Todos"
        cbTipoParticipante.setSelectedIndex(0);
        panel.add(cbTipoParticipante, gbc);
        
        // Institución (opcional)
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Institución:"), gbc);
        gbc.gridx = 1;
        txtInstitucion = new JTextField(20);
        panel.add(txtInstitucion, gbc);
        
        // Ruta de salida
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(crearPanelRuta(), gbc);
        
        // Botones
        gbc.gridx = 0; gbc.gridy = 4;
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
    
    private String[] cargarEventos() {
        try {
            EventoDAO eventoDAO = new EventoDAO();
            List<Evento> eventos = eventoDAO.obtenerTodos();
            return eventos.stream()
                .map(Evento::getCodigo)
                .toArray(String[]::new);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar eventos: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            return new String[0];
        }
    }
    
    private void generarReporte() {
        if (!validarCampos()) return;
        
        String codigoEvento = (String) cbEventos.getSelectedItem();
        if (codigoEvento == null || codigoEvento.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un evento", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        TipoParticipante tipo = (TipoParticipante) cbTipoParticipante.getSelectedItem();
        String institucion = txtInstitucion.getText().trim();
        String rutaSalida = txtRutaSalida.getText();
        
        try {
            // Preparar datos para el controlador
            String[] datos = {
                codigoEvento,
                tipo != null ? tipo.name() : "", 
                institucion
            };
            
            // Usar el método existente del controlador
            ArrayList<String> resultado = participanteController.generarReporte(datos, rutaSalida);
            
            if (resultado.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Reporte generado exitosamente en: " + rutaSalida, 
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
}
