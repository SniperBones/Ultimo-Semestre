import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClaseMatrizRMI extends UnicastRemoteObject implements InterfaceRMI{
    public ClaseMatrizRMI() throws RemoteException{
        super( );
    }
    public double[][] multiplica(double[][] A,double[][] B){
        int filasA = A.length;
        int filasB = B.length;
        double [][] auxC = new double[filasA][filasB];
        for (int i = 0; i < filasA; i++){
            for (int j = 0; j < filasB; j++){
                for (int k = 0; k < B[0].length; k++){
                    auxC[i][j] += A[i][k] * B[j][k];
                }
            }
        }
        return auxC;
    }
}
