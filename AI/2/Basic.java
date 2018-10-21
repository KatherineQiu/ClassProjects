import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class Basic {
    public static boolean tt_entails(Set<Sentence> kb, Sentence alpha) {
        Set<String> set = new HashSet<String>();
        for (Sentence s : kb) {
            set.addAll(s.getSymbols());
        }
        set.addAll(alpha.getSymbols());


        //convert String to Symbol
        LinkedList<Symbol> list = new LinkedList<Symbol>();
        for (String s : set) {
            list.add(new Symbol(s));
        }

        return tt_check_all(kb,alpha,list,new Model());
    }

    public static boolean tt_check_all(Set<Sentence> kb, Sentence alpha, LinkedList<Symbol> symbols, Model model) {
        if(symbols.isEmpty()){
            if(model.SatisfyKB(kb)){
                return model.Satisfy(alpha);
            }
            else{
                return true;
            }
        }
        else{
            Symbol b=symbols.removeFirst();
            return tt_check_all(kb,alpha,(LinkedList<Symbol>)symbols.clone(),model.clone().Assign(b,Boolean.TRUE)) && tt_check_all(kb,alpha,(LinkedList<Symbol>)symbols.clone(),model.clone().Assign(b,Boolean.FALSE));
        }
    }

    public static void main(String[] args) {
    	System.out.println("---------------------1-----------------------");
        Set<Sentence> kb = new HashSet<Sentence>();
        kb.add(new Sentence("P"));
        kb.add(new Sentence("P⇒Q"));

        Sentence alpha = new Sentence("Q");

        System.out.println("Q:Q------"+tt_entails(kb,alpha));
    	
        System.out.println("---------------------2-----------------------");
        kb = new HashSet<Sentence>();
        kb.add(new Sentence("¬A"));
        kb.add(new Sentence("B⇔(C∨D)"));
        kb.add(new Sentence("E⇔(A∨F∨G)"));
        kb.add(new Sentence("¬B"));
        kb.add(new Sentence("E"));

        System.out.println("Q:C------"+tt_entails(kb, new Sentence("C")));

        System.out.println("---------------------3-----------------------");
        kb = new HashSet<Sentence>();
        kb.add(new Sentence("P⇒Q"));
        kb.add(new Sentence("¬P⇒¬Q∧R"));
        kb.add(new Sentence("Q∨R⇒S"));
        kb.add(new Sentence("S⇒T"));


        System.out.println("Q:P------"+tt_entails(kb, new Sentence("P")));
        System.out.println("Q:T------"+tt_entails(kb, new Sentence("T")));
        System.out.println("Q:S------"+tt_entails(kb, new Sentence("S")));

        System.out.println("---------------------4-----------------------");
        Set<Sentence> kb1 = new HashSet<Sentence>();
        kb1.add(new Sentence("A⇔(C∧A)"));
        kb1.add(new Sentence("B⇔¬C"));
        kb1.add(new Sentence("C⇔B∨¬A"));

        Set<Sentence> kb2 = new HashSet<Sentence>();
        kb2.add(new Sentence("A⇔¬C"));
        kb2.add(new Sentence("B⇔A∧C"));
        kb2.add(new Sentence("C⇔B"));

        System.out.println("(a)");
        System.out.println("Q:A------"+tt_entails(kb1, new Sentence("A")));
        System.out.println("Q:B------"+tt_entails(kb1, new Sentence("B")));
        System.out.println("Q:C------"+tt_entails(kb1, new Sentence("C")));

        System.out.println("(b)");
        System.out.println("Q:A------"+tt_entails(kb2, new Sentence("A")));
        System.out.println("Q:B------"+tt_entails(kb2, new Sentence("B")));
        System.out.println("Q:C------"+tt_entails(kb2, new Sentence("C")));

        System.out.println("---------------------5-----------------------");
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

        for(int i=65;i<=76;i++){
            System.out.println("Q:"+String.valueOf((char)i)+"------"+tt_entails(kb, new Sentence(String.valueOf((char)i))));
        }

        System.out.println("---------------------6-----------------------");
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
        
        
        kb2 = new HashSet<Sentence>();
        kb2.add(new Sentence("X∨Y∨Z∨W"));
        kb2.add(new Sentence("A⇔X"));
        kb2.add(new Sentence("C⇔A∧(B∨C∨D∨E∨F∨G∨H)"));
        kb2.add(new Sentence("¬G⇒C"));
        kb2.add(new Sentence("H⇔((G∧H)⇒A)"));

        System.out.println("(a)");
        System.out.println("Q:X------"+tt_entails(kb1, new Sentence("X")));
        System.out.println("Q:Y------"+tt_entails(kb1, new Sentence("Y")));
        System.out.println("Q:Z------"+tt_entails(kb1, new Sentence("Z")));
        System.out.println("Q:W------"+tt_entails(kb1, new Sentence("W")));
        
        
        System.out.println("(b)");
        System.out.println("Q:X------"+tt_entails(kb2, new Sentence("X")));
        System.out.println("Q:Y------"+tt_entails(kb2, new Sentence("Y")));
        System.out.println("Q:Z------"+tt_entails(kb2, new Sentence("Z")));
        System.out.println("Q:W------"+tt_entails(kb2, new Sentence("W")));
    }
}