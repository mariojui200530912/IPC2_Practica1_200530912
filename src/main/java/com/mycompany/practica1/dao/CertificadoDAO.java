/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica1.dao;

import com.mycompany.practica1.model.Certificado;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Hp
 */
public class CertificadoDAO {
    private ConexionDB conexionDB;

    public CertificadoDAO() {
        this.conexionDB = new ConexionDB();
    }

    public void registrarCertificado(Certificado certificado) throws SQLException {
        String sql = "INSERT INTO certificado (id_participante, codigo_evento) VALUES (?, ?)";

        try (Connection conn = conexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, certificado.getIdParticipante());
            stmt.setString(2, certificado.getCodigoEvento());
            
            stmt.executeUpdate();
        } finally {
            conexionDB.cerrarConexion();
        }
    }

   public boolean existeCertificado(Certificado certificado) throws SQLException {
        String sql = "SELECT * FROM asistencia WHERE codigo_evento = ? AND id_participante =  ?";
        try (Connection conn = conexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, certificado.getCodigoEvento());
            stmt.setInt(2, certificado.getIdParticipante());
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }        
    }
}
