package compiler.frontend;

import org.antlr.v4.runtime.tree.ParseTree;

import antlr.SimpleCBaseVisitor;
import antlr.SimpleCParser;

public class SimpleCPrinter extends SimpleCBaseVisitor<String> {

	public String visitTranslationUnit(SimpleCParser.TranslationUnitContext ctx) {
		StringBuilder result = new StringBuilder();
		for (ParseTree c : ctx.children)
			result.append("\n").append(this.visit(c));
		return result.toString();
	}

	public String visitFunctionDefinition(SimpleCParser.FunctionDefinitionContext ctx) {
		StringBuilder result = new StringBuilder(this.visit(ctx.returnType) + " " + ctx.name.getText() + "(");

		if (!ctx.args.isEmpty()) {
			int num_args = ctx.args.size();
			for (ParseTree c : ctx.args.subList(0, num_args - 1))
				result.append(this.visit(c)).append(", ");
			result.append(this.visit(ctx.args.get(num_args - 1)));
		}
		result.append(")");

		return result + "\n" + this.visit(ctx.body) + "\n";

	}

	public String visitFunctionArgument(SimpleCParser.FunctionArgumentContext ctx) {
		String result = this.visit(ctx.argType) + " " + ctx.name.getText();
		if (ctx.size != null)
			result += "[" + ctx.size.getText() + "]";
		return result;
	}

	public String visitVoidType(SimpleCParser.VoidTypeContext ctx) {
		return "void";
	}

	public String visitIntType(SimpleCParser.IntTypeContext ctx) {
		return "int";
	}

	public String visitUintType(SimpleCParser.UintTypeContext ctx) {
		return "unsigned int";
	}

	@Override
	public String visitBlockStatement(SimpleCParser.BlockStatementContext ctx) {
		String result = "{\n";
		for (ParseTree child : ctx.statements) {
			result += visit(child);
		}
		return result + "}";
	}

	@Override
	public String visitReturnStatement(SimpleCParser.ReturnStatementContext ctx) {
		return "return " + visit(ctx.expr) + ";\n";
	}

	@Override
	public String visitExpressionStatement(SimpleCParser.ExpressionStatementContext ctx) {
		return visit(ctx.expr) + ";" ;
	}

	@Override
	public String visitExprNode(SimpleCParser.ExprNodeContext ctx) {
		return "(" + visit(ctx.expr1) + ")";
	}

	@Override
	public String visitMulExpr(SimpleCParser.MulExprContext ctx) {
		return visit(ctx.expr1) + " * " + visit(ctx.expr2);
	}

	@Override
	public String visitOppExpr(SimpleCParser.OppExprContext ctx) {
		return "-" +visit(ctx.expr1);
	}

	@Override
	public String visitIntNode(SimpleCParser.IntNodeContext ctx) {
		return ctx.getText();
	}

	@Override
	public String visitDivExpr(SimpleCParser.DivExprContext ctx) {
		return visit(ctx.expr1) + " / " + visit(ctx.expr2);
	}

	@Override
	public String visitCmpLtExpr(SimpleCParser.CmpLtExprContext ctx) {
		return visit(ctx.expr1) + " < " + visit(ctx.expr2);
	}

	@Override
	public String visitIdNode(SimpleCParser.IdNodeContext ctx) {
		return ctx.name.getText();
	}

	@Override
	public String visitCmpGtExpr(SimpleCParser.CmpGtExprContext ctx) {
		return visit(ctx.expr1) + " > " + visit(ctx.expr2);
	}

	@Override
	public String visitSubExpr(SimpleCParser.SubExprContext ctx) {
		return visit(ctx.expr1) + " - " + visit(ctx.expr2);
	}

	@Override
	public String visitAddExpr(SimpleCParser.AddExprContext ctx) {
		return visit(ctx.expr1) + " + " + visit(ctx.expr2);
	}

	@Override
	public String visitFunctionCall(SimpleCParser.FunctionCallContext ctx) {
		String result = ctx.name.getText() + "(";
		if (ctx.args.size() > 1){
			result += visit(ctx.args.get(0));
		}
		for (int child = 1; child < ctx.args.size(); child++) {
			result += ", " + visit(ctx.args.get(child));
		}
		return result + ")";
	}

}
