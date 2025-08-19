/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica1.dao;

import com.mycompany.practica1.model.Actividad;
import com.mycompany.practica1.model.TipoActividad;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Hp
 */
public class ActividadDAO {

    private ConexionDB conexionDB;

    public ActividadDAO() {
        this.conexionDB = new ConexionDB();
    }

    public void crearActividad(Actividad actividad) throws SQLException {
        String sql = "INSERT INTO actividad (codigo, tipo, titulo, hora_inicio, hora_fin, cupo_maximo, id_participante_encargado, evento_codigo_evento) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = conexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, actividad.getCodigo());
            stmt.setString(2, actividad.getTipo().name());
            stmt.setString(3, actividad.getTitulo());
            stmt.setTime(4, Time.valueOf(actividad.getHoraInicio()));
            stmt.setTime(5, Time.valueOf(actividad.getHoraFinal()));
            stmt.setInt(6, actividad.getCupoMaximo());
            stmt.setInt(7, actividad.getIdParticipanteEncargado());
            stmt.setString(8, actividad.getCodigoEvento());

            stmt.executeUpdate();
        } finally {
            conexionDB.cerrarConexion();
        }
    }

    public Actividad obtenerActividadPorCodigo(String codigo) throws SQLException {
        String sql = "SELECT * FROM actividad WHERE codigo = ?";
        Actividad actividad = null;

        try (Connection conn = conexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codigo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                actividad = new Actividad(
                        rs.getString("codigo"),
                        TipoActividad.valueOf(rs.getString("tipo")),
                        rs.getString("titulo"),
                        rs.getTime("hora_inicio").toLocalTime(),
                        rs.getTime("hora_fin").toLocalTime(),
                        rs.getInt("cupo_maximo"),
                        rs.getInt("id_participante_encargado"),
                        rs.getString("evento_codigo_evento")
                );
            }
        } finally {
            conexionDB.cerrarConexion();
        }
        return actividad;
    }

    public boolean existeCodigoActividad(String codigo) throws SQLException {
        String sql = "SELECT * FROM actividad WHERE codigo = ?";
        try (Connection conn = conexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codigo);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public List<Actividad> obtenerTodos() throws SQLException {
        String sql = "SELECT * FROM actividad";
        List<Actividad> actividades = new ArrayList<>();

        try (Connection conn = conexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Actividad actividad = new Actividad(
                        rs.getString("codigo"),
                        TipoActividad.valueOf(rs.getString("tipo")),
                        rs.getString("titulo"),
                        rs.getTime("hora_inicio").toLocalTime(),
                        rs.getTime("hora_fin").toLocalTime(),
                        rs.getInt("cupo_maximo"),
                        rs.getInt("id_participante_encargado"),
                        rs.getString("evento_codigo_evento")
                );
                actividades.add(actividad);
            }
        }
        return actividades;
    }

    public List<Actividad> obtenerActividadesPorEvento(String codigoEvento) throws SQLException {
        List<Actividad> actividades = new ArrayList<>();
        String sql = "SELECT * FROM actividad WHERE evento_codigo_evento = ?";

        try (Connection conn = conexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codigoEvento);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Actividad actividad = new Actividad(
                            rs.getString("codigo"),
                            TipoActividad.valueOf(rs.getString("tipo")),
                            rs.getString("titulo"),
                            rs.getTime("hora_inicio").toLocalTime(),
                            rs.getTime("hora_fin").toLocalTime(),
                            rs.getInt("cupo_maximo"),
                            rs.getInt("id_participante_encargado"),
                            rs.getString("evento_codigo_evento")
                    );
                    actividades.add(actividad);
                }
            }
        }
        return actividades;
    }

    public List<Actividad> obtenerActividadesParaReporte(String codigoEvento, String tipo, String emailEncargado) throws SQLException {
        String sql = "SELECT a.*, p.email as email_encargado, p.id FROM actividad a "
                + "JOIN participante p ON a.id_participante_encargado = p.id "
                + "WHERE a.evento_codigo_evento = ? "
                + (tipo.isEmpty() ? "" : "AND a.tipo = ? ")
                + (emailEncargado.isEmpty() ? "" : "AND p.email = ? ");

        try (Connection conn = conexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            int paramIndex = 1;
            stmt.setString(paramIndex++, codigoEvento);

            if (!tipo.isEmpty()) {
                stmt.setString(paramIndex++, tipo);
            }
            if (!emailEncargado.isEmpty()) {
                stmt.setString(paramIndex++, emailEncargado);
            }

            List<Actividad> actividades = new ArrayList<>();
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Actividad a = new Actividad(
                            rs.getString("codigo"),
                            TipoActividad.valueOf(rs.getString("tipo")),
                            rs.getString("titulo"),
                            rs.getTime("hora_inicio").toLocalTime(),
                            rs.getTime("hora_fin").toLocalTime(),
                            rs.getInt("cupo_maximo"),
                            rs.getInt("id"),
                            codigoEvento
                    );
                    a.setEmailParticipanteEncargado(emailEncargado);
                    actividades.add(a);
                }
            }
            return actividades;
        }
    }
}
