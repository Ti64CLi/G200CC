package ir.terminator;

import java.util.List;

import ir.core.IRBlock;
import ir.core.IROperation;

public abstract class IRTerminator extends IROperation {

    private final List<IRBlock> successors;

    protected IRTerminator(List<IRBlock> successors) {
        super();
        this.successors = successors;
    }

    public List<IRBlock> getSuccessors() {
        return successors;
    }

}
