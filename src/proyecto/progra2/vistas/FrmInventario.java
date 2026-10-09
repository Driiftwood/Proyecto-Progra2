package proyecto.progra2.vistas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import proyecto.progra2.modelos.GestionInventario;
import proyecto.progra2.modelos.Producto;

public class FrmInventario extends JFrame {

    private JTextField txtIdProducto;
    private JTextField txtNombre;
    private JComboBox<String> cbCategoria;
    private JTextField txtStock;
    private JTextField txtPrecio;
    private JButton btnAgregar;
    private JButton btnLimpiar;
    private JTable tblProductos;
    private DefaultTableModel modeloTabla;

    private GestionInventario gestionInventario;

    private static final Color COLOR_PRIMARY = new Color(24, 43, 73);      // Azul Marino
    private static final Color COLOR_BG_LIGHT = new Color(245, 247, 250);  // Gris Claro
    private static final Color COLOR_TEXT_DARK = new Color(33, 37, 41);   // Texto Oscuro
    private static final Color BTN_AGREGAR_BG = new Color(34, 139, 34);    // Verde Bosque
    private static final Color BTN_LIMPIAR_BG = new Color(108, 117, 125);  // Gris Secundario

    public FrmInventario() {
        gestionInventario = new GestionInventario();

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        setTitle("Sistema de Gestión de Inventario - Productos");
        setSize(850, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_BG_LIGHT);
        setLayout(new BorderLayout(15, 15));

        // --- 1. BANNER SUPERIOR ---
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(COLOR_PRIMARY);
        panelHeader.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel lblTitulo = new JLabel("MÓDULO DE INVENTARIO Y PRODUCTOS");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblSubtitulo = new JLabel("Administración de Catálogo y Stock");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubtitulo.setForeground(new Color(200, 215, 235));

        JPanel panelTextoHeader = new JPanel(new GridLayout(2, 1));
        panelTextoHeader.setOpaque(false);
        panelTextoHeader.add(lblTitulo);
        panelTextoHeader.add(lblSubtitulo);

        panelHeader.add(panelTextoHeader, BorderLayout.WEST);
        add(panelHeader, BorderLayout.NORTH);

        // --- 2. CONTENIDO PRINCIPAL ---
        JPanel panelCentro = new JPanel(new BorderLayout(10, 15));
        panelCentro.setOpaque(false);
        panelCentro.setBorder(new EmptyBorder(0, 15, 15, 15));

        // Formulario
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(Color.WHITE);
        panelFormulario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 224, 230), 1),
                new EmptyBorder(15, 15, 15, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font fontLabel = new Font("Segoe UI", Font.BOLD, 12);
        Font fontText = new Font("Segoe UI", Font.PLAIN, 13);

        // Fila 0
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.1;
        panelFormulario.add(crearLabel("ID Producto:", fontLabel), gbc);

        gbc.gridx = 1; gbc.weightx = 0.4;
        txtIdProducto = crearTextField("", fontText);
        panelFormulario.add(txtIdProducto, gbc);

        gbc.gridx = 2; gbc.weightx = 0.1;
        panelFormulario.add(crearLabel("Categoría:", fontLabel), gbc);

        gbc.gridx = 3; gbc.weightx = 0.4;
        String[] categorias = {"Bebidas", "Repostería", "Snacks", "Platos Fuertes", "Otros"};
        cbCategoria = new JComboBox<>(categorias);
        cbCategoria.setFont(fontText);
        panelFormulario.add(cbCategoria, gbc);

        // Fila 1
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.1;
        panelFormulario.add(crearLabel("Nombre:", fontLabel), gbc);

        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 0.9;
        txtNombre = crearTextField("", fontText);
        panelFormulario.add(txtNombre, gbc);

        // Fila 2
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.1;
        panelFormulario.add(crearLabel("Stock Inicial:", fontLabel), gbc);

        gbc.gridx = 1; gbc.weightx = 0.4;
        txtStock = crearTextField("", fontText);
        panelFormulario.add(txtStock, gbc);

        gbc.gridx = 2; gbc.weightx = 0.1;
        panelFormulario.add(crearLabel("Precio (Q):", fontLabel), gbc);

        gbc.gridx = 3; gbc.weightx = 0.4;
        txtPrecio = crearTextField("", fontText);
        panelFormulario.add(txtPrecio, gbc);

        // Fila 3: Botones
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 4;
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelBotones.setOpaque(false);

        btnLimpiar = new JButton("Limpiar Campos");
        estilarBotonSolido(btnLimpiar, BTN_LIMPIAR_BG);

        btnAgregar = new JButton("+ Registrar Producto");
        estilarBotonSolido(btnAgregar, BTN_AGREGAR_BG);

        panelBotones.add(btnLimpiar);
        panelBotones.add(btnAgregar);
        panelFormulario.add(panelBotones, gbc);

        panelCentro.add(panelFormulario, BorderLayout.NORTH);

        // Tabla
        String[] columnas = {"ID", "Nombre del Producto", "Categoría", "Stock Disponible", "Precio Unitario"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblProductos = new JTable(modeloTabla);
        tblProductos.setFont(fontText);
        tblProductos.setRowHeight(28);
        tblProductos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tblProductos.getTableHeader().setBackground(new Color(230, 235, 242));
        tblProductos.getTableHeader().setForeground(COLOR_TEXT_DARK);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tblProductos.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tblProductos.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        tblProductos.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        JScrollPane scrollTabla = new JScrollPane(tblProductos);
        scrollTabla.getViewport().setBackground(Color.WHITE);
        scrollTabla.setBorder(BorderFactory.createLineBorder(new Color(220, 224, 230), 1));

        panelCentro.add(scrollTabla, BorderLayout.CENTER);
        add(panelCentro, BorderLayout.CENTER);

        // Cargar productos iniciales
        actualizarTabla();

        // Eventos
        btnAgregar.addActionListener(e -> registrarProducto());
        btnLimpiar.addActionListener(e -> limpiarCampos());
    }

    private JLabel crearLabel(String texto, Font font) {
        JLabel label = new JLabel(texto);
        label.setFont(font);
        label.setForeground(COLOR_TEXT_DARK);
        return label;
    }

    private JTextField crearTextField(String textoInicial, Font font) {
        JTextField textField = new JTextField(textoInicial);
        textField.setFont(font);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(190, 195, 200), 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        return textField;
    }

    private void estilarBotonSolido(JButton btn, Color bgColor) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
    }

    private void registrarProducto() {
        try {
            if (txtIdProducto.getText().trim().isEmpty() || txtNombre.getText().trim().isEmpty()
                    || txtStock.getText().trim().isEmpty() || txtPrecio.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son requeridos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int id = Integer.parseInt(txtIdProducto.getText().trim());
            String nombre = txtNombre.getText().trim();
            String cat = cbCategoria.getSelectedItem().toString();
            int stock = Integer.parseInt(txtStock.getText().trim());
            double precio = Double.parseDouble(txtPrecio.getText().trim());

            Producto p = new Producto(id, nombre, cat, stock, precio);
            gestionInventario.agregarProducto(p);

            actualizarTabla();
            limpiarCampos();

            JOptionPane.showMessageDialog(this, "Producto registrado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Asegúrese de ingresar números válidos para ID, Stock y Precio.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        for (Producto p : gestionInventario.getListaProductos()) {
            Object[] fila = {
                p.getIdProducto(),
                p.getNombre(),
                p.getCategoria(),
                p.getStock(),
                String.format("Q %.2f", p.getPrecio())
            };
            modeloTabla.addRow(fila);
        }
    }

    private void limpiarCampos() {
        txtIdProducto.setText("");
        txtNombre.setText("");
        txtStock.setText("");
        txtPrecio.setText("");
        cbCategoria.setSelectedIndex(0);
        txtIdProducto.requestFocus();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmInventario().setVisible(true));
    }
}
