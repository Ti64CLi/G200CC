package backend.riscv.instruction;

import backend.riscv.RISCVOpcode;

import java.util.List;

public class RISCVECALLInstruction extends RISCVInstruction {
    public RISCVECALLInstruction() {
        super(RISCVOpcode.ECALL, List.of());
    }
}
