package proyecto.progra2.vistas;

import javax.swing.*;
import java.awt.*;

import proyecto.progra2.modelos.GestionUsuarios;
import proyecto.progra2.modelos.Usuario;

public class DialogAutorizacion extends JDialog {

    public static boolean solicitarAutorizacion(Component padre) {
        JTextField txtUser = new JTextField(12);
        JPasswordField txtPass = new JPasswordField(12);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Usuario Admin:"), gbc);
        gbc.gridx = 1;
        panel.add(txtUser, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Contraseña Admin:"), gbc);
        gbc.gridx = 1;
        panel.add(txtPass, gbc);

        int option = JOptionPane.showConfirmDialog(
                padre, 
                panel, 
                "Autorización Requerida de Administrador", 
                JOptionPane.OK_CANCEL_OPTION, 
                JOptionPane.WARNING_MESSAGE
        );

        if (option == JOptionPane.OK_OPTION) {
            String user = txtUser.getText().trim();
            String pass = new String(txtPass.getPassword()).trim();

            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(
                        padre, 
                        "Debe ingresar el usuario y la contraseña del administrador.", 
                        "Campos Incompletos", 
                        JOptionPane.WARNING_MESSAGE
                );
                return false;
            }

            GestionUsuarios gu = new GestionUsuarios();
            Usuario admin = gu.autenticar(user, pass);

            if (admin != null && admin.isActivo() && 
               ("Administrador".equalsIgnoreCase(admin.getRol().trim()) || "Admin".equalsIgnoreCase(admin.getRol().trim()))) {
                return true;
            } else {
                JOptionPane.showMessageDialog(
                        padre, 
                        "Credenciales de administrador inválidas o el usuario no tiene permisos suficientes.", 
                        "Acceso Denegado", 
                        JOptionPane.ERROR_MESSAGE
                );
                return false;
            }
        }
        return false;
    }
}