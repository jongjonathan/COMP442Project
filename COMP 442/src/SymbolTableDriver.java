import AST.*;
import Visitor.*;
import Visitor.SymbolTable.SymbolTableCreationVisitor;

import java.util.Stack;

public class SymbolTableDriver {
    public static void main(String[] args) {

        Parser p = new Parser();
        p.Parser();
        p.parse();
        //write ast
        p.writeAST();

        Stack<AST> sym = p.semStack;

        SymbolTableCreationVisitor symVisitor = new SymbolTableCreationVisitor();
        Stack<AST> astTables = symVisitor.createTables(sym);

        System.out.println("finish");


    }
}
