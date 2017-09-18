package tcpclient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.util.concurrent.TimeUnit;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {
    private static int port = 9090;
    private String host = "localhost";
    private Socket cs;
    private InputStream StreamFromServer;
    private OutputStream StreamToServer;
	
	
    public boolean Conect ()
    {
        boolean ret= true;
        try
        {
            System.out.println("Esperando conexão");
            cs = new Socket (host,port);
            System.out.println("Conectou!");
            StreamFromServer = cs.getInputStream();
            StreamToServer = cs.getOutputStream();
            cs.setSoTimeout(500);
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
        byte[] reply = new byte[1024];
        String message;
        String command[] = new String[]{"login","saldo","saque","logoff"};
        int numBytes;
        
        for (String request:command)
        {
            try{
                TimeUnit.SECONDS.sleep(2);
                System.out.println("Realizando "+request+"...");
                StreamToServer.write(request.getBytes());
                numBytes = StreamFromServer.read(reply);
                message = new String (reply, 0,numBytes);
                System.out.println(message);
            }
            catch (IOException e)
            {
                System.out.println("Erro na comunição com servidor.\nErro: " + e);
                break;
            } catch (InterruptedException ex) {
                System.out.println("Erro em sleep.\n Erro: "+ ex);
                break;
            }
        }
    }
    public static void main (String[] args)
    {
        Client cilent = new Client();
        if (cilent.Conect())
            cilent.ReadMessages();
    }
}
