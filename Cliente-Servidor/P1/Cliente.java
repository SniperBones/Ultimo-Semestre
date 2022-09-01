
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;


public class Cliente {
    public static void main(String[] args) throws Exception {
        Socket conexion = new Socket("localhost",5000);
        DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
        DataInputStream entrada = new DataInputStream(conexion.getInputStream());
        long num = 97;
        long numIni = 2;
        long numFin = 96;
        salida.writeLong(num);
        salida.writeLong(numIni);
        salida.writeLong(numFin);
        String hola = entrada.readUTF();
        System.out.println(hola);
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