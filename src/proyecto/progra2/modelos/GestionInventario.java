package proyecto.progra2.modelos;

import java.util.ArrayList;
import java.util.List;

public class GestionInventario {
    private List<ProductoIndividual> listaProductos;
    private List<MenuCompleto> listaMenus;

    public GestionInventario() {
        this.listaProductos = new ArrayList<>();
        this.listaMenus = new ArrayList<>();
        cargarDatosPrueba();
    }

    private void cargarDatosPrueba() {
        ProductoIndividual p1 = new ProductoIndividual("P01", "Sándwich de Pollo", "SANDWICH", 25.00, 20);
        ProductoIndividual p2 = new ProductoIndividual("P02", "Sándwich de Jamón", "SANDWICH", 22.00, 15);
        ProductoIndividual p3 = new ProductoIndividual("P03", "Gaseosa 500ml", "BEBIDA", 8.00, 30);
        ProductoIndividual p4 = new ProductoIndividual("P04", "Papas Fritas", "ACOMPANAMIENTO", 10.00, 25);

        listaProductos.add(p1);
        listaProductos.add(p2);
        listaProductos.add(p3);
        listaProductos.add(p4);

        MenuCompleto m1 = new MenuCompleto("M01", "Menú Completo de Pollo", 37.00, p1, p3, p4);
        listaMenus.add(m1);
    }

    public void agregarProducto(ProductoIndividual producto) {
        listaProductos.add(producto);
    }

    public boolean editarProducto(String codigoOriginal, String nuevoNombre, String nuevaCategoria, double nuevoPrecio) {
        ProductoIndividual p = buscarProducto(codigoOriginal);
        if (p != null) {
            p.setNombre(nuevoNombre);
            p.setCategoria(nuevaCategoria);
            p.setPrecio(nuevoPrecio);
            return true;
        }
        return false;
    }

    public boolean eliminarProducto(String codigo) {
        ProductoIndividual p = buscarProducto(codigo);
        if (p != null) {
            listaProductos.remove(p);
            return true;
        }
        return false;
    }

    public boolean abastecerInventario(String codigo, int cantidadIngreso) {
        if (cantidadIngreso <= 0) return false;
        ProductoIndividual p = buscarProducto(codigo);
        if (p != null) {
            p.setExistencia(p.getExistencia() + cantidadIngreso);
            return true;
        }
        return false;
    }

    public ProductoIndividual buscarProducto(String codigo) {
        for (ProductoIndividual p : listaProductos) {
            if (p.getCodigo().equalsIgnoreCase(codigo)) return p;
        }
        return null;
    }

    public List<ProductoIndividual> getListaProductos() { return listaProductos; }
    public List<MenuCompleto> getListaMenus() { return listaMenus; }
}
