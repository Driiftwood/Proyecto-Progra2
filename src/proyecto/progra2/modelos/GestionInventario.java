package proyecto.progra2.modelos;

import java.util.ArrayList;
import java.util.List;

public class GestionInventario {
    private List<Producto> listaProductos;

    public GestionInventario() {
        this.listaProductos = new ArrayList<>();
        // Productos de ejemplo iniciales
        listaProductos.add(new Producto(101, "Café Americano", "Bebidas", 50, 15.00));
        listaProductos.add(new Producto(102, "Muffin de Arándanos", "Repostería", 30, 18.50));
    }

    public void agregarProducto(Producto p) {
        listaProductos.add(p);
    }

    public List<Producto> getListaProductos() {
        return listaProductos;
    }

    public Producto buscarPorId(int id) {
        for (Producto p : listaProductos) {
            if (p.getIdProducto() == id) {
                return p;
            }
        }
        return null;
    }
}
