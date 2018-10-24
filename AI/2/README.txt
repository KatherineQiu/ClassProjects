Automated Reasoning
Second Project of CSC 442 Artificial Intelligence

Copyright: 2018, Ziyi Kou & Ziqiu Wu

Environment:
1) ubuntu 16.04
2) java 1.8(you need jdk1.8 or higher to run the code)

Documents:
1) Symbol.java: The class for symbols like 'P' 'Q' or 'Stench' which stands for a proposition that can be true or false.
2) Operator.java: The class representing five connectives used in the project, i.e. not('¬') | and('∧') | or('∨') | implies('⇒') | if_and_only_if('⇔')
3) Sentence.java: The class representing propositional logic sentences composed of one or more symbols with connectives.
4) Model.java: The class containing the basical functions for "truth-table enumeration method" for part1.
5) Clause.java: The class representing CNF transformed from original sentences and some basic function for it.
6) Basic.java: The class containing the implementation of "truth-table enumeration method" and testing procedure and result of first 6 test samples.
7) Advance.java: The class containing the implementation of "resolution-based theorem prover" and testing procedure and result of first 6 test samples.
8) files with same names but the 'class' format are compiled by JVM and could be run derectly. 

Steps for part1:
1) open terminal under the 'AI2/compiled' folder
2) run the file with the command "java Basic", make sure java version in your environment is 1.8 or higher. Information about the question and result  is displayed on the console. 

Steps for part2:
1) open terminal under the 'AI2/compiled' folder
2) run the file with the command "java Advance".
