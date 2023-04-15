import AST.*;
import Visitor.*;
import Visitor.SemanticCheck.TypeCheckingVisitor;
import Visitor.SymbolTable.ComputeMemorySizeVisitor;
import Visitor.SymbolTable.SymbolTableCreationVisitor;
import Visitor.CodeGeneration.TagsBasedCodeGenerationVisitor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.*;

public class SymbolTableDriver {
    public static void main(String[] args) {
        try{
            FileInputStream fin = new FileInputStream("COMP 442/projectInput/" + args[0]);
            String readingFile = args[0];
            Parser p = new Parser();
            p.Parser(readingFile);
            p.parse(readingFile);
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


            //compute mem size
            ComputeMemorySizeVisitor memorySizeVisitor = new ComputeMemorySizeVisitor();
            astTables.firstElement().accept(memorySizeVisitor);

            p.writeSymbolTable(astTables);

            //code generation
            TagsBasedCodeGenerationVisitor  CGVisitor    = new TagsBasedCodeGenerationVisitor("COMP 442/inputOutput/" + p.getFilename() + ".m");
            astTables.firstElement().accept(CGVisitor);






        }
        catch(Exception e){


        }


    }
}
