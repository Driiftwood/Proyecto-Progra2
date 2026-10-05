package proyecto.progra2.modelos;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private int idPedido;
    private int idCliente;
    // COMPOSICIÓN: El Pedido TIENE UNA LISTA (ArrayList) de DetallePedido
    private List<DetallePedido> detalles;

    // Constructor
    public Pedido(int idPedido, int idCliente) {
        this.idPedido = idPedido;
        this.idCliente = idCliente;
        // Inicializamos el ArrayList interno
        this.detalles = new ArrayList<>();
    }

    // Método para agregar un producto/detalle al pedido
    public void agregarDetalle(DetallePedido detalle) {
        this.detalles.add(detalle);
    }

    // Método para calcular el total general sumando los subtotales
    public double calcularTotal() {
        double total = 0.0;
        for (DetallePedido d : detalles) {
            total += d.getSubtotal();
        }
        return total;
    }

    // Getters
    public int getIdPedido() { return idPedido; }
    public int getIdCliente() { return idCliente; }
    public List<DetallePedido> getDetalles() { return detalles; }
}
