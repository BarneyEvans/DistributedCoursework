import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Dstore {
    static int port;
    static int cport;
    int timeout;
    String file_folder;
    static TCPSender sender;
    public Dstore(int port, int cport, int timeout, String file_folder){
        this.port = port;
        this.cport = cport;
        this.timeout = timeout;
        this.file_folder = file_folder;

        sender = new TCPSender();
        sender.sendMessageController(cport, "JOIN port" + port);

        initialise();
    }

    public static void main(String[] args){
        new Thread(() -> {
            new Dstore(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]), args[3]);
        }).start();
    }

    public void initialise() {
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(port);
            while (true) {
                try {
                    Socket client = ss.accept();
                    new Thread(new Dstore.ServiceThread(client)).start();
                } catch (Exception e) {
                    System.err.println("error: " + e);
                }
            }
        } catch (Exception e) {
            System.err.println("error: " + e);
        } finally {
            if (ss != null)
                try {
                    ss.close();
                } catch (IOException e) {
                    System.err.println("error: " + e);
                }
        }
    }

    static class ServiceThread implements Runnable {
        Socket client;

        ServiceThread(Socket c) {
            client = c;
        }

        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String line;
                String[] words;
                while ((line = in.readLine()) != null) {
                    System.out.println(line);
                    words = line.split(" ");
                    switch (words[0]) {
                        case "STORE" -> storing(words[1], words[2], client);
                    }
                }

                client.close();
            } catch (Exception e) {
                System.err.println("error: " + e);
            }
        }
    }

    private static void storing(String fileName, String fileSize, Socket client) throws IOException {
        String response = "ACK";
        sender.sendMessageClient(client, response);
    }
}
