package proyecto.progra2.modelos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Pedido {
    private static int contador = 1;
    private int idPedido;
    private Cliente cliente;
    private List<DetallePedido> detalles;
    private String estado; // "PENDIENTE", "PAGADO", "ANULADO"
    private String metodoPago; // "EFECTIVO", "TARJETA"
    private String numeroReferencia;
    private Date fecha;

    public Pedido(Cliente cliente) {
        this.idPedido = contador++;
        this.cliente = cliente;
        this.detalles = new ArrayList<>();
        this.estado = "PENDIENTE";
        this.fecha = new Date();
    }

    public void agregarDetalle(ItemVenta item, int cantidad) {
        detalles.add(new DetallePedido(item, cantidad));
    }

    public void removerDetalle(int indice) {
        if (indice >= 0 && indice < detalles.size()) {
            detalles.remove(indice);
        }
    }

    public double calcularTotal() {
        double total = 0;
        for (DetallePedido d : detalles) {
            total += d.getSubtotal();
        }
        return total;
    }

    public int calcularPuntosTotales() {
        int puntos = 0;
        for (DetallePedido d : detalles) {
            puntos += d.getPuntos();
        }
        return puntos;
    }
    public int getIdPedido() { return idPedido; }
    public Cliente getCliente() { return cliente; }
    public List<DetallePedido> getDetalles() { return detalles; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
    public String getNumeroReferencia() { return numeroReferencia; }
    public void setNumeroReferencia(String numeroReferencia) { this.numeroReferencia = numeroReferencia; }
    public Date getFecha() { return fecha; }
}
