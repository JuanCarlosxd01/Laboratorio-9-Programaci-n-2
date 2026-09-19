package hilos;

import sistema.CentroLogistico;
import modelo.Paquete;
import modelo.Prioridad;
import java.util.Random;

public class RecepcionThread extends Thread {

    private final CentroLogistico c;
    private final Random r = new Random();
    private final int ciclo;

    private int sec = 1;

    private final String[] clientes = {
        "Carlos López",
        "Ana Martínez",
        "Luis García",
        "María Rivera",
        "José Flores"
    };

    private final String[] ciudades = {
        "Centro",
        "Eixample",
        "Gràcia",
        "Sant Martí",
        "Badalona"
    };

    public RecepcionThread(CentroLogistico c, int ciclo) {
        super("Recepcion");
        this.c = c;
        this.ciclo = ciclo;
    }

    @Override
    public void run() {
        while (c.perteneceAlCiclo(ciclo)) {
            try {
                c.esperarSiPausado();

                String codigo = String.format("PKG-%03d", sec++);
                String cliente = clientes[r.nextInt(clientes.length)];
                String direccion = "Calle " + (r.nextInt(90) + 10);
                String ciudad = ciudades[r.nextInt(ciudades.length)];
                double peso = 0.5 + r.nextDouble() * 8.5;
                Prioridad prioridad = Prioridad.values()[r.nextInt(Prioridad.values().length)];

                Paquete p = new Paquete(codigo, cliente, direccion, ciudad, peso, prioridad);

                c.recepcion.agregar(p);

                c.estadisticas.generado();

                c.log(p.getCodigo() + " recibido");

                Thread.sleep(500 + r.nextInt(300));

            } catch (InterruptedException e) {
                break;
            }
        }
    }
}