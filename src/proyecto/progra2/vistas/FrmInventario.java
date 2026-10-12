package proyecto.progra2.vistas;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import proyecto.progra2.modelos.GestionInventario;
import proyecto.progra2.modelos.ProductoIndividual;
import proyecto.progra2.modelos.Usuario;

public class FrmInventario extends JFrame {

    private JTextField txtCodigo, txtNombre, txtPrecio, txtExistencia, txtAbastecer;
    private JComboBox<String> cbCategoria;
    private JButton btnGuardar, btnEditar, btnEliminar, btnLimpiar, btnAbastecer, btnRegresar;
    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;

    private GestionInventario gestionInventario;
    private Usuario usuarioSesion;
    private String codigoSeleccionado = null;

    private static final Color COLOR_PRIMARY = new Color(24, 43, 73);      
    private static final Color COLOR_BG_LIGHT = new Color(245, 247, 250);  
    private static final Color COLOR_TEXT_DARK = new Color(33, 37, 41);   
    private static final Color BTN_PRIMARY_BG = new Color(24, 43, 73);

    public FrmInventario() {
        this(null);
    }

    public FrmInventario(Usuario usuario) {
        this.usuarioSesion = usuario;
        gestionInventario = new GestionInventario();

        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}

        setTitle("Administración e Inventario de Productos");
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

        JLabel lblTitulo = new JLabel("MÓDULO DE INVENTARIO Y ABASTECIMIENTO");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Control de existencias, precios y catálogo de productos");
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

        // --- Panel Izquierdo ---
        JPanel panelIzquierdo = new JPanel(new BorderLayout(0, 10));
        panelIzquierdo.setOpaque(false);
        panelIzquierdo.setBorder(new EmptyBorder(10, 15, 10, 10));

