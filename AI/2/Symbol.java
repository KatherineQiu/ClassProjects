public class Symbol {
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
