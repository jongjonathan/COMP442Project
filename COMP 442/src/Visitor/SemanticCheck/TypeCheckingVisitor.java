package Visitor.SemanticCheck;
import AST.*;
import Lexer.*;
import Visitor.SymbolTable.SymbolTableCreationVisitor;
import Visitor.Visitor;
import java.io.File;
import java.io.PrintWriter;
import java.util.Stack;

public class TypeCheckingVisitor extends Visitor{
    public String m_outputfilename = new String();
    public String m_errors         = "";

    public TypeCheckingVisitor() {

    }
    public TypeCheckingVisitor(String p_filename) {
        this.m_outputfilename = p_filename;
    }
    public void visit(ProgNode p_node) {
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildNodes()) {
            child.accept(this);
        }
            if (!this.m_outputfilename.isEmpty()) {
                File file = new File(this.m_outputfilename);
                try {
                    PrintWriter out = new PrintWriter(file);
                    out.write(this.m_errors);
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

    }
    public void visit(ClassNode p_node){
        for (AST child : p_node.getChildNodes()) {
            child.accept(this);
        }
    }
    //    public abstract void visit(DimListNode      p_node);
//    public abstract void visit(DimNode          p_node);
    public void visit(FuncCallNode     p_node){
        for (AST progChild : p_node.parentNode.parentNode.getChildNodes()) {
                if(progChild.concept.equals("FUNC DEF"))   {
                    //name of func
                    String curName = ((Token)p_node.getChildNodes().get(1).concept).getLexeme();
                    String compName = ((Token) progChild.getChildNodes().get(0).concept).getLexeme();
                    if(curName.equals(compName)){

                        String checkProgParams = progChild.getChildNodes().get(1).m_symtabentry.toString();
                        String curParams =p_node.getChildNodes().get(2).m_symtabentry.toString();
                        //if params are equal
                        if(checkProgParams.equals(curParams)){
                            //return type
                           String curReturn = ((Token)p_node.getChildNodes().get(3).concept).getLexeme();
                           String compReturn = ((Token) progChild.getChildNodes().get(2).concept).getLexeme();

                           if(compReturn.equals(curReturn)){
                               System.out.println("They're equal");
                           }
                           else{
//                    p_node.setType("typeerror");
                               this.m_errors += "Undefined member function declaration type error: "
                                       + "\n";
                           }
                        }
                    }



                }




        }
    };
    public void visit(MemberVarDeclNode   p_node){


    };
    public void visit(FuncDefNode p_node){
        for (AST child : p_node.getChildNodes()) {
            child.accept(this);
        }

    };
    //    public abstract void visit(FuncDefNode      p_node);
    public void visit(IDNode           p_node){

    };
    public void visit(InheritNode      p_node){

    };
    //    public abstract void visit(MultOpNode       p_node);
    public void visit(AST             p_node){

    };
    //    public abstract void visit(NumNode          p_node);
    public void visit(ParamsListNode    p_node){
        for (AST child : p_node.getChildNodes()) {
            child.accept(this);
        }

    };
    //    public abstract void visit(ProgramBlockNode p_node);
//    public abstract void visit(PutStatNode      p_node);
//    public abstract void visit(ReturnStatNode   p_node);
    public void visit(StatBlockNode    p_node){

    };
    //    public abstract void visit(TypeNode         p_node);
    public void visit(VarDeclNode      p_node){
        for (AST child : p_node.getChildNodes()) {
            child.accept(this);
        }

    };


}
