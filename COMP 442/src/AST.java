import java.util.ArrayList;

public class AST {
    Object concept;
    AST parentNode;
    ArrayList<AST> childNodes;
    int depth;

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

    public void updateDepth(){
        if(this.childNodes == null){
            return;
        }
        for (var child: this.childNodes){
            child.setDepth(child.getDepth()+1);
            child.updateDepth();
        }
    }

    @Override
    public String toString() {
        StringBuilder tree = new StringBuilder();
        for(int i=0;i<depth; i++){
            tree.append("|    ");
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