
public class MultiplicaMatriz_2 {
    static int N ;
    static float[][] A ;
    static float[][] B ;
    static float[][] C ;

    public static void main(String[] args){
        N = Integer.parseInt(args[0]);
        A = new float[N][N];
        B = new float[N][N];
        C = new float[N][N];
        // inicializa las matrices A y B
        for (int i = 0; i < N; i++){
            for (int j = 0; j < N; j++){
                A[i][j] = i + 3 * j;
                B[i][j] = 2 * i - j;
                C[i][j] = 0;
            }
        }
        // transpone la matriz B, la matriz traspuesta queda en B
        for (int i = 0; i < N; i++){
            for (int j = 0; j < i; j++){
                float x = B[i][j];
                B[i][j] = B[j][i];
                B[j][i] = x;
            }
        }
        // multiplica la matriz A y la matriz B, el resultado queda en la matriz C
        // notar que los indices de la matriz B se han intercambiado
        for (int i = 0; i < N; i++){
            for (int j = 0; j < N; j++){
                for (int k = 0; k < N; k++){
                    C[i][j] += A[i][k] * B[j][k];
                }
            }
        }
        if(N<=12){
            mostrarMatriz(A, "A");
            mostrarMatriz(B, "B");
            mostrarMatriz(C, "C");
        }
        checksum(C);
    }
    static void mostrarMatriz(float[][] matriz,String nombre){
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
    public static void checksum(float[][] matriz){
        float suma = 0;
        for (int x=0; x < matriz.length; x++) {
            for (int y=0; y < matriz[x].length; y++) {
                suma+= matriz[x][y];
            }
        }
        System.out.println("Checksum:"+suma);
    }
}
