import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SerHTTP {
    static String flag="";
    public static void main(String[] args) throws Exception {
        ServerSocket servidor = new ServerSocket(8081);
        Socket conexion = servidor.accept();
        BufferedReader entradaServidor = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
        PrintWriter salidaServidor = new PrintWriter(conexion.getOutputStream());
        String s=entradaServidor.readLine();
        long num = numero(s);
        if (s.startsWith("GET /primo?numero=")){
            for(;;){
                calcPrimo w = new calcPrimo(5000,num,2,num-1);
                w.start();
                if(flag.equals("DIVIDE")){
                    String respuesta = "<html><H1>El numero "+num+" no es primo</H1></html>";
                    salidaServidor.println("HTTP/1.1 200 OK");
                    salidaServidor.println("Content-type: text/html; charset=utf-8");
                    salidaServidor.println("Content-length: "+respuesta.length());
                    salidaServidor.println();
                    salidaServidor.flush();
                    salidaServidor.println(respuesta);
                    salidaServidor.flush();
                    break;
                }
                w.sleep(3000);
                w.join();
            }
        }else{
            salidaServidor.println("HTTP/1.1 404 File Not Found");
            salidaServidor.flush();
        }
    }
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
                Socket conexion = new Socket("localhost",puerto);
                DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
                DataInputStream entrada = new DataInputStream(conexion.getInputStream());
                salida.writeLong(num);
                salida.writeLong(numIni);
                salida.writeLong(numFin);

                flag = entrada.readUTF();
                System.out.println(flag);

                entrada.close();
                salida.close();
                conexion.close();
            } catch (Exception e) {
                //TODO: handle exception
            }
            
        }
    }
    static void read(DataInputStream f,byte[] b,int posicion,int longitud) throws Exception{
        while (longitud > 0){
            int n = f.read(b,posicion,longitud);
            posicion += n;
            longitud -= n;
        }
    }
    static long numero(String s) throws Exception{
        String[] newStr = s.split("\\s+");
        String[] nStr = newStr[1].split("=");
        return Long.parseLong(nStr[1]);
    }
}