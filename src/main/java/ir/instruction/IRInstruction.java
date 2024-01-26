package ir.instruction;

import ir.core.IROperation;
import ir.core.IRType;

public abstract class IRInstruction extends IROperation {

    protected IRInstruction() {
        super();
    }

    // Used to get the type of the result of a binary instruction
    protected IRType getBinaryOpResultType(IRType type1, IRType type2) {
        if (type1 == IRType.FLOAT || type2 == IRType.FLOAT) {
            return IRType.FLOAT;
        } else if (type1 == IRType.UINT || type2 == IRType.UINT) {
            return IRType.UINT;
        } else if (type1 == IRType.INT && type2 == IRType.INT) {
            return IRType.INT;
        } else {
            return IRType.CHAR;
        }
    }
}
