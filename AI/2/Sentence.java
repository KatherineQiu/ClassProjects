import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Sentence {
    private String value;

    public Sentence(String value) {
        this.value = PreProcess(value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Set<Symbol> getSymbols(){
        //ziqiu: return all symbol with name
        Set<Symbol> set=new HashSet<Symbol>();
        Set<String> letters=new HashSet<String>();

        for(int i=0;i<this.value.length();i++){
            char letter=this.value.charAt(i);
            if(letter>=65 && letter<=90){
                letters.add(String.valueOf(letter));
            }
        }

        for(String letter:letters){
            set.add(new Symbol(letter));
        }

        return set;
    }

    private String PreProcess(String value){
        value=value.trim();
        value= value.replace("{","");
        value=value.replace("}","");

        return value;
    }

    //test
    public static void main(String[] args){
        Sentence sentence=new Sentence("{P,P ⇒ Q}");
        Set<Symbol> symbols=sentence.getSymbols();
        System.out.println();
    }
}
