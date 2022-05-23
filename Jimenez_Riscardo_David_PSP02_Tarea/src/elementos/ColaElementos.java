package elementos;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Título:          ColaElementos
 * Descripción:     Recurso de tipo cola
 * @author  David Jiménez Riscardo
 * @version 1.0
 */
public class ColaElementos {
    
    private Queue<Character> cola;
    
    
    /**
     * Método constructor
     */
    ColaElementos(){
        
        this.cola = new LinkedList<Character>();
    }
    
    /**
     * Método para agregar un elemento a la cola
     * @param elemento Carácter que agregaremos
     */
    public void addElemento(char elemento){
        this.cola.add(elemento);
    }
    
    /**
     * Método para sacar al primer elemento de la cola
     * @return Primer elemento de la cola
     */
    public char getElemento(){
        if(!this.cola.isEmpty()){
            //Devuelvo y elimino el primer elemento de la cola
            return cola.poll();
        } else {
            return 0;
        }       
    }
    
    /**
     * Método para conocer el tamaño de la cola
     * @return Tamaño de la cola
     */
    public int size(){
        return this.cola.size();
    }
    
    /**
     * Método para obtener una representación textual de la cola
     * @return Cadena de texto con el contenido de la cola. En el caso que la colA esté vacía devolveremos la cadena COLA VACIA
     */
    public String toString(){
        
        String contenidoCola="";
        
        if(!this.cola.isEmpty()){
          Iterator<Character> it = this.cola.iterator();  
          while(it.hasNext()){
             contenidoCola += it.next()+","; 
          }
          //para no incluir la última coma
          if(contenidoCola.charAt(contenidoCola.length()-1) == ',')
              contenidoCola = contenidoCola.substring(0, contenidoCola.length()-1);
        }
        
        if (contenidoCola=="") contenidoCola = "COLA VACIA";
        
        return contenidoCola.trim();
    }
    
    /*
    public static void main(String[] args){  
        //Creamos una cola y agregamos elementos
        ColaElementos ce = new ColaElementos();
        ce.addElemento('A');
        ce.addElemento('B');
        ce.addElemento('C');
        ce.addElemento('D');
        ce.addElemento('E');
        ce.addElemento('F');
        //Mostramos el tamaño de la cola
        System.out.println("Tamaño de la cola: "+ce.size());
        //Mostramos el contenido de la cola
        System.out.println("Contenido de la cola: "+ce.toString());
        //Mostramos el primer elemento de la cola y lo eliminamos de la misma
        System.out.println("Primer elemento de la cola: "+ce.getElemento());
        //Volvemos a mostrar el contenido de la cola para comprobar que se eliminó
        System.out.println("Contenido de la cola: "+ce.toString());
        //Mostramos el segundo elemento de la cola y lo eliminamos de la misma
        System.out.println("Primer elemento de la cola: "+ce.getElemento());
        //Volvemos a mostrar el contenido de la cola para comprobar que se eliminó
        System.out.println("Contenido de la cola: "+ce.toString());        
    }*/
    
    
}
