
package sistema;

public class Estadisticas {
    
    private int generados;
    private int entregados;
    private int devueltos;
    private long tiempoTotal;
    
    public synchronized void generado(){
        generados++;
    } 
    
    public synchronized void entregado(long ms){
        entregados++;
        tiempoTotal+=ms;
    } 
    
    public synchronized void devuelto(){
        devueltos++;
    }
    
    public synchronized int getGenerados(){
        return generados;
    } 
    
    public synchronized int getEntregados(){
        return entregados;
    } 
    
    public synchronized int getDevueltos(){
        return devueltos;
    }
    
    public synchronized double getPromedioSegundos(){
        return entregados==0?0:(tiempoTotal/1000.0)/entregados;
    }
    
    public synchronized void limpiar(){
        generados=entregados=devueltos=0;
        tiempoTotal=0;
    }
}
