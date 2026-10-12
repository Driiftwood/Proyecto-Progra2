package proyecto.progra2.modelos;

public class DetallePedido {
    private ItemVenta item;
    private int cantidad;

    public DetallePedido(ItemVenta item, int cantidad) {
        this.item = item;
        this.cantidad = cantidad;
    }

    public ItemVenta getItem() { return item; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getSubtotal() {
        return item.getPrecio() * cantidad;
    }

    public int getPuntos() {
        return item.calcularPuntos(cantidad);
    }
}
