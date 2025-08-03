import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {

    private final ArrayList<ClientHandler> clients;

    private static final int PORT = 5000;
    private ServerSocket serverSocket;
    private boolean done;
    private ExecutorService executor;



    public Server() {
        clients = new ArrayList<>();
        done = false;

    }

    @Override
    public void run() {

        Socket socket;

        try {

            serverSocket = new ServerSocket(PORT);
            System.out.println("Server is listening on port " + PORT);
            executor = Executors.newCachedThreadPool();
            while (!done) {
                socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket);
                clients.add(clientHandler);
                executor.execute(clientHandler);
            }



        } catch (IOException e) {

            throw new RuntimeException(e);

        }

    }

    public void broadcast(String message) throws IOException {

        synchronized (clients) {
            Iterator<ClientHandler> it = clients.iterator();
            while (it.hasNext()) {
                ClientHandler clientHandler = it.next();
                try {
                    clientHandler.sendMessage(message);
                } catch (IOException e) {
                    it.remove();
                }
            }
        }
    }

    public void shutdown() throws IOException {

        done = true;
        executor.shutdown();
        if (!serverSocket.isClosed()) {
            serverSocket.close();
        }

        for (ClientHandler clientHandler : clients) {
            clientHandler.shutdown();
        }
    }

    class ClientHandler implements Runnable {

        private Socket socket;
        public DataInputStream in;
        public DataOutputStream out;
        private String name;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try  {
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());

                out.writeUTF("Please enter your name: ");
                name = in.readUTF();

                System.out.println(name + " connected to the server!");
                broadcast(name + "joined the chat!");

                String message;

                while ((message = in.readUTF()) != null) {
                    if (message.startsWith("/quit")) {
                        System.out.println(name + " quitted from the chat!");
                        broadcast(name + " left the chat!");
                        shutdown();
                        break;
                    }else {
                        broadcast(name + " : " + message);
                    }
                }


            } catch (IOException e) {
                //
                System.out.println("error -> " + e.getMessage());
                System.out.println(name + " disconnected unexpectedly.");
                try {
                    broadcast(name + "left the chat!");
                }catch (IOException e1) {
                    //
                }

            }finally {
                try {
                    shutdown();
                }catch (IOException e1) {
                    //
                }

                synchronized (clients) {
                    clients.remove(this);
                }
            }
        }

        public void sendMessage(String message) throws IOException {
            out.writeUTF(message);
        }

        public void shutdown() throws IOException {
            if (in != null) in.close();
            if (out != null) out.close();

            if (!socket.isClosed() && socket != null) {
                socket.close();
            }
        }
    }

}