/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica1.ui;

import com.mycompany.practica1.controller.ParticipanteController;
import com.mycompany.practica1.model.Participante;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
/**
 *
 * @author Hp
 */
public class ParticipanteListInternalFrame extends JInternalFrame{
    private JTable tablaParticipantes;
    private ParticipanteController participanteController;

    public ParticipanteListInternalFrame() {
        super("Lista de Participantes", true, true, true, true);
        setSize(800, 600);
        
        // Inicializar controlador
        participanteController = new ParticipanteController();
        
        // Configurar tabla
        String[] columnas = {"Nombre", "Tipo", "Institución", "Email"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer que la tabla no sea editable
            }
        };
        
        tablaParticipantes = new JTable(modelo);
        tablaParticipantes.setAutoCreateRowSorter(true);
        tablaParticipantes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Cargar datos iniciales
        cargarParticipantes();
        
        JScrollPane scrollPane = new JScrollPane(tablaParticipantes);
        add(scrollPane, BorderLayout.CENTER);
        
        // Panel de botones
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(e -> cargarParticipantes());
        btnPanel.add(btnActualizar);
       
        
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        btnPanel.add(btnCerrar);
        
        add(btnPanel, BorderLayout.SOUTH);
    }

    private void cargarParticipantes() {
        DefaultTableModel modelo = (DefaultTableModel) tablaParticipantes.getModel();
        modelo.setRowCount(0); // Limpiar tabla
        
        try {
            List<Participante> participantes = participanteController.obtenerTodosParticipantes();
            
            for (Participante p : participantes) {
                modelo.addRow(new Object[]{
                    p.getNombre(),
                    p.getTipoParticipante().toString(),
                    p.getInstitucionProcedencia(),
                    p.getEmail()
                });
            }
            
            // Actualizar estadísticas en la barra de estado si es necesario
            JOptionPane.showMessageDialog(this, 
                "Datos cargados: " + participantes.size() + " participantes", 
                "Información", 
                JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al cargar participantes: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

}
