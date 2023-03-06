import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class AST {
    Object concept;
    AST parentNode;
    ArrayList<AST> childNodes;
    int depth;

    static Stack<AST> semStack = new Stack<>();

    public void setParentNode(AST parentNode) {
        this.parentNode = parentNode;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public AST(AST parentNode, ArrayList<AST> childNodes,Object concept, int depth){
        this.parentNode = parentNode;
        this.childNodes = childNodes;
        this.concept = concept;
        this.depth = depth;
    }

    //null node
    static public AST makeNode(){
        semStack.push(null);
        return null;
    }

    static public AST makeNode(Token concept){
        AST node = new AST(null, null, concept,  0);
        semStack.push(node);
        return node;
    }

    static public AST makeFamily(Object concept, int pops){
        ArrayList<AST> childNodes = new ArrayList<>();
        //pop for concept node creation
        if(pops != -1){
            for(int i = 0; i < pops; i++){
                childNodes.add(semStack.pop());
            }
        }
        else {
            while(semStack.peek() != null){
                childNodes.add(semStack.pop());
            }
            semStack.pop();
        }
        AST parentNode = new AST(null, childNodes, concept,  0);

        for (var child: parentNode.childNodes){
            child.setParentNode(parentNode);
        }
        parentNode.updateDepth();

        Collections.reverse(childNodes);

        semStack.push(parentNode);

        return parentNode;
    }

    public void updateDepth(){
        if(this.childNodes == null){
            return;
        }
        for (var child: this.childNodes){
            child.setDepth(child.getDepth()+1);
            child.updateDepth();
        }
    }

    public static String treeToString(){
        //returns ast node in the tree format
        return semStack.toString();
    }

    @Override
    public String toString() {
        StringBuilder tree = new StringBuilder();
        for(int i=0;i<depth; i++){
            tree.append("|  ");
        }
        tree.append(concept).append("\n");
        if(childNodes != null){
            for(var subtree: childNodes){
                tree.append(subtree.toString());
            }
        }

        return tree.toString();
    }
}