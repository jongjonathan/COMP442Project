package AST;
import java.util.ArrayList;
import java.util.List;
import Visitor.Visitor;

public class StatBlockNode extends AST {

    public StatBlockNode(AST parentNode, ArrayList<AST> childNodes, Object concept, int depth) {
        super(parentNode, childNodes, concept, depth);
    }

    public void accept(Visitor p_visitor) {
        p_visitor.visit(this);
    }
}
