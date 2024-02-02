package main;

import actions.Action;
import actions.ReduceAction;
import actions.WordCountMapAction;
import utils.Request;
import utils.Utils;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Client {
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) {
        try {
            // Crear socket server
            Socket socket = new Socket("localhost", 12345);

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

            
            saveOutputInResources();
    		
    		//Creo una lista de textos a procesar.
    		
    		List<String> textos = new ArrayList<String>();
    		textos.add(Utils.readFileAsString("src/main/resources/books/Libro1.txt"));
    		textos.add(Utils.readFileAsString("src/main/resources/books/Libro2.txt"));
    		textos.add(Utils.readFileAsString("src/main/resources/books/Libro3.txt"));
    		textos.add(Utils.readFileAsString("src/main/resources/books/Libro4.txt"));
    		textos.add(Utils.readFileAsString("src/main/resources/books/Libro5.txt"));
    		textos.add(Utils.readFileAsString("src/main/resources/books/Libro6.txt"));
    		textos.add(Utils.readFileAsString("src/main/resources/books/Libro7.txt"));
    		textos.add(Utils.readFileAsString("src/main/resources/books/Libro8.txt"));
    		textos.add(Utils.readFileAsString("src/main/resources/books/Libro9.txt"));
    		textos.add(Utils.readFileAsString("src/main/resources/books/Libro10.txt"));
            
    		
    		//Creo las acciones que voy a utilizar.
    		Action<String, Map<String,Integer>> wordCountMapAction = new WordCountMapAction();
    		
            
            //EL CLIENTE CREA LA PETICIÓN
            
            Request clientRequest = new Request(wordCountMapAction,textos,true,true);

            // MANDA LA PETICIÓN AL SERVIDOR.
            out.writeObject(clientRequest);

            
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            // RECIBE LA PETICIÓN DEL SERVIDOR RESPONDIDA.
            Request serverResponse = (Request) in.readObject();
            List<Map<String, Integer>> result = (List<Map<String, Integer>>) serverResponse.getResult();
           
            
            for(Map<String,Integer> map : result) {
            	Utils.printMap(map);
            }
            

            // Cerrar
            socket.close();
            

            // Crear socket server
            Socket socket2 = new Socket("localhost", 12345);

            ObjectOutputStream out2 = new ObjectOutputStream(socket2.getOutputStream());

    		
    		//Creo las acciones que voy a utilizar.
    		Action<List<Map<String, Integer>>, Map<String, Integer>> reduceAction =
    				new ReduceAction();
            
            //EL CLIENTE CREA LA PETICIÓN
            
            Request clientRequest2 = new Request(reduceAction,result,false,false);

            // MANDA LA PETICIÓN AL SERVIDOR.
            out2.writeObject(clientRequest2);

            
            ObjectInputStream in2 = new ObjectInputStream(socket2.getInputStream());

            // RECIBE LA PETICIÓN DEL SERVIDOR RESPONDIDA.
            Request serverResponse2 = (Request) in2.readObject();
            Map<String, Integer> result2 = (Map<String, Integer>) serverResponse2.getResult();
            
            Utils.printMap(result2);

            // Cerrar
            socket2.close();
            
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    private static void saveOutputInResources() {
		// Especifica el archivo al que deseas escribir
        String outputFile = "src/main/resources/output.txt";
        File file = new File(outputFile);

        // Crea un flujo de salida para la consola (System.out)
        OutputStream consoleOutputStream = System.out;

        // Crea un flujo de salida para el archivo
        OutputStream fileOutputStream;
		try {
			fileOutputStream = new FileOutputStream(file);
			// Combina los flujos de salida (consola y archivo)
	        //OutputStream combinedOutputStream = new TeeOutputStream(consoleOutputStream, fileOutputStream);
	     // Crea un nuevo PrintStream que apunta al flujo combinado
	        //PrintStream printStream = new PrintStream(combinedOutputStream);
	     // Redirige la salida estàndar a nuestro nuevo PrintStream
	        //System.setOut(printStream);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
