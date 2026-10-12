package proyecto.progra2.modelos;

public class Recompensa {
    private String id;
    private String nombre;
    private int puntosNecesarios;
    private ItemVenta itemAsociado;

    public Recompensa(String id, String nombre, int puntosNecesarios, ItemVenta itemAsociado) {
        this.id = id;
        this.nombre = nombre;
        this.puntosNecesarios = puntosNecesarios;
        this.itemAsociado = itemAsociado;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public int getPuntosNecesarios() { return puntosNecesarios; }
    public ItemVenta getItemAsociado() { return itemAsociado; }

    @Override
    public String toString() {
        return nombre + " (" + puntosNecesarios + " pts)";
    }
}
