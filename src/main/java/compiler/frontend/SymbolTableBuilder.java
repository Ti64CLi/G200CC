package compiler.frontend;

import java.lang.Void;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

import antlr.SimpleCBaseVisitor;
import antlr.SimpleCParser;
import antlr.SimpleCParser.*;
import compiler.frontend.SymbolTable;
import ir.core.IROperation;
import ir.core.IRType;
import ir.core.IRValue;

public class SymbolTableBuilder extends SimpleCBaseVisitor<Void> {
	protected SymbolTable symbolTable;

	/**
	 * SymbolTableBuilder constructor
	 */
	public SymbolTableBuilder() {
		this.symbolTable = new SymbolTable();
	}

	public void buildSymbolTable(ParseTree tree) {
		SymbolTableBuilder builder = new SymbolTableBuilder();
		builder.visit(tree);

		// TODO : manage errors
	}

	IRType translateType(TypeContext type) {
		if (type instanceof IntTypeContext) {
			return IRType.INT;
		} else if (type instanceof UintTypeContext) {
			return IRType.UINT;
		} else if (type instanceof VoidTypeContext) {
			return IRType.VOID;
		}

		throw new Error("[ERROR] Unknown type '" + type.getText() + "'\n");
	}

	@Override
	public Void visitFunctionDefinition(FunctionDefinitionContext ctx) {
		this.symbolTable.insert(ctx.name.getText(), new IRValue(this.translateType(ctx.returnType), null), true);

		for (FunctionArgumentContext argument : ctx.args) {
			visit(argument);
		}

		visit(ctx.body);

		return null;
	}

	@Override
	public Void visitFunctionArgument(FunctionArgumentContext ctx) {
		this.symbolTable.insert(ctx.name.getText(), new IRValue(this.translateType(ctx.argType), null), false);

		return null;
	}

	@Override
	public Void visitBlockStatement(BlockStatementContext ctx) {
		this.symbolTable.initializeScope(ctx);

		for (StatementContext statement : ctx.statements) {
			visit(statement);
		}

		this.symbolTable.finalizeScope();

		return null;
	}

	@Override
	public Void visitReturnStatement(ReturnStatementContext ctx) {
		visit(ctx.expr);

		return null;
	}

	@Override
	public Void visitFunctionCall(FunctionCallContext ctx) {
		if (this.symbolTable.lookup(ctx.name.getText()) == null) {
			// TODO : manage errors
		}

		for (ExpressionContext arg : ctx.args) {
			visit(arg);
		}

		return null;
	}

	@Override
	public Void visitVariableDeclaration(VariableDeclarationContext ctx) {
		// TODO : manage errors/warnings
		this.symbolTable.insert(ctx.id.getText(), new IRValue(translateType(ctx.variableType), null), false);

		return null;
	}

	@Override
	public Void visitVariableDefinition(VariableDefinitionContext ctx) {
		// TODO : manage errors/warnings
		this.symbolTable.insert(ctx.id.getText(), new IRValue(translateType(ctx.variableType), null), false);

		visit(ctx.expr);

		return null;
	}

	@Override
	public Void visitVariableAssignation(VariableAssignationContext ctx) {
		if (this.symbolTable.lookup(ctx.id.getText()) == null) {
			// TODO : manage errors
		}

		visit(ctx.expr);

		return null;
	}

	@Override
	public Void visitIfStatement(IfStatementContext ctx) {
		visit(ctx.condExpr);
		visit(ctx.thenBody);
		visit(ctx.else_);

		return null;
	}

	@Override
	public Void visitElseStatement(ElseStatementContext ctx) {
		visit(ctx.elseBody);

		return null;
	}

	@Override
	public Void visitForStatement(ForStatementContext ctx) {
		visit(ctx.initExpr);
		visit(ctx.condExpr);
		visit(ctx.incrExpr);

		visit(ctx.forBody);

		return null;
	}

	@Override
	public Void visitWhileStatement(WhileStatementContext ctx) {
		visit(ctx.condExpr);
		visit(ctx.whileBody);

		return null;
	}
}
