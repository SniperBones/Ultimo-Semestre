//
// Este automata acepta cadenas con infinito numeros de a pero que terminen en ab sin
// aceptar b en medio de la cadena
//
public class Automata{
    static String cadena;
    static boolean siquiente = false;
    static int indice = 0;
    public static void main(String[] args){
        cadena = args[0];
        q0(indice,cadena.length()-1);
    }
    static void q0(int i,int tam){
        if(cadena.charAt(i)=='a' && siquiente==false && i<tam){
            q0(i+1,tam);
        }
        if(cadena.charAt(i)=='b' && siquiente==false && i!=0){
            siquiente=true;
            q0(i-1,tam);
        }
        if(cadena.charAt(i)=='a' && siquiente==true && i<tam){
            siquiente=false;
            q1(i+1,tam);
        }
        if(cadena.charAt(i)=='a' && siquiente==false && i==tam){
            siquiente=true;
            q0(i-1,tam);
        }
        if(cadena.charAt(i)=='a' && siquiente==true && i<tam){
            siquiente=false;
            q1(i+1,tam);
        }
        if(cadena.charAt(i)=='b' && siquiente==false && i==0){
            qerr();
        }

    }
    static void q1(int i,int tam){
        if(cadena.charAt(i)=='b' && siquiente==false && i==tam){
            q2();
        }else{
            qerr();
        }
    }
    static void q2(){
        System.out.println("Cadena Valida");
    }
    static void qerr(){
        System.out.println("Cadena no valida");
    }
}