package proyecto.progra2.modelos;

public class ProductoIndividual extends ItemVenta {
    private String categoria;
    private int existencia;
    private boolean activo;

    public ProductoIndividual(String codigo, String nombre, String categoria, double precio, int existencia) {
        super(codigo, nombre, precio);
        this.categoria = categoria;
        this.existencia = existencia;
        this.activo = true;
    }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public int getExistencia() { return existencia; }
    public void setExistencia(int existencia) { this.existencia = existencia; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    @Override
    public int calcularPuntos(int cantidad) {
        if ("SANDWICH".equalsIgnoreCase(categoria)) {
            return cantidad * 2;
        }
        return 0;
    }
}