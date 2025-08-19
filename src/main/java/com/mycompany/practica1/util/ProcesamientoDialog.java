/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica1.util;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;


/**
 *
 * @author Hp
 */
public class ProcesamientoDialog extends JDialog {
    private JSpinner spinnerVelocidad;
    private JTextField txtRutaSalida;
    private JTextField txtArchivoInstrucciones;
    private boolean confirmado = false;
    private File archivoSeleccionado;
    
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
        
        panel.add(new JLabel("Archivo de instrucciones:"));
        JPanel panelArchivo = new JPanel(new BorderLayout());
        txtArchivoInstrucciones = new JTextField();
        txtArchivoInstrucciones.setEditable(false);
        JButton btnBuscarArchivo = new JButton("...");
        btnBuscarArchivo.addActionListener(e -> seleccionarArchivo());
        panelArchivo.add(txtArchivoInstrucciones, BorderLayout.CENTER);
        panelArchivo.add(btnBuscarArchivo, BorderLayout.EAST);
        panel.add(panelArchivo);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAceptar = new JButton("Aceptar");
        btnAceptar.addActionListener(e -> {
            if (validarCampos()) {
                confirmado = true;
                dispose();
            }
        });
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        panelBotones.add(btnCancelar);
        panelBotones.add(btnAceptar);
        
        add(panel, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void seleccionarDirectorio() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setDialogTitle("Seleccionar directorio de salida");
        
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            txtRutaSalida.setText(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void seleccionarArchivo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar archivo de instrucciones");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos de texto (.txt)", "txt"));
        
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            archivoSeleccionado = fileChooser.getSelectedFile();
            txtArchivoInstrucciones.setText(archivoSeleccionado.getAbsolutePath());
        }
    }

    private boolean validarCampos() {
        if (archivoSeleccionado == null || !archivoSeleccionado.exists()) {
            JOptionPane.showMessageDialog(this, 
                "Debe seleccionar un archivo de instrucciones válido", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (txtRutaSalida.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Debe especificar una ruta de salida para los reportes", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }

    public boolean mostrar() {
        setVisible(true);
        return confirmado;
    }

    public int getVelocidad() {
        return (int) spinnerVelocidad.getValue();
    }

    public String getRutaSalidaReportes() {
        return txtRutaSalida.getText();
    }

    public File getArchivoInstrucciones() {
        return archivoSeleccionado;
    }
}