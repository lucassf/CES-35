package tcpserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Server {
    
    private static int port = 9090;
    private ServerSocket ss;
    private Socket client;
    private InputStream StreamFromClient;
    private OutputStream StreamToClient;
	
	
    public boolean Conect ()
    {
        boolean ret = true;
        try
        {
            System.out.println("Esperando conexão");
            ss = new ServerSocket (port);
            client = ss.accept();
            System.out.println("Conectou!");
            StreamFromClient = client.getInputStream();
            StreamToClient = client.getOutputStream();
            client.setSoTimeout(5000);
        }
        catch (IOException e)
        {
            System.out.println("Não houve conexão.\n Erro: "+e);
            ret = false;
        }
        return ret;
    }

    public void ReadMessages()
    {
        byte[] request = new byte[1024];
        String message, reply;
        boolean exit = false;    
       
        while (!exit)
        {
            try
            {
                StreamFromClient.read(request);
                message = new String (request, "US-ASCII");
                reply = "Comando nao reconhecido";
                
                if (message.contains("login"))
                {
                    reply = "login realizado!";
                }
                else if (message.contains("saldo"))
                {
                    reply = "R$1000,00 de saldo";
                        
                }
                else if (message.contains("saque"))
                {
                    reply = "saque realizado!";
                }
                else if (message.contains("logoff"))
                {
                    reply = "logged-off!";
                    exit = true;
                }
                StreamToClient.write(reply.getBytes());
            }

            catch (IOException e)
            {
                System.out.println("Erro na comunição com cliente.\n Erro: " + e);
                break;
            }
        }
        System.out.println("Desconectou!");
    }
    public static void main (String[] args)
    {
        Server server = new Server();
        if (server.Conect())
            server.ReadMessages();
    }
}
