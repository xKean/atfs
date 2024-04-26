package atfs;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Atfs {
    private static final String EMPTY_SET = "\u2205";
    private static final String EPSILON = "\u03B5";
    private static final String[] ALPHABET = {"a", "b"};  // Beispielalphabet

    public static void main(String[] args) {
        int maxExpressions = 100;  // Maximale Anzahl an Ausdrücken, die generiert werden sollen
        Set<String> generatedExpressions = generateRegExps(maxExpressions);
        for (String expr : generatedExpressions) {
            System.out.println(expr);
        }
    }

    public static Set<String> generateRegExps(int maxExpressions) {
        Set<String> expressions = new LinkedHashSet<>();
        expressions.add(EMPTY_SET);
        expressions.add(EPSILON);
        
        // Hinzufügen von einfachen Zeichen aus dem Alphabet
        for (String symbol : ALPHABET) {
            expressions.add(symbol);
        }
        
        // Generiere neue Ausdrücke bis zur gewünschten Anzahl
        List<String> current = new LinkedList<>(expressions);
        while (expressions.size() < maxExpressions) {
            List<String> nextGeneration = new LinkedList<>();
            
            for (String exp1 : current) {
                for (String exp2 : current) {
                    // Konkatenation
                    String concat = "(" + exp1 + exp2 + ")";
                    // Vereinigung
                    String union = "(" + exp1 + "+" + exp2 + ")";
                    // Kleene-Stern für beide Ausdrücke
                    String star1 = "(" + exp1 + ")*";
                    String star2 = "(" + exp2 + ")*";
                    
                    // Hinzufügen, wenn nicht bereits vorhanden und Limit nicht erreicht
                    addIfNotPresent(expressions, nextGeneration, concat, maxExpressions);
                    addIfNotPresent(expressions, nextGeneration, union, maxExpressions);
                    addIfNotPresent(expressions, nextGeneration, star1, maxExpressions);
                    addIfNotPresent(expressions, nextGeneration, star2, maxExpressions);
                    
                    if (expressions.size() >= maxExpressions) break;
                }
                if (expressions.size() >= maxExpressions) break;
            }
            if (nextGeneration.isEmpty()) break;
            current = nextGeneration;
        }
        
        return expressions;
    }

    private static void addIfNotPresent(Set<String> expressions, List<String> nextGeneration, String newExpr, int maxExpressions) {
        if (expressions.size() < maxExpressions && expressions.add(newExpr)) {
            nextGeneration.add(newExpr);
        }
    }
}
