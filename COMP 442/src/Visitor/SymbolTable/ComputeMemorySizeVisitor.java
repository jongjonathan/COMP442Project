package Visitor.SymbolTable;

import java.io.File;
import java.io.PrintWriter;
import java.util.Vector;

import AST.*;
import Lexer.Token;
import SymbolTable.*;
import Visitor.Visitor;

/**
 */

public class ComputeMemorySizeVisitor extends Visitor {

    public String  m_outputfilename = new String();

    public ComputeMemorySizeVisitor() {
    }

    public ComputeMemorySizeVisitor(String p_filename) {
        this.m_outputfilename = p_filename;
    }

    public int sizeOfEntry(AST p_node) {
        int size = 0;
        if(p_node.m_symtabentry.m_type.equals("integer") || p_node.m_symtabentry.m_type.equals("int"))
            size = 4;
        else if(p_node.m_symtabentry.m_type.equals("float"))
            size = 8;
        else if(p_node.m_symtabentry.m_type.equals("id"))
            size = 4;
        // if it is an array, multiply by all dimension sizes
        VarEntry ve = (VarEntry) p_node.m_symtabentry;
        if(!ve.m_dims.isEmpty())
            for(Integer dim : ve.m_dims)
                size *= dim;
        return size;
    }

    public int sizeOfTypeNode(AST p_node) {
        int size = 0;
        if(p_node.concept == "")
            size = 4;
        else if(p_node.concept == "float")
            size = 8;
        return size;
    }


    public void visit(ProgNode p_node){
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildNodes() )
            child.accept(this);
//        // compute total size and offsets along the way
//        // this should be node on all nodes that represent
//        // a scope and contain their own table
//        for (SymTableEntry entry : p_node.m_symtab.m_symlist){
////            entry.m_offset     = p_node.m_symtab.m_size - entry.m_size;
//            p_node.m_symtab.m_size -= entry.m_size;
//        }
    };

    public void visit(ClassNode p_node){
        for (AST child : p_node.getChildNodes() )
            child.accept(this);
//        // propagate accepting the same visitor to all the children
//        // this effectively achieves Depth-First AST Traversal
//        for (AST child : p_node.getChildNodes() )
//            child.accept(this);
//        // compute total size and offsets along the way
//        // this should be node on all nodes that represent
//        // a scope and contain their own table
//        for (SymTableEntry entry : p_node.m_symtab.m_symlist){
////            entry.m_offset = p_node.m_symtab.m_size - entry.m_size;
//            p_node.m_symtab.m_size -= entry.m_size;
//        }
    };

    public void visit(FuncDefNode p_node){
        for (AST child : p_node.getChildNodes() )
            child.accept(this);
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
//        for (AST child : p_node.getChildNodes() )
//            child.accept(this);
//        // compute total size and offsets along the way
//        // this should be node on all nodes that represent
//        // a scope and contain their own table
//        // stack frame contains the return value at the bottom of the stack
//        p_node.m_symtab.m_size = -(this.sizeOfTypeNode(p_node.getChildNodes().get()).get(0)));
//        //then is the return addess is stored on the stack frame
//        p_node.m_symtab.m_size -= 4;
//        for (SymTableEntry entry : p_node.m_symtab.m_symlist){
////            entry.m_offset = p_node.m_symtab.m_size - entry.m_size;
//            p_node.m_symtab.m_size -= entry.m_size;
//        }
    };

    public void visit(VarDeclNode p_node){
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildNodes() )
            child.accept(this);
