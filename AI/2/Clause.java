/*
 * Project 2: Automated Reasoning
 * @author Ziyi Kou, Ziqiu Wu
 * @update 2018-10-21
 */

import java.util.HashSet;
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
        //remove all the spaces
        return resort(value.replaceAll(" ",""));
    }

    private String resort(String value){
        // sort literals in a clause in an increasing order of length, if clause contain "∨"
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
        // resolve the contradicted literals of a pair of clauses and return the union clause
        Set<String> fore=new HashSet<String>();
        Set<String> back=new HashSet<String>();

        // add values of literals in clause c1 to the set "fore"
        if(this.value.contains("∨")){
            for(String s:this.value.split("∨")){
                fore.add(s);
            }
        }
        else{
            fore.add(this.value);
        }

        // add values of literals in clause c2 to the set "back"
        if(c2.getValue().contains("∨")){
            for(String s:c2.getValue().split("∨")){
                back.add(s);
            }
        }
        else{
            back.add(c2.getValue());
        }

        // a set to store string, in set "fore", which has contradiction in set "back"
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
        // return negated statement
        if(liter.length()==1){
            return "¬"+liter;
        }
        else{   // liter[0] = "¬";
            return liter.substring(1);
        }
    }

    //test
    public static void main(String[] args) {
//        Clause test=new Clause(new Sentence("C⇔(B⇔A∧C)"));
        Clause clause1=new Clause("¬B");
    }
}