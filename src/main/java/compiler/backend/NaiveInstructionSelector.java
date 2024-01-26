package compiler.backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import antlr.IRTXTParser.IRCallOperationContext;
import backend.BackendInstruction;
import backend.riscv.RISCVPhysicalRegister;
import backend.riscv.RISCVRegister;
import backend.riscv.instruction.RISCVADDIInstruction;
import backend.riscv.instruction.RISCVADDInstruction;
import backend.riscv.instruction.RISCVBNEInstruction;
import backend.riscv.instruction.RISCVDIVInstruction;
import backend.riscv.instruction.RISCVInstruction;
import backend.riscv.instruction.RISCVJALInstruction;
import backend.riscv.instruction.RISCVJALRInstruction;
import backend.riscv.instruction.RISCVLUIInstruction;
import backend.riscv.instruction.RISCVLWInstruction;
import backend.riscv.instruction.RISCVMULInstruction;
import backend.riscv.instruction.RISCVPhiInstruction;
import backend.riscv.instruction.RISCVSLTInstruction;
import backend.riscv.instruction.RISCVSUBInstruction;
import backend.riscv.instruction.RISCVSWInstruction;
import backend.riscv.operand.RISCVImmediateOperand;
import backend.riscv.operand.RISCVMemoryOperand;
import backend.riscv.operand.RISCVOperand;
import backend.riscv.operand.RISCVRegisterOperand;
import ir.core.IRBlock;
import ir.core.IROperation;
import ir.core.IRPhiOperation;
import ir.core.IRValue;
import ir.instruction.IRAddInstruction;
import ir.instruction.IRAllocaInstruction;
import ir.instruction.IRCompareGtInstruction;
import ir.instruction.IRCompareLtInstruction;
import ir.instruction.IRConstantInstruction;
import ir.instruction.IRDivInstruction;
import ir.instruction.IRFunctionCallInstruction;
import ir.instruction.IRGetElementPtrInstruction;
import ir.instruction.IRLoadInstruction;
import ir.instruction.IRMulInstruction;
import ir.instruction.IRStoreInstruction;
import ir.instruction.IRSubInstruction;
import ir.terminator.IRCondBr;
import ir.terminator.IRGoto;
import ir.terminator.IRReturn;

public class NaiveInstructionSelector extends InstructionSelector {

	private HashMap<RISCVOperand, IRValue> operandsToSet;
	private final BackendBuilder builder;
	
	public NaiveInstructionSelector(BackendBuilder b) {
		this.valueToRegisterMap = new HashMap<IRValue, RISCVRegister>();
		operandsToSet = new HashMap<RISCVOperand, IRValue>();
		this.builder = b;
	}
	
