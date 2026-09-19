package interfaz;

import estructuras.ListaEnlazada;
import hilos.ClasificadorThread;
import hilos.EmpaquetadorThread;
import hilos.RepartidorThread;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import modelo.Paquete;
import sistema.CentroLogistico;
import sistema.Estadisticas;
import sistema.Simulador;

public class VentanaPrincipal extends JFrame {

    private final CentroLogistico c = new CentroLogistico();
    private final Simulador sim = new Simulador(c);

    private final JTextArea log = new JTextArea();

    private final JLabel rec = new JLabel();
    private final JLabel alm = new JLabel();
    private final JLabel cla = new JLabel();
    private final JLabel emp = new JLabel();
    private final JLabel exp = new JLabel();
    private final JLabel rep = new JLabel();
    private final JLabel ent = new JLabel();
    private final JLabel dev = new JLabel();

    private final JLabel lblClasificadores = new JLabel();
    private final JLabel lblEmpaquetadores = new JLabel();
    private final JLabel lblRepartidores = new JLabel();

    private final JProgressBar barraRecepcion = new JProgressBar();
    private final JProgressBar barraAlmacen = new JProgressBar();
    private final JProgressBar barraClasificacion = new JProgressBar();
    private final JProgressBar barraEmpaquetado = new JProgressBar();
    private final JProgressBar barraExpedicion = new JProgressBar();

    private final Color fondo = new Color(238, 241, 245);
    private final Color azul = new Color(52, 91, 138);
    private final Color verde = new Color(67, 140, 92);
    private final Color rojo = new Color(180, 75, 75);
    private final Color naranja = new Color(196, 132, 62);

    public VentanaPrincipal() {
        configurarVentana();
        crearInterfaz();
        configurarLog();
        iniciarActualizacion();
    }

