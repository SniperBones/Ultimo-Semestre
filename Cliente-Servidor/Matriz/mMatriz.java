import java.io.IOException;
import java.net.ServerSocket;
import java.io.*;
import java.net.Socket;
import java.rmi.ServerRuntimeException;
import javax.naming.directory.InvalidAttributeIdentifierException;
import javax.sound.sampled.FloatControl;

public class mMatriz{
    static float[][] A ;
    static float[][] B ;
    static float[][] BT ;
    static float[][] C ;
    static int servPort=50000;
    static String ip = "localhost";

    public static void main(String[] args) throws Exception{
        if(args.length < 2) throw new Error("\n\n\tEscriba todos los argumentos (Nodo y Tamaño de matriz)\n");
        if(args[0].equals("0")){
            Matriz.crearMatriz(Integer.parseInt(args[1]));
            Cliente conexion = new Cliente("20.83.153.8", 50000);
            Cliente conexion2 = new Cliente("4.227.185.167", 50000);
            float[][] A1,A2,C1,C2;

            A1 = Matriz.partirMatriz(A, true);
            A2 = Matriz.partirMatriz(A, false);

            conexion.enviarMatriz(A1);
            conexion.enviarMatriz(BT);

            conexion2.enviarMatriz(A2);
            conexion2.enviarMatriz(BT);

            C1=conexion.recibirMatriz();
            C2=conexion2.recibirMatriz();

            C=Matriz.unirMatriz(C1, C2);                                                                                                                            
            if(Integer.parseInt(args[1])<=16){
                Matriz.mostrarMatriz(A, "A");
                Matriz.mostrarMatriz(B, "B");
                Matriz.mostrarMatriz(C, "C");
            }
            Matriz.checksum(C);
        }else{
            Servidor conexion = new Servidor(servPort);
            float[][] particionA = conexion.recibirMatriz();
            float[][] particionB = conexion.recibirMatriz();
            float[][] particionC = Matriz.multiplica(particionA, particionB);
            conexion.enviarMatriz(particionC);
        }
    }

    public static class Matriz{
        public static void crearMatriz(int N){
            A = new float [N][N];
            B = new float [N][N];
            C = new float [N][N];
            BT = new float [N][N];
            for (int i = 0; i < N; i++){
                for (int j = 0; j < N; j++){
                    A[i][j] = i + 3 * j;
                    BT[i][j]= B[i][j] = 2 * i - j;
                    C[i][j] = 0;
                }
            }
            transponer(BT);
        }

        public static void transponer(float[][] matriz){
            for (int i = 0; i < matriz.length; i++){
                for (int j = 0; j < i; j++){
                    float x = matriz[i][j];
                    matriz[i][j] = matriz[j][i];
                    matriz[j][i] = x;
                }
            }
        }

        public static void mostrarMatriz(float[][] matriz,String nombre){
            System.out.println("------- Matriz "+nombre+" ---------");
            for (int x=0; x < matriz.length; x++) {
                System.out.print("|");
                for (int y=0; y < matriz[x].length; y++) {
                System.out.print (matriz[x][y]);
                if (y!=matriz[x].length-1) System.out.print("\t");
                }
                System.out.println("|");
            }
            System.out.println("------- Matriz "+nombre+" ---------");
        }

        public static float[][] multiplica(float[][] A,float[][] B){
            int filasA = A.length;
            int filasB = B.length;
            int columnasB = B[0].length;
            float [][] auxC = new float[filasA][filasB];
            for (int i = 0; i < filasA; i++){
                for (int j = 0; j < filasB; j++){
                    for (int k = 0; k < B[0].length; k++){
                        auxC[i][j] += A[i][k] * B[j][k];
                    }
                }
            }
            return auxC;
        }
        public static void checksum(float[][] matriz){
            float suma = 0;
            for (int x=0; x < matriz.length; x++) {
                for (int y=0; y < matriz[x].length; y++) {
                    suma+= matriz[x][y];
                }
            }
            System.out.println("Checksum:"+suma);
        }
        
        public static float[][] partirMatriz(float[][] matriz,boolean flag){
            int size = matriz.length/2;
            float[][] newMatriz = new float[size][matriz.length];
            if(flag){
                for (int i = 0; i < size; i++){
                    for (int j = 0; j < matriz.length; j++){
                        newMatriz[i][j] = matriz[i][j];
                    }
                }
            }else{
                for (int i = size; i < matriz.length; i++){
                    for (int j = 0; j < matriz.length; j++){
                        newMatriz[i-size][j] = matriz[i][j];
                    }
                }
            }
            return newMatriz;
        }
        public static float[][] unirMatriz(float[][] C1,float[][] C2){
            int size = C1.length;
            int size2 = C1[0].length;
            float[][] newMatriz = new float[size2][size2];
            for(int i=0;i<size;i++){
                for(int j=0;j<size2;j++){
                    newMatriz[i][j] = C1[i][j];
                    newMatriz[i+size][j] = C2[i][j];
                }
            }
            return newMatriz;
        }
    }
    public static class Cliente{
        private Socket cliente;
        public Cliente(String ip , int port) throws InterruptedException{
            for(;;){
                try{
                    cliente =  new Socket(ip,port);
                    break;
                }catch(IOException e){
                    Thread.sleep(5000);
                }
            }
        }
        public void cerrar() throws IOException {
            cliente.close();
        }
        public float[][] recibirMatriz() throws IOException, ClassNotFoundException {
            ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());
            return (float[][]) entrada.readObject();
        }

        public void enviarMatriz(float[][] C) throws IOException {
            ObjectOutputStream salida = new ObjectOutputStream(cliente.getOutputStream());
            salida.writeObject(C);
        }
    }

    public static class Servidor{
        private ServerSocket servidor;
        private final Socket cliente ;
        public Servidor(int port) throws IOException {
            servidor = new ServerSocket(port);
            cliente = servidor.accept();
        }
        public float[][] recibirMatriz() throws IOException, ClassNotFoundException {
            ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());
            return (float[][]) entrada.readObject();
        }

       public void enviarMatriz(float[][] matriz) throws IOException {
            ObjectOutputStream salida = new ObjectOutputStream(cliente.getOutputStream());
            salida.writeObject(matriz);
            salida.close();
            cliente.close();
        }
    }
}