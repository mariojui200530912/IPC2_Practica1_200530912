package com.mycompany.practica1.ui;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import com.mycompany.practica1.controller.ParticipanteController;
import com.mycompany.practica1.model.TipoParticipante;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
/**
 *
 * @author Hp
 */
public class ParticipanteFormInternalFrame extends JInternalFrame{
    private JTextField txtNombre;
    private JComboBox<TipoParticipante> cbTipo;
    private JTextField txtInstitucion;
    private JTextField txtEmail;

    public ParticipanteFormInternalFrame() {
        super("Registro de Participante", true, true, true, true);
        setSize(500, 350);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Nombre completo
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Nombre completo:"), gbc);
        gbc.gridx = 1;
        txtNombre = new JTextField(20);
        panel.add(txtNombre, gbc);

        // Tipo de participante
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Tipo de participante:"), gbc);
        gbc.gridx = 1;
        cbTipo = new JComboBox<>(TipoParticipante.values());
        panel.add(cbTipo, gbc);

        // Institución de procedencia
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Institución:"), gbc);
        gbc.gridx = 1;
        txtInstitucion = new JTextField(20);
        panel.add(txtInstitucion, gbc);

        // Correo electrónico
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Correo electrónico:"), gbc);
        gbc.gridx = 1;
        txtEmail = new JTextField(20);
        panel.add(txtEmail, gbc);

        // Botones
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.CENTER;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> guardarParticipante());
        btnPanel.add(btnGuardar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        btnPanel.add(btnCancelar);

        panel.add(btnPanel, gbc);

        add(panel);
    }

    private void guardarParticipante() {
        // Obtener datos del formulario
        String[] formParticipante = new String[4];
        formParticipante[0] = txtNombre.getText().trim();
        formParticipante[1] = cbTipo.getSelectedItem().toString();
        formParticipante[2] = txtInstitucion.getText().trim();
        formParticipante[3] = txtEmail.getText().trim();

        // Aquí necesitarías tener un ParticipanteController similar al de Evento
        ParticipanteController controller = new ParticipanteController();
        ArrayList<String> errores = controller.registrarParticipante(formParticipante);

        if (!errores.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                String.join("\n", errores), 
                "Error de validación", 
                JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Participante registrado con éxito ✅", 
                "Éxito", 
                JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }
    }
}
