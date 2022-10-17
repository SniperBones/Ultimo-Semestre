import java.io.FileReader;
import java.io.IOException;


public class prueba {
    
    public static void main(String []args) throws IOException{
        System.out.println(lee(args[0]));
    }
    public static String lee(String Archivo) {
        String texto="";
        try {
            FileReader entrada=new FileReader("CF/"+Archivo);

                int c=0;

                while(c!=-1) {
                    c=entrada.read();

                    char letra=(char)c;

                    texto+=letra;
                }

                entrada.close();

                System.out.println(texto);

        } catch (IOException e) {

            System.out.println("No se ha encontrado el archivo");
        }
        return texto;
    }
}
