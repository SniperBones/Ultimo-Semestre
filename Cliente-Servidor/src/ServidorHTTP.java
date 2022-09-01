import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

class ServidorHTTP{
    static class Worker extends Thread{
        Socket conexion;
        Worker(Socket conexion){
            this.conexion = conexion;
        }
        public void run(){
            try {
                BufferedReader entrada = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
                PrintWriter salida = new PrintWriter(conexion.getOutputStream());
                String s= entrada.readLine();

                System.out.println(s);
                System.out.println(conexion.getRemoteSocketAddress().toString());
                for (;;){
                    String encabezado= entrada.readLine();
                    System.out.println(encabezado);
                    if (encabezado.equals("")) break;
                }
                if (s.startsWith("GET /hola")){
                    String respuesta = "<html><button onclick='alert(\"Se presionó el botón\")'>Aceptar</button></html>";
                    salida.println("HTTP/1.1 200 OK");
                    salida.println("Content-type: text/html; charset=utf-8");
                    salida.println("Server: ServidorHTTP.java");
                    salida.println("Date: " + new Date());
                    salida.println("Content-length: "+respuesta.length());
                    salida.println();
                    salida.flush();
                    salida.println(respuesta);
                    salida.flush();
                }else{
                    salida.println("HTTP/1.1 404 File Not Found");
                    salida.flush();
                }
            }catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
    public static void main(String[] args) throws Exception{
        ServerSocket servidor = new ServerSocket(8081);
        for (;;){
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