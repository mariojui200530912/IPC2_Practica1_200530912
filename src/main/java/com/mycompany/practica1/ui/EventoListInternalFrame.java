/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica1.ui;

import com.mycompany.practica1.controller.EventoController;
import com.mycompany.practica1.model.Evento;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class EventoListInternalFrame extends JInternalFrame {
    private JTable tablaEventos;
    private EventoController eventoController;

    public EventoListInternalFrame() {
        super("Lista de Eventos", true, true, true, true);
        setSize(800, 600);
        
        eventoController = new EventoController();
        
        // Modelo de tabla
        String[] columnas = {"Código", "Fecha", "Tipo", "Título", "Ubicación", "Cupo", "Costo"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        tablaEventos = new JTable(modelo);
        tablaEventos.setAutoCreateRowSorter(true);
        tablaEventos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        cargarEventos();
        
        JScrollPane scrollPane = new JScrollPane(tablaEventos);
        add(scrollPane, BorderLayout.CENTER);
        
        // Panel de botones
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(e -> cargarEventos());
        btnPanel.add(btnActualizar);
        
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        btnPanel.add(btnCerrar);
        
        add(btnPanel, BorderLayout.SOUTH);
    }

    private void cargarEventos() {
        DefaultTableModel modelo = (DefaultTableModel) tablaEventos.getModel();
        modelo.setRowCount(0);
        
        try{
            List<Evento> eventos = eventoController.obtenerTodosEventos();
            
            for (Evento e : eventos) {
                modelo.addRow(new Object[]{
                    e.getCodigo(),
                    e.getFecha(),
                    e.getTipoEvento(),
                    e.getTituloEvento(),
                    e.getUbicacion(),
                    e.getCupoMaximo(),
                    e.getCostoInscripcion()
                });
            }
            
            // Actualizar estadísticas en la barra de estado si es necesario
            JOptionPane.showMessageDialog(this, 
                "Datos cargados: " + eventos.size() + " eventos", 
                "Información", 
                JOptionPane.INFORMATION_MESSAGE);
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, 
                "Error al cargar eventos " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
