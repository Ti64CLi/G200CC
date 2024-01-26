package ir.instruction;

import ir.core.IRType;
import ir.core.IRValue;
import ir.core.IRVisitor;

public class IRAllocaInstruction extends IRInstruction {

    private String varName;
    private int size;
    private IRType type;

    public IRAllocaInstruction(String name, IRType type, int size) {
        super();
        this.varName = name;
        this.type = type;
        this.addResult(IRType.ADDR);
    }

    public IRType getType() {
        return type;
    }

    public void setType(IRType type) {
        this.type = type;
    }

    public int getSize() {
        return size;
    }

    public String getVarName() {
        return varName;
    }

    @Override
    public Object accept(IRVisitor v) {
        return v.visitAllocaInstruction(this);
    }

}
