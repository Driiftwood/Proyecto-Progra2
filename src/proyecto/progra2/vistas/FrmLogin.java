package proyecto.progra2.vistas;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

import proyecto.progra2.conexion.ServidorApi;
import proyecto.progra2.modelos.GestionUsuarios;
import proyecto.progra2.modelos.Usuario;

public class FrmLogin extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnIngresar, btnSalir, btnVerPass;
    private GestionUsuarios gestionUsuarios;
    private boolean mostrandoPass = false;

    private static final Color COLOR_PRIMARY = new Color(24, 43, 73);
    private static final Color COLOR_ACCENT = new Color(0, 122, 255);
    private static final Color COLOR_BG_LIGHT = new Color(245, 247, 250);
    private static final Color COLOR_TEXT_DARK = new Color(33, 37, 41);

    public FrmLogin() {
        // --- Iniciar Servidor API HTTP para la App Android ---
        new Thread(ServidorApi::iniciarServidor).start();

        gestionUsuarios = new GestionUsuarios();

        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}

        setTitle("Pan, Puntos y Premios — Inicio de Sesión");
        setSize(420, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(COLOR_BG_LIGHT);
        setLayout(new BorderLayout());

        // --- Header ---
        JPanel panelHeader = new JPanel();
        panelHeader.setBackground(COLOR_PRIMARY);
        panelHeader.setLayout(new BoxLayout(panelHeader, BoxLayout.Y_AXIS));
        panelHeader.setBorder(new EmptyBorder(25, 20, 25, 20));

        JLabel lblTitulo = new JLabel("PAN, PUNTOS Y PREMIOS");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubtitulo = new JLabel("Sistema de Gestión y Fidelización");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubtitulo.setForeground(new Color(200, 215, 235));
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelHeader.add(lblTitulo);
        panelHeader.add(Box.createRigidArea(new Dimension(0, 6)));
        panelHeader.add(lblSubtitulo);

        add(panelHeader, BorderLayout.NORTH);

        // --- Panel Formulario ---
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setOpaque(false);
        panelForm.setBorder(new EmptyBorder(20, 30, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font fontLabel = new Font("Segoe UI", Font.BOLD, 12);
        Font fontText = new Font("Segoe UI", Font.PLAIN, 13);

        // Usuario
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblUser = new JLabel("Usuario:");
        lblUser.setFont(fontLabel);
        panelForm.add(lblUser, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        txtUsuario = new JTextField();
        txtUsuario.setFont(fontText);
        txtUsuario.setPreferredSize(new Dimension(280, 32));
        txtUsuario.setBorder(new CompoundBorder(new LineBorder(new Color(190, 195, 200), 1), new EmptyBorder(2, 8, 2, 8)));
        panelForm.add(txtUsuario, gbc);

        // Contraseña + Botón Ver
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblPass = new JLabel("Contraseña:");
        lblPass.setFont(fontLabel);
        panelForm.add(lblPass, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        JPanel panelPass = new JPanel(new BorderLayout(5, 0));
        panelPass.setOpaque(false);

        txtPassword = new JPasswordField();
        txtPassword.setFont(fontText);
        txtPassword.setPreferredSize(new Dimension(230, 32));
        txtPassword.setBorder(new CompoundBorder(new LineBorder(new Color(190, 195, 200), 1), new EmptyBorder(2, 8, 2, 8)));

        btnVerPass = new JButton("Ver");
        btnVerPass.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnVerPass.setBackground(new Color(220, 225, 230));
        btnVerPass.setForeground(COLOR_TEXT_DARK);
        btnVerPass.setPreferredSize(new Dimension(42, 32));
        btnVerPass.setFocusPainted(false);
        btnVerPass.setBorderPainted(false);
        btnVerPass.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVerPass.addActionListener(e -> alternarVisibilidadPass());

        panelPass.add(txtPassword, BorderLayout.CENTER);
        panelPass.add(btnVerPass, BorderLayout.EAST);

        panelForm.add(panelPass, gbc);

        // Botón Ingresar
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.insets = new Insets(20, 5, 8, 5);
        btnIngresar = new JButton("Iniciar Sesión");
        btnIngresar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnIngresar.setBackground(COLOR_ACCENT);
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setPreferredSize(new Dimension(280, 36));
        btnIngresar.setOpaque(true);
        btnIngresar.setContentAreaFilled(true);
        btnIngresar.setBorderPainted(false);
        btnIngresar.setFocusPainted(false);
        btnIngresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnIngresar.addActionListener(e -> autenticar());

        panelForm.add(btnIngresar, gbc);

        // Botón Salir
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.insets = new Insets(4, 5, 5, 5);
        btnSalir = new JButton("Salir");
        btnSalir.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSalir.setBackground(new Color(220, 224, 230));
        btnSalir.setForeground(COLOR_TEXT_DARK);
        btnSalir.setPreferredSize(new Dimension(280, 32));
        btnSalir.setOpaque(true);
        btnSalir.setContentAreaFilled(true);
        btnSalir.setBorderPainted(false);
        btnSalir.setFocusPainted(false);
        btnSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalir.addActionListener(e -> System.exit(0));

        panelForm.add(btnSalir, gbc);

        add(panelForm, BorderLayout.CENTER);

        // Enter key shortcut
        getRootPane().setDefaultButton(btnIngresar);
    }

    private void autenticar() {
        String user = txtUsuario.getText().trim();
        String pass = new String(txtPassword.getPassword()).trim();

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese usuario y contraseña.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Usuario usuarioLogueado = gestionUsuarios.autenticar(user, pass);

        if (usuarioLogueado != null) {
            JOptionPane.showMessageDialog(this, "¡Bienvenido/a " + usuarioLogueado.getNombre() + "!\nRol: " + usuarioLogueado.getRol(), "Acceso Concedido", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
            
            // Pasa el objeto del usuario logueado al FrmPrincipal
            new FrmPrincipal(usuarioLogueado).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos, o la cuenta está inactiva.", "Acceso Denegado", JOptionPane.ERROR_MESSAGE);
            txtPassword.setText("");
            txtPassword.requestFocus();
        }
    }

    private void alternarVisibilidadPass() {
        mostrandoPass = !mostrandoPass;
        if (mostrandoPass) {
            txtPassword.setEchoChar((char) 0);
        } else {
            txtPassword.setEchoChar('•');
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmLogin().setVisible(true));
    }
}