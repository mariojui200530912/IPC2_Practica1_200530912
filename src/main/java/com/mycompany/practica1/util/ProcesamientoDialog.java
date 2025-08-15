/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica1.util;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.*;


/**
 *
 * @author Hp
 */
public class ProcesamientoDialog extends JDialog {
    private JSpinner spinnerVelocidad;
    private JTextField txtRutaSalida;
    private boolean confirmado = false;
    
    public ProcesamientoDialog(JFrame parent){
        super(parent, "Configuracion de Procesamiento", true);
        setSize(400,200);
        setLocationRelativeTo(parent);
        
        JPanel panel = new JPanel(new GridLayout(3,2,10,10));
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        
        panel.add(new JLabel("Velocidad (ms):"));
        spinnerVelocidad = new JSpinner(new SpinnerNumberModel(500,100,5000,100));
        panel.add(spinnerVelocidad);
        
        panel.add(new JLabel("Ruta de salida:"));
        JPanel panelRuta = new JPanel(new BorderLayout());
        txtRutaSalida = new JTextField();
        JButton btnBuscar = new JButton("...");
        btnBuscar.addActionListener(e -> seleccionarDirectorio());
        panelRuta.add(txtRutaSalida, BorderLayout.CENTER);
        panelRuta.add(btnBuscar, BorderLayout.EAST);
        panel.add(panelRuta);
        
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAceptar = new JButton("Aceptar");
        btnAceptar.addActionListener(e -> {
            confirmado = true;
            dispose();
        });
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        panelBotones.add(btnCancelar);
        panelBotones.add(btnAceptar);
        
        add(panel, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }
    
    private void seleccionarDirectorio(){
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if(chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
            txtRutaSalida.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }
    
    public boolean mostrar(){
        setVisible(true);
        return confirmado;
    }
    
    public int getVelocidad(){
        return (int) spinnerVelocidad.getValue();
    }
    
    public String getRutaSalidaReportes(){
        return txtRutaSalida.getText();
    }
}
