
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;


public class Cliente {
    public static void main(String[] args) throws Exception {
        Socket conexion = new Socket("localhost",5000);
        DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
        DataInputStream entrada = new DataInputStream(conexion.getInputStream());
        int num = 97;
        int numIni = 2;
        int numFin = 96;
        salida.writeInt(num);
        salida.writeInt(numIni);
        salida.writeInt(numFin);
        byte[] buffer = new byte[9];
        read(entrada,buffer,0,9);
        System.out.println(new String(buffer,"UTF-8"));
        Thread.sleep(1000);
        entrada.close();
        salida.close();
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