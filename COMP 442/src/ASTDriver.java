import java.util.*;
import AST.*;

public class ASTDriver {
    public static void main(String[] args) {

        Parser p = new Parser();
        p.Parser();
        p.parse();
        //write ast
        p.writeAST();


        //call ast for traversal


    }
}
