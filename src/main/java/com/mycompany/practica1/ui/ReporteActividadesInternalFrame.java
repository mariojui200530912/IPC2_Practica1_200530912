/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica1.ui;

import com.mycompany.practica1.controller.ActividadController;
import com.mycompany.practica1.dao.EventoDAO;
import com.mycompany.practica1.model.Evento;
import com.mycompany.practica1.model.TipoActividad;
import java.util.List;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

/**
 *
 * @author Hp
 */
public class ReporteActividadesInternalFrame extends ReporteBaseInternalFrame {

    private JComboBox<String> cbEventos;
    private JComboBox<TipoActividad> cbTipoActividad;
    private JTextField txtEmailEncargado;
    private ActividadController actividadController;

    public ReporteActividadesInternalFrame() {
        super("Reporte de Actividades");
        this.actividadController = new ActividadController();
        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Evento (obligatorio)
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Evento *:"), gbc);
        gbc.gridx = 1;
        cbEventos = new JComboBox<>(cargarEventos());
        panel.add(cbEventos, gbc);

        // Tipo de actividad (opcional)
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Tipo de actividad:"), gbc);
        gbc.gridx = 1;
        cbTipoActividad = new JComboBox<>(TipoActividad.values());
        cbTipoActividad.insertItemAt(null, 0);
        cbTipoActividad.setSelectedIndex(0);
        panel.add(cbTipoActividad, gbc);

        // Email del encargado (opcional)
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Email del encargado:"), gbc);
        gbc.gridx = 1;
        txtEmailEncargado = new JTextField(20);
        panel.add(txtEmailEncargado, gbc);

        // Ruta de salida
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(crearPanelRuta(), gbc);

        // Botones
        gbc.gridx = 0;
        gbc.gridy = 4;
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
        if (!validarCampos()) {
            return;
        }

        String codigoEvento = (String) cbEventos.getSelectedItem();
        if (codigoEvento == null || codigoEvento.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un evento",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        TipoActividad tipo = (TipoActividad) cbTipoActividad.getSelectedItem();
        String emailEncargado = txtEmailEncargado.getText().trim();
        String rutaSalida = txtRutaSalida.getText();

        try {
            String[] datos = {
                codigoEvento,
                tipo != null ? tipo.name() : "",
                emailEncargado
            };

            ArrayList<String> resultado = actividadController.generarReporte(datos, rutaSalida);

            if (resultado.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Reporte de actividades generado exitosamente",
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

    private DefaultComboBoxModel<String> cargarEventos() {
        DefaultComboBoxModel<String> modelo = new DefaultComboBoxModel<>();
        try {
            EventoDAO eventoDAO = new EventoDAO();
            List<Evento> eventos = eventoDAO.obtenerTodos(); // tu método DAO que lista todos los eventos
            for (Evento evento : eventos) {
                modelo.addElement(evento.getCodigo());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar los eventos: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        return modelo;
    }

}
