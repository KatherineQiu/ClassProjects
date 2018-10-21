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

        Clause resolvents=null;
        Set<String> stringClauseSet=null;
        Set<String> stringNewSet=null;
        int min_length=0;
        while (true){
//            System.out.println("kb size:"+clauseList.size()+"new size:"+newClause.size());
            for(int i=0;i<clauseList.size();i++){
                for(int j=i+1;j<clauseList.size();j++){
//                    Clause x1=clauseList.get(i);
//                    Clause x2=clauseList.get(j);
                    resolvents=clauseList.get(i).intersect(clauseList.get(j));
                    if(resolvents==null){
                        continue;
                    }
//                    System.out.println(x1.getValue()+" + "+x2.getValue()+" -> "+resolvents.getValue());
                    if(resolvents.getValue().equals("")){
                        return true;
                    }
                    newClause.add(resolvents);
                }
            }

            stringClauseSet=new HashSet<String>();
            for(Clause clause:clauseList){
                stringClauseSet.add(clause.getValue());
            }
            stringNewSet=new HashSet<String>();
            for(Clause clause:newClause){
                stringNewSet.add(clause.getValue());
            }

            if(stringClauseSet.containsAll(stringNewSet)){
                return false;
            }

//            int maxLength=0;
//            for(String s:stringClauseSet){
//                if(s.split("∨").length>maxLength){
//                    maxLength=s.split("∨").length;
//                }
//            }
//            for(String s:stringNewSet){
//                if(s.split("∨").length<=maxLength){
//                    stringClauseSet.add(s);
//                }
//            }
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
        Set<Clause> kb2 = new HashSet<Clause>();
        kb2.add(new Clause("¬A"));
        kb2.add(new Clause("¬B∨C∨D"));
        kb2.add(new Clause("¬C∨B"));
        kb2.add(new Clause("¬D∨B"));
        kb2.add(new Clause("¬E∨A∨F∨G"));
        kb2.add(new Clause("E∨¬A"));
        kb2.add(new Clause("E∨¬F"));
        kb2.add(new Clause("E∨¬G"));
        kb2.add(new Clause("¬B"));
        kb2.add(new Clause("E"));

        System.out.println("Q:C------"+PL_Resolution(kb2, new Clause("¬C")));

        System.out.println("---------------------3-----------------------");
        Set<Clause> kb3 = new HashSet<Clause>();
        kb3.add(new Clause("¬P∨Q"));
        kb3.add(new Clause("P∨¬Q"));
        kb3.add(new Clause("P∨R"));
        kb3.add(new Clause("¬Q∨S"));
        kb3.add(new Clause("¬R∨S"));
        kb3.add(new Clause("¬S∨T"));

        System.out.println("Q:P------"+PL_Resolution(kb3, new Clause("¬P")));
        System.out.println("Q:T------"+PL_Resolution(kb3, new Clause("¬T")));
        System.out.println("Q:S------"+PL_Resolution(kb3, new Clause("¬S")));

        System.out.println("---------------------4-----------------------");
        Set<Clause> kb4_1 = new HashSet<Clause>();
        kb4_1.add(new Clause("¬A∨C"));
        kb4_1.add(new Clause("¬B∨¬C"));
        kb4_1.add(new Clause("B∨C"));
        kb4_1.add(new Clause("¬C∨B∨¬A"));
        kb4_1.add(new Clause("¬B∨C"));
        kb4_1.add(new Clause("A∨C"));

        Set<Clause> kb4_2 = new HashSet<Clause>();
        kb4_2.add(new Clause("¬A∨¬C"));
        kb4_2.add(new Clause("C∨A"));
        kb4_2.add(new Clause("¬B∨A"));
        kb4_2.add(new Clause("¬B∨C"));
        kb4_2.add(new Clause("¬C∨B"));

        System.out.println("---------------------(a)-----------------------");
        System.out.println("Q:A------"+PL_Resolution(kb4_1, new Clause("¬A")));
        System.out.println("Q:B------"+PL_Resolution(kb4_1, new Clause("¬B")));
        System.out.println("Q:C------"+PL_Resolution(kb4_1, new Clause("¬C")));

        System.out.println("---------------------(b)-----------------------");
        System.out.println("Q:A------"+PL_Resolution(kb4_2, new Clause("¬A")));
        System.out.println("Q:B------"+PL_Resolution(kb4_2, new Clause("¬B")));
        System.out.println("Q:C------"+PL_Resolution(kb4_2, new Clause("¬C")));

        System.out.println("---------------------5-----------------------");
        Set<Clause> kb5 = new HashSet<Clause>();
        kb5.add(new Clause("¬A∨H"));
        kb5.add(new Clause("¬A∨I"));
        kb5.add(new Clause("¬H∨¬I∨A"));
        kb5.add(new Clause("¬B∨A"));
        kb5.add(new Clause("¬B∨L"));
        kb5.add(new Clause("¬A∨¬L∨B"));
        kb5.add(new Clause("¬C∨B"));
        kb5.add(new Clause("¬C∨G"));
        kb5.add(new Clause("¬B∨¬G∨C"));
        kb5.add(new Clause("¬D∨E"));
        kb5.add(new Clause("¬D∨L"));
        kb5.add(new Clause("¬E∨¬L∨D"));
        kb5.add(new Clause("¬E∨C"));
        kb5.add(new Clause("¬E∨H"));
        kb5.add(new Clause("¬C∨¬H∨E"));
        kb5.add(new Clause("¬F∨D"));
        kb5.add(new Clause("¬F∨I"));
        kb5.add(new Clause("¬D∨¬I∨F"));
        kb5.add(new Clause("¬G∨¬E"));
        kb5.add(new Clause("¬G∨¬J"));
        kb5.add(new Clause("E∨J∨G"));
        kb5.add(new Clause("¬H∨¬F"));
        kb5.add(new Clause("¬H∨¬K"));
        kb5.add(new Clause("F∨K∨H"));
        kb5.add(new Clause("¬I∨¬G"));
        kb5.add(new Clause("¬I∨¬K"));
        kb5.add(new Clause("G∨K∨I"));
        kb5.add(new Clause("¬J∨¬A"));
        kb5.add(new Clause("¬J∨¬C"));
        kb5.add(new Clause("A∨C∨J"));
        kb5.add(new Clause("¬K∨¬D"));
        kb5.add(new Clause("¬K∨¬F"));
        kb5.add(new Clause("D∨F∨K"));
        kb5.add(new Clause("¬L∨¬B"));
        kb5.add(new Clause("¬L∨¬J"));
        kb5.add(new Clause("B∨J∨L"));

        for(int i=65;i<=76;i++){
            System.out.println("Q:"+String.valueOf((char)i)+"------"+PL_Resolution(kb5, new Clause("¬"+String.valueOf((char)i))));
        }

        System.out.println("---------------------6-----------------------");
        Set<Clause> kb6_1 = new HashSet<Clause>();
        kb6_1.add(new Clause("X∨Y∨Z∨W"));
        kb6_1.add(new Clause("¬A∨X"));
        kb6_1.add(new Clause("¬X∨A"));
        kb6_1.add(new Clause("¬B∨Y∨Z"));
        kb6_1.add(new Clause("¬Y∨B"));
        kb6_1.add(new Clause("¬Z∨B"));
        kb6_1.add(new Clause("¬C∨A"));
        kb6_1.add(new Clause("¬C∨B"));
        kb6_1.add(new Clause("¬A∨¬B∨C"));
        kb6_1.add(new Clause("¬D∨X"));
        kb6_1.add(new Clause("¬D∨Y"));
        kb6_1.add(new Clause("¬X∨¬Y∨D"));
        kb6_1.add(new Clause("¬E∨X"));
        kb6_1.add(new Clause("¬E∨Z"));
        kb6_1.add(new Clause("¬X∨¬Z∨¬E"));
        kb6_1.add(new Clause("¬F∨D∨E"));
        kb6_1.add(new Clause("¬F∨¬D∨¬E"));
        kb6_1.add(new Clause("¬D∨E∨F"));
        kb6_1.add(new Clause("D∨¬E∨F"));
        kb6_1.add(new Clause("¬G∨¬C∨F"));
        kb6_1.add(new Clause("C∨G"));
        kb6_1.add(new Clause("¬F∨G"));
        kb6_1.add(new Clause("¬H∨¬G∨A"));
        kb6_1.add(new Clause("G∨H"));
        kb6_1.add(new Clause("H"));
        kb6_1.add(new Clause("¬A∨H"));


        Set<Clause> kb6_2 = new HashSet<Clause>();
        kb6_2.add(new Clause("X∨Y∨Z∨W"));
        kb6_2.add(new Clause("¬A∨X"));
        kb6_2.add(new Clause("¬X∨A"));
        kb6_2.add(new Clause("¬C∨A"));
        kb6_2.add(new Clause("G∨C"));
        kb6_2.add(new Clause("¬C∨¬B∨A"));
        kb6_2.add(new Clause("¬H∨¬G∨A"));
        kb6_2.add(new Clause("G∨H"));
        kb6_2.add(new Clause("H"));
        kb6_2.add(new Clause("¬A∨H"));

        System.out.println("---------------------(a)-----------------------");
        System.out.println("Q:X------"+PL_Resolution(kb6_1, new Clause("¬X")));
        System.out.println("Q:Y------"+PL_Resolution(kb6_1, new Clause("¬Y")));
        System.out.println("Q:Z------"+PL_Resolution(kb6_1, new Clause("¬Z")));
        System.out.println("Q:W------"+PL_Resolution(kb6_1, new Clause("¬W")));

        System.out.println("---------------------(b)-----------------------");
        System.out.println("Q:X------"+PL_Resolution(kb6_2, new Clause("¬X")));
        System.out.println("Q:Y------"+PL_Resolution(kb6_2, new Clause("¬Y")));
        System.out.println("Q:Z------"+PL_Resolution(kb6_2, new Clause("¬Z")));
        System.out.println("Q:W------"+PL_Resolution(kb6_2, new Clause("¬W")));

    }
}
