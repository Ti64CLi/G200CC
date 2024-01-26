package ir.importExport;

import java.util.Iterator;

import ir.core.IRBlock;
import ir.core.IRFunction;
import ir.core.IROperation;
import ir.core.IRPhiOperation;
import ir.core.IRTopLevel;
import ir.core.IRValue;
import ir.core.IRVisitableObject;
import ir.core.IRVisitor;
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

public class IRExport extends IRVisitor<String> {

	public static String printIR(IRTopLevel t) {
		IRExport printer = new IRExport();
		return printer.visitTopLevel(t);
	}
	
	@Override
	public String visitTopLevel(IRTopLevel t) {
		String res = "";
		for (IRFunction f : t.getFunctions()) {
			res += this.visitFunction(f) + "\n";
		}
		return res;
	}

	@Override
	public String visitFunction(IRFunction f) {
		
		//We do not print lib prototypes
		if (f.getBlocks().isEmpty()) {
			return "";
		}
		
		String result = "function " + f.getName() + " (";
		
		if (f.getArgs().size() > 0) {
			result += "%" + f.getArgs().get(0).getId() + " : " + f.getArgs().get(0).getType();
		}	
		for (int i = 1; i < f.getArgs().size(); i++) {
			result += ", %" + f.getArgs().get(i).getId() + " : " + f.getArgs().get(i).getType();
		}
		
		result += ") : ";
		result += f.getReturnType();
		result += " {\n";
		
		for (IRBlock oneBlock : f.getBlocks()) 
			result += "  block_" + f.getBlocks().indexOf(oneBlock) + " {\n" + this.visitBlock(oneBlock) + "  }\n";
		
		result += "\n}";
		return result;
	}

	@Override
	public String visitBlock(IRBlock b) {
		String result = "";
		for (IROperation o : b.getOperations()) {
			result += "    " + o.accept(this) + "\n";
		}
		return result;
	}

	private String getStringOperation(IROperation o, String opcode) {
		String result = "%" + o.getResult().getId() + " = " + opcode + " ";
		if (o.getOperands().size() > 0) 
			result += "%" + o.getOperands().get(0).getId();
		
		for (int i = 1; i< o.getOperands().size(); i++) 
			result += ", %" + o.getOperands().get(i).getId();
		
		result = result + " : " + o.getResult().getType();
		
		return result;	
	}
		
	@Override
	public String visitPhiOperation(IRPhiOperation p) {
		return getStringOperation(p, "phi");
	}

	@Override
	public String visitAddInstruction(IRAddInstruction i) {
		return getStringOperation(i, "add");
	}

	@Override
	public String visitSubInstruction(IRSubInstruction i) {
		return getStringOperation(i, "sub");
	}

	@Override
	public String visitMulInstruction(IRMulInstruction i) {
		return getStringOperation(i, "mul");
	}

	@Override
	public String visitDivInstruction(IRDivInstruction i) {
		return getStringOperation(i, "div");
	}

	@Override
	public String visitAllocaInstruction(IRAllocaInstruction i) {
		return getStringOperation(i, "alloca");
	}

	@Override
	public String visitGetElementPtrInstruction(IRGetElementPtrInstruction i) {
		return "todo getelementptr";
	}

	@Override
	public String visitLoadInstruction(IRLoadInstruction i) {
		return getStringOperation(i, "load");
	}

	@Override
	public String visitStoreInstruction(IRStoreInstruction i) {
		return "store " + i.getOperands();
	}

	@Override
	public String visitFunctionCall(IRFunctionCallInstruction i) {
		return getStringOperation(i, "call " + i.targetFunction.getName());
	}

	@Override
	public String visitConstantInstruction(IRConstantInstruction i) {
		return i.getResult() + " = " + i.getValue().toString() + " : " + i.getResult().getType();
	}

	@Override
	public String visitGotoTerminator(IRGoto g) {
		return "goto block_" + g.getSuccessors().get(0).getBlockIndexInContainingFunc();
	}

	@Override
	public String visitCondBr(IRCondBr b) {
		return "condbr  " + b.getOperands().get(0) + " ? block_" + b.getSuccessors().get(0).getBlockIndexInContainingFunc() + " : block_" + b.getSuccessors().get(1).getBlockIndexInContainingFunc();
	}

	@Override
	public String visitReturnTerminator(IRReturn r) {
		// TODO Auto-generated method stub
		return "return " + r.getOperands().get(0);
	}

	@Override
	public String visitCompareLtInstruction(IRCompareLtInstruction c) {
		return getStringOperation(c, "lt");
	}

	@Override
	public String visitCompareGtInstruction(IRCompareGtInstruction c) {
		return getStringOperation(c, "gt");

	}

}
