package Visitor.SymbolTable;
import AST.*;
import SymbolTable.*;
import Visitor.*;
import java.io.*;
import java.util.*;
import Lexer.*;



public class SymbolTableCreationVisitor extends Visitor {

    public Integer m_tempVarNum     = 0;
    public String  m_outputfilename = new String();

    public SymbolTableCreationVisitor(){

    }

    public void visit(ProgNode p_node){
        p_node.m_symtab = new SymTable(0,"global", null);
        // propagate accepting tohe same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildNodes() ) {
            //make all children use this scopes' symbol table
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
        if (!this.m_outputfilename.isEmpty()) {
            File file = new File(this.m_outputfilename);
            try (PrintWriter out = new PrintWriter(file)){
                out.println(p_node.m_symtab);
            }
            catch(Exception e){
                e.printStackTrace();}
        }
        System.out.println("prog");
//        for (AST child : p_node.getChildNodes()) {
//            child.accept(this);
//        }
    }

    public void visit(ClassNode p_node) {
        String classname = ((Token) p_node.getChildNodes().get(0).concept).getLexeme();
        SymTable localtable = new SymTable(1,classname, p_node.m_symtab);
        p_node.m_symtabentry = new ClassEntry(classname, localtable);
        p_node.m_symtab.addEntry(p_node.m_symtabentry);
        p_node.m_symtab = localtable;
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildNodes() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
        System.out.println("classdecl");
    }

    public void visit(FuncDefNode p_node) {
        String ftype = "FUNCTION";
        String fname = ((Token) p_node.getChildNodes().get(0).concept).getLexeme();
        SymTable localtable = new SymTable(1,fname, p_node.m_symtab);
        Vector<VarEntry> paramlist = new Vector<VarEntry>();
        if ( p_node.getChildNodes().get(1).concept.equals("FPARAMS")){
            for (AST param : p_node.getChildNodes().get(1).getChildNodes()){
                paramlist.add((VarEntry) p_node.m_symtabentry);
            }
        }

        p_node.m_symtabentry = new FuncEntry(ftype, fname, paramlist, localtable);
        p_node.m_symtab.addEntry(p_node.m_symtabentry);
        p_node.m_symtab = localtable;
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildNodes() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
        System.out.println("func def");
    }

    @Override
    public void visit(AST p_node) {

    }

    public Stack<AST> createTables(Stack<AST> nodeStack){

        SymbolTableCreationVisitor stv = new SymbolTableCreationVisitor();
        nodeStack.firstElement().accept(stv);

        return nodeStack;
    }
}
