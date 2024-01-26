package ir.instruction;

import ir.core.IRType;
import ir.core.IRValue;
import ir.core.IRVisitor;

public class IRCompareGtInstruction extends IRInstruction {

    // Add instructions has two operands and produces a value
    public IRCompareGtInstruction(IRValue operand1, IRValue operand2) {
        super();
        this.addOperand(operand1);
        this.addOperand(operand2);
        this.addResult(IRType.UINT);
    }

    @Override
    public Object accept(IRVisitor v) {
        return v.visitCompareGtInstruction(this);
    }

}
