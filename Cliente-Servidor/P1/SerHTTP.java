import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SerHTTP {
    
    public static void main(String[] args) throws Exception {
        ServerSocket servidor = new ServerSocket(8081);
        Socket conexion = servidor.accept();
        BufferedReader entrada = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
        PrintWriter salida = new PrintWriter(conexion.getOutputStream());
        String s=entrada.readLine();
        System.out.println(s);
        if (s.startsWith("GET /primo?numero=")){
            String respuesta = "<html><H1>Entro</H1><br><H2>Numero:"+numero(s)+"</H2></html>";
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
        entrada.close();
        conexion.close();
        servidor.close();
    }
    static class calcPrimo extends Thread {
        int num,numIni,numFin;
        int puerto;
        calcPrimo(int puerto,int num,int numIni,int numFin){
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
    static int numero(String s) throws Exception{
        String[] newStr = s.split("\\s+");
        String[] nStr = newStr[1].split("=");
        return Integer.parseInt(nStr[1]);
    }
}