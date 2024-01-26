# TP2 - Optimisations

L'objectif de ce second TP est d'implémenter plusieurs analyses et transformations qui seront appliquées à l'IR générée par le front-end. Il est demandé d'implémenter deux transformations parmi les trois proposées. La propagation de constantes étant la plus simple à réaliser, nous considérons qu'elle doit être choisie parmi les trois.

## Propagation de constantes en forme SSA

Comme présenté dans les slides, la propagation de constantes peut se faire très simplement lorsqu'on est en forme SSA. Il faut implémenter un work-list algorithm qui stocke les variables à analyser, et pour chaque variable, propager l'information si c'est une constante. Dans ce cas, on peut propager la constante et les décisions de branchement. Les 

## Elimination de sous-expressions communes

Pour réaliser l'élimination de sous-expressions communes, il faut commencer par réaliser une analyse des *available expressions* puis une analyse des  *reaching expressions* aux points où une transformation serait possible (i.e. aux points où on calcule une expression qui est *available*).
Attention, lorsqu'on crée une variable contenant la sous-expression à conserver, il faut faire attention à rester en forme SSA et à ajouter les noeuds phi aux bons endroits (i.e. à la frontère de domination).

## Conditional constant propagation

La troisième transformation proposée est l'analyse sur treilli premettant d'obtenir les variables constantes et les blocks non exécutés. Pour faire cette analyse, il faudra implémenter le treilli proposé en cours et impléménter le *work-list algorithm* vu en cours.

## Bonus : interpreteur d'IR

Pour les groupes en avance, il est également proposé de coder un interpreteur d'IR. L'idée ici est de construire un cadre de fonction contenant une map faisant le lien entre les IRValue et une valeur calculée pendant l'interprétation, ainsi que de la mémoire allouée par chaque alloca. Lorsqu'on interprete un bloc de l'IR, on évalue chaque noeud et on met à jour la valeur de la IRValue en fonction de la valeur de nos opérandes.

Le point difficile de l'interpreteur est de gérer correctement les phi qui ont besoin de connaitre le bloc précédent exécuté.

Pour pouvoir utiliser cet interpreteur, il est demandé de compter le nombre d'opération exécutées pendant l'interprétation. On pourra ainsi mesurer le gain en performance des optimisations proposées.
