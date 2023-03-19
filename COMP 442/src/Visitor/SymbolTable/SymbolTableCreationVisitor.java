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
        String ftype = "";
        String fname = ((Token) p_node.getChildNodes().get(0).concept).getLexeme();
        SymTable localtable = new SymTable(1,fname, p_node.m_symtab);
        Vector<VarEntry> paramlist = new Vector<VarEntry>();

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
    public void visit(ParamsListNode p_node) {
        System.out.println("param");
        for (AST child : p_node.getChildNodes()) {
            String fname = ((Token) child.concept).getLexeme();
            child.m_symtab = p_node.m_symtab;
            p_node.m_symtabentry = new VarEntry("PARAM", "" + ((Token) child.concept).getLexeme(), fname, null);
            child.m_symtabentry = p_node.m_symtabentry;
            child.m_symtab.addEntry(p_node.m_symtabentry);
            child.accept(this);
        }
    }
    public void visit(IDNode p_node) {
        String fname = ((Token) p_node.concept).getLexeme();
        p_node.m_symtabentry = new VarEntry("ID", ""+((Token) p_node.concept).getTokenType(), fname,null);
        p_node.m_symtab.addEntry(p_node.m_symtabentry);
    };
    public void visit(StatBlockNode p_node) {
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildNodes() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }

    }
    public void visit(VarDeclNode p_node){
        String vartype = ((Token) p_node.getChildNodes().get(1).concept).getLexeme();
        String varid = ""+((Token)p_node.getChildNodes().get(0).concept).getLexeme();
        // loop over the list of dimension nodes and aggregate here
        Vector<Integer> dimlist = new Vector<Integer>();
        for (AST dim : p_node.getChildNodes().get(2).getChildNodes()){
            // parameter dimension
            Integer dimval = Integer.parseInt(((Token)dim.concept).getLexeme());
            dimlist.add(dimval);
        }
        // create the symbol table entry for this variable
        // it will be picked-up by another node above later
        p_node.m_symtabentry = new VarEntry("var", vartype, varid, dimlist);
        p_node.m_symtab.addEntry(p_node.m_symtabentry);
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
