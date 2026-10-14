package proyecto.progra2.vistas;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import proyecto.progra2.modelos.GestionUsuarios;
import proyecto.progra2.modelos.Usuario;

public class FrmUsuarios extends JFrame {

    private JTextField txtUser, txtNombre;
    private JPasswordField txtPass;
    private JComboBox<String> cbRol;
    private JButton btnGuardar, btnEditar, btnEstado, btnLimpiar, btnCambiarPass, btnVerPass, btnRegresar;
    private JTable tablaUsuarios;
    private DefaultTableModel modeloTabla;

    private GestionUsuarios gestionUsuarios;
    private Usuario usuarioSesion;
    private String usuarioSeleccionado = null;
    private boolean mostrandoPass = false;

    private static final Color COLOR_PRIMARY = new Color(24, 43, 73);      
    private static final Color COLOR_BG_LIGHT = new Color(245, 247, 250);  
    private static final Color COLOR_TEXT_DARK = new Color(33, 37, 41);   

    public FrmUsuarios() {
        this(null);
    }

    public FrmUsuarios(Usuario usuario) {
        this.usuarioSesion = usuario;
        this.gestionUsuarios = new GestionUsuarios();

        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}

        setTitle("Gestión de Usuarios y Permisos del Sistema");
        setSize(960, 560);
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

