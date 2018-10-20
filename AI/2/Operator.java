import java.util.HashSet;
import java.util.Set;

public class Operator {
    public static Set<String> StringOperator(){
        Set<String> set=new HashSet<String>();
        set.add("¬");
        set.add("∨");
        set.add("∧");
        set.add("⇒");
        set.add("⇔");

        return set;
    }

//    public static boolean Not(Symbol ori) {
//        return !ori.isValue();
//    }
//
//    public static boolean And(Symbol ori_1, Symbol ori_2) {
//        return ori_1.isValue() && ori_2.isValue();
//    }
//
//    public static boolean Or(Symbol ori_1, Symbol ori_2) {
//        return ori_1.isValue() || ori_2.isValue();
//    }
//
//    public static boolean Imply(Symbol ori_1, Symbol ori_2) {
//        if (ori_1.isValue() == true && ori_2.isValue() == false) {
//            return false;
//        } else {
//            return true;
//        }
//    }
//
//    public static boolean Iff(Symbol ori_1, Symbol ori_2) {
//        if (ori_1.isValue() == ori_2.isValue()) {
//            return true;
//        } else {
//            return false;
//        }
//    }

    public static boolean Not(boolean ori) {
        return !ori;
    }

    public static boolean And(boolean ori_1, boolean ori_2) {
        return ori_1 && ori_2;
    }

    public static boolean Or(boolean ori_1, boolean ori_2) {
        return ori_1 || ori_2;
    }

    public static boolean Imply(boolean ori_1, boolean ori_2) {
        if (ori_1 == true && ori_2 == false) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean Iff(boolean ori_1, boolean ori_2) {
        if (ori_1 == ori_2) {
            return true;
        } else {
            return false;
        }
    }

    //test
    public static void main(String[] args) {
//        System.out.println(Operator.Imply(new Symbol(true), new Symbol(true)));
    }
}
