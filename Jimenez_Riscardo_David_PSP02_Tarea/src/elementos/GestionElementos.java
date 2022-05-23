package elementos;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Título:          GestionElementos
 * Descripción:     Ejemplo de Productor/Consumidor trabajando sobre un recurso compartido tipo cola
 * @author  David Jiménez Riscardo
 * @version 1.0
 */
public class GestionElementos {

    // Nombre del archivo de texto con la configuración de hilos
    private static final String NOMBRE_ARCHIVO_ENTRADA_TXT_DEFAULT = "hilos.cfg";

    // Cola de elementos compartida por productor y consumidores
    private static final ColaElementos colaElementos = new ColaElementos();
    
    // Lista dónde guardaremos una referencia a cada uno de los hilos
    private static List<Thread> misHilos = new LinkedList<>();
    
    // Guardaré el contenido del fichero en memoria, en una estructura ArrayList
    private static ArrayList<String> lista = new ArrayList<>();
    
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    /**
     * Programa principal
     * @param args Argumentos de la línea de comandos. Si el valor es 0 entraríamos en el modo depuración con un registro más detallado de todas las operaciones
     */  
    public static void main(String[] args) {
        int modo = 1; // Por omisión el modo de funcionamiento es "silencioso"

        // Analizamos los posibles argumentos pasados al proceso
        if (args.length > 0) {
            try {
                modo = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                // Formato de número incorrecto. Argumento no válido (no se tiene en cuenta)
            }
        }

        System.out.println("HILOS PRODUCTORES Y CONSUMIDORES");
        System.out.println("--------------------------------");
        System.out.println("Leyendo configuración de hilos del archivo de texto...");

        // Lectura del archivo de configuración de hilos
        boolean lecturaCorrecta = false;
        
        //Leo el fichero y lo guardo en memoria          
        try {           
            FileReader fr = new FileReader(NOMBRE_ARCHIVO_ENTRADA_TXT_DEFAULT);
            BufferedReader br = new BufferedReader(fr);
            String lineaFichero = br.readLine();
                      
            while( lineaFichero != null){

                lista.add(lineaFichero);
                lineaFichero = br.readLine();
            }

            if( br != null ) br.close();
            if( fr != null ) fr.close(); 
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GestionElementos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GestionElementos.class.getName()).log(Level.SEVERE, null, ex);
        }    
                   
        //Muestro por pantalla el ArrayList
        /*
        for(String lineaArray : lista){
            System.out.println(lineaArray); 
        }*/
            
        if(lista.size()>0){
            lecturaCorrecta = true;
        }else {
            System.out.println("No pudo leerse el archivo de texto.");
        }
                       
        if (lecturaCorrecta) {
            
            String[] partesLinea;
            String tipo, nombre;
            int tiempo, numElementos;
            
            //Recorro el ArrayList y voy generando los hilos, los cuales guardaré en una lista
            for(String linea : lista){

                partesLinea = linea.split("::");
                tipo = partesLinea[0];
                nombre = partesLinea[1];
                tiempo = Integer.parseInt(partesLinea[2]);
                numElementos = Integer.parseInt(partesLinea[3]);
                
                System.out.println("Hilo "+tipo+": nombre '"+nombre+"', tiempo="+tiempo+", número de elementos="+numElementos);

                if(tipo.equals("Productor")){
                    ProductorElementos productor = new ProductorElementos(nombre, colaElementos, tiempo, numElementos, modo);
                    misHilos.add(productor);
                    //productor.start();
                }
                if(tipo.equals("Consumidor")){
                    ConsumidorElementos consumidor = new ConsumidorElementos(nombre, colaElementos, tiempo, numElementos, modo);
                    misHilos.add(consumidor);
                    //consumidor.start();
                }                    
            }

            System.out.println("\nCargada la configuración de hilos.");
            System.out.println("Cantidad de hilos:"+misHilos.size());

            System.out.println("\nEjecución de hilos concurrentes");
            System.out.println("--------------------------------");
            
            //Lanzo todos los hilos guardados en la lista de hilos
            for (Thread hilo : misHilos){

                hilo.start();
            }
            
            //Esperamos a que finalice la ejecución de todos los hilos para finalizar el programa principal
            for (Thread hilo : misHilos){
                
                try {
                    hilo.join();
                } catch (InterruptedException ex) {
                    Logger.getLogger(GestionElementos.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
    }//Fin del método main

}//Fin de la clase
