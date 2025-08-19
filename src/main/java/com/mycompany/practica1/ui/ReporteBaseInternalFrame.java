/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica1.ui;

import java.awt.*;
import javax.swing.JInternalFrame;
import javax.swing.*;

/**
 *
 * @author Hp
 */
public abstract class ReporteBaseInternalFrame extends JInternalFrame {
    protected JTextField txtRutaSalida;
    
    public ReporteBaseInternalFrame(String title) {
        super(title, true, true, true, true);
        setSize(500, 400);
    }
    
    protected JPanel crearPanelRuta() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Ruta de salida"));
        
        txtRutaSalida = new JTextField();
        JButton btnBuscar = new JButton("Examinar...");
        btnBuscar.addActionListener(e -> seleccionarDirectorio());
        
        panel.add(txtRutaSalida, BorderLayout.CENTER);
        panel.add(btnBuscar, BorderLayout.EAST);
        
        return panel;
    }
    
    protected void seleccionarDirectorio() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            txtRutaSalida.setText(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }
    
    protected boolean validarCampos() {
        if (txtRutaSalida.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una ruta de salida", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}