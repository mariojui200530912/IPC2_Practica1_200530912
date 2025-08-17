/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica1.dao;

import com.mycompany.practica1.model.Inscripcion;
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
}
