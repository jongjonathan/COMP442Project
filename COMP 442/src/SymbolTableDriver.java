import AST.*;
import Visitor.*;
import Visitor.SemanticCheck.TypeCheckingVisitor;
import Visitor.SymbolTable.ComputeMemorySizeVisitor;
import Visitor.SymbolTable.SymbolTableCreationVisitor;
import Visitor.CodeGeneration.TagsBasedCodeGenerationVisitor;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class SymbolTableDriver {
    public static void main(String[] args) {
        Parser p = new Parser();
        p.Parser();
        p.parse();
        //write ast
        p.writeAST();

        //AST stack
        Stack<AST> sym = p.semStack;


        //Create symbol table
        SymbolTableCreationVisitor symVisitor = new SymbolTableCreationVisitor();
        Stack<AST> astTables = symVisitor.createTables(sym);
        //created tables

        //Type checking
        TypeCheckingVisitor typeCheckingVisitor = new TypeCheckingVisitor("COMP 442/inputOutput/" + p.getFilename() + ".outsemanticerrors");
//        Stack<AST> astCheck = typeCheckingVisitor.semanticCheck(sym);
        astTables.firstElement().accept(typeCheckingVisitor);


        ComputeMemorySizeVisitor memorySizeVisitor = new ComputeMemorySizeVisitor();
        astTables.firstElement().accept(memorySizeVisitor);

        p.writeSymbolTable(astTables);


        TagsBasedCodeGenerationVisitor  CGVisitor    = new TagsBasedCodeGenerationVisitor("COMP 442/inputOutput/" + p.getFilename() + ".m");
        astTables.firstElement().accept(CGVisitor);


        System.out.println("temp finish");





    }
}
