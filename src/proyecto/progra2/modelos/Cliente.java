package proyecto.progra2.modelos;

public class Cliente {
    private String nit; 
    private String nombre;
    private String direccion;
    private String telefono;
    private int puntos;
    private boolean activo;

    public Cliente(String nit, String nombre, String direccion, String telefono) {
        this.nit = nit;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.puntos = 0; 
        this.activo = true;
    }

    public String getNit() { return nit; }
    public void setNit(String nit) { this.nit = nit; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public int getPuntos() { return puntos; }
    public void setPuntos(int puntos) { this.puntos = puntos; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    public void acumularPuntos(int cantidad) {
        if (cantidad > 0) {
            this.puntos += cantidad;
        }
    }

    public boolean descontarPuntos(int cantidad) {
        if (cantidad > 0 && this.puntos >= cantidad) {
            this.puntos -= cantidad;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return nombre + " (NIT: " + nit + ") - Puntos: " + puntos;
    }
}