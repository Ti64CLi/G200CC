package ir.instruction;

import ir.core.IRValue;
import ir.core.IRFunction;
import ir.core.IRType;
import ir.core.IRVisitor;

import java.util.List;

public class IRFunctionCallInstruction extends IRInstruction {
	
	public IRFunction targetFunction;
	
    public IRFunctionCallInstruction(IRFunction func, IRType returnType, List<IRValue> operands) {
        super();
        targetFunction = func;
        for (IRValue operand : operands) {
            this.addOperand(operand);
        }
        this.addResult(returnType);
    }

    @Override
    public Object accept(IRVisitor v) {
        return v.visitFunctionCall(this);
    }
}
