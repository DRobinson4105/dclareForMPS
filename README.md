# DclareForMPS
DclareForMPS adds a language aspect named 'rules' to MPS.  
A rule typically derives some part of a model.  
Rules can trigger other rules.  
A rule runs when something changes in the models that is read by this rule (reactive).  The system terminates on a fixpoint (if no changes occur anymore).  
The regular MPS base-language can be used to specify the rules.  
Rules definitions typically use quotations (simple or not) to define the derivations.  
The rules are executed incrementally, meaning they perform a minimal amount of changes to derive the defined patterns (expressed in quotations).

## Work in progress
DclareForMPS is a work in progress. In this phase of development, no guarantees are given on the stability and scalability of the software.  
All feedback, functional or technical, is appreciated.

## JDclare
DclareForMPS uses the transactions framework and the collections library that are part of the JDclare project.  
The JDclare project can be found here: [JDclare on GitHub](https://github.com/ModelingValueGroup/jdclare)

## Installation
In an installation of JetBrains MPS (2018.3 or higher), install the DclareForMPS.zip plugin and open the extracted ExamplesProject.zip as a Project in MPS.

## Examples
Two examples can be found in the ExamplesProject:

### 1. Entity to Class transformation
In this example two Languages are defined, the (Entity) SourceLanguage:
![picture alt](docs/SourceLanguage.jpg "SourceLanguage")  

and the (Class) TargetLanguage:  
![picture alt](docs/TargetLanguage.jpg "TargetLanguage")

Rules are defined that transform instances of the Entity Language to instances of the Target Language:
![picture alt](docs/Rules.jpg "Rules")

Two example entities (instances of the SourceLanguage) are also provided:  
![picture alt](docs/Solution.jpg "SourceSolution")

The classes in the SourceSolution/target folder will be 'generated' by the defined Rules.   
Please experiment with editing the source entities and check how the rules (reactively) change the target classes.
Also when a new entity is created in the source folder, it will immediately be transformed to a class in the target folder.
(Note that no generation is required to transform the model!)

Overview of this complete Example:
![picture alt](docs/Overview.jpg "Overview")

### 2. Sudoku
The Sudoku example is a more sophisticated example that uses Rules to solve a Sudoku puzzle.

The Sudoku Structure and Rules are defined in the Sudoku Language.

There are two example Sudoku game instances delivered in the SudokuSolution/games folder.

The sudoku31 example:
![picture alt](docs/sudoku31.jpg "Sudoku")
