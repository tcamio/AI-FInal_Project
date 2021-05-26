// Class to create a sentence
class Sentence {
    String symbol;
    String connective;
    Sentence[] children;

    Sentence(String s, String c, int num) {
        this.symbol = s;
        this.connective = c;
        children = new Sentence[num];
    }

}