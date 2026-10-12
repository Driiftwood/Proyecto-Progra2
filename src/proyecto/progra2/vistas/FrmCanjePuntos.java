package proyecto.progra2.vistas;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.Random;

import proyecto.progra2.modelos.Cliente;
import proyecto.progra2.modelos.GestionClientes;
import proyecto.progra2.modelos.Usuario;

public class FrmCanjePuntos extends JFrame {

    private JTextField txtNitCliente, txtNombreCliente, txtPuntosCliente;
    private JComboBox<String> cbPremios;
    private JLabel lblCodigoCuponGenerado;
    private JButton btnBuscar, btnCanjear, btnRegresar;

    private GestionClientes gestionClientes;
    private Cliente clienteSeleccionado;
    private Usuario usuarioSesion;

    private static final Color COLOR_PRIMARY = new Color(24, 43, 73);
    private static final Color COLOR_BG_LIGHT = new Color(245, 247, 250);

    public FrmCanjePuntos() {
        this(null);
    }

    public FrmCanjePuntos(Usuario usuario) {
        this.usuarioSesion = usuario;
        gestionClientes = new GestionClientes();

        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}

        setTitle("Módulo de Canje de Puntos y Generación de Cupones");
        setSize(780, 520);
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

        JLabel lblTitulo = new JLabel("MÓDULO DE CANJE DE PUNTOS DE FIDELIZACIÓN");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Canjea puntos acumulados por productos o menús y genera el cupón de descuento PAN-XXXXXX");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblSub.setForeground(new Color(200, 215, 235));

        panelTitulos.add(lblTitulo);
        panelTitulos.add(Box.createRigidArea(new Dimension(0, 4)));
        panelTitulos.add(lblSub);

        btnRegresar = crearBoton("Volver al Menú", new Color(70, 85, 105), Color.WHITE);
        btnRegresar.setPreferredSize(new Dimension(130, 32));
        btnRegresar.addActionListener(e -> regresarMenu());

        panelHeader.add(panelTitulos, BorderLayout.WEST);
        panelHeader.add(btnRegresar, BorderLayout.EAST);

        add(panelHeader, BorderLayout.NORTH);

        // --- Formulario Central ---
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setBackground(Color.WHITE);
        panelCentral.setBorder(new CompoundBorder(new EmptyBorder(15, 20, 15, 20), new LineBorder(new Color(210, 215, 220), 1)));

        Font fontText = new Font("Segoe UI", Font.PLAIN, 12);
        Font fontBold = new Font("Segoe UI", Font.BOLD, 12);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // NIT
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.0;
        panelCentral.add(new JLabel("NIT / DPI Cliente:"), gbc);

        txtNitCliente = crearCampoTexto(fontText);
        gbc.gridx = 1; gbc.weightx = 1.0;
        panelCentral.add(txtNitCliente, gbc);

        btnBuscar = crearBoton("Buscar Cliente", COLOR_PRIMARY, Color.WHITE);
        btnBuscar.addActionListener(e -> buscarCliente());
        gbc.gridx = 2; gbc.weightx = 0.0;
        panelCentral.add(btnBuscar, gbc);

        // Nombre
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0;
        panelCentral.add(new JLabel("Nombre Cliente:"), gbc);

        txtNombreCliente = crearCampoTexto(fontText);
        txtNombreCliente.setEditable(false);
        gbc.gridx = 1; gbc.gridwidth = 2; gbc.weightx = 1.0;
        panelCentral.add(txtNombreCliente, gbc);

        // Puntos
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1; gbc.weightx = 0.0;
        panelCentral.add(new JLabel("Puntos Disponibles:"), gbc);

        txtPuntosCliente = crearCampoTexto(fontBold);
        txtPuntosCliente.setEditable(false);
        txtPuntosCliente.setForeground(new Color(20, 110, 180));
        gbc.gridx = 1; gbc.gridwidth = 2; gbc.weightx = 1.0;
        panelCentral.add(txtPuntosCliente, gbc);

        // Selección de Premio
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1; gbc.weightx = 0.0;
        panelCentral.add(new JLabel("Seleccionar Premio:"), gbc);

