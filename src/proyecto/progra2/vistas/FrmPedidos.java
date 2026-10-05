package proyecto.progra2.vistas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import proyecto.progra2.modelos.DetallePedido;
import proyecto.progra2.modelos.Pedido;

public class FrmPedidos extends JFrame {

    // Componentes de la interfaz
    private JTextField txtIdCliente;
    private JTextField txtIdProducto;
    private JTextField txtNombreProducto;
    private JTextField txtCantidad;
    private JTextField txtPrecio;
    private JButton btnAgregar;
    private JButton btnEliminar;
    private JButton btnProcesar;
    private JTable tblDetalles;
    private DefaultTableModel modeloTabla;
    private JLabel lblTotal;

    // Objeto principal de la lógica
    private Pedido pedidoActual;

    // Paleta de Colores Corporativa
    private static final Color COLOR_PRIMARY = new Color(24, 43, 73);      // Azul Marino
    private static final Color COLOR_BG_LIGHT = new Color(245, 247, 250);  // Gris Claro
    private static final Color COLOR_TEXT_DARK = new Color(33, 37, 41);   // Texto Oscuro

    // Colores de Botones con Alto Contraste
    private static final Color BTN_AGREGAR_BG = new Color(34, 139, 34);    // Verde Bosque Sólido
    private static final Color BTN_ELIMINAR_BG = new Color(178, 34, 34);   // Rojo Fuego Sólido
    private static final Color BTN_PROCESAR_BG = new Color(24, 43, 73);    // Azul Marino Sólido

    public FrmPedidos() {
        pedidoActual = new Pedido(1001, 1);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        setTitle("Sistema de Gestión de Pedidos - Punto de Venta");
        setSize(850, 680);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_BG_LIGHT);
        setLayout(new BorderLayout(15, 15));

        // --- 1. HEADER BANNER ---
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(COLOR_PRIMARY);
        panelHeader.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel lblTitulo = new JLabel("MÓDULO DE REGISTRO DE PEDIDOS");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblSubtitulo = new JLabel("Gestión de Compras y Detalles");
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
        panelCentro.setBorder(new EmptyBorder(0, 15, 0, 15));

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

        // Fila 0: ID Cliente y ID Producto
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.1;
        panelFormulario.add(crearLabel("ID Cliente:", fontLabel), gbc);

        gbc.gridx = 1; gbc.weightx = 0.4;
        txtIdCliente = crearTextField("1", fontText);
        panelFormulario.add(txtIdCliente, gbc);

        gbc.gridx = 2; gbc.weightx = 0.1;
        panelFormulario.add(crearLabel("ID Producto:", fontLabel), gbc);

        gbc.gridx = 3; gbc.weightx = 0.4;
        txtIdProducto = crearTextField("", fontText);
        panelFormulario.add(txtIdProducto, gbc);

        // Fila 1: Nombre Producto
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.1;
        panelFormulario.add(crearLabel("Producto:", fontLabel), gbc);

        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 0.9;
        txtNombreProducto = crearTextField("", fontText);
        panelFormulario.add(txtNombreProducto, gbc);

        // Fila 2: Cantidad y Precio
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.1;
        panelFormulario.add(crearLabel("Cantidad:", fontLabel), gbc);

        gbc.gridx = 1; gbc.weightx = 0.4;
        txtCantidad = crearTextField("", fontText);
        panelFormulario.add(txtCantidad, gbc);

        gbc.gridx = 2; gbc.weightx = 0.1;
        panelFormulario.add(crearLabel("Precio (Q):", fontLabel), gbc);

        gbc.gridx = 3; gbc.weightx = 0.4;
        txtPrecio = crearTextField("", fontText);
        panelFormulario.add(txtPrecio, gbc);

        // Fila 3: Botones de Acción sobre formulario
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 4;
        JPanel panelBotonesForm = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelBotonesForm.setOpaque(false);

        btnEliminar = new JButton("Eliminar Seleccionado");
        estilarBotonSolido(btnEliminar, BTN_ELIMINAR_BG);

        btnAgregar = new JButton("+ Agregar Ítem al Pedido");
        estilarBotonSolido(btnAgregar, BTN_AGREGAR_BG);

        panelBotonesForm.add(btnEliminar);
        panelBotonesForm.add(btnAgregar);
        panelFormulario.add(panelBotonesForm, gbc);

        panelCentro.add(panelFormulario, BorderLayout.NORTH);

