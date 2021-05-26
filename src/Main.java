import java.util.*;

public class Main {
    public static void main(String[] args) {

        // Create knowledge base and models
        Sentence KB = createKB();
        Sentence alpha1 = createAlpha1();
        Sentence alpha2 = createAlpha2();
        Sentence alpha3 = createAlpha3();
        Sentence alpha4 = createAlpha4();

        System.out.println("The Knowledge base is: P => Q");
        System.out.println("The model alpha1 is: (not P) or Q");
        System.out.println("The model alphs2 is: P and (not Q)");
        System.out.println("The model alpha3 is: P and Q");
        System.out.println("The model alphs4 is: P => Q");
        System.out.println();

        System.out.println("The KB entails alpha1: " + TT_Entails(KB, alpha1));
        System.out.println("The KB entails alpha2: " + TT_Entails(KB, alpha2));
        System.out.println("The KB entails alpha3: " + TT_Entails(KB, alpha3));
        System.out.println("The KB entails alpha4: " + TT_Entails(KB, alpha4));
    }
    
    // Create knowledge base P=>Q
    public static Sentence createKB() {
        Sentence KB = new Sentence(null, "if", 2);
        Sentence P = new Sentence("P", null, 0);
        Sentence Q = new Sentence("Q", null, 0);

        KB.children[0] = P;
        KB.children[1] = Q;

        return KB;
    }

    // Create model alpha1 (not P) or Q
    public static Sentence createAlpha1() {
        Sentence alpha1 = new Sentence(null, "or", 2);
        Sentence not = new Sentence(null, "not", 1);
        Sentence P = new Sentence("P", null, 0);
        Sentence Q = new Sentence("Q", null, 0);

        alpha1.children[0] = not;
        alpha1.children[1] = Q;
        not.children[0] = P;

        return alpha1;
    }

    // Create model alpha2 P and (not Q)
    public static Sentence createAlpha2() {
        Sentence alpha2 = new Sentence(null, "and", 2);
        Sentence P = new Sentence("P", null, 0);
        Sentence not = new Sentence(null, "not", 1);
        Sentence Q = new Sentence("Q", null, 0);

        alpha2.children[0] = P;
        alpha2.children[1] = not;
        not.children[0] = Q;

        return alpha2;
    }

    // Create model alpha3 P and Q
    public static Sentence createAlpha3() {
        Sentence alpha3 = new Sentence(null, "and", 2);
        Sentence P = new Sentence("P", null, 0);
        Sentence Q = new Sentence("Q", null, 0);

        alpha3.children[0] = P;
        alpha3.children[1] = Q;

        return alpha3;
    }

    // Create model alpha4 P => P
    public static Sentence createAlpha4() {
        Sentence alpha3 = new Sentence(null, "if", 2);
        Sentence P1 = new Sentence("P", null, 0);
        Sentence P2 = new Sentence("P", null, 0);

        alpha3.children[0] = P1;
        alpha3.children[1] = P2;

        return alpha3;
    }

    // Return array list of symbols from sentence class
    public static ArrayList<String> getSymbols(Sentence s) {
        ArrayList<String> symbolList = new ArrayList<>();

        if (s.symbol != null) {
            symbolList.add(s.symbol);
        } else {
            for (Sentence child: s.children) {
                symbolList.addAll(getSymbols(child));
            }
        }

        return symbolList;
    }

    // Return true if a sentence holds within a model
    public static Boolean PL_True(Sentence s, HashMap<String, Boolean> model) {
        if (s.symbol != null) {
            return model.get(s.symbol);
        } else if (s.connective == "and") {
            for (Sentence child: s.children) {
                if (PL_True(child, model) == false) {
                    return false;
                }
            }
            return true;
        } else if (s.connective == "or") {
            for (Sentence child: s.children) {
                if (PL_True(child, model) == true) {
                    return true;
                }
            }
            return false;
        } else if (s.connective == "if") {
            Sentence left = s.children[0];
            Sentence right = s.children[1];
            if (PL_True(left, model) == true && PL_True(right, model) == false) {
                return false;
            }
            return true;
        } else if (s.connective == "iff") {
            Sentence left = s.children[0];
            Sentence right = s.children[1];
            if (PL_True(left, model) == PL_True(right, model)) {
                return true;
            } else {
                return false;
            }
        } else if (s.connective == "not") {
            Sentence child = s.children[0];
            return !PL_True(child, model);
        } else {
            System.out.println("The connective is wrong: " + s.connective);
            return false;
        }
    }

    // Return true if knowledge base entail model
    public static Boolean TT_Entails(Sentence KB, Sentence alpha) {
        ArrayList<String> KBSymbols = getSymbols(KB);
        ArrayList<String> alphaSymbols = getSymbols(alpha);

        // Copy the knowledge base list
        ArrayList<String> allSymbols = new ArrayList<>(KBSymbols);

        // Add alpha symbol list
        allSymbols.addAll(alphaSymbols);

        // Create empty map
        HashMap<String, Boolean> model = new HashMap<>();

        return TT_Check_All(KB, alpha, allSymbols, model);
    }

    // Return if knowledge base entails model
    public static Boolean TT_Check_All(Sentence KB, Sentence alpha, ArrayList<String> symbols, HashMap<String, Boolean> model) {
        if (symbols.isEmpty()) {
            if (PL_True(KB, model)) {
                return PL_True(alpha, model);
            } else {
                return true; // When KB is false, always return true
            }
        } else {
            String P = symbols.get(0);
            ArrayList<String> rest = new ArrayList<>(symbols);
            rest.remove(0);

            HashMap<String, Boolean> m1 = new HashMap<>(model);
            HashMap<String, Boolean> m2 = new HashMap<>(model);
            m1.put(P, true);
            m2.put(P, false);

            return TT_Check_All(KB, alpha, rest, m1) && TT_Check_All(KB, alpha, rest, m2);
        }
    }
}
