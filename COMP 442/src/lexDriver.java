import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;

public class lexDriver {
    public static void main(String args[]) {
        try {
//            FileInputStream fin = new FileInputStream("COMP 442/example-bubblesort.src");
//            FileInputStream fin = new FileInputStream("COMP 442/example-polynomial.src");
//            FileInputStream fin = new FileInputStream("COMP 442/test.txt");
            FileInputStream fin = new FileInputStream("COMP 442/" + args[0]);
            String readingFile = args[0];
            int noExtensionNameIndex = readingFile.indexOf(".");
            String fileName = readingFile.substring(0,noExtensionNameIndex);

            PrintWriter pwError = new PrintWriter(new File("COMP 442/"+fileName + ".outlexerrors"));
            PrintWriter pwTokens = new PrintWriter(new File("COMP 442/"+fileName + ".outlextokens"));
            Token tok;
            int rowCount = 1;
            boolean firstLine = true;
            Lexer lex = new Lexer(fin, pwError);
            //printing tokens
            while ((tok = lex.getNextToken()) != null) {
                if (firstLine == true) {
//                    System.out.print(tok);
                    pwTokens.write(""+tok);
                    firstLine = false;
                } else if (rowCount == tok.getPosition().getLineNum()) {
//                    System.out.print(" " + tok);
                    pwTokens.write(" "+tok);
                } else {
//                    System.out.print("\n" + tok);
                    pwTokens.write("\n"+tok);
                    rowCount++;
                }
            }
            lex.getNextToken();
            pwTokens.close();

        } catch (Exception e) {
            System.out.print(e);
        }

    }

}
