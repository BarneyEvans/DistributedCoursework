import java.io.*;
import java.net.*;
class TCPSender {
    public void sendMessageController(int port, String message){
        Socket socket = null;
        try {
            InetAddress address = InetAddress.getLocalHost();
            socket = new Socket(address, port);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), false);
            out.println(message); //out.flush();
            out.flush();
            Thread.sleep(1000);
        } catch(Exception e) { e.printStackTrace();
        } finally {
            if (socket != null)
                try { socket.close(); } catch (IOException e) { System.err.println("error2: " + e); }
        }
    }

    public void sendMessageClient(Socket socket, String message){
        try {
            InetAddress address = InetAddress.getByName("10.14.165.244");
            socket = new Socket(address, 69);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), false);
            out.println(message); //out.flush();
            out.flush();
            Thread.sleep(1000);
        } catch(Exception e) { e.printStackTrace();
        } finally {
            if (socket != null)
                try { socket.close(); } catch (IOException e) { System.err.println("error2: " + e); }
        }
    }

}