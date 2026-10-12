package proyecto.progra2.modelos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import proyecto.progra2.conexion.ConexionOracle;

public class GestionClientes {

    private List<Cliente> listaMemoria;

    public GestionClientes() {
        this.listaMemoria = new ArrayList<>();
        cargarDatosMemoria();
    }

    private void cargarDatosMemoria() {
        Cliente c1 = new Cliente("CF", "Consumidor Final", "Ciudad", "00000000");
        Cliente c2 = new Cliente("123456-7", "Juan Pérez", "Zona 10", "55554444");
        c2.setPuntos(20);

        Cliente c3 = new Cliente("123456-K", "Juan Pérez", "Zona 10", "55554444");
        c3.setPuntos(20);

        Cliente c4 = new Cliente("987654-3", "María López", "Zona 9", "22221111");
        c4.setPuntos(50);

        listaMemoria.add(c1);
        listaMemoria.add(c2);
        listaMemoria.add(c3);
        listaMemoria.add(c4);
    }

    // 1. Agregar cliente
    public boolean agregarCliente(Cliente cliente) {
        if (buscarPorNit(cliente.getNit()) != null) {
            return false;
        }

        String sql = "INSERT INTO CLIENTES (NIT, NOMBRE, PUNTOS) VALUES (?, ?, ?)";

        try (Connection conn = ConexionOracle.obtenerConexion()) {
            if (conn != null) {
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, cliente.getNit());
                    stmt.setString(2, cliente.getNombre());
                    stmt.setInt(3, cliente.getPuntos());

                    int filas = stmt.executeUpdate();
                    if (!conn.getAutoCommit()) conn.commit();
                    
                    listaMemoria.add(cliente);
                    return filas > 0;
                }
            }
        } catch (Exception e) {
            System.err.println("Nota Oracle: Insertando en memoria local -> " + e.getMessage());
        }

        listaMemoria.add(cliente);
        return true;
    }

    // 2. Editar cliente
    public boolean editarCliente(String nitOriginal, String nuevoNombre, String nuevaDireccion, String nuevoTelefono) {
        Cliente c = buscarMemoria(nitOriginal);
        if (c != null) {
            c.setNombre(nuevoNombre);
            c.setDireccion(nuevaDireccion);
            c.setTelefono(nuevoTelefono);
        }

        String sql = "UPDATE CLIENTES SET NOMBRE = ? WHERE UPPER(NIT) = UPPER(?)";

        try (Connection conn = ConexionOracle.obtenerConexion()) {
            if (conn != null) {
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, nuevoNombre);
                    stmt.setString(2, nitOriginal.trim());

                    int filas = stmt.executeUpdate();
                    if (!conn.getAutoCommit()) conn.commit();
                    return filas > 0;
                }
            }
        } catch (Exception e) {
            System.err.println("Nota Oracle: Modificado en memoria local.");
        }
        return c != null;
    }

    // 3. Cambiar Estado en memoria local
    public boolean cambiarEstadoCliente(String nit) {
        Cliente c = buscarMemoria(nit);
        if (c != null) {
            c.setActivo(!c.isActivo());
            return true;
        }
        return false;
    }

    // 4. Buscar por NIT (Solo usando campos confirmados NIT, NOMBRE y PUNTOS)
    public Cliente buscarPorNit(String nit) {
        if (nit == null || nit.trim().isEmpty()) return null;

        String sql = "SELECT NIT, NOMBRE, PUNTOS FROM CLIENTES WHERE UPPER(NIT) = UPPER(?)";

        try (Connection conn = ConexionOracle.obtenerConexion()) {
            if (conn != null) {
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, nit.trim());
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        String nitResp = rs.getString("NIT");
                        String nombre = rs.getString("NOMBRE");
                        int puntos = rs.getInt("PUNTOS");

                        Cliente c = new Cliente(nitResp, nombre, "Ciudad", "00000000");
                        c.setPuntos(puntos);
                        c.setActivo(true);
                        return c;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Aviso Oracle XE: " + e.getMessage());
        }

        return buscarMemoria(nit);
    }

    private Cliente buscarMemoria(String nit) {
        for (Cliente c : listaMemoria) {
            if (c.getNit().equalsIgnoreCase(nit.trim())) {
                return c;
            }
        }
        return null;
    }

    // 5. Descontar Puntos directamente en Oracle XE
    public boolean descontarPuntos(String nit, int puntosADescontar) {
        Cliente c = buscarPorNit(nit);
        if (c == null || !c.isActivo() || c.getPuntos() < puntosADescontar) {
            return false;
        }

        // Descontar en memoria local
        Cliente cMem = buscarMemoria(nit);
        if (cMem != null) {
            cMem.setPuntos(cMem.getPuntos() - puntosADescontar);
        }

        // Descontar en Oracle XE
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
            System.err.println("Aviso al descontar en Oracle XE: " + e.getMessage());
        }

        return true;
    }

    // 6. Obtener lista completa (Solo NIT, NOMBRE y PUNTOS)
    public List<Cliente> getListaClientes() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT NIT, NOMBRE, PUNTOS FROM CLIENTES";

        try (Connection conn = ConexionOracle.obtenerConexion()) {
            if (conn != null) {
                try (PreparedStatement stmt = conn.prepareStatement(sql);
                     ResultSet rs = stmt.executeQuery()) {

                    while (rs.next()) {
                        String nit = rs.getString("NIT");
                        String nombre = rs.getString("NOMBRE");
                        int puntos = rs.getInt("PUNTOS");

                        Cliente c = new Cliente(nit, nombre, "Ciudad", "00000000");
                        c.setPuntos(puntos);
                        c.setActivo(true);
                        lista.add(c);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Aviso al listar desde Oracle XE: " + e.getMessage());
        }

        if (lista.isEmpty()) {
            return listaMemoria;
        }
        return lista;
    }
}