package proyecto.progra2.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionOracle {

    // Configuración de conexión para Oracle XE 21c
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:XE"; // O "jdbc:oracle:thin:@localhost:1521/XEPDB1"
    private static final String USUARIO = "system"; // Tu usuario de Oracle (ej: system, admin, o el usuario creado)
    private static final String CLAVE = "Impor2026";    // Reemplaza por la contraseña que configuraste en Oracle XE

    public static Connection obtenerConexion() {
        Connection conn = null;
        try {
            // Cargar explícitamente el driver de Oracle
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection(URL, USUARIO, CLAVE);
        } catch (ClassNotFoundException e) {
            System.err.println("Error: Driver JDBC de Oracle no encontrado en las Librerias del proyecto.");
        } catch (SQLException e) {
            System.err.println("Error de conexion a Oracle XE: " + e.getMessage());
        }
        return conn;
    }
}
