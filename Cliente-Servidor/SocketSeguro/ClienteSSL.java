import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import javax.net.ssl.SSLSocketFactory;

public class ClienteSSL{
    public static void main(String []args) throws Exception{
        System.setProperty("javax.net.ssl.trustStore","keystore_cliente.jks");
        System.setProperty("javax.net.ssl.trustStorePassword","123456");
        SSLSocketFactory cliente = (SSLSocketFactory) SSLSocketFactory.getDefault();
        Socket conexion = cliente.createSocket("localhost",50000);
        DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
        DataInputStream entrada = new DataInputStream(conexion.getInputStream());
        salida.writeDouble(123456789.123456789);
        System.out.println("Se envia: 123456789.123456789");
        Thread.sleep(1000);
        entrada.close();
        salida.close();
        conexion.close();
    }
}