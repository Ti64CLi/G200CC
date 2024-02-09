package compiler.frontend;

import java.util.ArrayList;
import java.util.HashMap;

public class SymbolTableLevel {
    private HashMap<String, SymbolTableEntry> entries;
    private ArrayList<SymbolTable> children;

    /**
     * Constructor for SymbolTableLevel. Initializes entries and children as empty ArrayLists
     */
    public SymbolTableLevel() {
        entries = new HashMap<String, SymbolTableEntry>();
        children = new ArrayList<SymbolTable>();
    }

    /**
     * Insert an entry into the symbol table
     * @param entry SymbolTableEntry
     */
    public void insertSymbolTableEntry(SymbolTableEntry entry) {
        if (this.getSymbolTableEntry(entry.id) == null) {
            this.entries.put(entry.id, entry);
        }
        else {
            throw new RuntimeException("SymbolTableLevel Error: " + entry.id + " already declared in this scope");
        }
    }

    /**
     * Lookup an entry in the SymbolTableLevel
     * @param id String
     * @return SymbolTableEntry or null if not found
     */
    public SymbolTableEntry getSymbolTableEntry(String id) {
        for (SymbolTableEntry entry : this.entries.values()) {
            if (entry.id.equals(id)) {
                return entry;
            }
        }
        return null;
    }

    /**
     * Get the size of the SymbolTableEntry list
     * @return int
     */
    public int getSymbolTableEntrySize() {
        return this.entries.size();
    }

    /**
     * Insert a child into the symbol table
     * @param child SymbolTable
     */
    public void insertSymbolTable(SymbolTable child) {
        this.children.add(child);
    }

    /**
     * Get the SymbolTable at index index
     * @param index int
     * @return SymbolTable
     */
    public SymbolTable getSymbolTable(int index) {
        return this.children.get(index);
    }

    /**
     * Get the size of the SymbolTable list
     * @return int
     */
    public int getSymbolTableSize() {
        return this.children.size();
    }
}