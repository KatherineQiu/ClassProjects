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

        return resort(value.replaceAll(" ",""));
    }

    private String resort(String value){
        if(!value.contains("∨")){
            return value;
        }

        String[] strArr=value.split("∨");
        for(int i=0;i<strArr.length;i++){
            for(int j=i+1;j<strArr.length;j++){
                if(strArr[i].charAt(strArr[i].length()-1)>strArr[j].charAt(strArr[j].length()-1)){
                    String tmp=strArr[i];
                    strArr[i]=strArr[j];
                    strArr[j]=tmp;
                }
            }
        }

        return String.join("∨",strArr);
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

        if(deleteList.size()==0){
            return null;
        }
        else if(deleteList.size()==1){
            int fore_size=fore.size();
            int back_size=back.size();
            for(String delete:deleteList){
                fore.remove(delete);
                back.remove(Not(delete));
            }
            if(fore.size()+back.size()>fore_size && fore.size()+back.size()>back_size){
                return null;
            }
            back.addAll(fore);
            if(back.size()==0){
                return new Clause("");
            }
            else if(back.size()==1){
                return new Clause(String.valueOf(back.toArray()[0]));
            }

            else{
                return new Clause(resort(String.join("∨",back)));
            }
        }
        else{
            return null;
        }
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
    }
}
