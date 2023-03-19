package SymbolTable;

import java.util.ArrayList;

    public class SymTable {
        public String                 m_name       = null;
        public ArrayList<SymTableEntry> m_symlist    = null;
        public int                    m_size       = 0;
        public int                    m_tablelevel = 0;
        public SymTable                 m_uppertable = null;


        public SymTable(int p_level, SymTable p_uppertable){
            m_tablelevel = p_level;
            m_name = null;
            m_symlist = new ArrayList<SymTableEntry>();
            m_uppertable = p_uppertable;
        }

        public SymTable(int p_level, String p_name, SymTable p_uppertable){
            m_tablelevel = p_level;
            m_name = p_name;
            m_symlist = new ArrayList<SymTableEntry>();
            m_uppertable = p_uppertable;
        }

        public void addEntry(SymTableEntry p_entry){
            m_symlist.add(p_entry);
        }

        public SymTableEntry lookupName(String p_tolookup) {
            SymTableEntry returnvalue = new SymTableEntry();
            boolean found = false;
            for( SymTableEntry rec : m_symlist) {
                if (rec.m_name.equals(p_tolookup)) {
                    returnvalue = rec;
                    found = true;
                }
            }
            if (!found) {
                if (m_uppertable != null) {
                    returnvalue = m_uppertable.lookupName(p_tolookup);
                }
            }
            return returnvalue;
        }

        public String toString(){
            String stringtoreturn = new String();
            String prelinespacing = new String();
            for (int i = 0; i < this.m_tablelevel; i++)
                prelinespacing += "|    ";
            stringtoreturn += "\n" + prelinespacing + "=====================================================\n";
            stringtoreturn += prelinespacing + String.format("%-25s" , "| table: " + m_name) + String.format("%-27s" , " scope offset: " + m_size) + "|\n";
            stringtoreturn += prelinespacing        + "=====================================================\n";
            for (int i = 0; i < m_symlist.size(); i++){
                stringtoreturn +=  prelinespacing + m_symlist.get(i).toString() + '\n';
            }
            stringtoreturn += prelinespacing        + "=====================================================";
            return stringtoreturn;
        }
    }

