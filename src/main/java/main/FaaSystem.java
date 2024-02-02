package main;

import model.Controller;
import policies.RoundRobin;
import reflect.Calculator;
import utils.Request;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class FaaSystem {

	public static void main(String[] args) {
        ServerSocket serverSocket = null;
        //Creo el Controller con 3 invokers con una capacidad de memoria de 1000 cada invoker.
        //Y le asigno una política.
      

        try {
            // Creo socket server
            serverSocket = new ServerSocket(12345);

            System.out.println("Server is waiting for connections...");

            while (true) {
                // Aceptar peticiones de conexion entrantes
                Socket socket = serverSocket.accept();
                System.out.println("Client connected: " + socket);

                // Manejar el clientes en hilos separados
                Thread clientThread = new Thread(new ClientHandler(socket));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // Cerrar
                if (serverSocket != null && !serverSocket.isClosed()) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

/**
 * Clase cliente
 */
class ClientHandler implements Runnable {
	
	
    private Socket socket;
    private Calculator controller;

    /**
     * Constructor ClientHandler
     * @param socket
     */
    public ClientHandler(Socket socket) {
        this.socket = socket;
        this.controller = new Controller(3,1000, new RoundRobin());
    }

    @SuppressWarnings("rawtypes")
	@Override
    public void run() {
        try {

        	ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            Request clientRequest = null;
			try {
				clientRequest = (Request) in.readObject();
			} catch (ClassNotFoundException e) {
				System.err.println(e.getMessage());
				System.exit(0);
			}
			
            System.out.println("Received request from client: " + clientRequest.getAction());

            Request result = processRequest(clientRequest);

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

            out.writeObject(result);

            // Cerrar socket
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	private Request processRequest(Request request) throws Exception {
    	
    	//Registro la accion a utilizar en el controller, la proceso y devuelvo el resultado.
    	((Controller) controller).registerAction(request.getAction().getName(), request.getAction(), 100);
    	
    	/*		ASI PODRIA INVOCAR DIRECTAMENTE UN ADD DESDE EL PROXY.
        Calculator proxy = (Calculator) ActionProxy.newInstance(controller);

        // Ahora puedes invocar métodos directamente en el proxy
        int result = proxy.add(2, 3);
        System.out.println("Result: " + result);
       */
    	
    	
    	request.setResult(((Controller) controller).createActionProxy(request.getAction().getName(), request.isAsync(), request.isGroupalInvoke()).run(request.getInput()));
    	
        return request;
    }
}

