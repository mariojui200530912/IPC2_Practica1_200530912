/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica1.dao;

import com.mycompany.practica1.model.Participante;
import com.mycompany.practica1.model.TipoParticipante;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Hp
 */
public class ParticipanteDAO {

    private ConexionDB conexionDB;

    public ParticipanteDAO() {
        this.conexionDB = new ConexionDB();
    }

    public void crearParticipante(Participante participante) throws SQLException {
        String sql = "INSERT INTO participante (email, nombre, tipo, institucion_procedencia) VALUES (?, ?, ?, ?)";

        try (Connection conn = conexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, participante.getEmail());
            stmt.setString(2, participante.getNombre());
            stmt.setString(3, participante.getTipoParticipante().name());
            stmt.setString(4, participante.getInstitucionProcedencia());

            stmt.executeUpdate();
        } finally {
            conexionDB.cerrarConexion();
        }
    }

    public Participante obtenerParticipantePorEmail(String email) throws SQLException {
        String sql = "SELECT * FROM participante WHERE email = ?";
        Participante participante = null;

        try (Connection conn = conexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                participante = new Participante(
                        rs.getInt("id"),
                        rs.getString("email"),
                        rs.getString("nombre"),
                        TipoParticipante.valueOf(rs.getString("tipo")),
                        rs.getString("institucion_procedencia")
                );
            }
        } finally {
            conexionDB.cerrarConexion();
        }

        return participante;
    }

    public boolean existeParticipante(String email) throws SQLException {
        String sql = "SELECT * FROM participante WHERE email = ?";
        try (Connection conn = conexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public List<Participante> obtenerTodos() throws SQLException {
        String sql = "SELECT nombre, tipo, institucion_procedencia, email FROM participante";
        List<Participante> participantes = new ArrayList<>();

        try (Connection conn = conexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Participante participante = new Participante(
                        rs.getInt("id"),
                        rs.getString("email"),
                        rs.getString("nombre"),
                        TipoParticipante.valueOf(rs.getString("tipo")),
                        rs.getString("institucion_procedencia")
                );
                participantes.add(participante);
            }
        }
        return participantes;
    }
}
