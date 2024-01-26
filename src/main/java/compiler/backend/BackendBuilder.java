package compiler.backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.antlr.v4.parse.ANTLRParser.throwsSpec_return;

import backend.BackendBlock;
import backend.BackendFunction;
import backend.BackendInstruction;
import backend.BackendInstructionOperand;
import backend.BackendTopLevel;
import backend.riscv.RISCVPhysicalRegister;
import backend.riscv.RISCVRegister;
import backend.riscv.instruction.RISCVADDIInstruction;
import backend.riscv.instruction.RISCVPhiInstruction;
import backend.riscv.operand.RISCVImmediateOperand;
import backend.riscv.operand.RISCVOperand;
import backend.riscv.operand.RISCVRegisterOperand;
import ir.core.IRBlock;
import ir.core.IRFunction;
import ir.core.IRTopLevel;
import ir.core.IRValue;

public class BackendBuilder {
		
	HashMap<IRBlock, BackendBlock> blockToBlockMap;
	HashMap<IRFunction, BackendFunction> funcToFuncMap;
	
	private InstructionSelector selector;
	private AbstractRegisterAllocator allocator;
	
	public static BackendTopLevel lower(IRTopLevel top) {
		BackendBuilder builder = new BackendBuilder();
		
		//Assign other classes doing the job
		builder.selector = new NaiveInstructionSelector(builder);
		builder.allocator = new NaiveRegisterAllocator(builder);
		
		builder.blockToBlockMap = new HashMap<IRBlock, BackendBlock>();
		builder.funcToFuncMap = new HashMap<IRFunction, BackendFunction>();
		
		//Perform different steps of the lowering
		//Phi disambiguation
		builder.phiDisambiguation(top);
		
		for (IRFunction f : top.getFunctions()) {
			builder.sortBlocks(f);
		}
		
		//Building the BackendIR for top, functions and blocks
		BackendTopLevel res = builder.lowerIRTop(top);
		
		//We build pred and succ for BackendBlocks
		for (IRBlock ib : builder.blockToBlockMap.keySet()) {
			BackendBlock correspondingBlock = builder.blockToBlockMap.get(ib);
			for (IRBlock pred : ib.getPredecessors()) {
				correspondingBlock.predecessors.add(builder.blockToBlockMap.get(pred));
			}
			for (IRBlock succ : ib.getSuccessors()) {
				correspondingBlock.successors.add(builder.blockToBlockMap.get(succ));
			}
		}
		
		//Instructions selection for each block
		for (IRFunction f : top.getFunctions()) {
			for (IRBlock b : f.getBlocks()) {
				List<BackendInstruction> backendInstrs = builder.selector.selectInstructions(b);
				for (BackendInstruction instr : backendInstrs) {
					builder.blockToBlockMap.get(b).addInstruction(instr);
				}
			}
		}
		builder.selector.assignBackendRegisters();
		
		//Allocating the registers
		builder.registerAllocation(top);
		builder.removePhi(res);
		
		
		System.out.println(res.toString());
		
		return res;
		
	}
	
	public BackendBuilder() {
		blockToBlockMap = new HashMap<IRBlock, BackendBlock>();
	}
	
	public BackendTopLevel lowerIRTop(IRTopLevel top) {
		BackendTopLevel res = new BackendTopLevel();
		
		for (IRFunction f : top.getFunctions()) {
			//We lower and insert all functions
			res.addFunction(lowerIRFunction(f));
		}

		return res;
	}
	
	private BackendFunction lowerIRFunction(IRFunction f) {		
		BackendFunction result = new BackendFunction(f.getName());

		//FIXME args ?
		for (int oneArg = 0; oneArg < f.getArgs().size(); oneArg++) {
			RISCVRegister reg = new RISCVRegister();
			reg.setPhysicalRegister(RISCVPhysicalRegister.getArgRegister(oneArg));
			selector.registerIRValueToOperandMapping(f.getArgs().get(oneArg), reg);
		}
		
		for (IRBlock b : f.getBlocks()) {
			result.addBlock(lowerIRBlock(b));
		}
		
		//We register func map
		funcToFuncMap.put(f, result);
		
		return result;
	}

	private BackendBlock lowerIRBlock(IRBlock b) {
		BackendBlock result = new BackendBlock();
		
		//We register the block, it will be used to build pred/succ later
		blockToBlockMap.put(b, result);
		
		return result;
	}
	
	public void sortBlocks(IRFunction f) {
		//Sort functions such that second dest of a condbr is always after the br
		if (f.getBlocks().isEmpty())
			return;
		
		//We sort blocks
		List<IRBlock> newOrder = new ArrayList<IRBlock>();
		depthFirstInsertion(newOrder, f.getBlocks().get(0));
		
		//We reinsert blocks in the correct order
		f.getBlocks().clear();
		f.getBlocks().addAll(newOrder);
			
		//We ensure blocks are in the correct order
		for (IRBlock oneBlock : newOrder) {
			if (oneBlock.getSuccessors().size() == 2) {
				IRBlock succWhichShouldBeAfter = oneBlock.getSuccessors().get(1);
				assert(succWhichShouldBeAfter.getBlockIndexInContainingFunc() != oneBlock.getBlockIndexInContainingFunc() + 1);
			}
			
		}
	
	}
	private void depthFirstInsertion(List<IRBlock> blocks, IRBlock blockToInsert) {
		if (!blocks.contains(blockToInsert)) {
			blocks.add(blockToInsert);
			for (int oneSucc = blockToInsert.getSuccessors().size() - 1; oneSucc >= 0; oneSucc--) {
				depthFirstInsertion(blocks, blockToInsert.getSuccessors().get(oneSucc));
			}
		}
	}
	
	public void phiDisambiguation(IRTopLevel top) {

	}
	
	public void registerAllocation(IRTopLevel top) {
		for (IRFunction f : top.getFunctions()) {
			BackendFunction bf = funcToFuncMap.get(f);
			allocator.allocateRegisters(bf, f.getArgs().size());
			allocator.insertABIInstructions(f);
		}
	}
	
	public void removePhi(BackendTopLevel top) {
		for (BackendFunction f : top.getFunctions()) {
			for (BackendBlock b : f.getBlocks()) {
				//We copy the list to be able to iterate and remove elements on it
				ArrayList<BackendInstruction> originalInstrs = new ArrayList<BackendInstruction>( b.getInstructions());
				for (BackendInstruction i : originalInstrs) {
					if (i instanceof RISCVPhiInstruction) {
						//We have a phi instruction to remove : naive implementation consists in adding move in each pred
						for (int oneOperand = 0; oneOperand < i.getOperands().size(); oneOperand++){
							RISCVRegister reg = ((RISCVRegisterOperand) i.getOperands().get(oneOperand)).register;
							BackendBlock pred = b.getPredecessors().get(oneOperand);
							RISCVADDIInstruction mov = new RISCVADDIInstruction(new RISCVRegisterOperand(reg), new RISCVImmediateOperand(0));
							mov.getResult().setPhysicalRegister(((RISCVPhiInstruction) i).getResult().getPhysicalRegister());
							pred.addInstruction(mov, pred.getInstructions().size()-1);
							b.getInstructions().remove(i);
						}

					}
				}
			}
		}
	}
}
