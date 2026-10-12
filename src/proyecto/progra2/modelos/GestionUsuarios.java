package proyecto.progra2.modelos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import proyecto.progra2.conexion.ConexionOracle;

public class GestionUsuarios {

    public GestionUsuarios() {
        // Se gestiona dinámicamente mediante consultas a Oracle XE
    }

    public Usuario autenticar(String user, String pass) {
        String sql = "SELECT USERNAME, PASSWORD, NOMBRE, ROL, ESTADO FROM USUARIOS " +
                     "WHERE LOWER(USERNAME) = LOWER(?) AND PASSWORD = ? AND ESTADO = 1";

        try (Connection conn = ConexionOracle.obtenerConexion()) {
            if (conn != null) {
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, user.trim());
                    stmt.setString(2, pass.trim());

                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            Usuario u = new Usuario(
                                rs.getString("USERNAME"),
                                rs.getString("PASSWORD"),
                                rs.getString("NOMBRE"),
                                rs.getString("ROL")
                            );
                            u.setActivo(rs.getInt("ESTADO") == 1);
                            return u;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error de autenticación en Oracle XE: " + e.getMessage());
        }
        return null;
    }

    public boolean agregarUsuario(Usuario u) {
        if (buscarPorUsername(u.getUsername()) != null) return false;

        String sql = "INSERT INTO USUARIOS (ID_USUARIO, USERNAME, PASSWORD, NOMBRE, ROL, ESTADO) " +
                     "VALUES (SEQ_USUARIOS.NEXTVAL, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionOracle.obtenerConexion()) {
            if (conn != null) {
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, u.getUsername().trim());
                    stmt.setString(2, u.getPassword().trim());
                    stmt.setString(3, u.getNombre().trim());
                    stmt.setString(4, u.getRol().trim());
                    stmt.setInt(5, u.isActivo() ? 1 : 0);

                    int filas = stmt.executeUpdate();
                    if (!conn.getAutoCommit()) conn.commit();
                    return filas > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al agregar usuario en Oracle XE: " + e.getMessage());
        }
        return false;
    }

    public boolean editarUsuario(String usernameOriginal, String nuevoNombre, String nuevoRol) {
        String sql = "UPDATE USUARIOS SET NOMBRE = ?, ROL = ? WHERE LOWER(USERNAME) = LOWER(?)";

        try (Connection conn = ConexionOracle.obtenerConexion()) {
            if (conn != null) {
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, nuevoNombre.trim());
                    stmt.setString(2, nuevoRol.trim());
                    stmt.setString(3, usernameOriginal.trim());

                    int filas = stmt.executeUpdate();
                    if (!conn.getAutoCommit()) conn.commit();
                    return filas > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al editar usuario en Oracle XE: " + e.getMessage());
        }
        return false;
    }

    public boolean cambiarPassword(String username, String nuevaPassword) {
        String sql = "UPDATE USUARIOS SET PASSWORD = ? WHERE LOWER(USERNAME) = LOWER(?)";

        try (Connection conn = ConexionOracle.obtenerConexion()) {
            if (conn != null) {
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, nuevaPassword.trim());
                    stmt.setString(2, username.trim());

                    int filas = stmt.executeUpdate();
                    if (!conn.getAutoCommit()) conn.commit();
                    return filas > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al cambiar contraseña en Oracle XE: " + e.getMessage());
        }
        return false;
    }

    public boolean cambiarEstadoUsuario(String username) {
        String sql = "UPDATE USUARIOS SET ESTADO = CASE WHEN ESTADO = 1 THEN 0 ELSE 1 END " +
                     "WHERE LOWER(USERNAME) = LOWER(?)";

        try (Connection conn = ConexionOracle.obtenerConexion()) {
            if (conn != null) {
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, username.trim());

                    int filas = stmt.executeUpdate();
                    if (!conn.getAutoCommit()) conn.commit();
                    return filas > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al cambiar estado de usuario en Oracle XE: " + e.getMessage());
        }
        return false;
    }

    public Usuario buscarPorUsername(String username) {
        if (username == null || username.trim().isEmpty()) return null;

        String sql = "SELECT USERNAME, PASSWORD, NOMBRE, ROL, ESTADO FROM USUARIOS " +
                     "WHERE LOWER(USERNAME) = LOWER(?)";

        try (Connection conn = ConexionOracle.obtenerConexion()) {
            if (conn != null) {
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, username.trim());

                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            Usuario u = new Usuario(
                                rs.getString("USERNAME"),
                                rs.getString("PASSWORD"),
                                rs.getString("NOMBRE"),
                                rs.getString("ROL")
                            );
                            u.setActivo(rs.getInt("ESTADO") == 1);
                            return u;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar usuario en Oracle XE: " + e.getMessage());
        }
        return null;
    }

    public List<Usuario> getListaUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT USERNAME, PASSWORD, NOMBRE, ROL, ESTADO FROM USUARIOS ORDER BY ID_USUARIO ASC";

        try (Connection conn = ConexionOracle.obtenerConexion()) {
            if (conn != null) {
                try (PreparedStatement stmt = conn.prepareStatement(sql);
                     ResultSet rs = stmt.executeQuery()) {

                    while (rs.next()) {
                        Usuario u = new Usuario(
                            rs.getString("USERNAME"),
                            rs.getString("PASSWORD"),
                            rs.getString("NOMBRE"),
                            rs.getString("ROL")
                        );
                        u.setActivo(rs.getInt("ESTADO") == 1);
                        lista.add(u);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar usuarios desde Oracle XE: " + e.getMessage());
        }
        return lista;
    }
}
