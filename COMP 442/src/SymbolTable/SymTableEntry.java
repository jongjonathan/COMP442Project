package SymbolTable;

import java.util.Vector;


    public class SymTableEntry {
        public String          m_kind       = null;
        public String          m_type       = null;
        public String          m_name       = null;
        public int             m_size       = 0;
        public SymTable         m_subtable   = null;

        public SymTableEntry() {}

        public SymTableEntry(String p_kind, String p_type, String p_name, SymTable p_subtable){
            m_kind = p_kind;
            m_type = p_type;
            m_name = p_name;
            m_subtable = p_subtable;
        }
    }


