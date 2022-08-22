import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

public class Servidor {
    public static void main(String[] args) throws Exception {
        ServerSocket servidor = new ServerSocket(5000);
        Socket conexion = servidor.accept();
        DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
        DataInputStream entrada = new DataInputStream(conexion.getInputStream());
        long m1 = System.currentTimeMillis();
        byte[] a = new byte[10000*8];
        read(entrada,a,0,10000*8);
        ByteBuffer b = ByteBuffer.wrap(a);
        for (int i = 1; i < 10001; i++) System.out.println(b.getDouble());
        salida.write("Hola".getBytes());
        long m2 = System.currentTimeMillis();
        System.out.println("Tiempo(ms): "+ (m2 - m1));
        System.out.println();
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
