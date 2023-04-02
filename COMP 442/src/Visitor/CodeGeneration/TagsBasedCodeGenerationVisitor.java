package Visitor.CodeGeneration;

import java.io.File;
import java.io.PrintWriter;
import java.util.Stack;
import java.util.Vector;

import AST.*;
import Lexer.Token;
import SymbolTable.*;
import Visitor.Visitor;


public class TagsBasedCodeGenerationVisitor extends Visitor {
    public Stack<String> m_registerPool = new Stack<String>();
    public Integer m_tempVarNum = 0;
    public String m_moonExecCode = new String();               // moon code instructions part
    public String m_moonDataCode = new String();               // moon code data part
    public String m_mooncodeindent = new String("           ");
    public String m_outputfilename = new String();
    int indexRestart = 0;

    public TagsBasedCodeGenerationVisitor() {
        // create a pool of registers as a stack of Strings
        // assuming only r1, ..., r12 are available
        for (Integer i = 12; i >= 1; i--)
            m_registerPool.push("r" + i.toString());
    }

    public TagsBasedCodeGenerationVisitor(String p_filename) {
        this.m_outputfilename = p_filename;
        // create a pool of registers as a stack of Strings
        // assuming only r1, ..., r15 are available
        for (Integer i = 12; i >= 1; i--)
            m_registerPool.push("r" + i.toString());
    }


    public void visit(ProgNode p_node) {
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal	// generate moon program's entry point
        m_moonExecCode += m_mooncodeindent + "entry\n";
        m_moonExecCode += m_mooncodeindent + "addi r14,r0,topaddr\n";
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildNodes())
            child.accept(this);
        // generate moon program's end point
        m_moonDataCode += m_mooncodeindent + "% buffer space used for console output\n";
        m_moonDataCode += String.format("%-11s", "buf") + "res 20\n";
        m_moonExecCode += m_mooncodeindent + "hlt\n";

        if (!this.m_outputfilename.isEmpty()) {
            File file = new File(this.m_outputfilename);
            try (PrintWriter out = new PrintWriter(file)) {
                out.println(this.m_moonExecCode);
                out.println(this.m_moonDataCode);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    ;

    public void visit(ClassNode p_node) {
        String classname = ((Token) p_node.getChildNodes().get(0).concept).getLexeme();
        SymTable localtable = new SymTable(1, classname, p_node.m_symtab);
        p_node.m_symtabentry = new ClassEntry(classname, localtable);
        p_node.m_symtab.addEntry(p_node.m_symtabentry);
        p_node.m_symtab = localtable;
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildNodes()) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
//        System.out.println("classdecl");
    }

    public void visit(FuncDefNode p_node) {
        String ftype = "";
        String fname = ((Token) p_node.getChildNodes().get(0).concept).getLexeme();
        SymTable localtable = new SymTable(1, fname, p_node.m_symtab);
        String paramList = "";
        boolean returntypeneeded = false;
        int count = 0;
        for (AST child : p_node.getChildNodes()) {
            if (child instanceof ParamsListNode) {
                paramList = "(";
                for (AST secondChild : child.getChildNodes()) {
                    if (count % 2 == 1) {
                        paramList += ((Token) secondChild.concept).getLexeme() + ", ";
                    }
                    count++;
                }
                paramList = paramList.substring(0, paramList.length() - 2) + ")";
                returntypeneeded = true;
                continue;
            }
            if (returntypeneeded == true) {
                returntypeneeded = false;
                paramList += " " + ((Token) child.concept).getLexeme();
            }

        }

        p_node.m_symtabentry = new FuncEntry(ftype, fname, paramList, localtable);
        p_node.m_symtab.addEntry(p_node.m_symtabentry);
        p_node.m_symtab = localtable;
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildNodes()) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
//        System.out.println("func def");
    }

    public void visit(ParamsListNode p_node) {
//        System.out.println("param");
        int count = 0;
        for (AST child : p_node.getChildNodes()) {
            if (count % 2 == 0) {
                child.m_symtab = p_node.m_symtab;
                p_node.m_symtabentry = new VarEntry("PARAM", "" + ((Token) p_node.getChildNodes().get(count + 1).concept).getLexeme(),
                        "" + ((Token) child.concept).getLexeme(), null);
                child.m_symtabentry = p_node.m_symtabentry;
                child.m_symtab.addEntry(p_node.m_symtabentry);
                child.accept(this);

            }
            count++;

        }
    }

    public void visit(IDNode p_node) {
        String fname = ((Token) p_node.concept).getLexeme();
        p_node.m_symtabentry = new VarEntry("ID", "" + ((Token) p_node.concept).getTokenType(), fname, null);
        p_node.m_symtab.addEntry(p_node.m_symtabentry);
    }

