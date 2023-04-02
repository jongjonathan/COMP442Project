package AST;
import Visitor.Visitor;
import java.util.ArrayList;

public class ExprNode extends AST{

    public  String      exprVal        = "tempvar";

    public ExprNode(AST parentNode, ArrayList<AST> childNodes, Object concept, int depth) {
        super(parentNode, childNodes, concept, depth);
    }
    public void accept(Visitor p_visitor) {
        p_visitor.visit(this);
    }

}
