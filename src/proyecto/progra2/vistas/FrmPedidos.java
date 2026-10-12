package proyecto.progra2.vistas;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import proyecto.progra2.modelos.*;

public class FrmPedidos extends JFrame {

    private JTextField txtBuscarNit, txtNombreCliente, txtPuntosCliente, txtCantidad;
    private JComboBox<Object> cbProductos;
    private JTable tablaDetalle;
    private DefaultTableModel modeloTabla;
    private JLabel lblSubtotal, lblTotal, lblPuntosGanados;
    private JButton btnBuscarCliente, btnAgregarItem, btnEliminarItem, btnCobrarEfectivo, btnCobrarTarjeta, btnCobrarMixto, btnRegresar;

    private GestionClientes gestionClientes;
    private GestionInventario gestionInventario;

    private Cliente clienteActual;
    private Pedido pedidoActual;
    private Usuario usuarioSesion;

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

        setTitle("Módulo de Ventas, Pedidos y Cobro");
        setSize(1040, 660);
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

        JLabel lblTitulo = new JLabel("MÓDULO DE GESTIÓN DE PEDIDOS Y COBRO");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Selección de cliente, detalle de orden, acumulación de puntos y procesamiento de pago");
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

        // --- Panel Izquierdo: Selección de Cliente y Productos ---
        JPanel panelIzquierdo = new JPanel(new BorderLayout(0, 10));
        panelIzquierdo.setOpaque(false);
        panelIzquierdo.setPreferredSize(new Dimension(380, 0));
        panelIzquierdo.setBorder(new EmptyBorder(10, 15, 10, 5));

        Font fontLabel = new Font("Segoe UI", Font.BOLD, 12);
        Font fontText = new Font("Segoe UI", Font.PLAIN, 12);

