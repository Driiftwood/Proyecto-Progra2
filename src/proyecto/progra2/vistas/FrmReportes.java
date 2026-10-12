package proyecto.progra2.vistas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import proyecto.progra2.modelos.*;

public class FrmReportes extends JFrame {

    private JTabbedPane tabbedPane;
    private JButton btnRegresar;

    private GestionClientes gestionClientes;
    private GestionInventario gestionInventario;
    private Usuario usuarioSesion;

    private static final Color COLOR_PRIMARY = new Color(24, 43, 73);      
    private static final Color COLOR_BG_LIGHT = new Color(245, 247, 250);  

    public FrmReportes() {
        this(null);
    }

    public FrmReportes(Usuario usuario) {
        this.usuarioSesion = usuario;
        gestionClientes = new GestionClientes();
        gestionInventario = new GestionInventario();

        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}

        setTitle("Módulo de Reportes del Sistema");
        setSize(980, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_BG_LIGHT);
        setLayout(new BorderLayout(10, 10));

        // --- Header ---
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(COLOR_PRIMARY);
        panelHeader.setBorder(new EmptyBorder(15, 20, 15, 20));

        JPanel panelTitulos = new JPanel();
        panelTitulos.setOpaque(false);
        panelTitulos.setLayout(new BoxLayout(panelTitulos, BoxLayout.Y_AXIS));

        JLabel lblTitulo = new JLabel("MÓDULO DE REPORTES Y ESTADÍSTICAS");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Consultas consolidadas de ventas, inventario y programa de fidelización");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(new Color(200, 215, 235));

        panelTitulos.add(lblTitulo);
        panelTitulos.add(Box.createRigidArea(new Dimension(0, 4)));
        panelTitulos.add(lblSub);

        btnRegresar = new JButton("Volver al Menú");
        btnRegresar.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnRegresar.setBackground(new Color(70, 85, 105));
        btnRegresar.setForeground(Color.WHITE);
        btnRegresar.setFocusPainted(false);
        btnRegresar.setOpaque(true);
        btnRegresar.setContentAreaFilled(true);
        btnRegresar.setBorderPainted(false);
        btnRegresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegresar.setPreferredSize(new Dimension(140, 32));
        btnRegresar.addActionListener(e -> {
            this.dispose();
            new FrmPrincipal(usuarioSesion).setVisible(true);
        });

        panelHeader.add(panelTitulos, BorderLayout.WEST);
        panelHeader.add(btnRegresar, BorderLayout.EAST);

        add(panelHeader, BorderLayout.NORTH);

        // --- Pestañas de Reportes ---
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 12));

        tabbedPane.addTab("1. Ventas por Período", crearReporteVentasPeriodo());
        tabbedPane.addTab("2. Productos Más Vendidos", crearReporteMasVendidos());
        tabbedPane.addTab("3. Bajo Inventario", crearReporteBajoInventario());
        tabbedPane.addTab("4. Puntos por Cliente", crearReportePuntosClientes());
        tabbedPane.addTab("5. Historial de Canjes", crearReporteCanjes());
        tabbedPane.addTab("6. Ventas de Menús", crearReporteMenus());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel crearReporteVentasPeriodo() {
        String[] col = {"Fecha", "Monto Total", "Pago Efectivo", "Pago Tarjeta"};
        DefaultTableModel model = new DefaultTableModel(col, 0);
        model.addRow(new Object[]{"09/10/2026", "Q104.00", "Q104.00", "Q0.00"});
        model.addRow(new Object[]{"08/10/2026", "Q250.00", "Q100.00", "Q150.00"});
        return crearPanelTabla(model);
    }

    private JPanel crearReporteMasVendidos() {
        String[] col = {"Código", "Producto", "Categoría", "Unidades Vendidas"};
        DefaultTableModel model = new DefaultTableModel(col, 0);
        model.addRow(new Object[]{"P01", "Sándwich de Pollo", "SANDWICH", "24"});
        model.addRow(new Object[]{"M01", "Menú Completo de Pollo", "MENÚ", "18"});
        model.addRow(new Object[]{"P03", "Gaseosa 500ml", "BEBIDA", "15"});
        return crearPanelTabla(model);
    }

    private JPanel crearReporteBajoInventario() {
        String[] col = {"Código", "Producto", "Categoría", "Existencia Actual", "Estado"};
        DefaultTableModel model = new DefaultTableModel(col, 0);

        for (ProductoIndividual p : gestionInventario.getListaProductos()) {
            if (p.getExistencia() <= 5) {
                model.addRow(new Object[]{p.getCodigo(), p.getNombre(), p.getCategoria(), p.getExistencia(), p.getExistencia() == 0 ? "AGOTADO" : "BAJO STOCK"});
            }
        }
        if (model.getRowCount() == 0) {
            model.addRow(new Object[]{"P02", "Sándwich de Jamón (Ejemplo)", "SANDWICH", "4", "BAJO STOCK"});
        }

        return crearPanelTabla(model);
    }

    private JPanel crearReportePuntosClientes() {
        String[] col = {"NIT / DPI", "Nombre del Cliente", "Puntos Acumulados", "Estado"};
        DefaultTableModel model = new DefaultTableModel(col, 0);

        for (Cliente c : gestionClientes.getListaClientes()) {
            model.addRow(new Object[]{c.getNit(), c.getNombre(), c.getPuntos() + " pts", c.isActivo() ? "Activo" : "Inactivo"});
        }

        return crearPanelTabla(model);
    }

    private JPanel crearReporteCanjes() {
        String[] col = {"Cliente", "Fecha", "Recompensa Canjeada", "Puntos Utilizados"};
        DefaultTableModel model = new DefaultTableModel(col, 0);
        model.addRow(new Object[]{"Juan Pérez", "09/10/2026", "Bebida Gratuita", "-10 pts"});
        model.addRow(new Object[]{"María López", "07/10/2026", "Sándwich Individual", "-30 pts"});
        return crearPanelTabla(model);
    }

    private JPanel crearReporteMenus() {
        String[] col = {"Código Menú", "Nombre del Menú", "Cantidad Vendida", "Importe Generado"};
        DefaultTableModel model = new DefaultTableModel(col, 0);
        model.addRow(new Object[]{"M01", "Menú Completo de Pollo", "12", "Q444.00"});
        return crearPanelTabla(model);
    }

    private JPanel crearPanelTabla(DefaultTableModel model) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(new EmptyBorder(10, 10, 10, 10));
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        p.add(new JScrollPane(table), BorderLayout.CENTER);
        return p;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmReportes(null).setVisible(true));
    }
}
