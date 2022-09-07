import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.net.ssl.SSLServerSocketFactory;

public class ServidorSSL{

    public static void main(String []args) throws Exception{
        System.setProperty("javax.net.ssl.keyStore","keystore_servidor.jks");
        System.setProperty("javax.net.ssl.keyStorePassword","1234567");
        SSLServerSocketFactory socket_factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        ServerSocket socket_servidor = socket_factory.createServerSocket(50000);  
        Socket conexion = socket_servidor.accept();
        DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
        DataInputStream entrada = new DataInputStream(conexion.getInputStream());
        double x = entrada.readDouble();
        System.out.println(x);
        salida.close();
        entrada.close();
        conexion.close();
    }  
}
