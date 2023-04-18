import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.Stack;

import AST.AST;
import Lexer.*;
import Visitor.CodeGeneration.TagsBasedCodeGenerationVisitor;
import Visitor.SemanticCheck.TypeCheckingVisitor;
import Visitor.SymbolTable.ComputeMemorySizeVisitor;
import Visitor.SymbolTable.SymbolTableCreationVisitor;

public class Main {
    public static void main(String args[]) {
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

            //parser

            Parser p = new Parser();
            p.Parser(args[0]);
            p.parse(args[0]);

            //AST generation
            p.writeAST();

            //
            Stack<AST> sym = p.semStack;


            //Create symbol table
            SymbolTableCreationVisitor symVisitor = new SymbolTableCreationVisitor();
            Stack<AST> astTables = symVisitor.createTables(sym);
            //created tables

            //Type checking
            TypeCheckingVisitor typeCheckingVisitor = new TypeCheckingVisitor("COMP 442/errors/" + fileName + ".errors");
//        Stack<AST> astCheck = typeCheckingVisitor.semanticCheck(sym);
            astTables.firstElement().accept(typeCheckingVisitor);


            //compute mem size
            ComputeMemorySizeVisitor memorySizeVisitor = new ComputeMemorySizeVisitor();
            astTables.firstElement().accept(memorySizeVisitor);

            p.writeSymbolTable(astTables);

            //code generation
            TagsBasedCodeGenerationVisitor CGVisitor    = new TagsBasedCodeGenerationVisitor("COMP 442/moonOutput/" + fileName + ".m");
            astTables.firstElement().accept(CGVisitor);





        } catch (Exception e) {
            System.out.print(e);
        }

    }

}