	@Override
	public List<BackendInstruction> selectInstructions(IRBlock b) {
		ArrayList<BackendInstruction> res = new ArrayList<BackendInstruction>();
		for (IROperation op : b.getOperations()) {
			
			List<RISCVInstruction> translatedOp = new ArrayList<RISCVInstruction>();
			
			//Many tricky things to handle correctly operation operands: 
			//Cannot just allocate them in a single pass because phi may have unvisited operands
			//Memory operation have different operands, cmp operation may use operand in reverse order
			
			List<RISCVRegisterOperand> riscvRegisterOperands = new ArrayList<RISCVRegisterOperand>();
			List<RISCVMemoryOperand> riscvMemoryOperands = new ArrayList<RISCVMemoryOperand>();
			
			if (op instanceof IRLoadInstruction){
				riscvMemoryOperands.add(new RISCVMemoryOperand(null, 0));
			}
			else if (op instanceof IRStoreInstruction){
				RISCVMemoryOperand memOperand = new RISCVMemoryOperand(null, 0);
				RISCVRegisterOperand regOperand = new RISCVRegisterOperand();
				riscvMemoryOperands.add(memOperand);
				riscvRegisterOperands.add(regOperand);
				operandsToSet.put(regOperand, op.getOperands().get(0));
				operandsToSet.put(memOperand, op.getOperands().get(1));
			}
			else {
				for (IRValue v : op.getOperands()) {
					RISCVRegisterOperand operand = new RISCVRegisterOperand();
					riscvRegisterOperands.add(operand);
					operandsToSet.put(operand, v);
				}
			}
			
			switch (op) {
				case IRPhiOperation p -> translatedOp.add(new RISCVPhiInstruction(riscvRegisterOperands));
				case IRAddInstruction i -> translatedOp.add(new RISCVADDInstruction(riscvRegisterOperands.get(0), 
						riscvRegisterOperands.get(1)));
				case IRAllocaInstruction i -> throw new IllegalArgumentException(); //TODO
				case IRCompareGtInstruction i ->translatedOp.add(new RISCVSLTInstruction(riscvRegisterOperands.get(1), riscvRegisterOperands.get(0)));
				case IRCompareLtInstruction i -> translatedOp.add(new RISCVSLTInstruction(riscvRegisterOperands.get(0), riscvRegisterOperands.get(1)));
				case IRConstantInstruction i -> {
					RISCVInstruction lui = new RISCVLUIInstruction(new RISCVImmediateOperand(i.getValue().intValue() >> 12));
					RISCVInstruction addi = new RISCVADDIInstruction(new RISCVRegisterOperand(lui.getResult()), new RISCVImmediateOperand(i.getValue().intValue() & 0xfff));
					translatedOp.add(lui);
					translatedOp.add(addi);
				}
				case IRDivInstruction i -> translatedOp.add(new RISCVDIVInstruction(riscvRegisterOperands.get(0), riscvRegisterOperands.get(1)));
				case IRFunctionCallInstruction i -> {
					//We move args
					for (int oneArg = 0; oneArg < i.getOperands().size(); oneArg++) {
						RISCVInstruction argMov = new RISCVADDIInstruction(riscvRegisterOperands.get(oneArg), new RISCVImmediateOperand(0));
						argMov.getResult().setPhysicalRegister(RISCVPhysicalRegister.getArgRegister(oneArg));
						translatedOp.add(argMov);
					}
					//We do the call (JAL with rs1 at RA)
					RISCVJALInstruction jal = new RISCVJALInstruction(builder.funcToFuncMap.get(i.targetFunction), false);
					jal.getResult().setPhysicalRegister(RISCVPhysicalRegister.RA);
					translatedOp.add(jal);
					
					//We have to create a real value for the result of the call
					RISCVInstruction returnMov = new RISCVADDIInstruction(new RISCVRegisterOperand(RISCVPhysicalRegister.A0), new RISCVImmediateOperand(0));
					translatedOp.add(returnMov);
				}
				case IRGetElementPtrInstruction i -> throw new IllegalArgumentException(); //TODO
				case IRLoadInstruction i -> translatedOp.add(new RISCVLWInstruction(riscvMemoryOperands.get(0)));
				case IRMulInstruction i -> translatedOp.add(new RISCVMULInstruction(riscvRegisterOperands.get(0), riscvRegisterOperands.get(1)));
				case IRStoreInstruction i -> translatedOp.add(new RISCVSWInstruction(riscvRegisterOperands.get(0), riscvMemoryOperands.get(0)));
				case IRSubInstruction i -> translatedOp.add(new RISCVSUBInstruction(riscvRegisterOperands.get(0), riscvRegisterOperands.get(1)));
				case IRCondBr t -> {
					RISCVRegisterOperand operandR0 = new RISCVRegisterOperand(RISCVPhysicalRegister.ZERO);
					translatedOp.add(new RISCVBNEInstruction(riscvRegisterOperands.get(0), operandR0, 
							builder.blockToBlockMap.get(b.getSuccessors().get(0))));
				}
				case IRGoto t -> translatedOp.add(new RISCVJALInstruction(builder.blockToBlockMap.get(
							b.getSuccessors().get(0)), true));	
				case IRReturn t -> {
					RISCVRegisterOperand operandRa = new RISCVRegisterOperand(RISCVPhysicalRegister.RA);
					RISCVRegisterOperand operandR0 = new RISCVRegisterOperand(RISCVPhysicalRegister.ZERO);
					RISCVInstruction returnMov = new RISCVADDInstruction(riscvRegisterOperands.get(0), operandR0);
					returnMov.getResult().setPhysicalRegister(RISCVPhysicalRegister.A0);
					translatedOp.add(returnMov);
					translatedOp.add(new RISCVJALRInstruction(operandRa, true));
				}
				default -> throw new IllegalArgumentException();
			};

			//We register where the result is stored
			if (op.getResult() != null) {
				if (op instanceof IRFunctionCallInstruction) {
					//Different for calls !
				}
				else {
					valueToRegisterMap.put(op.getResult(), translatedOp.getLast().getResult());
				}
				
			}
			res.addAll(translatedOp);
		}	
		return res;
	}
	
	public void assignBackendRegisters() {
		for (RISCVOperand operand : operandsToSet.keySet()) {
			switch (operand) {
				case RISCVRegisterOperand r -> r.register = valueToRegisterMap.get(operandsToSet.get(operand));
				case RISCVMemoryOperand m -> m.base = valueToRegisterMap.get(operandsToSet.get(operand));
				default -> throw new IllegalArgumentException();
			}
		}
		operandsToSet.clear();
	}

}
