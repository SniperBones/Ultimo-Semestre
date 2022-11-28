import java.rmi.Naming;

public class ServidorRMI {
    public static void main(String[] args) throws Exception{
        String url = "rmi://localhost/matrices";
        ClaseMatrizRMI obj = new ClaseMatrizRMI();
        Naming.rebind(url, obj);
    }
}
