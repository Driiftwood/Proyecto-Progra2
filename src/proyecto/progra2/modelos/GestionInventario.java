package proyecto.progra2.modelos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import proyecto.progra2.conexion.ConexionOracle;

public class GestionInventario {

    public GestionInventario() {
    }

    public boolean agregarProducto(ProductoIndividual producto) {
        String sql = "INSERT INTO PRODUCTOS (ID_PRODUCTO, NOMBRE, CATEGORIA, PRECIO, EXISTENCIA, ESTADO) " +
                     "VALUES (SEQ_PRODUCTOS.NEXTVAL, ?, ?, ?, ?, 1)";

        try (Connection conn = ConexionOracle.obtenerConexion()) {
            if (conn != null) {
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, producto.getNombre().trim());
                    stmt.setString(2, normalizarCategoria(producto.getCategoria()));
                    stmt.setDouble(3, producto.getPrecio());
                    stmt.setInt(4, producto.getExistencia());

                    int filas = stmt.executeUpdate();
                    if (!conn.getAutoCommit()) conn.commit();
                    return filas > 0;
                }
            }
        } catch (Exception e) {
            System.err.println("Error al agregar producto en Oracle XE: " + e.getMessage());
        }
        return false;
    }

    public boolean editarProducto(String codigoOriginal, String nuevoNombre, String nuevaCategoria, double nuevoPrecio) {
        String sql = "UPDATE PRODUCTOS SET NOMBRE = ?, CATEGORIA = ?, PRECIO = ? WHERE ID_PRODUCTO = ?";

        try (Connection conn = ConexionOracle.obtenerConexion()) {
            if (conn != null) {
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, nuevoNombre.trim());
                    stmt.setString(2, normalizarCategoria(nuevaCategoria));
                    stmt.setDouble(3, nuevoPrecio);
                    stmt.setInt(4, Integer.parseInt(codigoOriginal.trim().replaceAll("[^0-9]", "")));

                    int filas = stmt.executeUpdate();
                    if (!conn.getAutoCommit()) conn.commit();
                    return filas > 0;
                }
            }
        } catch (Exception e) {
            System.err.println("Error al editar producto en Oracle XE: " + e.getMessage());
        }
        return false;
    }

    public boolean eliminarProducto(String codigo) {
        String sql = "UPDATE PRODUCTOS SET ESTADO = 0 WHERE ID_PRODUCTO = ?";

        try (Connection conn = ConexionOracle.obtenerConexion()) {
            if (conn != null) {
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, Integer.parseInt(codigo.trim().replaceAll("[^0-9]", "")));

                    int filas = stmt.executeUpdate();
                    if (!conn.getAutoCommit()) conn.commit();
                    return filas > 0;
                }
            }
        } catch (Exception e) {
            System.err.println("Error al eliminar producto en Oracle XE: " + e.getMessage());
        }
        return false;
    }
    public boolean abastecerInventario(String codigo, int cantidadIngreso) {
        String sql = "UPDATE PRODUCTOS SET EXISTENCIA = EXISTENCIA + ? WHERE ID_PRODUCTO = ?";

        try (Connection conn = ConexionOracle.obtenerConexion()) {
            if (conn != null) {
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, cantidadIngreso);
                    stmt.setInt(2, Integer.parseInt(codigo.trim().replaceAll("[^0-9]", "")));

                    int filas = stmt.executeUpdate();
                    if (!conn.getAutoCommit()) conn.commit();
                    return filas > 0;
                }
            }
        } catch (Exception e) {
            System.err.println("Error al modificar existencia en Oracle XE: " + e.getMessage());
        }
        return false;
    }
    public ProductoIndividual buscarProducto(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) return null;

        String sql = "SELECT ID_PRODUCTO, NOMBRE, CATEGORIA, PRECIO, EXISTENCIA FROM PRODUCTOS " +
                     "WHERE ID_PRODUCTO = ? AND ESTADO = 1";

        try (Connection conn = ConexionOracle.obtenerConexion()) {
            if (conn != null) {
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    int id = Integer.parseInt(codigo.trim().replaceAll("[^0-9]", ""));
                    stmt.setInt(1, id);

                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            return new ProductoIndividual(
                                "P" + String.format("%02d", rs.getInt("ID_PRODUCTO")),
                                rs.getString("NOMBRE"),
                                rs.getString("CATEGORIA"),
                                rs.getDouble("PRECIO"),
                                rs.getInt("EXISTENCIA")
                            );
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error al buscar producto en Oracle XE: " + e.getMessage());
        }
        return null;
    }
    public List<ProductoIndividual> getListaProductos() {
        List<ProductoIndividual> lista = new ArrayList<>();
        String sql = "SELECT ID_PRODUCTO, NOMBRE, CATEGORIA, PRECIO, EXISTENCIA FROM PRODUCTOS " +
                     "WHERE CATEGORIA != 'Combo' AND ESTADO = 1 ORDER BY ID_PRODUCTO ASC";

        try (Connection conn = ConexionOracle.obtenerConexion()) {
            if (conn != null) {
                try (PreparedStatement stmt = conn.prepareStatement(sql);
                     ResultSet rs = stmt.executeQuery()) {

                    while (rs.next()) {
                        int id = rs.getInt("ID_PRODUCTO");
                        String codigo = "P" + String.format("%02d", id);
                        String nombre = rs.getString("NOMBRE");
                        String categoria = rs.getString("CATEGORIA");
                        double precio = rs.getDouble("PRECIO");
                        int existencia = rs.getInt("EXISTENCIA");

                        lista.add(new ProductoIndividual(codigo, nombre, categoria, precio, existencia));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error al listar inventario desde Oracle XE: " + e.getMessage());
        }
        return lista;
    }
    public List<MenuCompleto> getListaMenus() {
        List<MenuCompleto> lista = new ArrayList<>();
        String sql = "SELECT ID_PRODUCTO, NOMBRE, PRECIO FROM PRODUCTOS WHERE CATEGORIA = 'Combo' AND ESTADO = 1";

        try (Connection conn = ConexionOracle.obtenerConexion()) {
            if (conn != null) {
                try (PreparedStatement stmt = conn.prepareStatement(sql);
                     ResultSet rs = stmt.executeQuery()) {

                    while (rs.next()) {
                        int id = rs.getInt("ID_PRODUCTO");
                        String codigo = "M" + String.format("%02d", id);
                        String nombre = rs.getString("NOMBRE");
                        double precio = rs.getDouble("PRECIO");
                        ProductoIndividual pSandwich = obtenerPrimerProductoPorCategoria("Sandwich");
                        ProductoIndividual pBebida = obtenerPrimerProductoPorCategoria("Bebida");
                        ProductoIndividual pPapas = obtenerPrimerProductoPorCategoria("Acompanamiento");

                        lista.add(new MenuCompleto(codigo, nombre, precio, pSandwich, pBebida, pPapas));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error al listar combos desde Oracle XE: " + e.getMessage());
        }
        return lista;
    }

    private ProductoIndividual obtenerPrimerProductoPorCategoria(String cat) {
        String sql = "SELECT ID_PRODUCTO, NOMBRE, CATEGORIA, PRECIO, EXISTENCIA FROM PRODUCTOS " +
                     "WHERE CATEGORIA = ? AND ESTADO = 1 AND ROWNUM = 1";

        try (Connection conn = ConexionOracle.obtenerConexion()) {
            if (conn != null) {
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, cat);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            return new ProductoIndividual(
                                "P" + String.format("%02d", rs.getInt("ID_PRODUCTO")),
                                rs.getString("NOMBRE"),
                                rs.getString("CATEGORIA"),
                                rs.getDouble("PRECIO"),
                                rs.getInt("EXISTENCIA")
                            );
                        }
                    }
                }
            }
        } catch (Exception ignored) {}
        return new ProductoIndividual("P00", "Componente " + cat, cat, 0.0, 50);
    }

    private String normalizarCategoria(String cat) {
        if (cat == null) return "Sandwich";
        String c = cat.toUpperCase();
        if (c.contains("BEBIDA")) return "Bebida";
        if (c.contains("ACOMP") || c.contains("PAPA")) return "Acompanamiento";
        if (c.contains("COMBO") || c.contains("MENU")) return "Combo";
        return "Sandwich";
    }
}