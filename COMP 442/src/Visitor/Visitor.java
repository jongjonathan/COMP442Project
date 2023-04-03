package Visitor;
import AST.*;

/**
 * Visitor superclass. Can be either an interface or an abstract class.
 * Needs to have one visit method for each of the visit methods
 * implemented by any of its subclasses.
 *
 * This forces all its subclasses to implement all of them, even if they
 * are not concerned with processing of this particular subtype, creating
 * visit methods with a body whose only function is to propagate accept() to
 * all the children of the visited node.
 */

public abstract class Visitor {
    public abstract void visit(AddOpNode        p_node);
    public abstract void visit(AssignOpNode   p_node);
    public abstract void visit(ArithmNode   p_node);
    public abstract void visit(ExprNode   p_node);
//    public abstract void visit(ClassListNode    p_node);
    public abstract void visit(ClassNode p_node);
//    public abstract void visit(DimListNode      p_node);
    public abstract void visit(ArrSizeNode          p_node);
    public abstract void visit(FuncCallNode     p_node);
    public abstract void visit(MemberVarDeclNode   p_node);
    public abstract void visit(FuncDefNode p_node);
//    public abstract void visit(FuncDefNode      p_node);
    public abstract void visit(IDNode           p_node);
    public abstract void visit(IfNode           p_node);
    public abstract void visit(InheritNode      p_node);
    public abstract void visit(IndiceNode      p_node);
    public abstract void visit(MultOpNode       p_node);
    public abstract void visit(AST             p_node);
    public abstract void visit(NumNode          p_node);
    public abstract void visit(ParamsListNode    p_node);
    public abstract void visit(ProgNode         p_node);
//    public abstract void visit(ProgramBlockNode p_node);
//    public abstract void visit(PutStatNode      p_node);
//    public abstract void visit(ReturnStatNode   p_node);
    public abstract void visit(StatBlockNode    p_node);
    public abstract void visit(StatNode    p_node);
//    public abstract void visit(TypeNode         p_node);
    public abstract void visit(VarDeclNode      p_node);
    public abstract void visit(WriteNode      p_node);
}