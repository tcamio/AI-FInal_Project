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
        }
    }
}
