import java.io.*;
import java.net.*;
import java.io.Console;


public class Chat{
    static void envia_mensaje_multicast(byte[] buffer,String ip,int puerto) throws IOException{
        DatagramSocket socket = new DatagramSocket();
        socket.send(new DatagramPacket(buffer,buffer.length,InetAddress.getByName(ip),puerto));
        socket.close();
    }
    static byte[] recibe_mensaje_multicast(MulticastSocket socket,int longitud_mensaje) throws IOException{
        byte[] buffer = new byte[longitud_mensaje];
        DatagramPacket paquete = new DatagramPacket(buffer,buffer.length);
        socket.receive(paquete);
        return paquete.getData();
    }
    static class Hilo extends Thread{
        public void run() {
            while(true) {
                try{
                    MulticastSocket socket = new MulticastSocket(10000);
                    InetSocketAddress grupo = new InetSocketAddress(InetAddress.getByName("239.10.10.10"),10000);
                    NetworkInterface netInter = NetworkInterface.getByName("eth0");
                    socket.joinGroup(grupo,netInter);
                    byte[] buffer = recibe_mensaje_multicast(socket, 80);
                    System.out.println(new String(buffer, "UTF-8"));
                    socket.leaveGroup(grupo,netInter);
                    socket.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
    public static void main(String[] args) throws IOException{
        Hilo h = new Hilo();
        h.start();
        String nombre = args[0];
        Console console = System.console();
        while(true){
            try{
                Thread.sleep(100);
                System.out.print("Mensaje:");
                String linea = console.readLine();
                System.out.println(linea);
                byte buffer[] = String.format("\n%s:-%s", nombre, linea).getBytes("UTF-8");
                envia_mensaje_multicast(buffer, "239.10.10.10",10000);
            }catch(Exception e){
                e.printStackTrace();
            }
            
        }
    }
}
