package ir.instruction;

import ir.core.IRValue;
import ir.core.IRVisitor;

public class IRStoreInstruction extends IRInstruction {

    // Store instructions has one address operand and a value operand
    public IRStoreInstruction(IRValue addr, IRValue val) {
        super();
        this.addOperand(addr);
        this.addOperand(val);
        this.addResult(null);
    }

    @Override
    public Object accept(IRVisitor v) {
        return v.visitStoreInstruction(this);
    }
}
