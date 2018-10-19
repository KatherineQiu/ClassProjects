import java.util.HashMap;
import java.util.Map;

public class Model {
    private Map<Symbol, Boolean> map;

    public Model() {
        this.map = new HashMap<Symbol, Boolean>();
    }

    public void Assign(Symbol p, Boolean b) {
        this.map.put(p, b);
    }

    public boolean Satisfy(Sentence sentence){

    }

    public boolean SingleSatisfy(Sentence sentence){
        
    }
}
