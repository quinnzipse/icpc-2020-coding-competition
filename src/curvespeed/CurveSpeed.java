package curvespeed;

import java.util.Scanner;

public class CurveSpeed {

    public static void main(String[] args) {
        var scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            solve(scanner.nextInt(), scanner.nextDouble());
        }

    }

    public static void solve(int radius, double superElevation) {
        System.out.println((int)Math.round(Math.sqrt((radius * (superElevation + .16))/.067)));
    }
}
