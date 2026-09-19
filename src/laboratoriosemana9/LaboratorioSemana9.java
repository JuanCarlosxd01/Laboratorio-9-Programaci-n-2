
package laboratoriosemana9;

import interfaz.VentanaPrincipal;
import javax.swing.SwingUtilities;


public class LaboratorioSemana9 {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });
    }

}
