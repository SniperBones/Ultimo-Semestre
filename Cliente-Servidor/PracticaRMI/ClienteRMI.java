import java.rmi.Naming;

public class ClienteRMI {
    static double[][] A ;
    static double[][] B ;
    static double[][] C ;
    static int N;
    static int M;
    public static void main(String[] args) throws Exception{
        N = Integer.parseInt(args[0]);
        M = Integer.parseInt(args[1]);
        Matriz.crearMatriz(N, M);
        double [][][] partA = new double [6][N/6][N];
        double [][][] partB = new double [6][N/6][N];
        double [][][] partC = new double [36][N/6][N/6];
        for(int y=0;y<6;y++){
            Matriz.partirMatriz(partA[y],A, y+1);
            Matriz.partirMatriz(partB[y],B, y+1);
        }
        String url = "rmi://localhost/matrices";
        String url2 = "rmi://localhost/matrices";

        Hilo ArrayT[] = new Hilo[24];
        int x=0;
        for(int i=0;i<2;i++){
            for(int i2=0;i2<6;i2++){
                Matriz.multiplica(partC[x],partA[i], partB[i2]);
                x++;
            }
        }
        x = 0;
        for (int i=2;i<6;i++){
            for(int i2=0;i2<6;i2++){
                if(i<4){
                    ArrayT[x] = new Hilo(url, partA[i], partB[i2]);
                }else{
                    ArrayT[x] = new Hilo(url2, partA[i], partB[i2]);
                }
                ArrayT[x].start();
                x++;
            }
        }
        for (int i=0;i<24;i++){
            ArrayT[i].join();
            partC[i+12] = ArrayT[i].getValue();
        }
        x=0;
        for(int i=0;i<6;i++){
            for(int y=0;y<6;y++){
                Matriz.unirMatriz(C, partC[x], i*(N/6), y*(N/6));
                x++;
            }
        }
        if(N<=18 && M<=18){
            Matriz.mostrarMatriz(A, "A",true); 
            Matriz.transponer(B);
            Matriz.mostrarMatriz(B, "B",false);
            Matriz.mostrarMatriz2(C, "C");
        }
        Matriz.checksum(C);
    }

    public static class Hilo extends Thread{
        String url;
        double [][] C,A,B;
        Hilo(String url,double A[][],double B[][]){
            this.url = url;
            this.A = A;
            this.B = B;
        }
        public void run(){
            try {
                InterfaceRMI r = (InterfaceRMI) Naming.lookup(url);
                C=r.multiplica(A,B);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        public double[][] getValue(){
            return C;
        }
    }

    public static class Matriz{
        public static void crearMatriz(int N,int M){
            A = new double [N][N];
            B = new double [N][N];
            C = new double [N][N];
            for (int i = 0; i < N; i++){
                for (int j = 0; j < N; j++){
                    if(j < M){
                        A[i][j] = (3 * i) + (2 * j);
                    }else{
                        A[i][j] = 0;
                    }
                    if(i < M){
                        B[i][j] = (2 * i) - (3 * j);
                    }else{
                        B[i][j] = 0;
                    }
                    C[i][j] = 0;
                }
            }
            transponer(B);
        }

        public static void transponer(double[][] matriz){
            for (int i = 0; i < matriz.length; i++){
                for (int j = 0; j < i; j++){
                    double x = matriz[i][j];
                    matriz[i][j] = matriz[j][i];
                    matriz[j][i] = x;
                }
            }
        }

        public static void mostrarMatriz2(double[][] matriz,String nombre){
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
        public static void mostrarMatriz(double[][] matriz,String nombre,boolean f){
            int limitN;
            int limitM;
            if(f){
                limitN=N;
                limitM=M;
            }else{
                limitN=M;
                limitM=N;
            }
            System.out.println("------- Matriz "+nombre+" ---------");
            for (int x=0; x < limitN; x++) {
                System.out.print("|");
                for (int y=0; y < limitM; y++) {
                System.out.print (matriz[x][y]);
                if (y!=limitM-1) System.out.print("\t");
                }
                System.out.println("|");
            }
            System.out.println("------- Matriz "+nombre+" ---------");
        }

        public static void multiplica(double[][] C,double[][] A,double[][] B){
            int filasA = A.length;
            int filasB = B.length;
            for (int i = 0; i < filasA; i++){
                for (int j = 0; j < filasB; j++){
                    for (int k = 0; k < B[0].length; k++){
                        C[i][j] += A[i][k] * B[j][k];
                    }
                }
            }
        }
        public static void checksum(double[][] matriz){
            double suma = 0;
            for (int x=0; x < matriz.length; x++) {
                for (int y=0; y < matriz[x].length; y++) {
                    suma+= matriz[x][y];
                }
            }
            System.out.println("Checksum:"+suma);
        }
        
        public static void partirMatriz(double[][] C,double[][] matriz,int num){
            int nMatriz = matriz.length;
            int inicio = ((num-1)*nMatriz)/6;
            for (int i = 0; i < C.length; i++){
                for (int j = 0; j < C[0].length; j++){
                    C[i][j] = matriz[inicio+i][j];
                }
            }
        }
        public static void unirMatriz(double[][] C,double[][] A,int renglon,int columna){
            for (int i = 0; i < N/6; i++){
                for (int j = 0; j < N/6; j++){
                    C[i + renglon][j + columna] = A[i][j];
                }
            }
        }
    }
}