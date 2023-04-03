package AST;
import Visitor.Visitor;
import java.util.ArrayList;

public class RelExprNode extends AST{

    public RelExprNode(AST parentNode, ArrayList<AST> childNodes, Object concept, int depth) {
        super(parentNode, childNodes, concept, depth);
    }
    public void accept(Visitor p_visitor) {
        p_visitor.visit(this);
    }

}
