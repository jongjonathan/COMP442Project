package Visitor.SymbolTable;
import AST.*;
import SymbolTable.*;
import Visitor.*;
import java.io.*;
import java.lang.reflect.Member;
import java.util.*;
import Lexer.*;



public class SymbolTableCreationVisitor extends Visitor {

    public Integer m_tempVarNum     = 0;
    public String  m_outputfilename = new String();

    public SymbolTableCreationVisitor(){

    }
    public String getNewTempVarName(){
        m_tempVarNum++;
        return "t" + m_tempVarNum.toString();
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
//        System.out.println("prog");
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
//        System.out.println("classdecl");
    }

    public void visit(FuncDefNode p_node) {
        String ftype = "";
        String fname = ((Token) p_node.getChildNodes().get(0).concept).getLexeme();
        SymTable localtable = new SymTable(1,fname, p_node.m_symtab);
        String paramList = "";
        boolean returntypeneeded = false;
        int count =0;
        for (AST child : p_node.getChildNodes()) {
            if(child instanceof ParamsListNode){
                paramList = "(";
                for (AST secondChild : child.getChildNodes()) {
                    if( count%2 ==1) {
                        paramList += ((Token)secondChild.concept).getLexeme()+", ";
                    }
                   count++;
                }
                paramList = paramList.substring(0,paramList.length()-2)+")";
                returntypeneeded = true;
                continue;
            }
            if(returntypeneeded == true){
                returntypeneeded = false;
                paramList += " "+((Token)child.concept).getLexeme();
            }

        }

        p_node.m_symtabentry = new FuncEntry(ftype, fname, paramList, localtable);
        p_node.m_symtab.addEntry(p_node.m_symtabentry);
        p_node.m_symtab = localtable;
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildNodes() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
//        System.out.println("func def");
    }
    public void visit(ParamsListNode p_node) {
//        System.out.println("param");
        int count =0;
        for (AST child : p_node.getChildNodes()) {
            if( count%2 ==0){
                child.m_symtab = p_node.m_symtab;
                p_node.m_symtabentry = new VarEntry("PARAM", ""+((Token)p_node.getChildNodes().get(count+1).concept).getLexeme(),
                        "" + ((Token) child.concept).getLexeme(), null);
                child.m_symtabentry = p_node.m_symtabentry;
                child.m_symtab.addEntry(p_node.m_symtabentry);
                child.accept(this);

            }
            count++;

        }
    }
    public void visit(IDNode p_node) {
        String fname = ((Token) p_node.getChildNodes().get(0).concept).getLexeme();
        p_node.m_moonVarName = ((Token)p_node.getChildNodes().get(0).concept).getLexeme();
        p_node.m_symtabentry = new VarEntry("ID", ""+((Token) p_node.getChildNodes().get(0).concept).getTokenType(), p_node.m_moonVarName,null);
        p_node.m_symtab.addEntry(p_node.m_symtabentry);
    };
    public void visit(NumNode p_node) {
        p_node.m_moonVarName =this.getNewTempVarName();
        String fname = ((Token) p_node.getChildNodes().get(0).concept).getLexeme();
        p_node.m_symtabentry = new VarEntry("litval", ""+((Token) p_node.getChildNodes().get(0).concept).getTokenType(), p_node.m_moonVarName,null);
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
        for (AST child : p_node.getChildNodes() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
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
//        p_node.m_symtabentry = new VarEntry("var", vartype, varid, dimlist[0]);
        p_node.m_symtab.addEntry(p_node.m_symtabentry);
    }
    public void visit(FuncCallNode p_node) {
        //  public function evaluate: (x: float) => float;

        String ftype ="";
        String fname = ((Token) p_node.getChildNodes().get(1).concept).getLexeme();
        String visibility = ((Token) p_node.getChildNodes().get(0).concept).getLexeme();
        SymTable localtable = new SymTable(2,fname, p_node.m_symtab);
        String paramList = "";
        boolean returntypeneeded = false;
        for (AST child : p_node.getChildNodes()) {
            if(child instanceof ParamsListNode){
                paramList = "(";
                for (AST secondChild : child.getChildNodes()) {
                    paramList += ((Token)secondChild.concept).getLexeme()+", ";
                }
                paramList = paramList.substring(0,paramList.length()-2)+")";
                returntypeneeded = true;
                continue;
            }
            if(returntypeneeded == true){
                returntypeneeded = false;
                paramList += " "+((Token)child.concept).getLexeme();
            }

        }

        p_node.m_symtabentry = new FuncEntry(ftype, fname, paramList, localtable, visibility);
        p_node.m_symtab.addEntry(p_node.m_symtabentry);
        p_node.m_symtab = localtable;
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildNodes() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
//        System.out.println("func call");
    };
    public void visit(MemberVarDeclNode p_node){
        for (AST child : p_node.getChildNodes() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
        String vartype = ((Token) p_node.getChildNodes().get(2).concept).getLexeme();
        String varid = ""+((Token)p_node.getChildNodes().get(1).concept).getLexeme();
        String visibility = ((Token)p_node.getChildNodes().get(0).concept).getLexeme();
        // loop over the list of dimension nodes and aggregate here
        Vector<Integer> dimlist = new Vector<Integer>();
        for (AST dim : p_node.getChildNodes().get(3).getChildNodes()){
            // parameter dimension
            Integer dimval = Integer.parseInt(((Token)dim.concept).getLexeme());
            dimlist.add(dimval);
        }
        // create the symbol table entry for this variable
        // it will be picked-up by another node above later
        p_node.m_symtabentry = new VarEntry("var", vartype, varid, dimlist,visibility );
        p_node.m_symtab.addEntry(p_node.m_symtabentry);
    }
    public void visit(InheritNode p_node){
        for (AST child : p_node.getChildNodes() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
        String varid = "none";
        if(p_node.getChildNodes().size()!=0){
            varid = ""+((Token)p_node.getChildNodes().get(0).concept).getLexeme();
        }

        // loop over the list of dimension nodes and aggregate here
        Vector<Integer> dimlist = new Vector<Integer>();
//        for (AST dim : p_node.getChildNodes().get(2).getChildNodes()){
//            // parameter dimension
//            Integer dimval = Integer.parseInt(((Token)dim.concept).getLexeme());
//            dimlist.add(dimval);
//        }
        // create the symbol table entry for this variable
        // it will be picked-up by another node above later
        p_node.m_symtabentry = new InheritEntry("inherit", null,varid,null);
        p_node.m_symtab.addEntry(p_node.m_symtabentry);
    }

    @Override
    public void visit(AST p_node) {

    }
    public void visit(ArrSizeNode    p_node){
        for (AST child : p_node.getChildNodes() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }

    }
    public void visit(AssignOpNode    p_node){
        for (AST child : p_node.getChildNodes() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }
    public void visit(AddOpNode    p_node){
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildNodes()) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
        String tempvarname = this.getNewTempVarName();
        p_node.m_moonVarName = tempvarname;
        p_node.m_symtabentry = new VarEntry("tempvar", ((Token)p_node.getChildNodes().get(1).getChildNodes().get(0).concept).getTokenType().toString().toLowerCase(), p_node.m_moonVarName, null);
        p_node.m_symtab.addEntry(p_node.m_symtabentry);

    }
    public void visit(MultOpNode    p_node){
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildNodes()) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
        String tempvarname = this.getNewTempVarName();
        p_node.m_moonVarName = tempvarname;
        p_node.m_symtabentry = new VarEntry("tempvar", ((Token)p_node.getChildNodes().get(1).getChildNodes().get(0).concept).getTokenType().toString().toLowerCase(), p_node.m_moonVarName, null);
        p_node.m_symtab.addEntry(p_node.m_symtabentry);

    }
    public void visit(ArithmNode    p_node){
        for (AST child : p_node.getChildNodes() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
        p_node.m_moonVarName = p_node.getChildNodes().get(0).m_moonVarName;
    }
    public void visit(ExprNode    p_node){
        for (AST child : p_node.getChildNodes() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
        p_node.m_moonVarName = p_node.getChildNodes().get(0).m_moonVarName;
    }
    public void visit(WriteNode    p_node){
        for (AST child : p_node.getChildNodes() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }
    public void visit(IndiceNode    p_node){
        for (AST child : p_node.getChildNodes() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }
    public void visit(StatNode    p_node){
        for (AST child : p_node.getChildNodes() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }
    public void visit(IfNode    p_node){
        for (AST child : p_node.getChildNodes() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }
    public void visit(RelExprNode    p_node){
        for (AST child : p_node.getChildNodes() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }
    public void visit(ReadNode    p_node){
        for (AST child : p_node.getChildNodes() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    public Stack<AST> createTables(Stack<AST> nodeStack){

        SymbolTableCreationVisitor stv = new SymbolTableCreationVisitor();
        nodeStack.firstElement().accept(stv);

        return nodeStack;
    }
}
