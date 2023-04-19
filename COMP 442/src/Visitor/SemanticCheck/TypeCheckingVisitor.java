package Visitor.SemanticCheck;
import AST.*;
import Lexer.*;
import Visitor.SymbolTable.SymbolTableCreationVisitor;
import Visitor.Visitor;
import java.io.File;
import java.io.FileOutputStream;
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
        boolean noMain = true;
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildNodes()) {
            child.accept(this);
        }
        for (AST progChild : p_node.getChildNodes()) {
            if(progChild instanceof FuncDefNode){
                if(((Token)progChild.getChildNodes().get(0).concept).getLexeme().equals("main")){
                    noMain = false;
                }
            }
        }
        if(noMain == true){
            this.m_errors += "[error 15.2] non-existing main function " +"\n";
        }
            if (!this.m_outputfilename.isEmpty()) {
                File file = new File(this.m_outputfilename);
                try {
                    PrintWriter out = new PrintWriter((new FileOutputStream(file, true)));
                    out.append(this.m_errors);
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
        int oneClass = 0;
        for (AST progChild : p_node.parentNode.getChildNodes()) {
            if(progChild instanceof ClassNode){
                String iterClassName = ((Token)progChild.getChildNodes().get(0).concept).getLexeme();
                if(((Token)p_node.getChildNodes().get(0).concept).getLexeme().equals(iterClassName)){
                    oneClass++;
                }
            }
        }
        if(oneClass>1){
            this.m_errors += "[error 8.1] multiply declared class : "
                    + ((Token) p_node.getChildNodes().get(0).concept).getLexeme()
                    +" , on line "+((Token) p_node.getChildNodes().get(0).concept).getPosition()+"\n";
        }

    }
    //    public abstract void visit(DimListNode      p_node);
//    public abstract void visit(DimNode          p_node);
    public void visit(FuncCallNode     p_node){
        for (AST child : p_node.getChildNodes()) {
            child.accept(this);
        }
        boolean found = false;
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
                               found =true;
                               ((FuncDefNode)progChild).declared = true;
                           }
                        }

                    }
                }
        }
        for (AST progChild : p_node.parentNode.getChildNodes()) {
            if(progChild instanceof FuncCallNode){
                String iterFuncName = ((Token)progChild.getChildNodes().get(1).concept).getLexeme();
                if(((Token)p_node.getChildNodes().get(1).concept).getLexeme().equals(iterFuncName)){
                    //if params not empty
                    if(p_node.getChildNodes().size()>3 && p_node.getChildNodes().size()>3) {
                        String checkParam = progChild.getChildNodes().get(2).m_symtabentry.toString();
                        String pnodeParam = p_node.getChildNodes().get(2).m_symtabentry.toString();
                        if ((checkParam.equals(pnodeParam))) {




                        } else {
                            this.m_errors += "[warning 9.2] Overloaded member function : "
                                    + ((Token) p_node.getChildNodes().get(1).concept).getLexeme()
                                    + " , on line " + ((Token) p_node.getChildNodes().get(1).concept).getPosition() + "\n";
                        }
                    }

                }
            }
        }

                if(p_node.parentNode.getChildNodes().get(1).getChildNodes().size()!=0){
                    String inherClass = ((Token)p_node.parentNode.getChildNodes().get(1).getChildNodes().get(0).concept).getLexeme();
                    for (AST progChild : p_node.parentNode.parentNode.getChildNodes()) {
                        //if class equal, go and compare function
                        if(((Token)(progChild.getChildNodes().get(0).concept)).getLexeme().equals(inherClass)){
                            for(AST child2 : progChild.getChildNodes()){
                                if(child2 instanceof FuncCallNode){
                                    if(child2.m_symtabentry.toString().equals(p_node.m_symtabentry.toString())){
                                        this.m_errors += "[warning 9.3] Overidden member function : "
                                                + ((Token) p_node.getChildNodes().get(1).concept).getLexeme()
                                                + " , on line " + ((Token) p_node.getChildNodes().get(1).concept).getPosition() + "\n";
                                    }
                                }
                            }
                            }
                        }
                    }

        if(found == false){
                this.m_errors += "[error 6.2] Undefined member function declaration type : "
                        + ((Token) p_node.getChildNodes().get(1).concept).getLexeme()
                        +" method not found, on line "+((Token) p_node.getChildNodes().get(1).concept).getPosition()+"\n";

        }
    };
    public void visit(MemberVarDeclNode   p_node){
        for (AST child : p_node.getChildNodes()) {
            child.accept(this);
        }
        int countVar = 0;
        for (AST child : p_node.parentNode.getChildNodes()) {
            if(child instanceof MemberVarDeclNode){
                String iterFuncName = ((Token)child.getChildNodes().get(1).concept).getLexeme();
                if(((Token)p_node.getChildNodes().get(1).concept).getLexeme().equals(iterFuncName)){
                    countVar++;
                }
            }
        }
        if(countVar>1){
            this.m_errors += "[error 8.3] multiply declared data member in class : "
                    + ((Token) p_node.getChildNodes().get(1).concept).getLexeme()
                    +" , on line "+((Token) p_node.getChildNodes().get(1).concept).getPosition()+"\n";
        }


    };
    public void visit(FuncDefNode p_node){
        for (AST child : p_node.getChildNodes()) {
            child.accept(this);
        }
        int multFreeFunc = 0;
        int overloadFunc = 0;
        int countMain = 0;
        String checkParam = "";
        String pnodeParam = "";
        for (AST progChild : p_node.parentNode.getChildNodes()) {
            if(progChild instanceof FuncDefNode){
                String iterFuncName = ((Token)progChild.getChildNodes().get(0).concept).getLexeme();
                if(((Token)p_node.getChildNodes().get(0).concept).getLexeme().equals(iterFuncName)){
                    //if params are same
                    //if contains params list so >3
                    if(progChild.getChildNodes().size()>3 && p_node.getChildNodes().size()>3){
                        if(progChild.getChildNodes().get(1) instanceof ParamsListNode){
                             checkParam = progChild.m_symtabentry.toString();
                             pnodeParam =p_node.m_symtabentry.toString();
                        }

                        if((checkParam.equals(pnodeParam))){
                            multFreeFunc++;
                        }
                        else{
                            overloadFunc++;
                        }
                    }
                    if(((Token)progChild.getChildNodes().get(0).concept).getLexeme().equals("main")){
                        countMain++;
                    }

                }

            }
        }
        if(p_node.declared == false && !((Token) p_node.getChildNodes().get(0).concept).getLexeme().equals("main") ){
            this.m_errors += "[error 6.1] undeclared member function definition "
                    + ((Token) p_node.getChildNodes().get(0).concept).getLexeme()
                    +" , on line "+((Token) p_node.getChildNodes().get(0).concept).getPosition()+"\n";
        }
        if(multFreeFunc>1){
            this.m_errors += "[error 8.2] multiply declared free function : "
                    + ((Token) p_node.getChildNodes().get(0).concept).getLexeme()
                    +" , on line "+((Token) p_node.getChildNodes().get(0).concept).getPosition()+"\n";
        }
        if(overloadFunc>0){
            this.m_errors += "[warning 9.1] Overloaded free function : "
                    + ((Token) p_node.getChildNodes().get(0).concept).getLexeme()
                    +" , on line "+((Token) p_node.getChildNodes().get(0).concept).getPosition()+"\n";
        }
        if(countMain>1){
            this.m_errors += "[error 15.3] duplicate main function "
                    + ((Token) p_node.getChildNodes().get(0).concept).getLexeme()
                    +" , on line "+((Token) p_node.getChildNodes().get(0).concept).getPosition()+"\n";
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
        for (AST child : p_node.getChildNodes()) {
            child.accept(this);
        }

    }
    //    public abstract void visit(TypeNode         p_node);
    public void visit(VarDeclNode      p_node){
        for (AST child : p_node.getChildNodes()) {
            child.accept(this);
        }
        int countID = 0;
        for (AST child : p_node.parentNode.parentNode.getChildNodes()) {
            if(child instanceof StatNode){
                for(AST child2 : child.getChildNodes()){
                    if(child2 instanceof VarDeclNode) {
                        String iterFuncName = ((Token) child2.getChildNodes().get(0).concept).getLexeme();
                        if (((Token) p_node.getChildNodes().get(0).concept).getLexeme().equals(iterFuncName)) {
                            countID++;
                        }
                    }
                }

            }
        }
        if(countID>1){
            this.m_errors += "[error 8.4] multiply declared identifier in function : "
                    + ((Token) p_node.getChildNodes().get(0).concept).getLexeme()
                    +" , on line "+((Token) p_node.getChildNodes().get(1).concept).getPosition()+"\n";
        }

        if(((Token) p_node.getChildNodes().get(1).concept).getTokenType().equals(TokenType.ID)){
            boolean found = false;
            String curClass = ((Token) p_node.getChildNodes().get(1).concept).getLexeme();
            for(AST progChild : p_node.parentNode.parentNode.parentNode.getChildNodes()){
                if(progChild instanceof ClassNode){
                    String progClass = ((Token) progChild.getChildNodes().get(0).concept).getLexeme();
                    if(curClass.equals(progClass)){
                        found = true;
                    }
                }
            }
            if(found == false){
                this.m_errors+="[error 11.5] Undeclared Class : "
                        + ((Token) p_node.getChildNodes().get(1).concept).getLexeme()
                        +", line "+((Token) p_node.getChildNodes().get(1).concept).getPosition()+"\n";
            }
        }


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
        for (AST child2 : p_node.getChildNodes() ) {
           if(child2 instanceof ArithmNode){
               for(AST child3 : child2.getChildNodes()){
                   if(child3 instanceof IDNode) {
                       if (((Token) child3.getChildNodes().get(0).concept).getTokenType().name().equals("ID")){
                           this.m_errors += "[error 13.2] Array index is not an integer : "
                                   + ((Token) child3.getChildNodes().get(0).concept).getLexeme()
                                   +" , on line "+((Token) child3.getChildNodes().get(0).concept).getPosition()+"\n";
                       }
                   }
               }
           }
        }

    }
    public void visit(StatNode    p_node){
        for (AST child : p_node.getChildNodes() ) {
            child.accept(this);
        }
    }
    public void visit(IfNode    p_node){
        for (AST child : p_node.getChildNodes() ) {
            child.accept(this);
        }
    }
    public void visit(RelExprNode    p_node){
        for (AST child : p_node.getChildNodes() ) {
            child.accept(this);
        }
    }
    public void visit(ReadNode    p_node){
        for (AST child : p_node.getChildNodes() ) {
            child.accept(this);
        }
    }



}
