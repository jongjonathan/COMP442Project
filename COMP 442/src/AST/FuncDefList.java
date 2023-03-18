package AST;

import java.util.ArrayList;

public class FuncDefList extends AST{

    public FuncDefList(AST parentNode, ArrayList<AST> childNodes, Object concept, int depth) {
        super(parentNode, childNodes, concept, depth);
    }

}
