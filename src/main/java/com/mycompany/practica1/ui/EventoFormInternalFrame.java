/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica1.ui;

import com.mycompany.practica1.controller.EventoController;
import com.mycompany.practica1.model.TipoEvento;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;

public class EventoFormInternalFrame extends JInternalFrame {

    private JTextField txtCodigo;
    private JTextField txtFecha;
    private JComboBox<TipoEvento> cbTipo;
    private JTextField txtTitulo;
    private JTextField txtUbicacion;
    private JSpinner spnCupoMaximo;
    private JTextField txtCostoInscripcion;

    public EventoFormInternalFrame() {
        super("Registro de Evento", true, true, true, true);
        setSize(500, 400);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Código del evento
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Código del evento:"), gbc);
        gbc.gridx = 1;
        txtCodigo = new JTextField(20);
        panel.add(txtCodigo, gbc);

        // Fecha del evento
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Fecha (dd/mm/aaaa):"), gbc);
        gbc.gridx = 1;
        txtFecha = new JTextField(20);
        panel.add(txtFecha, gbc);

        // Tipo de evento
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Tipo de evento:"), gbc);
        gbc.gridx = 1;
        cbTipo = new JComboBox<>(TipoEvento.values());
        panel.add(cbTipo, gbc);

        // Título del evento
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Título del evento:"), gbc);
        gbc.gridx = 1;
        txtTitulo = new JTextField(20);
        panel.add(txtTitulo, gbc);

        // Ubicación
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Ubicación:"), gbc);
        gbc.gridx = 1;
        txtUbicacion = new JTextField(20);
        panel.add(txtUbicacion, gbc);

        // Cupo máximo
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("Cupo máximo:"), gbc);
        gbc.gridx = 1;
        spnCupoMaximo = new JSpinner(new SpinnerNumberModel(50, 1, 1000, 1));
        panel.add(spnCupoMaximo, gbc);
        
        // Costo Inscripcion
        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(new JLabel("Costo Incripcion:"), gbc);
        gbc.gridx = 1;
        txtCostoInscripcion = new JTextField(20);
        panel.add(txtCostoInscripcion, gbc);

        // Botones
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.CENTER;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> guardarEvento());
        btnPanel.add(btnGuardar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        btnPanel.add(btnCancelar);

        panel.add(btnPanel, gbc);

        add(panel);
    }

    private void guardarEvento() {
        // Validar y guardar el evento
        String[] formEvento = new String[7];
        
        formEvento[0] = txtCodigo.getText().trim();
        formEvento[1] = txtFecha.getText().trim();
        formEvento[2] = cbTipo.getSelectedItem().toString();
        formEvento[3] = txtTitulo.getText().trim();
        formEvento[4] = txtUbicacion.getText().trim();
        formEvento[5] = spnCupoMaximo.getValue().toString();
        formEvento[6] = txtCostoInscripcion.getText().trim();

        EventoController controller = new EventoController();
        ArrayList<String> errores = controller.registrarEvento(formEvento);

        if (!errores.isEmpty()) {
            JOptionPane.showMessageDialog(this, String.join("\n", errores), "Error de validación", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Evento guardado con éxito ✅");
            dispose();
        }
    }
}
