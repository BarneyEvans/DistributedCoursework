import java.io.*;
import java.net.*;
import java.lang.*;
import java.security.Key;
import java.security.KeyPair;
import java.util.ArrayList;

class Controller {
    static int cport;
    static int R;
    int timeout;
    int rebalance_period;
    static TCPSender sender;

    static ArrayList<String> index = new ArrayList<String>();
    static ArrayList<String> indexValue = new ArrayList<String>();

    public Controller(int cport, int R, int timeout, int rebalance_period){
        this.cport = cport;
        this.R = R;
        this.timeout = timeout;
        this.rebalance_period = rebalance_period;
        sender = new TCPSender();
        initialise();
    }

    public static void main(String[] args){
        new Thread(() -> {
            new Controller(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
        }).start();
    }

    public void initialise() {
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(cport);
            while (true) {
                try {
                    Socket client = ss.accept();
                    new Thread(new ServiceThread(client)).start();
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
                        case "REMOVE" -> removing(words[1], client);
                        case "LOAD" -> loading(words[1],client);
                        case "JOIN" -> rebalance(words[1], client);
                    }
                }

                client.close();
            } catch (Exception e) {
                System.err.println("error: " + e);
            }
        }
    }

    private static void storing(String fileName, String fileSize, Socket client) throws IOException {
        String response = "STORE_TO";
        if(index.indexOf(fileName) != -1){
            response = "ERROR ALREADY_EXISTS";
        }else{
            index.add(fileName);
            indexValue.add("store in progress");
            for (int i = 1; i <= R; i++){
                response += (" port" + i);
            }
        }
        sender.sendMessageClient(client, response);
    }

    private static void removing(String fileName, Socket client) throws IOException {
        String response = "REMOVE_COMPLETE";

        InetAddress address = client.getInetAddress();
        int port = client.getPort();
        Socket responseSocket = new Socket(address, port);
        OutputStreamWriter out = new OutputStreamWriter(responseSocket.getOutputStream());
        out.write(response);
        out.flush();
        responseSocket.close();
    }

    private static void loading(String fileName, Socket client) throws IOException {
        String response = "LOAD_FROM";

        InetAddress address = client.getInetAddress();
        int port = client.getPort();
        Socket responseSocket = new Socket(address, port);
        OutputStreamWriter out = new OutputStreamWriter(responseSocket.getOutputStream());
        out.write(response);
        out.flush();
        responseSocket.close();
    }

    private static void rebalance(String totalPorts, Socket client) throws IOException {
        String response = "LOAD_FROM";

        InetAddress address = client.getInetAddress();
        int port = client.getPort();
        Socket responseSocket = new Socket(address, port);
        OutputStreamWriter out = new OutputStreamWriter(responseSocket.getOutputStream());
        out.write(response);
        out.flush();
        responseSocket.close();
    }
}