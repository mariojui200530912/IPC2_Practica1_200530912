/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica1.ui;

import com.mycompany.practica1.controller.EventoController;
import com.mycompany.practica1.util.FileProcessor;
import com.mycompany.practica1.util.ProcesamientoDialog;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Hp
 */
public class MainFrame extends JFrame {

    private JDesktopPane desktopPane;
    private LogWindow logWindow;

    public MainFrame() {
        super("Sistema de Gestion de Eventos - Triforce Software");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1024, 768);
        setLocationRelativeTo(null);

        // Configurar el JDesktopPane
        desktopPane = new JDesktopPane();
        setContentPane(desktopPane);

        // Crear barra de menú
        crearBarraMenu();

        // Mostrar ventana de bienvenida
        mostrarVentanaBienvenida();
    }

    private void crearBarraMenu() {
        JMenuBar menuBar = new JMenuBar();

        // Menú Archivo
        JMenu menuArchivo = new JMenu("Archivo");
        JMenuItem itemCargarArchivo = new JMenuItem("Cargar archivo de instrucciones");
        itemCargarArchivo.addActionListener(this::cargarArchivoInstrucciones);
        menuArchivo.add(itemCargarArchivo);
        menuArchivo.addSeparator();
        JMenuItem itemSalir = new JMenuItem("Salir");
        itemSalir.addActionListener(e -> System.exit(0));
        menuArchivo.add(itemSalir);

        // Menú Eventos
        JMenu menuEventos = new JMenu("Eventos");
        JMenuItem itemNuevoEvento = new JMenuItem("Registrar nuevo evento");
        itemNuevoEvento.addActionListener(this::mostrarFormularioEvento);
        menuEventos.add(itemNuevoEvento);
        JMenuItem itemListarEventos = new JMenuItem("Listar eventos");
        itemListarEventos.addActionListener(this::mostrarListaEventos);
        menuEventos.add(itemListarEventos);
        
        // Menú Participantes
        JMenu menuParticipantes = new JMenu("Participantes");
        JMenuItem itemNuevoParticipante = new JMenuItem("Registrar nuevo participante");
        itemNuevoParticipante.addActionListener(this::mostrarFormularioParticipante);
        menuParticipantes.add(itemNuevoParticipante);
        JMenuItem itemListarParticipantes = new JMenuItem("Listar participantes");
        itemListarParticipantes.addActionListener(this::mostrarListaParticipantes);
        menuParticipantes.add(itemListarParticipantes);
        /*
        // Menú Reportes
        JMenu menuReportes = new JMenu("Reportes");
        JMenuItem itemReporteParticipantes = new JMenuItem("Reporte de participantes");
        itemReporteParticipantes.addActionListener(this::generarReporteParticipantes);
        menuReportes.add(itemReporteParticipantes);
        JMenuItem itemReporteActividades = new JMenuItem("Reporte de actividades");
        itemReporteActividades.addActionListener(this::generarReporteActividades);
        menuReportes.add(itemReporteActividades);
        JMenuItem itemReporteEventos = new JMenuItem("Reporte de eventos");
        itemReporteEventos.addActionListener(this::generarReporteEventos);
        menuReportes.add(itemReporteEventos);
         */
        // Agregar menús a la barra
        menuBar.add(menuArchivo);
        menuBar.add(menuEventos);
        menuBar.add(menuParticipantes);
        // menuBar.add(menuReportes);

        setJMenuBar(menuBar);
    }

    private void mostrarVentanaBienvenida() {
        JInternalFrame welcomeFrame = new JInternalFrame("Bienvenido", true, true, true, true);
        welcomeFrame.setSize(500, 400);
        JPanel panelCentral = new JPanel(new GridBagLayout());

        JLabel lblWelcome = new JLabel("<html><div style='text-align:center;'>"
                + "<h1>Sistema de Gestión de Eventos</h1>"
                + "<p>Triforce Software</p>"
                + "<p>Reino de Hyrule</p>"
                + "</div></html>"
        );

        panelCentral.add(lblWelcome, new GridBagConstraints());

        welcomeFrame.add(panelCentral);
        centrarVentanaInterna(welcomeFrame);
        desktopPane.add(welcomeFrame);
        welcomeFrame.setVisible(true);
    }

    private void centrarVentanaInterna(JInternalFrame frame) {
        SwingUtilities.invokeLater(() -> {
            Dimension desktopSize = desktopPane.getSize();
            Dimension frameSize = frame.getSize();
            frame.setLocation((desktopSize.width - frameSize.width) / 2,
                    (desktopSize.height - frameSize.height) / 2
            );
        });
    }

    // Métodos de acción (implementar lógica real)
    private void cargarArchivoInstrucciones(ActionEvent e) {
        ProcesamientoDialog configDialog = new ProcesamientoDialog(this);
        if (!configDialog.mostrar()) {
            return;
        }

        if (logWindow == null) {
            logWindow = new LogWindow();
            desktopPane.add(logWindow);
            centrarVentanaInterna(logWindow);
            logWindow.setVisible(true);
        }

        // Lógica para procesar archivo
        FileProcessor processor = new FileProcessor(
                configDialog.getVelocidad(),
                configDialog.getRutaSalidaReportes(),
                logWindow
        );

        processor.procesarArchivo(configDialog.getArchivoInstrucciones());
    }

    private void mostrarResultadosProcesamiento(List<String> resultados) {
        JTextArea textArea = new JTextArea(15, 25);
        textArea.setEditable(false);
        textArea.setText(String.join("\n", resultados));

        JScrollPane scrollPane = new JScrollPane(textArea);
        JOptionPane.showMessageDialog(
                this,
                scrollPane,
                "Resultados del Procesamiento",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void mostrarFormularioEvento(ActionEvent e) {
        EventoFormInternalFrame form = new EventoFormInternalFrame();
        centrarVentanaInterna(form);
        desktopPane.add(form);
        form.setVisible(true);
    }

    private void mostrarListaEventos(ActionEvent e) {
        EventoListInternalFrame listFrame = new EventoListInternalFrame();
        centrarVentanaInterna(listFrame);
        desktopPane.add(listFrame);
        listFrame.setVisible(true);
    }

    
    private void mostrarFormularioParticipante(ActionEvent e) {
        ParticipanteFormInternalFrame form = new ParticipanteFormInternalFrame();
        centrarVentanaInterna(form);
        desktopPane.add(form);
        form.setVisible(true);
    }

    private void mostrarListaParticipantes(ActionEvent e) {
        ParticipanteListInternalFrame listFrame = new ParticipanteListInternalFrame();
        centrarVentanaInterna(listFrame);
        desktopPane.add(listFrame);
        listFrame.setVisible(true);
    }
    
    private void generarReporteParticipantes(ActionEvent e) {
        // Implementar generación de reporte
        JOptionPane.showMessageDialog(this, "Reporte de participantes generado");
    }

    private void generarReporteActividades(ActionEvent e) {
        // Implementar generación de reporte
        JOptionPane.showMessageDialog(this, "Reporte de actividades generado");
    }

    private void generarReporteEventos(ActionEvent e) {
        // Implementar generación de reporte
        JOptionPane.showMessageDialog(this, "Reporte de eventos generado");
    }
}
