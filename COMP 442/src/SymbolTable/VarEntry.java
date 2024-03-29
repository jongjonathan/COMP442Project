package SymbolTable;

import java.util.Vector;

public class VarEntry extends SymTableEntry {
    public String visibility = "";

    public VarEntry(String p_kind, String p_type, String p_name, Vector<Integer> p_dims){
        super(p_kind, p_type, p_name, null);
        m_dims = p_dims;
    }
    public VarEntry(String p_kind, String p_type, String p_name, int p_dims){
        super(p_kind, p_type, p_name, null);
    }

    public VarEntry(String p_kind, String p_type, String p_name, Vector<Integer> p_dims, String visibility){
        super(p_kind, p_type, p_name, null);
        m_dims = p_dims;
        this.visibility = visibility;
    }

    public String toString(){
        return 	String.format("%-12s" , "| " + m_kind) +
                String.format("%-12s" , "| " + m_name) +
                String.format("%-12s"  , "| " + m_type) +
                String.format("%-8s"  , "| " + m_size) +
                String.format("%-8s"  , "| " + visibility) +
                 "|";
    }
}
