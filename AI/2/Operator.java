/*
 * Project 2: Automated Reasoning
 * @author Ziyi Kou, Ziqiu Wu
 * @update 2018-10-21
 */

import java.util.HashSet;
import java.util.Set;

public class Operator {
	// set up all operators
    public static Set<String> StringOperator(){
        Set<String> set=new HashSet<String>();
        set.add("¬");	// not
        set.add("∨");	// or
        set.add("∧");	// and
        set.add("⇒");	// implies
        set.add("⇔");	// if and only if

        return set;
    }
    // define and express each operator
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