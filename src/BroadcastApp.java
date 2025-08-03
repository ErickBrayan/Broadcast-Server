import java.net.Socket;

public class BroadcastApp {

    public static void main(String[] args) {

        if (args.length != 1) {
            System.err.println("Usage: java BroadcastApp start/connect");
        }

        String command = args[0];

        switch (command) {
            case "start":
                new Server().run();
                break;
            case "connect":
                new  Client().run();
                break;
            default:
                System.err.println("Unknown command: " + command);
        }
    }
}
