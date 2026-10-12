package proyecto.progra2.vistas;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import proyecto.progra2.modelos.Usuario;

public class FrmPrincipal extends JFrame {

    private Usuario usuarioActual;

    private static final Color COLOR_PRIMARY = new Color(24, 43, 73);      
    private static final Color COLOR_ACCENT = new Color(0, 122, 255);       
    private static final Color COLOR_BG_LIGHT = new Color(245, 247, 250);  
    private static final Color COLOR_TEXT_DARK = new Color(33, 37, 41);   

    public FrmPrincipal(Usuario usuario) {
        this.usuarioActual = usuario;

        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}

        setTitle("Sistema Pan, Puntos y Premios - Menú Principal");
        setSize(1020, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_BG_LIGHT);
        setLayout(new BorderLayout());

        // --- Header ---
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(COLOR_PRIMARY);
        panelHeader.setBorder(new EmptyBorder(18, 25, 18, 25));

        JPanel panelInfoUser = new JPanel();
        panelInfoUser.setOpaque(false);
        panelInfoUser.setLayout(new BoxLayout(panelInfoUser, BoxLayout.Y_AXIS));

        JLabel lblTituloApp = new JLabel("PAN, PUNTOS Y PREMIOS — MENÚ PRINCIPAL");
        lblTituloApp.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTituloApp.setForeground(Color.WHITE);

        String textoUsuario = (usuarioActual != null) 
                ? "Usuario activo: " + usuarioActual.getNombre() + " (" + usuarioActual.getRol() + ")" 
                : "Usuario activo: Administrador Sistema";

        JLabel lblUsuario = new JLabel(textoUsuario);
        lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblUsuario.setForeground(new Color(200, 215, 235));

        panelInfoUser.add(lblTituloApp);
        panelInfoUser.add(Box.createRigidArea(new Dimension(0, 4)));
        panelInfoUser.add(lblUsuario);

        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCerrarSesion.setBackground(new Color(180, 40, 40));
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setOpaque(true);
        btnCerrarSesion.setContentAreaFilled(true);
        btnCerrarSesion.setBorderPainted(false);
        btnCerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrarSesion.addActionListener(e -> cerrarSesion());

        panelHeader.add(panelInfoUser, BorderLayout.CENTER);
        panelHeader.add(btnCerrarSesion, BorderLayout.EAST);

        add(panelHeader, BorderLayout.NORTH);

        // --- Grid de 6 Módulos ---
        JPanel panelGrid = new JPanel(new GridLayout(2, 3, 18, 18));
        panelGrid.setOpaque(false);
        panelGrid.setBorder(new EmptyBorder(25, 30, 25, 30));

        panelGrid.add(crearTarjetaModulo("Módulo 1: Usuarios", "Control de credenciales y accesos (Exclusivo Admin)", e -> abrirUsuarios()));
        panelGrid.add(crearTarjetaModulo("Módulo 2: Clientes", "Registro de clientes y saldo inicial de puntos", e -> abrirClientes()));
        panelGrid.add(crearTarjetaModulo("Módulo 3: Inventario", "Sándwiches, Bebidas, Menús y Abastecimiento", e -> abrirInventario()));
        panelGrid.add(crearTarjetaModulo("Módulo 4: Pedidos y Cobro", "Ventas, pagos (Efectivo/Tarjeta/Mixto) y puntos", e -> abrirPedidos()));
        panelGrid.add(crearTarjetaModulo("Módulo 5: Fidelización", "Canje de recompensas por catálogo de puntos", e -> abrirCanje()));
        panelGrid.add(crearTarjetaModulo("Módulo 6: Reportes", "Ventas, más vendidos, bajo stock e historial", e -> abrirReportes()));

        add(panelGrid, BorderLayout.CENTER);

        // --- Footer ---
        JPanel panelFooter = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelFooter.setOpaque(false);
        panelFooter.setBorder(new EmptyBorder(10, 10, 15, 10));

        JLabel lblFooter = new JLabel("Proyecto Programación II");
        lblFooter.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblFooter.setForeground(new Color(120, 120, 120));
        panelFooter.add(lblFooter);

        add(panelFooter, BorderLayout.SOUTH);
    }

    private JPanel crearTarjetaModulo(String titulo, String descripcion, java.awt.event.ActionListener accion) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(210, 215, 220), 1, true),
                new EmptyBorder(18, 18, 18, 18)
        ));

        JPanel panelTextos = new JPanel();
        panelTextos.setOpaque(false);
        panelTextos.setLayout(new BoxLayout(panelTextos, BoxLayout.Y_AXIS));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitulo.setForeground(COLOR_PRIMARY);

        JLabel lblDesc = new JLabel("<html>" + descripcion + "</html>");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDesc.setForeground(COLOR_TEXT_DARK);

        panelTextos.add(lblTitulo);
        panelTextos.add(Box.createRigidArea(new Dimension(0, 6)));
        panelTextos.add(lblDesc);

        JButton btnAbrir = new JButton("Abrir Módulo");
        btnAbrir.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnAbrir.setBackground(COLOR_ACCENT);
        btnAbrir.setForeground(Color.WHITE);
        btnAbrir.setOpaque(true);
        btnAbrir.setContentAreaFilled(true);
        btnAbrir.setBorderPainted(false);
        btnAbrir.setFocusPainted(false);
        btnAbrir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAbrir.addActionListener(accion);

        card.add(panelTextos, BorderLayout.CENTER);
        card.add(btnAbrir, BorderLayout.SOUTH);

        card.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                card.setBorder(new CompoundBorder(new LineBorder(COLOR_ACCENT, 2, true), new EmptyBorder(17, 17, 17, 17)));
            }
            @Override public void mouseExited(MouseEvent e) {
                card.setBorder(new CompoundBorder(new LineBorder(new Color(210, 215, 220), 1, true), new EmptyBorder(18, 18, 18, 18)));
            }
        });

        return card;
    }

    private void abrirUsuarios() {
        if (usuarioActual != null && "Cajero".equalsIgnoreCase(usuarioActual.getRol())) {
            JOptionPane.showMessageDialog(this, 
                    "Acceso Restringido: El Módulo de Gestión de Usuarios es exclusivo para el Administrador.", 
                    "Permisos Insuficientes", JOptionPane.WARNING_MESSAGE);
            return;
        }
        this.dispose();
        new FrmUsuarios(usuarioActual).setVisible(true);
    }

    private void abrirClientes() {
        this.dispose();
        new FrmClientes(usuarioActual).setVisible(true);
    }

    private void abrirInventario() {
        this.dispose();
        new FrmInventario(usuarioActual).setVisible(true);
    }

    private void abrirPedidos() {
        this.dispose();
        new FrmPedidos(usuarioActual).setVisible(true);
    }

    private void abrirCanje() {
        this.dispose();
        new FrmCanjePuntos(usuarioActual).setVisible(true);
    }

    private void abrirReportes() {
        this.dispose();
        new FrmReportes(usuarioActual).setVisible(true);
    }

    private void cerrarSesion() {
        int confirm = JOptionPane.showConfirmDialog(this, "¿Desea cerrar la sesión actual?", "Confirmación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();
            new FrmLogin().setVisible(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmPrincipal(null).setVisible(true));
    }
}