        // Card 1: Cliente
        JPanel panelCliente = new JPanel(new GridBagLayout());
        panelCliente.setBackground(Color.WHITE);
        panelCliente.setBorder(BorderFactory.createTitledBorder(new LineBorder(new Color(200, 205, 210), 1), "1. Datos del Cliente"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; panelCliente.add(new JLabel("NIT / DPI:"), gbc);
        gbc.gridx = 1; txtBuscarNit = crearCampoTexto(fontText); panelCliente.add(txtBuscarNit, gbc);

        btnBuscarCliente = crearBoton("Buscar", COLOR_PRIMARY, Color.WHITE);
        btnBuscarCliente.addActionListener(e -> buscarCliente());
        gbc.gridx = 2; panelCliente.add(btnBuscarCliente, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panelCliente.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        txtNombreCliente = crearCampoTexto(fontText);
        txtNombreCliente.setEditable(false);
        panelCliente.add(txtNombreCliente, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1; panelCliente.add(new JLabel("Puntos:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        txtPuntosCliente = crearCampoTexto(fontText);
        txtPuntosCliente.setEditable(false);
        panelCliente.add(txtPuntosCliente, gbc);

        panelIzquierdo.add(panelCliente, BorderLayout.NORTH);

        // Card 2: Agregar Productos
        JPanel panelProd = new JPanel(new GridBagLayout());
        panelProd.setBackground(Color.WHITE);
        panelProd.setBorder(BorderFactory.createTitledBorder(new LineBorder(new Color(200, 205, 210), 1), "2. Selección de Producto / Menú"));

        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(6, 5, 6, 5);
        gbc2.fill = GridBagConstraints.HORIZONTAL;

        gbc2.gridx = 0; gbc2.gridy = 0; panelProd.add(new JLabel("Ítem:"), gbc2);
        gbc2.gridx = 1; gbc2.gridwidth = 2;
        cbProductos = new JComboBox<>();
        cbProductos.setFont(fontText);
        panelProd.add(cbProductos, gbc2);

        gbc2.gridx = 0; gbc2.gridy = 1; gbc2.gridwidth = 1; panelProd.add(new JLabel("Cantidad:"), gbc2);
        gbc2.gridx = 1; txtCantidad = crearCampoTexto(fontText); txtCantidad.setText("1"); panelProd.add(txtCantidad, gbc2);

        btnAgregarItem = crearBoton("Agregar al Pedido", new Color(34, 153, 84), Color.WHITE);
        btnAgregarItem.addActionListener(e -> agregarItem());
        gbc2.gridx = 0; gbc2.gridy = 2; gbc2.gridwidth = 3;
        gbc2.insets = new Insets(12, 5, 5, 5);
        panelProd.add(btnAgregarItem, gbc2);

        panelIzquierdo.add(panelProd, BorderLayout.CENTER);

        add(panelIzquierdo, BorderLayout.WEST);

        // --- Panel Derecho: Tabla y Resumen Totales ---
        JPanel panelDerecho = new JPanel(new BorderLayout(5, 5));
        panelDerecho.setOpaque(false);
        panelDerecho.setBorder(new EmptyBorder(10, 5, 10, 15));

        String[] columnas = {"Producto / Menú", "Precio Unid.", "Cantidad", "Subtotal", "Puntos"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tablaDetalle = new JTable(modeloTabla);
        tablaDetalle.setFont(fontText);
        tablaDetalle.setRowHeight(25);
        tablaDetalle.getTableHeader().setFont(fontLabel);

        panelDerecho.add(new JScrollPane(tablaDetalle), BorderLayout.CENTER);

        // Panel Inferior: Totales y Opciones de Pago
        JPanel panelInferior = new JPanel(new BorderLayout(10, 10));
        panelInferior.setOpaque(false);

        JPanel panelTotales = new JPanel(new GridLayout(3, 1, 5, 5));
        panelTotales.setBackground(Color.WHITE);
        panelTotales.setBorder(new EmptyBorder(10, 15, 10, 15));

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

        JPanel panelBotonesPago = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        panelBotonesPago.setOpaque(false);

        btnEliminarItem = crearBoton("Quitar", new Color(176, 58, 46), Color.WHITE);
        btnEliminarItem.setPreferredSize(new Dimension(85, 36));
        btnEliminarItem.addActionListener(e -> eliminarItem());

        btnCobrarEfectivo = crearBoton("Efectivo", new Color(34, 153, 84), Color.WHITE);
        btnCobrarEfectivo.setPreferredSize(new Dimension(110, 36));
        btnCobrarEfectivo.addActionListener(e -> cobrarEfectivo());

        btnCobrarTarjeta = crearBoton("Tarjeta", new Color(40, 116, 166), Color.WHITE);
        btnCobrarTarjeta.setPreferredSize(new Dimension(100, 36));
        btnCobrarTarjeta.addActionListener(e -> cobrarTarjeta());

        btnCobrarMixto = crearBoton("Mixto/Dividido", new Color(142, 68, 173), Color.WHITE);
        btnCobrarMixto.setPreferredSize(new Dimension(135, 36));
        btnCobrarMixto.addActionListener(e -> cobrarMixto());

        panelBotonesPago.add(btnEliminarItem);
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
        field.setPreferredSize(new Dimension(130, 26));
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
        btn.setPreferredSize(new Dimension(110, 30));
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
            actualizarTabla();
        } else {
            JOptionPane.showMessageDialog(this, "Cliente no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agregarItem() {
        if (clienteActual == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ItemVenta itemSeleccionado = (ItemVenta) cbProductos.getSelectedItem();
        if (itemSeleccionado == null) return;

        try {
            int cant = Integer.parseInt(txtCantidad.getText().trim());
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
            } else if (itemSeleccionado instanceof MenuCompleto) {
                MenuCompleto m = (MenuCompleto) itemSeleccionado;
                if (!m.hayStockDisponible(cant)) {
                    JOptionPane.showMessageDialog(this, "Stock insuficiente de los componentes del menú.", "Inventario", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            pedidoActual.agregarDetalle(itemSeleccionado, cant);
            actualizarTabla();
            txtCantidad.setText("1");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ingrese una cantidad entera válida.", "Error", JOptionPane.ERROR_MESSAGE);
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

        lblSubtotal.setText(String.format("Subtotal: Q%.2f", pedidoActual.calcularTotal()));
        lblPuntosGanados.setText("Puntos que generará la compra: +" + pedidoActual.calcularPuntosTotales() + " pts");
        lblTotal.setText(String.format("TOTAL A PAGAR: Q%.2f", pedidoActual.calcularTotal()));
    }

    private void cobrarEfectivo() {
        if (!validarPedido()) return;

        double total = pedidoActual.calcularTotal();
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

        double totalPedido = pedidoActual.calcularTotal();

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

    private void confirmarVenta(String mensajePago) {
        for (DetallePedido d : pedidoActual.getDetalles()) {
            ItemVenta item = d.getItem();
            int cant = d.getCantidad();

            if (item instanceof ProductoIndividual) {
                ProductoIndividual p = (ProductoIndividual) item;
                p.setExistencia(p.getExistencia() - cant);
            } else if (item instanceof MenuCompleto) {
                MenuCompleto m = (MenuCompleto) item;
                m.getSandwich().setExistencia(m.getSandwich().getExistencia() - cant);
                m.getBebida().setExistencia(m.getBebida().getExistencia() - cant);
                m.getAcompanamiento().setExistencia(m.getAcompanamiento().getExistencia() - cant);
            }
        }

        int puntosGanados = pedidoActual.calcularPuntosTotales();
        clienteActual.acumularPuntos(puntosGanados);
        pedidoActual.setEstado("PAGADO");

        String comprobante = "============== COMPROBANTE DE VENTA ==============\n"
                + "Cliente: " + clienteActual.getNombre() + " (NIT: " + clienteActual.getNit() + ")\n"
                + "Total Pagado: Q" + String.format("%.2f", pedidoActual.calcularTotal()) + "\n"
                + "Método de Pago: " + pedidoActual.getMetodoPago() + "\n"
                + "Puntos Acreditados: +" + puntosGanados + " pts\n"
                + "Saldo Total Puntos: " + clienteActual.getPuntos() + " pts\n"
                + "==================================================";

        JOptionPane.showMessageDialog(this, mensajePago + "\n\n" + comprobante, "Venta Finalizada", JOptionPane.INFORMATION_MESSAGE);

        buscarCliente();
    }

    private void regresarMenu() {
        this.dispose();
        new FrmPrincipal(usuarioSesion).setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmPedidos(null).setVisible(true));
    }
}