    private void configurarVentana() {
        setTitle("Centro de Distribución de Paquetería");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1120, 720);
        setMinimumSize(new Dimension(1000, 650));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(fondo);
    }

    private void crearInterfaz() {
        add(crearEncabezado(), BorderLayout.NORTH);

        JPanel contenido = new JPanel(new BorderLayout(8, 8));
        contenido.setBackground(fondo);
        contenido.setBorder(new EmptyBorder(8, 10, 10, 10));

        contenido.add(crearPanelBotones(), BorderLayout.NORTH);
        contenido.add(crearPanelCentral(), BorderLayout.CENTER);
        contenido.add(crearPanelLog(), BorderLayout.SOUTH);

        add(contenido, BorderLayout.CENTER);
    }

    private JPanel crearEncabezado() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(43, 62, 80));
        panel.setBorder(new EmptyBorder(10, 15, 10, 15));

        JLabel titulo = new JLabel("CENTRO DE DISTRIBUCIÓN DE PAQUETERÍA");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 19));
        titulo.setForeground(Color.WHITE);

        JLabel estado = new JLabel("Sistema logístico");
        estado.setFont(new Font("SansSerif", Font.PLAIN, 12));
        estado.setForeground(new Color(210, 215, 220));

        panel.add(titulo, BorderLayout.WEST);
        panel.add(estado, BorderLayout.EAST);

        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 7, 5));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(210, 215, 220)));

        JButton btnIniciar = crearBoton("Iniciar", verde);
        JButton btnPausar = crearBoton("Pausar", naranja);
        JButton btnReanudar = crearBoton("Reanudar", azul);
        JButton btnDetener = crearBoton("Detener", rojo);
        JButton btnReiniciar = crearBoton("Reiniciar", new Color(100, 105, 110));
        JButton btnEstadisticas = crearBoton("Estadísticas", azul);

        btnIniciar.addActionListener(e -> sim.iniciar());
        btnPausar.addActionListener(e -> sim.pausar());
        btnReanudar.addActionListener(e -> sim.reanudar());
        btnDetener.addActionListener(e -> sim.detener());
        btnReiniciar.addActionListener(e -> sim.reiniciar());
        btnEstadisticas.addActionListener(e -> mostrarEstadisticas());

        panel.add(btnIniciar);
        panel.add(btnPausar);
        panel.add(btnReanudar);
        panel.add(btnDetener);
        panel.add(btnReiniciar);
        panel.add(btnEstadisticas);

        return panel;
    }

    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);

        boton.setFont(new Font("SansSerif", Font.BOLD, 11));
        boton.setForeground(Color.WHITE);
        boton.setBackground(color);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(105, 30));

        return boton;
    }

    private JPanel crearPanelCentral() {
        JPanel principal = new JPanel(new BorderLayout(8, 8));
        principal.setBackground(fondo);

        JPanel zonas = new JPanel(new GridLayout(2, 4, 7, 7));
        zonas.setBackground(fondo);

        zonas.add(crearZona("Recepción", rec, barraRecepcion));
        zonas.add(crearZona("Almacén", alm, barraAlmacen));
        zonas.add(crearZona("Clasificación", cla, barraClasificacion));
        zonas.add(crearZona("Empaquetado", emp, barraEmpaquetado));
        zonas.add(crearZona("Expedición", exp, barraExpedicion));
        zonas.add(crearZonaSimple("Reparto", rep));
        zonas.add(crearZonaSimple("Entregados", ent));
        zonas.add(crearZonaSimple("Devueltos", dev));

        principal.add(zonas, BorderLayout.CENTER);

        JPanel trabajadores = new JPanel(new GridLayout(1, 3, 7, 7));
        trabajadores.setBackground(fondo);
        trabajadores.setPreferredSize(new Dimension(100, 115));

        trabajadores.add(crearPanelTrabajador("Clasificadores", lblClasificadores));
        trabajadores.add(crearPanelTrabajador("Empaquetadores", lblEmpaquetadores));
        trabajadores.add(crearPanelTrabajador("Repartidores", lblRepartidores));

        principal.add(trabajadores, BorderLayout.SOUTH);

        return principal;
    }

    private JPanel crearZona(String titulo, JLabel contenido, JProgressBar barra) {
        JPanel panel = new JPanel(new BorderLayout(3, 3));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(titulo));

        contenido.setVerticalAlignment(SwingConstants.TOP);
        contenido.setFont(new Font("SansSerif", Font.PLAIN, 11));
        contenido.setBorder(new EmptyBorder(2, 6, 2, 6));

        barra.setMinimum(0);
        barra.setStringPainted(true);
        barra.setForeground(azul);
        barra.setFont(new Font("SansSerif", Font.PLAIN, 10));
        barra.setPreferredSize(new Dimension(100, 21));

        panel.add(contenido, BorderLayout.CENTER);
        panel.add(barra, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearZonaSimple(String titulo, JLabel contenido) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(titulo));

        contenido.setVerticalAlignment(SwingConstants.TOP);
        contenido.setFont(new Font("SansSerif", Font.PLAIN, 11));
        contenido.setBorder(new EmptyBorder(2, 6, 2, 6));

        panel.add(contenido, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelTrabajador(String titulo, JLabel contenido) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(titulo));

        contenido.setVerticalAlignment(SwingConstants.TOP);
        contenido.setFont(new Font("SansSerif", Font.PLAIN, 11));
        contenido.setBorder(new EmptyBorder(3, 6, 3, 6));

        panel.add(contenido, BorderLayout.CENTER);

        return panel;
    }

    private JScrollPane crearPanelLog() {
        log.setEditable(false);
        log.setFont(new Font("Monospaced", Font.PLAIN, 11));
        log.setBackground(new Color(248, 248, 248));
        log.setForeground(new Color(45, 45, 45));

        JScrollPane scroll = new JScrollPane(log);
        scroll.setBorder(BorderFactory.createTitledBorder("Registro del sistema"));
        scroll.setPreferredSize(new Dimension(100, 135));

        return scroll;
    }

    private void configurarLog() {
        c.setListenerLog(mensaje -> SwingUtilities.invokeLater(() -> {
            log.append(mensaje + "\n");
            log.setCaretPosition(log.getDocument().getLength());
        }));
    }

    private void iniciarActualizacion() {
        Timer timer = new Timer(400, e -> actualizar());
        timer.start();
    }

    private String texto(ListaEnlazada<Paquete> lista) {
        StringBuilder texto = new StringBuilder("<html>");

        texto.append("<b>");
        texto.append(lista.tamanio());
        texto.append(" / ");
        texto.append(lista.getCapacidad());
        texto.append("</b><br>");

        for (String paquete : lista.aArregloTexto()) {
            texto.append(paquete);
            texto.append("<br>");
        }

        texto.append("</html>");

        return texto.toString();
    }

    private void actualizar() {
        rec.setText(texto(c.recepcion));
        alm.setText(texto(c.almacen));
        cla.setText(texto(c.clasificacion));
        emp.setText(texto(c.empaquetado));
        exp.setText(texto(c.expedicion));
        rep.setText(texto(c.reparto));
        ent.setText(texto(c.entregados));
        dev.setText(texto(c.devueltos));

        actualizarBarra(barraRecepcion, c.recepcion);
        actualizarBarra(barraAlmacen, c.almacen);
        actualizarBarra(barraClasificacion, c.clasificacion);
        actualizarBarra(barraEmpaquetado, c.empaquetado);
        actualizarBarra(barraExpedicion, c.expedicion);

        actualizarClasificadores();
        actualizarEmpaquetadores();
        actualizarRepartidores();
    }

    private void actualizarBarra(JProgressBar barra, ListaEnlazada<Paquete> lista) {
        barra.setMaximum(lista.getCapacidad());
        barra.setValue(lista.tamanio());
        barra.setString(lista.tamanio() + " / " + lista.getCapacidad());
    }

    private void actualizarClasificadores() {
        ClasificadorThread[] clasificadores = sim.getClasificadores();

        if (clasificadores == null) {
            lblClasificadores.setText("Sin iniciar");
            return;
        }

        StringBuilder texto = new StringBuilder("<html>");

        for (ClasificadorThread clasificador : clasificadores) {
            texto.append(clasificador.getName());
            texto.append(": ");

            if (clasificador.getProcesando().equals("-")) {
                texto.append("Disponible");
            } else {
                texto.append(clasificador.getProcesando());
            }

            texto.append("<br>");
        }

        texto.append("</html>");

        lblClasificadores.setText(texto.toString());
    }

    private void actualizarEmpaquetadores() {
        EmpaquetadorThread[] empaquetadores = sim.getEmpaquetadores();

        if (empaquetadores == null) {
            lblEmpaquetadores.setText("Sin iniciar");
            return;
        }

        StringBuilder texto = new StringBuilder("<html>");

        for (EmpaquetadorThread empaquetador : empaquetadores) {
            texto.append(empaquetador.getName());
            texto.append(": ");

            if (empaquetador.getProcesando().equals("-")) {
                texto.append("Disponible");
            } else {
                texto.append(empaquetador.getProcesando());
            }

            texto.append("<br>");
        }

        texto.append("</html>");

        lblEmpaquetadores.setText(texto.toString());
    }

    private void actualizarRepartidores() {
        RepartidorThread[] repartidores = sim.getRepartidores();

        if (repartidores == null) {
            lblRepartidores.setText("Sin iniciar");
            return;
        }

        StringBuilder texto = new StringBuilder("<html>");

        for (RepartidorThread repartidor : repartidores) {
            texto.append(repartidor.getName());
            texto.append(" - ");
            texto.append(repartidor.getRuta());
            texto.append(" - ");
            texto.append(repartidor.getEstadoRepartidor());
            texto.append(" (");
            texto.append(repartidor.getCargaActual());
            texto.append("/");
            texto.append(repartidor.getCapacidad());
            texto.append(")<br>");
        }

        texto.append("</html>");

        lblRepartidores.setText(texto.toString());
    }

    private void mostrarEstadisticas() {
        Estadisticas e = c.estadisticas;

        StringBuilder mensaje = new StringBuilder();

        mensaje.append("Paquetes generados: ");
        mensaje.append(e.getGenerados());

        mensaje.append("\nEntregados: ");
        mensaje.append(e.getEntregados());

        mensaje.append("\nDevueltos: ");
        mensaje.append(e.getDevueltos());

        mensaje.append("\nEn proceso: ");
        mensaje.append(e.getEnProceso());

        mensaje.append("\nTiempo promedio: ");
        mensaje.append(String.format("%.2f segundos", e.getPromedioSegundos()));

        RepartidorThread[] repartidores = sim.getRepartidores();

        if (repartidores != null) {
            mensaje.append("\n\nEntregas por repartidor:");
            for (RepartidorThread repartidor : repartidores) {
                mensaje.append("\n");
                mensaje.append(repartidor.getName());
                mensaje.append(": ");
                mensaje.append(repartidor.getEntregados());
            }
        }

        JOptionPane.showMessageDialog(this, mensaje.toString(), "Estadísticas", JOptionPane.INFORMATION_MESSAGE);
    }
}