import javax.management.OperationsException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Model{
    private Map<String, Boolean> map;

    public Model() {
        this.map = new HashMap<String, Boolean>();
    }

    public Map<String, Boolean> getMap() {
        return map;
    }

    public void setMap(Map<String, Boolean> map) {
        this.map = map;
    }

    public Model Assign(Symbol p, Boolean b) {
        this.map.put(p.getName(), b);
        return this;
    }

    public Model clone(){
        Model newModel=new Model();
        newModel.setMap(this.getMap());

        return newModel;
    }


    public boolean SatisfyKB(Set<Sentence> kb){
        boolean pool=true;
        for(Sentence s:kb){
            boolean current=Satisfy(s);
            if(current==false){
                return false;
            }
        }

        return pool;
    }

    public boolean Satisfy(Sentence sentence){
        String sentenceValue=sentence.getValue();
        return SingleSatisfy(sentenceValue);
    }

    public boolean SingleSatisfy(String sentenceValue){
        if(sentenceValue.length()==1){
            return this.map.get(sentenceValue);
        }

        while(sentenceValue.indexOf('(')==0 && sentenceValue.indexOf(')')==sentenceValue.length()-1){
            sentenceValue=sentenceValue.replace("(","");
            sentenceValue=sentenceValue.replace(")","");
        }

        if(sentenceValue.contains("(") && sentenceValue.contains(")")){
            Map<Integer,String> map=new HashMap<Integer,String>();
            int left=0;
            for(int i=0;i<sentenceValue.length();i++){
                char c=sentenceValue.charAt(i);
                if(c=='('){
                    if(i!=0 && left==0){
                        map.put(i-1,String.valueOf(sentenceValue.charAt(i-1)));
                    }
                    left++;
                }
                else if(c==')'){
                    left--;
                    if(i!=sentenceValue.length()-1 && left==0){
                        map.put(i+1,String.valueOf(sentenceValue.charAt(i-1)));
                    }
                }
            }

            if(map.values().contains("⇔")){
                String[] arr=sentenceValue.split("⇔");
                String firstSentence=arr[0];
                String secondSentence=arr[1];
                return Operator.Iff(SingleSatisfy(firstSentence),SingleSatisfy(secondSentence));
            }
            else if(map.values().contains("⇒")){
                String[] arr=sentenceValue.split("⇒");
                String firstSentence=arr[0];
                String secondSentence=arr[1];
                return Operator.Imply(SingleSatisfy(firstSentence),SingleSatisfy(secondSentence));
            }
            else if(map.values().contains("∨") || map.values().contains("∧")){
                int max=0;
                for(Integer i:map.keySet()){
                    if(map.get(i).equals("∨") || map.get(i).equals("∧")){
                        if(i>max){
                            max=i;
                        }
                    }
                }
                if(map.get(max).equals("∨")){
                    return Operator.Or(SingleSatisfy(sentenceValue.substring(0,max)),SingleSatisfy(sentenceValue.substring(max+1)));
                }
                else{
                    return Operator.And(SingleSatisfy(sentenceValue.substring(0,max)),SingleSatisfy(sentenceValue.substring(max+1)));
                }
            }
            else if(map.values().contains("¬")){
                return Operator.Not(SingleSatisfy(sentenceValue.substring(1)));
            }
        }

        else if(sentenceValue.contains("⇔")){
            String[] arr=sentenceValue.split("⇔");
            String firstSentence=arr[0];
            String secondSentence=arr[1];
            return Operator.Iff(SingleSatisfy(firstSentence),SingleSatisfy(secondSentence));
        }
        else if(sentenceValue.contains("⇒")){
            String[] arr=sentenceValue.split("⇒");
            String firstSentence=arr[0];
            String secondSentence=arr[1];
            return Operator.Imply(SingleSatisfy(firstSentence),SingleSatisfy(secondSentence));
        }
        else if(sentenceValue.contains("∧") || sentenceValue.contains("∨")){
            int index=Math.max(sentenceValue.lastIndexOf("∧"),sentenceValue.lastIndexOf("∨"));
            if(sentenceValue.charAt(index)=='∧'){
                return Operator.And(SingleSatisfy(sentenceValue.substring(0,index)),SingleSatisfy(sentenceValue.substring(index+1)));
            }
            else{
                return Operator.Or(SingleSatisfy(sentenceValue.substring(0,index)),SingleSatisfy(sentenceValue.substring(index+1)));
            }
        }
        else if(sentenceValue.contains("¬")){
            return Operator.Not(SingleSatisfy(sentenceValue.substring(sentenceValue.indexOf('¬')+1)));
        }

        return false;
    }
}
