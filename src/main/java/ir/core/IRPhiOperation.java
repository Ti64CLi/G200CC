package ir.core;

public class IRPhiOperation extends IROperation {

    public IRPhiOperation(IRType type) {
        super();
        this.addResult(type);
    }

    @Override
    public Object accept(IRVisitor v) {
        return v.visitPhiOperation(this);
    }
    
    @Override
    public void addOperand(IRValue v) {
        super.addOperand(v);
    }
}
