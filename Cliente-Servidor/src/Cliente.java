
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;


public class Cliente {
    public static void main(String[] args) throws Exception {
        Socket conexion = new Socket("localhost",5000);
        DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
        DataInputStream entrada = new DataInputStream(conexion.getInputStream());
        salida.writeInt(123);
        salida.writeDouble(1234567890.1234567890);
        salida.write("Hola".getBytes());
        byte[] buffer = new byte[4];
        read(entrada,buffer,0,4);
        System.out.println(new String(buffer,"UTF-8"));
        //conexion.close();
    }
    static void read(DataInputStream f,byte[] b,int posicion,int longitud) throws Exception{
        while (longitud > 0){
            int n = f.read(b,posicion,longitud);
            posicion += n;
            longitud -= n;
        }
    }
}