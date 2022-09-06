//
// Este automata acepta cadenas con infinito numeros de a pero que terminen en ab sin
// aceptar b en medio de la cadena
//
public class Automata2{
    static String cadena;
    static boolean siquiente = false;
    static int indice = 0;
    static int tam;
    static String ruta;
    public static void main(String[] args){
        cadena = args[0];
        tam=cadena.length()-1;
        System.out.println(cadena.substring(tam-1, tam+1));
        q0(indice);
    }
    static void q0(int i){
        if(cadena.charAt(i)=='a'){
            if(siquiente==false && i<tam){
                q0(i+1);
            }else if(siquiente==true && i<tam){
                siquiente=false;
                q1(i+1);
            }else if(siquiente==false && i==tam){
                siquiente=true;
                q0(i-1);
            }
        }else if(cadena.charAt(i)=='b'){
            if(siquiente==false && i!=0){
                siquiente=true;
                q0(i-1);
            }else if(siquiente==false && i==0){
                qerr();
            }
        }
    }
    static void q1(int i){
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
    static void agregarRuta(String estado,boolean agrQui){
        if(agrQui==true){
            ruta+=estado;
        }else{
            
        }
    }
}