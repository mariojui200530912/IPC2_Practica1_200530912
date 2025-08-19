/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica1.dao;

import com.mycompany.practica1.model.Pago;
import com.mycompany.practica1.model.TipoPago;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Hp
 */
public class PagoDAO {
    private ConexionDB conexionDB;
    
    public PagoDAO() {
        this.conexionDB = new ConexionDB();
    }

    public void registrarPago(Pago pago) throws SQLException {
        String sql = "INSERT INTO pago (tipo_pago, monto, inscripcion_codigo_evento, inscripcion_id_participante) VALUES (?, ?, ?, ?)";

        try (Connection conn = conexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, pago.getTipo().name());
            stmt.setFloat(2, pago.getMonto());
            stmt.setString(3, pago.getCodigoEvento());
            stmt.setInt(4, pago.getIdParticipante());

            stmt.executeUpdate();
        } finally {
            conexionDB.cerrarConexion();
        }
    }
    
    public Pago obtenerPago(int idParticipante, String codigoEvento) throws SQLException {
        String sql = "SELECT * FROM pago WHERE inscripcion_codigo_evento = ? AND inscripcion_id_participante = ?";
        Pago pago = null;

        try (Connection conn = conexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, codigoEvento);
            stmt.setInt(2, idParticipante);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                pago = new Pago(
                        TipoPago.valueOf(rs.getString("tipo_pago")),
                        rs.getFloat("monto"),
                        rs.getString("inscripcion_codigo_evento"),
                        rs.getInt("inscripcion_id_participante")
                );
            }
        } finally {
            conexionDB.cerrarConexion();
        }
        return pago;
    }    
}