        JPanel panelReg = new JPanel(new GridBagLayout());
        panelReg.setBackground(Color.WHITE);
        panelReg.setBorder(BorderFactory.createTitledBorder(new LineBorder(new Color(200, 205, 210), 1), "Datos del Producto"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 5, 4, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font fontLabel = new Font("Segoe UI", Font.BOLD, 11);
        Font fontText = new Font("Segoe UI", Font.PLAIN, 12);

        gbc.gridx = 0; gbc.gridy = 0; panelReg.add(new JLabel("Código:"), gbc);
        gbc.gridx = 1; txtCodigo = crearCampoTexto(fontText); panelReg.add(txtCodigo, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panelReg.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; txtNombre = crearCampoTexto(fontText); panelReg.add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panelReg.add(new JLabel("Categoría:"), gbc);
        gbc.gridx = 1; 
        cbCategoria = new JComboBox<>(new String[]{"SANDWICH", "BEBIDA", "ACOMPANAMIENTO"});
        cbCategoria.setFont(fontText);
        panelReg.add(cbCategoria, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panelReg.add(new JLabel("Precio (Q):"), gbc);
        gbc.gridx = 1; txtPrecio = crearCampoTexto(fontText); panelReg.add(txtPrecio, gbc);

        gbc.gridx = 0; gbc.gridy = 4; panelReg.add(new JLabel("Stock Inicial:"), gbc);
        gbc.gridx = 1; txtExistencia = crearCampoTexto(fontText); panelReg.add(txtExistencia, gbc);

        JPanel panelGridBotones = new JPanel(new GridLayout(2, 2, 5, 5));
        panelGridBotones.setOpaque(false);

        btnGuardar = crearBoton("Guardar", BTN_PRIMARY_BG, Color.WHITE);
        btnEditar = crearBoton("Editar", new Color(40, 116, 166), Color.WHITE);
        btnEliminar = crearBoton("Eliminar", new Color(176, 58, 46), Color.WHITE);
        btnLimpiar = crearBoton("Limpiar", new Color(220, 224, 230), COLOR_TEXT_DARK);

        panelGridBotones.add(btnGuardar);
        panelGridBotones.add(btnEditar);
        panelGridBotones.add(btnEliminar);
        panelGridBotones.add(btnLimpiar);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 5, 5, 5);
        panelReg.add(panelGridBotones, gbc);

        panelIzquierdo.add(panelReg, BorderLayout.CENTER);

        JPanel panelAbast = new JPanel(new GridBagLayout());
        panelAbast.setBackground(Color.WHITE);
        panelAbast.setBorder(BorderFactory.createTitledBorder(new LineBorder(new Color(200, 205, 210), 1), "Abastecer Existencias"));

        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(5, 5, 5, 5);
        gbc2.fill = GridBagConstraints.HORIZONTAL;

        gbc2.gridx = 0; gbc2.gridy = 0; panelAbast.add(new JLabel("Cantidad +:"), gbc2);
        gbc2.gridx = 1; txtAbastecer = crearCampoTexto(fontText); panelAbast.add(txtAbastecer, gbc2);

        btnAbastecer = crearBoton("Ingresar Stock", new Color(34, 153, 84), Color.WHITE);
        gbc2.gridx = 0; gbc2.gridy = 1; gbc2.gridwidth = 2; panelAbast.add(btnAbastecer, gbc2);

        panelIzquierdo.add(panelAbast, BorderLayout.SOUTH);

        add(panelIzquierdo, BorderLayout.WEST);

        // --- Panel Derecho: Tabla ---
        JPanel panelTabla = new JPanel(new BorderLayout(5, 5));
        panelTabla.setOpaque(false);
        panelTabla.setBorder(new EmptyBorder(10, 5, 10, 15));

        String[] columnas = {"Código", "Nombre", "Categoría", "Precio (Q)", "Existencia", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tablaProductos = new JTable(modeloTabla);
        tablaProductos.setFont(fontText);
        tablaProductos.setRowHeight(25);
        tablaProductos.getTableHeader().setFont(fontLabel);

        tablaProductos.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { seleccionarFila(); }
        });

        panelTabla.add(new JScrollPane(tablaProductos), BorderLayout.CENTER);
        add(panelTabla, BorderLayout.CENTER);

        btnGuardar.addActionListener(e -> guardarProducto());
        btnEditar.addActionListener(e -> editarProducto());
        btnEliminar.addActionListener(e -> eliminarProducto());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnAbastecer.addActionListener(e -> abastecer());

        cargarTabla();
    }

    private JTextField crearCampoTexto(Font font) {
        JTextField field = new JTextField();
        field.setFont(font);
        field.setPreferredSize(new Dimension(140, 26));
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
        btn.setPreferredSize(new Dimension(90, 30));
        return btn;
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        for (ProductoIndividual p : gestionInventario.getListaProductos()) {
            modeloTabla.addRow(new Object[]{
                p.getCodigo(), p.getNombre(), p.getCategoria(),
                String.format("%.2f", p.getPrecio()), p.getExistencia(),
                p.isActivo() ? "Activo" : "Inactivo"
            });
        }
    }

    private void seleccionarFila() {
        int fila = tablaProductos.getSelectedRow();
        if (fila >= 0) {
            codigoSeleccionado = modeloTabla.getValueAt(fila, 0).toString();
            txtCodigo.setText(codigoSeleccionado);
            txtCodigo.setEditable(false);
            txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
            cbCategoria.setSelectedItem(modeloTabla.getValueAt(fila, 2).toString());
            txtPrecio.setText(modeloTabla.getValueAt(fila, 3).toString().replace(",", "."));
            txtExistencia.setText(modeloTabla.getValueAt(fila, 4).toString());
            txtExistencia.setEditable(false);
        }
    }

    private void guardarProducto() {
        try {
            String cod = txtCodigo.getText().trim();
            String nom = txtNombre.getText().trim();
            String cat = cbCategoria.getSelectedItem().toString();
            double pre = Double.parseDouble(txtPrecio.getText().trim());
            int exi = Integer.parseInt(txtExistencia.getText().trim());

            if (cod.isEmpty() || nom.isEmpty() || pre < 0 || exi < 0) {
                JOptionPane.showMessageDialog(this, "Campos obligatorios vacíos o valores negativos.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (gestionInventario.buscarProducto(cod) != null) {
                JOptionPane.showMessageDialog(this, "Ya existe un producto con el mismo código.", "Duplicado", JOptionPane.WARNING_MESSAGE);
                return;
            }

            gestionInventario.agregarProducto(new ProductoIndividual(cod, nom, cat, pre, exi));
            cargarTabla();
            limpiarCampos();
            JOptionPane.showMessageDialog(this, "Producto registrado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Revise los campos numéricos de Precio y Stock.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarProducto() {
        if (codigoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Si la sesión activa es de un Cajero, exige clave de Administrador
        if (usuarioSesion != null && "Cajero".equalsIgnoreCase(usuarioSesion.getRol())) {
            if (!DialogAutorizacion.solicitarAutorizacion(this)) return;
        }

        try {
            String nom = txtNombre.getText().trim();
            String cat = cbCategoria.getSelectedItem().toString();
            double pre = Double.parseDouble(txtPrecio.getText().trim());

            if (nom.isEmpty() || pre < 0) {
                JOptionPane.showMessageDialog(this, "El nombre y precio deben ser válidos.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (gestionInventario.editarProducto(codigoSeleccionado, nom, cat, pre)) {
                cargarTabla();
                limpiarCampos();
                JOptionPane.showMessageDialog(this, "Producto actualizado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un precio válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarProducto() {
        if (codigoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Si la sesión activa es de un Cajero, exige clave de Administrador
        if (usuarioSesion != null && "Cajero".equalsIgnoreCase(usuarioSesion.getRol())) {
            if (!DialogAutorizacion.solicitarAutorizacion(this)) return;
        }

        int opc = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar el producto: " + codigoSeleccionado + "?",
                "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

        if (opc == JOptionPane.YES_OPTION) {
            if (gestionInventario.eliminarProducto(codigoSeleccionado)) {
                cargarTabla();
                limpiarCampos();
                JOptionPane.showMessageDialog(this, "Producto eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void abastecer() {
        if (codigoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto de la tabla para reabastecer.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            int cant = Integer.parseInt(txtAbastecer.getText().trim());
            if (gestionInventario.abastecerInventario(codigoSeleccionado, cant)) {
                cargarTabla();
                txtAbastecer.setText("");
                limpiarCampos();
                JOptionPane.showMessageDialog(this, "Inventario abastecido con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a 0.", "Error", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ingrese una cantidad entera válida.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        codigoSeleccionado = null;
        txtCodigo.setText(""); txtCodigo.setEditable(true);
        txtNombre.setText(""); txtPrecio.setText("");
        txtExistencia.setText(""); txtExistencia.setEditable(true);
        txtAbastecer.setText("");
        tablaProductos.clearSelection();
    }

    private void regresarMenu() {
        this.dispose();
        new FrmPrincipal(usuarioSesion).setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmInventario(null).setVisible(true));
    }
}
