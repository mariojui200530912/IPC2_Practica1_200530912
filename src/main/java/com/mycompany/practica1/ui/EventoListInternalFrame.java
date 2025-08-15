/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica1.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class EventoListInternalFrame extends JInternalFrame {
    private JTable tablaEventos;

    public EventoListInternalFrame() {
        super("Lista de Eventos", true, true, true, true);
        setSize(800, 600);
        
        // Modelo de tabla
        String[] columnas = {"Código", "Fecha", "Tipo", "Título", "Ubicación", "Cupo"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);
        
        // Aquí deberías cargar los datos reales de la base de datos
        // Esto es solo un ejemplo con datos dummy
        modelo.addRow(new Object[]{"EVT-001", "25/08/2023", "CHARLA", "Tecnología Sheikah", "Auditorio Central", 150});
        modelo.addRow(new Object[]{"EVT-002", "30/08/2023", "TALLER", "Taller de Pociones", "Laboratorio 3", 30});
        
        tablaEventos = new JTable(modelo);
        tablaEventos.setAutoCreateRowSorter(true);
        
        JScrollPane scrollPane = new JScrollPane(tablaEventos);
        add(scrollPane, BorderLayout.CENTER);
        
        // Panel de botones
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(e -> actualizarLista());
        btnPanel.add(btnActualizar);
        
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        btnPanel.add(btnCerrar);
        
        add(btnPanel, BorderLayout.SOUTH);
    }

    private void actualizarLista() {
        // Lógica para actualizar la lista desde la base de datos
        JOptionPane.showMessageDialog(this, "Lista actualizada");
    }
}
