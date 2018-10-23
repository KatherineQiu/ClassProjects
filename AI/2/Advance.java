/*
 * Project 2: Automated Reasoning
 * @author Ziyi Kou, Ziqiu Wu
 * @update 2018-10-21
 */

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Advance {
    // A simple resolution algorithm for propositional logic.
    public static boolean PL_Resolution(Set<Clause> kb, Clause alpha){
        // the set of clauses of knowledge base and Not query
        Set<Clause> clauses=new HashSet<Clause>();
        for(Clause clause:kb){
            clauses.add(clause);
        }
        clauses.add(alpha);

        List<Clause> clauseList=new LinkedList<Clause>(clauses);

        Set<Clause> newClause=new HashSet<Clause>();

        Clause resolvents=null;
        Set<String> stringClauseSet=null;
        Set<String> stringNewSet=null;
        int min_length=0;
        while (true){
//            System.out.println("kb size:"+clauseList.size()+"new size:"+newClause.size());
            for(int i=0;i<clauseList.size();i++){
                for(int j=i+1;j<clauseList.size();j++){
                    Clause x1=clauseList.get(i);
                    Clause x2=clauseList.get(j);
                    resolvents=clauseList.get(i).intersect(clauseList.get(j));
                    if(resolvents==null){
                        continue;
                    }
//                    System.out.println(x1.getValue()+" + "+x2.getValue()+" -> "+resolvents.getValue());
                    if(resolvents.getValue().equals("")){
                        // resolvents contains empty clause
                        return true;
                    }
                    if(x1.getValue().contains("∨") && x2.getValue().contains("∨") && resolvents.getValue().contains("∨")){
                        continue;
                    }
                    newClause.add(resolvents);
                }
            }

            stringClauseSet=new HashSet<String>();
            for(Clause clause:clauseList){
                stringClauseSet.add(clause.getValue());
            }
            stringNewSet=new HashSet<String>();
            for(Clause clause:newClause){
                stringNewSet.add(clause.getValue());
            }

            if(stringClauseSet.containsAll(stringNewSet)){
                return false;
            }

            stringClauseSet.addAll(stringNewSet);
            clauseList=new LinkedList<Clause>();
            for(String s:stringClauseSet){
                clauseList.add(new Clause(s));
            }
        }
    }

    public static String test_both_side(Set<Clause> kb,Clause alpha){
        if(PL_Resolution(kb,alpha)==true){
            return "true";
        }
        else {
            if(PL_Resolution(kb,new Clause(alpha.getValue().substring(1)))==true){
                return "false";
            }
            else {
                return "not sure";
            }
        }
    }

    public static void print_kb(Set<Clause> kb){
        for(Clause clause:kb){
            System.out.println(clause.getValue());
        }
    }

    public static void main(String[] args) {
        // run sample problems using a resolution-based theorem prover
        System.out.println("---------------------1:Modus Ponens-----------------------");
        System.out.println("-----letter mapping:same");

        System.out.println("-----kb:");
        Set<Clause> kb = new HashSet<Clause>();
        kb.add(new Clause("P"));
        kb.add(new Clause("¬P∨Q"));
        print_kb(kb);

        System.out.println("-----query:");
        System.out.println("Q:"+test_both_side(kb,new Clause("¬Q")));

        System.out.println("---------------------2:Wumpus World-----------------------");
        System.out.println("-----letter mapping:");
        System.out.println("A -> P1,1");
        System.out.println("B -> B1,1");
        System.out.println("C -> P1,2");
        System.out.println("D -> P2,1");
        System.out.println("E -> B2,1");
        System.out.println("F -> P2,2");
        System.out.println("G -> P3,1");

        System.out.println("-----kb:");
        Set<Clause> kb2 = new HashSet<Clause>();
        kb2.add(new Clause("¬A"));
        kb2.add(new Clause("¬B∨C∨D"));
        kb2.add(new Clause("¬C∨B"));
        kb2.add(new Clause("¬D∨B"));
        kb2.add(new Clause("¬E∨A∨F∨G"));
        kb2.add(new Clause("E∨¬A"));
        kb2.add(new Clause("E∨¬F"));
        kb2.add(new Clause("E∨¬G"));
        kb2.add(new Clause("¬B"));
        kb2.add(new Clause("E"));
        print_kb(kb2);

        System.out.println("-----query:");
        System.out.println("C:"+test_both_side(kb2, new Clause("¬C")));

        System.out.println("---------------------3:Horn Clauses-----------------------");
        System.out.println("-----letter mapping:");
        System.out.println("P -> mythical");
        System.out.println("Q -> immortal");
        System.out.println("R -> mammal");
        System.out.println("S -> horned");
        System.out.println("T -> magical");

        System.out.println("-----kb:");
        Set<Clause> kb3 = new HashSet<Clause>();
        kb3.add(new Clause("¬P∨Q"));
        kb3.add(new Clause("P∨¬Q"));
        kb3.add(new Clause("P∨R"));
        kb3.add(new Clause("¬Q∨S"));
        kb3.add(new Clause("¬R∨S"));
        kb3.add(new Clause("¬S∨T"));
        print_kb(kb3);

        System.out.println("-----query:");
        System.out.println("P:"+test_both_side(kb3, new Clause("¬P")));
        System.out.println("T:"+test_both_side(kb3, new Clause("¬T")));
        System.out.println("S:"+test_both_side(kb3, new Clause("¬S")));

        System.out.println("---------------------4:Liars and Truth-tellers-----------------------");
        System.out.println("-----letter mapping:");
        System.out.println("A -> Amy");
        System.out.println("B -> Bob");
        System.out.println("C -> Cal");
        System.out.println("Each name variable X here means that \"X is truth-teller\"");

        System.out.println("(a)");
        System.out.println("-----kb:");
        Set<Clause> kb4_1 = new HashSet<Clause>();
        kb4_1.add(new Clause("¬A∨C"));
        kb4_1.add(new Clause("¬B∨¬C"));
        kb4_1.add(new Clause("B∨C"));
        kb4_1.add(new Clause("¬C∨B∨¬A"));
        kb4_1.add(new Clause("¬B∨C"));
        kb4_1.add(new Clause("A∨C"));
        print_kb(kb4_1);

        System.out.println("-----query:");
        System.out.println("A:"+test_both_side(kb4_1, new Clause("¬A")));
        System.out.println("B:"+test_both_side(kb4_1, new Clause("¬B")));
        System.out.println("C:"+test_both_side(kb4_1, new Clause("¬C")));

        // 4.Liars and Truth-tellers (b)
        System.out.println("(b)");
        System.out.println("-----kb:");
        Set<Clause> kb4_2 = new HashSet<Clause>();
        kb4_2.add(new Clause("¬A∨¬C"));
        kb4_2.add(new Clause("C∨A"));
        kb4_2.add(new Clause("¬B∨A"));
        kb4_2.add(new Clause("¬B∨C"));
        kb4_2.add(new Clause("¬C∨B"));
        print_kb(kb4_2);

        System.out.println("A:"+test_both_side(kb4_2, new Clause("¬A")));
        System.out.println("B:"+test_both_side(kb4_2, new Clause("¬B")));
        System.out.println("C:"+test_both_side(kb4_2, new Clause("¬C")));

        System.out.println("---------------------5:More Liars and Truth-tellers-----------------------");
        System.out.println("-----letter mapping:");
        System.out.println("each symbol represents the person who has the name of the same first letter");

        System.out.println("-----kb:");
        Set<Clause> kb5 = new HashSet<Clause>();
        kb5.add(new Clause("¬A∨H"));
        kb5.add(new Clause("¬A∨I"));
        kb5.add(new Clause("¬H∨¬I∨A"));
        kb5.add(new Clause("¬B∨A"));
        kb5.add(new Clause("¬B∨L"));
        kb5.add(new Clause("¬A∨¬L∨B"));
        kb5.add(new Clause("¬C∨B"));
        kb5.add(new Clause("¬C∨G"));
        kb5.add(new Clause("¬B∨¬G∨C"));
        kb5.add(new Clause("¬D∨E"));
        kb5.add(new Clause("¬D∨L"));
        kb5.add(new Clause("¬E∨¬L∨D"));
        kb5.add(new Clause("¬E∨C"));
        kb5.add(new Clause("¬E∨H"));
        kb5.add(new Clause("¬C∨¬H∨E"));
        kb5.add(new Clause("¬F∨D"));
        kb5.add(new Clause("¬F∨I"));
        kb5.add(new Clause("¬D∨¬I∨F"));
        kb5.add(new Clause("¬G∨¬E"));
        kb5.add(new Clause("¬G∨¬J"));
        kb5.add(new Clause("E∨J∨G"));
        kb5.add(new Clause("¬H∨¬F"));
        kb5.add(new Clause("¬H∨¬K"));
        kb5.add(new Clause("F∨K∨H"));
        kb5.add(new Clause("¬I∨¬G"));
        kb5.add(new Clause("¬I∨¬K"));
        kb5.add(new Clause("G∨K∨I"));
        kb5.add(new Clause("¬J∨¬A"));
        kb5.add(new Clause("¬J∨¬C"));
        kb5.add(new Clause("A∨C∨J"));
        kb5.add(new Clause("¬K∨¬D"));
        kb5.add(new Clause("¬K∨¬F"));
        kb5.add(new Clause("D∨F∨K"));
        kb5.add(new Clause("¬L∨¬B"));
        kb5.add(new Clause("¬L∨¬J"));
        kb5.add(new Clause("B∨J∨L"));
        print_kb(kb5);

        System.out.println("-----query:");
        for(int i=65;i<=76;i++){
            System.out.println(String.valueOf((char)i)+":"+test_both_side(kb5, new Clause("¬"+String.valueOf((char)i))));
        }

        System.out.println("---------------------6:The Doors of Enlightenment-----------------------");
        System.out.println("-----letter mapping:");
        System.out.println("For each x in A, B, C, D, E, F, G, and H we assume x means \"x is a knight\" and ¬x means \"x is a knave\"");
        System.out.println("For each x in X Y Z W we assume x means \"x is a good door\" and ¬x means \"x is a bad door\"");

        System.out.println("(a)");
        System.out.println("-----kb:");
        Set<Clause> kb6_1 = new HashSet<Clause>();
        kb6_1.add(new Clause("X∨Y∨Z∨W"));
        kb6_1.add(new Clause("¬A∨X"));
        kb6_1.add(new Clause("¬X∨A"));
        kb6_1.add(new Clause("¬B∨Y∨Z"));
        kb6_1.add(new Clause("¬Y∨B"));
        kb6_1.add(new Clause("¬Z∨B"));
        kb6_1.add(new Clause("¬C∨A"));
        kb6_1.add(new Clause("¬C∨B"));
        kb6_1.add(new Clause("¬A∨¬B∨C"));
        kb6_1.add(new Clause("¬D∨X"));
        kb6_1.add(new Clause("¬D∨Y"));
        kb6_1.add(new Clause("¬X∨¬Y∨D"));
        kb6_1.add(new Clause("¬E∨X"));
        kb6_1.add(new Clause("¬E∨Z"));
        kb6_1.add(new Clause("¬X∨¬Z∨¬E"));
        kb6_1.add(new Clause("¬F∨D∨E"));
        kb6_1.add(new Clause("¬F∨¬D∨¬E"));
        kb6_1.add(new Clause("¬D∨E∨F"));
        kb6_1.add(new Clause("D∨¬E∨F"));
        kb6_1.add(new Clause("¬G∨¬C∨F"));
        kb6_1.add(new Clause("C∨G"));
        kb6_1.add(new Clause("¬F∨G"));
        kb6_1.add(new Clause("¬H∨¬G∨A"));
        kb6_1.add(new Clause("G∨H"));
        kb6_1.add(new Clause("H"));
        kb6_1.add(new Clause("¬A∨H"));
        print_kb(kb6_1);

        System.out.println("-----query:");
        System.out.println("X:"+test_both_side(kb6_1, new Clause("¬X")));
        System.out.println("Y:"+test_both_side(kb6_1, new Clause("¬Y")));
        System.out.println("Z:"+test_both_side(kb6_1, new Clause("¬Z")));
        System.out.println("W:"+test_both_side(kb6_1, new Clause("¬W")));

        // 6.The Doors of Enlightenment (b)
        System.out.println("(b)");
        System.out.println("-----kb:");
        Set<Clause> kb6_2 = new HashSet<Clause>();
        kb6_2.add(new Clause("X∨Y∨Z∨W"));
        kb6_2.add(new Clause("¬A∨X"));
        kb6_2.add(new Clause("¬X∨A"));
        kb6_2.add(new Clause("¬C∨A"));
        kb6_2.add(new Clause("G∨C"));
        kb6_2.add(new Clause("¬C∨¬B∨A"));
        kb6_2.add(new Clause("¬H∨¬G∨A"));
        kb6_2.add(new Clause("G∨H"));
        kb6_2.add(new Clause("H"));
        kb6_2.add(new Clause("¬A∨H"));
        print_kb(kb6_2);

        System.out.println("-----query:");
        System.out.println("X:"+test_both_side(kb6_2, new Clause("¬X")));
        System.out.println("Y:"+test_both_side(kb6_2, new Clause("¬Y")));
        System.out.println("Z:"+test_both_side(kb6_2, new Clause("¬Z")));
        System.out.println("W:"+test_both_side(kb6_2, new Clause("¬W")));

    }
}