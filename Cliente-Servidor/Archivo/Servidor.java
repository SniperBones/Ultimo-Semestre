import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import javax.net.ssl.SSLServerSocketFactory;

public class Servidor {
    static class fileThread extends Thread{
        Socket conexion;
        public fileThread(Socket conexion){
            this.conexion = conexion;
        }
        public void run(){
          try {
            DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
            DataInputStream entrada = new DataInputStream(conexion.getInputStream());
            String nomArchivo = entrada.readUTF();  // Se obtiene el nombre de los archivos
            int length = entrada.readInt(); // Se obtiene la longitud del archivo
            ByteBuffer buffer = ByteBuffer.allocate(length); // Creacion del buffer 
            byte[] array = buffer.array();  
            read(entrada, array, 0, length);   // lectura de la entrada 
            if(buffer.capacity()!=0){   // Se valida la escritura del archivo
                Servidor.escribe_archivo("SF/" + nomArchivo, array);
                salida.writeUTF("Archivo OK");
            }else{
                salida.writeUTF("El servidor no pudo guardar el archivo "+nomArchivo);
            }
            conexion.close();  //Cierre de conexión 
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
    }
    public static void main(String []args){
        System.setProperty("javax.net.ssl.keyStore","keystore_servidor.jks");
        System.setProperty("javax.net.ssl.keyStorePassword","1234567");
        // Conexión con los clientes 
        try {
            SSLServerSocketFactory socket_factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            ServerSocket socket = socket_factory.createServerSocket(50000);
            for(;;){
                fileThread w = new fileThread(socket.accept());
                w.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Funcion para escribir el archivo en la carpeta
    static void escribe_archivo(String archivo, byte[] buffer) throws FileNotFoundException, IOException{
        try (FileOutputStream f = new FileOutputStream(archivo)) {
            f.write(buffer);
        }
    }
    // Funcion de lectura de la entrada
    static void read(DataInputStream f,byte[] b,int posicion,int longitud) throws Exception{
        while (longitud > 0){
            int n = f.read(b,posicion,longitud);
            posicion += n;
            longitud -= n;
        }
    }
    
}
