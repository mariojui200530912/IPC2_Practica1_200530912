/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica1.dao;

import com.mycompany.practica1.model.Inscripcion;
import com.mycompany.practica1.model.Participante;
import com.mycompany.practica1.model.TipoEstatus;
import com.mycompany.practica1.model.TipoInscripcion;
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
public class InscripcionDAO {

    private ConexionDB conexionDB;

    public InscripcionDAO() {
        this.conexionDB = new ConexionDB();
    }

    public void registrarIncripcion(Inscripcion inscripcion) throws SQLException {
        String sql = "INSERT INTO inscripcion (codigo_evento, id_participante, tipo, estatus) VALUES (?, ?, ?, ?)";

        try (Connection conn = conexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, inscripcion.getCodigoEvento());
            stmt.setInt(2, inscripcion.getIdParticipante());
            stmt.setString(3, inscripcion.getTipoInscripcion().name());
            stmt.setString(4, inscripcion.getEstatus().name());

            stmt.executeUpdate();
        } finally {
            conexionDB.cerrarConexion();
        }
    }

    public Inscripcion obtenerInscripcion(String codigo, int idParticipante) throws SQLException {
        String sql = "SELECT * FROM inscripcion WHERE codigo_evento = ? AND id_participante = ?";
        Inscripcion inscripcion = null;

        try (Connection conn = conexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codigo);
            stmt.setInt(2, idParticipante);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                inscripcion = new Inscripcion(
                        rs.getString("codigo_evento"),
                        rs.getInt("id_participante"),
                        TipoInscripcion.valueOf(rs.getString("tipo")),
                        TipoEstatus.valueOf(rs.getString("estatus"))
                );
            }
        } finally {
            conexionDB.cerrarConexion();
        }

        return inscripcion;
    }

    public boolean actualizarEstadoInscripcion(String codigo, int idParticipante, TipoEstatus nuevoEstatus) throws SQLException {
        String sql = "UPDATE inscripcion SET estatus = ? WHERE codigo_evento = ? AND id_participante = ?";

        try (Connection conn = conexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nuevoEstatus.name());
            stmt.setString(2, codigo);
            stmt.setInt(3, idParticipante);

            int filasActualizadas = stmt.executeUpdate();
            return filasActualizadas > 0;
        } finally {
            conexionDB.cerrarConexion();
        }
    }

    public int contarInscritos(String codigoEvento) throws SQLException {
        String sql = "SELECT COUNT(*) AS total FROM inscripcion WHERE codigo_evento = ?";

        try (Connection conn = conexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codigoEvento);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        }
        return 0;
    }
}
