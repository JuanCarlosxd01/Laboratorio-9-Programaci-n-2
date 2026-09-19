
package hilos;
import sistema.CentroLogistico;
import modelo.*;

public class AlmacenThread extends Thread{
    
    private final CentroLogistico c;
    
    public AlmacenThread(CentroLogistico c){
        super("Almacen");
        this.c = c;
    }
    
    public void run(){
        while(c.isActivo()){
            try{
                c.esperarSiPausado();
                Paquete p = c.recepcion.extraerMejor(x ->true);
                p.setEstado(EstadoPaquete.ALMACENADO);
                c.almacen.agregar(p);
                c.log(p.getCodigo() + "amacenado");
                Thread.sleep(300);
            } catch(InterruptedException e){
                break;
            }
        }
    }
    
}
