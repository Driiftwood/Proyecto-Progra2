package proyecto.progra2.vistas;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import proyecto.progra2.modelos.Usuario;

public class FrmAbout extends JFrame {

    private static final Color COLOR_PRIMARY = new Color(24, 43, 73);
    private static final Color COLOR_BG_LIGHT = new Color(245, 247, 250);
    private Usuario usuarioSesion;

    public FrmAbout(Usuario usuario) {
        this.usuarioSesion = usuario;

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        setTitle("Acerca del Sistema - Información del Grupo");
        setSize(850, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_BG_LIGHT);
        setLayout(new BorderLayout(10, 10));

        // --- HEADER / ENCABEZADO ---
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(COLOR_PRIMARY);
        panelHeader.setBorder(new EmptyBorder(15, 20, 15, 20));

        JPanel panelTitulos = new JPanel();
        panelTitulos.setOpaque(false);
        panelTitulos.setLayout(new BoxLayout(panelTitulos, BoxLayout.Y_AXIS));

        JLabel lblTitulo = new JLabel("PROYECTO FINAL - PROGRAMACIÓN II");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Sistema de Gestión de Ventas, Puntos de Lealtad y App Móvil Android");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(new Color(200, 215, 235));

        panelTitulos.add(lblTitulo);
        panelTitulos.add(Box.createRigidArea(new Dimension(0, 4)));
        panelTitulos.add(lblSub);

        JButton btnCerrar = new JButton("Volver");
        btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCerrar.setBackground(new Color(70, 85, 105));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFocusPainted(false);
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrar.addActionListener(e -> dispose());

        panelHeader.add(panelTitulos, BorderLayout.WEST);
        panelHeader.add(btnCerrar, BorderLayout.EAST);

        add(panelHeader, BorderLayout.NORTH);

        // --- CONTENIDO PRINCIPAL ---
        JPanel panelCenter = new JPanel(new BorderLayout(10, 10));
        panelCenter.setOpaque(false);
        panelCenter.setBorder(new EmptyBorder(10, 20, 15, 20));

        // Card superior: Info General
        JPanel panelInfoGeneral = new JPanel(new GridLayout(2, 2, 10, 5));
        panelInfoGeneral.setBackground(Color.WHITE);
        panelInfoGeneral.setBorder(new CompoundBorder(
                new LineBorder(new Color(210, 215, 220), 1),
                new EmptyBorder(10, 15, 10, 15)
        ));

        panelInfoGeneral.add(new JLabel("<html><b>Universidad Mariano Galvéz de Guatemala / Curso:</b> Programación II</html>"));
        panelInfoGeneral.add(new JLabel("<html><b>Base de Datos:</b> Oracle XE 21c (JDBC)</html>"));
        panelInfoGeneral.add(new JLabel("<html><b>Ecosistema:</b> Java Swing POS + Servidor API + Android App</html>"));
        panelInfoGeneral.add(new JLabel("<html><b>Versión:</b> 1.0</html>"));

        panelCenter.add(panelInfoGeneral, BorderLayout.NORTH);

        // Tabla de Integrantes y Distribución del Trabajo
        String[] columnas = {"No. Carné", "Nombre Completo", "Rol / Módulo", "Aportes Realizados"};
        
        // MODIFICA LOS DATOS DE ESTA TABLA CON LA INFORMACIÓN REAL DE TU GRUPO:
        Object[][] datos = {
            {"2023-00001", "Nombre Integrante 1", "Líder / Backend", "Diseño de Base de Datos Oracle XE, JDBC y Servidor API HTTP (Puerto 80)."},
            {"2023-00002", "Nombre Integrante 2", "Desarrollador Android", "App Móvil Material Design 3, consumo Volley JSON y generación de cupones."},
            {"2023-00003", "Nombre Integrante 3", "Desarrollador Desktop POS", "Módulo de Ventas FrmPedidos, validación de cupones de canje y reportes."},
            {"2023-00004", "Nombre Integrante 4", "QA / Modelos", "Clases de modelo (Cliente, Pedido, Inventario) y pruebas de integración."}
        };

        DefaultTableModel model = new DefaultTableModel(datos, columnas) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable tabla = new JTable(model);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabla.setRowHeight(28);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabla.getTableHeader().setBackground(new Color(230, 235, 242));

        // Ajustar anchos de columnas
        tabla.getColumnModel().getColumn(0).setPreferredWidth(100);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(180);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(150);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(350);

        JScrollPane scrollTabla = new JScrollPane(tabla);
        scrollTabla.setBorder(new LineBorder(new Color(210, 215, 220), 1));

        panelCenter.add(scrollTabla, BorderLayout.CENTER);

        add(panelCenter, BorderLayout.CENTER);
    }
}