        // Tabla de Detalles
        String[] columnas = {"ID Prod.", "Descripción del Producto", "Cantidad", "Precio Unitario", "Subtotal"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblDetalles = new JTable(modeloTabla);
        tblDetalles.setFont(fontText);
        tblDetalles.setRowHeight(28);
        tblDetalles.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tblDetalles.getTableHeader().setBackground(new Color(230, 235, 242));
        tblDetalles.getTableHeader().setForeground(COLOR_TEXT_DARK);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tblDetalles.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tblDetalles.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

        JScrollPane scrollTabla = new JScrollPane(tblDetalles);
        scrollTabla.getViewport().setBackground(Color.WHITE);
        scrollTabla.setBorder(BorderFactory.createLineBorder(new Color(220, 224, 230), 1));

        panelCentro.add(scrollTabla, BorderLayout.CENTER);
        add(panelCentro, BorderLayout.CENTER);

        // --- 3. PANEL INFERIOR ---
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBackground(Color.WHITE);
        panelInferior.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 224, 230)),
                new EmptyBorder(12, 20, 12, 20)
        ));

        btnProcesar = new JButton("Procesar y Guardar Pedido");
        estilarBotonSolido(btnProcesar, BTN_PROCESAR_BG);
        btnProcesar.setPreferredSize(new Dimension(220, 42));

        JPanel panelTotalCard = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        panelTotalCard.setOpaque(false);

        JLabel lblEtiqTotal = new JLabel("TOTAL A PAGAR:");
        lblEtiqTotal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblEtiqTotal.setForeground(COLOR_TEXT_DARK);

        lblTotal = new JLabel("Q 0.00");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTotal.setForeground(COLOR_PRIMARY);

        panelTotalCard.add(lblEtiqTotal);
        panelTotalCard.add(lblTotal);

        panelInferior.add(btnProcesar, BorderLayout.WEST);
        panelInferior.add(panelTotalCard, BorderLayout.EAST);

        add(panelInferior, BorderLayout.SOUTH);

        // Eventos
        btnAgregar.addActionListener(e -> agregarProducto());
        btnEliminar.addActionListener(e -> eliminarProductoSeleccionado());
        btnProcesar.addActionListener(e -> procesarPedido());
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

    // Método para forzar relleno de color sólido y letras blancas perfectamente legibles
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

    private void agregarProducto() {
        try {
            if (txtIdProducto.getText().trim().isEmpty() || txtNombreProducto.getText().trim().isEmpty()
                    || txtCantidad.getText().trim().isEmpty() || txtPrecio.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int idProd = Integer.parseInt(txtIdProducto.getText().trim());
            String nombre = txtNombreProducto.getText().trim();
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());
            double precio = Double.parseDouble(txtPrecio.getText().trim());

            DetallePedido detalle = new DetallePedido(idProd, nombre, cantidad, precio);
            pedidoActual.agregarDetalle(detalle);

            Object[] fila = {
                detalle.getIdProducto(),
                detalle.getNombreProducto(),
                detalle.getCantidad(),
                String.format("Q %.2f", detalle.getPrecioUnitario()),
                String.format("Q %.2f", detalle.getSubtotal())
            };
            modeloTabla.addRow(fila);

            lblTotal.setText(String.format("Q %.2f", pedidoActual.calcularTotal()));

            txtIdProducto.setText("");
            txtNombreProducto.setText("");
            txtCantidad.setText("");
            txtPrecio.setText("");
            txtIdProducto.requestFocus();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese valores numéricos válidos para ID, Cantidad y Precio.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarProductoSeleccionado() {
        int filaSeleccionada = tblDetalles.getSelectedRow();
        if (filaSeleccionada >= 0) {
            pedidoActual.getDetalles().remove(filaSeleccionada);
            modeloTabla.removeRow(filaSeleccionada);
            lblTotal.setText(String.format("Q %.2f", pedidoActual.calcularTotal()));
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un ítem de la tabla para eliminar.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void procesarPedido() {
        if (pedidoActual.getDetalles().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El pedido no contiene ítems para procesar.", "Pedido Vacío", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, 
            String.format("¡Pedido #%d registrado con éxito!\nTotal en Quetzales: Q %.2f\nÍtems procesados: %d", 
                pedidoActual.getIdPedido(), pedidoActual.calcularTotal(), pedidoActual.getDetalles().size()),
            "Confirmación de Venta", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmPedidos().setVisible(true));
    }
}