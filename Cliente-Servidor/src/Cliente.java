
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;


public class Cliente {
    public static void main(String[] args) throws Exception {
        // Sending 10000 doubles to the server and then reading the response.
        Socket conexion = new Socket("localhost",5000);
        DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
        DataInputStream entrada = new DataInputStream(conexion.getInputStream());
        long m1 = System.currentTimeMillis();
        for(int i = 1 ;i<10001;i++){
            salida.writeDouble(i);
        }
        ;
        byte[] buffer = new byte[4];
        read(entrada,buffer,0,4);
        System.out.println(new String(buffer,"UTF-8"));
        long m2 = System.currentTimeMillis();
        System.out.println("Tiempo(ms): " + (m2 - m1));
        Thread.sleep(1000);
        conexion.close();
    }
    static void read(DataInputStream f,byte[] b,int posicion,int longitud) throws Exception{
        while (longitud > 0){
            int n = f.read(b,posicion,longitud);
            posicion += n;
            longitud -= n;
        }
    }
}