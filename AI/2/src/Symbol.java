/*
 * Project 2: Automated Reasoning
 * @author Ziyi Kou, Ziqiu Wu
 * @update 2018-10-21
 */

public class Symbol {
	// a class for symbols such as P or Q, which have properties of value and name
    private boolean value;
    private String name;

    public Symbol() {

    }

    public Symbol(boolean value) {
        this.value = value;
    }

    public Symbol(String name) {
        this.name = name;
    }

    public Symbol(boolean value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    //test
    public static void main(String[] args){
        Symbol test=new Symbol();
        System.out.println();
    }
}