package proyecto.progra2.vistas;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import proyecto.progra2.modelos.Cliente;
import proyecto.progra2.modelos.GestionClientes;
import proyecto.progra2.modelos.Usuario;

public class FrmClientes extends JFrame {

    private JTextField txtNit, txtNombre, txtDireccion, txtTelefono, txtPuntos;
    private JButton btnGuardar, btnEditar, btnEstado, btnLimpiar, btnRegresar;
    private JTable tablaClientes;
    private DefaultTableModel modeloTabla;

    private GestionClientes gestionClientes;
    private Usuario usuarioSesion;
    private String nitSeleccionado = null;

    private static final Color COLOR_PRIMARY = new Color(24, 43, 73);      
    private static final Color COLOR_BG_LIGHT = new Color(245, 247, 250);  
    private static final Color COLOR_TEXT_DARK = new Color(33, 37, 41);   
    private static final Color BTN_PRIMARY_BG = new Color(24, 43, 73);

    public FrmClientes() {
        this(null);
    }

    public FrmClientes(Usuario usuario) {
        this.usuarioSesion = usuario;
        gestionClientes = new GestionClientes();

        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}

        setTitle("Administración de Clientes y Fidelización");
        setSize(980, 580);
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

        JLabel lblTitulo = new JLabel("MÓDULO DE CLIENTES Y FIDELIZACIÓN");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Registro de clientes, saldo de puntos y estado de cuenta");
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
        Font fontText = new Font("Segoe UI", Font.PLAIN, 13);

        gbc.gridx = 0; gbc.gridy = 0; panelForm.add(new JLabel("NIT / DPI:"), gbc);
        gbc.gridx = 1; txtNit = crearCampoTexto(fontText); panelForm.add(txtNit, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panelForm.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; txtNombre = crearCampoTexto(fontText); panelForm.add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panelForm.add(new JLabel("Dirección:"), gbc);
        gbc.gridx = 1; txtDireccion = crearCampoTexto(fontText); panelForm.add(txtDireccion, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panelForm.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1; txtTelefono = crearCampoTexto(fontText); panelForm.add(txtTelefono, gbc);

        gbc.gridx = 0; gbc.gridy = 4; panelForm.add(new JLabel("Saldo Puntos:"), gbc);
        gbc.gridx = 1; txtPuntos = crearCampoTexto(fontText);
        txtPuntos.setText("0"); txtPuntos.setEditable(false);
        txtPuntos.setBackground(new Color(235, 240, 245));
        panelForm.add(txtPuntos, gbc);

        JPanel panelAccionesForm = new JPanel(new GridLayout(2, 2, 8, 8));
        panelAccionesForm.setOpaque(false);

        btnGuardar = crearBoton("Guardar", BTN_PRIMARY_BG, Color.WHITE);
        btnEditar = crearBoton("Editar", new Color(40, 116, 166), Color.WHITE);
        btnEstado = crearBoton("Desactivar", new Color(176, 58, 46), Color.WHITE);
        btnLimpiar = crearBoton("Limpiar", new Color(220, 224, 230), COLOR_TEXT_DARK);

        panelAccionesForm.add(btnGuardar);
        panelAccionesForm.add(btnEditar);
        panelAccionesForm.add(btnEstado);
        panelAccionesForm.add(btnLimpiar);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 6, 8, 6);
        panelForm.add(panelAccionesForm, gbc);

        add(panelForm, BorderLayout.WEST);

        JPanel panelTabla = new JPanel(new BorderLayout(5, 5));
        panelTabla.setOpaque(false);
        panelTabla.setBorder(new EmptyBorder(15, 5, 15, 15));

        String[] columnas = {"NIT / DPI", "Nombre", "Dirección", "Teléfono", "Puntos", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tablaClientes = new JTable(modeloTabla);
        tablaClientes.setFont(fontText);
        tablaClientes.setRowHeight(26);
        tablaClientes.getTableHeader().setFont(fontLabel);

        tablaClientes.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { seleccionarFila(); }
        });

        panelTabla.add(new JScrollPane(tablaClientes), BorderLayout.CENTER);
        add(panelTabla, BorderLayout.CENTER);

        btnGuardar.addActionListener(e -> guardarCliente());
        btnEditar.addActionListener(e -> editarCliente());
        btnEstado.addActionListener(e -> alternarEstadoCliente());
        btnLimpiar.addActionListener(e -> limpiarCampos());

