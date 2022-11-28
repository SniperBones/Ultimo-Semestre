import java.rmi.RemoteException;
import java.rmi.Remote;

public interface InterfaceRMI extends Remote{
    public double[][] multiplica(double[][] A,double[][] B) throws RemoteException;
}
