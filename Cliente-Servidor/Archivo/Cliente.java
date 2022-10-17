import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import javax.net.ssl.SSLSocketFactory;
import java.io.RandomAccessFile;

public class Cliente {
    // Clase Hilo con la conexión en al servidor
    static class Hilo extends Thread{
        String Archivo;
        Socket conexion;
        public Hilo(String Archivo){
            this.Archivo = Archivo;
        }
        public void run(){
            try {
                SSLSocketFactory cliente = (SSLSocketFactory) SSLSocketFactory.getDefault();
                // Intentos de reconexión al servidor
                for(;;){
                    try{
                        Socket conexion = cliente.createSocket("localhost",50000);
                        this.conexion = conexion;
                        break;
                    }catch(Exception ee){
                        System.out.println("Intentando conectar con el servidor");
                        Thread.sleep(1000);
                    }
                }
                DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
                DataInputStream entrada = new DataInputStream(conexion.getInputStream());
                salida.writeUTF(Archivo); // Se envia el nombre del archivo
                byte[] buffer = leer_archivo(Archivo);  // Se obtiene la informacion del archivo
                salida.writeInt(buffer.length); // Se envia el tamaño del archivo
                salida.write(buffer); // Se envia la informacion del archivo
                System.out.println(entrada.readUTF()); // Respuesta del servidor
                entrada.close();
                salida.close();
                conexion.close();   //Cierre de la conexión 
            } catch (IOException e) {
                e.printStackTrace();
            }catch (InterruptedException e) {
                e.printStackTrace();
            }  
        }
    }
    public static void main(String[] args) throws Exception {
        // Socket seguro
        System.setProperty("javax.net.ssl.trustStore","keystore_cliente.jks");
        System.setProperty("javax.net.ssl.trustStorePassword","123456");
        if (args.length < 1) {  // Caso de error 
            System.err.println("Ingrese los nombres de los archivos"); 
            System.exit(1);
        }
        for(int i=0;i<args.length;i++){ // Creacion de los hilos por archivo
            Hilo h = new Hilo(args[i]);
            h.start();
        }
    }

    // Funcion para obtener la informacion de los archivos 

    static byte[] leer_archivo(String archivo) throws IOException{  
        RandomAccessFile f = new RandomAccessFile("CF/"+archivo, "r");
        byte[] b = new byte[(int)f.length()];
        f.readFully(b);
        return b;
    }
}
