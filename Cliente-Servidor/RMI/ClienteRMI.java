import javax.print.attribute.standard.MultipleDocumentHandling;

public class ClienteRMI {
    static float[][] A ;
    static float[][] B ;
    static float[][] BT ;
    static float[][] C ;
    static int N;
    static int M;
    public static void main(String[] args) throws Exception{
        N = Integer.parseInt(args[0]);
        M = Integer.parseInt(args[1]);
        Matriz.crearMatriz(N, M);
        float [][][] partA = new float [N][N][N/6];
        float [][][] partB = new float [N][N][N/6];
        for(int y=0;y<6;y++){
            partA[y]=Matriz.partirMatriz(A, y+1);
            partB[y]=Matriz.partirMatriz(BT, y+1);
        }
        Matriz.mostrarMatriz(A, "A",true);
        for(int y=0;y<6;y++){
            //Matriz.mostrarMatriz2(partA[y], "A"+(y+1));
        }

        Matriz.mostrarMatriz(B, "B",false);
        Matriz.mostrarMatriz2(B, "B");
        for(int y=0;y<6;y++){
            //Matriz.mostrarMatriz2(partB[y], "B"+(y+1));
        }
        C = Matriz.multiplica(A, BT);
        Matriz.mostrarMatriz(C, "C",true);
        //Matriz.mostrarMatriz(BT, "BT");
        //C = Matriz.multiplica(A, BT);
        //Matriz.mostrarMatriz(C, "C");
    }
    public static class Matriz{
        public static void crearMatriz(int N,int M){
            A = new float [N][N];
            B = new float [N][N];
            C = new float [N][N];
            BT = new float [N][N];
            for (int i = 0; i < N; i++){
                for (int j = 0; j < N; j++){
                    if(j < M){
                        A[i][j] = i + 3 * j;
                    }else{
                        A[i][j] = 0;
                    }
                    if(i < M){
                        BT[i][j]= B[i][j] = 2 * i - j;
                    }else{
                        BT[i][j]= B[i][j] = 0;
                    }
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

        public static void mostrarMatriz2(float[][] matriz,String nombre){
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
        public static void mostrarMatriz(float[][] matriz,String nombre,boolean f){
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
        
        public static float[][] partirMatriz(float[][] matriz,int num){
            int nMatriz = matriz.length;
            int inicio = ((num-1)*nMatriz)/6;
            int size = ((nMatriz*num)/(6));
            float[][] newMatriz = new float[nMatriz/6][nMatriz];
            for (int i = 0; i < nMatriz/6; i++){
                for (int j = 0; j < nMatriz; j++){
                    newMatriz[i][j] = matriz[inicio+i][j];
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
}
