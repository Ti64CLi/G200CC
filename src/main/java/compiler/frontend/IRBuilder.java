package compiler.frontend;

import java.util.ArrayList;
import org.antlr.v4.runtime.tree.ParseTree;

import com.ibm.icu.impl.SimpleFilteredSentenceBreakIterator.Builder;

import antlr.SimpleCBaseVisitor;
import antlr.SimpleCParser;
import antlr.SimpleCParser.AddExprContext;
import antlr.SimpleCParser.BlockStatementContext;
import antlr.SimpleCParser.CmpGtExprContext;
import antlr.SimpleCParser.CmpLtExprContext;
import antlr.SimpleCParser.DivExprContext;
import antlr.SimpleCParser.DoWhileStatementContext;
import antlr.SimpleCParser.ExprNodeContext;
import antlr.SimpleCParser.ExpressionContext;
import antlr.SimpleCParser.ForStatementContext;
import antlr.SimpleCParser.FunctionArgumentContext;
import antlr.SimpleCParser.FunctionCallContext;
import antlr.SimpleCParser.FunctionDefinitionContext;
import antlr.SimpleCParser.IdNodeContext;
import antlr.SimpleCParser.IfStatementContext;
import antlr.SimpleCParser.IntNodeContext;
import antlr.SimpleCParser.IntTypeContext;
import antlr.SimpleCParser.MulExprContext;
import antlr.SimpleCParser.OppExprContext;
import antlr.SimpleCParser.ReturnStatementContext;
import antlr.SimpleCParser.StatementContext;
import antlr.SimpleCParser.SubExprContext;
import antlr.SimpleCParser.TypeContext;
import antlr.SimpleCParser.UintTypeContext;
import antlr.SimpleCParser.VariableAssignationContext;
import antlr.SimpleCParser.VariableDeclarationContext;
import antlr.SimpleCParser.VariableDefinitionContext;
import antlr.SimpleCParser.VoidTypeContext;
import antlr.SimpleCParser.WhileStatementContext;
import ir.core.IRBlock;
import ir.core.IRFunction;
import ir.core.IRTopLevel;
import ir.core.IRType;
import ir.core.IRValue;
import ir.instruction.IRAddInstruction;
import ir.instruction.IRCompareGtInstruction;
import ir.instruction.IRCompareLtInstruction;
import ir.instruction.IRConstantInstruction;
import ir.instruction.IRDivInstruction;
import ir.instruction.IRFunctionCallInstruction;
import ir.instruction.IRMulInstruction;
import ir.instruction.IRSubInstruction;
import ir.terminator.IRCondBr;
import ir.terminator.IRGoto;
import ir.terminator.IRReturn;

public class IRBuilder extends SimpleCBaseVisitor<BuilderResult> {

	IRTopLevel top;
	IRFunction currentFunction = null;
	IRBlock currentBlock = null;
	SymbolTable symbolTable;

	public static IRTopLevel buildTopLevel(ParseTree t) {
		IRBuilder builder = new IRBuilder();
		builder.visit(t);
		return builder.top;
	}

	public IRBuilder() {
		top = new IRTopLevel();
		symbolTable = new SymbolTable();
	}

	IRType translateType(TypeContext t) {
		if (t instanceof IntTypeContext) {
			return IRType.INT;
		} else if (t instanceof UintTypeContext) {
			return IRType.UINT;
		} else if (t instanceof VoidTypeContext) {
			return IRType.VOID;
		}
		return null;
	}

	@Override
	public BuilderResult visitFunctionDefinition(FunctionDefinitionContext ctx) {
		// Add function to the symbol table
		this.symbolTable.insert(ctx.name.getText(), new IRValue(translateType(ctx.returnType), null), true);
		// We build the list of arg types
		ArrayList<IRType> argTypes = new ArrayList<IRType>();
		for (FunctionArgumentContext a : ctx.args) {
			argTypes.add(translateType(a.argType));
			// Add arg to the symbol table
			this.symbolTable.insert(a.name.getText(), new IRValue(translateType(a.argType), null), false);
		}

		// We instantiate a new function and add it in the toplevel
		IRFunction func = new IRFunction(ctx.name.getText(), translateType(ctx.returnType), argTypes);
		top.addFunction(func);

		// We mark the newly created function as currentFunction : blocks will be added
		// inside
		currentFunction = func;
		IRBlock entryBlock = createBlock(func);

		// Recursive call to the body to get its IR
		BuilderResult body = visitBlockStatement(ctx.body);

		// We connect the result with the entry block and seal the body
		entryBlock.addTerminator(new IRGoto(body.entry));

		// Don't care about the value returned
		return null;
	}

	@Override
	public BuilderResult visitStatement(StatementContext ctx) {
		return this.visit(ctx.children.get(0));
	}

