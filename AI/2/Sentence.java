/*
 * Project 2: Automated Reasoning
 * @author Ziyi Kou, Ziqiu Wu
 * @update 2018-10-21
 */

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Sentence {
    private String value;

    public Sentence(String value) {
        this.value = preProcess(value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Set<String> getSymbols(){
        // extract all symbols from a sentence, with their name
        Set<String> letters=new HashSet<String>();

        for(int i=0;i<this.value.length();i++){
            char letter=this.value.charAt(i);
            if(letter>=65 && letter<=90){
                letters.add(String.valueOf(letter));
            }
        }

        return letters;
    }

    private String preProcess(String value){
    	// remove all the spaces in the string
        value=value.replaceAll(" ","");
        return value;
    }

    //test
    public static void main(String[] args){
        Sentence sentence=new Sentence("{P,P ⇒ Q}");
        Set<String> symbols=sentence.getSymbols();
        System.out.println();
    }
}