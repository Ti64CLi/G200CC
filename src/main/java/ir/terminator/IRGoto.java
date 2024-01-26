package ir.terminator;

import ir.core.IRBlock;
import ir.core.IRVisitor;

import java.util.List;

public class IRGoto extends IRTerminator {

    // IRGoto
    // One successor, no operand

    public IRGoto(IRBlock dest) {
        super(List.of(dest));
    }

    @Override
    public Object accept(IRVisitor v) {
        return v.visitGotoTerminator(this);
    }
}