//        // determine the size for basic variables
        p_node.m_symtabentry.m_size = this.sizeOfEntry(p_node);
    }


    public void visit(StatBlockNode p_node){
        for (AST child : p_node.getChildNodes() )
            child.accept(this);
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
//        for (AST child : p_node.getChildNodes() )
//            child.accept(this);
    };


    public void visit(FuncCallNode p_node) {
        for (AST child : p_node.getChildNodes() )
            child.accept(this);
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
//        for (AST child : p_node.getChildNodes() )
//            child.accept(this);
//        p_node.m_symtabentry.m_size = this.sizeOfEntry(p_node);
    };
    public void visit(ParamsListNode p_node) {
        for (AST child : p_node.getChildNodes() )
            child.accept(this);
////        System.out.println("param");
//        int count =0;
//        for (AST child : p_node.getChildNodes()) {
//            if( count%2 ==0){
//                child.m_symtab = p_node.m_symtab;
//                p_node.m_symtabentry = new VarEntry("PARAM", ""+((Token)p_node.getChildNodes().get(count+1).concept).getLexeme(),
//                        "" + ((Token) child.concept).getLexeme(), null);
//                child.m_symtabentry = p_node.m_symtabentry;
//                child.m_symtab.addEntry(p_node.m_symtabentry);
//                child.accept(this);
//
//            }
//            count++;
//
//        }
    }
    public void visit(IDNode p_node) {
        for (AST child : p_node.getChildNodes() )
            child.accept(this);
//        String fname = ((Token) p_node.concept).getLexeme();
//        p_node.m_symtabentry = new VarEntry("ID", ""+((Token) p_node.concept).getTokenType(), fname,null);
//        p_node.m_symtab.addEntry(p_node.m_symtabentry);
    };
    public void visit(MemberVarDeclNode p_node){
        for (AST child : p_node.getChildNodes() )
            child.accept(this);
//        String vartype = ((Token) p_node.getChildNodes().get(2).concept).getLexeme();
//        String varid = ""+((Token)p_node.getChildNodes().get(1).concept).getLexeme();
//        String visibility = ((Token)p_node.getChildNodes().get(0).concept).getLexeme();
//        // loop over the list of dimension nodes and aggregate here
//        Vector<Integer> dimlist = new Vector<Integer>();
////        for (AST dim : p_node.getChildNodes().get(2).getChildNodes()){
////            // parameter dimension
////            Integer dimval = Integer.parseInt(((Token)dim.concept).getLexeme());
////            dimlist.add(dimval);
////        }
//        // create the symbol table entry for this variable
//        // it will be picked-up by another node above later
//        p_node.m_symtabentry = new VarEntry("var", vartype, varid, dimlist,visibility );
//        p_node.m_symtab.addEntry(p_node.m_symtabentry);
        p_node.m_symtabentry.m_size = this.sizeOfEntry(p_node);
    }
    public void visit(InheritNode p_node){
        for (AST child : p_node.getChildNodes() )
            child.accept(this);
//        String varid = "none";
//        if(p_node.getChildNodes().size()!=0){
//            varid = ""+((Token)p_node.getChildNodes().get(0).concept).getLexeme();
//        }
//
//        // loop over the list of dimension nodes and aggregate here
//        Vector<Integer> dimlist = new Vector<Integer>();
////        for (AST dim : p_node.getChildNodes().get(2).getChildNodes()){
////            // parameter dimension
////            Integer dimval = Integer.parseInt(((Token)dim.concept).getLexeme());
////            dimlist.add(dimval);
////        }
//        // create the symbol table entry for this variable
//        // it will be picked-up by another node above later
//        p_node.m_symtabentry = new InheritEntry("inherit", null,varid,null);
//        p_node.m_symtab.addEntry(p_node.m_symtabentry);
    }
    public void visit(AST   p_node){

    };
    public void visit(ArrSizeNode    p_node){
        for (AST child : p_node.getChildNodes()) {
            child.accept(this);
        }

    }
    public void visit(AssignOpNode    p_node){
        for (AST child : p_node.getChildNodes()) {
            child.accept(this);
        }

    }
    public void visit(AddOpNode    p_node){
        for (AST child : p_node.getChildNodes()) {
            child.accept(this);
        }

    }
    public void visit(MultOpNode    p_node){
        for (AST child : p_node.getChildNodes()) {
            child.accept(this);
        }

    }
    public void visit(ArithmNode    p_node){
        for (AST child : p_node.getChildNodes()) {
            child.accept(this);
        }

    }
    public void visit(ExprNode    p_node){
        for (AST child : p_node.getChildNodes()) {
            child.accept(this);
        }

    }
    public void visit(WriteNode    p_node){
        for (AST child : p_node.getChildNodes() ) {
            child.accept(this);
        }
    }
    public void visit(NumNode    p_node){
        for (AST child : p_node.getChildNodes() ) {
            child.accept(this);
        }
    }
    public void visit(IndiceNode    p_node){
        for (AST child : p_node.getChildNodes() ) {
            child.accept(this);
        }
    }
    public void visit(StatNode    p_node){
        for (AST child : p_node.getChildNodes() ) {
            child.accept(this);
        }
    }

}
