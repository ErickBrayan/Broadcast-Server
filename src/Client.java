import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Runnable {


    private final static int PORT = 5000;
    private final static String HOST = "127.0.0.1";

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private boolean done;


    @Override
    public void run() {

        try {

            socket = new Socket(HOST, PORT);
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());

            if (!socket.isConnected()){
                System.out.println("We have problems to connect to the server, please try again later.");
            }


            System.out.println("WELCOME!!!");

            InputHandler inputHandler = new InputHandler();
            new Thread(inputHandler).start();
            String message;

            while ((message = in.readUTF()) != null) {

                System.out.println(message);

            }

        } catch (IOException e) {
            // handle
        }
    }

    public void shutdown() {
        done = true;
        try {
            in.close();
            out.close();
            if (!socket.isClosed()) {
                socket.close();
            }
        }catch (IOException e) {
            //TODO: handle
        }
    }

    class InputHandler implements Runnable {

        @Override
        public void run() {
            try {

                Scanner input = new Scanner(System.in);

                while (!done) {
                    String message = input.nextLine();
                    if (message.equals("/quit")) {
                        out.writeUTF(message);
                        input.close();
                        shutdown();
                    }else {
                        out.writeUTF(message);

                    }
                }
            } catch (Exception e) {
                //TODO: handle
            }
        }
    }
}



