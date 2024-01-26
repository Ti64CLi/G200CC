package ir.core;

import java.util.ArrayList;
import java.util.List;

public class IRValue implements IRObject{

    private static int number = 0;
    private final List<IROperation> uses; /* !< List of operations that use the value as operand */
    public  IRType type; 
    public IROperation definingOperation;
    private final int id;

    public IRValue(IRType type, IROperation definingOperation) {
        this.id = number++;
        this.type = type;
        this.uses = new ArrayList<>();
        this.definingOperation = definingOperation;
    }

    public List<IROperation> getUses() {
        return uses;
    }

    public void addUse(IROperation user) {
        uses.add(user);
    }

    public IRType getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public IROperation getDefiningOperation() {
        return definingOperation;
    }
    
    public String toString() {
    	return "%" + getId();
    }
}
