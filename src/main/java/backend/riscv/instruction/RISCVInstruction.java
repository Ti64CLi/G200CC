package backend.riscv.instruction;

import backend.BackendInstruction;
import backend.riscv.RISCVOpcode;
import backend.riscv.operand.RISCVOperand;
import backend.riscv.RISCVRegister;

import java.util.List;
import java.util.StringJoiner;

public abstract class RISCVInstruction extends BackendInstruction {
    private RISCVRegister result;

    protected RISCVInstruction(RISCVOpcode opcode, List<? extends RISCVOperand> operands) {
        super(opcode, operands);
    }

    protected void addResult() {
        result = new RISCVRegister();
    }

    public RISCVRegister getResult() {
        return result;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(getOpcode().toString());
        result.append(' ');

        RISCVRegister rd = getResult();
        if (rd != null)
            result.append(rd.toString()).append(", ");

        StringJoiner joiner = new StringJoiner(", ");
        getOperands().forEach(operand -> joiner.add(operand.toString()));
        result.append(joiner);

        return result.toString();
    }
}
