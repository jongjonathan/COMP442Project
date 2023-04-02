package AST;
import Visitor.*;
import SymbolTable.*;
import java.util.ArrayList;

public class AST {
    public Object concept;
    public AST parentNode;
    private ArrayList<AST> childNodes;
    public int depth;
    public String      m_data      = null;

    public String getData() {
        return this.m_data;
    }

    public  SymTable      m_symtab             = null;
    public  SymTableEntry m_symtabentry        = null;
    public  String      m_moonVarName        = new String();

    public void setParentNode(AST parentNode) {
        this.parentNode = parentNode;
    }

    public void addChild(AST p_child) {
        p_child.setParentNode(this);
        this.childNodes.add(p_child);
    }

    public ArrayList<AST> getChildNodes(){ return childNodes;}

    public void setChildNodes(ArrayList<AST> childNodes){ this.childNodes= childNodes;}

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
    public void accept(Visitor p_visitor) {
        p_visitor.visit(this);
    }
}