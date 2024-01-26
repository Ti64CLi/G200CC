package ir.importExport;

import java.util.ArrayList;
import java.util.HashMap;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;

import antlr.IRTXTBaseVisitor;
import antlr.IRTXTParser;
import antlr.IRTXTParser.FunctionArgumentContext;
import antlr.IRTXTParser.IrBlockContext;
import antlr.IRTXTParser.IrFunctionContext;
import antlr.IRTXTParser.IrOperationContext;
import antlr.IRTXTParser.IrTypeContext;
import ir.core.IRBlock;
import ir.core.IRFunction;
import ir.core.IRObject;
import ir.core.IROperation;
import ir.core.IRPhiOperation;
import ir.core.IRTopLevel;
import ir.core.IRType;
import ir.core.IRValue;
import ir.instruction.IRAddInstruction;
import ir.instruction.IRCompareGtInstruction;
import ir.instruction.IRCompareLtInstruction;
import ir.instruction.IRConstantInstruction;
import ir.instruction.IRDivInstruction;
import ir.instruction.IRFunctionCallInstruction;
import ir.instruction.IRLoadInstruction;
import ir.instruction.IRMulInstruction;
import ir.instruction.IRStoreInstruction;
import ir.instruction.IRSubInstruction;
import ir.terminator.IRCondBr;
import ir.terminator.IRGoto;
import ir.terminator.IRReturn;
import ir.terminator.IRTerminator;

public class IRImport extends IRTXTBaseVisitor<IRObject> {

	
	public IRImport() {
		this.idToBlockMap = new HashMap<String, IRBlock>();
		this.idToFuncMap = new HashMap<String, IRFunction>();
		this.idToValueMap = new HashMap<String, IRValue>();
	}
	
	/*******************************************************************************************
	 * Symbol tables
	 *
	 *******************************************************************************************/
	
	private HashMap<String, IRFunction> idToFuncMap; 
	private HashMap<String, IRBlock> idToBlockMap; 
	private HashMap<String, IRValue> idToValueMap; 
	
	private IRValue getValueFromId(String id) {
		if (!idToValueMap.containsKey(id)) {
			//Value has never been seen before (happen in phi...) we create one;
			idToValueMap.put(id, new IRValue(null, null));
		}
		return idToValueMap.get(id);
	}

	private IRFunction getFunctionFromId(String id) {
		if (!idToFuncMap.containsKey(id)) {
			throw new IllegalArgumentException("Function " + id + " is used before being defined");
		}
		return idToFuncMap.get(id);
	}
	
	private IRBlock getBlockFromId(String id) {
		if (!idToBlockMap.containsKey(id)) {
			throw new IllegalArgumentException("Block " + id + " is used before being defined");
		}
		return idToBlockMap.get(id);
	}
	
	/*******************************************************************************************
	 * Function to import types
	 *
	 *******************************************************************************************/
	
	public IRType visitIrType(IrTypeContext ctx) {
		return switch(ctx) {
			case IRTXTParser.VoidTypeContext st -> null;
			case IRTXTParser.IRINTContext st -> IRType.INT;
			case IRTXTParser.IRUINTContext st -> IRType.UINT;
			case IRTXTParser.IRCHARContext st -> IRType.CHAR;
			case IRTXTParser.IRFLOATContext st -> IRType.FLOAT;
			case IRTXTParser.IRADDRContext st -> IRType.ADDR;
			default -> throw new IllegalArgumentException("Unexpected value: " + ctx);
		};
	}
	
	
	/*******************************************************************************************
	 * Functions to import top, function and blocks
	 *
	 *******************************************************************************************/

	@Override
	public IRTopLevel visitIrTop(IRTXTParser.IrTopContext ctx) {
		IRTopLevel top = new IRTopLevel();
		
		//We register lib functions
		for (IRFunction f : top.getFunctions()) {
			this.idToFuncMap.put(f.getName(), f);
		}
		
		for (ParseTree c : ctx.children) {
			top.addFunction(visitIrFunction((IrFunctionContext) c));
		}
		return top;
	}

	@Override
	public IRFunction visitIrFunction(IRTXTParser.IrFunctionContext ctx) {
		
		//For each function we clear values and blocks maps
		idToBlockMap.clear();
		idToValueMap.clear();
		
		ArrayList<IRValue> args = new ArrayList<IRValue>();
		ArrayList<IRType> argTypes = new ArrayList<IRType>();

		for (FunctionArgumentContext oneArg : ctx.args) {
			argTypes.add(visitIrType(oneArg.type));
		}
		
		IRFunction result = new IRFunction(ctx.name.getText(), IRType.CHAR, argTypes);
		
		//We register the function
		this.idToFuncMap.put(ctx.name.getText(), result);
		
		//We create and register all blocks to ease the creation of terminator instructions
		for (IrBlockContext blockCtx : ctx.blocks) {
			this.idToBlockMap.put(blockCtx.id.getText(), result.addBlock());
		}
		
		//We register the argument values
		for (int argIndex = 0; argIndex < result.getArgs().size(); argIndex++) {
			this.idToValueMap.put(ctx.args.get(argIndex).id.getText(), result.getArgs().get(argIndex));
		}
		
		for (IrBlockContext blockCtx : ctx.blocks) {
			visitIrBlock(blockCtx);
		}
		
		return result;
	}

	@Override
	public IRValue visitFunctionArgument(IRTXTParser.FunctionArgumentContext ctx) {
		return new IRValue(this.visitIrType(ctx.type), null);
	}
	

