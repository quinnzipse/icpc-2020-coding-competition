package substringcharacters;

import java.util.*;

public class SubChars {
    private final ArrayDeque<Character> substring = new ArrayDeque<>();
    private final HashSet<String> unique_substrings = new HashSet<>();
    private final HashSet<Character> substring_generalized_period = new HashSet<>();
    private final HashSet<Character> generalized_period = new HashSet<>();
    private final String input;

    public static void main(String[] args) {
        var scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            var test_case = new SubChars(scanner.nextLine());
            test_case.solve();
        }

    }

    public SubChars(String input) {
        this.input = input;

        for (int i = 0; i < input.length(); i++) {
            generalized_period.add(input.charAt(i));
        }
    }

    public void solve() {

        if(input.length() == 0) {
            System.out.println("0");
            return;
        }

        for (int i = 0; i < input.length(); i++) {
            char current_char = input.charAt(i);
            substring.add(current_char);
            substring_generalized_period.add(current_char);

            if (substring.size() > 1 && substring.getFirst() == current_char) {
                substring.removeFirst();
            }

            if (isProperMinimalUniqueSubstring()) {
                unique_substrings.add(substring.toString());

                char removed = substring.removeFirst();
                substring_generalized_period.remove(removed);

                while(firstCharIsIllegal()) {
                    substring.removeFirst();
                }
            }
        }

        System.out.println(unique_substrings.size());
    }

    private boolean firstCharIsIllegal(){
        if(substring.size() <= 1) return false;

        var first = substring.getFirst();
        var sub_comp = substring.clone();
        sub_comp.removeFirst();
        return sub_comp.contains(first);
    }

    private boolean isProperMinimalUniqueSubstring() {
        return generalized_period.equals(substring_generalized_period) && substring.size() != input.length();
    }
}
