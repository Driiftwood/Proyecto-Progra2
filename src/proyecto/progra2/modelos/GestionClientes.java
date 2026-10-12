package proyecto.progra2.modelos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import proyecto.progra2.conexion.ConexionOracle;

public class GestionClientes {

    public GestionClientes() {
        // Gestión directa desde Oracle XE
    }

    // 1. Agregar cliente directamente en Oracle XE
    public boolean agregarCliente(Cliente cliente) {
        if (buscarPorNit(cliente.getNit()) != null) {
            return false;
        }

        String sql = "INSERT INTO CLIENTES (NIT, NOMBRE, PUNTOS, ESTADO) VALUES (?, ?, ?, 1)";

        try (Connection conn = ConexionOracle.obtenerConexion()) {
            if (conn != null) {
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, cliente.getNit().trim());
                    stmt.setString(2, cliente.getNombre().trim());
                    stmt.setInt(3, cliente.getPuntos());

                    int filas = stmt.executeUpdate();
                    if (!conn.getAutoCommit()) conn.commit();
                    return filas > 0;
                }
            }
        } catch (Exception e) {
            System.err.println("Error al agregar cliente en Oracle XE: " + e.getMessage());
        }

        return false;
    }

    // 2. Editar cliente en Oracle XE
    public boolean editarCliente(String nitOriginal, String nuevoNombre, String nuevaDireccion, String nuevoTelefono) {
        String sql = "UPDATE CLIENTES SET NOMBRE = ? WHERE UPPER(NIT) = UPPER(?)";

        try (Connection conn = ConexionOracle.obtenerConexion()) {
            if (conn != null) {
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, nuevoNombre.trim());
                    stmt.setString(2, nitOriginal.trim());

                    int filas = stmt.executeUpdate();
                    if (!conn.getAutoCommit()) conn.commit();
                    return filas > 0;
                }
            }
        } catch (Exception e) {
            System.err.println("Error al editar cliente en Oracle XE: " + e.getMessage());
        }
        return false;
    }

    // 3. Cambiar Estado en Oracle XE
    public boolean cambiarEstadoCliente(String nit) {
        String sql = "UPDATE CLIENTES SET ESTADO = CASE WHEN ESTADO = 1 THEN 0 ELSE 1 END WHERE UPPER(NIT) = UPPER(?)";

        try (Connection conn = ConexionOracle.obtenerConexion()) {
            if (conn != null) {
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, nit.trim());

                    int filas = stmt.executeUpdate();
                    if (!conn.getAutoCommit()) conn.commit();
                    return filas > 0;
                }
            }
        } catch (Exception e) {
            System.err.println("Error al cambiar estado en Oracle XE: " + e.getMessage());
        }
        return false;
    }

    // 4. Buscar por NIT desde Oracle XE
    public Cliente buscarPorNit(String nit) {
        if (nit == null || nit.trim().isEmpty()) return null;

        String sql = "SELECT NIT, NOMBRE, PUNTOS, ESTADO FROM CLIENTES WHERE UPPER(NIT) = UPPER(?)";

        try (Connection conn = ConexionOracle.obtenerConexion()) {
            if (conn != null) {
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, nit.trim());
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            String nitResp = rs.getString("NIT");
                            String nombre = rs.getString("NOMBRE");
                            int puntos = rs.getInt("PUNTOS");
                            boolean activo = (rs.getInt("ESTADO") == 1);

                            Cliente c = new Cliente(nitResp, nombre, "Ciudad", "00000000");
                            c.setPuntos(puntos);
                            c.setActivo(activo);
                            return c;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error al buscar cliente en Oracle XE: " + e.getMessage());
        }

        return null;
    }

    // 5. Descontar Puntos directamente en Oracle XE con COMMIT
    public boolean descontarPuntos(String nit, int puntosADescontar) {
        if (nit == null || nit.trim().isEmpty()) return false;

        String sql = "UPDATE CLIENTES SET PUNTOS = PUNTOS - ? WHERE UPPER(NIT) = UPPER(?) AND PUNTOS >= ?";

        try (Connection conn = ConexionOracle.obtenerConexion()) {
            if (conn != null) {
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, puntosADescontar);
                    stmt.setString(2, nit.trim());
                    stmt.setInt(3, puntosADescontar);

                    int filas = stmt.executeUpdate();
                    if (!conn.getAutoCommit()) conn.commit();
                    return filas > 0;
                }
            }
        } catch (Exception e) {
            System.err.println("Error al descontar puntos en Oracle XE: " + e.getMessage());
        }

        return false;
    }

    // 6. Obtener lista completa directamente desde Oracle XE
    public List<Cliente> getListaClientes() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT NIT, NOMBRE, PUNTOS, ESTADO FROM CLIENTES ORDER BY NIT ASC";

        try (Connection conn = ConexionOracle.obtenerConexion()) {
            if (conn != null) {
                try (PreparedStatement stmt = conn.prepareStatement(sql);
                     ResultSet rs = stmt.executeQuery()) {

                    while (rs.next()) {
                        String nit = rs.getString("NIT");
                        String nombre = rs.getString("NOMBRE");
                        int puntos = rs.getInt("PUNTOS");
                        boolean activo = (rs.getInt("ESTADO") == 1);

                        Cliente c = new Cliente(nit, nombre, "Ciudad", "00000000");
                        c.setPuntos(puntos);
                        c.setActivo(activo);
                        lista.add(c);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error al listar clientes desde Oracle XE: " + e.getMessage());
        }

        return lista;
    }
}