package atfs;

import java.util.LinkedHashSet;
import java.util.Set;

public class Atfs {

    private static final String EPSILON = "\u03B5";   // Epsilon für leere Zeichenkette
    private static final String EMPTY_SET = "\u2205"; // Symbol für leere Menge

    public static Set<String> generateRegex(int maxDepth, String[] alphabet, int maxCount) {
        Set<String> results = new LinkedHashSet<>();
        if (maxDepth == 0) {
            addBasicExpressions(results, alphabet);
            return results;
        }

        Set<String> previousLayer = generateRegex(maxDepth - 1, alphabet, maxCount);
        results.addAll(previousLayer);
        
        // Kombiniert Regular Expressions der vorherigen Ebene
        for (String r1 : previousLayer) {
            for (String r2 : previousLayer) {
                if (results.size() >= maxCount) return results;

                
                results.add(concatenate(r1, r2));
               // if (!r1.equals(r2)) { //Alternieren nur wenn ungleiche Zeichen weil a+a = a
                results.add(alternate(r1, r2));
               // }
                results.add(applyKleeneStar(r1));
                
            }
        }
        return results;
    }

    // Fügt die Basisausdrücke (epsilon und leere Menge) und Alphabet Symbole hinzu
    private static void addBasicExpressions(Set<String> results, String[] alphabet) {
    	
        results.add(EMPTY_SET);
        results.add(EPSILON);
    	
    	for (String symbol : alphabet) {
            results.add(symbol);
        }
        
    }

    private static String concatenate(String r1, String r2) {
        String left = needsParenthesesForConcat(r1) ? "(" + r1 + ")" : r1;
        String right = needsParenthesesForConcat(r2) ? "(" + r2 + ")" : r2;
        return left + "⋅" + right;
    }

    private static String alternate(String r1, String r2) {
        return (needsParenthesesForAlt(r1) ? "(" + r1 + ")" : r1)
        		+ "+" + 
        		(needsParenthesesForAlt(r2) ? "(" + r2 + ")" : r2);
    }

    // Wendet den Kleene-Stern an wenn der Ausdruck noch keinen hat
    private static String applyKleeneStar(String regex) {
    	if(regex.endsWith("*")||regex.length() > 2) {
    		return "("+regex+")*";
    	}else {
    		return regex + "*";
    	}
    }

    // Bestimmt, ob Klammern für die Konkatenation notwendig sind
    private static boolean needsParenthesesForConcat(String regex) {
        return regex.contains("+") || (regex.contains("⋅") && !isSimpleConcat(regex));
    }

    // Wir brauchen nur Klammern wenn ein Term bereits eine Alternierung hat
    private static boolean needsParenthesesForAlt(String regex) {
        return regex.contains("+");
    }

    // Überprüft, ob es sich um eine einfache Konkatenation handelt (nur ein Punktzeichen)
    private static boolean isSimpleConcat(String regex) {
        int dotCount = 0;
        for (char c : regex.toCharArray()) {
            if (c == '⋅') dotCount++;
            if (dotCount > 1) return false;
        }
        return dotCount == 1;
    }

    public static void main(String[] args) {
        String[] alphabet = {"a", "b"};
        int maxDepth = 2;
        int maxCount = 10000;
        Set<String> regex = generateRegex(maxDepth, alphabet, maxCount);

        regex.forEach(System.out::println);
        
        System.out.println("Der Index der Inversen zu den ersten 7 ist: "indexOfSet(regex, "(a+b)*"));
        
        
    }

    public static int indexOfSet(Set<String> set, String target) {
        int index = 0;
        for (String item : set) {
            if (item.equals(target)) {
                return index;
            }
            index++;
        }
        return -1; // Gibt -1 zurück, wenn das Element nicht im Set gefunden wurde
    }

    
    
}
