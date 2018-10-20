import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Advance {
    public static boolean PL_Resolution(Set<Clause> kb, Clause alpha){
        Set<Clause> clauses=new HashSet<Clause>();
        for(Clause clause:kb){
            clauses.add(clause);
        }
        clauses.add(alpha);

        List<Clause> clauseList=new LinkedList<Clause>(clauses);

        Set<Clause> newClause=new HashSet<Clause>();
        while (true){
            for(int i=0;i<clauseList.size();i++){
                for(int j=i+1;j<clauseList.size();j++){
                    Clause resolvents=clauseList.get(i).intersect(clauseList.get(j));
                    if(resolvents.getValue().equals("")){
                        return true;
                    }
                    newClause.add(resolvents);
                }
            }

            Set<String> stringClauseSet=new HashSet<String>();
            for(Clause clause:clauseList){
                stringClauseSet.add(clause.getValue());
            }
            Set<String> stringNewSet=new HashSet<String>();
            for(Clause clause:newClause){
                stringNewSet.add(clause.getValue());
            }

            if(stringClauseSet.contains(stringNewSet)){
                return false;
            }

            stringClauseSet.addAll(stringNewSet);
            clauseList=new LinkedList<Clause>();
            for(String s:stringClauseSet){
                clauseList.add(new Clause(s));
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("---------------------1-----------------------");
        Set<Clause> kb = new HashSet<Clause>();
        kb.add(new Clause("P"));
        kb.add(new Clause("¬P∨Q"));


        System.out.println("Q:Q------"+PL_Resolution(kb,new Clause("¬Q")));

        System.out.println("---------------------2-----------------------");

    }
}
