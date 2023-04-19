import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.*;
import AST.*;
import Lexer.Token;
public class ASTDriver {
    public static void main(String[] args) {

        try {
//            FileInputStream fin = new FileInputStream("COMP 442/example-bubblesort.src");
//            FileInputStream fin = new FileInputStream("COMP 442/example-polynomial.src");
//            FileInputStream fin = new FileInputStream("COMP 442/test.txt");
            FileInputStream fin = new FileInputStream("COMP 442/projectInput/" + args[0]);
            String readingFile = args[0];
            int noExtensionNameIndex = readingFile.indexOf(".");
            String fileName = readingFile.substring(0,noExtensionNameIndex);

            PrintWriter pwError = new PrintWriter(new File("COMP 442/errors/"+fileName + ".errors"));
            PrintWriter pwTokens = new PrintWriter(new File("COMP 442/lexerOutput/"+fileName + ".outlextokens"));
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
                    rowCount = tok.getPosition().getLineNum();
                    pwTokens.write("\n"+tok);

                }
            }
            lex.getNextToken();
            pwTokens.close();
            Parser p = new Parser();
            p.Parser(readingFile, lex);
            p.parse(readingFile, lex);
            //write ast
            p.writeAST();


            //call ast for traversal
        }
        catch(Exception e){

        }


    }
}