        cargarTabla();
    }

    private JTextField crearCampoTexto(Font font) {
        JTextField field = new JTextField();
        field.setFont(font);
        field.setPreferredSize(new Dimension(190, 30));
        field.setBorder(new CompoundBorder(new LineBorder(new Color(190, 195, 200), 1), new EmptyBorder(4, 8, 4, 8)));
        return field;
    }

    private JButton crearBoton(String texto, Color bg, Color fg) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(95, 32));
        return btn;
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        for (Cliente c : gestionClientes.getListaClientes()) {
            modeloTabla.addRow(new Object[]{
                c.getNit(), c.getNombre(), c.getDireccion(), c.getTelefono(),
                c.getPuntos(), c.isActivo() ? "Activo" : "Inactivo"
            });
        }
    }

    private void seleccionarFila() {
        int fila = tablaClientes.getSelectedRow();
        if (fila >= 0) {
            nitSeleccionado = modeloTabla.getValueAt(fila, 0).toString();
            txtNit.setText(nitSeleccionado);
            txtNit.setEditable(false);
            txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
            txtDireccion.setText(modeloTabla.getValueAt(fila, 2).toString());
            txtTelefono.setText(modeloTabla.getValueAt(fila, 3).toString());
            txtPuntos.setText(modeloTabla.getValueAt(fila, 4).toString());

            Cliente c = gestionClientes.buscarPorNit(nitSeleccionado);
            if (c != null) {
                if (c.isActivo()) {
                    btnEstado.setText("Desactivar");
                    btnEstado.setBackground(new Color(176, 58, 46));
                } else {
                    btnEstado.setText("Reactivar");
                    btnEstado.setBackground(new Color(34, 153, 84));
                }
            }
        }
    }

    private void guardarCliente() {
        String nit = txtNit.getText().trim();
        String nombre = txtNombre.getText().trim();
        String dir = txtDireccion.getText().trim();
        String tel = txtTelefono.getText().trim();

        if (nit.isEmpty() || nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar al menos el NIT y el Nombre.", "Campos Requeridos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Cliente nuevo = new Cliente(nit, nombre, dir, tel);
        if (gestionClientes.agregarCliente(nuevo)) {
            cargarTabla();
            limpiarCampos();
            JOptionPane.showMessageDialog(this, "Cliente registrado exitosamente (Saldo inicial: 0 Puntos).", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Ya existe un cliente registrado con ese NIT/DPI.", "Duplicado", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void editarCliente() {
        if (nitSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente de la tabla.", "Sin Selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (usuarioSesion != null && "Cajero".equalsIgnoreCase(usuarioSesion.getRol())) {
            if (!DialogAutorizacion.solicitarAutorizacion(this)) return;
        }

        String nombre = txtNombre.getText().trim();
        String dir = txtDireccion.getText().trim();
        String tel = txtTelefono.getText().trim();

        if (gestionClientes.editarCliente(nitSeleccionado, nombre, dir, tel)) {
            cargarTabla();
            limpiarCampos();
            JOptionPane.showMessageDialog(this, "Cliente actualizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "No se puede editar un cliente desactivado.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void alternarEstadoCliente() {
        if (nitSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente de la tabla.", "Sin Selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (usuarioSesion != null && "Cajero".equalsIgnoreCase(usuarioSesion.getRol())) {
            if (!DialogAutorizacion.solicitarAutorizacion(this)) return;
        }

        Cliente c = gestionClientes.buscarPorNit(nitSeleccionado);
        if (c == null) return;

        String accion = c.isActivo() ? "desactivar" : "reactivar";
        int opc = JOptionPane.showConfirmDialog(this,
                "¿Desea " + accion + " al cliente con NIT: " + nitSeleccionado + "?",
                "Confirmar Cambio de Estado", JOptionPane.YES_NO_OPTION);

        if (opc == JOptionPane.YES_OPTION) {
            if (gestionClientes.cambiarEstadoCliente(nitSeleccionado)) {
                cargarTabla();
                limpiarCampos();
                JOptionPane.showMessageDialog(this, "El cliente ha sido " + (c.isActivo() ? "reactivado" : "desactivado") + " exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void limpiarCampos() {
        nitSeleccionado = null;
        txtNit.setText(""); txtNit.setEditable(true);
        txtNombre.setText(""); txtDireccion.setText(""); txtTelefono.setText("");
        txtPuntos.setText("0");
        btnEstado.setText("Desactivar"); btnEstado.setBackground(new Color(176, 58, 46));
        tablaClientes.clearSelection();
    }

    private void regresarMenu() {
        this.dispose();
        new FrmPrincipal(usuarioSesion).setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmClientes(null).setVisible(true));
    }
}