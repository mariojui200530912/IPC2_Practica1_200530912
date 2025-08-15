/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.practica1;

import com.mycompany.practica1.ui.MainFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 *
 * @author Hp
 */
public class Practica1 {

    public static void main(String[] args) {
       SwingUtilities.invokeLater(() -> {
            try {
                // Establecer el look and feel del sistema para mejor integración
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.err.println("Error al establecer el look and feel: " + e.getMessage());
            }

            // Crear y mostrar la ventana principal
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
            
            // Opcional: Mostrar mensaje de inicio
            System.out.println("Aplicación iniciada correctamente");
        });
    
    }
}
