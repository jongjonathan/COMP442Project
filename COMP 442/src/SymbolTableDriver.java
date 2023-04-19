import AST.*;
import Visitor.*;
import Visitor.SemanticCheck.TypeCheckingVisitor;
import Visitor.SymbolTable.ComputeMemorySizeVisitor;
import Visitor.SymbolTable.SymbolTableCreationVisitor;
import Visitor.CodeGeneration.TagsBasedCodeGenerationVisitor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;
import Lexer.*;

public class SymbolTableDriver {
    public static void main(String[] args) {
        try{
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

            //AST stack
            Stack<AST> sym = p.semStack;


            //Create symbol table
            SymbolTableCreationVisitor symVisitor = new SymbolTableCreationVisitor();
            Stack<AST> astTables = symVisitor.createTables(sym);
            //created tables

            //Type checking
            TypeCheckingVisitor typeCheckingVisitor = new TypeCheckingVisitor("COMP 442/projectOutput/" + p.getFilename() + ".outsemanticerrors");
//        Stack<AST> astCheck = typeCheckingVisitor.semanticCheck(sym);
            astTables.firstElement().accept(typeCheckingVisitor);


            //compute mem size
            ComputeMemorySizeVisitor memorySizeVisitor = new ComputeMemorySizeVisitor();
            astTables.firstElement().accept(memorySizeVisitor);

            p.writeSymbolTable(astTables);

            //code generation
            TagsBasedCodeGenerationVisitor  CGVisitor    = new TagsBasedCodeGenerationVisitor("COMP 442/moonOutput/" + p.getFilename() + ".m");
            astTables.firstElement().accept(CGVisitor);






        }
        catch(Exception e){


        }


    }
}
