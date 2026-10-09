package proyecto.progra2.modelos;

import java.util.ArrayList;
import java.util.List;

public class GestionUsuarios {
    private List<Usuario> listaUsuarios;

    public GestionUsuarios() {
        this.listaUsuarios = new ArrayList<>();
        // Usuarios predeterminados del sistema
        listaUsuarios.add(new Usuario(1, "Administrador Sistema", "admin", "admin123", "Administrador"));
        listaUsuarios.add(new Usuario(2, "Juan Pérez (Cajero)", "cajero1", "1234", "Cajero"));
        listaUsuarios.add(new Usuario(3, "María López (Mesero)", "mesero1", "1234", "Mesero"));
    }

    public Usuario autenticar(String user, String pass) {
        for (Usuario u : listaUsuarios) {
            if (u.getUsuario().equalsIgnoreCase(user) && u.getPassword().equals(pass)) {
                return u;
            }
        }
        return null;
    }

    public void agregarUsuario(Usuario u) {
        listaUsuarios.add(u);
    }

    public List<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }
}
