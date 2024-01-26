package compiler.backend;

import java.util.HashMap;
import java.util.List;

import backend.BackendInstruction;
import backend.riscv.RISCVRegister;
import ir.core.IRBlock;
import ir.core.IRValue;

public abstract class InstructionSelector {
	
	protected HashMap<IRValue, RISCVRegister> valueToRegisterMap;
	
	public abstract List<BackendInstruction>selectInstructions(IRBlock b);
	public abstract void assignBackendRegisters();

	public void registerIRValueToOperandMapping(IRValue val, RISCVRegister reg) {
		valueToRegisterMap.put(val, reg);
	}

}
