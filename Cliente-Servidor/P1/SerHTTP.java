import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SerHTTP {
    static String flag = "";
    static int servidores;
    static Object obj = new Object();
    public static void main(String[] args) throws Exception {
        //Establecimiento de conexión con el navegador
        ServerSocket servidor = new ServerSocket(8081);
        Socket conexion = servidor.accept();
        servidores=0;
        BufferedReader entradaServidor = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
        PrintWriter salidaServidor = new PrintWriter(conexion.getOutputStream());
        String s=entradaServidor.readLine();
        long num = numero(s);
        long m = num/2;
        if (s.startsWith("GET /primo?numero=")){
            for(;;){
                calcPrimo w1 = new calcPrimo(5000,num,2,m/4);
                calcPrimo w2 = new calcPrimo(5001, num, (m/4)+1, m/2);
                calcPrimo w3 = new calcPrimo(5002, num, (m/2)+1, (3*m)/4);
                calcPrimo w4 = new calcPrimo(5003, num, ((3*m)/4)+1, m);
                w1.start();
                w2.start();
                w3.start();
                w4.start();
                // Condicinales para enviar al navegador el resultado del procesamiento del numero primo/no primo
                if(flag.equals("DIVIDE")){
                    String respuesta = "<html><H1>El numero "+num+" no es primo</H1></html>";
                    enviarPagina(salidaServidor, respuesta);
                    break;
                }
                if(servidores==4 && flag.equals("NO DIVIDE")){
                    String respuesta = "<html><H1>El numero "+num+" es primo</H1></html>";
                    enviarPagina(salidaServidor, respuesta);
                    System.out.println("entro al caso primo");
                    break;
                }
                // Barrera para los hilos con los servidores 
                w1.join();
                w2.join();
                w3.join();
                w4.join();
            }
        }else{
            salidaServidor.println("HTTP/1.1 404 File Not Found");
            salidaServidor.flush();
        }
    }
    // Definicion de los hilos
    // Realiza la conexión con el servidor que le corresponde a su puerto
    static class calcPrimo extends Thread {
        long num,numIni,numFin;
        int puerto;
        calcPrimo(int puerto,long num,long numIni,long numFin){
            this.num = num;
            this.numFin = numFin;
            this.numIni = numIni;
            this.puerto = puerto;    
        }
        public void run(){
            try {
                //Establecimiento de la conexión con el servidor 
                Socket conexion = new Socket("localhost",puerto);
                DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
                DataInputStream entrada = new DataInputStream(conexion.getInputStream());
                // Envio de datos al servidor numero , inicio y fin del rango a evaluar
                salida.writeLong(num);  
                salida.writeLong(numIni);
                salida.writeLong(numFin);
                // Fin de envio de datos

                // Sincronizacion de las variables
                synchronized(obj){
                    flag = entrada.readUTF();
                    if(flag.equals("NO DIVIDE")){
                        servidores++;
                    }
                }
                // Fin de la sincronización

                //System.out.println("Variable servidores:"+servidores+" Flag:"+flag);
                
                // Cierre de conexion y buffers
                entrada.close();
                salida.close();
                conexion.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            
        }
    }
    // Funcion read
    // Asegura la correcta lectura del buffer 
    // NOTA: No se ocupa en este programa 
    static void read(DataInputStream f,byte[] b,int posicion,int longitud) throws Exception{
        while (longitud > 0){
            int n = f.read(b,posicion,longitud);
            posicion += n;
            longitud -= n;
        }
    }
    // Funcion numero
    // A partir de la peticion del navegador convierte el numero en un long 
    static long numero(String s) throws Exception{
        String[] newStr = s.split("\\s+");
        String[] nStr = newStr[1].split("=");
        return Long.parseLong(nStr[1]);
    }
    // Funcion enviarPagina
    // Envia lo necesario al navegador para poder mostrar una estructura de texto HTML definida en el mensaje 
    static void enviarPagina(PrintWriter salidaBuffer, String mensaje){
        salidaBuffer.println("HTTP/1.1 200 OK");
        salidaBuffer.println("Content-type: text/html; charset=utf-8");
        salidaBuffer.println("Content-length: "+mensaje.length());
        salidaBuffer.println();
        salidaBuffer.flush();
        salidaBuffer.println(mensaje);
        salidaBuffer.flush();
    }
}