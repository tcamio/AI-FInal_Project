import java.util.*;

public class Main {
    public static void main(String[] args) {

    }
    
    public ArrayList<String> getSymbols(Sentence s) {
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

    public Boolean PL_True(Sentence s, HashMap<String, Boolean> model) {
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

    public Boolean TT_Entails(Sentence KB, Sentence alpha) {
        ArrayList<String> KBSymbols = getSymbols(KB);
        ArrayList<String> alphaSymbols = getSymbols(alpha);

        // Copy the knowledge base list
        ArrayList<String> allSymbols = new ArrayList<>(KBSymbols);

        // Add alpha symbol list
        allSymbols.addAll(alphaSymbols);

        //
        HashMap<String, Boolean> model = new HashMap<>();

        return TT_Check_All(KB, alpha, allSymbols, model);
    }

    public Boolean TT_Check_All(Sentence KB, Sentence alpha, ArrayList<String> symbols, HashMap<String, Boolean> model) {
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
