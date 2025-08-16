/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica1.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author Hp
 */
public class ConexionDB {
    private Connection conexion;
    private String url;
    private String usuario;
    private String contrasena;
    
    public ConexionDB(){
        cargarConfiguracion();
        conectar();
    }
    
    
    // Carga la configuración desde el archivo properties
    private void cargarConfiguracion() {
        Properties prop = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if(input == null){
                throw new RuntimeException("No se encontro el archivo config.properties");
            }
            prop.load(input);
            this.url = "jdbc:mysql://" + 
                      prop.getProperty("db.host") + ":" + 
                      prop.getProperty("db.port") + "/" + 
                      prop.getProperty("db.name") +
                      "?useSSL=false&serverTimezone=UTC";
            this.usuario = prop.getProperty("db.user");
            this.contrasena = prop.getProperty("db.password");
            
            // Cargar el driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar configuración de la base de datos: " + e.getMessage());
            throw new RuntimeException("Error en configuración de BD", e);
        }
    }

    // Establece la conexión con la base de datos
    private void conectar() {
        try {
            this.conexion = DriverManager.getConnection(url, usuario, contrasena);
            System.out.println("Conexión establecida con éxito");
        } catch (SQLException e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
            throw new RuntimeException("Error al conectar a BD", e);
        }
    }

    // Obtiene la conexión activa
    public Connection getConexion() {
        try {
            if (this.conexion == null || this.conexion.isClosed()) {
                conectar(); // Reconectar si es necesario
            }
            return this.conexion;
        } catch (SQLException e) {
            System.err.println("Error al verificar conexión: " + e.getMessage());
            throw new RuntimeException("Error al obtener conexión", e);
        }
    }

    // Cierra la conexión
    public void cerrarConexion() {
        if (this.conexion != null) {
            try {
                this.conexion.close();
                System.out.println("Conexión cerrada con éxito");
            } catch (SQLException e) {
                System.err.println("Error al cerrar conexión: " + e.getMessage());
            }
        }
    }

    // Método para probar la conexión
    public boolean probarConexion() {
        try (Connection conn = getConexion()) {
            return conn.isValid(2); // Prueba de 2 segundos
        } catch (SQLException e) {
            System.err.println("Error al probar conexión: " + e.getMessage());
            return false;
        }
    }
    
}