	@Override
	public IRBlock visitIrBlock(IRTXTParser.IrBlockContext ctx) {
		IRBlock result = getBlockFromId(ctx.id.getText());
		
		for (IrOperationContext op : ctx.operations) {
			result.addOperation(visitIrOperation(op));
		}
		result.addTerminator(visitTerminator(ctx.terminator));
		
		return result;
	}
	
	/*******************************************************************************************
	 * Functions to import operations
	 *
	 *******************************************************************************************/

	public IROperation visitIrOperation(IRTXTParser.IrOperationContext ctx) {
		return switch (ctx) {
			case IRTXTParser.IRCommonOperationContext o -> visitIRCommonOperation(o);
			case IRTXTParser.IRConstantOperationContext o -> visitIRConstantOperation(o);
			case IRTXTParser.IRCallOperationContext o -> visitIRCallOperation(o);
			case IRTXTParser.IRStoreOperationContext o -> visitIRStoreOperation(o);
			default -> throw new IllegalArgumentException("Unexpected value: " + ctx);
		};
	}
	
	@Override
	public IROperation visitIRCommonOperation(IRTXTParser.IRCommonOperationContext ctx) {
		IROperation res = null;
		switch(ctx.op.getText()) {
			case "add" -> res = new IRAddInstruction(getValueFromId(ctx.operands.get(0).getText()), 
					getValueFromId(ctx.operands.get(1).getText()));
			case "gt" -> res = new IRCompareGtInstruction(getValueFromId(ctx.operands.get(0).getText()), 
					getValueFromId(ctx.operands.get(1).getText()));
			case "lt" -> res = new IRCompareLtInstruction(getValueFromId(ctx.operands.get(0).getText()), 
					getValueFromId(ctx.operands.get(1).getText()));
			case "div" -> res = new IRDivInstruction(getValueFromId(ctx.operands.get(0).getText()), 
					getValueFromId(ctx.operands.get(1).getText()));
			case "load" -> res = new IRLoadInstruction(getValueFromId(ctx.operands.get(0).getText()), 
					visitIrType(ctx.type));
			case "mul" -> res = new IRMulInstruction(getValueFromId(ctx.operands.get(0).getText()), 
					getValueFromId(ctx.operands.get(1).getText()));
			case "sub" -> res = new IRSubInstruction(getValueFromId(ctx.operands.get(0).getText()), 
					getValueFromId(ctx.operands.get(1).getText()));
			case "phi" -> { 
				 res = new IRPhiOperation(visitIrType(ctx.type));
				for (Token operand : ctx.operands)
					res.addOperand(getValueFromId(operand.getText()));
			}
			default -> throw new IllegalArgumentException("Unexpected value: " + ctx.op.getText());
		};
		
		//We register the value created if any
		if (idToValueMap.containsKey(ctx.res.getText())) {
			//The key has already been seen, we used the already created
			res.setResult(idToValueMap.get(ctx.res.getText()));
			res.getResult().type = visitIrType(ctx.type);
			res.getResult().definingOperation = res;
		}
		else {
			idToValueMap.put(ctx.res.getText(), res.getResult());
		}
		
		return res;
	}

	@Override
	public IROperation visitIRConstantOperation(IRTXTParser.IRConstantOperationContext ctx) {
		IROperation res = new IRConstantInstruction<Integer>(visitIrType(ctx.type), Integer.parseInt(ctx.val.getText()));
		
		//We register the value
		idToValueMap.put(ctx.res.getText(), res.getResult());
		return res;
	}
	
	@Override
	public IROperation visitIRStoreOperation(IRTXTParser.IRStoreOperationContext ctx) {
		return new IRStoreInstruction(getValueFromId(ctx.dest.getText()), getValueFromId(ctx.val.getText()));
	}

	@Override
	public IROperation visitIRCallOperation(IRTXTParser.IRCallOperationContext ctx) {
		ArrayList<IRValue> args = new ArrayList<IRValue>();
		for (Token a : ctx.operands) {
			args.add(getValueFromId(a.getText()));
		}
		IROperation res = new IRFunctionCallInstruction(getFunctionFromId(ctx.func.getText()), visitIrType(ctx.type), args);
		
		//We register the value
		idToValueMap.put(ctx.res.getText(), res.getResult());
		return res;
	}
	
	/*******************************************************************************************
	 * Functions to import terminators
	 *
	 *******************************************************************************************/

	
	public IRTerminator visitTerminator(IRTXTParser.IrTerminatorContext ctx) {
		return switch (ctx) {
			case IRTXTParser.IRCondBrContext t -> visitIRCondBr(t);
			case IRTXTParser.IRGotoContext t -> visitIRGoto(t);
			case IRTXTParser.IRReturnContext t -> visitIRReturn(t);
			default -> throw new IllegalArgumentException("Unexpected value: " + ctx);
		};
	}
	
	@Override
	public IRTerminator visitIRCondBr(IRTXTParser.IRCondBrContext ctx) {
		return new IRCondBr(getValueFromId(ctx.val.getText()), getBlockFromId(ctx.dests.get(0).getText()), 
				getBlockFromId(ctx.dests.get(1).getText()));
	}

	@Override
	public IRTerminator visitIRGoto(IRTXTParser.IRGotoContext ctx) {
		return new IRGoto(getBlockFromId(ctx.dest.getText()));
	}

	@Override
	public IRTerminator visitIRReturn(IRTXTParser.IRReturnContext ctx) {
		return new IRReturn(getValueFromId(ctx.val.getText()));
	}

}
