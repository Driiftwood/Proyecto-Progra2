package proyecto.progra2.modelos;

import java.util.ArrayList;
import java.util.List;

public class GestionClientes {
    private List<Cliente> listaClientes;

    public GestionClientes() {
        this.listaClientes = new ArrayList<>();
        cargarDatosIniciales();
    }

    private void cargarDatosIniciales() {
        Cliente c1 = new Cliente("CF", "Consumidor Final", "Ciudad", "00000000");
        Cliente c2 = new Cliente("123456-7", "Juan Pérez", "Zona 10", "55554444");
        c2.setPuntos(20); // Ejemplo del documento oficial[cite: 7]

        Cliente c3 = new Cliente("987654-3", "María López", "Zona 9", "22221111");

        listaClientes.add(c1);
        listaClientes.add(c2);
        listaClientes.add(c3);
    }

    public boolean agregarCliente(Cliente cliente) {
        if (buscarPorNit(cliente.getNit()) != null) {
            return false; // Ya existe un cliente con ese NIT/DPI
        }
        listaClientes.add(cliente);
        return true;
    }

    public boolean editarCliente(String nitOriginal, String nuevoNombre, String nuevaDireccion, String nuevoTelefono) {
        Cliente c = buscarPorNit(nitOriginal);
        if (c != null && c.isActivo()) {
            c.setNombre(nuevoNombre);
            c.setDireccion(nuevaDireccion);
            c.setTelefono(nuevoTelefono);
            return true;
        }
        return false;
    }

    // Cambia el estado entre activo e inactivo manteniendo el historial[cite: 5]
    public boolean cambiarEstadoCliente(String nit) {
        Cliente c = buscarPorNit(nit);
        if (c != null) {
            c.setActivo(!c.isActivo());
            return true;
        }
        return false;
    }

    public Cliente buscarPorNit(String nit) {
        for (Cliente c : listaClientes) {
            if (c.getNit().equalsIgnoreCase(nit.trim())) {
                return c;
            }
        }
        return null;
    }

    public List<Cliente> getListaClientes() {
        return listaClientes;
    }
}
