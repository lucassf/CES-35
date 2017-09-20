package tcpclient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;
import java.net.Socket;

public class Client {

    private static int port = 9090;
    private String host = "localhost";
    private Socket cs;
    private InputStream StreamFromServer;
    private OutputStream StreamToServer;

    public boolean Conect() {
        boolean ret = true;
        try {
            System.out.println("Esperando conexão");
            cs = new Socket(host, port);
            System.out.println("Conectou!");
            StreamFromServer = cs.getInputStream();
            StreamToServer = cs.getOutputStream();
            cs.setSoTimeout(500);
        } catch (IOException e) {
            System.out.println("Servidor fora do ar.\n Erro: " + e);
            ret = false;
        }
        return ret;
    }

    public void SendMessages() {
        byte[] reply = new byte[1024];
        String message;
        String command[] = new String[]{ "login: 0","saque", "logoff","login: 1","saldo", "logoff"};
        int numBytes;
        
        try {
            for (int i=0;i<command.length;i++) {
                String request = command[i];
                System.out.println("Realizando " + request + "...");
                TimeUnit.SECONDS.sleep(2);
                StreamToServer.write(request.getBytes());
                StreamToServer.flush();
                //if (request.equalsIgnoreCase("saque"))TimeUnit.SECONDS.sleep(5);
                numBytes = StreamFromServer.read(reply);
                message = new String(reply, 0, numBytes);
                System.out.println(message);
            }
            StreamToServer.write("exit".getBytes());
            StreamToServer.flush();
        } catch (IOException e) {
            System.out.println("Erro na comunição com servidor.\nErro: " + e);
        } catch (InterruptedException ex) {
            System.out.println("Erro em sleep.\n Erro: " + ex);
        }

    }

    public static void main(String[] args) {
        Client cilent = new Client();
        if (cilent.Conect()) {
            cilent.SendMessages();
        }
    }
}
