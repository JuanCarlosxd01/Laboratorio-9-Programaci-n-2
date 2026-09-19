
package hilos;

import sistema.CentroLogistico;
import modelo.*;
import java.util.Random;

public class RecepcionThread extends Thread {

    private final CentroLogistico c;
    private final Random r = new Random();
    private int sec = 1;

    private final String[] clientes = {"Carlos López", "Ana Martínez", "Luis García", "María Rivera", "José Flores"};

    private final String[] ciudades = {"Centro", "Eixample", "Gràcia", "Sant Martí", "Badalona"};

    public RecepcionThread(CentroLogistico c) {
        super("Recepcion");
        this.c = c;
    }

    @Override
    public void run() {
        while (c.isActivo()) {
            try {
                c.esperarSiPausado();
                String codigo = String.format("PKG-%03d", sec++);
                String cliente = clientes[r.nextInt(clientes.length)];
                String direccion = "Calle " + (r.nextInt(90) + 10);
                String ciudad = ciudades[r.nextInt(ciudades.length)];
                double peso = 0.5 + r.nextDouble() * 8.5;
                Prioridad prioridad = Prioridad.values()[r.nextInt(4)];

                Paquete p = new Paquete(codigo, cliente, direccion, ciudad, peso, prioridad);

                c.recepcion.agregar(p);
                c.estadisticas.generado();
                c.log(p.getCodigo() + " recibido");
                Thread.sleep(1200 + r.nextInt(1000));

            } catch (InterruptedException e) {
                break;
            }
        }
    }
    
}