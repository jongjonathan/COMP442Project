import java.io.FileInputStream;
import java.util.*;
import AST.*;

public class ASTDriver {
    public static void main(String[] args) {

        try {
            FileInputStream fin = new FileInputStream("COMP 442/projectInput/" + args[0]);
            String readingFile = args[0];


            Parser p = new Parser();
            p.Parser(readingFile);
            p.parse(readingFile);
            //write ast
            p.writeAST();


            //call ast for traversal
        }
        catch(Exception e){

        }


    }
}
