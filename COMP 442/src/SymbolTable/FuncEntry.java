package SymbolTable;

import java.util.Vector;

public class FuncEntry extends SymTableEntry {

//    public Vector<VarEntry> m_params   = new Vector<VarEntry>();

    public String m_params = "";

    public FuncEntry(String p_type, String p_name, String p_params, SymTable p_table){
        super(new String("function"), p_type, p_name, p_table);
        m_params = p_params;
    }


    public String toString(){
        return 	String.format("%-12s" , "| " + m_kind) +
                String.format("%-12s" , "| " + m_name) +
                String.format("%-12s"  , "| " + m_type) +
                String.format("%-28s"  , "| " + m_params) +
                "|" +
                m_subtable;
    }
}
