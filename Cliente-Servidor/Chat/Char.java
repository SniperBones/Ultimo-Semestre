import java.io.Console;
public class Char{
    public static void main(String args[]) {
        Console console = System.console();
        if (console == null) {
            System.err.println("No console");
            System.exit(1);
        }
        String s = console.readLine("Enter string: ");
        System.out.println(s);
    }

}
