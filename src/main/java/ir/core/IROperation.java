package ir.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class IROperation implements IRVisitableObject {

    private IRValue result; /* !< Value representing the result of the operation */
    private final List<IRValue> operands; /* !< List of values representing the operands of the operation */
    private IRBlock containingBlock;

    protected IROperation() {
        this.operands = new ArrayList<IRValue>();
    }

    public void addOperand(IRValue v) {
        this.operands.add(v);
        v.addUse(this);
    }

    public IRValue getResult() {
        return result;
    }
    public void setResult(IRValue v) {
        result = v;
    }

    protected void addResult(IRType type) {
        this.result = new IRValue(type, this);
    }

    public List<IRValue> getOperands() {
        return operands;
    }

    void setContainingBlock(IRBlock containingBlock) {
        this.containingBlock = containingBlock;
    }

    public IRBlock getContainingBlock() {
        return containingBlock;
    }
    
    public void replaceOperand(IRValue oldOperand, IRValue newOperand) {
    	int index = this.operands.indexOf(oldOperand);
    	this.operands.remove(index);
    	this.operands.add(index, newOperand);
    }
}
