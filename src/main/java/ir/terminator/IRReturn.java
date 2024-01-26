package ir.terminator;

import ir.core.IRBlock;
import ir.core.IRValue;
import ir.core.IRVisitor;

import java.util.List;

public class IRReturn extends IRTerminator {
    public IRReturn(IRValue value) {
        super(List.of());
        if (value != null)
            this.addOperand(value);
    }

    @Override
    public Object accept(IRVisitor v) {
        return v.visitReturnTerminator(this);
    }
}
