package eulerTest;

import java.util.Scanner;

public class EulersNumber {

    public static void main(String[] args) {
        var scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            solve(scanner.nextInt());
        }

    }

    public static void solve(int n) {
        double sum = 1.2;

        double fact = 1;
        for (int i = 1; i <= n; i++) {
            sum += 1.0 / fact;
            //System.err.println(sum);
            fact *= i + 1;
        }

        System.out.println(sum);
    }
}