    ;

    public void visit(StatBlockNode p_node) {
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildNodes()) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }

    }

    public void visit(VarDeclNode p_node) {
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildNodes() )
            child.accept(this);
        // Then, do the processing of this nodes' visitor
        if (((Token)p_node.getChildNodes().get(1).concept).getLexeme().equals("int") || ((Token)p_node.getChildNodes().get(1).concept).getLexeme().equals("integer")){
            m_moonDataCode += m_mooncodeindent + "% space for variable " + ((Token)p_node.getChildNodes().get(0).concept).getLexeme() + "\n";
            m_moonDataCode += ((Token)p_node.getChildNodes().get(0).concept).getLexeme() + " res 4\n";
        }


    }

    public void visit(FuncCallNode p_node) {
        //  public function evaluate: (x: float) => float;

        String ftype = "";
        String fname = ((Token) p_node.getChildNodes().get(1).concept).getLexeme();
        String visibility = ((Token) p_node.getChildNodes().get(0).concept).getLexeme();
        SymTable localtable = new SymTable(2, fname, p_node.m_symtab);
        String paramList = "";
        boolean returntypeneeded = false;
        for (AST child : p_node.getChildNodes()) {
            if (child instanceof ParamsListNode) {
                paramList = "(";
                for (AST secondChild : child.getChildNodes()) {
                    paramList += ((Token) secondChild.concept).getLexeme() + ", ";
                }
                paramList = paramList.substring(0, paramList.length() - 2) + ")";
                returntypeneeded = true;
                continue;
            }
            if (returntypeneeded == true) {
                returntypeneeded = false;
                paramList += " " + ((Token) child.concept).getLexeme();
            }

        }

        p_node.m_symtabentry = new FuncEntry(ftype, fname, paramList, localtable, visibility);
        p_node.m_symtab.addEntry(p_node.m_symtabentry);
        p_node.m_symtab = localtable;
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildNodes()) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
//        System.out.println("func call");
    }

    ;

    public void visit(MemberVarDeclNode p_node) {
        String vartype = ((Token) p_node.getChildNodes().get(2).concept).getLexeme();
        String varid = "" + ((Token) p_node.getChildNodes().get(1).concept).getLexeme();
        String visibility = ((Token) p_node.getChildNodes().get(0).concept).getLexeme();
        // loop over the list of dimension nodes and aggregate here
        Vector<Integer> dimlist = new Vector<Integer>();
//        for (AST dim : p_node.getChildNodes().get(2).getChildNodes()){
//            // parameter dimension
//            Integer dimval = Integer.parseInt(((Token)dim.concept).getLexeme());
//            dimlist.add(dimval);
//        }
        // create the symbol table entry for this variable
        // it will be picked-up by another node above later
        p_node.m_symtabentry = new VarEntry("var", vartype, varid, dimlist, visibility);
        p_node.m_symtab.addEntry(p_node.m_symtabentry);
    }

    public void visit(InheritNode p_node) {
        String varid = "none";
        if (p_node.getChildNodes().size() != 0) {
            varid = "" + ((Token) p_node.getChildNodes().get(0).concept).getLexeme();
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
        p_node.m_symtabentry = new InheritEntry("inherit", null, varid, null);
        p_node.m_symtab.addEntry(p_node.m_symtabentry);
    }

    public void visit(AST p_node) {

    }


    public void visit(ArrSizeNode p_node) {
        for (AST child : p_node.getChildNodes()) {
            child.accept(this);
        }

    }
    public void visit(AssignOpNode    p_node){
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildNodes() )
            child.accept(this);
        // Then, do the processing of this nodes' visitor
        // allocate local register
        String localRegister = this.m_registerPool.pop();
        //generate code
        int indexBefore = -1;

        String indexBeforeName = "";
        //before name
        for (AST child : p_node.parentNode.getChildNodes()) {
            indexBefore ++;
            if(child instanceof AssignOpNode && child == p_node){
                indexBeforeName = ((Token)p_node.parentNode.getChildNodes().get(indexBefore-1).concept).getLexeme();
                indexRestart = indexBefore;
                break;

            }
        }
        String tempVarOp = p_node.getChildNodes().get(1).getChildNodes().get(0).getChildNodes().get(1).m_moonVarName;
//((Token)p_node.getChildNodes().get(1).getChildNodes().get(0).getChildNodes().get(0).concept).getLexeme()
       // ((Token)p_node.getChildNodes().get(1).getChildNodes().get(0).getChildNodes().get(0).concept).getLexeme()
        m_moonExecCode += m_mooncodeindent + "% processing: "  + indexBeforeName+ " := " +  tempVarOp+ "\n";
        m_moonExecCode += m_mooncodeindent + "lw " + localRegister + "," +tempVarOp+ "(r0)\n";
        m_moonExecCode += m_mooncodeindent + "sw " + indexBeforeName + "(r0)," + localRegister + "\n";
        //deallocate local register
        this.m_registerPool.push(localRegister);

    }
    public void visit(AddOpNode    p_node){
// propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildNodes() )
            child.accept(this);
        // Then, do the processing of this nodes' visitor
        // create a local variable and allocate a register to this subcomputation
        String localRegister      = this.m_registerPool.pop();
        String leftChildRegister  = this.m_registerPool.pop();
        String rightChildRegister = this.m_registerPool.pop();
        // generate code
        int indexBefore = -1;
        String indexBeforeName = "";
        //before name of assign
        for (AST child : p_node.parentNode.parentNode.parentNode.parentNode.getChildNodes()) {
            indexBefore ++;
            if(child instanceof AssignOpNode){
                //ex: n from n = 1 + 1
                indexBeforeName = ((Token)p_node.parentNode.parentNode.parentNode.parentNode.getChildNodes().get(indexBefore-1).concept).getLexeme();

            }
        }
        String left = ((Token)p_node.parentNode.getChildNodes().get(0).concept).getLexeme();
        String right = ((Token)p_node.getChildNodes().get(1).concept).getLexeme();

        m_moonExecCode += m_mooncodeindent + "% processing: " + p_node.m_moonVarName + " := " + left + " + " + right + "\n";
        m_moonExecCode += m_mooncodeindent + "lw "  + leftChildRegister +  "," + left + "(r0)\n";
        m_moonExecCode += m_mooncodeindent + "lw "  + rightChildRegister + "," + right + "(r0)\n";
        m_moonExecCode += m_mooncodeindent + "add " + localRegister +      "," + leftChildRegister + "," + rightChildRegister + "\n";
        m_moonDataCode += m_mooncodeindent + "% space for " + left + " + " + right + "\n";
        m_moonDataCode += String.format("%-10s",p_node.m_moonVarName) + " res 4\n";
        m_moonExecCode += m_mooncodeindent + "sw " + p_node.m_moonVarName + "(r0)," + localRegister + "\n";
        // deallocate the registers for the two children, and the current node
        this.m_registerPool.push(leftChildRegister);
        this.m_registerPool.push(rightChildRegister);
        this.m_registerPool.push(localRegister);

    }
    public void visit(MultOpNode    p_node){
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildNodes() )
            child.accept(this);
        // Then, do the processing of this nodes' visitor
        // create a local variable and allocate a register to this subcomputation
        String localRegister      = this.m_registerPool.pop();
        String leftChildRegister  = this.m_registerPool.pop();
        String rightChildRegister = this.m_registerPool.pop();
        int indexBefore = -1;
        String indexBeforeName = "";
        //before name of assign
        for (AST child : p_node.parentNode.parentNode.parentNode.parentNode.getChildNodes()) {
            indexBefore ++;
            if(child instanceof AssignOpNode){
                //ex: n from n = 1 + 1
                indexBeforeName = ((Token)p_node.parentNode.parentNode.parentNode.parentNode.getChildNodes().get(indexBefore-1).concept).getLexeme();

            }
        }
        String left = ((Token)p_node.parentNode.getChildNodes().get(0).concept).getLexeme();
        String right = ((Token)p_node.getChildNodes().get(1).concept).getLexeme();
        // generate code
        m_moonExecCode += m_mooncodeindent + "% processing: " + p_node.m_moonVarName + " := " + left + " * " + right + "\n";
        m_moonExecCode += m_mooncodeindent + "lw "  + leftChildRegister  + "," + left + "(r0)\n";
        m_moonExecCode += m_mooncodeindent + "lw "  + rightChildRegister + "," + right + "(r0)\n";
        m_moonExecCode += m_mooncodeindent + "mul " + localRegister      + "," + leftChildRegister + "," + rightChildRegister + "\n";
        m_moonDataCode += m_mooncodeindent + "% space for " + left + " * " + right + "\n";
        m_moonDataCode += String.format("%-10s",p_node.m_moonVarName) + " res 4\n";
        m_moonExecCode += m_mooncodeindent + "sw " + p_node.m_moonVarName + "(r0)," + localRegister + "\n";
        // deallocate the registers for the two children, and the current node
        this.m_registerPool.push(leftChildRegister);
        this.m_registerPool.push(rightChildRegister);
        this.m_registerPool.push(localRegister);
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
}