	@Override
	public BuilderResult visitBlockStatement(BlockStatementContext ctx) {
		this.symbolTable.initializeScope(ctx);

		// We create a new block, save it as in point and current point
		IRBlock in = createBlock(currentFunction);
		IRBlock current = in;
		currentBlock = current;

		// Recursive call for each child
		for (StatementContext s : ctx.statements) {
			BuilderResult r = visit(s);
			if (r.hasBlock) {
				// We have to insert blocks from recursive call
				current.addTerminator(new IRGoto(r.entry));
				current = r.exit;
				currentBlock = current;
			}
		}

		this.symbolTable.finalizeScope();

		return new BuilderResult(true, in, current, null);
	}

	/****************************************************************************
	 * Control flow statements
	 * 
	 ****************************************************************************/

	@Override
	public BuilderResult visitIfStatement(IfStatementContext ctx) {
		IRBlock in = createBlock(currentFunction);
		IRBlock out = createBlock(currentFunction);

		currentBlock = in;

		BuilderResult condResult = this.visit(ctx.condExpr);
		if (condResult.hasBlock) {
			currentBlock.addTerminator(new IRGoto(condResult.entry));
			currentBlock = condResult.exit;
		}

		BuilderResult thenBodyResult = this.visit(ctx.thenBody);
		BuilderResult elseBodyResult = this.visit(ctx.else_);

		currentBlock.addTerminator(new IRCondBr(condResult.value, thenBodyResult.entry, elseBodyResult.entry));
		thenBodyResult.exit.addTerminator(new IRGoto(out)); // TODO : Add support for non-block statement then and else
															// later
		elseBodyResult.exit.addTerminator(new IRGoto(out));

		currentBlock = out;

		return new BuilderResult(true, in, currentBlock, null);
	}

	@Override
	public BuilderResult visitForStatement(ForStatementContext ctx) {
		IRBlock in = createBlock(currentFunction);
		IRBlock out = createBlock(currentFunction);

		currentBlock = in;

		BuilderResult initResult = this.visit(ctx.initExpr);
		if (initResult.hasBlock) {
			currentBlock.addTerminator(new IRGoto(initResult.entry));
			currentBlock = initResult.exit;
		}

		BuilderResult condResult = this.visit(ctx.condExpr);
		if (condResult.hasBlock) {
			currentBlock.addTerminator(new IRGoto(condResult.entry));
			currentBlock = condResult.exit;
		}

		IRBlock condExitBlock = currentBlock;

		BuilderResult forBodyResult = this.visit(ctx.forBody); // TODO : Add support for non-block statement for
		currentBlock = forBodyResult.exit;

		BuilderResult incrResult = this.visit(ctx.incrExpr);
		if (incrResult.hasBlock) {
			currentBlock.addTerminator(new IRGoto(incrResult.entry));
			currentBlock = incrResult.exit;
		}

		condExitBlock.addTerminator(new IRCondBr(condResult.value, forBodyResult.entry, out));

		currentBlock = out;

		return new BuilderResult(true, in, currentBlock, null);
	}

	@Override
	public BuilderResult visitWhileStatement(WhileStatementContext ctx) {
		IRBlock in = createBlock(currentFunction);
		IRBlock out = createBlock(currentFunction);

		currentBlock = in;

		BuilderResult condResult = this.visit(ctx.condExpr);
		if (condResult.hasBlock) {
			currentBlock.addTerminator(new IRGoto(condResult.entry));
			currentBlock = condResult.exit;
		}

		BuilderResult whileBodyResult = this.visit(ctx.whileBody); // TODO : Add support for non-block statement while
		currentBlock.addTerminator(new IRCondBr(condResult.value, whileBodyResult.entry, out));

		currentBlock = out;

		return new BuilderResult(true, in, currentBlock, null);
	}

	@Override
	public BuilderResult visitDoWhileStatement(DoWhileStatementContext ctx) {
		return null;
	}

	/****************************************************************************
	 * Return/call statements
	 * 
	 ****************************************************************************/

	@Override
	public BuilderResult visitReturnStatement(ReturnStatementContext ctx) {
		BuilderResult res = this.visit(ctx.expr);
		IRReturn newInstr = new IRReturn(res.value);
		currentBlock.addOperation(newInstr);
		return new BuilderResult(false, null, null, null);
	}

	@Override
	public BuilderResult visitFunctionCall(FunctionCallContext ctx) {
		// TODO : check existing values
		// We gather arg values
		ArrayList<IRValue> args = new ArrayList<IRValue>();
		for (ExpressionContext a : ctx.args) {
			BuilderResult res = this.visit(a);
			assert (res.value != null);
			args.add(res.value);
		}

		IRType returnType = IRType.UINT;
		IRFunction func = null;
		for (IRFunction f : top.getFunctions()) {
			if (f.getName().equals(ctx.name.getText())) {
				returnType = f.getReturnType();
				func = f;
			}
		}
		IRFunctionCallInstruction funcCall = new IRFunctionCallInstruction(func, returnType, args);
		currentBlock.addOperation(funcCall);

		return new BuilderResult(false, null, null, funcCall.getResult());
	}

	/****************************************************************************
	 * Non control flow statements
	 * 
	 ****************************************************************************/

