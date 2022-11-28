import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

class Servidor2{
    static class Worker extends Thread{
        Socket conexion;
        Worker(Socket conexion){
            this.conexion = conexion;
        }
        public void run(){
            try{
                DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
                DataInputStream entrada = new DataInputStream(conexion.getInputStream());

                long m1 = System.currentTimeMillis();
                for(int i=1 ;i<10001;i++){
                    double x = entrada.readDouble();
                    System.out.println(x);
                }
                salida.write("Hola".getBytes());
                long m2 = System.currentTimeMillis();
                System.out.println("Tiempo(ms): "+ (m2 - m1));
                salida.close();
                entrada.close();
                conexion.close();
            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }

        }
    }
    public static void main(String[] args) throws Exception{
        ServerSocket servidor = new ServerSocket(5000);
        for(;;){
            Socket conexion = servidor.accept();
            Worker w = new Worker(conexion);
            w.start();
        }
    }
    static void read(DataInputStream f,byte[] b,int posicion,int longitud) throws Exception{
        while (longitud > 0){
            int n = f.read(b,posicion,longitud);
            posicion += n;
            longitud -= n;
        }
    }

}