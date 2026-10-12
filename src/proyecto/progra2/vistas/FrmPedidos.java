package proyecto.progra2.vistas;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import proyecto.progra2.conexion.ConexionOracle;
import proyecto.progra2.modelos.*;

public class FrmPedidos extends JFrame {

    private JTextField txtBuscarNit, txtNombreCliente, txtPuntosCliente, txtCodigoCupon;
    private JSpinner spCantidad;
    private JComboBox<Object> cbProductos;
    private JTable tablaDetalle;
    private DefaultTableModel modeloTabla;
    private JLabel lblSubtotal, lblTotal, lblPuntosGanados, lblCuponAplicado;
    private JButton btnBuscarCliente, btnAgregarItem, btnEliminarItem, btnCobrarEfectivo, btnCobrarTarjeta, btnCobrarMixto, btnCobrarCupon, btnAplicarCupon, btnRegresar;

    private GestionClientes gestionClientes;
    private GestionInventario gestionInventario;

    private Cliente clienteActual;
    private Pedido pedidoActual;
    private Usuario usuarioSesion;
    private boolean cuponAplicado = false;
    private String codigoCuponValidado = "";

    private static final Color COLOR_PRIMARY = new Color(24, 43, 73);     
    private static final Color COLOR_BG_LIGHT = new Color(245, 247, 250);  

    public FrmPedidos() {
        this(null);
    }

    public FrmPedidos(Usuario usuario) {
        this.usuarioSesion = usuario;
        gestionClientes = new GestionClientes();
        gestionInventario = new GestionInventario();

        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}

