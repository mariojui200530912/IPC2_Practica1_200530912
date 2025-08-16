/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica1.ui;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.*;
import javax.swing.SwingUtilities;

/**
 *
 * @author Hp
 */
public class LogWindow extends JInternalFrame{
    private JTextArea logArea;
    private JScrollPane scrollPane;
    
    public LogWindow(){
        super("Procesamiento de Archivo", true, true, true, true);
        setSize(600, 400);
        initComponents();
    }
    
    private void initComponents(){
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        scrollPane = new JScrollPane(logArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        getContentPane().add(scrollPane, BorderLayout.CENTER);
    }
    
    public void agregarLog(String mensaje){
        SwingUtilities.invokeLater(() -> {
            logArea.append(mensaje + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }
    
    public void limpiarLog(){
        SwingUtilities.invokeLater(() -> {
            logArea.setText("");
        });
    }
}
