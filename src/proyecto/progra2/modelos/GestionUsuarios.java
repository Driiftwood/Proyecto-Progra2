package proyecto.progra2.modelos;

import java.util.ArrayList;
import java.util.List;

public class GestionUsuarios {
    private List<Usuario> listaUsuarios;

    public GestionUsuarios() {
        listaUsuarios = new ArrayList<>();
        listaUsuarios.add(new Usuario("admin", "1234", "Administrador Sistema", "Administrador"));
        listaUsuarios.add(new Usuario("cajero1", "1234", "Carlos López", "Cajero"));
    }

    public Usuario autenticar(String user, String pass) {
        for (Usuario u : listaUsuarios) {
            if (u.getUsername().equalsIgnoreCase(user.trim()) && u.getPassword().equals(pass.trim()) && u.isActivo()) {
                return u;
            }
        }
        return null;
    }

    public boolean agregarUsuario(Usuario u) {
        if (buscarPorUsername(u.getUsername()) != null) return false;
        listaUsuarios.add(u);
        return true;
    }

    public boolean editarUsuario(String usernameOriginal, String nuevoNombre, String nuevoRol) {
        Usuario u = buscarPorUsername(usernameOriginal);
        if (u != null) {
            u.setNombre(nuevoNombre);
            u.setRol(nuevoRol);
            return true;
        }
        return false;
    }

    public boolean cambiarPassword(String username, String nuevaPassword) {
        Usuario u = buscarPorUsername(username);
        if (u != null) {
            u.setPassword(nuevaPassword);
            return true;
        }
        return false;
    }

    public boolean cambiarEstadoUsuario(String username) {
        Usuario u = buscarPorUsername(username);
        if (u != null) {
            u.setActivo(!u.isActivo());
            return true;
        }
        return false;
    }

    public Usuario buscarPorUsername(String username) {
        for (Usuario u : listaUsuarios) {
            if (u.getUsername().equalsIgnoreCase(username.trim())) return u;
        }
        return null;
    }

    public List<Usuario> getListaUsuarios() { return listaUsuarios; }
}
