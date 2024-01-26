package compiler.backend;

import java.util.ArrayList;
import java.util.List;

import backend.BackendBlock;
import backend.BackendFunction;
import backend.BackendInstruction;
import backend.riscv.RISCVPhysicalRegister;
import backend.riscv.RISCVRegister;
import backend.riscv.instruction.RISCVADDIInstruction;
import backend.riscv.instruction.RISCVInstruction;
import backend.riscv.instruction.RISCVLWInstruction;
import backend.riscv.instruction.RISCVSWInstruction;
import backend.riscv.operand.RISCVImmediateOperand;
import backend.riscv.operand.RISCVMemoryOperand;
import backend.riscv.operand.RISCVRegisterOperand;
import ir.core.IRBlock;
import ir.core.IRFunction;
import ir.terminator.IRReturn;

public class NaiveRegisterAllocator extends AbstractRegisterAllocator {

	public List<RISCVPhysicalRegister> availableList;
	public List<RISCVPhysicalRegister> usedRegisters;
	
	public NaiveRegisterAllocator(BackendBuilder b) {
		super(b);
	}
	
	@Override
	public void allocateRegisters(BackendFunction f, int nbArgs) {
		usedRegisters = new ArrayList<RISCVPhysicalRegister>();
		availableList = new ArrayList<RISCVPhysicalRegister>();
		availableList.addAll(List.of(RISCVPhysicalRegister.S0,
				RISCVPhysicalRegister.S1,
				RISCVPhysicalRegister.S2,
				RISCVPhysicalRegister.S3,
				RISCVPhysicalRegister.S4,
				RISCVPhysicalRegister.S5,
				RISCVPhysicalRegister.S6,
				RISCVPhysicalRegister.S7,
				RISCVPhysicalRegister.S8,
				RISCVPhysicalRegister.S9,
				RISCVPhysicalRegister.S10,
				RISCVPhysicalRegister.S11,
				RISCVPhysicalRegister.T0,
				RISCVPhysicalRegister.T1,
				RISCVPhysicalRegister.T2,
				RISCVPhysicalRegister.T3,
				RISCVPhysicalRegister.T4,
				RISCVPhysicalRegister.T5,
				RISCVPhysicalRegister.T6));
		
		for (int oneNotArg = nbArgs; oneNotArg<=7; oneNotArg++) {
			availableList.add(RISCVPhysicalRegister.getArgRegister(oneNotArg));
		}
		
		for (BackendBlock b : f.getBlocks()) {
			for (BackendInstruction op : b.getInstructions()) {
				RISCVInstruction instr = (RISCVInstruction) op;
				if (availableList.isEmpty()) {
					throw new IllegalArgumentException("Cannot allocate with naive allocator !"); //Naive regalloc can only allocate free registers once
				}
				if (instr.getResult() != null && instr.getResult().getPhysicalRegister() == null) {
					instr.getResult().setPhysicalRegister(availableList.removeFirst());
					usedRegisters.add(instr.getResult().getPhysicalRegister());
				}
			}
		}
	}

	@Override
	public void insertABIInstructions(IRFunction f) {
		
		if (f.getBlocks().isEmpty()) {
			//Nothing to do on lib functions
			return;
		}
		
		//We find entry and exit blocks
		BackendBlock entry = builder.funcToFuncMap.get(f).getBlocks().getFirst();
		List<BackendBlock> exits = new ArrayList<BackendBlock>();
		
		for (IRBlock b : f.getBlocks()) {
			if (b.getTerminator() instanceof IRReturn) {
				exits.add(builder.blockToBlockMap.get(b));
			}
		}
		
		//We insert save/restore operations
		//decrement SP
		int incr = (usedRegisters.size() + 1) * 4; //don't forget that ra has to be saved
		RISCVInstruction spSub = new RISCVADDIInstruction(new RISCVRegisterOperand(RISCVPhysicalRegister.SP), 
				new RISCVImmediateOperand(-incr));
		spSub.getResult().setPhysicalRegister(RISCVPhysicalRegister.SP);		
		entry.addInstruction(spSub, 0);
		
		//We save RA
		entry.addInstruction(new RISCVSWInstruction(new RISCVRegisterOperand(RISCVPhysicalRegister.RA), 
				new RISCVMemoryOperand(new RISCVRegister(RISCVPhysicalRegister.SP), 0)),1);

		
		
		for (int regToSave = 0; regToSave<usedRegisters.size(); regToSave++) {
			//We save each register used
			entry.addInstruction(new RISCVSWInstruction(new RISCVRegisterOperand(usedRegisters.get(regToSave)), 
					new RISCVMemoryOperand(new RISCVRegister(RISCVPhysicalRegister.SP), (1+regToSave) * 4)), regToSave+2);
		}
		
		for (BackendBlock exitBlock : exits) {
			//We insert register restore for each exit block
			
			//Spadd increment sp
			RISCVInstruction spAdd = new RISCVADDIInstruction(new RISCVRegisterOperand(RISCVPhysicalRegister.SP), 
					new RISCVImmediateOperand(-incr));
			spAdd.getResult().setPhysicalRegister(RISCVPhysicalRegister.SP);
			
			//raLW restore RA
			RISCVInstruction raLW = new RISCVLWInstruction(new RISCVMemoryOperand(new RISCVRegister(RISCVPhysicalRegister.SP), 0));
			raLW.getResult().setPhysicalRegister(RISCVPhysicalRegister.RA);
			exitBlock.addInstruction(raLW, exitBlock.getInstructions().size()-1);
			
			for (int regToSave = 0; regToSave<usedRegisters.size(); regToSave++) {
				//We restore each register used
				RISCVInstruction lwInstr = new RISCVLWInstruction(new RISCVMemoryOperand(new RISCVRegister(RISCVPhysicalRegister.SP), (1+regToSave) * 4));
				lwInstr.getResult().setPhysicalRegister(usedRegisters.get(regToSave));
				exitBlock.addInstruction(lwInstr, exitBlock.getInstructions().size()-1);
			}
			exitBlock.addInstruction(spAdd, exitBlock.getInstructions().size()-1);

			
		}
		
	}

}
