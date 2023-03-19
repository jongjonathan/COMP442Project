import AST.*;
import Visitor.*;
import Visitor.SymbolTable.SymbolTableCreationVisitor;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class SymbolTableDriver {
    public static void main(String[] args) {
        FileWriter f;
        Parser p = new Parser();
        p.Parser();
        p.parse();
        //write ast
        p.writeAST();

        Stack<AST> sym = p.semStack;

        SymbolTableCreationVisitor symVisitor = new SymbolTableCreationVisitor();
        Stack<AST> astTables = symVisitor.createTables(sym);

        p.writeSymbolTable(astTables);

        System.out.println("finish");


    }
}