        setTitle("Módulo de Ventas, Pedidos y Cobro con Canje de Cupones");
        setSize(1120, 740);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_BG_LIGHT);
        setLayout(new BorderLayout(12, 12));

        // --- Header ---
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(COLOR_PRIMARY);
        panelHeader.setBorder(new EmptyBorder(15, 20, 15, 20));

        JPanel panelTitulos = new JPanel();
        panelTitulos.setOpaque(false);
        panelTitulos.setLayout(new BoxLayout(panelTitulos, BoxLayout.Y_AXIS));

        JLabel lblTitulo = new JLabel("MÓDULO DE GESTIÓN DE PEDIDOS Y COBRO");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Selección de cliente, detalle de orden, acumulación de puntos y validación de cupones de canje");
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
        panelIzquierdo.setPreferredSize(new Dimension(420, 0));
        panelIzquierdo.setBorder(new EmptyBorder(10, 15, 10, 5));

        Font fontLabel = new Font("Segoe UI", Font.BOLD, 12);
        Font fontText = new Font("Segoe UI", Font.PLAIN, 12);

        JPanel panelCards = new JPanel();
        panelCards.setLayout(new BoxLayout(panelCards, BoxLayout.Y_AXIS));
        panelCards.setOpaque(false);

        // Card 1: Cliente
        JPanel panelCliente = new JPanel(new GridBagLayout());
        panelCliente.setBackground(Color.WHITE);
        panelCliente.setBorder(BorderFactory.createTitledBorder(new LineBorder(new Color(200, 205, 210), 1), "1. Datos del Cliente", 0, 0, fontLabel, COLOR_PRIMARY));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.0; panelCliente.add(new JLabel("NIT / DPI:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; txtBuscarNit = crearCampoTexto(fontText); panelCliente.add(txtBuscarNit, gbc);

        btnBuscarCliente = crearBoton("Buscar", COLOR_PRIMARY, Color.WHITE);
        btnBuscarCliente.addActionListener(e -> buscarCliente());
        gbc.gridx = 2; gbc.weightx = 0.0; panelCliente.add(btnBuscarCliente, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0; panelCliente.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2; gbc.weightx = 1.0;
        txtNombreCliente = crearCampoTexto(fontText);
        txtNombreCliente.setEditable(false);
        panelCliente.add(txtNombreCliente, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1; gbc.weightx = 0.0; panelCliente.add(new JLabel("Puntos:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2; gbc.weightx = 1.0;
        txtPuntosCliente = crearCampoTexto(fontText);
        txtPuntosCliente.setEditable(false);
        panelCliente.add(txtPuntosCliente, gbc);

        panelCards.add(panelCliente);
        panelCards.add(Box.createRigidArea(new Dimension(0, 10)));

        // Card 2: Validar Cupón App Móvil
        JPanel panelCupon = new JPanel(new GridBagLayout());
        panelCupon.setBackground(Color.WHITE);
        panelCupon.setBorder(BorderFactory.createTitledBorder(new LineBorder(new Color(200, 205, 210), 1), "2. Validar Cupón App Móvil", 0, 0, fontLabel, COLOR_PRIMARY));

        GridBagConstraints gbcC = new GridBagConstraints();
        gbcC.insets = new Insets(6, 8, 6, 8);
        gbcC.fill = GridBagConstraints.HORIZONTAL;

        gbcC.gridx = 0; gbcC.gridy = 0; gbcC.weightx = 0.0; panelCupon.add(new JLabel("Cupón:"), gbcC);
        gbcC.gridx = 1; gbcC.weightx = 1.0; txtCodigoCupon = crearCampoTexto(fontText); panelCupon.add(txtCodigoCupon, gbcC);

        btnAplicarCupon = crearBoton("Aplicar", new Color(142, 68, 173), Color.WHITE);
        btnAplicarCupon.addActionListener(e -> validarYAplicarCupon());
        gbcC.gridx = 2; gbcC.weightx = 0.0; panelCupon.add(btnAplicarCupon, gbcC);

        lblCuponAplicado = new JLabel("Ningún cupón aplicado");
        lblCuponAplicado.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblCuponAplicado.setForeground(Color.GRAY);
        gbcC.gridx = 0; gbcC.gridy = 1; gbcC.gridwidth = 3;
        panelCupon.add(lblCuponAplicado, gbcC);

        panelCards.add(panelCupon);
        panelCards.add(Box.createRigidArea(new Dimension(0, 10)));

        // Card 3: Productos
        JPanel panelProd = new JPanel(new GridBagLayout());
        panelProd.setBackground(Color.WHITE);
        panelProd.setBorder(BorderFactory.createTitledBorder(new LineBorder(new Color(200, 205, 210), 1), "3. Selección de Producto / Menú", 0, 0, fontLabel, COLOR_PRIMARY));

        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(6, 8, 6, 8);
        gbc2.fill = GridBagConstraints.HORIZONTAL;

        gbc2.gridx = 0; gbc2.gridy = 0; gbc2.weightx = 0.0; panelProd.add(new JLabel("Producto:"), gbc2);
        gbc2.gridx = 1; gbc2.gridwidth = 2; gbc2.weightx = 1.0;
        cbProductos = new JComboBox<>();
        cbProductos.setFont(fontText);
        cbProductos.setPreferredSize(new Dimension(200, 28));
        panelProd.add(cbProductos, gbc2);

        gbc2.gridx = 0; gbc2.gridy = 1; gbc2.gridwidth = 1; gbc2.weightx = 0.0; panelProd.add(new JLabel("Cantidad:"), gbc2);
        gbc2.gridx = 1; gbc2.gridwidth = 2; gbc2.weightx = 1.0;
        
        SpinnerNumberModel modelSpinner = new SpinnerNumberModel(1, 1, 999, 1);
        spCantidad = new JSpinner(modelSpinner);
        spCantidad.setFont(fontText);
        spCantidad.setPreferredSize(new Dimension(100, 28));
        panelProd.add(spCantidad, gbc2);

        btnAgregarItem = crearBoton("Agregar al Pedido", new Color(34, 153, 84), Color.WHITE);
        btnAgregarItem.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnAgregarItem.setPreferredSize(new Dimension(0, 34));
        btnAgregarItem.addActionListener(e -> agregarItem());
        gbc2.gridx = 0; gbc2.gridy = 2; gbc2.gridwidth = 3;
        gbc2.insets = new Insets(10, 8, 8, 8);
        panelProd.add(btnAgregarItem, gbc2);

        panelCards.add(panelProd);

        panelIzquierdo.add(panelCards, BorderLayout.NORTH);
        add(panelIzquierdo, BorderLayout.WEST);

        // --- Panel Derecho: Tabla y Resumen Totales ---
        JPanel panelDerecho = new JPanel(new BorderLayout(8, 8));
        panelDerecho.setOpaque(false);
        panelDerecho.setBorder(new EmptyBorder(10, 5, 10, 15));

        String[] columnas = {"Producto / Menú", "Precio Unid.", "Cantidad", "Subtotal", "Puntos"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tablaDetalle = new JTable(modeloTabla);
        tablaDetalle.setFont(fontText);
        tablaDetalle.setRowHeight(28);
        tablaDetalle.getTableHeader().setFont(fontLabel);

        panelDerecho.add(new JScrollPane(tablaDetalle), BorderLayout.CENTER);

        // Panel Inferior: Totales
        JPanel panelInferior = new JPanel(new BorderLayout(10, 10));
        panelInferior.setOpaque(false);

        JPanel panelTotales = new JPanel(new GridLayout(3, 1, 5, 5));
        panelTotales.setBackground(Color.WHITE);
        panelTotales.setBorder(new CompoundBorder(new LineBorder(new Color(200, 205, 210), 1), new EmptyBorder(12, 18, 12, 18)));

        lblSubtotal = new JLabel("Subtotal: Q0.00");
        lblSubtotal.setFont(new Font("Segoe UI", Font.BOLD, 13));

        lblPuntosGanados = new JLabel("Puntos que generará la compra: +0 pts");
        lblPuntosGanados.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPuntosGanados.setForeground(new Color(20, 110, 180));

        lblTotal = new JLabel("TOTAL A PAGAR: Q0.00");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTotal.setForeground(new Color(180, 40, 40));

        panelTotales.add(lblSubtotal);
        panelTotales.add(lblPuntosGanados);
        panelTotales.add(lblTotal);

        JPanel panelBotonesPago = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 6));
        panelBotonesPago.setOpaque(false);

        btnEliminarItem = crearBoton("Quitar Ítem", new Color(176, 58, 46), Color.WHITE);
        btnEliminarItem.setPreferredSize(new Dimension(100, 36));
        btnEliminarItem.addActionListener(e -> eliminarItem());

        btnCobrarCupon = crearBoton("Canjear Cupón", new Color(142, 68, 173), Color.WHITE);
        btnCobrarCupon.setPreferredSize(new Dimension(125, 36));
        btnCobrarCupon.addActionListener(e -> cobrarConCupon());

        btnCobrarEfectivo = crearBoton("Efectivo", new Color(34, 153, 84), Color.WHITE);
        btnCobrarEfectivo.setPreferredSize(new Dimension(100, 36));
        btnCobrarEfectivo.addActionListener(e -> cobrarEfectivo());

        btnCobrarTarjeta = crearBoton("Tarjeta", new Color(40, 116, 166), Color.WHITE);
        btnCobrarTarjeta.setPreferredSize(new Dimension(100, 36));
        btnCobrarTarjeta.addActionListener(e -> cobrarTarjeta());

        btnCobrarMixto = crearBoton("Mixto", new Color(70, 80, 95), Color.WHITE);
        btnCobrarMixto.setPreferredSize(new Dimension(95, 36));
        btnCobrarMixto.addActionListener(e -> cobrarMixto());

        panelBotonesPago.add(btnEliminarItem);
        panelBotonesPago.add(btnCobrarCupon);
        panelBotonesPago.add(btnCobrarEfectivo);
        panelBotonesPago.add(btnCobrarTarjeta);
        panelBotonesPago.add(btnCobrarMixto);

        panelInferior.add(panelTotales, BorderLayout.CENTER);
        panelInferior.add(panelBotonesPago, BorderLayout.SOUTH);

        panelDerecho.add(panelInferior, BorderLayout.SOUTH);

        add(panelDerecho, BorderLayout.CENTER);

        cargarCatalogo();
        iniciarConsumidorFinal();
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

    private void cargarCatalogo() {
        cbProductos.removeAllItems();
        for (ProductoIndividual p : gestionInventario.getListaProductos()) {
            if (p.isActivo()) cbProductos.addItem(p);
        }
        for (MenuCompleto m : gestionInventario.getListaMenus()) {
            if (m.isActivo()) cbProductos.addItem(m);
        }
    }

    private void iniciarConsumidorFinal() {
        clienteActual = gestionClientes.buscarPorNit("CF");
        if (clienteActual != null) {
            txtBuscarNit.setText(clienteActual.getNit());
            txtNombreCliente.setText(clienteActual.getNombre());
            txtPuntosCliente.setText(String.valueOf(clienteActual.getPuntos()));
            pedidoActual = new Pedido(clienteActual);
        }
    }

    private void buscarCliente() {
        String nit = txtBuscarNit.getText().trim();
        Cliente c = gestionClientes.buscarPorNit(nit);
        if (c != null) {
            if (!c.isActivo()) {
                JOptionPane.showMessageDialog(this, "El cliente se encuentra desactivado.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            clienteActual = c;
            txtNombreCliente.setText(c.getNombre());
            txtPuntosCliente.setText(String.valueOf(c.getPuntos()));
            pedidoActual = new Pedido(clienteActual);
            cuponAplicado = false;
            lblCuponAplicado.setText("Ningún cupón aplicado");
            lblCuponAplicado.setForeground(Color.GRAY);
            actualizarTabla();
        } else {
            JOptionPane.showMessageDialog(this, "Cliente no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void validarYAplicarCupon() {
        String codigo = txtCodigoCupon.getText().trim();

        if (codigo.isEmpty() || !codigo.toUpperCase().startsWith("PAN-")) {
            JOptionPane.showMessageDialog(this, "Ingrese un código de cupón válido de la App Móvil (Ej: PAN-8K39A).", "Cupón Inválido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!validarPedido()) return;

        cuponAplicado = true;
        codigoCuponValidado = codigo.toUpperCase();
        lblCuponAplicado.setText("✓ Cupón " + codigoCuponValidado + " APLICADO (100% Descuento)");
        lblCuponAplicado.setForeground(new Color(34, 153, 84));

        actualizarTabla();
        JOptionPane.showMessageDialog(this, "¡Cupón " + codigoCuponValidado + " validado correctamente!\nSe ha aplicado un 100% de descuento al producto/menú canjeado.", "Cupón Validado", JOptionPane.INFORMATION_MESSAGE);
    }

    private void agregarItem() {
        if (clienteActual == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ItemVenta itemSeleccionado = (ItemVenta) cbProductos.getSelectedItem();
        if (itemSeleccionado == null) return;

        try {
            int cant = (Integer) spCantidad.getValue();
            if (cant <= 0) {
                JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a 0.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (itemSeleccionado instanceof ProductoIndividual) {
                ProductoIndividual p = (ProductoIndividual) itemSeleccionado;
                if (p.getExistencia() < cant) {
                    JOptionPane.showMessageDialog(this, "Stock insuficiente. Unidades disponibles: " + p.getExistencia(), "Inventario", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            pedidoActual.agregarDetalle(itemSeleccionado, cant);
            actualizarTabla();
            spCantidad.setValue(1);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ingrese una cantidad válida.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarItem() {
        int fila = tablaDetalle.getSelectedRow();
        if (fila >= 0) {
            pedidoActual.removerDetalle(fila);
            actualizarTabla();
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un ítem de la tabla para quitar.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        if (pedidoActual == null) return;

        for (DetallePedido d : pedidoActual.getDetalles()) {
            modeloTabla.addRow(new Object[]{
                d.getItem().getNombre(),
                String.format("Q%.2f", d.getItem().getPrecio()),
                d.getCantidad(),
                String.format("Q%.2f", d.getSubtotal()),
                "+" + d.getPuntos() + " pts"
            });
        }

        double totalOriginal = pedidoActual.calcularTotal();
        double totalPagar = cuponAplicado ? 0.00 : totalOriginal;

        lblSubtotal.setText(String.format("Subtotal: Q%.2f", totalOriginal));
        lblPuntosGanados.setText("Puntos que generará la compra: +" + (cuponAplicado ? 0 : pedidoActual.calcularPuntosTotales()) + " pts");
        lblTotal.setText(String.format("TOTAL A PAGAR: Q%.2f %s", totalPagar, cuponAplicado ? "(DESCUENTO CANJE 100%)" : ""));
    }

    private void cobrarConCupon() {
        if (!validarPedido()) return;

        if (!cuponAplicado) {
            validarYAplicarCupon();
            if (!cuponAplicado) return;
        }

        pedidoActual.setMetodoPago("CANJE DE CUPÓN APP (" + codigoCuponValidado + ")");
        confirmarVenta("CUPÓN VALIDADO Y PROCESADO EN CAJA.\n"
                     + "Código de Cupón: " + codigoCuponValidado + "\n"
                     + "Descuento Aplicado: 100%\n"
                     + "Total Cobrado en Efectivo: Q0.00");
    }

    private void cobrarEfectivo() {
        if (!validarPedido()) return;

        double total = cuponAplicado ? 0.00 : pedidoActual.calcularTotal();
        String input = JOptionPane.showInputDialog(this, String.format("Total a pagar: Q%.2f\nIngrese el efectivo recibido (Q):", total), "Pago en Efectivo", JOptionPane.QUESTION_MESSAGE);

        if (input == null) return;

        try {
            double efectivo = Double.parseDouble(input.trim());
            if (efectivo < total) {
                JOptionPane.showMessageDialog(this, "Monto insuficiente. Falta Q" + String.format("%.2f", (total - efectivo)), "Pago Rechazado", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double cambio = efectivo - total;
            pedidoActual.setMetodoPago("EFECTIVO");
            confirmarVenta("Pago en Efectivo procesado.\nEfectivo recibido: Q" + String.format("%.2f", efectivo) + "\nCambio entregado: Q" + String.format("%.2f", cambio));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un monto válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cobrarTarjeta() {
        if (!validarPedido()) return;

        String ref = JOptionPane.showInputDialog(this, "Ingrese el número de referencia de la tarjeta:", "Pago con Tarjeta", JOptionPane.QUESTION_MESSAGE);
        if (ref == null || ref.trim().isEmpty()) return;

        int confirm = JOptionPane.showConfirmDialog(this, "¿La transacción con tarjeta fue APROBADA en el POS?", "Simulación POS Tarjeta", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            pedidoActual.setMetodoPago("TARJETA");
            pedidoActual.setNumeroReferencia(ref.trim());
            confirmarVenta("Pago con tarjeta APROBADO.\nRef: " + ref.trim());
        } else {
            JOptionPane.showMessageDialog(this, "Operación de pago rechazada por el banco.", "Tarjeta Rechazada", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cobrarMixto() {
        if (!validarPedido()) return;

        double totalPedido = cuponAplicado ? 0.00 : pedidoActual.calcularTotal();

        JTextField txtEfectivo = new JTextField("0.00");
        JTextField txtTarjeta = new JTextField("0.00");
        JTextField txtRef = new JTextField("");

        JPanel panelModal = new JPanel(new GridLayout(4, 2, 8, 8));
        panelModal.add(new JLabel("Total del Pedido:"));
        panelModal.add(new JLabel(String.format("Q%.2f", totalPedido)));
        panelModal.add(new JLabel("Abono Efectivo (Q):"));
        panelModal.add(txtEfectivo);
        panelModal.add(new JLabel("Abono Tarjeta (Q):"));
        panelModal.add(txtTarjeta);
        panelModal.add(new JLabel("No. Ref. Tarjeta:"));
        panelModal.add(txtRef);

        int res = JOptionPane.showConfirmDialog(this, panelModal, "Procesar Pago Mixto / Dividido", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (res == JOptionPane.OK_OPTION) {
            try {
                double montoEfectivo = Double.parseDouble(txtEfectivo.getText().trim());
                double montoTarjeta = Double.parseDouble(txtTarjeta.getText().trim());
                String refTarjeta = txtRef.getText().trim();

                double sumaPagos = montoEfectivo + montoTarjeta;

                if (sumaPagos < totalPedido) {
                    JOptionPane.showMessageDialog(this, "El pago total (Q" + String.format("%.2f", sumaPagos) + ") es menor al costo del pedido (Q" + String.format("%.2f", totalPedido) + ").", "Pago Insuficiente", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (montoTarjeta > 0 && refTarjeta.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Debe ingresar el número de referencia para el pago con tarjeta.", "Datos Faltantes", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                double cambio = (montoEfectivo + montoTarjeta) - totalPedido;
                pedidoActual.setMetodoPago("MIXTO (Efectivo: Q" + String.format("%.2f", montoEfectivo) + " / Tarjeta: Q" + String.format("%.2f", montoTarjeta) + ")");
                pedidoActual.setNumeroReferencia(refTarjeta.isEmpty() ? "N/A" : refTarjeta);

                confirmarVenta("Pago Mixto procesado exitosamente.\n"
                        + "• Efectivo recibido: Q" + String.format("%.2f", montoEfectivo) + "\n"
                        + "• Tarjeta cobrada: Q" + String.format("%.2f", montoTarjeta) + " (Ref: " + refTarjeta + ")\n"
                        + "• Cambio entregado: Q" + String.format("%.2f", cambio));

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ingrese montos numéricos válidos en Efectivo y Tarjeta.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean validarPedido() {
        if (pedidoActual == null || pedidoActual.getDetalles().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El pedido no tiene ítems agregados.", "Pedido Vacío", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    // REGISTRO DE VENTA, DETALLES Y PUNTOS EN BASE DE DATOS ORACLE XE
    private void confirmarVenta(String mensajePago) {
        double totalFinal = cuponAplicado ? 0.00 : pedidoActual.calcularTotal();
        int puntosGanados = cuponAplicado ? 0 : pedidoActual.calcularPuntosTotales();

        int idPedidoGenerado = guardarPedidoEnOracle(totalFinal, puntosGanados);

        if (idPedidoGenerado > 0) {
            // Guardar detalles y descontar inventarios
            for (DetallePedido d : pedidoActual.getDetalles()) {
                ItemVenta item = d.getItem();
                int cant = d.getCantidad();
                int idProducto = obtenerIdProductoPorNombre(item.getNombre());

                guardarDetalleEnOracle(idPedidoGenerado, idProducto, cant, item.getPrecio(), d.getSubtotal(), d.getPuntos());

                if (item instanceof ProductoIndividual) {
                    ProductoIndividual p = (ProductoIndividual) item;
                    gestionInventario.abastecerInventario(p.getCodigo(), -cant);
                } else if (item instanceof MenuCompleto) {
                    descontarComponentesCombo(cant);
                }
            }

            // Actualizar Puntos del Cliente en Oracle XE
            if (puntosGanados > 0 && clienteActual != null) {
                actualizarPuntosClienteEnOracle(clienteActual.getNit(), puntosGanados);
                clienteActual.acumularPuntos(puntosGanados);
            }

            String comprobante = "============== COMPROBANTE DE VENTA ==============\n"
                    + "No. Orden: PED-" + String.format("%04d", idPedidoGenerado) + "\n"
                    + "Cliente: " + clienteActual.getNombre() + " (NIT: " + clienteActual.getNit() + ")\n"
                    + "Total Pagado: Q" + String.format("%.2f", totalFinal) + "\n"
                    + "Método de Pago: " + pedidoActual.getMetodoPago() + "\n"
                    + (cuponAplicado ? "Cupón de Canje Aplicado: " + codigoCuponValidado + "\n" : "")
                    + "Puntos Acreditados: +" + puntosGanados + " pts\n"
                    + "Saldo Total Puntos: " + clienteActual.getPuntos() + " pts\n"
                    + "==================================================";

            JOptionPane.showMessageDialog(this, mensajePago + "\n\n" + comprobante, "Venta Finalizada", JOptionPane.INFORMATION_MESSAGE);

            cuponAplicado = false;
            codigoCuponValidado = "";
            lblCuponAplicado.setText("Ningún cupón aplicado");
            lblCuponAplicado.setForeground(Color.GRAY);
            txtCodigoCupon.setText("");

            buscarCliente();
        } else {
            JOptionPane.showMessageDialog(this, "Ocurrió un error al registrar la venta en la base de datos.", "Error de Persistencia", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int guardarPedidoEnOracle(double total, int puntos) {
        String sql = "INSERT INTO PEDIDOS (ID_PEDIDO, NIT_CLIENTE, TOTAL, PUNTOS_GANADOS, METODO_PAGO, NO_REFERENCIA, ESTADO) " +
                     "VALUES (SEQ_PEDIDOS.NEXTVAL, ?, ?, ?, ?, ?, 'PAGADO')";

        try (Connection conn = ConexionOracle.obtenerConexion()) {
            if (conn != null) {
                String[] returnCols = {"ID_PEDIDO"};
                try (PreparedStatement stmt = conn.prepareStatement(sql, returnCols)) {
                    stmt.setString(1, clienteActual != null ? clienteActual.getNit() : "CF");
                    stmt.setDouble(2, total);
                    stmt.setInt(3, puntos);
                    stmt.setString(4, pedidoActual.getMetodoPago());
                    stmt.setString(5, pedidoActual.getNumeroReferencia() != null ? pedidoActual.getNumeroReferencia() : "N/A");

                    int filas = stmt.executeUpdate();
                    if (filas > 0) {
                        try (ResultSet rs = stmt.getGeneratedKeys()) {
                            if (rs.next()) {
                                int id = rs.getInt(1);
                                if (!conn.getAutoCommit()) conn.commit();
                                return id;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error al insertar PEDIDO en Oracle XE: " + e.getMessage());
        }
        return -1;
    }

    private void guardarDetalleEnOracle(int idPedido, int idProducto, int cantidad, double precioUnit, double subtotal, int puntos) {
        String sql = "INSERT INTO DETALLE_PEDIDOS (ID_DETALLE, ID_PEDIDO, ID_PRODUCTO, CANTIDAD, PRECIO_UNITARIO, SUBTOTAL, PUNTOS_GENERADOS) " +
                     "VALUES (SEQ_DETALLE_PEDIDOS.NEXTVAL, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionOracle.obtenerConexion()) {
            if (conn != null) {
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, idPedido);
                    stmt.setInt(2, idProducto);
                    stmt.setInt(3, cantidad);
                    stmt.setDouble(4, precioUnit);
                    stmt.setDouble(5, subtotal);
                    stmt.setInt(6, puntos);

                    stmt.executeUpdate();
                    if (!conn.getAutoCommit()) conn.commit();
                }
            }
        } catch (Exception e) {
            System.err.println("Error al insertar DETALLE_PEDIDO en Oracle XE: " + e.getMessage());
        }
    }

    private int obtenerIdProductoPorNombre(String nombre) {
        String sql = "SELECT ID_PRODUCTO FROM PRODUCTOS WHERE LOWER(NOMBRE) = LOWER(?) AND ROWNUM = 1";

        try (Connection conn = ConexionOracle.obtenerConexion()) {
            if (conn != null) {
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, nombre.trim());
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            return rs.getInt("ID_PRODUCTO");
                        }
                    }
                }
            }
        } catch (Exception ignored) {}
        return 1;
    }

    private void actualizarPuntosClienteEnOracle(String nit, int puntosSuma) {
        String sql = "UPDATE CLIENTES SET PUNTOS = PUNTOS + ? WHERE UPPER(NIT) = UPPER(?)";

        try (Connection conn = ConexionOracle.obtenerConexion()) {
            if (conn != null) {
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, puntosSuma);
                    stmt.setString(2, nit.trim());

                    stmt.executeUpdate();
                    if (!conn.getAutoCommit()) conn.commit();
                }
            }
        } catch (Exception e) {
            System.err.println("Error al actualizar puntos del cliente en Oracle XE: " + e.getMessage());
        }
    }

    private void descontarComponentesCombo(int cantidadCombos) {
        String sqlDescuento = "UPDATE PRODUCTOS SET EXISTENCIA = EXISTENCIA - ? " +
                             "WHERE CATEGORIA = ? AND ROWNUM = 1 AND EXISTENCIA >= ?";

        try (Connection conn = ConexionOracle.obtenerConexion()) {
            if (conn != null) {
                try (PreparedStatement stmt = conn.prepareStatement(sqlDescuento)) {
                    stmt.setInt(1, cantidadCombos);
                    stmt.setString(2, "Sandwich");
                    stmt.setInt(3, cantidadCombos);
                    stmt.executeUpdate();
                }
                try (PreparedStatement stmt = conn.prepareStatement(sqlDescuento)) {
                    stmt.setInt(1, cantidadCombos);
                    stmt.setString(2, "Bebida");
                    stmt.setInt(3, cantidadCombos);
                    stmt.executeUpdate();
                }
                try (PreparedStatement stmt = conn.prepareStatement(sqlDescuento)) {
                    stmt.setInt(1, cantidadCombos);
                    stmt.setString(2, "Acompanamiento");
                    stmt.setInt(3, cantidadCombos);
                    stmt.executeUpdate();
                }
                if (!conn.getAutoCommit()) conn.commit();
            }
        } catch (Exception e) {
            System.err.println("Error al descontar componentes del combo en Oracle XE: " + e.getMessage());
        }
    }

    private void regresarMenu() {
        this.dispose();
        new FrmPrincipal(usuarioSesion).setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmPedidos(null).setVisible(true));
    }
}