        JLabel lblTitulo = new JLabel("MÓDULO DE GESTIÓN DE USUARIOS Y PERMISOS");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Administración de credenciales, roles, cambio de claves y estados de cuenta");
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

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setOpaque(false);
        panelForm.setBorder(new EmptyBorder(15, 15, 15, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font fontLabel = new Font("Segoe UI", Font.BOLD, 12);
        Font fontText = new Font("Segoe UI", Font.PLAIN, 12);

        gbc.gridx = 0; gbc.gridy = 0; panelForm.add(new JLabel("Usuario:"), gbc);
        gbc.gridx = 1; txtUser = new JTextField(); txtUser.setFont(fontText);
        txtUser.setPreferredSize(new Dimension(160, 28));
        txtUser.setBorder(new CompoundBorder(new LineBorder(new Color(190, 195, 200), 1), new EmptyBorder(2, 6, 2, 6)));
        panelForm.add(txtUser, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panelForm.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; txtNombre = new JTextField(); txtNombre.setFont(fontText);
        txtNombre.setPreferredSize(new Dimension(160, 28));
        txtNombre.setBorder(new CompoundBorder(new LineBorder(new Color(190, 195, 200), 1), new EmptyBorder(2, 6, 2, 6)));
        panelForm.add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panelForm.add(new JLabel("Contraseña:"), gbc);

        JPanel panelPassContainer = new JPanel(new BorderLayout(4, 0));
        panelPassContainer.setOpaque(false);
        txtPass = new JPasswordField(); txtPass.setFont(fontText);
        txtPass.setPreferredSize(new Dimension(110, 28));
        txtPass.setBorder(new CompoundBorder(new LineBorder(new Color(190, 195, 200), 1), new EmptyBorder(2, 6, 2, 6)));

        btnVerPass = crearBoton("Ver", new Color(180, 185, 190), COLOR_TEXT_DARK);
        btnVerPass.setPreferredSize(new Dimension(42, 28));
        btnVerPass.setToolTipText("Mostrar / Ocultar Contraseña");
        btnVerPass.addActionListener(e -> alternarVisibilidadPass());

        panelPassContainer.add(txtPass, BorderLayout.CENTER);
        panelPassContainer.add(btnVerPass, BorderLayout.EAST);

        gbc.gridx = 1; panelForm.add(panelPassContainer, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panelForm.add(new JLabel("Rol:"), gbc);
        gbc.gridx = 1; 
        cbRol = new JComboBox<>(new String[]{"Administrador", "Cajero"});
        cbRol.setFont(fontText);
        panelForm.add(cbRol, gbc);

        JPanel panelAcciones = new JPanel(new GridLayout(2, 2, 6, 6));
        panelAcciones.setOpaque(false);

        btnGuardar = crearBoton("Guardar", COLOR_PRIMARY, Color.WHITE);
        btnEditar = crearBoton("Editar", new Color(40, 116, 166), Color.WHITE);
        btnEstado = crearBoton("Desactivar", new Color(176, 58, 46), Color.WHITE);
        btnLimpiar = crearBoton("Limpiar", new Color(220, 224, 230), COLOR_TEXT_DARK);

        panelAcciones.add(btnGuardar);
        panelAcciones.add(btnEditar);
        panelAcciones.add(btnEstado);
        panelAcciones.add(btnLimpiar);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.insets = new Insets(12, 6, 6, 6);
        panelForm.add(panelAcciones, gbc);

        btnCambiarPass = crearBoton("Cambiar Contraseña", new Color(142, 68, 173), Color.WHITE);
        btnCambiarPass.setPreferredSize(new Dimension(180, 32));
        btnCambiarPass.addActionListener(e -> cambiarPassword());

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        gbc.insets = new Insets(6, 6, 6, 6);
        panelForm.add(btnCambiarPass, gbc);

        add(panelForm, BorderLayout.WEST);

        JPanel panelTabla = new JPanel(new BorderLayout(5, 5));
        panelTabla.setOpaque(false);
        panelTabla.setBorder(new EmptyBorder(15, 5, 15, 15));

        String[] columnas = {"Usuario", "Nombre Completo", "Rol", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tablaUsuarios = new JTable(modeloTabla);
        tablaUsuarios.setFont(fontText);
        tablaUsuarios.setRowHeight(25);
        tablaUsuarios.getTableHeader().setFont(fontLabel);

        tablaUsuarios.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { seleccionarFila(); }
        });

        panelTabla.add(new JScrollPane(tablaUsuarios), BorderLayout.CENTER);
        add(panelTabla, BorderLayout.CENTER);

        btnGuardar.addActionListener(e -> guardarUsuario());
        btnEditar.addActionListener(e -> editarUsuario());
        btnEstado.addActionListener(e -> alternarEstadoUsuario());
        btnLimpiar.addActionListener(e -> limpiarCampos());

        cargarTabla();
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
        btn.setPreferredSize(new Dimension(85, 30));
        return btn;
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        for (Usuario u : gestionUsuarios.getListaUsuarios()) {
            modeloTabla.addRow(new Object[]{
                u.getUsername(), u.getNombre(), u.getRol(), u.isActivo() ? "Activo" : "Inactivo"
            });
        }
    }

    private void seleccionarFila() {
        int fila = tablaUsuarios.getSelectedRow();
        if (fila >= 0) {
            usuarioSeleccionado = modeloTabla.getValueAt(fila, 0).toString();
            Usuario u = gestionUsuarios.buscarPorUsername(usuarioSeleccionado);
            if (u != null) {
                txtUser.setText(u.getUsername());
                txtUser.setEditable(false);
                txtNombre.setText(u.getNombre());
                txtPass.setText(u.getPassword());
                cbRol.setSelectedItem(u.getRol());

                if (u.isActivo()) {
                    btnEstado.setText("Desactivar");
                    btnEstado.setBackground(new Color(176, 58, 46));
                } else {
                    btnEstado.setText("Reactivar");
                    btnEstado.setBackground(new Color(34, 153, 84));
                }
            }
        }
    }

    private void guardarUsuario() {
        String usr = txtUser.getText().trim();
        String nom = txtNombre.getText().trim();
        String pass = new String(txtPass.getPassword()).trim();
        String rol = cbRol.getSelectedItem().toString();

        if (usr.isEmpty() || nom.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos del formulario.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (gestionUsuarios.agregarUsuario(new Usuario(usr, pass, nom, rol))) {
            cargarTabla();
            limpiarCampos();
            JOptionPane.showMessageDialog(this, "Usuario registrado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "El nombre de usuario ya se encuentra registrado.", "Duplicado", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void editarUsuario() {
        if (usuarioSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario de la tabla.", "Sin Selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nom = txtNombre.getText().trim();
        String rol = cbRol.getSelectedItem().toString();

        if (nom.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (gestionUsuarios.editarUsuario(usuarioSeleccionado, nom, rol)) {
            cargarTabla();
            limpiarCampos();
            JOptionPane.showMessageDialog(this, "Datos de usuario actualizados correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void cambiarPassword() {
        if (usuarioSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario de la tabla para cambiar su contraseña.", "Sin Selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nuevaPass = JOptionPane.showInputDialog(this, 
                "Ingrese la nueva contraseña para el usuario [" + usuarioSeleccionado + "]:", 
                "Cambio de Contraseña", JOptionPane.QUESTION_MESSAGE);

        if (nuevaPass != null && !nuevaPass.trim().isEmpty()) {
            if (gestionUsuarios.cambiarPassword(usuarioSeleccionado, nuevaPass.trim())) {
                txtPass.setText(nuevaPass.trim());
                cargarTabla();
                JOptionPane.showMessageDialog(this, "Contraseña actualizada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void alternarEstadoUsuario() {
        if (usuarioSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario de la tabla.", "Sin Selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Usuario u = gestionUsuarios.buscarPorUsername(usuarioSeleccionado);
        if (u == null) return;

        String accion = u.isActivo() ? "desactivar" : "reactivar";
        int opc = JOptionPane.showConfirmDialog(this,
                "¿Desea " + accion + " la cuenta del usuario: " + usuarioSeleccionado + "?",
                "Confirmar Cambio de Estado", JOptionPane.YES_NO_OPTION);

        if (opc == JOptionPane.YES_OPTION) {
            if (gestionUsuarios.cambiarEstadoUsuario(usuarioSeleccionado)) {
                cargarTabla();
                limpiarCampos();
                JOptionPane.showMessageDialog(this, "Estado del usuario actualizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void alternarVisibilidadPass() {
        mostrandoPass = !mostrandoPass;
        if (mostrandoPass) {
            txtPass.setEchoChar((char) 0);
        } else {
            txtPass.setEchoChar('•');
        }
    }

    private void limpiarCampos() {
        usuarioSeleccionado = null;
        txtUser.setText("");
        txtUser.setEditable(true);
        txtNombre.setText("");
        txtPass.setText("");
        txtPass.setEchoChar('•');
        mostrandoPass = false;
        cbRol.setSelectedIndex(0);
        btnEstado.setText("Desactivar");
        btnEstado.setBackground(new Color(176, 58, 46));
        tablaUsuarios.clearSelection();
    }

    private void regresarMenu() {
        this.dispose();
        new FrmPrincipal(usuarioSesion).setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmUsuarios(null).setVisible(true));
    }
}