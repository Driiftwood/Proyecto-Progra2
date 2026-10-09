package proyecto.progra2.vistas;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

import proyecto.progra2.modelos.GestionUsuarios;
import proyecto.progra2.modelos.Usuario;

public class FrmLogin extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnIngresar;
    private JButton btnCancelar;

    private GestionUsuarios gestionUsuarios;

    private static final Color COLOR_PRIMARY = new Color(24, 43, 73);      // Azul Marino
    private static final Color COLOR_BG_LIGHT = new Color(245, 247, 250);  // Gris Claro
    private static final Color COLOR_TEXT_DARK = new Color(33, 37, 41);   // Texto Oscuro
    private static final Color BTN_INGRESAR_BG = new Color(24, 43, 73);

    public FrmLogin() {
        gestionUsuarios = new GestionUsuarios();

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        setTitle("Acceso al Sistema - Control de Usuarios");
        setSize(460, 420); // Aumentado para dar espacio suficiente y no cortar textos
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(COLOR_BG_LIGHT);
        setLayout(new BorderLayout());

        // --- Header ---
        JPanel panelHeader = new JPanel();
        panelHeader.setBackground(COLOR_PRIMARY);
        panelHeader.setBorder(new EmptyBorder(22, 15, 22, 15));
        panelHeader.setLayout(new BoxLayout(panelHeader, BoxLayout.Y_AXIS));

        JLabel lblTitulo = new JLabel("INICIO DE SESIÓN");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSub = new JLabel("Ingrese sus credenciales de acceso");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(new Color(200, 215, 235));
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelHeader.add(lblTitulo);
        panelHeader.add(Box.createRigidArea(new Dimension(0, 6)));
        panelHeader.add(lblSub);

        add(panelHeader, BorderLayout.NORTH);

        // --- Formulario ---
        JPanel panelCentro = new JPanel(new GridBagLayout());
        panelCentro.setOpaque(false);
        panelCentro.setBorder(new EmptyBorder(20, 35, 10, 35));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 8, 10, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font fontLabel = new Font("Segoe UI", Font.BOLD, 13);
        Font fontText = new Font("Segoe UI", Font.PLAIN, 13);

        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblUser = new JLabel("Usuario:");
        lblUser.setFont(fontLabel);
        lblUser.setForeground(COLOR_TEXT_DARK);
        panelCentro.add(lblUser, gbc);

        gbc.gridx = 1; gbc.weightx = 1.0;
        txtUsuario = new JTextField();
        txtUsuario.setFont(fontText);
        txtUsuario.setPreferredSize(new Dimension(200, 32));
        txtUsuario.setBorder(new CompoundBorder(
                new LineBorder(new Color(190, 195, 200), 1),
                new EmptyBorder(4, 8, 4, 8)
        ));
        panelCentro.add(txtUsuario, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        JLabel lblPass = new JLabel("Contraseña:");
        lblPass.setFont(fontLabel);
        lblPass.setForeground(COLOR_TEXT_DARK);
        panelCentro.add(lblPass, gbc);

        gbc.gridx = 1; gbc.weightx = 1.0;
        txtPassword = new JPasswordField();
        txtPassword.setFont(fontText);
        txtPassword.setPreferredSize(new Dimension(200, 32));
        txtPassword.setBorder(new CompoundBorder(
                new LineBorder(new Color(190, 195, 200), 1),
                new EmptyBorder(4, 8, 4, 8)
        ));
        panelCentro.add(txtPassword, gbc);

        add(panelCentro, BorderLayout.CENTER);

        // --- Botones ---
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 18));
        panelBotones.setOpaque(false);
        panelBotones.setBorder(new EmptyBorder(0, 0, 10, 25));

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(fontLabel);
        btnCancelar.setPreferredSize(new Dimension(100, 35));

        btnIngresar = new JButton("Ingresar");
        btnIngresar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnIngresar.setBackground(BTN_INGRESAR_BG);
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setOpaque(true);
        btnIngresar.setContentAreaFilled(true);
        btnIngresar.setBorderPainted(false);
        btnIngresar.setFocusPainted(false);
        btnIngresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnIngresar.setPreferredSize(new Dimension(110, 35));

        panelBotones.add(btnCancelar);
        panelBotones.add(btnIngresar);

        add(panelBotones, BorderLayout.SOUTH);

        // Eventos
        btnIngresar.addActionListener(e -> validarAcceso());
        btnCancelar.addActionListener(e -> System.exit(0));
    }

    private void validarAcceso() {
        String user = txtUsuario.getText().trim();
        String pass = new String(txtPassword.getPassword()).trim();

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese usuario y contraseña.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Usuario usuarioAutenticado = gestionUsuarios.autenticar(user, pass);

        if (usuarioAutenticado != null) {
            JOptionPane.showMessageDialog(this, 
                "Bienvenido " + usuarioAutenticado.getNombre() + "\nRol: " + usuarioAutenticado.getRol(), 
                "Acceso Concedido", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // Aquí se abrirá la ventana principal del sistema
            this.dispose(); 
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.", "Error de Autenticación", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmLogin().setVisible(true));
    }
}