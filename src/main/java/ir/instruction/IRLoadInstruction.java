package ir.instruction;

import ir.core.IRType;
import ir.core.IRValue;
import ir.core.IRVisitor;

public class IRLoadInstruction extends IRInstruction {

    // Load instructions has one address operand and produces a value
    public IRLoadInstruction(IRValue operand1, IRType type) {
        super();
        this.addOperand(operand1);
        this.addResult(type);
    }

    @Override
    public Object accept(IRVisitor v) {
        return v.visitLoadInstruction(this);
    }
}
