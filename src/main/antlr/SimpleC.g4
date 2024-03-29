grammar SimpleC;

@header {
    package antlr;
}

IDENTIFIER: [a-zA-Z]+ [0-9a-zA-Z]*;
INTEGER: '-'? [0-9]+;

WS: ( ' ' | '\t' | '\r' '\n' | '\n') -> skip;

translationUnit: functionDefinition+;

functionDefinition:
	returnType = type name = IDENTIFIER '(' (
		args += functionArgument ','
	)* args += functionArgument? ')' body = blockStatement;

functionArgument:
	argType = type name = IDENTIFIER ('[' size = INTEGER ']')?;

type:
	'void'				# VoidType
	| 'int'				# IntType
	| 'unsigned int'	# UintType;

statement:
	blockStatement
	| controlStatement
	| expressionStatement
	| returnStatement;

blockStatement: '{' statements += statement* '}';

//TODO: add statements for var def/var decl/var assign/if/for/while

returnStatement: 'return' expr = expression? ';';

expressionStatement: expr = expression ';';

expression:
	expr1 = expression '+' expr2 = expression		# AddExpr
	| expr1 = expression '-' expr2 = expression		# SubExpr
	| expr1 = expression '*' expr2 = expression		# MulExpr
	| expr1 = expression '/' expr2 = expression		# DivExpr
	| expr1 = expression '<' expr2 = expression		# CmpLtExpr
	| expr1 = expression '>' expr2 = expression		# CmpGtExpr
	| expr1 = expression '==' expr2 = expression	# EqExpr
	| expr1 = expression '!=' expr2 = expression	# NEqExpr
	| '-' expr1 = expression						# OppExpr
	| '(' expr1 = expression ')'					# ExprNode
	| name = IDENTIFIER								# IdNode
	| functionCall									# functionCallExpr
	| INTEGER										# IntNode
	| variableDeclaration							# VarDeclExpr
	| variableAssignation							# VarAssignExpr
	| variableDefinition							# VarDefExpr;

functionCall:
	name = IDENTIFIER '(' (args += expression ',')* args += expression? ')';

// TODO : support multiple variable declaration and definition
variableDeclaration: variableType = type id = IDENTIFIER;
variableDefinition:
	variableType = type id = IDENTIFIER '=' expr = expression;
variableAssignation: id = IDENTIFIER '=' expr = expression;

controlStatement: ifStatement | forStatement | whileStatement;

ifStatement:
	'if' '(' condExpr = expression ')' thenBody = blockStatement else = elseStatement?;
elseStatement: 'else' elseBody = blockStatement;

forStatement:
	'for' '(' initExpr = expression ';' condExpr = expression ';' incrExpr = expression ')' forBody
		= blockStatement;

whileStatement:
	'while' '(' condExpr = expression ')' whileBody = blockStatement;