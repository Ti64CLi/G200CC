package ir.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ir.terminator.IRTerminator;

public class IRBlock implements IRVisitableObject {

    private final List<IROperation> operations; /*
     * !< List of operations inside the block. Last one should be a IRTerminator
     */
    private final List<IRBlock> predecessors; /*
     * !< List of predecessors in the control flow graph. Built automatically when
     * calling addTerminator() on a block
     */
    
    public final IRFunction containingFunction;

    public IRBlock(IRFunction f) {
        operations = new ArrayList<>();
        predecessors = new ArrayList<>();
        containingFunction = f;
    }

    public IRTerminator getTerminator() {
        assert (operations.get(operations.size() - 1) instanceof IRTerminator);
        return (IRTerminator) operations.get(operations.size() - 1);
    }

    public void addTerminator(IRTerminator t) {
        // We add predecessor to each successor
        for (IRBlock successor : t.getSuccessors())
            successor.predecessors.add(this);
        // We insert the terminator operation
        addOperation(t);
    }

    public void addOperation(IROperation op) {
        op.setContainingBlock(this);
        this.operations.add(op);
    }

    public List<IRBlock> getSuccessors() {
        return getTerminator().getSuccessors();
    }

    public List<IRBlock> getPredecessors() {
        return predecessors;
    }

    public List<IROperation> getOperations() {
        return operations;
    }
    
    public int getBlockIndexInContainingFunc() {
    	return this.containingFunction.getBlocks().indexOf(this);
    }
    
    @Override
    public Object accept(IRVisitor v) {
        return v.visitBlock(this);
    }

}
