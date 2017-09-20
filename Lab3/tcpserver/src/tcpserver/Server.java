package tcpserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static int port = 9090;
    private ServerSocket ss;
    private Socket client;
    private InputStream StreamFromClient;
    private OutputStream StreamToClient;
    private Double savings = 2000.0;

    boolean validClient(int id) {
        return true;
    }

    public boolean Conect() {
        boolean ret = true;
        try {
            System.out.println("Esperando conexão");
            ss = new ServerSocket(port);
            client = ss.accept();
            System.out.println("Conectou!");
            StreamFromClient = client.getInputStream();
            StreamToClient = client.getOutputStream();
            client.setSoTimeout(5000);
        } catch (IOException e) {
            System.out.println("Não houve conexão.\n Erro: " + e);
            ret = false;
        }
        return ret;
    }

    public void ReadMessages() {
        byte[] request = new byte[1024];
        String message, reply;
        boolean logged = false;
        
        try {
            while (true) {
                StreamFromClient.read(request);
                message = new String(request, "US-ASCII");
                reply = "Comando nao reconhecido";

                if (message.contains("login")) {
                    if (validClient(message.charAt(message.length() - 1) - '0')) {
                        if (!logged)reply = "login realizado!";
                        else reply = "Ja conectado!";
                        logged = true;
                    } else {
                        reply = "senha invalida!";
                    }
                } else if (message.contains("exit")) {
                    break;
                }
                else if (!logged){
                    reply = "Deve-se conectar antes de realizar a operação!";
                } 
                else if (message.contains("saldo")) {
                    reply = "R$1000,00 de saldo";

                } else if (message.contains("saque")) {
                    reply = "saque realizado!";
                } else if (message.contains("logoff")) {
                    reply = "logged-off!";
                    logged = false;
                }
                StreamToClient.write(reply.getBytes());
                StreamToClient.flush();
            }
        } catch (IOException e) {
            System.out.println("Erro na comunição com cliente.\n Erro: " + e);
        }

        System.out.println("Desconectou!");
    }

    public static void main(String[] args) {
        Server server = new Server();
        if (server.Conect()) {
            server.ReadMessages();
        }
    }
}