	// TODO: varDecl / varDef / varAssign
	@Override
	public BuilderResult visitVariableDeclaration(VariableDeclarationContext ctx) {
		this.symbolTable.insert(ctx.id.getText(), new IRValue(translateType(ctx.variableType), null), false);

		return new BuilderResult(false, null, null, null);
	}

	@Override
	public BuilderResult visitVariableDefinition(VariableDefinitionContext ctx) {
		BuilderResult res = ctx.expr.accept(this);
		IRValue varValue = res.value;
		varValue.type = translateType(ctx.type());
		this.symbolTable.insert(ctx.id.getText(), varValue, false);

		return new BuilderResult(false, null, null, null);
	}

	@Override
	public BuilderResult visitVariableAssignation(VariableAssignationContext ctx) {
		BuilderResult res = ctx.expr.accept(this);
		IRValue varValue = res.value;

		return new BuilderResult(false, null, null, varValue);
	}

	@Override
	public BuilderResult visitAddExpr(AddExprContext ctx) {
		BuilderResult res1 = ctx.expr1.accept(this);
		BuilderResult res2 = ctx.expr2.accept(this);

		IRAddInstruction instr = new IRAddInstruction(res1.value, res2.value);
		currentBlock.addOperation(instr);

		return new BuilderResult(false, null, null, instr.getResult());
	}

	@Override
	public BuilderResult visitSubExpr(SubExprContext ctx) {

		BuilderResult res1 = ctx.expr1.accept(this);
		BuilderResult res2 = ctx.expr2.accept(this);

		IRSubInstruction instr = new IRSubInstruction(res1.value, res2.value);
		currentBlock.addOperation(instr);

		return new BuilderResult(false, null, null, instr.getResult());
	}

	@Override
	public BuilderResult visitMulExpr(MulExprContext ctx) {

		BuilderResult res1 = ctx.expr1.accept(this);
		BuilderResult res2 = ctx.expr2.accept(this);

		IRMulInstruction instr = new IRMulInstruction(res1.value, res2.value);
		currentBlock.addOperation(instr);

		return new BuilderResult(false, null, null, instr.getResult());
	}

	@Override
	public BuilderResult visitDivExpr(DivExprContext ctx) {

		BuilderResult res1 = ctx.expr1.accept(this);
		BuilderResult res2 = ctx.expr2.accept(this);

		IRDivInstruction instr = new IRDivInstruction(res1.value, res2.value);
		currentBlock.addOperation(instr);

		return new BuilderResult(false, null, null, instr.getResult());
	}

	@Override
	public BuilderResult visitCmpGtExpr(CmpGtExprContext ctx) {

		BuilderResult res1 = ctx.expr1.accept(this);
		BuilderResult res2 = ctx.expr2.accept(this);

		IRCompareGtInstruction instr = new IRCompareGtInstruction(res1.value, res2.value);
		currentBlock.addOperation(instr);

		return new BuilderResult(false, null, null, instr.getResult());
	}

	@Override
	public BuilderResult visitCmpLtExpr(CmpLtExprContext ctx) {

		BuilderResult res1 = ctx.expr1.accept(this);
		BuilderResult res2 = ctx.expr2.accept(this);

		IRCompareLtInstruction instr = new IRCompareLtInstruction(res1.value, res2.value);
		currentBlock.addOperation(instr);

		return new BuilderResult(false, null, null, instr.getResult());
	}

	@Override
	public BuilderResult visitOppExpr(OppExprContext ctx) {

		BuilderResult res1 = ctx.expr1.accept(this);

		IRConstantInstruction<Integer> zeroCst = new IRConstantInstruction<Integer>(IRType.INT, 0);
		IRSubInstruction instr = new IRSubInstruction(zeroCst.getResult(), res1.value);
		currentBlock.addOperation(zeroCst);
		currentBlock.addOperation(instr);

		return new BuilderResult(false, null, null, instr.getResult());
	}

	@Override
	public BuilderResult visitExprNode(ExprNodeContext ctx) {

		BuilderResult res1 = ctx.expr1.accept(this);

		return new BuilderResult(false, null, null, res1.value);
	}

	@Override
	public BuilderResult visitIntNode(IntNodeContext ctx) {
		Integer val = Integer.parseInt(ctx.children.get(0).getText());
		IRConstantInstruction<Integer> instr = new IRConstantInstruction<Integer>(IRType.INT, val);
		currentBlock.addOperation(instr);

		return new BuilderResult(false, null, null, instr.getResult());
	}

	@Override
	public BuilderResult visitIdNode(IdNodeContext ctx) {
		// Key function for having SSA working properly
		SymbolTableEntry entry = symbolTable.lookup(ctx.name.getText());
		IRValue val = null; // TODO: find the correct value in SSA form

		return new BuilderResult(false, null, null, val);
	}

	@Override
	public BuilderResult visitExpressionStatement(SimpleCParser.ExpressionStatementContext ctx) {
		return visit(ctx.expr);
	}

	private IRBlock createBlock(IRFunction f) {
		return f.addBlock();
	}
}
