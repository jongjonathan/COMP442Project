package AST;

import java.util.ArrayList;

public class ClassNode extends AST{

    public ClassNode(AST parentNode, ArrayList<AST> childNodes, Object concept, int depth) {
        super(parentNode, childNodes, concept, depth);
    }

}
