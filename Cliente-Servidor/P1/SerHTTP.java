import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SerHTTP {
    static String flag="NO DIVIDE";
    public static void main(String[] args) throws Exception {
        ServerSocket servidor = new ServerSocket(8081);
        Socket conexion = servidor.accept();
        BufferedReader entrada = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
        PrintWriter salida = new PrintWriter(conexion.getOutputStream());
        String s=entrada.readLine();
        long num = numero(s);
     
        System.out.println(s);
        if (s.startsWith("GET /primo?numero=")){
            String respuesta = "<html><H1>Entro</H1><br><H2>Numero:"+num+"</H2></html>";
            salida.println("HTTP/1.1 200 OK");
            salida.println("Content-type: text/html; charset=utf-8");
            salida.println("Content-length: "+respuesta.length());
            salida.println();
            salida.flush();
            salida.println(respuesta);
            salida.flush();
        }else{
            salida.println("HTTP/1.1 404 File Not Found");
            salida.flush();
        }
        for(;;){
            calcPrimo w = new calcPrimo(5000,num,2,num-1);
            w.start();
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