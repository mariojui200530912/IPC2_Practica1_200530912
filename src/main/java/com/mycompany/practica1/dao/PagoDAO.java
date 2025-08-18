/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica1.dao;

import com.mycompany.practica1.model.Pago;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
        String sql = "INSERT INTO pago (tipo_pago, monto, evento_codigo, id_participante) VALUES (?, ?, ?, ?)";

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
}

