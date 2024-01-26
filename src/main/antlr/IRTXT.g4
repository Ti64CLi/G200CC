grammar IRTXT;

@header {
    package antlr;
}

IDENTIFIER : [a-zA-Z]+ [0-9a-zA-Z_]*;
IRVALUE : '%'[0-9]+;
INTEGER : '-'? [0-9]+;

WS  :   ( ' ' | '\t' | '\r' '\n' | '\n' ) -> skip;

irType : 'void'  	#VoidType
	| 'INT' 	#IRINT
	| 'UINT'  	#IRUINT
	| 'CHAR'    #IRCHAR
	| 'FLOAT'    #IRFLOAT
	| 'ADDR'    #IRADDR;
	
irTop : 
	irFunction+;
	
irFunction : 
	'function' name=IDENTIFIER '(' (args+=functionArgument ',')* args+=functionArgument? ')' ':' retType = irType '{' blocks+=irBlock+ '}';
	
functionArgument : 
	id=IRVALUE ':' type=irType;
	
irBlock : 
    id=IDENTIFIER '{' operations+=irOperation* terminator=irTerminator '}';
    
irOperation :
	  res=IRVALUE '=' op=IDENTIFIER (operands+=IRVALUE ',')* operands+=IRVALUE? ':' type=irType 	#IRCommonOperation
	| res=IRVALUE '=' val=INTEGER  ':' type=irType													#IRConstantOperation
	| res=IRVALUE '=' 'call' + func=IDENTIFIER (operands+=IRVALUE ',')* operands+=IRVALUE? ':' type=irType 	#IRCallOperation
	| 'store' val=IRVALUE dest=IRVALUE																#IRStoreOperation;

irTerminator : 
	'condbr' (val=IRVALUE)? '?' dests+=IDENTIFIER ':' dests+=IDENTIFIER #IRCondBr
	| 'goto' dest=IDENTIFIER											#IRGoto
	| 'return' val=IRVALUE												#IRReturn;
	
	
	