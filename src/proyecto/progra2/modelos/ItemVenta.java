package proyecto.progra2.modelos;

public abstract class ItemVenta {
    private String codigo;
    private String nombre;
    private double precio;

    public ItemVenta(String codigo, String nombre, double precio) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
    }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    // Método polimórfico obligatorio
    public abstract int calcularPuntos(int cantidad);

    @Override
    public String toString() {
        return nombre + " (Q" + String.format("%.2f", precio) + ")";
    }
}