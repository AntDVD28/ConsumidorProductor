package elementos;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Título:          ConsumidorElementos
 * Descripción:     Clase para la gestión de los consumidores
 * @author  David Jiménez Riscardo
 * @version 1.0
 */
public class ConsumidorElementos extends Thread {
    

    String nombreConsumidor;
    ColaElementos colaElementos;
    int tiempoConsumo;
    int cantidadElementos;
    //0: modo depuración, 1: modo silencioso
    int modoFuncionamiento;
    //array donde guardaremos los elementos consumidos
    char[] elementosConsumidos;
    
    /**
     * Método constructor
     * @param nombre Nombre del consumidor
     * @param cola Recurso compartido de tipo cola
     * @param tiempo Tiempo que tarda en consumirse cada elemento
     * @param cantidad Número de elementos que serán consumidos
     * @param modo Modo de funcionamiento, 0 para el modo de depuración
     */
    ConsumidorElementos (String nombre, ColaElementos cola, int tiempo, int cantidad, int modo){        
        nombreConsumidor = nombre;
        colaElementos = cola;
        tiempoConsumo = tiempo;
        cantidadElementos = cantidad;
        modoFuncionamiento = modo;
    }
    
    /**
     * Método necesario en el desarrollo de hilos
     */
    public void run(){
        
        String cadenaElementos="";
        
        //instanciamos un array dónde guardaremos los elementos consumidos
        elementosConsumidos = new char[cantidadElementos];
        
        if(modoFuncionamiento==0) 
            System.out.println(GestionElementos.ANSI_PURPLE + "Inicio del consumidor "+nombreConsumidor);
        
        for(int i=0; i<cantidadElementos; i++){
            
            synchronized(colaElementos){
                //si la cola estuviese vacía tengo que esperar a que tenga contenido
                while(colaElementos.size()==0){
                    try {
                        colaElementos.wait();
                        if(modoFuncionamiento==0)
                            System.out.println(GestionElementos.ANSI_RED + "Consumidor "+nombreConsumidor+" esperando. No hay elementos disponibles.");
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ConsumidorElementos.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                //Consumiendo elemento... simulamos un tiempo
                try {
                    TimeUnit.MILLISECONDS.sleep(tiempoConsumo);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ProductorElementos.class.getName()).log(Level.SEVERE, null, ex);
                }
                //Obtengo el elemento de la cola y lo guardo en el array
                elementosConsumidos[i] = colaElementos.getElemento();
               
                //Obtengo una cadena con los elementos que llevo consumidos
                cadenaElementos = getStringLlevoConsumidos(elementosConsumidos, i+1);
                
                if(modoFuncionamiento==0)
                    System.out.println(GestionElementos.ANSI_GREEN + "Consumidor "+nombreConsumidor+" consumiendo elemento "+elementosConsumidos[i]+
                            "| Consumidos hasta ahora: "+cadenaElementos+" | Elementos disponibles en la cola: ["+colaElementos.toString()+"]");
            }
        }//fin del bucle for
        
        //Obtengo una cadena con todos los elementos consumidos
        cadenaElementos = getStringTodosConsumidos(elementosConsumidos);
            
        //Es recomendable mostrar por pantalla la información en una sóla línea. Si ponemos varias, podrían entremezclarse con la información de otros hilos
        System.out.println(GestionElementos.ANSI_PURPLE + "Fin del consumidor "+nombreConsumidor+". Elementos consumidos: ["+cadenaElementos+"]"+
                " | Elementos disponibles en la cola: ["+colaElementos.toString()+"]");     
    }//Fin del método run
  
    
    /**
     * Función mediante la cual obtenemos todos los elementos de un array concatenados en una cadena, estarán separados por comas
     * @param array Array de tipo char
     * @return Cadena formada por todos los elementos del array separados por coma
     */
    public String getStringTodosConsumidos(char[] array){
        
        String cadena="";
        
        for(int i=0; i<array.length;i++){
            
            cadena += array[i];
            if(i!=array.length-1)
                cadena += ","; 
        }
        return cadena.trim();
    }
    
    /**
     * Función mediante la cual obtenemos todos los elementos del array concatenados en una cadena, estarán separados por comas
     * @param array Array de tipo char
     * @param tamanio Tamaño del array
     * @return Cadena formada por todos los elementos del array separados por coma
     */
    public String getStringLlevoConsumidos(char[] array, int tamanio){
        
        String cadena="";
        
        for(int i=0; i<tamanio;i++){
            
            cadena += array[i];
            if(i!=tamanio-1)
               cadena += ","; 
        }
        return cadena.trim();
        
    }
    
    /*
    public static void main(String args[]){
        //Creamos una cola 
        ColaElementos ce = new ColaElementos();
        
        ConsumidorElementos consumidor = new ConsumidorElementos("c1", ce, 5000, 7, 0);
        consumidor.start();
        ProductorElementos productor = new ProductorElementos("p1", ce, 1000, 8, 0);
        productor.start();       
    }*/
    
}
