import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Clause {
    private String value;

    public Clause(String value) {
        this.value = PreProcess(value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private String PreProcess(String value){
        value=value.replaceAll(" ","");

        return value;
    }

    public boolean belongTo(Clause c2){
        Set<String> fore=new HashSet<String>();
        Set<String> back=new HashSet<String>();
        if(this.value.contains("∨") && !c2.getValue().contains("∨")){
            return false;
        }
        if(this.value.contains("∨")){
            for(String s:this.value.split("∨")){
                fore.add(s);
            }
        }
        else{
            fore.add(this.value);
        }

        if(c2.getValue().contains("∨")){
            for(String s:c2.getValue().split("∨")){
                back.add(s);
            }
        }
        else{
            back.add(c2.getValue());
        }

        return back.contains(fore);
    }

    public Clause intersect(Clause c2){
        Set<String> fore=new HashSet<String>();
        Set<String> back=new HashSet<String>();

        if(this.value.contains("∨")){
            for(String s:this.value.split("∨")){
                fore.add(s);
            }
        }
        else{
            fore.add(this.value);
        }

        if(c2.getValue().contains("∨")){
            for(String s:c2.getValue().split("∨")){
                back.add(s);
            }
        }
        else{
            back.add(c2.getValue());
        }

        Set<String> deleteList=new HashSet<String>();
        for(String s1:fore){
            if(back.contains(Not(s1))){
                deleteList.add(s1);
            }
        }

        for(String delete:deleteList){
            fore.remove(delete);
            back.remove(Not(delete));
        }

        back.addAll(fore);

        return new Clause(String.join("∨",back));
    }

    private String Not(String liter){
        if(liter.length()==1){
            return "¬"+liter;
        }
        else{
            return liter.substring(1);
        }
    }

    //test
    public static void main(String[] args) {
//        Clause test=new Clause(new Sentence("C⇔(B⇔A∧C)"));
        Clause clause1=new Clause("¬B");
        System.out.println(clause1.intersect(new Clause("B")).getValue());
    }
}
