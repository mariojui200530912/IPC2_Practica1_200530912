/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica1.dao;

import com.mycompany.practica1.model.Evento;
import com.mycompany.practica1.model.TipoEvento;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Hp
 */
public class EventoDAO {

    private ConexionDB conexionDB;
    
    public EventoDAO() {
        this.conexionDB = new ConexionDB();
    }
    
    public void crearEvento(Evento evento) throws SQLException {
        String sql = "INSERT INTO evento (codigo, fecha, tipo, titulo_evento, ubicacion, cupo_maximo, costo_inscripcion) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = conexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, evento.getCodigo());
            stmt.setDate(2, Date.valueOf(evento.getFecha()));
            stmt.setString(3, evento.getTipoEvento().name());
            stmt.setString(4, evento.getTituloEvento());
            stmt.setString(5, evento.getUbicacion());
            stmt.setInt(6, evento.getCupoMaximo());
            stmt.setFloat(7, evento.getCostoInscripcion());
            
            stmt.executeUpdate();
        } finally {
            conexionDB.cerrarConexion();
        }
    }
    
    public Evento obtenerEventoPorCodigo(String codigo) throws SQLException {
        String sql = "SELECT * FROM evento WHERE codigo = ?";
        Evento evento = null;
        
        try (Connection conn = conexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, codigo);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                evento = new Evento(
                        rs.getString("codigo"),
                        rs.getDate("fecha").toLocalDate(),
                        TipoEvento.valueOf(rs.getString("tipo")),
                        rs.getString("titulo_evento"),
                        rs.getString("ubicacion"),
                        rs.getInt("cupo_maximo"),
                        rs.getFloat("costo_inscripcion")
                );
            }
        } finally {
            conexionDB.cerrarConexion();
        }
        
        return evento;
    }
    
    public boolean existeCodigoEvento(String codigo) throws SQLException {
        String sql = "SELECT * FROM evento WHERE codigo = ?";
        try (Connection conn = conexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, codigo);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }        
    }

}
