package proyecto.progra2.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionOracle {
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String USUARIO = "pan_puntos";
    private static final String PASSWORD = "admin";

    public static Connection obtenerConexion() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            return DriverManager.getConnection(URL, USUARIO, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Nota: Trabajando en modo local (sin BD activa): " + e.getMessage());
            return null;
        }
    }
}
