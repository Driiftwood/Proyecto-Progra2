package proyecto.progra2.vistas;

import javax.swing.*;
import java.awt.*;

import proyecto.progra2.modelos.GestionUsuarios;
import proyecto.progra2.modelos.Usuario;

public class DialogAutorizacion extends JDialog {

    private boolean autorizado = false;

    public static boolean solicitarAutorizacion(Component padre) {
        JPasswordField txtPass = new JPasswordField();
        JTextField txtUser = new JTextField();

        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        panel.add(new JLabel("Usuario Admin:"));
        panel.add(txtUser);
        panel.add(new JLabel("Contraseña Admin:"));
        panel.add(txtPass);

        int option = JOptionPane.showConfirmDialog(padre, panel, 
                "Autorización Requerida de Administrador", 
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            String user = txtUser.getText().trim();
            String pass = new String(txtPass.getPassword()).trim();

            GestionUsuarios gu = new GestionUsuarios();
            Usuario admin = gu.autenticar(user, pass);

            if (admin != null && "Administrador".equalsIgnoreCase(admin.getRol())) {
                return true;
            } else {
                JOptionPane.showMessageDialog(padre, 
                        "Credenciales de administrador inválidas o sin permisos suficientes.", 
                        "Acceso Denegado", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return false;
    }
}
