package proyecto.progra2.vistas;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import proyecto.progra2.modelos.*;

public class FrmCanjePuntos extends JFrame {

    private JTextField txtNit, txtCliente, txtPuntosActuales;
    private JComboBox<Recompensa> cbRecompensas;
    private JButton btnBuscarCliente, btnCanjear, btnRegresar;
    private JTable tablaHistorial;
    private DefaultTableModel modeloTabla;

    private GestionClientes gestionClientes;
    private GestionInventario gestionInventario;
    private Cliente clienteActual;
    private Usuario usuarioSesion;
    private List<Recompensa> catalogoRecompensas;

    private static final Color COLOR_PRIMARY = new Color(24, 43, 73);      
    private static final Color COLOR_BG_LIGHT = new Color(245, 247, 250);  

    public FrmCanjePuntos() {
        this(null);
    }

    public FrmCanjePuntos(Usuario usuario) {
        this.usuarioSesion = usuario;
        gestionClientes = new GestionClientes();
        gestionInventario = new GestionInventario();
        catalogoRecompensas = new ArrayList<>();

        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}

        cargarCatalogoRecompensas();

        setTitle("Módulo de Fidelización y Canje de Puntos");
        setSize(880, 540);
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

        JLabel lblTitulo = new JLabel("MÓDULO DE FIDELIZACIÓN Y CANJE DE RECOMPENSAS");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Canje de puntos acumulados por productos del catálogo");
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

        // --- Panel Izquierdo: Selección y Canje ---
        JPanel panelIzquierdo = new JPanel(new GridBagLayout());
        panelIzquierdo.setOpaque(false);
        panelIzquierdo.setPreferredSize(new Dimension(380, 0));
        panelIzquierdo.setBorder(new EmptyBorder(10, 15, 10, 5));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 6, 8, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font fontLabel = new Font("Segoe UI", Font.BOLD, 12);
        Font fontText = new Font("Segoe UI", Font.PLAIN, 12);

        gbc.gridx = 0; gbc.gridy = 0; panelIzquierdo.add(new JLabel("NIT / DPI:"), gbc);
        gbc.gridx = 1; txtNit = crearCampoTexto(fontText); panelIzquierdo.add(txtNit, gbc);

