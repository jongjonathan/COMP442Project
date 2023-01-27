import java.io.FileInputStream;

public class LexerDriver {
    public static void main(String args[]) {
        try {
//            FileInputStream fin = new FileInputStream("/Users/jonathanjong/Developer/COMP 442 Project/COMP 442/example-bubblesort.src");
            FileInputStream fin = new FileInputStream("/Users/jonathanjong/Developer/COMP 442 Project/COMP 442/test.txt");
            Lexer lex = new Lexer(fin);

        } catch (Exception e) {
            System.out.println(e);
        }

    }

}
