# Travaux pratiques GCC

## Partie 1 : Implémentation du front-end

L'objectif de cette première partie est de se familiariser avec la grammaire de notre langage et avec l'AST qui sera généré. Pour ce faire, vous avez à disposition une grammaire très restreinte que vous devrez enrichir. 
Dans un second temps, vous devrez mettre en place une analyse sémantique simple qui consiste à vérifier que toutes les variables utilisées dans votre code ont bien été définies préalablement.

### Enrichissement de la grammaire

La grammaire de base utilisée dans le projet (donnée au format ANTLR) est la suivante : 

```
grammar SimpleC;

@header {
    package antlr;
}

IDENTIFIER : [a-zA-Z]+ [0-9a-zA-Z]*;
INTEGER : '-'? [0-9]+;

WS  :   ( ' ' | '\t' | '\r' '\n' | '\n' ) -> skip;



translationUnit : functionDefinition+;

functionDefinition : returnType=type name=IDENTIFIER '(' (args+=functionArgument ',')* args+=functionArgument? ')' body=blockStatement ;

functionArgument : argType=type name=IDENTIFIER ('[' size=INTEGER ']')?;

type : 'void'  	#VoidType
	| 'int' 	#IntType
	| 'unsigned int'  	#UintType;

statement : blockStatement | expressionStatement | returnStatement;

blockStatement : '{' statements+=statement* '}';


//TODO: add statements for var def/var decl/var assign/if/for/while


returnStatement : 'return' expr=expression?';';

expressionStatement : expr=expression ';';

expression : expr1=expression '+' expr2=expression  #AddExpr
		| expr1=expression '-' expr2=expression 	#SubExpr 
		| expr1=expression '*' expr2=expression 	#MulExpr 
		| expr1=expression '/' expr2=expression 	#DivExpr
		| expr1=expression '<' expr2=expression 	#CmpLtExpr
		| expr1=expression '>' expr2=expression 	#CmpGtExpr		
		|'-' expr1=expression					#OppExpr
	    | '(' expr1=expression ')' 		    #ExprNode
		| name=IDENTIFIER 					#IdNode
		| functionCall                  #functionCallExpr
		| INTEGER 						#IntNode;
		
           
functionCall : name=IDENTIFIER '(' (args+=expression ',')* args+=expression? ')';
```

La syntaxe de description d'une grammaire ANTLR4 est simple. Considérons l'exemple suivant permettant de définir des fonctions : 

```
functionDefinition : returnType=type name=IDENTIFIER '(' (args+=functionArgument ',')* args+=functionArgument? ')' body=blockStatement ;
```

Sur cet exemple, il est important de noter : 
- Un symbole non terminal de la grammaire doit commencer par une minuscule (les majuscules sont associées aux règles de lexer). 
- Il est possible de nommer les occurrences d'autres symboles. Dans l'exemple précédent, il sera possible d'accéder aux champs `returnType` du noeud créé pour obtenir le noeud créé par la règle `type`. L'utilisation de cette fonctionnalité nous permet de nous rapprocher de la structure d'un AST directement dans la grammaire.
- Il est possible d'utiliser des notations de regex directement dans les productions de la grammaire (par exemple l'opérateur *)
- Il est possible de créer des listes en utilisant l'opérateur `+=`

La production suivante permet de générer les types d'une variable ou le type de retour d'une fonction :

```
type : 'void'          #VoidType
| 'int'             #IntType
| 'unsigned int'    #UintType;

```
La syntaxe ANTLR4 permet de choisir le type du noeud qui va être créé en fonction de l'alternative qui va être utilisée dans la production. Dans l'exemple précédent, si l'outil parse un `void`, il va créer un noeud de type `VoidType`. Vous avez ainsi plus de contrôle sur l'arbre que vous créez, sans avoir besoin de définir de nombreux symboles non terminaux et d'augmenter le nombre de productions.

Pour générer le parser, vous devez passer par `gradle`. Dans le dossier racine du projet, entrez la commande : 

```
gradle build
```

La commande doit générer les fichiers java à partir de la description de la grammaire puis compiler tout votre projet. **Il est important de bien relancer le gradle quand vous modifiez le fichier de grammaire pour que les fichiers soient regénérés**.

**Question 1 :** Complétez la grammaire en ajoutant les différents *statements* dont vous aurez besoin. Il faut à minima pouvoir parser les fichiers `.sc` utilisés pour les tests. Il faudra donc ajouter les `if`, les `for`, les `while` et les définition, déclaration et assignation de variables.
 
**/!\ Attention :** essayez de ne pas trop changer le reste de la structure de la grammaire pour ne pas avoir à changer le code existant. Vous pourrez les faire si vous voulez lorsque vous comprendrez mieux les différents morceaux du compilateur existant. Le langage d'entrée est très restrictif et loin d'être pratique, mais ce n'est pas grave...

**Question 2 :** Complétez le fichier `SimpleCPrinter.java` qui permet d'exporter l'AST généré par ANTLR sous forme de texte. L'objectif est d'être capable d'atteindre un point fixe sur tous les fichiers de tests (`original.sc` -> AST -> `printed.sc` -> AST -> `printed2.sc`).

### Analyse sémantique

Une fois la grammaire complétée, vous pourrez mettre en place votre première analyse sémantique qui consiste à vérifier que toute variable utilisée a bien été définie auparavant. Cette analyse est faite directement sur l'AST.

**Question 3 :** Dans le package `compiler.frontend`, créez les classes nécessaires pour construire une table des symboles multiniveau. 

- La classe `SymbolTable` est déjà créée, mais doit être complétée avec les fonctions `initializeScope`, `finalizeScope`, `insert` et `lookup`.
- Il faut également implémenter une classe qui représente un niveau de la table des symboles et une classe qui représente en entrée dans la table des symboles. Dans un premier temps, cette entrée ne contient que le nom du symbole. Si besoin vous pourrez ajouter de nouvelles informations (type, dernière écriture, etc.).
- Nous vous conseillons de lier les objets de l'AST aux niveaux correspondants pour pouvoir réutiliser la table de symboles dans les transformations futures (en utilisant une hashmap associant les `ParserRuleContext` aux `SymbolTableLevel`).
	
**Question 4 :** Dans le package `compiler.frontend`, implémentez un visiteur de l'AST qui va parcourir l'arbre en ajoutant les éléments dans la table des symbole et en gérant correctement les différents niveaux. A chaque utilisation d'une variable, s'il ne peut pas la trouver dans la table des symboles, il doit retourner une erreur.
