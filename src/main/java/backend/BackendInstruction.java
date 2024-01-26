package backend;

import java.util.List;
import java.util.StringJoiner;

public abstract class BackendInstruction implements BackendVisitableObject {

    private final BackendOpcode opcode;
    private final List<? extends BackendInstructionOperand> operands;
    private BackendBlock containingBlock;

    protected BackendInstruction(BackendOpcode opcode, List<? extends BackendInstructionOperand> operands) {
        this.opcode = opcode;
        this.operands = operands;
    }

    void setContainingBlock(BackendBlock block) {
        containingBlock = block;
    }

    public BackendBlock getContainingBlock() {
        return containingBlock;
    }

    public BackendOpcode getOpcode() {
        return opcode;
    }

    public List<? extends BackendInstructionOperand> getOperands() {
        return operands;
    }

    @Override
    public Object accept(BackendVisitor v) {
        return v.visitInstruction(this);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(opcode.toString());
        result.append(' ');

        StringJoiner joiner = new StringJoiner(",");
        getOperands().forEach(operand -> joiner.add(operand.toString()));
        result.append(joiner);

        return result.toString();
    }
}
