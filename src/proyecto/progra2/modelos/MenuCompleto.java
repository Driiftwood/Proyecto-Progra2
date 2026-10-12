package proyecto.progra2.modelos;

public class MenuCompleto extends ItemVenta {
    private ProductoIndividual sandwich;
    private ProductoIndividual bebida;
    private ProductoIndividual acompanamiento;
    private boolean activo;

    public MenuCompleto(String codigo, String nombre, double precio, 
                        ProductoIndividual sandwich, ProductoIndividual bebida, ProductoIndividual acompanamiento) {
        super(codigo, nombre, precio);
        this.sandwich = sandwich;
        this.bebida = bebida;
        this.acompanamiento = acompanamiento;
        this.activo = true;
    }

    public ProductoIndividual getSandwich() { return sandwich; }
    public ProductoIndividual getBebida() { return bebida; }
    public ProductoIndividual getAcompanamiento() { return acompanamiento; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    // Verifica si hay existencias suficientes de sus 3 componentes
    public boolean hayStockDisponible(int cantidad) {
        return sandwich.getExistencia() >= cantidad &&
               bebida.getExistencia() >= cantidad &&
               acompanamiento.getExistencia() >= cantidad;
    }

    @Override
    public int calcularPuntos(int cantidad) {
        return cantidad * 8; // Menú completo = 8 puntos por unidad
    }
}