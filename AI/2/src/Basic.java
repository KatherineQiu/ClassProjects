/*
 * Project 2: Automated Reasoning
 * @author Ziyi Kou, Ziqiu Wu
 * @update 2018-10-21
 */

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class Basic {
	
	//  A truth-table enumeration algorithm for deciding propositional entailment.
    public static boolean tt_entails(Set<Sentence> kb, Sentence alpha) {
        Set<String> set = new HashSet<String>();
        for (Sentence s : kb) {
            set.addAll(s.getSymbols());
        }
        set.addAll(alpha.getSymbols());

        //convert type String to type Symbol
        LinkedList<Symbol> list = new LinkedList<Symbol>();
        for (String s : set) {
            list.add(new Symbol(s));
        }

        return tt_check_all(kb,alpha,list,new Model());
    }

    public static boolean tt_check_all(Set<Sentence> kb, Sentence alpha, LinkedList<Symbol> symbols, Model model) {
    	// The variable model represents a partial model - an assignment to some of the symbols.
        if(symbols.isEmpty()){
            if(model.satisfyKB(kb)){
                return model.Satisfy(alpha);
            }
            else{
                return true;	// when KB is false, always return true
            }
        }
        else{
            Symbol b=symbols.removeFirst();
            return tt_check_all(kb,alpha,(LinkedList<Symbol>)symbols.clone(),model.clone().assign(b,Boolean.TRUE)) && tt_check_all(kb,alpha,(LinkedList<Symbol>)symbols.clone(),model.clone().assign(b,Boolean.FALSE));
        }
    }

    public static String test_both_side(Set<Sentence> kb,Sentence alpha){
        if(tt_entails(kb,alpha)==true){
            return "true";
        }
        else {
            if(tt_entails(kb,new Sentence("¬"+alpha.getValue()))==true){
                return "false";
            }
            else {
                return "not sure";
            }
        }
    }

    public static void print_kb(Set<Sentence> kb){
        for(Sentence sentence:kb){
            System.out.println(sentence.getValue());
        }
    }

    public static void main(String[] args) {
        // run sample problems using truth-table enumeration method

    	System.out.println("---------------------1:Modus Ponens-----------------------");
        System.out.println("-----letter mapping:same");
        System.out.println("-----kb:");
        Set<Sentence> kb = new HashSet<Sentence>();
        kb.add(new Sentence("P"));
        kb.add(new Sentence("P⇒Q"));
        print_kb(kb);

        System.out.println("-----query:");
        System.out.println("Q:"+test_both_side(kb,new Sentence("Q")));
    	
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
        kb = new HashSet<Sentence>();
        kb.add(new Sentence("¬A"));
        kb.add(new Sentence("B⇔(C∨D)"));
        kb.add(new Sentence("E⇔(A∨F∨G)"));
        kb.add(new Sentence("¬B"));
        kb.add(new Sentence("E"));
        print_kb(kb);

        System.out.println("-----query:");
        System.out.println("C:"+test_both_side(kb, new Sentence("C")));

        System.out.println("---------------------3:Horn Clauses-----------------------");
        System.out.println("-----letter mapping:");
        System.out.println("P -> mythical");
        System.out.println("Q -> immortal");
        System.out.println("R -> mammal");
        System.out.println("S -> horned");
        System.out.println("T -> magical");

        System.out.println("-----kb:");
        kb = new HashSet<Sentence>();
        kb.add(new Sentence("P⇒Q"));
        kb.add(new Sentence("¬P⇒¬Q∧R"));
        kb.add(new Sentence("Q∨R⇒S"));
        kb.add(new Sentence("S⇒T"));
        print_kb(kb);

        System.out.println("-----query:");
        System.out.println("P:"+test_both_side(kb, new Sentence("P")));
        System.out.println("T:"+test_both_side(kb, new Sentence("T")));
        System.out.println("S:"+test_both_side(kb, new Sentence("S")));

        System.out.println("---------------------4:Liars and Truth-tellers-----------------------");
        System.out.println("-----letter mapping:");
        System.out.println("A -> Amy");
        System.out.println("B -> Bob");
        System.out.println("C -> Cal");
        System.out.println("Each name variable X here means that \"X is truth-teller\"");

        System.out.println("(a)");
        System.out.println("-----kb:");
        Set<Sentence> kb1 = new HashSet<Sentence>();
        kb1.add(new Sentence("A⇔(C∧A)"));
        kb1.add(new Sentence("B⇔¬C"));
        kb1.add(new Sentence("C⇔B∨¬A"));
        print_kb(kb1);

        System.out.println("-----query:");
        System.out.println("A:"+test_both_side(kb1, new Sentence("A")));
        System.out.println("B:"+test_both_side(kb1, new Sentence("B")));
        System.out.println("C:"+test_both_side(kb1, new Sentence("C")));

        // 4.Liars and Truth-tellers (b)
        System.out.println("(b)");
        System.out.println("-----kb:");
        Set<Sentence> kb2 = new HashSet<Sentence>();
        kb2.add(new Sentence("A⇔¬C"));
        kb2.add(new Sentence("B⇔A∧C"));
        kb2.add(new Sentence("C⇔B"));
        print_kb(kb2);

        System.out.println("-----query:");
        System.out.println("A:"+test_both_side(kb2, new Sentence("A")));
        System.out.println("B:"+test_both_side(kb2, new Sentence("B")));
        System.out.println("C:"+test_both_side(kb2, new Sentence("C")));

        System.out.println("---------------------5:More Liars and Truth-tellers-----------------------");
        System.out.println("-----letter mapping:");
        System.out.println("each symbol represents the person who has the name of the same first letter");

        System.out.println("-----kb:");
        kb = new HashSet<Sentence>();
        kb.add(new Sentence("A⇔H∧I"));
        kb.add(new Sentence("B⇔A∧L"));
        kb.add(new Sentence("C⇔B∧G"));
        kb.add(new Sentence("D⇔E∧L"));
        kb.add(new Sentence("E⇔C∧H"));
        kb.add(new Sentence("F⇔D∧I"));
        kb.add(new Sentence("G⇔¬E∧¬J"));
        kb.add(new Sentence("H⇔¬F∧¬K"));
        kb.add(new Sentence("I⇔¬G∧¬K"));
        kb.add(new Sentence("J⇔¬A∧¬C"));
        kb.add(new Sentence("K⇔¬D∧¬F"));
        kb.add(new Sentence("L⇔¬B∧¬J"));
        print_kb(kb);

        System.out.println("-----query:");
        for(int i=65;i<=76;i++){
            System.out.println(String.valueOf((char)i)+":"+test_both_side(kb, new Sentence(String.valueOf((char)i))));
        }

        System.out.println("---------------------6:The Doors of Enlightenment-----------------------");
        System.out.println("-----letter mapping:");
        System.out.println("For each x in A, B, C, D, E, F, G, and H we assume x means \"x is a knight\" and ¬x means \"x is a knave\"");
        System.out.println("For each x in X Y Z W we assume x means \"x is a good door\" and ¬x means \"x is a bad door\"");

        System.out.println("(a)");
        System.out.println("-----kb:");
        kb1 = new HashSet<Sentence>();
        kb1.add(new Sentence("X∨Y∨Z∨W"));
        kb1.add(new Sentence("A⇔X"));
        kb1.add(new Sentence("B⇔Y∨Z"));
        kb1.add(new Sentence("C⇔A∧B"));
        kb1.add(new Sentence("D⇔X∧Y"));
        kb1.add(new Sentence("E⇔X∧Z"));
        kb1.add(new Sentence("F⇔(D∧¬E)∨(¬D∧E)"));
        kb1.add(new Sentence("G⇔(C⇒F)"));
        kb1.add(new Sentence("H⇔((G∧H)⇒A)"));
        print_kb(kb1);

        System.out.println("-----query:");
        System.out.println("X:"+test_both_side(kb1, new Sentence("X")));
        System.out.println("Y:"+test_both_side(kb1, new Sentence("Y")));
        System.out.println("Z:"+test_both_side(kb1, new Sentence("Z")));
        System.out.println("W:"+test_both_side(kb1, new Sentence("W")));

        // 6.The Doors of Enlightenment (b)
        System.out.println("(b)");
        System.out.println("-----kb:");
        kb2 = new HashSet<Sentence>();
        kb2.add(new Sentence("X∨Y∨Z∨W"));
        kb2.add(new Sentence("A⇔X"));
        kb2.add(new Sentence("C⇔A∧(B∨C∨D∨E∨F∨G∨H)"));
        kb2.add(new Sentence("¬G⇒C"));
        kb2.add(new Sentence("H⇔((G∧H)⇒A)"));
        print_kb(kb2);

        System.out.println("-----query:");
        System.out.println("X:"+test_both_side(kb2, new Sentence("X")));
        System.out.println("Y:"+test_both_side(kb2, new Sentence("Y")));
        System.out.println("Z:"+test_both_side(kb2, new Sentence("Z")));
        System.out.println("W:"+test_both_side(kb2, new Sentence("W")));
    }
}
