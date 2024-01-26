package backend.riscv.operand;

import backend.riscv.RISCVPhysicalRegister;
import backend.riscv.RISCVRegister;

public class RISCVMemoryOperand extends RISCVOperand {
    public RISCVRegister base;
    public Integer offset;

    public RISCVMemoryOperand(RISCVRegister base, Integer offset) {
        this.base = base;
        this.offset = offset;
    }

    public RISCVRegister getBase() {
        return base;
    }

    public Integer getOffset() {
        return offset;
    }
    
    public String toString() {
		return offset.toString() + "(" + base.toString() + ")";
    }
}