        cbPremios = new JComboBox<>(new String[]{
            "Sándwich Jamón y Queso (25 Puntos)",
            "Gaseosa / Bebida (10 Puntos)",
            "Papas Fritas (15 Puntos)",
            "Menú Completo (50 Puntos)"
        });
        cbPremios.setFont(fontText);
        gbc.gridx = 1; gbc.gridwidth = 2; gbc.weightx = 1.0;
        panelCentral.add(cbPremios, gbc);

        // Botón Canjear
        btnCanjear = crearBoton("PROCESAR CANJE Y GENERAR CUPÓN", new Color(34, 153, 84), Color.WHITE);
        btnCanjear.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCanjear.setPreferredSize(new Dimension(0, 38));
        btnCanjear.addActionListener(e -> procesarCanje());
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 3; gbc.insets = new Insets(15, 10, 10, 10);
        panelCentral.add(btnCanjear, gbc);

        // Cupón Generado
        lblCodigoCuponGenerado = new JLabel("CUPÓN: NINGUNO GENERADO", SwingConstants.CENTER);
        lblCodigoCuponGenerado.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblCodigoCuponGenerado.setForeground(new Color(142, 68, 173));
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 3;
        panelCentral.add(lblCodigoCuponGenerado, gbc);

        add(panelCentral, BorderLayout.CENTER);
    }

    private JTextField crearCampoTexto(Font font) {
        JTextField field = new JTextField();
        field.setFont(font);
        field.setPreferredSize(new Dimension(140, 28));
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
        return btn;
    }

    private void buscarCliente() {
        String nit = txtNitCliente.getText().trim();
        Cliente c = gestionClientes.buscarPorNit(nit);
        if (c != null) {
            if (!c.isActivo()) {
                JOptionPane.showMessageDialog(this, "El cliente está desactivado.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            clienteSeleccionado = c;
            txtNombreCliente.setText(c.getNombre());
            txtPuntosCliente.setText(c.getPuntos() + " pts");
        } else {
            JOptionPane.showMessageDialog(this, "Cliente no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void procesarCanje() {
        if (clienteSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione o busque un cliente válido.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int index = cbPremios.getSelectedIndex();
        int costoPuntos = 0;

        switch (index) {
            case 0: costoPuntos = 25; break;
            case 1: costoPuntos = 10; break;
            case 2: costoPuntos = 15; break;
            case 3: costoPuntos = 50; break;
        }

        if (clienteSeleccionado.getPuntos() < costoPuntos) {
            JOptionPane.showMessageDialog(this, "Puntos insuficientes. El premio requiere " + costoPuntos + " pts.", "Puntos Insuficientes", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // DESCUENTO REAL Y DIRECTO EN ORACLE XE
        boolean exitoBD = gestionClientes.descontarPuntos(clienteSeleccionado.getNit(), costoPuntos);

        if (exitoBD) {
            // Actualizar referencia local
            clienteSeleccionado = gestionClientes.buscarPorNit(clienteSeleccionado.getNit());
            txtPuntosCliente.setText(clienteSeleccionado.getPuntos() + " pts");

            String codigoCupon = "PAN-" + generarAlfanumerico(5);
            lblCodigoCuponGenerado.setText("CUPÓN VALIDADOR: " + codigoCupon);

            JOptionPane.showMessageDialog(this, "¡Canje exitoso!\nSe descontaron " + costoPuntos + " puntos en Oracle XE.\n"
                    + "Código de Cupón Generado: " + codigoCupon + "\n\n"
                    + "Ingrese este código en el Módulo de Pedidos para aplicar el 100% de descuento.", "Canje Confirmado", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo procesar el descuento de puntos en la base de datos.", "Error de BD", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String generarAlfanumerico(int longitud) {
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
        StringBuilder sb = new StringBuilder();
        Random rnd = new Random();
        for (int i = 0; i < longitud; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }

    private void regresarMenu() {
        this.dispose();
        new FrmPrincipal(usuarioSesion).setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmCanjePuntos(null).setVisible(true));
    }
}