        btnBuscarCliente = crearBoton("Buscar", COLOR_PRIMARY, Color.WHITE);
        btnBuscarCliente.addActionListener(e -> buscarCliente());
        gbc.gridx = 2; panelIzquierdo.add(btnBuscarCliente, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panelIzquierdo.add(new JLabel("Cliente:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        txtCliente = crearCampoTexto(fontText); txtCliente.setEditable(false);
        panelIzquierdo.add(txtCliente, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1; panelIzquierdo.add(new JLabel("Saldo Puntos:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        txtPuntosActuales = crearCampoTexto(fontText); txtPuntosActuales.setEditable(false);
        txtPuntosActuales.setFont(new Font("Segoe UI", Font.BOLD, 13));
        txtPuntosActuales.setForeground(new Color(20, 110, 180));
        panelIzquierdo.add(txtPuntosActuales, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1; panelIzquierdo.add(new JLabel("Recompensa:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        cbRecompensas = new JComboBox<>();
        cbRecompensas.setFont(fontText);
        for (Recompensa r : catalogoRecompensas) cbRecompensas.addItem(r);
        panelIzquierdo.add(cbRecompensas, gbc);

        btnCanjear = crearBoton("Confirmar Canje", new Color(34, 153, 84), Color.WHITE);
        btnCanjear.setPreferredSize(new Dimension(160, 36));
        btnCanjear.addActionListener(e -> realizarCanje());
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 3;
        gbc.insets = new Insets(18, 6, 8, 6);
        panelIzquierdo.add(btnCanjear, gbc);

        add(panelIzquierdo, BorderLayout.WEST);

        // --- Panel Derecho: Historial de Canjes ---
        JPanel panelTabla = new JPanel(new BorderLayout(5, 5));
        panelTabla.setOpaque(false);
        panelTabla.setBorder(new EmptyBorder(10, 5, 10, 15));

        String[] columnas = {"Recompensa", "Puntos Requeridos", "Estado Canje"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tablaHistorial = new JTable(modeloTabla);
        tablaHistorial.setFont(fontText);
        tablaHistorial.setRowHeight(25);
        tablaHistorial.getTableHeader().setFont(fontLabel);

        panelTabla.add(new JScrollPane(tablaHistorial), BorderLayout.CENTER);
        add(panelTabla, BorderLayout.CENTER);
    }

    private void cargarCatalogoRecompensas() {
        ProductoIndividual beb = gestionInventario.getListaProductos().stream().filter(p -> p.getCategoria().equals("BEBIDA")).findFirst().orElse(null);
        ProductoIndividual aco = gestionInventario.getListaProductos().stream().filter(p -> p.getCategoria().equals("ACOMPANAMIENTO")).findFirst().orElse(null);
        ProductoIndividual san = gestionInventario.getListaProductos().stream().filter(p -> p.getCategoria().equals("SANDWICH")).findFirst().orElse(null);
        MenuCompleto men = gestionInventario.getListaMenus().isEmpty() ? null : gestionInventario.getListaMenus().get(0);

        catalogoRecompensas.add(new Recompensa("REC01", "Bebida Gratuita", 10, beb));
        catalogoRecompensas.add(new Recompensa("REC02", "Acompañamiento Gratuito", 15, aco));
        catalogoRecompensas.add(new Recompensa("REC03", "Sándwich Individual Gratuito", 30, san));
        catalogoRecompensas.add(new Recompensa("REC04", "Menú Completo Gratuito", 50, men));
    }

    private JTextField crearCampoTexto(Font font) {
        JTextField field = new JTextField();
        field.setFont(font);
        field.setPreferredSize(new Dimension(130, 28));
        field.setBorder(new CompoundBorder(new LineBorder(new Color(190, 195, 200), 1), new EmptyBorder(2, 6, 2, 6)));
        return field;
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
        btn.setPreferredSize(new Dimension(100, 30));
        return btn;
    }

    private void buscarCliente() {
        String nit = txtNit.getText().trim();
        Cliente c = gestionClientes.buscarPorNit(nit);
        if (c != null) {
            if (!c.isActivo()) {
                JOptionPane.showMessageDialog(this, "El cliente se encuentra inactivo.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            clienteActual = c;
            txtCliente.setText(c.getNombre());
            txtPuntosActuales.setText(c.getPuntos() + " pts");
        } else {
            JOptionPane.showMessageDialog(this, "Cliente no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void realizarCanje() {
        if (clienteActual == null) {
            JOptionPane.showMessageDialog(this, "Busque y seleccione un cliente primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Recompensa recompensa = (Recompensa) cbRecompensas.getSelectedItem();
        if (recompensa == null) return;

        if (clienteActual.getPuntos() < recompensa.getPuntosNecesarios()) {
            JOptionPane.showMessageDialog(this, 
                "Puntos insuficientes.\nEl cliente tiene: " + clienteActual.getPuntos() + " pts\nRequiere: " + recompensa.getPuntosNecesarios() + " pts", 
                "Canje Rechazado", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ItemVenta item = recompensa.getItemAsociado();
        if (item instanceof ProductoIndividual) {
            ProductoIndividual p = (ProductoIndividual) item;
            if (p.getExistencia() < 1) {
                JOptionPane.showMessageDialog(this, "No hay existencias disponibles del producto en inventario.", "Canje Rechazado", JOptionPane.WARNING_MESSAGE);
                return;
            }
            p.setExistencia(p.getExistencia() - 1);
        } else if (item instanceof MenuCompleto) {
            MenuCompleto m = (MenuCompleto) item;
            if (!m.hayStockDisponible(1)) {
                JOptionPane.showMessageDialog(this, "No hay existencias suficientes de los componentes del menú.", "Canje Rechazado", JOptionPane.WARNING_MESSAGE);
                return;
            }
            m.getSandwich().setExistencia(m.getSandwich().getExistencia() - 1);
            m.getBebida().setExistencia(m.getBebida().getExistencia() - 1);
            m.getAcompanamiento().setExistencia(m.getAcompanamiento().getExistencia() - 1);
        }

        clienteActual.descontarPuntos(recompensa.getPuntosNecesarios());
        txtPuntosActuales.setText(clienteActual.getPuntos() + " pts");

        modeloTabla.addRow(new Object[]{recompensa.getNombre(), "-" + recompensa.getPuntosNecesarios() + " pts", "APROBADO"});

        JOptionPane.showMessageDialog(this, 
            "¡CANJE EXITOSO!\n\nRecompensa: " + recompensa.getNombre() + "\nPuntos Descontados: -" + recompensa.getPuntosNecesarios() + " pts\nNuevo Saldo: " + clienteActual.getPuntos() + " pts", 
            "Comprobante de Canje", JOptionPane.INFORMATION_MESSAGE);
    }

    private void regresarMenu() {
        this.dispose();
        new FrmPrincipal(usuarioSesion).setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmCanjePuntos(null).setVisible(true));
    }
}