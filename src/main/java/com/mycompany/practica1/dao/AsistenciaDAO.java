/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica1.dao;

import com.mycompany.practica1.model.Asistencia;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Hp
 */
public class AsistenciaDAO {

    private ConexionDB conexionDB;

    public AsistenciaDAO() {
        this.conexionDB = new ConexionDB();
    }

    public void registrarAsistencia(Asistencia asistencia) throws SQLException {
        String sql = "INSERT INTO asistencia (codigo_actividad, id_participante) VALUES (?, ?)";

        try (Connection conn = conexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, asistencia.getCodigoActividad());
            stmt.setInt(2, asistencia.getIdParticipante());

            stmt.executeUpdate();
        } finally {
            conexionDB.cerrarConexion();
        }
    }

    public boolean existeAsistencia(Asistencia asistencia) throws SQLException {
        String sql = "SELECT * FROM asistencia WHERE codigo_actividad = ? AND id_participante =  ?";
        try (Connection conn = conexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, asistencia.getCodigoActividad());
            stmt.setInt(2, asistencia.getIdParticipante());
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean verificarAsistencia(String codigoActividad, int idParticipante) throws SQLException {
        String sql = "SELECT * FROM asistencia WHERE codigo_actividad = ? AND id_participante =  ?";
        try (Connection conn = conexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codigoActividad);
            stmt.setInt(2, idParticipante);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }
}
