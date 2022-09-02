import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

class ServidorA{
    static class Worker extends Thread{
        Socket conexion;
        Worker(Socket conexion){
            this.conexion = conexion;
        }
        public void run(){
            try{
                DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
                DataInputStream entrada = new DataInputStream(conexion.getInputStream());
                long num,numIni,numFin;
                long mod;

                // Lectura de las variables enviadas por el servidor B(HTTP) para su evaluacion
                num = entrada.readLong();
                numIni=entrada.readLong();
                numFin=entrada.readLong();

                //System.out.println(num+","+numIni+","+numFin);

                // Ciclo de comprobacion por fuerza bruta del numero primo
                for(long i=numIni;i<=numFin;i++){
                    mod=num%i;
                    // Condicionales que daran respuesta al servidor si el numero es o no primo
                    if(mod==0){
                        salida.writeUTF("DIVIDE");
                        break;
                    }
                    if(i==numFin && mod!=0){
                        salida.writeUTF("NO DIVIDE");
                    }
                }
                // Cierre de los buffers y conexión
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
        // Establecimiento del conexion por el puerto en el argumento
        ServerSocket servidor = new ServerSocket(Integer.parseInt(args[0]));
        System.out.println("Servidor iniciado en el puerto("+args[0]+")");
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