package elementos;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Título:          ProductorElementos
 * Descripción:     Clase para la gestión de los productores
 * @author  David Jiménez Riscardo
 * @version 1.0
 */
public class ProductorElementos extends Thread {
    
           
    String nombreProductor;
    ColaElementos colaElementos;
    int tiempoProduccion;
    int cantidadElementos;
    //0: modo depuración, 1: modo silencioso
    int modoFuncionamiento;
    //array dónde guardaremos los elementos producidos
    char[] elementosProducidos;
 
    
    /**
     * Método constructor
     * @param nombre Nombre del productor
     * @param cola Recurso compartido de tipo cola      
     * @param tiempo Tiempo que tarda en producirse cada elemento
     * @param cantidad Cantidad de elementos que generará el productor
     * @param modo Modo de funcionamiento, 0 para el modo de depuración
     */
    ProductorElementos (String nombre, ColaElementos cola, int tiempo, int cantidad, int modo){        
        nombreProductor = nombre;
        colaElementos = cola;
        tiempoProduccion = tiempo;
        cantidadElementos = cantidad;
        modoFuncionamiento = modo;
    }
    
    /**
     * Método necesario en el desarrollo de hilos
     */
    public void run(){
        char elementos[]={'A','B','C','D','E','F','G','H','I','J','K','L','M','N','Ñ','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
        String cadenaElementos="";
        
        //instanciamos un array dónde guardaremos los elementos producidos
        elementosProducidos = new char[cantidadElementos];
        
        if(modoFuncionamiento==0) 
            System.out.println(GestionElementos.ANSI_BLUE + "Inicio del productor "+nombreProductor);
        
        for(int i=0; i<cantidadElementos; i++){
            
            //Generando elemento... simulamos un tiempo
            try {
                TimeUnit.MILLISECONDS.sleep(tiempoProduccion);
            } catch (InterruptedException ex) {
                Logger.getLogger(ProductorElementos.class.getName()).log(Level.SEVERE, null, ex);
            }

            //guardamos el elemento producido en nuestro array
            elementosProducidos[i] = elementos[i%27];
            
            //debemos de meter en la sección crítica lo esctrictamente necesario para conseguir así una mejor optimización en el uso de la cpu
            synchronized(colaElementos){
                            
                //agregamos el elemento a la cola
                colaElementos.addElemento(elementosProducidos[i]);
                
                if(modoFuncionamiento==0)          
                    System.out.println(GestionElementos.ANSI_CYAN + "Productor "+nombreProductor+" generando y añadiendo elemento "+elementosProducidos[i]+
                            "| Elementos disponibles en la cola: ["+colaElementos.toString()+"]");
                                 
                //desbloqueamos la cola
                colaElementos.notify();                
            }
        } 
          
        //Recorremos el array de elementos producidos y los guardamos en un String para mostrarlos posteriormente por pantalla
        for(int i=0; i<cantidadElementos; i++){
            
            cadenaElementos += elementosProducidos[i];
            if(i!=cantidadElementos-1)
                cadenaElementos += ",";
            
        }
   
        //Es recomendable mostrar por pantalla la información en una sóla línea. Si ponemos varias, podrían entremezclarse con la información de otros hilos
        System.out.println(GestionElementos.ANSI_BLUE + "Fin del productor "+nombreProductor+". Elementos producidos: ["+cadenaElementos.trim()+"]"+
                "| Elementos disponibles en la cola("+nombreProductor+"): ["+colaElementos.toString()+"]");
        
    }//Fin del método run
    
    /*
    public static void main(String[] args){
        //Creamos una cola 
        ColaElementos ce = new ColaElementos();
        //Agregamos elementos a través de un objeto ProductorElementos
        ProductorElementos pe = new ProductorElementos("p1", ce, 1000, 8, 0);
        pe.start();
    }*/
    
}
