package compiler.backend;

import backend.BackendFunction;
import ir.core.IRFunction;

public abstract class AbstractRegisterAllocator {

	BackendBuilder builder;
	
	public AbstractRegisterAllocator(BackendBuilder b) {
		this.builder = b;
	}
	
	public abstract void allocateRegisters(BackendFunction f, int nbArg);
	public abstract void insertABIInstructions(IRFunction f);
}
