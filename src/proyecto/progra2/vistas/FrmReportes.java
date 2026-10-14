package proyecto.progra2.vistas;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import proyecto.progra2.conexion.ConexionOracle;
import proyecto.progra2.modelos.Usuario;

public class FrmReportes extends JFrame {

    private JTable tablaReporte;
    private DefaultTableModel modeloTabla;
    private JLabel lblTotalVentas, lblTotalPedidos, lblTotalPuntosEmitidos;
    private JButton btnReporteVentas, btnReporteMasVendidos, btnReporteFidelizacion, btnRegresar;
    private Usuario usuarioSesion;

    private static final Color COLOR_PRIMARY = new Color(24, 43, 73);
    private static final Color COLOR_BG_LIGHT = new Color(245, 247, 250);

    public FrmReportes() {
        this(null);
    }

    public FrmReportes(Usuario usuario) {
        this.usuarioSesion = usuario;

        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}

        setTitle("Módulo de Reportes de Auditoría y Fidelización");
        setSize(1000, 680);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_BG_LIGHT);
        setLayout(new BorderLayout(10, 10));

        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(COLOR_PRIMARY);
        panelHeader.setBorder(new EmptyBorder(15, 20, 15, 20));

        JPanel panelTitulos = new JPanel();
        panelTitulos.setOpaque(false);
        panelTitulos.setLayout(new BoxLayout(panelTitulos, BoxLayout.Y_AXIS));

        JLabel lblTitulo = new JLabel("MÓDULO DE REPORTES Y AUDITORÍA");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Consultas consolidadas en tiempo real desde la base de datos Oracle XE");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(new Color(200, 215, 235));

        panelTitulos.add(lblTitulo);
        panelTitulos.add(Box.createRigidArea(new Dimension(0, 4)));
        panelTitulos.add(lblSub);

        btnRegresar = crearBoton("Volver al Menú", new Color(70, 85, 105), Color.WHITE);
        btnRegresar.setPreferredSize(new Dimension(140, 32));
        btnRegresar.addActionListener(e -> regresarMenu());

        panelHeader.add(panelTitulos, BorderLayout.WEST);
        panelHeader.add(btnRegresar, BorderLayout.EAST);

        add(panelHeader, BorderLayout.NORTH);

        JPanel panelControles = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        panelControles.setBackground(Color.WHITE);
        panelControles.setBorder(new CompoundBorder(new LineBorder(new Color(210, 215, 220), 1), new EmptyBorder(8, 12, 8, 12)));

        btnReporteVentas = crearBoton("1. Historial de Ventas", COLOR_PRIMARY, Color.WHITE);
        btnReporteVentas.setPreferredSize(new Dimension(170, 34));
        btnReporteVentas.addActionListener(e -> cargarReporteVentas());

        btnReporteMasVendidos = crearBoton("2. Productos Más Vendidos", new Color(40, 116, 166), Color.WHITE);
        btnReporteMasVendidos.setPreferredSize(new Dimension(190, 34));
        btnReporteMasVendidos.addActionListener(e -> cargarReporteMasVendidos());

        btnReporteFidelizacion = crearBoton("3. Puntos y Fidelización", new Color(142, 68, 173), Color.WHITE);
        btnReporteFidelizacion.setPreferredSize(new Dimension(180, 34));
        btnReporteFidelizacion.addActionListener(e -> cargarReporteFidelizacion());

        panelControles.add(btnReporteVentas);
        panelControles.add(btnReporteMasVendidos);
        panelControles.add(btnReporteFidelizacion);

        modeloTabla = new DefaultTableModel() {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tablaReporte = new JTable(modeloTabla);
        tablaReporte.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tablaReporte.setRowHeight(26);
        tablaReporte.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        JPanel panelCentro = new JPanel(new BorderLayout(5, 5));
        panelCentro.setOpaque(false);
        panelCentro.setBorder(new EmptyBorder(0, 15, 0, 15));
        panelCentro.add(panelControles, BorderLayout.NORTH);
        panelCentro.add(new JScrollPane(tablaReporte), BorderLayout.CENTER);

        add(panelCentro, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new GridLayout(1, 3, 15, 0));
        panelInferior.setOpaque(false);
        panelInferior.setBorder(new EmptyBorder(5, 15, 15, 15));

        lblTotalVentas = crearTarjetaKPI("Total Ingresos", "Q0.00", new Color(34, 153, 84));
        lblTotalPedidos = crearTarjetaKPI("Total Pedidos", "0 transacciones", COLOR_PRIMARY);
        lblTotalPuntosEmitidos = crearTarjetaKPI("Puntos Acumulados", "0 pts emitidos", new Color(142, 68, 173));

        panelInferior.add(lblTotalVentas);
        panelInferior.add(lblTotalPedidos);
        panelInferior.add(lblTotalPuntosEmitidos);

        add(panelInferior, BorderLayout.SOUTH);

        cargarReporteVentas();
    }

    private JLabel crearTarjetaKPI(String titulo, String valorInicial, Color colorTexto) {
        JLabel lbl = new JLabel("<html><div style='text-align: center;'><b>" + titulo + "</b><br><span style='font-size:14px; color:" + String.format("#%06x", (colorTexto.getRGB() & 0xFFFFFF)) + ";'>" + valorInicial + "</span></div></html>");
        lbl.setOpaque(true);
        lbl.setBackground(Color.WHITE);
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setBorder(new CompoundBorder(new LineBorder(new Color(210, 215, 220), 1), new EmptyBorder(10, 10, 10, 10)));
        return lbl;
    }

    private JButton crearBoton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void cargarReporteVentas() {
        modeloTabla.setRowCount(0);
        String[] columnas = {"ID Pedido", "NIT Cliente", "Nombre Cliente", "Fecha / Hora", "Método Pago", "Total (Q)", "Puntos Ganados"};
        modeloTabla.setDataVector(null, columnas);

        String sql = "SELECT p.ID_PEDIDO, p.NIT_CLIENTE, NVL(c.NOMBRE, 'Consumidor Final') AS CLIENTE, " +
                     "TO_CHAR(p.FECHA, 'DD/MM/YYYY HH24:MI') AS FECHA_FMT, p.METODO_PAGO, p.TOTAL, p.PUNTOS_GANADOS " +
                     "FROM PEDIDOS p LEFT JOIN CLIENTES c ON p.NIT_CLIENTE = c.NIT ORDER BY p.ID_PEDIDO DESC";

        double sumaTotal = 0;
        int contadorPedidos = 0;
        int sumaPuntos = 0;

        try (Connection conn = ConexionOracle.obtenerConexion()) {
            if (conn != null) {
                try (PreparedStatement stmt = conn.prepareStatement(sql);
                     ResultSet rs = stmt.executeQuery()) {

                    while (rs.next()) {
                        int id = rs.getInt("ID_PEDIDO");
                        String nit = rs.getString("NIT_CLIENTE");
                        String cliente = rs.getString("CLIENTE");
                        String fecha = rs.getString("FECHA_FMT");
                        String pago = rs.getString("METODO_PAGO");
                        double total = rs.getDouble("TOTAL");
                        int puntos = rs.getInt("PUNTOS_GANADOS");

                        sumaTotal += total;
                        sumaPuntos += puntos;
                        contadorPedidos++;

                        modeloTabla.addRow(new Object[]{
                            "PED-" + String.format("%04d", id),
                            nit,
                            cliente,
                            fecha != null ? fecha : "N/A",
                            pago,
                            String.format("Q%.2f", total),
                            "+" + puntos + " pts"
                        });
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar reporte de ventas desde Oracle XE: " + e.getMessage());
        }

        actualizarKPI("Q" + String.format("%.2f", sumaTotal), contadorPedidos + " pedidos", sumaPuntos + " pts emitidos");
    }

    private void cargarReporteMasVendidos() {
        modeloTabla.setRowCount(0);
        String[] columnas = {"ID Producto", "Nombre Producto", "Categoría", "Unidades Vendidas", "Total Recaudado (Q)"};
        modeloTabla.setDataVector(null, columnas);

        String sql = "SELECT p.ID_PRODUCTO, p.NOMBRE, p.CATEGORIA, SUM(d.CANTIDAD) AS TOTAL_UNIDADES, SUM(d.SUBTOTAL) AS TOTAL_VENTA " +
                     "FROM DETALLE_PEDIDOS d JOIN PRODUCTOS p ON d.ID_PRODUCTO = p.ID_PRODUCTO " +
                     "GROUP BY p.ID_PRODUCTO, p.NOMBRE, p.CATEGORIA ORDER BY TOTAL_UNIDADES DESC";

        double sumaTotal = 0;
        int totalUnidades = 0;

        try (Connection conn = ConexionOracle.obtenerConexion()) {
            if (conn != null) {
                try (PreparedStatement stmt = conn.prepareStatement(sql);
                     ResultSet rs = stmt.executeQuery()) {

                    while (rs.next()) {
                        int id = rs.getInt("ID_PRODUCTO");
                        String nombre = rs.getString("NOMBRE");
                        String cat = rs.getString("CATEGORIA");
                        int unidades = rs.getInt("TOTAL_UNIDADES");
                        double venta = rs.getDouble("TOTAL_VENTA");

                        sumaTotal += venta;
                        totalUnidades += unidades;

                        modeloTabla.addRow(new Object[]{
                            "PROD-" + String.format("%03d", id),
                            nombre,
                            cat,
                            unidades + " uds",
                            String.format("Q%.2f", venta)
                        });
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar reporte de productos desde Oracle XE: " + e.getMessage());
        }

        actualizarKPI("Q" + String.format("%.2f", sumaTotal), totalUnidades + " unidades vendidas", "Categorías en Oracle XE");
    }

    private void cargarReporteFidelizacion() {
        modeloTabla.setRowCount(0);
        String[] columnas = {"NIT / DPI", "Nombre Cliente", "Puntos Acumulados", "Estado", "Nivel Fidelidad"};
        modeloTabla.setDataVector(null, columnas);

        String sql = "SELECT NIT, NOMBRE, PUNTOS, ESTADO FROM CLIENTES ORDER BY PUNTOS DESC";

        int totalPuntosSuma = 0;
        int clientesActivos = 0;

        try (Connection conn = ConexionOracle.obtenerConexion()) {
            if (conn != null) {
                try (PreparedStatement stmt = conn.prepareStatement(sql);
                     ResultSet rs = stmt.executeQuery()) {

                    while (rs.next()) {
                        String nit = rs.getString("NIT");
                        String nombre = rs.getString("NOMBRE");
                        int puntos = rs.getInt("PUNTOS");
                        boolean activo = rs.getInt("ESTADO") == 1;

                        totalPuntosSuma += puntos;
                        if (activo) clientesActivos++;

                        String nivel = "Bronce";
                        if (puntos >= 50) nivel = "Oro ***";
                        else if (puntos >= 30) nivel = "Plata **";
                        else if (puntos >= 10) nivel = "Bronce *";

                        modeloTabla.addRow(new Object[]{
                            nit,
                            nombre,
                            puntos + " pts",
                            activo ? "Activo" : "Inactivo",
                            nivel
                        });
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al recargar reporte de fidelización desde Oracle XE: " + e.getMessage());
        }

        actualizarKPI("Padrón de Clientes", clientesActivos + " clientes activos", totalPuntosSuma + " pts acumulados");
    }

    private void actualizarKPI(String kpi1, String kpi2, String kpi3) {
        lblTotalVentas.setText("<html><div style='text-align: center;'><b>Métrica Principal</b><br><span style='font-size:14px; color:#229954;'>" + kpi1 + "</span></div></html>");
        lblTotalPedidos.setText("<html><div style='text-align: center;'><b>Volumen Operativo</b><br><span style='font-size:14px; color:#182B49;'>" + kpi2 + "</span></div></html>");
        lblTotalPuntosEmitidos.setText("<html><div style='text-align: center;'><b>Puntos Totales</b><br><span style='font-size:14px; color:#8E44AD;'>" + kpi3 + "</span></div></html>");
    }

    private void regresarMenu() {
        this.dispose();
        new FrmPrincipal(usuarioSesion).setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmReportes(null).setVisible(true));
    }
}