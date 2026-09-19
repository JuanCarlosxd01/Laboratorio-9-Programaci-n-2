
package sistema; 
import hilos.*;
import java.util.ArrayList; 
import java.util.List;


public class Simulador { 
    
    private final CentroLogistico c; 
    private final List<Thread> hilos=new ArrayList<>();
    
    public Simulador(CentroLogistico c){
        this.c=c;
    } 
    
    public synchronized void iniciar(){
        if(c.isActivo()){
            return;
        }
        c.setActivo(true);
        c.setPausado(false);
        hilos.clear();
        hilos.add(new RecepcionThread(c));
        hilos.add(new AlmacenThread(c));
        for(int i=1;i<=3;i++){
            hilos.add(new ClasificadorThread(c,i));
        }
        
        for(int i=1;i<=2;i++){
            hilos.add(new EmpaquetadorThread(c,i));
        }
        
        int[] caps={5,4,6,5};
        
        for(int i=0;i<4;i++){
            hilos.add(new RepartidorThread(c,i+1,caps[i]));
        }
        hilos.forEach(Thread::start);
        c.log("Simulación iniciada");
    } 
    
    public void pausar(){
        c.setPausado(true);
        c.log("Simulación pausada");
    } 
    
    public void reanudar(){
        c.setPausado(false);
        c.log("Simulación reanudada");
    } 
    
    public synchronized void detener(){
        c.setActivo(false);
        c.setPausado(false);
        hilos.forEach(Thread::interrupt);
        hilos.clear();
        c.log("Simulación detenida");
    } 
    
    public void reiniciar(){
        detener();
        c.limpiar();
        iniciar();
    }
}
