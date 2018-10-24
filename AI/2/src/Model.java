/*
 * Project 2: Automated Reasoning
 * @author Ziyi Kou, Ziqiu Wu
 * @update 2018-10-21
 */

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Model{
    private Map<String, Boolean> map;

    public Model() {
        this.map = new HashMap<String, Boolean>();
    }

    public Model(Map<String, Boolean> map) {
        this.map = map;
    }

    public Map<String, Boolean> getMap() {
        return map;
    }

    public void setMap(Map<String, Boolean> map) {
        this.map = map;
    }

    public Model assign(Symbol p, Boolean b) {
    	// assign true or false to a specific symbol
        this.map.put(p.getName(), b);
        return this;
    }

    public Model clone(){
    	// make a clone version of this model
        Model newModel=new Model();
        Map<String, Boolean> map=new HashMap<String, Boolean>();
        for(String s:this.getMap().keySet()){
            map.put(s,this.getMap().get(s));
        }
        newModel.setMap(map);

        return newModel;
    }


    public boolean satisfyKB(Set<Sentence> kb){
    	// check whether all sentences in knowledge base are satisfied
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
    	// check whether a sentence holds within a model
        String sentenceValue=sentence.getValue();
        return singleSatisfy(sentenceValue);
    }

    public boolean singleSatisfy(String sentenceValue){
    	// get value directly if there is only one symbol
        if(sentenceValue.length()==1){
            return this.map.get(sentenceValue);
        }
        //if there exist a "(" at the front and a ")" at the end, check if they are of a pair so that we can remove them without changing the meaning of sentence
        while(sentenceValue.charAt(0)=='(' && sentenceValue.charAt(sentenceValue.length()-1)==')'){
            String newSen="";
            for(int i=1;i<sentenceValue.length()-1;i++){
                newSen+=String.valueOf(sentenceValue.charAt(i));
            }

            int left=0;
            for(int i=0;i<newSen.length();i++){
                char c=newSen.charAt(i);
                if(c=='('){
                    left++;
                }
                if(c==')'){
                    left--;
                    if(left<0){
                        break;
                    }
                }
            }

            if(left>=0){
                sentenceValue=newSen;
            }
            else{
                break;
            }
        }
        // if there exist "()" among the sentence, take it as an integrated part and operate the operations outside of "()", following the priority
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
                        map.put(i+1,String.valueOf(sentenceValue.charAt(i+1)));
                    }
                }
            }

            if(map.values().contains("⇔")){
                int max=0;
                for(Integer i:map.keySet()){
                    if(map.get(i).equals("⇔")){
                        if(i>max){
                            max=i;
                        }
                    }
                }
                String firstSentence=sentenceValue.substring(0,max);
                String secondSentence=sentenceValue.substring(max+1);
                return Operator.Iff(singleSatisfy(firstSentence),singleSatisfy(secondSentence));
            }
            else if(map.values().contains("⇒")){
                String[] arr=sentenceValue.split("⇒");
                String firstSentence=arr[0];
                String secondSentence=arr[1];
                return Operator.Imply(singleSatisfy(firstSentence),singleSatisfy(secondSentence));
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
                    return Operator.Or(singleSatisfy(sentenceValue.substring(0,max)),singleSatisfy(sentenceValue.substring(max+1)));
                }
                else{
                    return Operator.And(singleSatisfy(sentenceValue.substring(0,max)),singleSatisfy(sentenceValue.substring(max+1)));
                }
            }
            else if(map.values().contains("¬")){
                return Operator.Not(singleSatisfy(sentenceValue.substring(1)));
            }
        }
        
        // if two parts are connected by "⇔", do IFF operation
        else if(sentenceValue.contains("⇔")){
            String[] arr=sentenceValue.split("⇔");
            String firstSentence=arr[0];
            String secondSentence=arr[1];
            return Operator.Iff(singleSatisfy(firstSentence),singleSatisfy(secondSentence));
        }
        // if two parts are connected by "⇒", do Imply operation
        else if(sentenceValue.contains("⇒")){
            String[] arr=sentenceValue.split("⇒");
            String firstSentence=arr[0];
            String secondSentence=arr[1];
            return Operator.Imply(singleSatisfy(firstSentence),singleSatisfy(secondSentence));
        }
        // if a sentence contains "∧" or "∨", do operations following the rule left to right
        // So we split the two parts at the last operator 
        else if(sentenceValue.contains("∧") || sentenceValue.contains("∨")){
            int index=Math.max(sentenceValue.lastIndexOf("∧"),sentenceValue.lastIndexOf("∨"));
            if(sentenceValue.charAt(index)=='∧'){
                return Operator.And(singleSatisfy(sentenceValue.substring(0,index)),singleSatisfy(sentenceValue.substring(index+1)));
            }
            else{
                return Operator.Or(singleSatisfy(sentenceValue.substring(0,index)),singleSatisfy(sentenceValue.substring(index+1)));
            }
        }
        // if two parts are connected by "¬", do Not operation
        else if(sentenceValue.contains("¬")){
            return Operator.Not(singleSatisfy(sentenceValue.substring(sentenceValue.indexOf('¬')+1)));
        }

        return false;
    }
}