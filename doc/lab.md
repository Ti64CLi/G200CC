# Travaux pratiques GCC

L'objectif de la série de TP de ce module est de développer un compilateur permettant de compiler un langage similaire au C vers de l'assembleur RISC-V. Le compilateur sera codé en Java, en reprenant des éléments de la compilation orientée objet.

Les TP se séparent en plusieurs parties :
 - **Partie 1 :** Compléter la grammaire du langage
 - **Partie 2 :** Construire la représentation intermédiaire
 - **Partie 3 :** Implémentation de l'allocation de registres
 - **Partie 4 :** Ajout d'une optimisation dans le compilateur


A la fin de chaque partie, les étudiants devront : 
 - faire une demonstration de l'outil en TP
 - rendre le code qu'ils ont écris via git
 - rendre un document de une page qui décris les choix qu'ils ont fait et la façon dont ils ont testé le code rendu
 
## Présentation des fichiers donnés 

Pour qu'il soit possible de faire les différentes parties proposées, un squelette de code vous est donné. Ce squelette contient une base de la grammaires, la définition de la représentation intermédiaire et la définition du *back-end*. De plus, le squelette contient plusieures suites de tests qu'il faudra compléter pour s'assurer du bon fonctionnement de votre outil.

Le répertoire `src` du projet squelette suit l'arborescence suivante :  

```
.
├── main
│   ├── antlr						#Fichiers ANTLR définissant les deux grammaires utilisées
│   └── java	
│       ├── antlr					#Fichiers générés par ANTLR
│       ├── backend					#Définition des classes core du backend
│       │   └── riscv					#Définition des classes spécifiques au RISC-V du backend
│       │       ├── instruction
│       │       └── operand
│       ├── compiler					#Dossier contenant les fichiers à modifier 
│       │   ├── backend					#Partie 3
│       │   ├── dataflow				#Partie 4
│       │   └── frontend				#Parties 1 et 2
│       └── ir						#Définition des classes de l'IR
│           ├── core
│           ├── importExport
│           ├── instruction
│           └── terminator
└── test						# Tests et fichiers d'entrée
    ├── java
    │   └── compiler
    └── resources

```

Comme montré dans l'arborescence, la plupart des modifications que vous devrez faire se situent dans le dossier `src/java/compiler/`. Vous devrez sans doute regarder les autres fichiers pour comprendre comment manipuler les objets mais normalement il n'y aura que des modification à la marge à réaliser.

## Installation d'un environnement de développement

L'environnement de développement par défaut est Eclipse. Vous pouvez utiliser un autre IDE mais nous ne répondrons pas aux questions liées à la configuration d'un autre IDE, et nous ne vous aiderons pas à trouver un bug si vous ne pouvez pas exécuter votre code en mode *debug*.

Le projet a besoin de Java 21 pour fonctionner correctement. Normalement vous pourrez trouver des tutos en ligne qui vous expliquent comment l'installer sur votre distribution.

1. Téléchargez et installez Eclipse (Java edition)
2. Téléchargez les sources du projet sur [https://gitlab.istic.univ-rennes1.fr/srokicki/cours-gcc](https://gitlab.istic.univ-rennes1.fr/srokicki/cours-gcc)
3. Naviguez dans les fichiers téléchargés et lancez la commande `gradle build`. Cette commande doit générer les fichiers ANTLR pour le squelette de grammaire donné et vérifier que tout compile bien.
4. Dans eclipse, naviguez dans File - Import - Existing projects into workspace... et renseignez le dossier dans lequel vous avez téléchargé les sources. Vous devriez alors pouvoir importer le projet 'cours-gcc-TP'.
5. Testez votre instalation : 
	- vous pouvez exécuter le fichier `Compiler.java` dans le package `compiler` qui permet d'appeler le compilateur sur un fichier donné. Pour cela, faire un clic droit sur le fichier et sélectionner `Run As - Java application`
	- vous pouvez lancer les fichiers de test JUnit dans le dossier `src/test/java/compiler`. Pour cela, sélectionnez l'un des fichiers de test et choisissez `Run as - JUnit test`.
	- Vous pouvez lancer en mode debug en sélectionnant `Debug as` au lieu de `Run as`.
