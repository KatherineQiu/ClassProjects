import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
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
            return tt_check_all(kb,alpha,symbols,model.clone().Assign(b,Boolean.TRUE)) && tt_check_all(kb,alpha,symbols,model.clone().Assign(b,Boolean.FALSE));
        }
    }

    public static void main(String[] args) {
        Set<Sentence> kb = new HashSet<Sentence>();
        Sentence kb_1 = new Sentence("¬A");
        Sentence kb_2 = new Sentence("B⇔(C∨D)");
        Sentence kb_3=new Sentence("E⇔(A∨F∨G)");
        Sentence kb_4=new Sentence("¬B");
        Sentence kb_5=new Sentence("E");
        kb.add(kb_1);
        kb.add(kb_2);
        kb.add(kb_3);
        kb.add(kb_4);
        kb.add(kb_5);

        Sentence alpha = new Sentence("C");

        System.out.println(tt_entails(kb, alpha));
    }
}
